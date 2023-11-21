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
import java.util.TimeZone;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Library_book_transaction;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;


public class LibraryNFC extends Activity {

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


    public static final int WRITE_BOOK_ID = 0;
    public static final int WRITE_ISSUE_RETURN = 1;
    public static final int WRITE_ISSUE_DATE = 2;


    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;
    private final List<String> BlkdCards = new ArrayList<>();
    public Button tap_the_card;
    char[] key1 = {0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e};
    char[] key2 = {0x52, 0x34, 0x3f, 0x36, 0x21, 0x37};
    char[] key3 = {0x53, 0x23, 0x61, 0x37, 0x5e, 0x39};
    char[] key4 = {0x42, 0x21, 0x62, 0x40, 0x49, 0x23};
    char[] key5 = {0x50, 0x24, 0x72, 0x40, 0x41, 0x5e};
    char[] key6 = {0x43, 0x25, 0x63, 0x31, 0x5e, 0x39};
    StringBuffer read_serial = new StringBuffer();
    StringBuffer read_current = new StringBuffer();
    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();
    StringBuffer read_school = new StringBuffer();

    //    
    StringBuffer read_status = new StringBuffer();

    //    static {
//
//        System.loadLibrary("gl11rfid");
//        System.out.println("Library Loaded");
//
//        rfidService = IRfidService.Stub.asInterface(ServiceManager
//                .getService("rfid"));
//        System.out.println("CHILLR: "+rfidService);
//    }
    StringBuffer read_expiry = new StringBuffer();
    Context context;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int authsuc1, authsuc2, authsuc3, authsuc4, authsuc5, readauthsuc1, readauthsuc2, readauthsuc3;
    int writesuc1, writesuc2, writesuc3, writesuc4, writesuc5;
    int readsuc0, readsuc1, readsuc2, readsuc3, readsuc4, readsuc5, readsuc6;
    int tama_on;
    //    DatabaseHandler db = new DatabaseHandler(this);
    DatabaseHandler db;
    int transtype_id;
    String trans_id, machine_id, rech_id;
    Float total = (float) 0;
    String resultCardSerial, resultPrevbal, currentDateTimeString, currentDateTimeString1;
    String User_name, Pass_word;
    int userid;
    Float currentbalance;
    int paymentTypeId;
    Float previousbalnce;
    String formattedDate, bookId, localTime, issueReturn;
    Date expirydate, today1;
    ProgressBar progressBar;
    String print = "";

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
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        db = DatabaseHandler.getInstance(getApplicationContext());


        context = getApplicationContext();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        tap_the_card = (Button) findViewById(R.id.tap);

        Bundle b = getIntent().getExtras();
        bookId = b.getString("bookid");
        issueReturn = String.valueOf(b.getInt("issuereturn"));

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);
        DateFormat df1 = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");

// Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        currentDateTimeString = df1.format(today);
//        currentDateTimeString=currentDateTimeString.replace(" ","%20");


        DateFormat df11 = new SimpleDateFormat("dd/MM/yyyy");

// Get the date today using Calendar object.
        Date today11 = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        currentDateTimeString1 = df11.format(today11);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

//        Toast.makeText(getApplicationContext(), "date : "+formattedDate, Toast.LENGTH_SHORT).show();


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        localTime = date.format(currentLocalTime);
        localTime = localTime.replace(" ", "");
//        Toast.makeText(getApplicationContext(), "time : "+localTime, Toast.LENGTH_SHORT).show();


        machine_id = db.getMachineID();
        rech_id = String.valueOf(db.lastRechID());


        System.out.println("CHILL MachineID " + machine_id + " TransId " + trans_id + " Amount " + total);

        transtype_id = db.getTransTypID("LIBRARY");
        System.out.println("CHILL TransTypeID" + transtype_id);


//        try {
//            ret_poweron = rfidService.rfid_poweron();
//            System.out.println("PowerOn :" + ret_poweron);
//
//            if (ret_poweron == 0 || ret_poweron == 2) {
//
//                Toast.makeText(context, "Module open success", Toast.LENGTH_SHORT).show();
//
//                ret_fieldon = rfidService.rfid_rffield_on();
//
//                System.out.println("FieldOn :" + ret_fieldon);
//
//                tama_on = apis.tama_open();
//
//            } else {
//                Toast.makeText(context, "Module open Failed", Toast.LENGTH_SHORT).show();
//                System.out.println("Module open Failed");
//            }
//
//        } catch (RemoteException e) {
//
//            e.printStackTrace();
//        }


//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                trans_id = db.lastTransID();
//
//                tap_the_card.setEnabled(false);
//                tap_the_card.setClickable(false);
//
//                //CHECKING THE BUTTON FIELD ON
//
//                if (SecondActivity.ret_fieldon == 0) {
//
//                    System.out.println("Tama open :" + tama_on);
//
//                    tardtc = apis.tama_detectTarget(TARGET_NUM,
//                            target_detected, target_id);
//
//                    if (tardtc == 0) {
//
//                        try {
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
//
//                            String schId = String.valueOf(read_school);
////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
////                                Toast.LENGTH_LONG).show();
//
//                            String serial = String.valueOf(read_serial);
//                            resultCardSerial = serial;
//                            String cardstat = String.valueOf(read_status);
//
//                            int cardstat1 = Integer.parseInt(cardstat);
//
//                            if (cardstat1 == 0) {
//                                finish();
//                            } else {
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
////                                            if(today1.after(expirydate)){
////
////                                                System.out.println("ELDHO: card expired");
////                                                Toast.makeText(getApplicationContext(),"Card already expired. ",Toast.LENGTH_LONG).show();
////                                                finish();
////                                            }else {
//
//                                        readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                        if (readauthsuc2 == 0) {
//                                            readsuc2 = apis.tama_read_target(
//                                                    READ_CURR_BAL, read_current);
//
//
//                                            if (readsuc2 == 0) {
////                                        Toast.makeText(context, "Read Success current bal"+read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                                System.out.println("read Success current bal");
//
//
//                                            } else {
//                                                Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                        .show();
//                                                System.out.println("Read Failed");
//                                            }
//
//                                        } else {
//                                            Toast.makeText(context, "Read Authentication Failed current bal",
//                                                    Toast.LENGTH_SHORT).show();
//                                            System.out.println("Authentication Failed");
//                                        }
//
//                                        String currnt = String.valueOf(read_current);
//
//                                        System.out.println("NewChillr : " + currnt);
//
//                                        previousbalnce = Float.valueOf(currnt);
//                                        System.out.println("NewChillr : " + previousbalnce);
//
//                                        if ((readsuc0 == 0) && (readsuc1 == 0) && (readsuc2 == 0)) {
//
//                                            System.out.println("NewChillr success ");
//
//                                            LibraryEnter();
//
//
////                                    print=print+"     TransID  "+trans_id+"\n"+
////                                            "     UserID   "+userid+"\n"+
////                                            "     BookID   "+bookId+"\n"+
////                                            "     PrevBal  "+previousbalnce+"\n"+
////                                            "     CurrBal  "+currentbalance+"\n"+
////                                            "     IsueRet  "+formattedDate+"\n"+
////                                            "     IsueTme  "+localTime+"\n"+
////                                            "     CardSer  "+resultCardSerial+"\n"
////                                    ;
////
////                                    System.out.println("     TransID  "+trans_id+"\n"+
////                                            "     UserID   "+userid+"\n"+
////                                            "     BookID   "+bookId+"\n"+
////                                            "     PrevBal  "+previousbalnce+"\n"+
////                                            "     CurrBal  "+currentbalance+"\n"+
////                                            "     CardSer  "+resultCardSerial+"\n"+
////                                            "     Amount   "+total+"\n");
//
//                                            Toast.makeText(context, "SUCCESSSSS!!!!!!",
//                                                    Toast.LENGTH_SHORT).show();
//
//                                            Intent in = new Intent(getApplicationContext(), LibraryActivity.class);
////                                    Bundle printbundle = new Bundle();
////                                    printbundle.putString("print",print );
////                                    in.putExtras(printbundle);
//                                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            startActivity(in);
//                                            finish();
//
//
//                                        }
////                                            }
//                                    } else {
//                                        Toast.makeText(context, "Card not belonging to this school!!",
//                                                Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//
//                                }
//
//
//                            }
//
//
//                        } catch (NumberFormatException e) {
//
//
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//
//                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    LibraryNFC.this);
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
////                                            Intent i=new Intent(getApplicationContext(),LibraryActivity.class);
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
//                        } catch (NullPointerException e) {
//                            Toast.makeText(context, "Card Error!!",
//                                    Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//
//
//                    } else {
//
//
//                        tap_the_card.setClickable(false);
//                        tap_the_card.setEnabled(false);
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                LibraryNFC.this);
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
//
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
//

    }

    private Boolean BlocardsCheck(String card) {


        List<Blocked_Cards_Info> blocked_cards_infos = db.getAllBlockCardInfo();
        for (Blocked_Cards_Info cn : blocked_cards_infos) {
            String log = "Id: " + cn.getBlocked_cards_id() + " ,Name: " + cn.getCard_serial();

            String cardSerial = cn.getCard_serial();
            BlkdCards.add(cardSerial);

            System.out.println("CHILLAR Blocked: " + log);
        }


        for (int i = 0; i < BlkdCards.size(); i++) {

            System.out.println("Card serial  " + BlkdCards.get(i));
            if (card.equalsIgnoreCase(BlkdCards.get(i))) {
                System.out.println("Card serial Blkdcard exiting ");
                Toast.makeText(getApplicationContext(), "This card is blocked", Toast.LENGTH_SHORT).show();
                return true;
            }

        }


        return false;
    }


    private void LibraryEnter() {


        String cardserialnumber = resultCardSerial;

        currentbalance = previousbalnce;


        Library_book_transaction lib = new Library_book_transaction(trans_id, userid, transtype_id, previousbalnce,
                currentbalance, cardserialnumber, currentDateTimeString, "", trans_id, issueReturn, bookId, currentDateTimeString, "");

        db.addSuccesstransaction(lib);
        db.addlib(lib);

        System.out.println("CHILL New Prev " + lib.getprevious_balnce());
        System.out.println("CHILL New Curr " + lib.getcurrent_balance());
        System.out.println("CHILL TransID" + lib.gettransaction_id());
        System.out.println("CHILL IssueTime" + lib.getissue_time());

//        List<Library_book_transaction> contacts1 = db.getAlllib();
//        for (Library_book_transaction cn : contacts1) {
//            String log = "Id: " + cn.getid() + " ,BookId: " + cn.getbook_id()+" ,Issued:"+cn.getissue_time()+",TransId:"+
//                    cn.gettransaction_id()+" ,ServerTimeStamp:"+cn.getserver_timestamp()+" ,Return:"+cn.getissue_return() ;
//            // Writing Contacts to log
//            System.out.println("CHILLAR LIBRARY : "+log);
//        }
//        db.close();
//
//        List<Success_Transaction> successtransaction = db.getAllSuccess();
//        for (Success_Transaction usp : successtransaction) {
//            String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
//                    "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
//                    + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
//                    "  Server timestamp  " + usp.getserver_timestamp();
//            System.out.println("CHILLAR LIBRARY success transaction table " + testpermission);
//
//
//        }


//        Toast.makeText(getApplicationContext(),"Success!!",Toast.LENGTH_LONG).show();


    }

    @Override
    public void onBackPressed() {

        if (Constants.AsyncFlag == 0) {
            Toast.makeText(getApplicationContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
        } else {
            Constants.AsyncFlag = -1;
            super.onBackPressed();
        }
    }

//    private class FieldRestartTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            apis.tama_close();
//
//            try {
//                int ret_fieldoff = SecondActivity.rfidService.rfid_rffield_off();
//
//                System.out.println("Field Off:" + ret_fieldoff);
//
//                int ret_poweroff = SecondActivity.rfidService.rfid_poweroff();
//
//                System.out.println("PowerOFF :" + ret_poweroff);
//
//                ret_poweron = SecondActivity.rfidService.rfid_poweron();
//                System.out.println("RFID PowerOn :" + ret_poweron);
//
//                if (ret_poweron == 0 || ret_poweron == 2) {
//
////                Toast.makeText(getApplicationContext(), "Module open success", Toast.LENGTH_SHORT).show();
//
//                    ret_fieldon = SecondActivity.rfidService.rfid_rffield_on();
//
//                    System.out.println("RFID FieldOn :" + ret_fieldon);
//
//                    tama_on = apis.tama_open();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Module open Failed", Toast.LENGTH_SHORT).show();
//                    System.out.println("RFID Module open Failed");
//                }
//
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Constants.AsyncFlag = 0;
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            Constants.AsyncFlag = 1;
//            progressBar.setVisibility(View.GONE);
//            tap_the_card.setClickable(true);
//            tap_the_card.setEnabled(true);
//        }
//    }

}