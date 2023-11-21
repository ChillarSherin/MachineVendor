package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Refund;
import com.chillarcards.machinevendor.ModelClass.Refund_Error;


public class RefundNFCNew extends Activity {

    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;


    public static final int READ_ONLINE_TRANS_ID = 2;
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



    public static final int WRITE_ONLINE_TRANS_ID = 2;
    public static final int WRITE_CURR_BAL = 8;
    public static final int WRITE_PREV_BAL = 9;
    public static final int WRITE_LASTTRANS_MC_ID = 16;
    public static final int WRITE_TRANS_ID = 17;
    public static final int WRITE_TRANS_AMT = 18;

    public static final int WRITE_LAST_RECH_MC_ID = 20;
    public static final int WRITE_LAST_RECH_ID = 21;
    public static final int WRITE_LAST_RECH_AMT = 22;


    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;

    char[] key1 = { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff };
    char[] oldkey1 = { 0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e };
    char[] key2 = { 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
    char[] key3 = { 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
    char[] key4 = { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
    char[] key5 = { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
    char[] key6 = { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };
    char[] keyNew ;

    StringBuffer read_serial = new StringBuffer();
    StringBuffer read_serial1 = new StringBuffer();
    StringBuffer read_current = new StringBuffer();
    StringBuffer read_current1 = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();
    StringBuffer read_status = new StringBuffer();
    StringBuffer read_status1 = new StringBuffer();
    StringBuffer read_data5 = new StringBuffer();
    StringBuffer read_trans_id = new StringBuffer();
    StringBuffer read_data8 = new StringBuffer();
    StringBuffer read_data9 = new StringBuffer();
    StringBuffer read_data10 = new StringBuffer();
    StringBuffer read_data11 = new StringBuffer();
    StringBuffer read_data12 = new StringBuffer();
    StringBuffer read_data13 = new StringBuffer();
    StringBuffer read_data14 = new StringBuffer();

    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();

    
    
    

    DatabaseHandler db ;

    Button tap_the_card;

    int authZeroSectRead=-1,authFirstSectRead=-1,authSecondSectRead=-1,authThirdSectRead=-1,authZeroSectWrite=-1,authSecondSectWrite=-1,authFifthSectorWrite=-1;
    int rdOnlineTransId=-1,rdSchoolId=-1,rdCardSerial=-1,rdCardStatus=-1,rdCurrentBal=-1,rdPrevBal=-1,rdExpiry=-1;
    int wrOnlineTransId=-1,wrCurrBal=-1,wrPrevBal=-1,wrLastRechMcId=-1,wrLastRechId=-1,wrlastRechAmt=-1;


    Date expirydate,today1;

    Context context;

    int ret_poweron;
    int ret_fieldon;

    int tardtc,tardtc1,tardtc2;

    int tama_on;

    int transtype_id;

    String trans_id,machine_id,rech_id,typeId,typeName;

    Float total=(float) 0;


    String resultCardSerial,resultPrevbal,resultCurrenBal,resultCardSerial1;

    String User_name,Pass_word,cardSl;
    int userid,sale_transid;
    String schooid,schoomachcode,machineserial,mSchoolId;
    Float currentbalance;
    int paymentTypeId;
    Float previousbalnce;
    String currentDateTimeString,currentDateTimeString1;
    String print="",paytypename,print2="";
    ProgressBar progressBar;

    String URL="";

    String TAG="RefundActivity";



    private final List<String> BlkdCards = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        initialise();


//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (total == (float) 0 || trans_id.equals(null) || trans_id.equals("")) {
//
//                    Toast.makeText(getApplicationContext(), "Sorry, Refund Failed. Try Again.", Toast.LENGTH_SHORT).show();
//                    finish();
//
//                }else{
//
//                    tap_the_card.setEnabled(false);
//                    tap_the_card.setClickable(false);
//
//                    if (SecondActivity.ret_fieldon == 0) {
//
//                        System.out.println("Tama open :" + tama_on);
//
//                        tardtc = apis.tama_detectTarget(TARGET_NUM,
//                                target_detected, target_id);
//
//                        if (tardtc == 0) {
//
//                            try {
//
//                                authFirstSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_SECND_SECT, key2, KEYA);
//
//                                if (authFirstSectRead == 0) {
//
//                                    rdSchoolId = apis.tama_read_target(READ_SCHOOL_ID
//                                            , read_school);
//                                    rdCardSerial = apis.tama_read_target(
//                                            READ_CARD_SERIAL, read_serial);
//                                    rdCardStatus = apis.tama_read_target(
//                                            READ_STATUS, read_status);
//
//
//                                    if (rdSchoolId == 0 && rdCardSerial == 0 && rdCardStatus == 0) {
//
//
//                                        String schId = String.valueOf(read_school);
//                                        resultCardSerial = String.valueOf(read_serial);
//                                        String cardstat = String.valueOf(read_status);
//                                        int cardstat1 = Integer.parseInt(cardstat);
//
//
//
//                                        /* Expiry Date Reading */
//                                    /*authThirdSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                    if (authThirdSectRead == 0) {
//                                        rdExpiry = apis.tama_read_target(
//                                                READ_EXPIRY, read_expiry);
//
//                                        if (rdExpiry == 0) {
//
//                                            String expdate = String.valueOf(read_expiry);
//                                            String expdate1 = expdate.replaceFirst("^0*", "");
//
//
//                                            cardExpired(expdate1);
//
//
//                                        }
//
//                                    }*/
//
//
//                                        /* Checking if card is active (0- inactive, 1 - active)*/
//                                        if (cardstat1 == 1) {
//
//                                            /*Check whether the card is blocked*/
//                                            if (!cardBlocked(resultCardSerial)) {
//
//                                                /*Check whether the card belongs to this school*/
//                                                if (schId.replace(" ", "").equalsIgnoreCase(mSchoolId)) {
//
//                                                    if (resultCardSerial.equals(cardSl)) {
//
//                                                        authSecondSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                                AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                        if (authSecondSectRead == 0) {
//                                                            rdCurrentBal = apis.tama_read_target(
//                                                                    READ_CURR_BAL, read_current);
//
//
//                                                            if (rdCurrentBal == 0) {
//
//                                                                String prev = String.valueOf(read_current).replaceFirst("^0+(?!$)", "");
//
//                                                                Float Prev = Float.valueOf(prev);
//                                                                Float Current = (Prev + total);
//
//                                                                char[] curBal = fillerFn(String.valueOf(Current));
//                                                                char[] prevBal = String.valueOf(read_current).toCharArray();
//
//
//                                                                authSecondSectWrite = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                                if (authSecondSectWrite == 0) {
//                                                                    wrCurrBal = apis.tama_write_target(WRITE_CURR_BAL,
//                                                                            curBal, curBal.length);
//
//                                                                    wrPrevBal = apis.tama_write_target(WRITE_PREV_BAL,
//                                                                            prevBal, prevBal.length);
//
//                                                                    if (wrCurrBal == 0 && wrPrevBal == 0) {
//
//
//                                                                        DBWriteRefund(trans_id,Prev,Current,total,resultCardSerial);
//
//                                                                    }else {
//                                                                        Toast.makeText(context, "Error 100!", Toast.LENGTH_SHORT).show();
//                                                                        finish();
//                                                                    }
//                                                                }else {
//                                                                    Toast.makeText(context, "Error 101!", Toast.LENGTH_SHORT).show();
//                                                                    finish();
//                                                                }
//                                                            }else {
//                                                                Toast.makeText(context, "Error 102!", Toast.LENGTH_SHORT).show();
//                                                                finish();
//                                                            }
//                                                        }else {
//                                                            Toast.makeText(context, "Error 103!", Toast.LENGTH_SHORT).show();
//                                                            finish();
//                                                        }
//
//                                                    }else{
//                                                        Toast.makeText(context, "Card Mismatch Error!", Toast.LENGTH_SHORT).show();
//                                                        finish();
//                                                    }
//
//                                                }else{
//                                                    Toast.makeText(context, "Card does not belong to this school!", Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                }
//                                            }else{
//                                                finish();
//                                            }
//                                        }else {
//                                            Toast.makeText(context, "Card not active!", Toast.LENGTH_SHORT).show();
//                                            finish();
//                                        }
//                                    }else {
//                                        Toast.makeText(context, "Error 104!", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//                                }else {
//                                    Toast.makeText(context, "Error 105!", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }
//                            } catch (Exception e) {
//                                tap_the_card.setEnabled(false);
//                                tap_the_card.setClickable(false);
//
//
//                                Toast.makeText(context, "Target Moved", Toast.LENGTH_SHORT)
//                                        .show();
//                                AlertDialog.Builder alert = new AlertDialog.Builder(
//                                        RefundNFCNew.this);
//                                alert.setTitle("RFID");
//
//                                alert.setCancelable(false);
//                                alert.setMessage("Target Moved");
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
//
//                                                new FieldRestartTask().execute();
//
//
//                                            }
//                                        });
//                                alert.show();
//
//                            }
//                        }else{
//                            tap_the_card.setEnabled(false);
//                            tap_the_card.setClickable(false);
//
//
//                            Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    RefundNFCNew.this);
//                            alert.setTitle("RFID");
//
//                            alert.setCancelable(false);
//                            alert.setMessage("Target detect Failed");
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
//
//                                            new FieldRestartTask().execute();
//
//
//                                        }
//                                    });
//                            alert.show();
//                        }
//                    }
//
//                }
//
//
//            }
//        });

    }

    private void DBWriteRefund(String old_trans_id, Float prev, Float current, Float total, String resultCardSerial) {


        String newTransId = db.lastTransID();
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int RetryCount1 = 10;
        int RetryCount2 = 10;
        int RetryCount3 = 10;


        int saleItemSize = db.getAllsalebyBill(old_trans_id).size();


        System.out.println("ChillarPay: saleItemSize: " + saleItemSize);


        while (count1 == 0 && RetryCount1 >= 0) {
            count1 = db.updatesuccessRefund1(old_trans_id);
            --RetryCount1;
            System.out.println("ChillarPay: Count1: " + count1 + " RetryCount1: " + RetryCount1);
        }


        while (count2 == 0 && RetryCount2 >= 0) {
            count2 = db.updateitemssRefund1(old_trans_id);
            --RetryCount2;
            System.out.println("ChillarPay: Count2: " + count2 + " RetryCount2: " + RetryCount2);
        }


        while (count3 < saleItemSize && RetryCount3 >= 0) {
            count3 = db.updatesaleRefund1(old_trans_id);
            --RetryCount3;
            System.out.println("ChillarPay: Count3: " + count3 + " RetryCount3: " + RetryCount3);
        }

        if (RetryCount1 == 0 || RetryCount2 == 0 || RetryCount3 == 0) {

            Refund_Error refund_error = new Refund_Error(old_trans_id, resultCardSerial, "");
            db.addRefundError(refund_error);

        }


        Refund refund = new Refund(newTransId, userid, transtype_id, prev, current, resultCardSerial, currentDateTimeString, "", newTransId, old_trans_id, total, "");

        long val1 = db.addSuccesstransaction(refund);
        long val2 = db.addRefund(refund);

        if(val1!=-1 && val2!=-1){

            Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(), RefundDetails.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
            b.putString("previobal", String.valueOf(prev));
            b.putString("currentbal", String.valueOf(current));
            b.putString("typeId", typeId);
            b.putString("typeName",typeName);
            i.putExtras(b);
            startActivity(i);
            finish();

        }else{
            Toast.makeText(context, "Refund failure!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void initialise() {

        db = DatabaseHandler.getInstance(getApplicationContext());

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        context = getApplicationContext();
        tap_the_card = (Button) findViewById(R.id.tap);


        Bundle b = getIntent().getExtras();
        total = b.getFloat("amount");
        trans_id = b.getString("transid1");
//        sale_transid = b.getInt("saletrans");
        cardSl = b.getString("cardserial");
        typeId = b.getString("typeId");
        typeName = b.getString("typeName");

        System.out.println("CODMOBTECH: amount " + total + " transid " + trans_id + " cardserail " + cardSl + " typeId " + typeId + " typeName " + typeName);


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);

        mSchoolId = db.getSchoolId();
        transtype_id = db.getTransTypID("REFUND");


        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);


        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        Date today11 = Calendar.getInstance().getTime();
        currentDateTimeString1 = df1.format(today11);
    }






    private boolean cardBlocked(String card) {


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
                Toast.makeText(getApplicationContext(), "Sorry, This card is blocked", Toast.LENGTH_SHORT).show();
                return true;
            }

        }

        return false;
    }


    private char[] fillerFn(String data){

        String filler = "";
        int len = 16 - data.length();

        for (int j = 0; j < len; ++j) {

            filler = filler + "0";

        }

        String dataResult = filler + data;

        System.out.println("Chillar Filler Data: "+ dataResult);

        char[] result=dataResult.toCharArray();


        return result;
    }


//    private class FieldRestartTask extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            apis.tama_close();
//
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
//
//                    ret_fieldon = SecondActivity.rfidService.rfid_rffield_on();
//
//                    System.out.println("RFID FieldOn :" + ret_fieldon);
//
//                    tama_on = apis.tama_open();
//
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


    @Override
    public void onBackPressed() {

        if (Constants.AsyncFlag == 0) {
            Toast.makeText(getApplicationContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
        } else {
            Constants.AsyncFlag = -1;
            super.onBackPressed();
        }
    }

}
