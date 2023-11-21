//package com.chillarcards.machinevendor;
//
//import android.app.Activity;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.database.CursorIndexOutOfBoundsException;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.RemoteException;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cloudpos.DeviceException;
//import com.cloudpos.OperationListener;
//import com.cloudpos.OperationResult;
//import com.cloudpos.POSTerminal;
//import com.cloudpos.TimeConstants;
//import com.cloudpos.card.Card;
//import com.cloudpos.card.MifareCard;
//import com.cloudpos.rfcardreader.RFCardReaderDevice;
//import com.cloudpos.rfcardreader.RFCardReaderOperationResult;
//
//import java.nio.charset.StandardCharsets;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
//
//
//public class StudentCodeInitNFC extends Activity {
//    public static final int AUTH_FIRST_SECT = 3;
//    public static final int AUTH_SECND_SECT = 7;
//    public static final int AUTH_THIRD_SECT = 11;
//    public static final int AUTH_FOURTH_SECT = 15;
//    public static final int AUTH_FIFTH_SECT = 19;
//    public static final int AUTH_SIXTH_SECT = 23;
//
//    public static final int READ_SCHOOL_ID = 4;
//    public static final int READ_CARD_SERIAL = 5;
//    public static final int READ_STATUS = 6;
//    public static final int READ_CURR_BAL = 8;
//    public static final int READ_PREV_BAL = 9;
//    public static final int READ_EXPIRY = 12;
//    public static final int READ_IN_OUT = 13;
//    public static final int READ_ATTND_TYPE = 14;
//    public static final int READ_LASTTRANS_MC_ID = 16;
//    public static final int READ_TRANS_ID = 17;
//    public static final int READ_TRANS_AMT = 18;
//    public static final int READ_LAST_RECH_MC_ID = 20;
//    public static final int READ_LAST_RECH_ID = 21;
//    public static final int READ_LAST_RECH_AMT = 22;
//    public static final int READ_ATTND_DATE = 10;
//
//    public static final int WRITE_STUDENT_CODE = 1;
//    public static final int WRITE_ONLINE_TRANSID = 2;
//    public static final int WRITE_SCHOOL_ID = 4;
//    public static final int WRITE_CARD_SERIAL = 5;
//    public static final int WRITE_STATUS = 6;
//    public static final int WRITE_CURR_BAL = 8;
//    public static final int WRITE_PREV_BAL = 9;
//    public static final int WRITE_EXPIRY = 12;
//    public static final int WRITE_IN_OUT = 13;
//    public static final int WRITE_ATTND_TYPE = 14;
//    public static final int WRITE_LASTTRANS_MC_ID = 16;
//    public static final int WRITE_TRANS_ID = 17;
//    public static final int WRITE_TRANS_AMT = 18;
//    public static final int WRITE_LAST_RECH_MC_ID = 20;
//    public static final int WRITE_LAST_RECH_ID = 21;
//    public static final int WRITE_LAST_RECH_AMT = 22;
//    public static final int WRITE_ATTND_DATE = 10;
//
//    public static final int KEYA = 0x0a;
//    public static final int KEYB = 0x0b;
//    public static final int TARGET_NUM = 1;
//
//    char[] key1 = { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
//    byte[] key11 = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
//    char[] key2 = { 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
//    byte[] key21 = new byte[]{ 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
//    char[] key3 = { 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
//    byte[] key31 = new byte[]{ 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
//    char[] key4 = { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
//    byte[] key41 = new byte[] { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
//    char[] key5 = { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
//    byte[] key51 =  new byte[] { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
//    char[] key6 = { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };
//    byte[] key61 = new byte[] { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };
//
//
//    StringBuffer read_data1 = new StringBuffer();
//    StringBuffer read_data2 = new StringBuffer();
//    StringBuffer read_data3 = new StringBuffer();
//    StringBuffer read_data4 = new StringBuffer();
//    StringBuffer read_data6 = new StringBuffer();
//
//
//    StringBuffer read_serial = new StringBuffer();
//    StringBuffer read_current = new StringBuffer();
//    StringBuffer read_school = new StringBuffer();
//    StringBuffer read_expiry = new StringBuffer();
//    StringBuffer read_status = new StringBuffer();
//    StringBuffer read_data5 = new StringBuffer();
//    StringBuffer read_data7 = new StringBuffer();
//    StringBuffer read_data8 = new StringBuffer();
//    StringBuffer read_data9 = new StringBuffer();
//    StringBuffer read_data10 = new StringBuffer();
//    StringBuffer read_data11 = new StringBuffer();
//    StringBuffer read_data12 = new StringBuffer();
//    StringBuffer read_data13 = new StringBuffer();
//    StringBuffer read_data14 = new StringBuffer();
//    StringBuffer read_date = new StringBuffer();
//
//
//    StringBuffer read_data = new StringBuffer();
//
//
//
//    StringBuffer target_detected = new StringBuffer();
//    StringBuffer target_id = new StringBuffer();
//
//
//
//    String resultCardSerial,resultPrevbal,resultCurrenBal;
//
//    Context context;
//
//
//
//    public Button read;
//
//    public Button tap_the_card,go_back;
//    ProgressBar progressBar;
//    
//    int ret_poweron;
//    int ret_fieldon;
//
//    int tardtc;
//    int authsuc1=-1,authsuc2,authsuc3,authsuc4,authsuc5,authsuc6,readauthsuc1,readauthsuc2,readauthsuc3,readauthsuc4,readauthsuc5,readauthsuc6;
//    int writesuc01,writesuc010,writesuc1,writesuc2,writesuc3,writesuc4,writesuc5,writesuc6,writesuc7,writesuc8,writesuc9,writesuc10,writesuc11,writesuc12,writesuc13,writesuc14;
//    int readsuc0,readsuc1,readsuc2,readsuc3,readsuc4,readsuc5,readsuc6,readsuc7,readsuc8,readsuc9,readsuc10,readsuc11,readsuc12,readsuc13,readsuc14;
//    int tama_on;
//    boolean authSuccess1=false,authSuccess2=false,authSuccess3=false,authSuccess4=false,authSuccess5=false,authSuccess6=false;
//    String exp,expire;
//
////    DatabaseHandler db = new DatabaseHandler(this);
//
//    DatabaseHandler db ;
//    String schoolID,serialNo,serial,serial1,studCode,studentCode;
//    TextView cardsl,schIdd,balan,expiry1,statustxt,prevbal,attype,attinout,lasttrnsmcid,lasttransid,lasttransamt,lastrechmcid,lastrechid,lastrechamt;
//
//    String formattedDate,currentDateTimeString,transid;
//    int transtype_id,userid;
//
//    private RFCardReaderDevice device = null;
//    Card rfCard;
//    Context mContext;
//    Boolean deviceTapStatus = false;
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//
//    }
//
//    private void CardInitialise() {
//
//        String filler1 ="";
//        String filler2 ="";
//        String filler3 ="";
//
//
//        int len1=16-db.getSchoolId().length();
//        for(int j=0;j<len1;++j){
//
//            filler1=filler1+" ";
//
//        }
//
//        schoolID=db.getSchoolId()+filler1;
//
//
//
//        int len2=16-studCode.length();
//        for(int j=0;j<len2;++j){
//
//            filler2=filler2+" ";
//
//        }
//
//        studentCode=filler2+studCode;
//
//
//
//        int len3=16-exp.length();
//        for(int j=0;j<len3;++j){
//
//            filler3=filler3+"0";
//
//        }
//
//        expire=filler3+exp;
//
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_carddetails);
//
//        db = DatabaseHandler.getInstance(getApplicationContext());
//
//
//        progressBar= (ProgressBar) findViewById(R.id.progress);
//        progressBar.setVisibility(View.GONE);
//
//        tap_the_card = (Button) findViewById(R.id.tap);
//        go_back= (Button) findViewById(R.id.tapback);
//
//
//        context = getApplicationContext();
//
//        cardsl= (TextView) findViewById(R.id.cardser);
//        schIdd= (TextView) findViewById(R.id.schId);
//        balan= (TextView) findViewById(R.id.balance);
//        expiry1= (TextView) findViewById(R.id.expiry);
//        statustxt= (TextView) findViewById(R.id.status);
//        prevbal= (TextView) findViewById(R.id.prevbalance);
//        attype= (TextView) findViewById(R.id.att_typ);
//        attinout= (TextView) findViewById(R.id.inout);
//        lasttrnsmcid= (TextView) findViewById(R.id.lst_trnsmcid);
//        lasttransid= (TextView) findViewById(R.id.lst_trnsid);
//        lasttransamt= (TextView) findViewById(R.id.lst_trnsamt);
//        lastrechmcid= (TextView) findViewById(R.id.lst_rechmcid);
//        lastrechid= (TextView) findViewById(R.id.lst_rechid);
//        lastrechamt= (TextView) findViewById(R.id.lst_rechamt);
//
//
////        serialNo=Constants.SerialNo;
//
//        Bundle b=getIntent().getExtras();
//        serial=b.getString("cardsl");
//        studCode=b.getString("stdCode");
//
//
//        transtype_id=db.getTransTypID("CARD INITIALISATION");
//
//
//        System.out.println("TRANS TYPE ID: "+transtype_id);
//
//        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
//        serial1=editor.getString("machineserial","");
//        userid= editor.getInt("userid",0);
//        System.out.println("RFID: serial "+serialNo);
//
//
//
//
//
//
//
//
//        Calendar c= Calendar.getInstance();
//        int cyear = c.get(Calendar.YEAR);
//        int nxtyear=cyear+1;
//
//        System.out.println("CHILLRif: "+cyear);
//        int cmonth = c.get(Calendar.MONTH)+1;
//
//        if(cmonth<=5){
//            exp="31/05/"+cyear;
//            System.out.println("CHILLRif: "+exp);
//        }else{
//            exp="31/05/"+nxtyear;
//            System.out.println("CHILLRelse: "+exp);
//        }
//
//        CardInitialise();
//
//
//        final char[] chillkey2={ 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey3={ 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey4={ 0x42, 0x21, 0x62, 0x40, 0x49, 0x23, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey5={ 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey6={ 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//
//
//
//
//
//        final char[] studentCode1=new String(studentCode).toCharArray();
//        final byte[] studentCode11=new String(studentCode).getBytes();
//        final char[] online=new String("0000000000000000").toCharArray();
//        final char[] schoolid=new String(schoolID).toCharArray();
//        final byte[] schoolid11=new String(schoolID).getBytes();
//        final char[] cardserial=new String(serial).toCharArray();
//        final byte[] cardserial11=new String(serial).getBytes();
//        final char[] status=new String("0000000000000001").toCharArray();
//        final byte[] status11=new String("0000000000000001").getBytes();
//        final char[] curr_bal=new String("0000000000000000").toCharArray();
//        final byte[] curr_bal11=new String("0000000000000000").getBytes();
//        final char[] prev_bal=new String("0000000000000000").toCharArray();
//        final byte[] prev_bal11=new String("0000000000000000").getBytes();
//        final char[] expiry=new String(expire).toCharArray();
//        final byte[] expiry11=new String(expire).getBytes();
//        final char[] inout=new String("0000000000000000").toCharArray();
//        final char[] att_type=new String("0000000000000000").toCharArray();
//        final char[] last_trans_mc_id=new String("0000000000000000").toCharArray();
//        final char[] last_trans_id=new String("0000000000000000").toCharArray();
//        final char[] last_trans_amt=new String("0000000000000000").toCharArray();
//        final char[] last_rech_mc_id=new String("0000000000000000").toCharArray();
//        final char[] last_rech_id=new String("0000000000000000").toCharArray();
//        final char[] last_rech_amt=new String("0000000000000000").toCharArray();
//        final char[] att_date=new String("0000000000000000").toCharArray();
//        final byte[] att_date11=new String("0000000000000000").getBytes();
//
//
//
//        //INITIALIZING THE CARD WHEN TAPPED ON THE tap_card_button
//
//
//        tap_the_card.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
////                if (SecondActivity.ret_fieldon == 0) {
//                if(getDeviceStatus()) {
//                    System.out.println("RFID Tama open :" + tama_on);
//
////                    tardtc = apis.tama_detectTarget(TARGET_NUM,
////                            target_detected, target_id);
//
//                    System.out.println("RFID tard: "+tardtc);
////                    if (tardtc == 0) {
//                    if(getDeviceStatus()) {
//                        try{
//
//
////						    authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
////								    AUTH_FIRST_SECT, key1, KEYA);
//
//                            try {
//                                authSuccess1 = ((MifareCard) rfCard).verifyKeyA(AUTH_FIRST_SECT, key11);
//                            } catch (DeviceException e) {
//                                e.printStackTrace();
//                            }
////                            if(authsuc1==0){
//                            if(authSuccess1) {
//                                try {
//                                    ((MifareCard) rfCard).writeBlock(WRITE_STUDENT_CODE, 0, studentCode11);
//                                    ((MifareCard) rfCard).writeBlock(WRITE_ONLINE_TRANSID, 0, studentCode11);
//                                } catch (DeviceException e) {
//                                    e.printStackTrace();
//                                }
////                                writesuc01= apis.tama_write_target(WRITE_STUDENT_CODE,
////                                        studentCode1, studentCode1.length);
////
////                                writesuc010= apis.tama_write_target(WRITE_ONLINE_TRANSID,
////                                        online, online.length);
//
//
//                                System.out.println("ELdosdsa+ "+ studentCode1+" "+studentCode1.length);
//                                System.out.println("ELdos+ "+ authsuc1+" "+writesuc01);
//                                System.out.println("ELdos df+ "+ authsuc1+" "+writesuc010);
//
//                            }
//
//
////                            authsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                    AUTH_SECND_SECT, key2, KEYA);
//                            try {
//                                authSuccess2 = ((MifareCard) rfCard).verifyKeyA(AUTH_SECND_SECT, key21);
//                            } catch (DeviceException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            System.out.println("Authentication Success");
//
//
//
//
////                            if (authsuc2 == 0) {
//                            if(authSuccess2) {
//
////                                writesuc1 = apis.tama_write_target(WRITE_SCHOOL_ID,
////                                        schoolid, schoolid.length);
////
////                                writesuc2 = apis.tama_write_target(WRITE_CARD_SERIAL,
////                                        cardserial, cardserial.length);
////
////                                writesuc3 = apis.tama_write_target(WRITE_STATUS,
////                                        status, status.length);
//
//                                try {
//                                    ((MifareCard) rfCard).writeBlock(WRITE_SCHOOL_ID, 0, schoolid11);
//                                    ((MifareCard) rfCard).writeBlock(WRITE_CARD_SERIAL, 0, cardserial11);
//                                    ((MifareCard) rfCard).writeBlock(WRITE_STATUS, 0, status11);
//                                } catch (DeviceException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                            else{
//
//
//                                Toast.makeText(context, "Authentication FAILED2",
//                                        Toast.LENGTH_SHORT).show();
//
//
//
//                            }
//
////                            authsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                    AUTH_THIRD_SECT, key3, KEYA);
//                            try {
//                                authSuccess3 = ((MifareCard) rfCard).verifyKeyA(AUTH_THIRD_SECT, key31);
//                            } catch (DeviceException e) {
//                                e.printStackTrace();
//                            }
//
//
////                            if(authsuc3==0) {
//                            if(authSuccess3) {
////                            Toast.makeText(context, "Authentication Success3",
////                                    Toast.LENGTH_SHORT).show();
//
////                                writesuc4 = apis.tama_write_target(WRITE_CURR_BAL,
////                                        curr_bal, curr_bal.length);
////
////                                writesuc5 = apis.tama_write_target(WRITE_PREV_BAL,
////                                        prev_bal, prev_bal.length);
////
////                                writesuc14 = apis.tama_write_target(WRITE_ATTND_DATE,
////                                        att_date, att_date.length);
//
//                                try {
//                                    ((MifareCard) rfCard).writeBlock(WRITE_CURR_BAL, 0, curr_bal11);
//                                    ((MifareCard) rfCard).writeBlock(WRITE_PREV_BAL, 0, prev_bal11);
//                                    ((MifareCard) rfCard).writeBlock(WRITE_ATTND_DATE, 0, att_date11);
//                                } catch (DeviceException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            else{
//
//                                Toast.makeText(context, "Authentication FAILED3",
//                                        Toast.LENGTH_SHORT).show();
//
//                            }
//
////                            authsuc4 = apis.tama_authenticate_mifare(TARGET_NUM,
////                                    AUTH_FOURTH_SECT, key4, KEYA);
//                            try {
//                                authSuccess3 = ((MifareCard) rfCard).verifyKeyA(AUTH_FOURTH_SECT, key41);
//                            } catch (DeviceException e) {
//                                e.printStackTrace();
//                            }
//
//                            if(authsuc4 ==0) {
////                            Toast.makeText(context, "Authentication Success4",
////                                    Toast.LENGTH_SHORT).show();
//
//                                writesuc6 = apis.tama_write_target(WRITE_EXPIRY,
//                                        expiry, expiry.length);
//
//                                writesuc7 = apis.tama_write_target(WRITE_IN_OUT,
//                                        inout, inout.length);
//
//                                writesuc8 = apis.tama_write_target(WRITE_ATTND_TYPE,
//                                        att_type, att_type.length);
//
//                                try {
//                                    ((MifareCard) rfCard).writeBlock(WRITE_EXPIRY, 0, curr_bal11);
//                                } catch (DeviceException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                            else{
//
//                                Toast.makeText(context, "Authentication FAILED4",
//                                        Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            authsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                            if(authsuc5==0) {
////                            Toast.makeText(context, "Authentication Success5",
////                                    Toast.LENGTH_SHORT).show();
//
//
//                                writesuc9 = apis.tama_write_target(WRITE_LASTTRANS_MC_ID,
//                                        last_trans_mc_id, last_trans_mc_id.length);
//
//                                writesuc10 = apis.tama_write_target(WRITE_TRANS_ID,
//                                        last_trans_id, last_trans_id.length);
//
//                                writesuc11 = apis.tama_write_target(WRITE_TRANS_AMT,
//                                        last_trans_amt, last_trans_amt.length);
//                            }
//                            else{
//
//                                Toast.makeText(context, "Authentication FAILED5",
//                                        Toast.LENGTH_SHORT).show();
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
////                            Toast.makeText(context, "Authentication Success6",
////                                    Toast.LENGTH_SHORT).show();
//
//                                writesuc12 = apis.tama_write_target(WRITE_LAST_RECH_MC_ID,
//                                        last_rech_mc_id, last_rech_mc_id.length);
//
//                                writesuc13 = apis.tama_write_target(WRITE_LAST_RECH_ID,
//                                        last_rech_id, last_rech_id.length);
//
//                                writesuc13 = apis.tama_write_target(WRITE_LAST_RECH_AMT,
//                                        last_rech_amt, last_rech_amt.length);
//
//
//                            }
//                            else{
//
//                                Toast.makeText(context, "Authentication FAILED6",
//                                        Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            if(writesuc1==0&&writesuc2==0&&writesuc3==0&&writesuc4==0&&writesuc5==0&&writesuc6==0&&
//                                    writesuc7==0&&writesuc8==0&writesuc9==0&&writesuc10==0&&writesuc11==0&&writesuc12==0&&writesuc13==0){
//
//                                tap_the_card.setVisibility(View.GONE);
//                                DBwriteFunc();
//                            }
//
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
//                                readsuc4=apis.tama_read_target(
//                                        READ_STATUS, read_status);
//
//
//
//                                if (readsuc1 == 0) {
////                                Toast.makeText(context, "Read Success serial "+read_serial,
////                                        Toast.LENGTH_SHORT).show();
//                                    System.out.println("read Success");
//
//                                    String schId= String.valueOf(read_school);
////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
////                                Toast.LENGTH_LONG).show();
//
//                                    String serial=String.valueOf(read_serial);
//                                    resultCardSerial=serial;
//                                    System.out.println("read Success cardsl: "+resultCardSerial);
//                                    String status= String.valueOf(read_status);
//                                    int status1= Integer.parseInt(status);
//
//                                    schIdd.setText(schId);
//                                    statustxt.setText(status1+"");
//                                    cardsl.setText(serial);
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
//
//
//
//
//
//                            readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                            if (readauthsuc2 == 0) {
//                                readsuc2 = apis.tama_read_target(
//                                        READ_CURR_BAL, read_current);
//                                readsuc5 = apis.tama_read_target(
//                                        READ_PREV_BAL, read_data5);
//                                readsuc14= apis.tama_read_target(
//                                        READ_ATTND_DATE, read_date);
//
//
//                                if (readsuc2 == 0) {
////                                        Toast.makeText(context, "Read Success current bal" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                    System.out.println("read Success current bal");
//
//                                    String curr = String.valueOf(read_current);
//                                    resultCurrenBal=curr;
//                                    int currnntt= Integer.parseInt(curr);
//
//                                    String prevv= String.valueOf(read_data5);
//                                    resultPrevbal=prevv;
//                                    int previo= Integer.parseInt(prevv);
//
//                                    balan.setText(currnntt+"");
//                                    prevbal.setText(previo+"");
//
//
//                                } else {
//                                    Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                            .show();
//                                    System.out.println("Read Failed");
//                                }
//
//                            } else {
//                                Toast.makeText(context, "Read Authentication Failed current bal",
//                                        Toast.LENGTH_SHORT).show();
//                                System.out.println("Authentication Failed");
//                            }
//
//
//
//                            readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                            if (readauthsuc3 == 0) {
//                                readsuc3 = apis.tama_read_target(
//                                        READ_EXPIRY, read_expiry);
//                                readsuc7 = apis.tama_read_target(
//                                        READ_IN_OUT, read_data7);
//                                readsuc8 = apis.tama_read_target(
//                                        READ_ATTND_TYPE, read_data8);
//
//
//                                if (readsuc3 == 0) {
////                                        Toast.makeText(context, "Read Success Expiry" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                    System.out.println("read Success expiry");
//                                    String expir= String.valueOf(read_expiry);
//                                    String inoutatt= String.valueOf(read_data7);
//                                    String atttyp= String.valueOf(read_data8);
//
//                                    int inout1= Integer.parseInt(inoutatt);
//                                    int atttyp1= Integer.parseInt(atttyp);
//                                    expir.replaceFirst("0", "");
//                                    expiry1.setText(expir);
//                                    attinout.setText(inout1+"");
//                                    attype.setText(atttyp1+"");
//
//
//
//                                } else {
//                                    Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                            .show();
//                                    System.out.println("Read Failed");
//                                }
//
//                            } else {
//                                Toast.makeText(context, "Read Authentication Failed expiry",
//                                        Toast.LENGTH_SHORT).show();
//                                System.out.println("Authentication Failed");
//                            }
//
//
//
//
//                            readauthsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                            if (readauthsuc5 == 0) {
//                                readsuc9 = apis.tama_read_target(
//                                        READ_LASTTRANS_MC_ID, read_data9);
//                                readsuc10 = apis.tama_read_target(
//                                        READ_TRANS_ID, read_data10);
//                                readsuc11 = apis.tama_read_target(
//                                        READ_TRANS_AMT, read_data11);
//
//                                if (readsuc9 == 0) {
////                                    Toast.makeText(context, "Read Success LAst trans mc ID "+read_data9,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last trans ID "+read_data10,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success Last Trans amt "+read_data11,
////                                            Toast.LENGTH_SHORT).show();
//                                    System.out.println("read Success");
//
//                                    String lasttransmc= String.valueOf(read_data9);
//                                    String lasttrnsid= String.valueOf(read_data10);
//                                    String lasttrnsamt= String.valueOf(read_data11);
//
//                                    Float lasttrnsamt1= Float.parseFloat(lasttrnsamt);
//
//                                    lasttrnsmcid.setText(lasttransmc);
//                                    lasttransid.setText(lasttrnsid);
//                                    lasttransamt.setText(lasttrnsamt1+"");
//
//
//                                } else {
//                                    Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                            .show();
//                                    System.out.println("Read Failed");
//                                }
//
//
//
//
//
//                            } else {
//                                Toast.makeText(context, "Authentication Failed",
//                                        Toast.LENGTH_SHORT).show();
//                                System.out.println("Authentication Failed");
//                            }
////
//                            readauthsuc6 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_SIXTH_SECT, key6, KEYA);
//
//
//                            if (readauthsuc6 == 0) {
//                                readsuc12 = apis.tama_read_target(
//                                        READ_LAST_RECH_MC_ID, read_data12);
//                                readsuc13 = apis.tama_read_target(
//                                        READ_LAST_RECH_ID, read_data13);
//                                readsuc13 = apis.tama_read_target(
//                                        READ_LAST_RECH_AMT, read_data14);
//
//                                if (readsuc12 == 0) {
////                                    Toast.makeText(context, "Read Success  last rech mc id "+read_data12,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech Id "+read_data13,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech amt "+read_data14,
////                                            Toast.LENGTH_SHORT).show();
//                                    System.out.println("read Success");
//                                    String lastmc= String.valueOf(read_data12);
//                                    String lastrecid= String.valueOf(read_data13);
//                                    String lastrecamt= String.valueOf(read_data14);
//
//                                    Float lastrecamt1= Float.parseFloat(lastrecamt);
//
//                                    lastrechmcid.setText(lastmc);
//                                    lastrechid.setText(lastrecid);
//                                    lastrechamt.setText(lastrecamt1+"");
//
//
//
//                                } else {
//                                    Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                            .show();
//                                    System.out.println("Read Failed");
//                                }
//
//
//
//
//
//
//                            } else {
//                                Toast.makeText(context, "Authentication Failed",
//                                        Toast.LENGTH_SHORT).show();
//                                System.out.println("Authentication Failed");
//                            }
//
////                            //Switching OFF RFID FIELD
////                            new FieldOffTask().execute();
//
//                        }catch(NumberFormatException e){
//
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//
//                            Toast.makeText(context, "Target Moved. Try Again", Toast.LENGTH_SHORT)
//                                    .show();
//                            Builder alert = new Builder(
//                                    StudentCodeInitNFC.this);
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
//                                StudentCodeInitNFC.this);
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
//
//        go_back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                finish();
//            }
//        });
//
//    }
//
//    private void DBwriteFunc() {
//
//
//        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
//
//// Get the date today using Calendar object.
//        Date today = Calendar.getInstance().getTime();
//
//        currentDateTimeString = df.format(today);
//
//        transid=db.lastTransID();
//
//
//        Success_Transaction success_transaction=new Success_Transaction(transid,userid,transtype_id,(float)0,(float)0,serial,currentDateTimeString,"");
//
//        db.addSuccesstransaction(success_transaction);
//
////        List<Success_Transaction> successtransaction = db.getAllSuccess();
////        for (Success_Transaction usp : successtransaction) {
////            String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
////                    "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
////                    + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
////                    "  Server timestamp  " + usp.getserver_timestamp();
////            System.out.println("CHILLAR CARDINIT success transaction table1 " + testpermission);
////
////        }
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if( Constants.AsyncFlag==0){
//            Toast.makeText(getApplicationContext(),"Please Wait..",Toast.LENGTH_SHORT).show();
//        }else {
//            Constants.AsyncFlag=-1;
//            super.onBackPressed();
//        }
//    }
//
//
//
//
//    private class FieldRestartTask extends AsyncTask<Void,Void,Void> {
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
//
//
//    private boolean getDeviceStatus() {
//        mContext = this;
//        if (device == null) {
//            device = (RFCardReaderDevice) POSTerminal.getInstance(mContext)
//                    .getDevice("cloudpos.device.rfcardreader");
//        }
//        try {
//            device.open();
//            System.out.println("RFID open success");
//            return true;
//        } catch (DeviceException e) {
//            System.out.println("RFID open failed");
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private void callDeviceTap() {
//        try {
//            OperationListener listener = new OperationListener() {
//                @Override
//                public void handleResult(OperationResult arg0) {
//                    if (arg0.getResultCode() == OperationResult.SUCCESS) {
//                        System.out.println("************FIND CARD SUCCEED");
//                        rfCard = ((RFCardReaderOperationResult) arg0).getCard();
//                        System.out.println("************ Card Type" + rfCard.toString());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                buttonsColor(true);
////                                dialogbox("tap the card success", true);
//                            }
//                        });
//                        deviceTapStatus = true;
//                    } else {
//                        System.out.println("************FIND CARD FAILED");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                dialogbox("tap card failed", false);
//                            }
//                        });
//                        deviceTapStatus = false;
//                    }
//                }
//            };
//            device.listenForCardPresent(listener, TimeConstants.FOREVER);
//        } catch (DeviceException e) {
//            e.printStackTrace();
//            System.out.println("************FIND CARD ERROR " + e.toString());
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
////                    dialogbox("tap card failed", false);
//                }
//            });
//            deviceTapStatus = false;
//        }
//    }
//    private boolean verifyKey(int sectorIndex, byte[] key) {
//        boolean status = false;
//        try {
//            status = ((MifareCard) rfCard).verifyKeyA(sectorIndex, key);
//        } catch (DeviceException e) {
//            e.printStackTrace();
//        }
//        return status;
//    }
//}
