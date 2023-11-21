package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Attendance_Data;
import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;


/**
 * Created by Codmob on 12-10-16.
 */
public class GateEntryActivity extends Activity {

    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;

    public static final int WRITE_IN_OUT = 13;
    public static final int WRITE_ATTND_TYPE = 14;

    public static final int READ_CURR_BAL = 8;
    public static final int READ_IN_OUT = 13;
    public static final int READ_ATTND_TYPE = 14;

    public static final int READ_SCHOOL_ID = 4;
    public static final int READ_CARD_SERIAL = 5;
    public static final int READ_STATUS = 6;
    public static final int READ_EXPIRY = 12;



    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;

    char[] key1 = { 0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e };
    char[] key2 = { 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
    char[] key3 = { 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
    char[] key4 = { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
    char[] key5 = { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
    char[] key6 = { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };

    StringBuffer read_serial = new StringBuffer();
    StringBuffer read_current = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
    StringBuffer read_inout = new StringBuffer();
    StringBuffer read_type = new StringBuffer();
    StringBuffer read_status = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();

    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();

    //    
    char[] write_data = {  '0', '0', '0', '0','4', '1', '2', '6', '7', '2', '2', 'a', '2', '3',
            '3', 'e' };



    Context context;

//    static {
//
//        System.loadLibrary("gl11rfid");
//        System.out.println("Library Loaded");
//
//        rfidService = IRfidService.Stub.asInterface(ServiceManager
//                .getService("rfid"));
//        System.out.println("CHILLR: "+rfidService);
//    }

    public Button tap_the_card;

    
    int ret_poweron;
    int ret_fieldon;

    int tardtc;

    int authsuc1,authsuc2,authsuc3,authsuc4,authsuc5,readauthsuc1,readauthsuc2,readauthsuc3;
    int writesuc1,writesuc2,writesuc3,writesuc4,writesuc5;
    int readsuc0,readsuc1,readsuc2,readsuc3,readsuc4,readsuc5,readsuc6;

    int tama_on;

    //    DatabaseHandler db = new DatabaseHandler(this);
    DatabaseHandler db ;
    int transtype_id;

    String trans_id,machine_id;

//    String attend_typ;
    String inout="0000000000000000";
    String attName;
    int typeattend;

    String resultCardSerial,resultPrevbal;

    String User_name,Pass_word,currentDateTimeString,currentDateTimeString1;
    int userid;
    Float currentbalance;
    int paymentTypeId;
    Float previousbalnce;
    Date expirydate,today1;

    ProgressBar progressBar;


    private final List<String> BlkdCards = new ArrayList<>();

    @Override
    protected void onDestroy() {

//        apis.tama_close();
//
//        try {
//            int ret_fieldoff = rfidService.rfid_rffield_off();
//
//            System.out.println("Field Off:" + ret_fieldoff);
//
//            int ret_poweroff = rfidService.rfid_poweroff();
//
//            System.out.println("PowerOFF :" + ret_poweroff);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

        super.onDestroy();

    }

    //




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        db = DatabaseHandler.getInstance(getApplicationContext());


        context = getApplicationContext();
        tap_the_card = (Button) findViewById(R.id.tap);

        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        machine_id=db.getMachineID();



//        Bundle b=getIntent().getExtras();
//        attend_typ=b.getString("id");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid= editor.getInt("userid",0);

        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");

// Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        currentDateTimeString = df.format(today);

//        currentDateTimeString=currentDateTimeString.replace(" ","%20");

        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

// Get the date today using Calendar object.
        Date today11 = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        currentDateTimeString1 = df1.format(today11);


//        attName=db.getAttTypbyID(attend_typ);


//        transtype_id=db.getTransTypID("ATTENDANCE");

        System.out.println("CHILL DATETIME "+currentDateTimeString);

//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                trans_id=db.lastTransID();
//
////                tap_the_card.setEnabled(false);
////                tap_the_card.setClickable(false);
//
//
//                if (SecondActivity.ret_fieldon == 0) {
//
//                    System.out.println("Tama open :" + tama_on);
//
//                    tardtc = apis.tama_detectTarget(TARGET_NUM,
//                            target_detected, target_id);
//
//                    if (tardtc == 0){
//
//                        try{
//
//                            readauthsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_SECND_SECT, key2, KEYA);
//
//                            if (readauthsuc1 == 0) {
//
//                                readsuc0 = apis.tama_read_target(READ_SCHOOL_ID
//                                        , read_school);
//                                readsuc1 = apis.tama_read_target(
//                                        READ_CARD_SERIAL, read_serial);
//                                readsuc3 = apis.tama_read_target(
//                                        READ_STATUS, read_status);
//
//
//
//                                if (readsuc1 == 0) {
////                                Toast.makeText(context, "Read Success serial "+read_serial,
////                                        Toast.LENGTH_SHORT).show();
////                                alertDlg("FAILURE!");
//                                    System.out.println("read Success");
//
//
//                                } else {
////                                alertDlg("FAILURE!");
//                                    System.out.println("Read Failed");
//                                }
//
//                            } else {
////                            Toast.makeText(context, "Read Authentication Failed serial",
////                                    Toast.LENGTH_SHORT).show();
//
//                                System.out.println("Authentication Failed");
//                            }
//
//
//
//                            String schId= String.valueOf(read_school);
////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
////                                Toast.LENGTH_LONG).show();
//
//                            String serial=String.valueOf(read_serial);
//                            //reading card serial
//                            resultCardSerial=serial;
//                            String cardstat = String.valueOf(read_status);
//
//                            int cardstat1 = Integer.parseInt(cardstat);
//
//                            if (cardstat1 == 0) {
//                                finish();
//                            } else {
//
//
//                                readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                if (readauthsuc3 == 0) {
//                                    readsuc4 = apis.tama_read_target(
//                                            READ_EXPIRY, read_expiry);
//
//                                    if (readsuc4 == 0) {
////                                        Toast.makeText(context, "Read Success Expiry" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("read Success expiry");
//
//                                    } else {
////                                        Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
////                                                .show();
////                                        alertDlg("FAILURE!");
//                                        System.out.println("Read Failed");
//                                    }
//
//                                } else {
////                                    Toast.makeText(context, "Read Authentication Failed expiry",
////                                            Toast.LENGTH_SHORT).show();
////                                    alertDlg("FAILURE!");
//                                    System.out.println("Authentication Failed");
//                                }
//
//                                //reading card expire date
//                                String expdate = String.valueOf(read_expiry);
//                                String expdate1 = expdate.replaceFirst("^0*", "");
//                                System.out.println("CHILLRRRR: " + expdate1);
//
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                                try {
//                                    expirydate = sdf.parse(expdate1);
//                                    today1 = sdf.parse(currentDateTimeString1);
//
//
//                                    System.out.println("CHILLRRRR: " + expirydate);
//                                    System.out.println("CHILLRRRR: today " + today1);
//                                } catch (ParseException ex) {
//                                    System.out.println("Parse exception");
//                                }
//
//
//                                if (BlocardsCheck(serial)) {
//
////                                    finish();
//                                        alertDlg("FAILURE! \n Blocked Card!");
//
//                                } else {
//
//                                    if (schId.replace(" ", "").equalsIgnoreCase(db.getSchoolId())) {
//
////                                        if(today1.after(expirydate)){
////
////                                            System.out.println("ELDHO: card expired");
//////                                            Toast.makeText(getApplicationContext(),"Card already expired. ",Toast.LENGTH_LONG).show();
//////                                            finish();
////                                            alertDlg("FAILURE! \n Expired Card!");
////                                        }else {
//
//                                            alertDlg("SUCCESS!");
//
//
////                                        }
//                                    } else {
////                                        Toast.makeText(context, "Card not belonging to this school!!",
////                                                Toast.LENGTH_SHORT).show();
//
////                                        AlertDialog.Builder alert = new AlertDialog.Builder(
////                                                AttendenceNFC.this);
////                                        alert.setTitle("RFID");
////
////                                        alert.setCancelable(false);
////                                        alert.setMessage("AUTHENTICATION FAILURE!");
////
////                                        android.content.DialogInterface.OnClickListener listener;
////                                        alert.setPositiveButton(
////                                                "OK",
////                                                new DialogInterface.OnClickListener() {
////
////                                                    @Override
////                                                    public void onClick(
////                                                            DialogInterface dialog,
////                                                            int which) {
////
////                                                        dialog.dismiss();
////
////
////                                                    }
////                                                });
////                                        alert.show();
//////                                        finish();
//                                        alertDlg("FAILURE!");
//
//                                    }
//
//
//                                }
//
//                            }
//
//                        }catch(NumberFormatException e){
//                            tap_the_card.setEnabled(false);
//                            tap_the_card.setClickable(false);
//
//
////                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
////                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    GateEntryActivity.this);
//                            alert.setTitle("RFID");
//
//                            alert.setCancelable(false);
//                            alert.setMessage("Target Moved.Try again.");
//
//                            DialogInterface.OnClickListener listener;
//                            alert.setPositiveButton(
//                                    "OK",
//                                    new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(
//                                                DialogInterface dialog,
//                                                int which) {
////
//                                            dialog.dismiss();
//////                                            Intent i=new Intent(getApplicationContext(),SecondActivity.class);
//////                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////                                            startActivity(i);
//////                                            finish();
//                                            new FieldRestartTask().execute();
//
//                                        }
//                                    });
//                            alert.show();
//                            alertDlg("FAILURE!");
//
//                            System.out.println("Target detect Failed");
//                        }catch(NullPointerException e){
//                            Toast.makeText(context, "Card Error!!",
//                                    Toast.LENGTH_LONG).show();
////                            finish();
//                        }
//                    } else {
//
//                        tap_the_card.setEnabled(false);
//                        tap_the_card.setClickable(false);
//                        Toast.makeText(context, "FAILURE!", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                GateEntryActivity.this);
//                        alert.setTitle("RFID");
//
//                        alert.setCancelable(false);
//                        alert.setMessage("FAILURE!");
//
//                        DialogInterface.OnClickListener listener;
//                        alert.setPositiveButton(
//                                "OK",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int which) {
//
//                                        dialog.dismiss();
////                                        Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        startActivity(i);
////                                        finish();
//                                        new FieldRestartTask().execute();
//
//                                    }
//                                });
//                        alert.show();
//
//                        System.out.println("Target detect Failed");
////                        alertDlg("FAILURE!");
//                    }
//
//                } else {
//                    Toast.makeText(context, "Card open Failed", Toast.LENGTH_SHORT).show();
//                    System.out.println("Card open Field");
//                }
//
//            }
//        });



    }



    private void alertDlg(String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(
                GateEntryActivity.this);
        alert.setTitle("RFID");

        alert.setCancelable(false);
        alert.setMessage(msg);

        DialogInterface.OnClickListener listener;
        alert.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {


                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), GateEntryActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();




                    }
                });
        alert.show();

    }

    private Boolean BlocardsCheck(String card) {


        List<Blocked_Cards_Info> blocked_cards_infos = db.getAllBlockCardInfo();
        for (Blocked_Cards_Info cn : blocked_cards_infos) {
            String log = "Id: " + cn.getBlocked_cards_id() + " ,Name: " + cn.getCard_serial() ;

            String cardSerial=cn.getCard_serial();
            BlkdCards.add(cardSerial);

            System.out.println("CHILLAR Blocked: "+log);
        }


        for(int i=0;i<BlkdCards.size();i++)
        {

            System.out.println("Card serial  "+BlkdCards.get(i));
            if (card.equalsIgnoreCase(BlkdCards.get(i)))
            {
                System.out.println("Card serial Blkdcard exiting ");
//                Toast.makeText(getApplicationContext(), "This card is blocked", Toast.LENGTH_SHORT).show();
                alertDlg("FAILURE! \n Blocked Card!");
                return true;
            }

        }


        return false;
    }


    

    @Override
    public void onBackPressed() {

//        super.onBackPressed();

        if( Constants.AsyncFlag==0){
            Toast.makeText(getApplicationContext(),"Please Wait..",Toast.LENGTH_SHORT).show();
        }else {
            Constants.AsyncFlag=-1;
            super.onBackPressed();
        }
    }

}
