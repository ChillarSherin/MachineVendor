package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by Codmob on 20-07-16.
 */
public class OpenFirstSector extends Activity {
    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;

    public static final int READ_KEY1 = 3;
    public static final int READ_KEY2 = 7;
    public static final int READ_KEY3 = 11;
    public static final int READ_KEY4 = 15;
    public static final int READ_KEY5= 19;
    public static final int READ_KEY6 = 23;


    public static final int READ_SCHOOL_ID = 4;
    public static final int READ_CARD_SERIAL = 5;
    public static final int READ_STATUS = 6;
    public static final int READ_CURR_BAL = 8;
    public static final int READ_PREV_BAL = 9;
    public static final int READ_EXPIRY = 12;
    public static final int READ_IN_OUT = 13;
    public static final int READ_ATTND_TYPE = 14;
    public static final int READ_LASTTRANS_MC_ID = 16;
    public static final int READ_TRANS_ID = 17;
    public static final int READ_TRANS_AMT = 18;
    public static final int READ_LAST_RECH_MC_ID = 20;
    public static final int READ_LAST_RECH_ID = 21;
    public static final int READ_LAST_RECH_AMT = 22;



    public static final int WRITE_KEY1 = 3;
    public static final int WRITE_KEY2 = 7;
    public static final int WRITE_KEY3 = 11;
    public static final int WRITE_KEY4 = 15;
    public static final int WRITE_KEY5= 19;
    public static final int WRITE_KEY6 = 23;

    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;

    char[] key1 = { 0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e };
    //    char[] key1 = { 0x30, 0x30, 0x30, 0x30, 0x30, 0x30 };
//    char[] key2 = { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff };
    char[] key2 = { 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
    char[] key3 = { 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
    char[] key4 = { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
    char[] key5 = { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
    char[] key6 = { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };


    char[] Newkey1 = { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff };
    char[] Newkey2 = { 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
    char[] Newkey3 = { 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
    char[] Newkey4 = { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
    char[] Newkey5 = { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
    char[] Newkey6 = { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };


    StringBuffer read_serial = new StringBuffer();
    StringBuffer read_current = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();
    StringBuffer read_status = new StringBuffer();
    StringBuffer read_data5 = new StringBuffer();
    StringBuffer read_data7 = new StringBuffer();
    StringBuffer read_data8 = new StringBuffer();
    StringBuffer read_data9 = new StringBuffer();
    StringBuffer read_data10 = new StringBuffer();
    StringBuffer read_data11 = new StringBuffer();
    StringBuffer read_data12 = new StringBuffer();
    StringBuffer read_data13 = new StringBuffer();
    StringBuffer read_data14 = new StringBuffer();
    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();



    Context context;



    public Button read;

    public Button tap_the_card,go_back;

    String resultCardSerial,resultPrevbal,resultCurrenBal;

    
    int ret_poweron;
    int ret_fieldon;

    int tardtc;
    int newauthsuc1=-2,newauthsuc2=-2,newauthsuc3=-2,newauthsuc4=-2,newauthsuc5=-2,newauthsuc6=-2;
    int authsuc1=-2,authsuc2=-2,authsuc3=-2,authsuc4=-2,authsuc5=-2,authsuc6=-2,
            readauthsuc1=-2,readauthsuc2=-2,readauthsuc3=-2,readauthsuc4=-2,readauthsuc5=-2,readauthsuc6=-2;
    int writesuc1=-2,writesuc2=-2,writesuc3=-2,writesuc4=-2,writesuc5=-2,writesuc6=-2,writesuc7=-2,writesuc8=-2,writesuc9=-2,writesuc10=-2,writesuc11=-2,writesuc12=-2,writesuc13=-2;
    int readsuc0,readsuc1,readsuc2,readsuc3,readsuc4,readsuc5,readsuc6,readsuc7,readsuc8,readsuc9,readsuc10,readsuc11,readsuc12,readsuc13;
    int tama_on;

    int flag1=0,flag2=0,flag3=0,flag4=0,flag5=0,flag6=0;

    TextView cardsl,schIdd,balan,expiry1,statustxt,prevbal,attype,attinout,lasttrnsmcid,lasttransid,lasttransamt,lastrechmcid,lastrechid,lastrechamt;

    ProgressBar progressBar;

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        context = getApplicationContext();

        tap_the_card = (Button) findViewById(R.id.tap);
//        tap_the_card.setClickable(false);
//        tap_the_card.setEnabled(false);


        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

//        new FieldOnTask().execute();

        //To Write New Key
        final char[] chillkey1={ 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
        final char[] chillkey2={ 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
        final char[] chillkey3={ 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
        final char[] chillkey4={ 0x42, 0x21, 0x62, 0x40, 0x49, 0x23, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
        final char[] chillkey5={ 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
        final char[] chillkey6={ 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };


        //To write Old Key
//        final char[] chillkey1={ 0x41, 0x26, 0x72, 0x2a, 0x25, 0x3e ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
//        final char[] chillkey2={ 0x52, 0x34, 0x3f, 0x35, 0x21, 0x36 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey3={ 0x53, 0x23, 0x61, 0x38, 0x5e, 0x39 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey4={ 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey5={ 0x50, 0x24, 0x72, 0x25, 0x41, 0x5e ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey6={ 0x43, 0x25, 0x63, 0x31, 0x5e, 0x37 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };


        //ACTIVATE THE CARD USING CHILLAR KEY

//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //Checking if the RFID module field is ON
//                if (SecondActivity.ret_fieldon == 0) {
//
//
//                    System.out.println("Tama open :" + tama_on);
//
//                    //Detecting if there is any target card around
//                    tardtc = apis.tama_detectTarget(TARGET_NUM,
//                            target_detected, target_id);
//
//                    if (tardtc == 0) {
//
//                        //If there is any card around, trying to authenticate its firts sector with Default key FFFFF...
//                        try{
//
//
////						authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
////								AUTH_FIRST_SECT, key1, KEYA);
//
//                            authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FIRST_SECT, key1, KEYA);
//
//
//
//                            //If authenticating with Default key success, writing Chillar Key to it.
//                            if (authsuc1 == 0) {
//
//                                System.out.println("Authentication Success1321313");
//                                writesuc1 = apis.tama_write_target(WRITE_KEY1,
//                                        chillkey1, chillkey1.length);
//
//                                System.out.println("Authentication Success1321313 writesuc1 "+writesuc1);
//
//                            }
//                            else{
//
//
//                                flag1=1;
//
//                            }
//
//                            //Authenticating second sector with Default key to write Chillar Key to card.
//                            authsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_SECND_SECT, key2, KEYA);
//
//
//
//                            if(authsuc2==0) {
//
//
//                                writesuc2 = apis.tama_write_target(WRITE_KEY2,
//                                        chillkey2, chillkey2.length);
//
//                            }
//                            else{
//
//
//                                flag2=1;
//
//                            }
//
//                            authsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                            if(authsuc3 ==0) {
//
//
//                                writesuc3 = apis.tama_write_target(WRITE_KEY3,
//                                        chillkey3, chillkey3.length);
//
//
//
//                            }
//                            else{
//
//
//                                flag3=1;
//
//                            }
//
//                            authsuc4 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                            if(authsuc4==0) {
//
//                                writesuc4 = apis.tama_write_target(WRITE_KEY4,
//                                        chillkey4, chillkey4.length);
//
//                            }
//                            else{
//
//                                flag4=1;
//
//                            }
//
//
//                            authsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                            if(authsuc5==0) {
//
//                                writesuc5 = apis.tama_write_target(WRITE_KEY5,
//                                        chillkey5, chillkey5.length);
//
//
//                            }
//                            else{
//
//
//                                flag5=1;
//
//                            }
//
//
//                            authsuc6 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_SIXTH_SECT, key6, KEYA);
//
//
//                            if(authsuc6==0) {
//
//                                writesuc6 = apis.tama_write_target(WRITE_KEY6,
//                                        chillkey6, chillkey6.length);
//
//
//                            }
//                            else{
//
//
//                                flag6=1;
//
//                            }
//
//                            System.out.println("DefaultKey "+authsuc1+" "+authsuc2+" "+authsuc3+" "+authsuc4+" "+authsuc5+" "+authsuc6 );
//
//
//
//                            if ((authsuc1 == 0) && (authsuc2 == 0) && (authsuc3 == 0) && (authsuc4 == 0) && (authsuc5 == 0) && (authsuc6 == 0) ) {
//
//
//                                Toast.makeText(getApplicationContext(),"Card Activated!",Toast.LENGTH_SHORT).show();
//
//
//
//                            }else {
//
//
//                                ChillarkeyAuth();
//
//
//                            }
////                        readauthsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                AUTH_SECND_SECT, Newkey2, KEYA);
////
////                        if (readauthsuc1 == 0) {
////
////                            readsuc0 = apis.tama_read_target(READ_SCHOOL_ID
////                                    , read_school);
////                            readsuc1 = apis.tama_read_target(
////                                    READ_CARD_SERIAL, read_serial);
////                            readsuc4=apis.tama_read_target(
////                                    READ_STATUS, read_status);
////
////
////
////                            if (readsuc1 == 0) {
//////                                Toast.makeText(context, "Read Success serial "+read_serial,
//////                                        Toast.LENGTH_SHORT).show();
////                                System.out.println("read Success "+read_status);
////
////                                String schId= String.valueOf(read_school);
//////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
//////                                Toast.LENGTH_LONG).show();
////
////                                String serial=String.valueOf(read_serial);
////                                resultCardSerial=serial;
////
////                                String status= String.valueOf(read_status);
////                                int status1= Integer.parseInt(status);
////
////                                schIdd.setText("");
////                                statustxt.setText("");
////                                cardsl.setText("");
////
////
////                            } else {
////                                Toast.makeText(context, "Read Failed serial", Toast.LENGTH_SHORT)
////                                        .show();
////                                System.out.println("Read Failed");
////                            }
////
////                        } else {
////                            Toast.makeText(context, "Read Authentication Failed serial",
////                                    Toast.LENGTH_SHORT).show();
////                            System.out.println("Authentication Failed");
////                        }
////
////
////
////
////
////
////
////                        readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                AUTH_THIRD_SECT, Newkey3, KEYA);
////
////
////                        if (readauthsuc2 == 0) {
////                            readsuc2 = apis.tama_read_target(
////                                    READ_CURR_BAL, read_current);
////                            readsuc5 = apis.tama_read_target(
////                                    READ_PREV_BAL, read_data5);
////
////
////                            if (readsuc2 == 0) {
//////                                        Toast.makeText(context, "Read Success current bal" + read_current,
//////                                                Toast.LENGTH_SHORT).show();
////                                System.out.println("read Success current bal");
////
////
////                                String curr = String.valueOf(read_current);
////                                resultCurrenBal=curr;
////                                int currnntt= Integer.parseInt(curr);
////
////                                String prevv= String.valueOf(read_data5);
////                                resultPrevbal=prevv;
////                                int previo= Integer.parseInt(prevv);
////
////                                balan.setText(currnntt+"");
////                                prevbal.setText(previo+"");
////
////
////                            } else {
////                                Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
////                                        .show();
////                                System.out.println("Read Failed");
////                            }
////
////                        } else {
////                            Toast.makeText(context, "Read Authentication Failed current bal",
////                                    Toast.LENGTH_SHORT).show();
////                            System.out.println("Authentication Failed");
////                        }
////
////
////                        readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                AUTH_FOURTH_SECT, Newkey4, KEYA);
////
////
////                        if (readauthsuc3 == 0) {
////                            readsuc3 = apis.tama_read_target(
////                                    READ_EXPIRY, read_expiry);
////                            readsuc7 = apis.tama_read_target(
////                                    READ_IN_OUT, read_data7);
////                            readsuc8 = apis.tama_read_target(
////                                    READ_ATTND_TYPE, read_data8);
////
////
////                            if (readsuc3 == 0) {
//////                                        Toast.makeText(context, "Read Success Expiry" + read_current,
//////                                                Toast.LENGTH_SHORT).show();
////                                System.out.println("read Success expiry");
////
////                                String expir= String.valueOf(read_expiry);
////                                String inoutatt= String.valueOf(read_data7);
////                                String atttyp= String.valueOf(read_data8);
////
////                                int inout1= Integer.parseInt(inoutatt);
////                                int atttyp1= Integer.parseInt(atttyp);
////                                expir.replaceFirst("0", "");
////                                expiry1.setText(expir);
////                                attinout.setText(inout1+"");
////                                attype.setText(atttyp1+"");
////
////
////                            } else {
////                                Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
////                                        .show();
////                                System.out.println("Read Failed");
////                            }
////
////                        } else {
////                            Toast.makeText(context, "Read Authentication Failed expiry",
////                                    Toast.LENGTH_SHORT).show();
////                            System.out.println("Authentication Failed");
////                        }
////
////
////
////
////                        readauthsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                AUTH_FIFTH_SECT, Newkey5, KEYA);
////
////
////                        if (readauthsuc5 == 0) {
////                            readsuc9 = apis.tama_read_target(
////                                    READ_LASTTRANS_MC_ID, read_data9);
////                            readsuc10 = apis.tama_read_target(
////                                    READ_TRANS_ID, read_data10);
////                            readsuc11 = apis.tama_read_target(
////                                    READ_TRANS_AMT, read_data11);
////
////                            if (readsuc9 == 0) {
//////                                    Toast.makeText(context, "Read Success LAst trans mc ID "+read_data9,
//////                                            Toast.LENGTH_SHORT).show();
//////                                    Toast.makeText(context, "Read Success last trans ID "+read_data10,
//////                                            Toast.LENGTH_SHORT).show();
//////                                    Toast.makeText(context, "Read Success Last Trans amt "+read_data11,
//////                                            Toast.LENGTH_SHORT).show();
////                                System.out.println("read Success");
////
////                                String lasttransmc= String.valueOf(read_data9);
////                                String lasttrnsid= String.valueOf(read_data10);
////                                String lasttrnsamt= String.valueOf(read_data11);
////
////                                Float lasttrnsamt1= Float.parseFloat(lasttrnsamt);
////
////                                lasttrnsmcid.setText(lasttransmc);
////                                lasttransid.setText(lasttrnsid);
////                                lasttransamt.setText(lasttrnsamt1+"");
////
////
////                            } else {
////                                Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
////                                        .show();
////                                System.out.println("Read Failed");
////                            }
////
////
////
////
////
////                        } else {
////                            Toast.makeText(context, "Authentication Failed",
////                                    Toast.LENGTH_SHORT).show();
////                            System.out.println("Authentication Failed");
////                        }
//////
////                        readauthsuc6 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                AUTH_SIXTH_SECT, Newkey6, KEYA);
////
////
////                        if (readauthsuc6 == 0) {
////                            readsuc12 = apis.tama_read_target(
////                                    READ_LAST_RECH_MC_ID, read_data12);
////                            readsuc13 = apis.tama_read_target(
////                                    READ_LAST_RECH_ID, read_data13);
////                            readsuc13 = apis.tama_read_target(
////                                    READ_LAST_RECH_AMT, read_data14);
////
////                            if (readsuc12 == 0) {
//////                                    Toast.makeText(context, "Read Success  last rech mc id "+read_data12,
//////                                            Toast.LENGTH_SHORT).show();
//////                                    Toast.makeText(context, "Read Success last rech Id "+read_data13,
//////                                            Toast.LENGTH_SHORT).show();
//////                                    Toast.makeText(context, "Read Success last rech amt "+read_data14,
//////                                            Toast.LENGTH_SHORT).show();
////                                System.out.println("read Success");
////
////                                String lastmc= String.valueOf(read_data12);
////                                String lastrecid= String.valueOf(read_data13);
////                                String lastrecamt= String.valueOf(read_data14);
////
////                                Float lastrecamt1= Float.parseFloat(lastrecamt);
////
////                                lastrechmcid.setText(lastmc);
////                                lastrechid.setText(lastrecid);
////                                lastrechamt.setText(lastrecamt1+"");
////
////
////                            } else {
////                                Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
////                                        .show();
////                                System.out.println("Read Failed");
////                            }
////
////
////
////
////
////                        } else {
////                            Toast.makeText(context, "Authentication Failed",
////                                    Toast.LENGTH_SHORT).show();
////                            System.out.println("Authentication Failed");
////                        }
//                        }catch(NumberFormatException e){
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//
//
//
//                            Toast.makeText(context, "Target Moved. Try Again", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    OpenFirstSector.this);
//                            alert.setTitle("RFID");
//
//                            alert.setCancelable(false);
//                            alert.setMessage("Target Moved. Try Again");
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
////                                            Intent i=new Intent(getApplicationContext(),SecondActivity.class);
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
//                        }
//
//                    } else {
//
//                        tap_the_card.setClickable(false);
//                        tap_the_card.setEnabled(false);
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                OpenFirstSector.this);
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
////
////                                        Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        startActivity(i);
////                                        finish();
//
//
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

//        go_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                finish();
//            }
//        });

    }

    @Override
    public void onBackPressed() {

        if( Constants.AsyncFlag==0){
            Toast.makeText(getApplicationContext(),"Please Wait..",Toast.LENGTH_SHORT).show();
        }else {
            Constants.AsyncFlag=-1;
//            new FieldOffTask().execute();

            super.onBackPressed();
        }


    }

//    private void ChillarkeyAuth() {
//
//        System.out.println("Tama open :" + tama_on);
//
//        tardtc = apis.tama_detectTarget(TARGET_NUM,
//                target_detected, target_id);
//
//        if (tardtc == 0) {
//
//            newauthsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                    AUTH_FIRST_SECT, Newkey1, KEYA);
//
//            System.out.println("ChillarDefaultKey : "+newauthsuc1);
//
//            newauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                    AUTH_SECND_SECT, Newkey2, KEYA);
//
//            newauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                    AUTH_THIRD_SECT, Newkey3, KEYA);
//
//            newauthsuc4 = apis.tama_authenticate_mifare(TARGET_NUM,
//                    AUTH_FOURTH_SECT, Newkey4, KEYA);
//
//            newauthsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                    AUTH_FIFTH_SECT, Newkey5, KEYA);
//
//            newauthsuc6 = apis.tama_authenticate_mifare(TARGET_NUM,
//                    AUTH_SIXTH_SECT, Newkey6, KEYA);
//
//
//            System.out.println("ChillarKey "+newauthsuc1+" "+newauthsuc2+" "+newauthsuc3+" "+newauthsuc4+" "+newauthsuc5+" "+newauthsuc6 );
//
//            if ((newauthsuc1 == 0) && (newauthsuc2 == 0) && (newauthsuc3 == 0) && (newauthsuc4 == 0) &&
//                    (newauthsuc5 == 0) && (newauthsuc6 == 0)) {
//                Toast.makeText(getApplicationContext(), "Card Already Activated !", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
//
//
//    private class FieldRestartTask extends AsyncTask<Void,Void,Void>{
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
//                    ret_fieldon =SecondActivity.rfidService.rfid_rffield_on();
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
//            Constants.AsyncFlag=0;
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//
//            Constants.AsyncFlag=1;
//            progressBar.setVisibility(View.GONE);
//            tap_the_card.setClickable(true);
//            tap_the_card.setEnabled(true);
//        }
//    }
//
//    private class FieldOnTask extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            try {
//                ret_poweron = SecondActivity.rfidService.rfid_poweron();
//                System.out.println("RFID PowerOn :" + ret_poweron);
//
//                if (ret_poweron == 0 || ret_poweron == 2) {
//
////                Toast.makeText(getApplicationContext(), "Module open success", Toast.LENGTH_SHORT).show();
//
//                    ret_fieldon =SecondActivity.rfidService.rfid_rffield_on();
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
//
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            progressBar.setVisibility(View.GONE);
//            tap_the_card.setClickable(true);
//            tap_the_card.setEnabled(true);
//        }
//    }
//
//    private class FieldOffTask extends AsyncTask<Void,Void,Void> {
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
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
////        @Override
////        protected void onPreExecute() {
////
////            progressBar.setVisibility(View.VISIBLE);
////        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
////            progressBar.setVisibility(View.GONE);
//            tap_the_card.setClickable(true);
//            tap_the_card.setEnabled(true);
//        }
//    }




}
