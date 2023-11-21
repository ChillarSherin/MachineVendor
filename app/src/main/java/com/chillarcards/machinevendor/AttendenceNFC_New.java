package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
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



/**
 * Created by Ijaz on 23-11-2016.
 */

public class AttendenceNFC_New extends Activity implements View.OnKeyListener {


    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;


    public static final int READ_SCHOOL_ID = 4;
    public static final int READ_CARD_SERIAL = 5;
    public static final int READ_STATUS = 6;
    public static final int READ_CURR_BAL = 8;
    public static final int READ_PREV_BAL = 9;
    public static final int READ_EXPIRY = 12;
    public static final int READ_IN_OUT = 13;
    public static final int READ_ATTND_TYPE = 14;
    public static final int READ_ATTND_DATE = 10;
    public static final int READ_LASTTRANS_MC_ID = 16;
    public static final int READ_TRANS_ID = 17;
    public static final int READ_TRANS_AMT = 18;
    public static final int READ_LAST_RECH_MC_ID = 20;
    public static final int READ_LAST_RECH_ID = 21;
    public static final int READ_LAST_RECH_AMT = 22;


    public static final int WRITE_CURR_BAL = 8;
    public static final int WRITE_PREV_BAL = 9;
    public static final int WRITE_LASTTRANS_MC_ID = 16;
    public static final int WRITE_TRANS_ID = 17;
    public static final int WRITE_TRANS_AMT = 18;

    public static final int WRITE_LAST_RECH_MC_ID = 20;
    public static final int WRITE_LAST_RECH_ID = 21;
    public static final int WRITE_LAST_RECH_AMT = 22;

    public static final int WRITE_IN_OUT = 13;
    public static final int WRITE_ATTND_TYPE = 14;
    public static final int WRITE_ATTND_DATE = 10;

    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;

    static {

        System.loadLibrary("gl11rfid");
        System.out.println("Library Loaded");

    }

    static {

        System.loadLibrary("printerfinal");
        System.out.println("Printer Library Loaded Successfully");
    }

    private final List<String> BlkdCards = new ArrayList<>();
    char[] key1 = {0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e};
    char[] key2 = {0x52, 0x34, 0x3f, 0x36, 0x21, 0x37};
    char[] key3 = {0x53, 0x23, 0x61, 0x37, 0x5e, 0x39};
    char[] key4 = {0x42, 0x21, 0x62, 0x40, 0x49, 0x23};
    char[] key5 = {0x50, 0x24, 0x72, 0x40, 0x41, 0x5e};
    char[] key6 = {0x43, 0x25, 0x63, 0x31, 0x5e, 0x39};
    StringBuffer read_serial = new StringBuffer();
    StringBuffer read_current = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();
    StringBuffer read_status = new StringBuffer();
    //    StringBuffer read_status = new StringBuffer();
    StringBuffer read_data5 = new StringBuffer();
    StringBuffer read_inout = new StringBuffer();
    StringBuffer read_type = new StringBuffer();
    StringBuffer read_date = new StringBuffer();
    StringBuffer read_data;
    StringBuffer target_detected;
    StringBuffer target_id;
    String filler3 = "";
    String filler4 = "";
    String filler5 = "";
    char[] write_data = {'1', '2', '3', '4', '5', '6', '7', '7', '9', '0',
            'A', 'B', 'C', 'D', 'E', 'F'};
    Context context;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int authsuc;
    int writesuc;
    int readsuc;
    int tama_on;
    int authsuc1, authsuc2, authsuc3, authsuc4, authsuc5, authsuc6, readauthsuc1, readauthsuc2, readauthsuc3, readauthsuc4, readauthsuc5, readauthsuc6, authsuc7, readauthsuc7;
    int writesuc1, writesuc2, writesuc3, writesuc4, writesuc5, writesuc6, writesuc7, writesuc8, writesuc9, writesuc10, writesuc11, writesuc12, writesuc13, writesuc14;
    int readsuc0, readsuc1, readsuc2, readsuc3, readsuc4, readsuc5, readsuc6, readsuc7, readsuc8, readsuc9, readsuc10, readsuc11, readsuc12, readsuc13, readsuc14;
    String resultCardSerial, resultCurrenBal, resultPrevbal, resultCardSerial1;
    Float total = (float) 0;
    ProgressBar progressBar;
    Float previousbalnce;
    
    //    ProgressDialog progressDialog;
    String times, formattedDate;
    int pb;
    int paymentTypeId;
    int transtype_id;
    float currentbalance;
    String User_name, Pass_word;
    String print = "";
    String currentDateTimeString, currentDateTimeString1, schoolname, schoolplace;
    Date expirydate, today1, att_date_check;
    String a_date, a_type;
    int userid;
    int useridinteger;
    String attend_typ;
    String inout = "0000000000000000";
    String attName, at_type;
    int typeattend;
    String schooid, schoomachcode, machineserial, tranact_id;
    ArrayList<String> card_list = new ArrayList<String>();
    String trans_id = "", machine_id, rech_id;
    DatabaseHandler db;
//    @Override
//    protected void onDestroy() {
//
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
//        if (hand != null) {
//            hand.removeCallbacks(r);
//        }
//        if (hand1 != null) {
//            hand1.removeCallbacks(r1);
//        }
//        super.onDestroy();
//
//    }

    Runnable r1;
    Handler hand1;

    //    ProgressDialog progressDialog;
    Runnable r;
    Handler hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        context = getApplicationContext();

        db = DatabaseHandler.getInstance(getApplicationContext());

//        int po = ap.prn_open();


//        progressDialog=new ProgressDialog(this);
        Bundle b = getIntent().getExtras();
        attend_typ = b.getString("id");
        card_list = b.getStringArrayList("card_list");
        at_type = attend_typ;
        System.out.println("card_list122 : " + card_list);
        System.out.println("CHILLAR: Filler: attend_typ id : " + attend_typ);

//        progressDialog = new ProgressDialog(this);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);
        schooid = editor.getString("schoolId", "");
        schoomachcode = editor.getString("schlMachCode", "");
        machineserial = editor.getString("machineserial", "");
        schoolname = editor.getString("schoolname", "");
        schoolplace = editor.getString("schoolplace", "");

        System.out.println("CODMOB: 123  serial " + schooid + schoomachcode + machineserial);

        useridinteger = userid;
        System.out.println("CODMOB: 123  user " + useridinteger);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        transtype_id = db.getTransTypID("ATTENDANCE");

//        Bundle b=getIntent().getExtras();

//        paytypename=b.getString("paytypename");

        DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

// Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        currentDateTimeString = df.format(today);

        System.out.println("CurrentDAteTime: " + currentDateTimeString);
//        currentDateTimeString=currentDateTimeString.replace(" ","%20");


        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

// Get the date today using Calendar object.
        Date today11 = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        currentDateTimeString1 = df1.format(today11);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df2.format(c.getTime());

//        Toast.makeText(getApplicationContext(), "date : " + formattedDate, Toast.LENGTH_SHORT).show();


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        times = hour + ":" + minute;
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

        // TODO Auto-generated method stub
        r1 = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println("in handler");

                //onTapCard();

            }
        };

        // if(hand1!=null){
        // hand1.removeCallbacks(r1);
        // }

        hand1 = new Handler();
        hand1.postDelayed(r1, 500);

        // onTapCard();
        // onTapCard();
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

    }

//    public void onTapCard() {
//
//        try {
//
//            tama_on = apis.tama_open();
//
//            if (SecondActivity.ret_fieldon == 0) {
//
////            progressDialog.show(this,"Processing..","Please Wait..",false,false);
//
//                System.out.println("Tama open :" + tama_on);
//                target_detected = new StringBuffer();
//                target_id = new StringBuffer();
//                tardtc = apis.tama_detectTarget(TARGET_NUM, target_detected,
//                        target_id);
//                System.out.println("targer detect return :" + tardtc);
//                if (tardtc == 0) {
//
////                    progressDialog.show();
//
//                    try {
//
//
//                        readauthsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                AUTH_SECND_SECT, key2, KEYA);
//
//                        if (readauthsuc1 == 0) {
//
//                            readsuc0 = apis.tama_read_target(READ_SCHOOL_ID
//                                    , read_school);
//
//                            readsuc1 = apis.tama_read_target(
//                                    READ_CARD_SERIAL, read_serial);
//                            readsuc3 = apis.tama_read_target(
//                                    READ_STATUS, read_status);
//
//
//                            if (readsuc1 == 0) {
//
//                                System.out.println("read Success");
//
//                            } else {
////                                Toast.makeText(context, "Read Failed serial", Toast.LENGTH_SHORT)
////                                        .show();
//                                System.out.println("Read Failed");
//                            }
//
//                        } else {
////                            Toast.makeText(context, "Read Authentication Failed serial",
////                                    Toast.LENGTH_SHORT).show();
//                            System.out.println("Authentication Failed");
//                        }
//
//
//                        String schId = String.valueOf(read_school);
//
//                        String serial = String.valueOf(read_serial);
//                        resultCardSerial = serial;
//
////455666
//                        String cardstat = String.valueOf(read_status);
//
//
//                        int cardstat1 = Integer.parseInt(cardstat);
//
//                        if (std_list(serial)) {
//
//                            if (cardstat1 == 0) {
////                        finish();
////                            Toast.makeText(getApplicationContext(), "Card not active!", Toast.LENGTH_SHORT).show();
////                                AlertDialog.Builder alert = new AlertDialog.Builder(
////                                        AttendenceNFC_New.this);
////                                alert.setTitle("Sorry");
////
////                                alert.setCancelable(false);
////                                alert.setMessage("Card Not Active");
////
////                                DialogInterface.OnClickListener listener;
////                                alert.setPositiveButton("OK",
////                                        new DialogInterface.OnClickListener() {
////
////                                            @Override
////                                            public void onClick(
////                                                    DialogInterface dialog,
////                                                    int which) {
////
////                                                dialog.dismiss();
////                                                progressDialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                                                Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                                                Bundle b = new Bundle();
////                                                b.putStringArrayList("card_list", card_list);
////                                                b.putString("id", attend_typ);
////                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                                i.putExtras(b);
////                                                startActivity(i);
////                                            }
////                                        });
//////                                            alert.setNegativeButton("CANCEL",
//////                                                    new DialogInterface.OnClickListener() {
//////
//////                                                        @Override
//////                                                        public void onClick(
//////                                                                DialogInterface dialog,
//////                                                                int which) {
//////
//////                                                            dialog.dismiss();
//////                                                            finish();
//////                                                        }
//////                                                    });
////                                alert.show();
//
//                                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                                dialog.setTitle("Sorry");
//
//                                dialog.setCancelable(false);
//                                dialog.setMessage("Card Not Active");
//
//                                DialogInterface.OnClickListener listener;
//
//                                final AlertDialog alert = dialog.create();
//                                alert.show();
//
////        alert.setPositiveButton("OK",
////                new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(
////                            DialogInterface dialog,
////                            int which) {
////
////                        dialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                        Bundle b=new Bundle();
////                        b.putString("id",attend_typ);
////                        b.putStringArrayList("card_list", card_list);
////                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        i.putExtras(b);
////                        startActivity(i);
////                    }
////                });
//
//                                final Handler handler = new Handler();
//                                final Runnable runnable = new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (alert.isShowing()) {
//                                            alert.dismiss();
//
//                                            Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                                            Bundle b = new Bundle();
//                                            b.putString("id", attend_typ);
//                                            b.putStringArrayList("card_list", card_list);
//                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            i.putExtras(b);
//                                            startActivity(i);
//                                        }
//                                    }
//                                };
//
//                                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                    @Override
//                                    public void onDismiss(DialogInterface dialog) {
//                                        handler.removeCallbacks(runnable);
//                                    }
//                                });
//
//                                handler.postDelayed(runnable, 1000);
//
//
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
//
//                                        System.out.println("read Success expiry");
//
//                                    } else {
////                                    Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
////                                            .show();
//                                        System.out.println("Read Failed");
//                                    }
//
//                                } else {
////                                Toast.makeText(context, "Read Authentication Failed expiry",
////                                        Toast.LENGTH_SHORT).show();
//                                    System.out.println("Authentication Failed");
//                                }
//
//                                String expdate = String.valueOf(read_expiry);
//                                String expdate1 = expdate.replaceFirst("^0*", "");
//                                System.out.println("CHILLRRRR:1 " + expdate1);
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
//                                    ex.printStackTrace();
//                                    System.out.println("Parse exception");
//                                }
//
//
//                                if (BlocardsCheck(serial)) {
//
////                            finish();
////                                    AlertDialog.Builder alert = new AlertDialog.Builder(
////                                            AttendenceNFC_New.this);
////                                    alert.setTitle("Sorry");
////
////                                    alert.setCancelable(false);
////                                    alert.setMessage("Blocked Card!");
////
////                                    DialogInterface.OnClickListener listener;
////                                    alert.setPositiveButton("OK",
////                                            new DialogInterface.OnClickListener() {
////
////                                                @Override
////                                                public void onClick(
////                                                        DialogInterface dialog,
////                                                        int which) {
////
////                                                    dialog.dismiss();
////                                                    progressDialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                                                    Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                                                    Bundle b = new Bundle();
////                                                    b.putString("id", attend_typ);
////                                                    b.putStringArrayList("card_list", card_list);
////                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                                    i.putExtras(b);
////                                                    startActivity(i);
////                                                }
////                                            });
//////                                            alert.setNegativeButton("CANCEL",
//////                                                    new DialogInterface.OnClickListener() {
//////
//////                                                        @Override
//////                                                        public void onClick(
//////                                                                DialogInterface dialog,
//////                                                                int which) {
//////
//////                                                            dialog.dismiss();
//////                                                            finish();
//////                                                        }
//////                                                    });
////                                    alert.show();
//
//
//                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                                    dialog.setTitle("Sorry");
//
//                                    dialog.setCancelable(false);
//                                    dialog.setMessage("Blocked Card!");
//
//                                    DialogInterface.OnClickListener listener;
//
//                                    final AlertDialog alert = dialog.create();
//                                    alert.show();
//
////        alert.setPositiveButton("OK",
////                new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(
////                            DialogInterface dialog,
////                            int which) {
////
////                        dialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                        Bundle b=new Bundle();
////                        b.putString("id",attend_typ);
////                        b.putStringArrayList("card_list", card_list);
////                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        i.putExtras(b);
////                        startActivity(i);
////                    }
////                });
//
//                                    final Handler handler = new Handler();
//                                    final Runnable runnable = new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (alert.isShowing()) {
//                                                alert.dismiss();
//
//                                                Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                                                Bundle b = new Bundle();
//                                                b.putString("id", attend_typ);
//                                                b.putStringArrayList("card_list", card_list);
//                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                i.putExtras(b);
//                                                startActivity(i);
//                                            }
//                                        }
//                                    };
//
//                                    alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                        @Override
//                                        public void onDismiss(DialogInterface dialog) {
//                                            handler.removeCallbacks(runnable);
//                                        }
//                                    });
//
//                                    handler.postDelayed(runnable, 1000);
//
//
//                                } else {
//
//                                    if (schId.replace(" ", "").equalsIgnoreCase(db.getSchoolId())) {
//
////                                        if (today1.after(expirydate)) {
////
////                                            System.out.println("ELDHO: card expired");
//////                                        Toast.makeText(getApplicationContext(), "Card already expired. ", Toast.LENGTH_LONG).show();
//////                                    finish();
////
//////                                            AlertDialog.Builder alert = new AlertDialog.Builder(
//////                                                    AttendenceNFC_New.this);
//////                                            alert.setTitle("Sorry");
//////
//////                                            alert.setCancelable(false);
//////                                            alert.setMessage("Card Expired!");
//////
//////                                            DialogInterface.OnClickListener listener;
//////                                            alert.setPositiveButton("OK",
//////                                                    new DialogInterface.OnClickListener() {
//////
//////                                                        @Override
//////                                                        public void onClick(
//////                                                                DialogInterface dialog,
//////                                                                int which) {
//////
//////                                                            dialog.dismiss();
//////                                                            progressDialog.dismiss();
////////                                                            printBill(resultCurrenBal,resultPrevbal);
////////                                                            WritetoDB();
////////                                                            onTapCard();
//////                                                            Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//////                                                            Bundle b = new Bundle();
//////                                                            b.putString("id", attend_typ);
//////                                                            b.putStringArrayList("card_list", card_list);
//////                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////                                                            i.putExtras(b);
//////                                                            startActivity(i);
//////                                                        }
//////                                                    });
////////                                            alert.setNegativeButton("CANCEL",
////////                                                    new DialogInterface.OnClickListener() {
////////
////////                                                        @Override
////////                                                        public void onClick(
////////                                                                DialogInterface dialog,
////////                                                                int which) {
////////
////////                                                            dialog.dismiss();
////////                                                            finish();
////////                                                        }
////////                                                    });
//////                                            alert.show();
////
////
////                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
////                                            dialog.setTitle("Sorry");
////
////                                            dialog.setCancelable(false);
////                                            dialog.setMessage("Card Expired!");
////
////                                            DialogInterface.OnClickListener listener;
////
////                                            final AlertDialog alert = dialog.create();
////                                            alert.show();
////
//////        alert.setPositiveButton("OK",
//////                new DialogInterface.OnClickListener() {
//////
//////                    @Override
//////                    public void onClick(
//////                            DialogInterface dialog,
//////                            int which) {
//////
//////                        dialog.dismiss();
////////                                                            printBill(resultCurrenBal,resultPrevbal);
////////                                                            WritetoDB();
////////                                                            onTapCard();
//////                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//////                        Bundle b=new Bundle();
//////                        b.putString("id",attend_typ);
//////                        b.putStringArrayList("card_list", card_list);
//////                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////                        i.putExtras(b);
//////                        startActivity(i);
//////                    }
//////                });
////
////                                            final Handler handler  = new Handler();
////                                            final Runnable runnable = new Runnable() {
////                                                @Override
////                                                public void run() {
////                                                    if (alert.isShowing()) {
////                                                        alert.dismiss();
////
////                                                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                                                        Bundle b=new Bundle();
////                                                        b.putString("id",attend_typ);
////                                                        b.putStringArrayList("card_list", card_list);
////                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                                        i.putExtras(b);
////                                                        startActivity(i);
////                                                    }
////                                                }
////                                            };
////
////                                            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
////                                                @Override
////                                                public void onDismiss(DialogInterface dialog) {
////                                                    handler.removeCallbacks(runnable);
////                                                }
////                                            });
////
////                                            handler.postDelayed(runnable, 1000);
////
////
////                                        } else {
//
//                                        readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_FOURTH_SECT, key4, KEYA);
//
//                                        if (readauthsuc2 == 0) {
//
//                                            readsuc1 = apis.tama_read_target(READ_IN_OUT
//                                                    , read_inout);
//                                            readsuc2 = apis.tama_read_target(READ_ATTND_TYPE
//                                                    , read_type);
////
//
//
//                                            if (readsuc1 == 0) {
////                                    Toast.makeText(context, "Read Success Inout "+read_inout,
////                                            Toast.LENGTH_SHORT).show();
//                                                System.out.println("read Success");
//
//
//                                            } else {
//                                                Toast.makeText(context, "Read Failed inout", Toast.LENGTH_SHORT)
//                                                        .show();
//                                                System.out.println("Read Failed");
//                                            }
//
//                                        } else {
//                                            Toast.makeText(context, "Read Authentication Failed inout",
//                                                    Toast.LENGTH_SHORT).show();
//                                            System.out.println("Authentication Failed");
//                                        }
//                                        String in_out = String.valueOf(read_inout);
//                                        String type1 = String.valueOf(read_type);
//
//                                        typeattend = Integer.parseInt(type1);
//
//
//                                        readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                        if (readauthsuc3 == 0) {
//                                            readsuc3 = apis.tama_read_target(
//                                                    READ_CURR_BAL, read_current);
//                                            readsuc14 = apis.tama_read_target(READ_ATTND_DATE
//                                                    , read_date);
//
//
//                                            if (readsuc3 == 0) {
////                                            Toast.makeText(context, "Read Success current bal"+read_current,
////                                                    Toast.LENGTH_SHORT).show();
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
//                                        String att_date;
//                                        try {
//                                            att_date = String.valueOf(read_date);
//                                            if (att_date.equals("0000000000000000")) {
//                                                a_date = getcurrentdatetime();
//                                                int len3 = 16 - a_date.length();
//                                                for (int j = 0; j < len3; ++j) {
//
//                                                    filler5 = filler5 + "0";
//
//                                                }
//
//                                                att_date = filler5 + a_date;
//                                                System.out.println("test att_date" + att_date);
//                                                System.out.println("test att_date" + getcurrentdatetime());
//                                                char[] attent_date = att_date.toCharArray();
//
//                                                authsuc7 = apis.tama_authenticate_mifare(TARGET_NUM, AUTH_THIRD_SECT, key3, KEYA);
//                                                if (authsuc7 == 0) {
//
////                                            Toast.makeText(context, "Authentication Success2",
////                                                    Toast.LENGTH_SHORT).show();
//
//
//                                                    writesuc14 = apis.tama_write_target(WRITE_ATTND_DATE,
//                                                            attent_date, attent_date.length);
//
//
////                                    Toast.makeText(context, "writesuc "+writesuc1,
////                                            Toast.LENGTH_SHORT).show();
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Authentication FAILED2",
//                                                            Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            } else {
//                                                System.out.println("att_date att_date att_date   : " + att_date);
//                                                String expdate12 = String.valueOf(att_date);
//                                                String expdate112 = expdate12.replaceFirst("^0*", "");
//                                                System.out.println("CHILLRRRR:1  test " + expdate112);
//
//                                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
//
//                                                att_date_check = sdf1.parse(expdate112);
//                                                today1 = sdf1.parse(currentDateTimeString1);
//
//
//                                                System.out.println("CHILLRRRR: test " + att_date_check);
//                                                System.out.println("CHILLRRRR: today  test " + today1);
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            a_date = getcurrentdatetime();
//                                            int len3 = 16 - a_date.length();
//                                            for (int j = 0; j < len3; ++j) {
//
//                                                filler3 = filler3 + "0";
//
//                                            }
//
//                                            att_date = filler3 + a_date;
//                                            System.out.println("test att_date" + att_date);
//                                            System.out.println("test att_date" + getcurrentdatetime());
//                                            char[] attent_date = att_date.toCharArray();
//
//                                            authsuc7 = apis.tama_authenticate_mifare(TARGET_NUM, AUTH_THIRD_SECT, key3, KEYA);
//                                            if (authsuc7 == 0) {
//
////                                            Toast.makeText(context, "Authentication Success2",
////                                                    Toast.LENGTH_SHORT).show();
//
//
//                                                writesuc14 = apis.tama_write_target(WRITE_ATTND_DATE,
//                                                        attent_date, attent_date.length);
//
//
////                                    Toast.makeText(context, "writesuc "+writesuc1,
////                                            Toast.LENGTH_SHORT).show();
//
//
//                                            } else {
//                                                Toast.makeText(context, "Authentication FAILED2",
//                                                        Toast.LENGTH_SHORT).show();
//
//                                            }
//
//                                        }
//
//
//                                        System.out.println("NewChillr : " + currnt);
//
//                                        previousbalnce = Float.valueOf(currnt);
//                                        System.out.println("NewChillr : " + previousbalnce);
//
//
//                                        int attTyp = Integer.parseInt(type1);
//
//
//                                        String filler = "";
//
//
//                                        int len = 16 - attend_typ.length();
//                                        for (int j = 0; j < len; ++j) {
//
//                                            filler = filler + "0";
//
//                                        }
//
//                                        //                        String currentbal= String.valueOf(Current);
//                                        String data = filler + attend_typ;
//
//                                        System.out.println("ijaz :  CHILLAR: Filler: data : " + data);
//                                        System.out.println("ijaz :  CHILLAR: Filler: type1 : " + type1);
//
//                                        if (type1.equals(data)) {
//                                            System.out.println("CHILLAR: Filler: today1 : " + today1);
//                                            System.out.println("CHILLAR: Filler: att_date_check : " + att_date_check);
//                                            if (!today1.after(att_date_check)) {
//
//                                                System.out.println("CHILLAR: Filler: in_out : " + in_out);
//
//                                                if (in_out.equals("0000000000000000")) {
//                                                    inout = "0000000000000001";
//                                                    a_date = getcurrentdatetime();
//                                                    int len3 = 16 - a_date.length();
//                                                    for (int j = 0; j < len3; ++j) {
//
//                                                        filler3 = filler3 + "0";
//
//                                                    }
//
//                                                    att_date = filler3 + a_date;
//
//
//                                                    cardwrite(inout, att_date, at_type);
//
//                                                } else if (in_out.equals("0000000000000001")) {
//
//                                                    inout = "0000000000000000";
//                                                    a_date = getcurrentdatetime();
//                                                    int len3 = 16 - a_date.length();
//                                                    for (int j = 0; j < len3; ++j) {
//
//                                                        filler3 = filler3 + "0";
//
//                                                    }
//
//                                                    att_date = filler3 + a_date;
//
//                                                    cardwrite(inout, att_date, at_type);
//                                                }
//                                            } else {
//                                                inout = "0000000000000001";
//                                                a_date = getcurrentdatetime();
//                                                int len3 = 16 - a_date.length();
//                                                for (int j = 0; j < len3; ++j) {
//
//                                                    filler3 = filler3 + "0";
//
//                                                }
//
//                                                att_date = filler3 + a_date;
//
//                                                cardwrite(inout, att_date, at_type);
//
//                                            }
//                                        } else {
//
//                                            System.out.println("CHILLAR: Filler:    in_out" + in_out);
//                                            if (in_out.equals("0000000000000000")) {
//                                                inout = "0000000000000001";
//
//                                                a_date = getcurrentdatetime();
//                                                int len3 = 16 - a_date.length();
//                                                for (int j = 0; j < len3; ++j) {
//
//                                                    filler3 = filler3 + "0";
//
//                                                }
//
//                                                att_date = filler3 + a_date;
//
//                                                System.out.println("test att_date" + att_date);
//                                                cardwrite(inout, att_date, at_type);
//                                            } else if (in_out.equals("0000000000000001")) {
//                                                if (!today1.after(att_date_check)) {
//                                                    Toast.makeText(context, "Checkout from " + db.getAttTypbyID(String.valueOf(attTyp)) + " first!",
//                                                            Toast.LENGTH_SHORT).show();
//
//                                                    alertbox("Checkout from " + db.getAttTypbyID(String.valueOf(attTyp)) + " first!");
//
//                                                } else {
//                                                    a_date = getcurrentdatetime();
//                                                    int len3 = 16 - a_date.length();
//                                                    for (int j = 0; j < len3; ++j) {
//
//                                                        filler3 = filler3 + "0";
//
//                                                    }
//
//                                                    att_date = filler3 + a_date;
//                                                    inout = "0000000000000001";
//
//                                                    cardwrite(inout, att_date, at_type);
//                                                }
//                                            }
//
//                                        }
//
//                                        //check
//
//
////                                            if (type1.equals("0000000000000000")) {
////
////                                                inout = "0000000000000001";
////
////                                                char[] type = data.toCharArray();
////                                                char[] inout1 = inout.toCharArray();
////
////
////                                                authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                                        AUTH_FOURTH_SECT, key4, KEYA);
////
////
////                                                System.out.println("Authentication Success");
////
////
////                                                if (authsuc1 == 0) {
////
//////                                            Toast.makeText(context, "Authentication Success2",
//////                                                    Toast.LENGTH_SHORT).show();
////
////                                                    writesuc1 = apis.tama_write_target(WRITE_ATTND_TYPE,
////                                                            type, type.length);
////
////                                                    writesuc2 = apis.tama_write_target(WRITE_IN_OUT,
////                                                            inout1, inout1.length);
////
////
//////                                                    Toast.makeText(context, "writesuc " + writesuc1,
//////                                                            Toast.LENGTH_SHORT).show();
////
////
////                                                } else {
////
////                                                    Toast.makeText(context, "Authentication FAILED2",
////                                                            Toast.LENGTH_SHORT).show();
////
////                                                }
////
////
//////                                        Toast.makeText(context, writesuc1+","+writesuc2,
//////                                                Toast.LENGTH_SHORT).show();
////
////
////                                                if ((writesuc1 == 0) && (writesuc2 == 0)) {
////
////                                                    Attendence();
////                                                    Toast.makeText(context, "SUCCESSSSS!!!!!!",
////                                                            Toast.LENGTH_SHORT).show();
////
////                                                    alertbox1("SUCCESS");
////
//////                                                Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
//////
//////                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////                                                startActivity(i);
//////                                                finish();
////
////                                                }
////
////
////                                            }
////                                            else if (type1.equals(data)) {
////                                                if (in_out.equals("0000000000000000")) {
////                                                    inout = "0000000000000001";
////                                                } else if (in_out.equals("0000000000000001")) {
////
////                                                    inout = "0000000000000000";
////                                                }
////
////                                                char[] type = data.toCharArray();
////                                                char[] inout1 = inout.toCharArray();
////
////
////                                                authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                                        AUTH_FOURTH_SECT, key4, KEYA);
////
////
////                                                System.out.println("Authentication Success");
////
////
////                                                if (authsuc1 == 0) {
////
//////                                            Toast.makeText(context, "Authentication Success2",
//////                                                    Toast.LENGTH_SHORT).show();
////
////                                                    writesuc1 = apis.tama_write_target(WRITE_ATTND_TYPE,
////                                                            type, type.length);
////
////                                                    writesuc2 = apis.tama_write_target(WRITE_IN_OUT,
////                                                            inout1, inout1.length);
////
////
//////                                    Toast.makeText(context, "writesuc "+writesuc1,
//////                                            Toast.LENGTH_SHORT).show();
////
////
////                                                } else {
////
////                                                    Toast.makeText(context, "Authentication FAILED2",
////                                                            Toast.LENGTH_SHORT).show();
////
////                                                }
////
////
//////                                Toast.makeText(context, writesuc1+","+writesuc2,
//////                                        Toast.LENGTH_SHORT).show();
////
////
////                                                if ((writesuc1 == 0) && (writesuc2 == 0)) {
////
////                                                    Attendence();
//////                                                Toast.makeText(context, "SUCCESSSSS!!!!!!",
//////                                                        Toast.LENGTH_SHORT).show();
////
////                                                    alertbox1("SUCCESS");
//////                                                Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
//////
//////                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////                                                startActivity(i);
//////                                                finish();
////
////                                                }
////
////
////                                            }
////                                            else if (in_out.equals("0000000000000000")) {
////
////                                                inout = "0000000000000001";
////
////                                                char[] type = data.toCharArray();
////                                                char[] inout1 = inout.toCharArray();
////
////
////                                                authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                                        AUTH_FOURTH_SECT, key4, KEYA);
////
////
////                                                System.out.println("Authentication Success");
////
////
////                                                if (authsuc1 == 0) {
////
//////                                                    Toast.makeText(context, "Authentication Success2",
//////                                                            Toast.LENGTH_SHORT).show();
////
////                                                    writesuc1 = apis.tama_write_target(WRITE_ATTND_TYPE,
////                                                            type, type.length);
////
////                                                    writesuc2 = apis.tama_write_target(WRITE_IN_OUT,
////                                                            inout1, inout1.length);
////
////
//////                                        Toast.makeText(context, "writesuc "+writesuc1,
//////                                                Toast.LENGTH_SHORT).show();
////
////
////                                                } else {
////
////                                                    Toast.makeText(context, "Authentication FAILED2",
////                                                            Toast.LENGTH_SHORT).show();
////
////                                                }
////
////
//////                                    Toast.makeText(context, writesuc1+","+writesuc2,
//////                                            Toast.LENGTH_SHORT).show();
////
////
////                                                if ((writesuc1 == 0) && (writesuc2 == 0)) {
////
////
////                                                    Attendence();
////
////
//////                                                Toast.makeText(context, "SUCCESSSSS!!!!!!",
//////                                                        Toast.LENGTH_SHORT).show();
////                                                    alertbox1("SUCCESS");
////
//////                                                    Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
//////
//////                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////                                                    startActivity(i);
//////                                                finish();
////
////
////                                                }
////
////
////                                            }
////                                            else {
////
////
////                                                Toast.makeText(context, "Checkout from " + db.getAttTypbyID(String.valueOf(attTyp)) + " first!",
////                                                        Toast.LENGTH_SHORT).show();
////
////                                                alertbox("Checkout from " + db.getAttTypbyID(String.valueOf(attTyp)) + " first!");
//////                                            finish();
////                                            }
//
//
//                                        //check
////                                        }
//                                    } else {
////                                    Toast.makeText(context, "Card not belonging to this temple!!",
////                                            Toast.LENGTH_SHORT).show();
////                                finish();
////                                        AlertDialog.Builder alert = new AlertDialog.Builder(
////                                                AttendenceNFC_New.this);
////                                        alert.setTitle("Sorry");
////
////                                        alert.setCancelable(false);
////                                        alert.setMessage("Card not belonging to this School!!");
////
////                                        DialogInterface.OnClickListener listener;
////                                        alert.setPositiveButton("OK",
////                                                new DialogInterface.OnClickListener() {
////
////                                                    @Override
////                                                    public void onClick(
////                                                            DialogInterface dialog,
////                                                            int which) {
////
////                                                        dialog.dismiss();
////                                                        progressDialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                                                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                                                        Bundle b = new Bundle();
////                                                        b.putString("id", attend_typ);
////                                                        b.putStringArrayList("card_list", card_list);
////                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                                        i.putExtras(b);
////                                                        startActivity(i);
////                                                    }
////                                                });
//////                                            alert.setNegativeButton("CANCEL",
//////                                                    new DialogInterface.OnClickListener() {
//////
//////                                                        @Override
//////                                                        public void onClick(
//////                                                                DialogInterface dialog,
//////                                                                int which) {
//////
//////                                                            dialog.dismiss();
//////                                                            finish();
//////                                                        }
//////                                                    });
////                                        alert.show();
//
//
//                                        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                                        dialog.setTitle("Sorry");
//
//                                        dialog.setCancelable(false);
//                                        dialog.setMessage("Card not belonging to this School!!");
//
//                                        DialogInterface.OnClickListener listener;
//
//                                        final AlertDialog alert = dialog.create();
//                                        alert.show();
//
////        alert.setPositiveButton("OK",
////                new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(
////                            DialogInterface dialog,
////                            int which) {
////
////                        dialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                        Bundle b=new Bundle();
////                        b.putString("id",attend_typ);
////                        b.putStringArrayList("card_list", card_list);
////                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        i.putExtras(b);
////                        startActivity(i);
////                    }
////                });
//
//                                        final Handler handler = new Handler();
//                                        final Runnable runnable = new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                if (alert.isShowing()) {
//                                                    alert.dismiss();
//
//                                                    Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                                                    Bundle b = new Bundle();
//                                                    b.putString("id", attend_typ);
//                                                    b.putStringArrayList("card_list", card_list);
//                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    i.putExtras(b);
//                                                    startActivity(i);
//                                                }
//                                            }
//                                        };
//
//                                        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                            @Override
//                                            public void onDismiss(DialogInterface dialog) {
//                                                handler.removeCallbacks(runnable);
//                                            }
//                                        });
//
//                                        handler.postDelayed(runnable, 1000);
//                                    }
//
//                                }
//
//                            }
//
//                        } else {
//                            alertbox("Card Not Found");
//                        }
////                        555655
//
//
//                    } catch (NumberFormatException e) {
//
//
////                        Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
////                                .show();
////                        AlertDialog.Builder alert = new AlertDialog.Builder(
////                                AttendenceNFC_New.this);
////                        alert.setTitle("RFID");
////
////                        alert.setCancelable(false);
////                        alert.setMessage("Target Moved.Try again.");
////
////                        DialogInterface.OnClickListener listener;
////                        alert.setPositiveButton(
////                                "OK",
////                                new DialogInterface.OnClickListener() {
////
////                                    @Override
////                                    public void onClick(
////                                            DialogInterface dialog,
////                                            int which) {
////
////                                        dialog.dismiss();
////                                        progressDialog.dismiss();
////
//////                                        onTapCard();
////                                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                                        Bundle b=new Bundle();
////                                        b.putString("id",attend_typ);
////                                        b.putStringArrayList("card_list", card_list);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        i.putExtras(b);
////                                        startActivity(i);
////
//////                                            Intent i=new Intent(getApplicationContext(),SecondActivity.class);
//////                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////                                            startActivity(i);
//////                                            finish();
//////                                    new FieldRestartTask().execute();
////
////                                    }
////                                });
////                        alert.show();
//
//
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                        dialog.setTitle("RFID");
//
//                        dialog.setCancelable(false);
//                        dialog.setMessage("Target Moved.Try again.");
//
//                        DialogInterface.OnClickListener listener;
//
//                        final AlertDialog alert = dialog.create();
//                        alert.show();
//
////        alert.setPositiveButton("OK",
////                new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(
////                            DialogInterface dialog,
////                            int which) {
////
////                        dialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                        Bundle b=new Bundle();
////                        b.putString("id",attend_typ);
////                        b.putStringArrayList("card_list", card_list);
////                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        i.putExtras(b);
////                        startActivity(i);
////                    }
////                });
//
//                        final Handler handler = new Handler();
//                        final Runnable runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                if (alert.isShowing()) {
//                                    alert.dismiss();
//
//                                    Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                                    Bundle b = new Bundle();
//                                    b.putString("id", attend_typ);
//                                    b.putStringArrayList("card_list", card_list);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    i.putExtras(b);
//                                    startActivity(i);
//                                }
//                            }
//                        };
//
//                        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                                handler.removeCallbacks(runnable);
//                            }
//                        });
//
//                        handler.postDelayed(runnable, 1000);
//
//                        System.out.println("Target detect Failed");
//                    } catch (NullPointerException e) {
//                        onTapCard();
////                        Toast.makeText(context, "Card Error!!",
////                                Toast.LENGTH_LONG).show();
////                    finish();
////                        AlertDialog.Builder alert = new AlertDialog.Builder(
////                                AttendenceNFC_New.this);
////                        alert.setTitle("Sorry");
////
////                        alert.setCancelable(false);
////                        alert.setMessage("Card Error!");
////
////                        DialogInterface.OnClickListener listener;
////                        alert.setPositiveButton("OK",
////                                new DialogInterface.OnClickListener() {
////
////                                    @Override
////                                    public void onClick(
////                                            DialogInterface dialog,
////                                            int which) {
////
////                                        dialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                                        Bundle b=new Bundle();
////                                        b.putString("id",attend_typ);
////                                        b.putStringArrayList("card_list", card_list);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        i.putExtras(b);
////                                        startActivity(i);
////                                    }
////                                });
//////                                            alert.setNegativeButton("CANCEL",
//////                                                    new DialogInterface.OnClickListener() {
//////
//////                                                        @Override
//////                                                        public void onClick(
//////                                                                DialogInterface dialog,
//////                                                                int which) {
//////
//////                                                            dialog.dismiss();
//////                                                            finish();
//////                                                        }
//////                                                    });
////                        alert.show();
//
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                        dialog.setTitle("Sorry");
//
//                        dialog.setCancelable(false);
////                        dialog.setMessage(success+" "+ msg);
//                        dialog.setMessage("Card Error!");
//
//                        DialogInterface.OnClickListener listener;
//
//                        final AlertDialog alert = dialog.create();
//                        alert.show();
//
////        alert.setPositiveButton("OK",
////                new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(
////                            DialogInterface dialog,
////                            int which) {
////
////                        dialog.dismiss();
//////                                                            printBill(resultCurrenBal,resultPrevbal);
//////                                                            WritetoDB();
//////                                                            onTapCard();
////                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
////                        Bundle b=new Bundle();
////                        b.putString("id",attend_typ);
////                        b.putStringArrayList("card_list", card_list);
////                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        i.putExtras(b);
////                        startActivity(i);
////                    }
////                });
//
//                        final Handler handler = new Handler();
//                        final Runnable runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                if (alert.isShowing()) {
//                                    alert.dismiss();
//
//                                    Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                                    Bundle b = new Bundle();
//                                    b.putString("id", attend_typ);
//                                    b.putStringArrayList("card_list", card_list);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    i.putExtras(b);
//                                    startActivity(i);
//                                }
//                            }
//                        };
//
//                        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                                handler.removeCallbacks(runnable);
//                            }
//                        });
//
//                        handler.postDelayed(runnable, 1000);
//
//                    } catch (StackOverflowError e) {
//                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                        Bundle b = new Bundle();
//                        b.putString("id", attend_typ);
//                        b.putStringArrayList("card_list", card_list);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.putExtras(b);
//                        startActivity(i);
//                    } catch (InflateException e) {
//                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                        Bundle b = new Bundle();
//                        b.putString("id", attend_typ);
//                        b.putStringArrayList("card_list", card_list);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.putExtras(b);
//                        startActivity(i);
//                    }
//                } else {
//
//                    onTapCard();
//                    System.out.println("Authentication Failed");
//                }
//
//            } else {
//
//                // onTapCard();
//                r = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        System.out.println("in handler");
//
//                        onTapCard();
//
//                    }
//                };
//
//                if (hand != null) {
//                    hand.removeCallbacks(r);
//                }
//
//                hand = new Handler();
//                hand.postDelayed(r, 10);
//
//            }
//        } catch (StackOverflowError e) {
//            Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//            Bundle b = new Bundle();
//            b.putString("id", attend_typ);
//            b.putStringArrayList("card_list", card_list);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.putExtras(b);
//            startActivity(i);
//        } catch (InflateException e) {
//            Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//            Bundle b = new Bundle();
//            b.putString("id", attend_typ);
//            b.putStringArrayList("card_list", card_list);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.putExtras(b);
//            startActivity(i);
//        }
//    }

//    private void cardwrite(String inout, String att_date, String at_type) {
//
//        a_type = at_type;
//        int len3 = 16 - a_type.length();
//        for (int j = 0; j < len3; ++j) {
//
//            filler4 = filler4 + "0";
//
//        }
//
//        String attet_type = filler4 + a_type;
//
//        char[] type = attet_type.toCharArray();
//        char[] inout1 = inout.toCharArray();
//        char[] attent_date = att_date.toCharArray();
//
//        authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                AUTH_FOURTH_SECT, key4, KEYA);
//
//
//        System.out.println("Authentication Success   test");
//
////        if (authsuc1 == 0) {
////
////            System.out.println("Authentication Success   test 1  ");
////
//////                                            Toast.makeText(context, "Authentication Success2",
//////                                                    Toast.LENGTH_SHORT).show();
////
////            writesuc1 = apis.tama_write_target(WRITE_ATTND_TYPE,
////                    type, type.length);
////            writesuc2 = apis.tama_write_target(WRITE_IN_OUT,
////                    inout1, inout1.length);
////
////            System.out.println("Authentication Success   test " + writesuc1);
////            System.out.println("Authentication Success   test " + writesuc2);
////
////
//////                                    Toast.makeText(context, "writesuc "+writesuc1,
//////                                            Toast.LENGTH_SHORT).show();
////
////
////        } else {
////            Toast.makeText(context, "Authentication FAILED2",
////                    Toast.LENGTH_SHORT).show();
////
////        }
//
//
////                                Toast.makeText(context, writesuc1+","+writesuc2,
////                                        Toast.LENGTH_SHORT).show();
////
////        authsuc7 = apis.tama_authenticate_mifare(TARGET_NUM, AUTH_THIRD_SECT, key3, KEYA);
////        if (authsuc7 == 0) {
////
////            System.out.println("Authentication Success   test  2");
////
//////                                            Toast.makeText(context, "Authentication Success2",
//////                                                    Toast.LENGTH_SHORT).show();
////
////
////            writesuc14 = apis.tama_write_target(WRITE_ATTND_DATE,
////                    attent_date, attent_date.length);
////
////            System.out.println("Authentication Success   test  " + writesuc14);
////
//////                                    Toast.makeText(context, "writesuc "+writesuc1,
//////                                            Toast.LENGTH_SHORT).show();
////
////
////        } else {
////            Toast.makeText(context, "Authentication FAILED2",
////                    Toast.LENGTH_SHORT).show();
////
////        }
//
//        if ((writesuc1 == 0) && (writesuc2 == 0) && (writesuc14 == 0)) {
//            Attendence();
////                                                Toast.makeText(context, "SUCCESSSSS!!!!!!",
////                                                        Toast.LENGTH_SHORT).show();
//
//            System.out.println("Authentication Success   test  5");
//
//            if (inout.equals("0000000000000001")) {
////                alertbox1("SUCCESS","IN");
//                alertbox1("SUCCESS", "");
//            } else if (inout.equals("0000000000000000")) {
////                alertbox1("SUCCESS","OUT");
//                alertbox1("SUCCESS", "");
//            }
////                                                Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
////
////                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                                startActivity(i);
////                                                finish();
//
//        }
//    }

    private boolean std_list(String serial) {
        System.out.println("serial1  :  " + serial);
        if (card_list.contains(serial)) {
            System.out.println("serial2  :  " + serial);
            return true;

        }

        System.out.println("serial3  :  " + serial);

        return false;

    }

    private void Attendence() {

        System.out.println("CHILLER: HERE");
        trans_id = db.lastTransID();
        currentbalance = previousbalnce;


        // Fee_Transaction fee=new Fee_Transaction(trans_id,1,10,previousbalnce,(float)currentbalance,cardserialnumber,"timestmp1","servertime1",db.getBillno(),amt,trans_id,"servertime1");

//        db.deleteAllSussecc();
//        db.deleteAllAttendancedata();


        Attendance_Data attendancedata = new Attendance_Data(trans_id, userid, transtype_id, previousbalnce, currentbalance, resultCardSerial,
                currentDateTimeString, "", Integer.parseInt(attend_typ), Integer.parseInt(inout), trans_id, "");
        db.addSuccesstransaction(attendancedata);
        db.addUserAttendancedata(attendancedata);


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
//                Toast.makeText(getApplicationContext(), "This card is blocked", Toast.LENGTH_SHORT).show();
                return true;
            }

        }


        return false;
    }

    public void alertbox(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SORRY");

        dialog.setCancelable(false);
        dialog.setMessage("Sorry," + " " + msg);

        DialogInterface.OnClickListener listener;

        final AlertDialog alert = dialog.create();
        alert.show();

//        alert.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(
//                            DialogInterface dialog,
//                            int which) {
//
//                        dialog.dismiss();
////                                                            printBill(resultCurrenBal,resultPrevbal);
////                                                            WritetoDB();
////                                                            onTapCard();
//                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                        Bundle b=new Bundle();
//                        b.putString("id",attend_typ);
//                        b.putStringArrayList("card_list", card_list);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.putExtras(b);
//                        startActivity(i);
//                    }
//                });

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();

                    Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
                    Bundle b = new Bundle();
                    b.putString("id", attend_typ);
                    b.putStringArrayList("card_list", card_list);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 1000);
    }

    public void alertbox1(String success, String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(AttendenceNFC_New.this);
        dialog.setTitle("SUCCESS");

        dialog.setCancelable(false);
        dialog.setMessage(success + " " + msg);

        DialogInterface.OnClickListener listener;

        final AlertDialog alert = dialog.create();


        try {
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
        }

//        alert.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(
//                            DialogInterface dialog,
//                            int which) {
//
//                        dialog.dismiss();
////                                                            printBill(resultCurrenBal,resultPrevbal);
////                                                            WritetoDB();
////                                                            onTapCard();
//                        Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
//                        Bundle b=new Bundle();
//                        b.putString("id",attend_typ);
//                        b.putStringArrayList("card_list", card_list);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.putExtras(b);
//                        startActivity(i);
//                    }
//                });

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.dismiss();
                        }
                    });
//                    alert.dismiss();

                    Intent i = new Intent(getApplicationContext(), AttendenceNFC_New.class);
                    Bundle b = new Bundle();
                    b.putString("id", attend_typ);
                    b.putStringArrayList("card_list", card_list);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 1000);
//                                            alert.setNegativeButton("CANCEL",
//                                                    new DialogInterface.OnClickListener() {
//
//                                                        @Override
//                                                        public void onClick(
//                                                                DialogInterface dialog,
//                                                                int which) {
//
//                                                            dialog.dismiss();
//                                                            finish();
//                                                        }
//                                                    });

    }

    public String getcurrentdate() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if (hand != null) {

            System.out.println("handle not null");
            hand.removeCallbacks(r);
        }
        if (hand1 != null) {
            System.out.println("handle1 not null");
            hand1.removeCallbacks(r1);
        }

        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
//        Intent i = getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//
//        super.onBackPressed();
//        finish();
        //        // Toast.makeText(getApplicationContext(), "backpress",2000).show();
//        return;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            System.out.println("In Wait Back Clicked");
            // setFinishOnTouchOutside(false);
            finish();
            // Toast.makeText(getApplicationContext(), "onkey",2000).show();
            return false;

        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        // onDestroy();
        finish();
        // Toast.makeText(getApplicationContext(), "back",2000).show();
        return false;
    }

    public String getcurrentdatetime() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }

}
