package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.chillarcards.machinevendor.ModelClass.Recharge_Data;


public class MachineBlkTrnsferNFC extends Activity {
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
    public static final int READ_LASTTRANS_MC_ID = 16;
    public static final int READ_TRANS_ID = 17;
    public static final int READ_TRANS_AMT = 18;
    public static final int READ_LAST_RECH_MC_ID = 20;
    public static final int READ_LAST_RECH_ID = 21;
    public static final int READ_LAST_RECH_AMT = 22;
    public static final int READ_ATTND_DATE = 10;


    public static final int WRITE_STUDENT_CODE = 1;
    public static final int WRITE_SCHOOL_ID = 4;
    public static final int WRITE_CARD_SERIAL = 5;
    public static final int WRITE_STATUS = 6;
    public static final int WRITE_CURR_BAL = 8;
    public static final int WRITE_PREV_BAL = 9;
    public static final int WRITE_EXPIRY = 12;
    public static final int WRITE_IN_OUT = 13;
    public static final int WRITE_ATTND_TYPE = 14;
    public static final int WRITE_LASTTRANS_MC_ID = 16;
    public static final int WRITE_TRANS_ID = 17;
    public static final int WRITE_TRANS_AMT = 18;
    public static final int WRITE_LAST_RECH_MC_ID = 20;
    public static final int WRITE_LAST_RECH_ID = 21;
    public static final int WRITE_LAST_RECH_AMT = 22;
    public static final int WRITE_ATTND_DATE = 10;

    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;

    char[] key1 = { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
    char[] oldkey1 = { 0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e };
    char[] key2 = { 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
    char[] key3 = { 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
    char[] key4 = { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
    char[] key5 = { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
    char[] key6 = { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };


    StringBuffer read_data1 = new StringBuffer();
    StringBuffer read_data2 = new StringBuffer();
    StringBuffer read_data3 = new StringBuffer();
    StringBuffer read_data4 = new StringBuffer();
    StringBuffer read_data6 = new StringBuffer();


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
    StringBuffer read_date = new StringBuffer();


    StringBuffer read_data = new StringBuffer();



    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();



//    char[] write_data = {  '0', '0', '0', '0','4', '1', '2', '6', '7', '2', '2', 'a', '2', '3',
//            '3', 'e' };

    String resultCardSerial,resultPrevbal,resultCurrenBal;

    Context context;



    public Button read;

    public Button tap_the_card,go_back;
    ProgressBar progressBar;
    
    
    int ret_poweron;
    int ret_fieldon;

    int tardtc,tardtc1;
    int authsuc1=-1,authsuc2,authsuc3,authsuc4,authsuc5,authsuc6,readauthsuc1,readauthsuc2,readauthsuc3,readauthsuc4,readauthsuc5,readauthsuc6;
    int writesuc01,writesuc1,writesuc2,writesuc3,writesuc4,writesuc5,writesuc6,writesuc7,writesuc8,writesuc9,writesuc10,writesuc11,writesuc12,writesuc13,writesuc14;
    int readsuc0,readsuc1,readsuc2,readsuc3,readsuc4,readsuc5,readsuc6,readsuc7,readsuc8,readsuc9,readsuc10,readsuc11,readsuc12,readsuc13,readsuc14;
    int tama_on;

    String exp,expire;

//    DatabaseHandler db = new DatabaseHandler(this);

    DatabaseHandler db ;
    String schoolID,serialNo,serial,serial1;
    TextView cardsl,schIdd,balan,expiry1,statustxt,prevbal,attype,attinout,lasttrnsmcid,lasttransid,lasttransamt,lastrechmcid,lastrechid,lastrechamt;

    String formattedDate,currentDateTimeString,transid,amount,amount1,studCode,studentCode;
    int transtype_id,userid;


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    private void CardInitialise() {

        String filler1 ="";
        String filler2 ="";
        String filler3 ="";
        String filler4 ="";


        int len1=16-db.getSchoolId().length();
        for(int j=0;j<len1;++j){

            filler1=filler1+" ";

        }

        schoolID=db.getSchoolId()+filler1;


        int len2=16-amount.length();
        for(int j=0;j<len2;++j){

            filler2=filler2+"0";

        }

        amount1=filler2+amount;



        System.out.println("CHILLARNFC serial111: "+ serial);


        int len3=16-exp.length();
        for(int j=0;j<len3;++j){

            filler3=filler3+"0";

        }

        expire=filler3+exp;



        int len4=16-studCode.length();
        for(int j=0;j<len4;++j){

            filler4=filler4+" ";

        }

        studentCode=filler4+studCode;



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_machine_blk_trnsfr_nfc);

        db = DatabaseHandler.getInstance(getApplicationContext());


        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        tap_the_card = (Button) findViewById(R.id.tap);
        go_back= (Button) findViewById(R.id.tapback);
//        tap_the_card.setClickable(false);
//        tap_the_card.setEnabled(false);

        //Switching ON RFID FIELD
//        new FieldOnTask().execute();

        context = getApplicationContext();

        cardsl= (TextView) findViewById(R.id.cardser);
        schIdd= (TextView) findViewById(R.id.schId);
        balan= (TextView) findViewById(R.id.balance);
        expiry1= (TextView) findViewById(R.id.expiry);
        statustxt= (TextView) findViewById(R.id.status);
        prevbal= (TextView) findViewById(R.id.prevbalance);
        attype= (TextView) findViewById(R.id.att_typ);
        attinout= (TextView) findViewById(R.id.inout);
        lasttrnsmcid= (TextView) findViewById(R.id.lst_trnsmcid);
        lasttransid= (TextView) findViewById(R.id.lst_trnsid);
        lasttransamt= (TextView) findViewById(R.id.lst_trnsamt);
        lastrechmcid= (TextView) findViewById(R.id.lst_rechmcid);
        lastrechid= (TextView) findViewById(R.id.lst_rechid);
        lastrechamt= (TextView) findViewById(R.id.lst_rechamt);


        serialNo=Constants.SerialNo;
        transtype_id=db.getTransTypID("RECHARGE");


        System.out.println("TRANS TYPE ID: "+transtype_id);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        serial1=editor.getString("machineserial","");
        userid= editor.getInt("userid",0);
        System.out.println("RFID: serial "+serialNo);



        Bundle b=getIntent().getExtras();
        serial=b.getString("cardsl");
        amount=b.getString("amount");
        studCode=b.getString("stdCode");



        System.out.println("AutoBlkTrnsfr "+serial+" "+amount);




        Calendar c= Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int nxtyear=cyear+1;

        System.out.println("CHILLRif: "+cyear);
        int cmonth = c.get(Calendar.MONTH)+1;

        if(cmonth<=5){
            exp="31/05/"+cyear;
            System.out.println("CHILLRif: "+exp);
        }else{
            exp="31/05/"+nxtyear;
            System.out.println("CHILLRelse: "+exp);
        }

        CardInitialise();




        final char[] studentCode1=new String(studentCode).toCharArray();
        final char[] schoolid=new String(schoolID).toCharArray();
        final char[] cardserial=new String(serial).toCharArray();
        final char[] status=new String("0000000000000001").toCharArray();
        final char[] curr_bal=new String(amount1).toCharArray();
        final char[] prev_bal=new String("0000000000000000").toCharArray();
        final char[] expiry=new String(expire).toCharArray();
        final char[] inout=new String("0000000000000000").toCharArray();
        final char[] att_type=new String("0000000000000000").toCharArray();
        final char[] last_trans_mc_id=new String("0000000000000000").toCharArray();
        final char[] last_trans_id=new String("0000000000000000").toCharArray();
        final char[] last_trans_amt=new String("0000000000000000").toCharArray();
        final char[] last_rech_mc_id=new String("0000000000000000").toCharArray();
        final char[] last_rech_id=new String("0000000000000000").toCharArray();
        final char[] last_rech_amt=new String("0000000000000000").toCharArray();
        final char[] att_date=new String("0000000000000000").toCharArray();




        //INITIALIZING THE CARD WHEN TAPPED ON THE tap_card_button


//        tap_the_card.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (SecondActivity.ret_fieldon == 0) {
//
//                    System.out.println("RFID Tama open :" + tama_on);
//
//                    tardtc = apis.tama_detectTarget(TARGET_NUM,
//                            target_detected, target_id);
//
//                    System.out.println("RFID tard: "+tardtc);
//                    if (tardtc == 0) {
//
//                        try{
//
//                            authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FIRST_SECT, key1, KEYA);
//
//                            if(authsuc1==0){
//
//                                writesuc01= apis.tama_write_target(WRITE_STUDENT_CODE,
//                                        studentCode1, studentCode1.length);
//                                System.out.println("ELdosdsa+ "+ studentCode1+" "+studentCode1.length);
//                                System.out.println("ELdos+ "+ authsuc1+" "+writesuc01);
//
//
//
//                                authsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_SECND_SECT, key2, KEYA);
//
//
//
//                                System.out.println("Authentication Success");
//
//
//
//
//                                if (authsuc2 == 0) {
//
////                            Toast.makeText(context, "Authentication Success2",
////                                    Toast.LENGTH_SHORT).show();
//
//                                    writesuc1 = apis.tama_write_target(WRITE_SCHOOL_ID,
//                                            schoolid, schoolid.length);
//
//                                    writesuc2 = apis.tama_write_target(WRITE_CARD_SERIAL,
//                                            cardserial, cardserial.length);
//
//                                    writesuc3 = apis.tama_write_target(WRITE_STATUS,
//                                            status, status.length);
////                            Toast.makeText(context, "writesuc "+writesuc1,
////                                    Toast.LENGTH_SHORT).show();
//
//
//                                }
//                                else{
//
//                                    Toast.makeText(context, "Authentication FAILED2",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//
//                                authsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//
//                                if(authsuc3==0) {
//
////                            Toast.makeText(context, "Authentication Success3",
////                                    Toast.LENGTH_SHORT).show();
//
//                                    writesuc4 = apis.tama_write_target(WRITE_CURR_BAL,
//                                            curr_bal, curr_bal.length);
//
//                                    writesuc5 = apis.tama_write_target(WRITE_PREV_BAL,
//                                            prev_bal, prev_bal.length);
//
//                                    writesuc14 = apis.tama_write_target(WRITE_ATTND_DATE,
//                                            att_date, att_date.length);
//                                }
//                                else{
//
//                                    Toast.makeText(context, "Authentication FAILED3",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//
//                                authsuc4 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                if(authsuc4 ==0) {
////                            Toast.makeText(context, "Authentication Success4",
////                                    Toast.LENGTH_SHORT).show();
//
//                                    writesuc6 = apis.tama_write_target(WRITE_EXPIRY,
//                                            expiry, expiry.length);
//
//                                    writesuc7 = apis.tama_write_target(WRITE_IN_OUT,
//                                            inout, inout.length);
//
//                                    writesuc8 = apis.tama_write_target(WRITE_ATTND_TYPE,
//                                            att_type, att_type.length);
//
//                                }
//                                else{
//
//                                    Toast.makeText(context, "Authentication FAILED4",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//
//                                authsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                if(authsuc5==0) {
////                            Toast.makeText(context, "Authentication Success5",
////                                    Toast.LENGTH_SHORT).show();
//
//
//                                    writesuc9 = apis.tama_write_target(WRITE_LASTTRANS_MC_ID,
//                                            last_trans_mc_id, last_trans_mc_id.length);
//
//                                    writesuc10 = apis.tama_write_target(WRITE_TRANS_ID,
//                                            last_trans_id, last_trans_id.length);
//
//                                    writesuc11 = apis.tama_write_target(WRITE_TRANS_AMT,
//                                            last_trans_amt, last_trans_amt.length);
//                                }
//                                else{
//
//                                    Toast.makeText(context, "Authentication FAILED5",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//                                authsuc6 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_SIXTH_SECT, key6, KEYA);
//
//
//                                if(authsuc6==0) {
//
////                            Toast.makeText(context, "Authentication Success6",
////                                    Toast.LENGTH_SHORT).show();
//
//                                    writesuc12 = apis.tama_write_target(WRITE_LAST_RECH_MC_ID,
//                                            last_rech_mc_id, last_rech_mc_id.length);
//
//                                    writesuc13 = apis.tama_write_target(WRITE_LAST_RECH_ID,
//                                            last_rech_id, last_rech_id.length);
//
//                                    writesuc13 = apis.tama_write_target(WRITE_LAST_RECH_AMT,
//                                            last_rech_amt, last_rech_amt.length);
//
//
//                                }
//                                else{
//
//                                    Toast.makeText(context, "Authentication FAILED6",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//
//                                if(writesuc1==0&&writesuc2==0&&writesuc3==0&&writesuc4==0&&writesuc5==0&&writesuc6==0&&
//                                        writesuc7==0&&writesuc8==0&writesuc9==0&&writesuc10==0&&writesuc11==0&&writesuc12==0&&writesuc13==0) {
//
//
//                                    tap_the_card.setVisibility(View.GONE);
//
//
//
//                                    readauthsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_SECND_SECT, key2, KEYA);
//
//                                    if (readauthsuc1 == 0) {
//
//                                        readsuc0 = apis.tama_read_target(READ_SCHOOL_ID
//                                                , read_school);
//                                        readsuc1 = apis.tama_read_target(
//                                                READ_CARD_SERIAL, read_serial);
//                                        readsuc4 = apis.tama_read_target(
//                                                READ_STATUS, read_status);
//
//
//                                        if (readsuc1 == 0) {
////                                Toast.makeText(context, "Read Success serial "+read_serial,
////                                        Toast.LENGTH_SHORT).show();
//                                            System.out.println("read Success");
//
//                                            String schId = String.valueOf(read_school);
////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
////                                Toast.LENGTH_LONG).show();
//
//                                            String serial = String.valueOf(read_serial);
//                                            resultCardSerial = serial;
//                                            System.out.println("read Success cardsl: " + resultCardSerial);
//                                            String status = String.valueOf(read_status);
//                                            int status1 = Integer.parseInt(status);
//
//                                            schIdd.setText(schId);
//                                            statustxt.setText(status1 + "");
//                                            cardsl.setText(serial);
//
//
//                                        } else {
//                                            Toast.makeText(context, "Read Failed serial", Toast.LENGTH_SHORT)
//                                                    .show();
//                                            System.out.println("Read Failed");
//                                        }
//
//                                    } else {
//                                        Toast.makeText(context, "Read Authentication Failed serial",
//                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("Authentication Failed");
//                                    }
//
//
//                                    readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                    if (readauthsuc2 == 0) {
//                                        readsuc2 = apis.tama_read_target(
//                                                READ_CURR_BAL, read_current);
//                                        readsuc5 = apis.tama_read_target(
//                                                READ_PREV_BAL, read_data5);
//                                        readsuc14= apis.tama_read_target(
//                                                READ_ATTND_DATE, read_date);
//
//
//                                        if (readsuc2 == 0) {
////                                        Toast.makeText(context, "Read Success current bal" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                            System.out.println("read Success current bal");
//
//                                            String curr = String.valueOf(read_current);
//                                            resultCurrenBal = curr;
//                                            String curBal=curr.replaceFirst("^0+(?!$)", "");
//                                            System.out.println("curBal " +curBal);
//
//                                            Float currnntt = Float.parseFloat(curBal);
//
//                                            String prevv = String.valueOf(read_data5);
//                                            resultPrevbal = prevv;
//                                            prevv.replaceFirst("^0+(?!$)", "");
//                                            Float previo = Float.parseFloat(prevv);
//
//                                            balan.setText(currnntt + "");
//                                            prevbal.setText(previo + "");
//
//
//                                        } else {
//                                            Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                    .show();
//                                            System.out.println("Read Failed");
//                                        }
//
//                                    } else {
//                                        Toast.makeText(context, "Read Authentication Failed current bal",
//                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("Authentication Failed");
//                                    }
//
//
//                                    readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                    if (readauthsuc3 == 0) {
//                                        readsuc3 = apis.tama_read_target(
//                                                READ_EXPIRY, read_expiry);
//                                        readsuc7 = apis.tama_read_target(
//                                                READ_IN_OUT, read_data7);
//                                        readsuc8 = apis.tama_read_target(
//                                                READ_ATTND_TYPE, read_data8);
//
//
//                                        if (readsuc3 == 0) {
////                                        Toast.makeText(context, "Read Success Expiry" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                            System.out.println("read Success expiry");
//                                            String expir = String.valueOf(read_expiry);
//                                            String inoutatt = String.valueOf(read_data7);
//                                            String atttyp = String.valueOf(read_data8);
//
//                                            int inout1 = Integer.parseInt(inoutatt);
//                                            int atttyp1 = Integer.parseInt(atttyp);
//                                            expir.replaceFirst("0", "");
//                                            expiry1.setText(expir);
//                                            attinout.setText(inout1 + "");
//                                            attype.setText(atttyp1 + "");
//
//
//                                        } else {
//                                            Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                    .show();
//                                            System.out.println("Read Failed");
//                                        }
//
//                                    } else {
//                                        Toast.makeText(context, "Read Authentication Failed expiry",
//                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("Authentication Failed");
//                                    }
//
//
//                                    readauthsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                    if (readauthsuc5 == 0) {
//                                        readsuc9 = apis.tama_read_target(
//                                                READ_LASTTRANS_MC_ID, read_data9);
//                                        readsuc10 = apis.tama_read_target(
//                                                READ_TRANS_ID, read_data10);
//                                        readsuc11 = apis.tama_read_target(
//                                                READ_TRANS_AMT, read_data11);
//
//                                        if (readsuc9 == 0) {
////                                    Toast.makeText(context, "Read Success LAst trans mc ID "+read_data9,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last trans ID "+read_data10,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success Last Trans amt "+read_data11,
////                                            Toast.LENGTH_SHORT).show();
//                                            System.out.println("read Success");
//
//                                            String lasttransmc = String.valueOf(read_data9);
//                                            String lasttrnsid = String.valueOf(read_data10);
//                                            String lasttrnsamt = String.valueOf(read_data11);
//
//                                            Float lasttrnsamt1 = Float.parseFloat(lasttrnsamt);
//
//                                            lasttrnsmcid.setText(lasttransmc);
//                                            lasttransid.setText(lasttrnsid);
//                                            lasttransamt.setText(lasttrnsamt1 + "");
//
//
//                                        } else {
//                                            Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                                    .show();
//                                            System.out.println("Read Failed");
//                                        }
//
//
//                                    } else {
//                                        Toast.makeText(context, "Authentication Failed",
//                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("Authentication Failed");
//                                    }
////
//                                    readauthsuc6 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_SIXTH_SECT, key6, KEYA);
//
//
//                                    if (readauthsuc6 == 0) {
//                                        readsuc12 = apis.tama_read_target(
//                                                READ_LAST_RECH_MC_ID, read_data12);
//                                        readsuc13 = apis.tama_read_target(
//                                                READ_LAST_RECH_ID, read_data13);
//                                        readsuc13 = apis.tama_read_target(
//                                                READ_LAST_RECH_AMT, read_data14);
//
//                                        if (readsuc12 == 0) {
////                                    Toast.makeText(context, "Read Success  last rech mc id "+read_data12,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech Id "+read_data13,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech amt "+read_data14,
////                                            Toast.LENGTH_SHORT).show();
//                                            System.out.println("read Success");
//                                            String lastmc = String.valueOf(read_data12);
//                                            String lastrecid = String.valueOf(read_data13);
//                                            String lastrecamt = String.valueOf(read_data14);
//
//                                            Float lastrecamt1 = Float.parseFloat(lastrecamt);
//
//                                            lastrechmcid.setText(lastmc);
//                                            lastrechid.setText(lastrecid);
//                                            lastrechamt.setText(lastrecamt1 + "");
//
//
//                                        } else {
//                                            Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                                    .show();
//                                            System.out.println("Read Failed");
//                                        }
//
//
//                                    } else {
//                                        Toast.makeText(context, "Authentication Failed",
//                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("Authentication Failed");
//                                    }
//
//
//                                    tap_the_card.setVisibility(View.GONE);
//
//                                    DBwriteFunc();
//
//
//
////                            //Switching OFF RFID FIELD
////                            new FieldOffTask().execute();
//                                }else{
//                                    Toast.makeText(getApplicationContext(),"Write Failure!",Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//
//
//
//                            }else if(authsuc1== -1){
//
//                                tardtc1 = apis1.tama_detectTarget(TARGET_NUM,
//                                        target_detected, target_id);
//
//                                System.out.println("RFID tardtc1 : "+tardtc);
//
//
//                                //////////////////////////////////////////////////
//
//                                if (tardtc1 == 0) {
//
//                                    try{
//
//                                        authsuc1 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_FIRST_SECT, oldkey1, KEYA);
//
//                                        if(authsuc1==0){
//
//                                            writesuc01= apis1.tama_write_target(WRITE_STUDENT_CODE,
//                                                    studentCode1, studentCode1.length);
//                                            System.out.println("ELdosdsa+ "+ studentCode1+" "+studentCode1.length);
//                                            System.out.println("ELdos+ "+ authsuc1+" "+writesuc01);
//
//                                        }
//
//
//
//                                        authsuc2 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_SECND_SECT, key2, KEYA);
//
//
//
//                                        System.out.println("Authentication Success");
//
//
//
//
//                                        if (authsuc2 == 0) {
//
////                            Toast.makeText(context, "Authentication Success2",
////                                    Toast.LENGTH_SHORT).show();
//
//                                            writesuc1 = apis1.tama_write_target(WRITE_SCHOOL_ID,
//                                                    schoolid, schoolid.length);
//
//                                            writesuc2 = apis1.tama_write_target(WRITE_CARD_SERIAL,
//                                                    cardserial, cardserial.length);
//
//                                            writesuc3 = apis1.tama_write_target(WRITE_STATUS,
//                                                    status, status.length);
////                            Toast.makeText(context, "writesuc "+writesuc1,
////                                    Toast.LENGTH_SHORT).show();
//
//
//                                        }
//                                        else{
//
//                                            Toast.makeText(context, "Authentication FAILED2",
//                                                    Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                        authsuc3 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_THIRD_SECT, key3, KEYA);
//
//
//
//                                        if(authsuc3==0) {
//
////                            Toast.makeText(context, "Authentication Success3",
////                                    Toast.LENGTH_SHORT).show();
//
//                                            writesuc4 = apis1.tama_write_target(WRITE_CURR_BAL,
//                                                    curr_bal, curr_bal.length);
//
//                                            writesuc5 = apis1.tama_write_target(WRITE_PREV_BAL,
//                                                    prev_bal, prev_bal.length);
//
//                                            writesuc14 = apis1.tama_write_target(WRITE_ATTND_DATE,
//                                                    att_date, att_date.length);
//                                        }
//                                        else{
//
//                                            Toast.makeText(context, "Authentication FAILED3",
//                                                    Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                        authsuc4 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                        if(authsuc4 ==0) {
////                            Toast.makeText(context, "Authentication Success4",
////                                    Toast.LENGTH_SHORT).show();
//
//                                            writesuc6 = apis1.tama_write_target(WRITE_EXPIRY,
//                                                    expiry, expiry.length);
//
//                                            writesuc7 = apis1.tama_write_target(WRITE_IN_OUT,
//                                                    inout, inout.length);
//
//                                            writesuc8 = apis1.tama_write_target(WRITE_ATTND_TYPE,
//                                                    att_type, att_type.length);
//
//                                        }
//                                        else{
//
//                                            Toast.makeText(context, "Authentication FAILED4",
//                                                    Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                        authsuc5 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                        if(authsuc5==0) {
////                            Toast.makeText(context, "Authentication Success5",
////                                    Toast.LENGTH_SHORT).show();
//
//
//                                            writesuc9 = apis1.tama_write_target(WRITE_LASTTRANS_MC_ID,
//                                                    last_trans_mc_id, last_trans_mc_id.length);
//
//                                            writesuc10 = apis1.tama_write_target(WRITE_TRANS_ID,
//                                                    last_trans_id, last_trans_id.length);
//
//                                            writesuc11 = apis1.tama_write_target(WRITE_TRANS_AMT,
//                                                    last_trans_amt, last_trans_amt.length);
//                                        }
//                                        else{
//
//                                            Toast.makeText(context, "Authentication FAILED5",
//                                                    Toast.LENGTH_SHORT).show();
//
//                                        }
//
//
//                                        authsuc6 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_SIXTH_SECT, key6, KEYA);
//
//
//                                        if(authsuc6==0) {
//
////                            Toast.makeText(context, "Authentication Success6",
////                                    Toast.LENGTH_SHORT).show();
//
//                                            writesuc12 = apis1.tama_write_target(WRITE_LAST_RECH_MC_ID,
//                                                    last_rech_mc_id, last_rech_mc_id.length);
//
//                                            writesuc13 = apis1.tama_write_target(WRITE_LAST_RECH_ID,
//                                                    last_rech_id, last_rech_id.length);
//
//                                            writesuc13 = apis1.tama_write_target(WRITE_LAST_RECH_AMT,
//                                                    last_rech_amt, last_rech_amt.length);
//
//
//                                        }
//                                        else{
//
//                                            Toast.makeText(context, "Authentication FAILED6",
//                                                    Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                        if(writesuc1==0&&writesuc2==0&&writesuc3==0&&writesuc4==0&&writesuc5==0&&writesuc6==0&&
//                                                writesuc7==0&&writesuc8==0&writesuc9==0&&writesuc10==0&&writesuc11==0&&writesuc12==0&&writesuc13==0) {
//
//
//                                            tap_the_card.setVisibility(View.GONE);
//
//
//
//                                            readauthsuc1 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_SECND_SECT, key2, KEYA);
//
//                                            if (readauthsuc1 == 0) {
//
//                                                readsuc0 = apis1.tama_read_target(READ_SCHOOL_ID
//                                                        , read_school);
//                                                readsuc1 = apis1.tama_read_target(
//                                                        READ_CARD_SERIAL, read_serial);
//                                                readsuc4 = apis1.tama_read_target(
//                                                        READ_STATUS, read_status);
//
//
//                                                if (readsuc1 == 0) {
////                                Toast.makeText(context, "Read Success serial "+read_serial,
////                                        Toast.LENGTH_SHORT).show();
//                                                    System.out.println("read Success");
//
//                                                    String schId = String.valueOf(read_school);
////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
////                                Toast.LENGTH_LONG).show();
//
//                                                    String serial = String.valueOf(read_serial);
//                                                    resultCardSerial = serial;
//                                                    System.out.println("read Success cardsl: " + resultCardSerial);
//                                                    String status = String.valueOf(read_status);
//                                                    int status1 = Integer.parseInt(status);
//
//                                                    schIdd.setText(schId);
//                                                    statustxt.setText(status1 + "");
//                                                    cardsl.setText(serial);
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Read Failed serial", Toast.LENGTH_SHORT)
//                                                            .show();
//                                                    System.out.println("Read Failed");
//                                                }
//
//                                            } else {
//                                                Toast.makeText(context, "Read Authentication Failed serial",
//                                                        Toast.LENGTH_SHORT).show();
//                                                System.out.println("Authentication Failed");
//                                            }
//
//
//                                            readauthsuc2 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                            if (readauthsuc2 == 0) {
//                                                readsuc2 = apis1.tama_read_target(
//                                                        READ_CURR_BAL, read_current);
//                                                readsuc5 = apis1.tama_read_target(
//                                                        READ_PREV_BAL, read_data5);
//                                                readsuc14= apis1.tama_read_target(
//                                                        READ_ATTND_DATE, read_date);
//
//
//                                                if (readsuc2 == 0) {
////                                        Toast.makeText(context, "Read Success current bal" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                                    System.out.println("read Success current bal");
//
//                                                    String curr = String.valueOf(read_current);
//                                                    resultCurrenBal = curr;
//                                                    curr.replaceFirst("^0+(?!$)", "");
//                                                    Float currnntt = Float.parseFloat(curr);
//
//                                                    String prevv = String.valueOf(read_data5);
//                                                    resultPrevbal = prevv;
//                                                    prevv.replaceFirst("^0+(?!$)", "");
//                                                    Float previo = Float.parseFloat(prevv);
//
//                                                    balan.setText(currnntt + "");
//                                                    prevbal.setText(previo + "");
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
//
//                                            readauthsuc3 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                            if (readauthsuc3 == 0) {
//                                                readsuc3 = apis1.tama_read_target(
//                                                        READ_EXPIRY, read_expiry);
//                                                readsuc7 = apis1.tama_read_target(
//                                                        READ_IN_OUT, read_data7);
//                                                readsuc8 = apis1.tama_read_target(
//                                                        READ_ATTND_TYPE, read_data8);
//
//
//                                                if (readsuc3 == 0) {
////                                        Toast.makeText(context, "Read Success Expiry" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                                    System.out.println("read Success expiry");
//                                                    String expir = String.valueOf(read_expiry);
//                                                    String inoutatt = String.valueOf(read_data7);
//                                                    String atttyp = String.valueOf(read_data8);
//
//                                                    int inout1 = Integer.parseInt(inoutatt);
//                                                    int atttyp1 = Integer.parseInt(atttyp);
//                                                    expir.replaceFirst("0", "");
//                                                    expiry1.setText(expir);
//                                                    attinout.setText(inout1 + "");
//                                                    attype.setText(atttyp1 + "");
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                            .show();
//                                                    System.out.println("Read Failed");
//                                                }
//
//                                            } else {
//                                                Toast.makeText(context, "Read Authentication Failed expiry",
//                                                        Toast.LENGTH_SHORT).show();
//                                                System.out.println("Authentication Failed");
//                                            }
//
//
//                                            readauthsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                            if (readauthsuc5 == 0) {
//                                                readsuc9 = apis1.tama_read_target(
//                                                        READ_LASTTRANS_MC_ID, read_data9);
//                                                readsuc10 = apis1.tama_read_target(
//                                                        READ_TRANS_ID, read_data10);
//                                                readsuc11 = apis1.tama_read_target(
//                                                        READ_TRANS_AMT, read_data11);
//
//                                                if (readsuc9 == 0) {
////                                    Toast.makeText(context, "Read Success LAst trans mc ID "+read_data9,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last trans ID "+read_data10,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success Last Trans amt "+read_data11,
////                                            Toast.LENGTH_SHORT).show();
//                                                    System.out.println("read Success");
//
//                                                    String lasttransmc = String.valueOf(read_data9);
//                                                    String lasttrnsid = String.valueOf(read_data10);
//                                                    String lasttrnsamt = String.valueOf(read_data11);
//
//                                                    Float lasttrnsamt1 = Float.parseFloat(lasttrnsamt);
//
//                                                    lasttrnsmcid.setText(lasttransmc);
//                                                    lasttransid.setText(lasttrnsid);
//                                                    lasttransamt.setText(lasttrnsamt1 + "");
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                                            .show();
//                                                    System.out.println("Read Failed");
//                                                }
//
//
//                                            } else {
//                                                Toast.makeText(context, "Authentication Failed",
//                                                        Toast.LENGTH_SHORT).show();
//                                                System.out.println("Authentication Failed");
//                                            }
////
//                                            readauthsuc6 = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_SIXTH_SECT, key6, KEYA);
//
//
//                                            if (readauthsuc6 == 0) {
//                                                readsuc12 = apis1.tama_read_target(
//                                                        READ_LAST_RECH_MC_ID, read_data12);
//                                                readsuc13 = apis1.tama_read_target(
//                                                        READ_LAST_RECH_ID, read_data13);
//                                                readsuc13 = apis1.tama_read_target(
//                                                        READ_LAST_RECH_AMT, read_data14);
//
//                                                if (readsuc12 == 0) {
////                                    Toast.makeText(context, "Read Success  last rech mc id "+read_data12,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech Id "+read_data13,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech amt "+read_data14,
////                                            Toast.LENGTH_SHORT).show();
//                                                    System.out.println("read Success");
//                                                    String lastmc = String.valueOf(read_data12);
//                                                    String lastrecid = String.valueOf(read_data13);
//                                                    String lastrecamt = String.valueOf(read_data14);
//
//                                                    Float lastrecamt1 = Float.parseFloat(lastrecamt);
//
//                                                    lastrechmcid.setText(lastmc);
//                                                    lastrechid.setText(lastrecid);
//                                                    lastrechamt.setText(lastrecamt1 + "");
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                                            .show();
//                                                    System.out.println("Read Failed");
//                                                }
//
//
//                                            } else {
//                                                Toast.makeText(context, "Authentication Failed",
//                                                        Toast.LENGTH_SHORT).show();
//                                                System.out.println("Authentication Failed");
//                                            }
//
//
//                                            tap_the_card.setVisibility(View.GONE);
//
//                                            DBwriteFunc();
//
//
//
////                            //Switching OFF RFID FIELD
////                            new FieldOffTask().execute();
//                                        }else{
//                                            Toast.makeText(getApplicationContext(),"Write Failure!",Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                    }catch(NumberFormatException e){
//
//                                        tap_the_card.setClickable(false);
//                                        tap_the_card.setEnabled(false);
//                                        e.printStackTrace();
//
//                                        Toast.makeText(context, "Target Moved. Try Again", Toast.LENGTH_SHORT)
//                                                .show();
//                                        Builder alert = new Builder(
//                                                MachineBlkTrnsferNFC.this);
//                                        alert.setTitle("RFID");
//
//                                        alert.setCancelable(false);
//                                        alert.setMessage("Target Moved. Try Again");
//
//                                        DialogInterface.OnClickListener listener;
//                                        alert.setPositiveButton(
//                                                "OK",
//                                                new DialogInterface.OnClickListener() {
//
//                                                    @Override
//                                                    public void onClick(
//                                                            DialogInterface dialog,
//                                                            int which) {
//
//                                                        dialog.dismiss();
////                                            Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                            startActivity(i);
////                                            finish();
//                                                        new FieldRestartTask().execute();
//
//                                                    }
//                                                });
//                                        alert.show();
//
//                                        System.out.println("Target detect Failed");
//                                    }
//
//
//                                } else {
//                                    tap_the_card.setClickable(false);
//                                    tap_the_card.setEnabled(false);
//
//                                    Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                            .show();
//                                    Builder alert = new Builder(
//                                            MachineBlkTrnsferNFC.this);
//                                    alert.setTitle("RFID");
//
//                                    alert.setCancelable(false);
//                                    alert.setMessage("Target detect Failed");
//
//                                    DialogInterface.OnClickListener listener;
//                                    alert.setPositiveButton(
//                                            "OK",
//                                            new DialogInterface.OnClickListener() {
//
//                                                @Override
//                                                public void onClick(
//                                                        DialogInterface dialog,
//                                                        int which) {
//
//                                                    dialog.dismiss();
////                                        Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        startActivity(i);
////                                        finish();
//                                                    new FieldRestartTask().execute();
//
//                                                }
//                                            });
//                                    alert.show();
//
//                                    System.out.println("Target detect Failed");
//                                }
//
//
//                            /////////////////////////////////////////////////////
//                            }
//
//
//
//
//                        }catch(NumberFormatException e){
//
//                            e.printStackTrace();
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//
//                            Toast.makeText(context, "Target Moved. Try Again", Toast.LENGTH_SHORT)
//                                    .show();
//                            Builder alert = new Builder(
//                                    MachineBlkTrnsferNFC.this);
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
//
//                    } else {
//                        tap_the_card.setClickable(false);
//                        tap_the_card.setEnabled(false);
//
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        Builder alert = new Builder(
//                                MachineBlkTrnsferNFC.this);
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

        go_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    private void DBwriteFunc() {




        Float amt= Float.valueOf(amount);

        String resPrevbal1=resultPrevbal.replaceFirst("^0+(?!$)", "");
        String resCurrbal1=resultCurrenBal.replaceFirst("^0+(?!$)", "");
        Float prevBal= Float.valueOf(resPrevbal1);
        Float currbal= Float.valueOf(resCurrbal1);

        if((currbal-prevBal)==amt) {

            DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");

// Get the date today using Calendar object.
            Date today = Calendar.getInstance().getTime();

            currentDateTimeString = df.format(today);

            transid = db.lastTransID();


            Recharge_Data recharg = new Recharge_Data(transid, userid, transtype_id, prevBal, currbal, serial,
                    currentDateTimeString, "", transid, currentDateTimeString, amt, 3, "");
            db.addSuccesstransaction(recharg);
            recharg.gettransaction_id();
            System.out.println("CODMOB: RechAMT blocked" + recharg.getRech_amt());

            db.addrecharge(recharg);


//        List<Success_Transaction> successtransaction = db.getAllSuccess();
//        for (Success_Transaction usp : successtransaction) {
//            String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
//                    "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
//                    + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
//                    "  Server timestamp  " + usp.getserver_timestamp();
//            System.out.println("CHILLAR CARDINIT success transaction table1 " + testpermission);
//
//        }
        }else{

            Toast.makeText(MachineBlkTrnsferNFC.this, "Some error in recharge. ", Toast.LENGTH_SHORT).show();
            finish();
        }
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




//    private class FieldRestartTask extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            apis.tama_close();
//            apis1.tama_close();
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
//                    tama_on = apis1.tama_open();
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
//            Constants.AsyncFlag=1;
//            progressBar.setVisibility(View.GONE);
//            tap_the_card.setClickable(true);
//            tap_the_card.setEnabled(true);
//        }
//    }

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

//        @Override
//        protected void onPreExecute() {
//
//            progressBar.setVisibility(View.VISIBLE);
//        }

//        @Override
//        protected void onPostExecute(Void aVoid) {
////            progressBar.setVisibility(View.GONE);
//            tap_the_card.setClickable(true);
//            tap_the_card.setEnabled(true);
//        }
//    }


//    private void ErrorPHP() {
//
//
//        String tag_string_req="errorphp";
//
//
//        String URL=Constants.APP_URL+"c_online_recharge.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="/*+tranact_id+"&time_stamp="+getcurrentdate()*/;
//        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
//        System.out.println("URL ErrorPHP " + URL);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                URL, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("TESTT", response.toString());
//                System.out.println("CODMOB: TESTS "+response);
//                System.out.println("testf"+response.toString());
//                // progressDialog.dismiss();
//                JSONObject jsobj;
//                String server_respose;
//                try {
//
//                    jsobj = new JSONObject(response);
//                    server_respose=jsobj.getString("status");
//                    JSONObject jsonObject=new JSONObject(server_respose);
//                    String code=jsonObject.getString("code");
//                    String msg=jsonObject.getString("message");
//
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getApplicationContext(), "Some error occurred . Please try again", Toast.LENGTH_SHORT).show();
////                finish();
//            }
//
//        });
//
//
//// Adding request to request queue
//
//        App.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//
//    }



}
