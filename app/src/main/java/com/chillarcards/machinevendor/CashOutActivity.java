package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.Printer.PaymentPrinter;


public class CashOutActivity extends Activity{

    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;

    public static final int READ_CARD_SERIAL = 5;
    public static final int READ_CURR_BAL = 8;
    public static final int READ_SCHOOL_ID = 4;
    public static final int READ_STATUS = 6;
    public static final int READ_EXPIRY = 12;

    public static final int WRITE_CURR_BAL = 8;
    public static final int WRITE_PREV_BAL = 9;
    public static final int WRITE_LASTTRANS_MC_ID = 16;
    public static final int WRITE_TRANS_ID = 17;
    public static final int WRITE_TRANS_AMT = 18;


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
    StringBuffer read_status = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();

    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
//
//    
//    char[] write_data = {  '0', '0', '0', '0','4', '1', '2', '6', '7', '2', '2', 'a', '2', '3',
//            '3', 'e' };



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

    int authsuc1,authsuc2,authsuc3,authsuc4,authsuc5,readauthsuc1,readauthsuc2,readauthsuc3;;
    int writesuc1,writesuc2,writesuc3,writesuc4,writesuc5;
    int readsuc0,readsuc1,readsuc2,readsuc3,readsuc4,readsuc5,readsuc6;

    int tama_on;

    //    DatabaseHandler db = new DatabaseHandler(this);
    DatabaseHandler db ;
    int transtype_id,userid;

    String trans_id,machine_id;

//    Float total=(float) 0;

    private final List<String> BlkdCards = new ArrayList<>();

    String resultCardSerial,resultPrevbal;
    Float previousbalnce;
    int currentbalance;
    String User_name,Pass_word;
    String print="";
    String currentDateTimeString,currentDateTimeString1;
    Date expirydate,today1;

    ProgressBar progressBar;

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



    @Override
    protected void onCreate(Bundle savedInstanceState)  throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        db = DatabaseHandler.getInstance(getApplicationContext());


        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        context = getApplicationContext();
        tap_the_card = (Button) findViewById(R.id.tap);
        trans_id=db.lastTransID();
        machine_id=db.getMachineID();

//        Bundle b=getIntent().getExtras();
//        total=b.getFloat("amount");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid=editor.getInt("userid", 0);

//        System.out.println("CHILL MachineID "+machine_id+" TransId "+trans_id+" Amount "+total);


        DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        currentDateTimeString = df.format(today);

        System.out.println("CurrentDAteTime: "+currentDateTimeString);
//        currentDateTimeString=currentDateTimeString.replace(" ","%20");


        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        // Get the date today using Calendar object.
        Date today11 = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        currentDateTimeString1 = df1.format(today11);

        // textView is the TextView view that should display it


        transtype_id=db.getTransTypID("BALANCE RETURN");

        System.out.println("CHILL TransTypeID"+transtype_id);


//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
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
//                        try {
//
//                            readauthsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_SECND_SECT, key2, KEYA);
//
//                            if (readauthsuc1 == 0) {
//
//                                readsuc0 = apis.tama_read_target(READ_SCHOOL_ID
//                                        , read_school);
//
//                                readsuc1 = apis.tama_read_target(
//                                        READ_CARD_SERIAL, read_serial);
//                                readsuc3 = apis.tama_read_target(
//                                        READ_STATUS, read_status);
//
//
//                                if (readsuc1 == 0) {
////                                Toast.makeText(context, "Read Success serial "+read_serial,
////                                        Toast.LENGTH_SHORT).show();
//                                    System.out.println("read Success");
//
//
//                                } else {
//                                    Toast.makeText(context, "Read Failed serial", Toast.LENGTH_SHORT)
//                                            .show();
//                                    System.out.println("Read Failed");
//                                }
//
//                            } else {
//                                Toast.makeText(context, "Read Authentication Failed serial",
//                                        Toast.LENGTH_SHORT).show();
//                                System.out.println("Authentication Failed");
//                            }
//
//                            String schId = String.valueOf(read_school);
////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
////                                Toast.LENGTH_LONG).show();
//
//                            String serial = String.valueOf(read_serial);
//
//
//                            //reading card serial
//                            resultCardSerial = serial;
//
//                            String cardstat = String.valueOf(read_status);
//
//                            int cardstat1 = Integer.parseInt(cardstat);
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
//                                        Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                .show();
//                                        System.out.println("Read Failed");
//                                    }
//
//                                } else {
//                                    Toast.makeText(context, "Read Authentication Failed expiry",
//                                            Toast.LENGTH_SHORT).show();
//                                    System.out.println("Authentication Failed");
//                                }
//
//                                //reading card expiry
//
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
//                                    finish();
//
//                                } else {
//
//                                    if (schId.replace(" ", "").equalsIgnoreCase(db.getSchoolId())) {
//
//
//
////                                        if(today1.after(expirydate)){
////
////                                            System.out.println("ELDHO: card expired");
////                                            Toast.makeText(getApplicationContext(),"Card already expired. ",Toast.LENGTH_LONG).show();
////                                            finish();
////                                        }else {
//
//                                            readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                            if (readauthsuc2 == 0) {
//                                                readsuc2 = apis.tama_read_target(
//                                                        READ_CURR_BAL, read_current);
//
//
//                                                if (readsuc2 == 0) {
////                                Toast.makeText(context, "Read Success current bal"+read_current,
////                                        Toast.LENGTH_SHORT).show();
//                                                    System.out.println("read Success current bal");
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                            .show();
//                                                    System.out.println("Read Failed");
//                                                }
//
//                                            } else {
//                                                Toast.makeText(context, "Read Authentication Failed current bal",
//                                                        Toast.LENGTH_SHORT).show();
//                                                System.out.println("Authentication Failed");
//                                            }
//
//                                            String prev = String.valueOf(read_current);
//
//
//                                            Float Prev = Float.valueOf(prev);
//                                            previousbalnce = Prev;
//
//
//
//                                            amountDialog(Prev,prev,resultCardSerial);
//
//
////                                        }
//
//                                    } else {
//                                        Toast.makeText(context, "Card not belonging to this school!!",
//                                                Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//
//                                }
//
//
//
//                            }
//
//                        }catch(NumberFormatException e){
//                            e.printStackTrace();
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    CashOutActivity.this);
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
//
//                                            dialog.dismiss();
////                                            Intent i=new Intent(getApplicationContext(),PaymentActivity.class);
////                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                            startActivity(i);
////                                            finish();
//                                            new FieldRestartTask().execute();
//
//                                        }
//                                    });
//                            alert.show();
//
//                            System.out.println("Target detect Failed");
//                        }catch(NullPointerException e){
//                            Toast.makeText(context, "Card Error!!",
//                                    Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//
//                    } else {
//                        tap_the_card.setClickable(false);
//                        tap_the_card.setEnabled(false);
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                CashOutActivity.this);
//                        alert.setTitle("RFID");
//
//                        alert.setCancelable(false);
//                        alert.setMessage("Target detect Failed");
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


    public void amountDialog(final Float Prev, final String prev, final String resultCardSerial1){

        final Dialog dlg=new Dialog(CashOutActivity.this);
        dlg.setContentView(R.layout.withdraw_money_dlg);
        dlg.setTitle("Withdraw Money");
        dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button OkButton = (Button) dlg.findViewById(R.id.btnok);
        final Button NoButton = (Button) dlg.findViewById(R.id.btnno);
        TextView cardsl= (TextView) dlg.findViewById(R.id.cardsl1);
        TextView balance= (TextView) dlg.findViewById(R.id.balan1);
        final ProgressBar progressBar=(ProgressBar) dlg.findViewById(R.id.progress);
        final EditText amt= (EditText) dlg.findViewById(R.id.amount);

        cardsl.setText(resultCardSerial1);
        balance.setText(Prev+"");



        int Currentbal;


        dlg.show();


        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dlg.cancel();

                Float total=(float) 0;
                if(amt.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter a valid amount.", Toast.LENGTH_SHORT).show();
                }else {
                    total = Float.valueOf(amt.getText().toString());

                    progressBar.setVisibility(View.VISIBLE);

//                    if (total <= Prev) {
//
//                        if (SecondActivity.ret_fieldon == 0) {
//
//                            System.out.println("Tama open :" + tama_on);
//
//                            tardtc = apis.tama_detectTarget(TARGET_NUM,
//                                    target_detected, target_id);
//
//                            if (tardtc == 0) {
//
//                                int Current = (int) (Prev - total);
//
//                                String filler = "";
//
//                                String curbal = String.valueOf(Current);
//                                int len = 16 - curbal.length();
//                                for (int j = 0; j < len; ++j) {
//
//                                    filler = filler + "0";
//
//                                }
//
//                                //                        String currentbal= String.valueOf(Current);
//                                String data = filler + curbal;
//
//                                System.out.println("CHILLAR: Filler: " + data);
//
//                                char[] curren = data.toCharArray();
//                                char[] prevv = prev.toCharArray();
//
//
//                                authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                System.out.println("Authentication Success");
//
//
//                                if (authsuc1 == 0) {
//
////                                        Toast.makeText(context, "Authentication Success2",
////                                                Toast.LENGTH_SHORT).show();
//
//                                    writesuc1 = apis.tama_write_target(WRITE_CURR_BAL,
//                                            curren, curren.length);
//
//                                    writesuc2 = apis.tama_write_target(WRITE_PREV_BAL,
//                                            prevv, prevv.length);
//
//
////                                Toast.makeText(context, "writesuc " + writesuc1,
////                                        Toast.LENGTH_SHORT).show();
//
//
//                                } else {
//
//                                    Toast.makeText(context, "Authentication FAILED2",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//                                String filler1 = "";
//
//
//                                int len1 = 16 - trans_id.length();
//                                for (int j = 0; j < len1; ++j) {
//
//                                    filler1 = filler1 + "0";
//
//                                }
//
//
//                                String data1 = filler1 + trans_id;
//
//                                System.out.println("CHILLAR: Filler transid: " + data1);
//
//                                String filler2 = "";
//
//
//                                int len2 = 16 - machine_id.length();
//                                for (int j = 0; j < len2; ++j) {
//
//                                    filler2 = filler2 + "0";
//
//                                }
//
//
//                                String data2 = filler2 + machine_id;
//
//                                System.out.println("CHILLAR: Filler machinid: " + data2);
//
//
//                                char[] transs = data1.toCharArray();
//                                char[] machine = data2.toCharArray();
//
//                                String totall = String.valueOf(total);
//
//                                String filler3 = "";
//
//
//                                int len3 = 16 - totall.length();
//                                for (int j = 0; j < len3; ++j) {
//
//                                    filler3 = filler3 + "0";
//
//                                }
//
//
//                                String data3 = filler3 + totall;
//
//                                System.out.println("CHILLAR: Filler totalamt: " + data3);
//                                char[] tott = data3.toCharArray();
//
//
//                                authsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                if (authsuc2 == 0) {
//
////                                        Toast.makeText(context, "Authentication Success3",
////                                                Toast.LENGTH_SHORT).show();
//
//                                    writesuc3 = apis.tama_write_target(WRITE_LASTTRANS_MC_ID,
//                                            machine, machine.length);
//
//                                    writesuc4 = apis.tama_write_target(WRITE_TRANS_ID,
//                                            transs, transs.length);
//                                    writesuc5 = apis.tama_write_target(WRITE_TRANS_AMT,
//                                            tott, tott.length);
//                                } else {
//
//                                    Toast.makeText(context, "Authentication FAILED3",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
////
////                                    Toast.makeText(context, writesuc1+","+writesuc2+","+writesuc3+","+writesuc4+","+writesuc5,
////                                            Toast.LENGTH_SHORT).show();
//
//
//                                //passing the values to anothe r activity to print the bill
//                                if (authsuc1 == 0 && authsuc2 == 0 && (writesuc1 == 0) && (writesuc2 == 0) && (writesuc3 == 0) && (writesuc4 == 0) && (writesuc5 == 0)) {
//
//                                    WriteToDB(resultCardSerial1, Prev, total);
//
//                                    progressBar.setVisibility(View.GONE);
//
//                                    dlg.dismiss();
//
//                                    Toast.makeText(context, "SUCCESS!",
//                                            Toast.LENGTH_SHORT).show();
//
//
//                                }
//
//
//                            } else {
//                                tap_the_card.setClickable(false);
//                                tap_the_card.setEnabled(false);
//                                Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                        .show();
//                                AlertDialog.Builder alert = new AlertDialog.Builder(
//                                        CashOutActivity.this);
//                                alert.setTitle("RFID");
//
//                                alert.setCancelable(false);
//                                alert.setMessage("Target detect Failed");
//
//                                DialogInterface.OnClickListener listener;
//                                alert.setPositiveButton(
//                                        "OK",
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//
//                                                dialog.dismiss();
////                                        Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        startActivity(i);
////                                        finish();
//                                                new FieldRestartTask().execute();
//
//                                            }
//                                        });
//                                alert.show();
//
//                                System.out.println("Target detect Failed");
//                            }
//
//
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Requested amount not available", Toast.LENGTH_SHORT).show();
//                    }
                }


            }
        });


        NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();


            }
        });
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
                Toast.makeText(getApplicationContext(), "This card is blocked", Toast.LENGTH_SHORT).show();
                return true;
            }

        }


        return false;
    }


    public void WriteToDB(String resultCardSerial1, Float Prev, Float total)
    {
        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");

// Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();

        currentDateTimeString = df.format(today);

        trans_id=db.lastTransID();


        Success_Transaction success_transaction=new Success_Transaction(trans_id,userid,transtype_id,Prev,(Prev-total),resultCardSerial1,currentDateTimeString,"");

        db.addSuccesstransaction(success_transaction);
//        db.addPaymenttransaction(pay_transaction);


//
//        Log.d("CODMOB", "User BillNo: " + pay_transaction.getbillno());
//        Log.d("CODMOB", "User listsuccess: " + db.getAllSuccess());
//
        List<Success_Transaction> successtransaction = db.getAllSuccess();
        for (Success_Transaction usp : successtransaction) {
            String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                    "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                    + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                    "  Server timestamp  " + usp.getserver_timestamp();
            System.out.println("CHILLARAAAA: success transaction table" + testpermission);


        }
//
//
//        Log.d("CODMOB", "pay transaction: " + db.getAllpaytransaction().size());
//        Log.d("CODMOB", "pay transaction2 : " + db.getAllpaytransaction());
//
//        List<Payment_Transaction> paytransaction = db.getAllpaytransaction();
//        for (Payment_Transaction uspaytrans : paytransaction) {
//            String payString = " ID " + uspaytrans.getId() + "trans_id1 "
//                    + uspaytrans.gettrans_id() + " Amount " + uspaytrans.getamount() + " Server_time_timestamp " + uspaytrans.getserver_timestamp();
//            System.out.println("CODMOB: paytransaction  amount:" + payString);
//        }
//
////        if (true) {
////
////            System.out.println("CHILLARCODMOB");
////            db.lastTransID();
////
////        }




    }


    @Override
    public void onBackPressed() {

        if( Constants.AsyncFlag==0){
            Toast.makeText(getApplicationContext(),"Please Wait..",Toast.LENGTH_SHORT).show();
        }else {
            Constants.AsyncFlag=-1;
            super.onBackPressed();
        }
    }

    


}