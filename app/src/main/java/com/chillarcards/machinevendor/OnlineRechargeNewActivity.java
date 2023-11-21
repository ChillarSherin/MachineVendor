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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Online_Recharge;
import com.chillarcards.machinevendor.ModelClass.Recharge_Data;


/**
 * Created by Codmob on 08-03-18.
 */

public class OnlineRechargeNewActivity extends Activity {

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

    int authZeroSectRead=-1,authFirstSectRead=-1,authSecondSectRead=-1,authThirdSectRead=-1,authZeroSectWrite=-1,authSecondSectWrite=-1;
    int rdOnlineTransId=-1,rdSchoolId=-1,rdCardSerial=-1,rdCardStatus=-1,rdCurrentBal=-1,rdPrevBal=-1,rdExpiry=-1;
    int wrOnlineTransId=-1,wrCurrBal=-1,wrPrevBal=-1;


    Date expirydate,today1;

    Context context;

    int ret_poweron;
    int ret_fieldon;

    int tardtc,tardtc1,tardtc2;

    int tama_on;

    int transtype_id;

    String trans_id,machine_id,rech_id;

    Float total=(float) 0;
    String tranact_id;

    String resultCardSerial,resultPrevbal,resultCurrenBal,resultCardSerial1;

    String User_name,Pass_word;
    int userid;
    String schooid,schoomachcode,machineserial,mSchoolId;
    Float currentbalance;
    int paymentTypeId;
    Float previousbalnce;
    String currentDateTimeString,currentDateTimeString1;
    String print="",paytypename,print2="";
    ProgressBar progressBar;
    String online;
    String URL="";

    String TAG="OnlineRechargeActivity";

    boolean alreadyExecuted = false;


    private final List<String> BlkdCards = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);


        initialise();


//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                alreadyExecuted = false;
//                tap_the_card.setEnabled(false);
//                tap_the_card.setClickable(false);
//                progressBar.setVisibility(View.VISIBLE);
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
//                        try{
//
//                            /*Trying to open Zeroeth sector with default key*/
//                            authZeroSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FIRST_SECT, key1, KEYA);
//
//                            /* if 0- Zeroeth sect key is default key, if -1 - Zeroeth sect key is Chillar key */
//                            if(authZeroSectRead==0){
//
//                                keyNew=key1;
//
//                                rdOnlineTransId = apis.tama_read_target(READ_ONLINE_TRANS_ID
//                                        , read_trans_id);
//
//                                if(rdOnlineTransId==0){
//
//                                    online=String.valueOf(read_trans_id);
//
//                                    /* Authenticating and Reading second sector for card serial,school id and card status */
//
//                                    authFirstSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_SECND_SECT, key2, KEYA);
//
//                                    if (authFirstSectRead == 0) {
//
//                                        rdSchoolId = apis.tama_read_target(READ_SCHOOL_ID
//                                                , read_school);
//                                        rdCardSerial = apis.tama_read_target(
//                                                READ_CARD_SERIAL, read_serial);
//                                        rdCardStatus = apis.tama_read_target(
//                                                READ_STATUS, read_status);
//
//
//                                        if(rdSchoolId==0 && rdCardSerial==0 && rdCardStatus==0){
//
//
//                                            String schId= String.valueOf(read_school);
//                                            resultCardSerial=String.valueOf(read_serial);
//                                            String cardstat = String.valueOf(read_status);
//                                            int cardstat1 = Integer.parseInt(cardstat);
//
//
//
//                                            /* Expiry Date Reading */
//                                            authThirdSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                            if (authThirdSectRead == 0) {
//                                                rdExpiry = apis.tama_read_target(
//                                                        READ_EXPIRY, read_expiry);
//
//                                                if (rdExpiry == 0) {
//
//                                                    String expdate = String.valueOf(read_expiry);
//                                                    String expdate1 = expdate.replaceFirst("^0*", "");
//
//
//                                                    cardExpired(expdate1);
//
//
//                                                }
//
//                                            }
//
//
//                                            /* Checking if card is active (0- inactive, 1 - active)*/
//                                            if(cardstat1 == 1){
//
//                                                /*Check whether the card is blocked*/
//                                                if(!cardBlocked(resultCardSerial)){
//
//                                                    /*Check whether the card belongs to this school*/
//                                                    if (schId.replace(" ", "").equalsIgnoreCase(mSchoolId)) {
//
//                                                        /*Checking Online TransactionID obtained from zeroth sector*/
//                                                        try {
//
//                                                            String result = online.substring(0, online.indexOf("_"));
//                                                            boolean digitsOnly = TextUtils.isDigitsOnly(result);
//
//                                                            long online2= Integer.parseInt(result);
//
//                                                            System.out.println("Chillar : digitsOnly "+digitsOnly);
//
//                                                            if(digitsOnly){
//
//                                                                if (online.equals("0000000000000000")){
//
//                                                                    //Block initialised card - First time New Online recharging
//                                                                    System.out.println("Chillar : online "+online);
//
//                                                                    if (!alreadyExecuted) {
//                                                                        GetAmountfromserverNew(resultCardSerial, "");
//                                                                    } else {
//                                                                        Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                    }
//
//
//                                                                }else{
//                                                                    System.out.println("Chillar : online else ");
//                                                                    // Not first time ,Already New Online recharged
//                                                                    String onlinetrans = online.replaceFirst("^0*", "");
//                                                                    System.out.println("CHILLRRRR:Online " + onlinetrans);
//                                                                    String resultOnline = onlinetrans.substring(0, onlinetrans.indexOf("_"));
//
//
//                                                                    if (!alreadyExecuted) {
//                                                                        GetAmountfromserverNew(resultCardSerial, resultOnline);
//                                                                    } else {
//                                                                        Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                    }
//
//                                                                }
//
//
//                                                            }else {
//                                                                System.out.println("Chillar : online elsedsf ");
//                                                                String online_transid="0000000000000000";
//                                                                char[] online1=online_transid.toCharArray();
//
//                                                                authZeroSectWrite = apis.tama_authenticate_mifare(TARGET_NUM,AUTH_FIRST_SECT,key1,KEYA);
//                                                                if (authZeroSectWrite == 0) {
//
//                                                                    wrOnlineTransId = apis.tama_write_target(WRITE_ONLINE_TRANS_ID,
//                                                                            online1, online1.length);
//
//
//                                                                    if(wrOnlineTransId==0){
//                                                                        System.out.println("Chillar : online wrOnlineTransId "+wrOnlineTransId);
//
//
//                                                                        if (!alreadyExecuted) {
//                                                                            GetAmountfromserverNew(resultCardSerial, "");
//                                                                        } else {
//                                                                            Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                        }
//
//
//                                                                    }else{
//
//                                                                        Toast.makeText(context, "Error 400!", Toast.LENGTH_SHORT).show();
//                                                                        finish();
//
//                                                                    }
//
//
//                                                                } else {
//
//                                                                    Toast.makeText(context, "Error 401!", Toast.LENGTH_SHORT).show();
//                                                                    finish();
//
//                                                                }
//
//                                                            }
//
//
//
//
//                                                        }catch (Exception e){
//
//
//                                                            e.printStackTrace();
//
//                                                            //Block NOT initialised card - First time New Online recharging
//                                                            String online_transid="0000000000000000";
//                                                            char[] online1=online_transid.toCharArray();
//
//                                                            authZeroSectWrite = apis.tama_authenticate_mifare(TARGET_NUM,AUTH_FIRST_SECT,key1,KEYA);
//
//                                                            if (authZeroSectWrite == 0) {
//
//
//
//                                                                wrOnlineTransId = apis.tama_write_target(WRITE_ONLINE_TRANS_ID,
//                                                                        online1, online1.length);
//
//
//                                                                if(wrOnlineTransId==0){
//
//
//                                                                    if (!alreadyExecuted) {
//                                                                        GetAmountfromserverNew(resultCardSerial, "");
//                                                                    } else {
//                                                                        Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                    }
//
//                                                                }else{
//
//                                                                    Toast.makeText(context, "Error 402!", Toast.LENGTH_SHORT).show();
//                                                                    finish();
//
//                                                                }
//
//
//                                                            }
//                                                            else {
//
//                                                                Toast.makeText(context, "Error 403!", Toast.LENGTH_SHORT).show();
//                                                                finish();
//
//                                                            }
//
//
//                                                        }
//
//
//                                                    }else{
//
//                                                        Toast.makeText(context, "Sorry, Card doesn't belong to this school!", Toast.LENGTH_SHORT).show();
//                                                        finish();
//
//                                                    }
//
//
//                                                }else{
//
//                                                    finish();
//
//                                                }
//
//
//                                            } else{
//
//                                                Toast.makeText(context, "Sorry, Card Not Active!", Toast.LENGTH_SHORT).show();
//                                                finish();
//
//                                            }
//
//
//                                        }else {
//
//                                            Toast.makeText(context, "Error 101!", Toast.LENGTH_SHORT).show();
//                                            finish();
//
//                                        }
//
//
//                                    }else {
//
//                                        Toast.makeText(context, "Error 100!", Toast.LENGTH_SHORT).show();
//                                        finish();
//
//                                    }
//
//                                }else {
//
//                                    Toast.makeText(context, "Error 201!", Toast.LENGTH_SHORT).show();
//                                    finish();
//
//                                }
//
//
//
//                            }else{
//
//                                /*
//                                * First Sector authentication failed with FFFF Key. So its a Chillar Key authenticated card. Opening with chillar key
//                                * */
//                                tardtc1 = apis1.tama_detectTarget(TARGET_NUM,
//                                        target_detected, target_id);
//
//                                if (tardtc1 == 0){
//
//
//                                    Log.d("Chillar","Old Card");
//
//                                    try{
//
//                                       /*Trying to open Zeroeth sector with default key*/
//                                        authZeroSectRead = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_FIRST_SECT, oldkey1, KEYA);
//
//                                        /* if 0- Zeroeth sect key is default key, if -1 - Zeroeth sect key is Chillar key */
//                                        if(authZeroSectRead==0) {
//
//                                            keyNew = oldkey1;
//
//                                            rdOnlineTransId = apis1.tama_read_target(READ_ONLINE_TRANS_ID
//                                                    , read_trans_id);
//
//                                            if (rdOnlineTransId == 0) {
//
//                                                online = String.valueOf(read_trans_id);
//
//
//                                                /* Authenticating and Reading second sector for card serial,school id and card status */
//                                                authFirstSectRead = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                        AUTH_SECND_SECT, key2, KEYA);
//
//                                                if (authFirstSectRead == 0) {
//
//                                                    rdSchoolId = apis1.tama_read_target(READ_SCHOOL_ID
//                                                            , read_school);
//                                                    rdCardSerial = apis1.tama_read_target(
//                                                            READ_CARD_SERIAL, read_serial);
//                                                    rdCardStatus = apis1.tama_read_target(
//                                                            READ_STATUS, read_status);
//
//
//                                                    if (rdSchoolId == 0 && rdCardSerial == 0 && rdCardStatus == 0) {
//
//
//                                                        String schId = String.valueOf(read_school);
//                                                        resultCardSerial = String.valueOf(read_serial);
//                                                        String cardstat = String.valueOf(read_status);
//                                                        int cardstat1 = Integer.parseInt(cardstat);
//
//
//
//                                                        /* Expiry Date Reading */
//                                                        authThirdSectRead = apis1.tama_authenticate_mifare(TARGET_NUM,
//                                                                AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                                        if (authThirdSectRead == 0) {
//                                                            rdExpiry = apis1.tama_read_target(
//                                                                    READ_EXPIRY, read_expiry);
//
//                                                            if (rdExpiry == 0) {
//
//                                                                String expdate = String.valueOf(read_expiry);
//                                                                String expdate1 = expdate.replaceFirst("^0*", "");
//
//
//                                                                cardExpired(expdate1);
//
//
//                                                            }
//
//                                                        }
//
//
//                                                        /* Checking if card is active (0- inactive, 1 - active)*/
//                                                        if (cardstat1 == 1) {
//
//                                                            /*Check whether the card is blocked*/
//                                                            if (!cardBlocked(resultCardSerial)) {
//
//                                                                /*Check whether the card belongs to this school*/
//                                                                if (schId.replace(" ", "").equalsIgnoreCase(mSchoolId)) {
//
//                                                                    /*Checking Online TransactionID obtained from zeroth sector*/
//                                                                    try {
//                                                                        String result = online.substring(0, online.indexOf("_"));
//                                                                        boolean digitsOnly = TextUtils.isDigitsOnly(result);
//
//                                                                        long online2= Integer.parseInt(result);
//                                                                        if (digitsOnly) {
//
//                                                                            if (online.equals("0000000000000000")) {
//
//                                                                                //Block initialised card - First time New Online recharging
//
//
//                                                                                if (!alreadyExecuted) {
//                                                                                    GetAmountfromserverNew(resultCardSerial, "");
//                                                                                } else {
//                                                                                    Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                                }
//
//
//                                                                            } else {
//
//                                                                                // Not first time ,Already New Online recharged
//                                                                                String onlinetrans = online.replaceFirst("^0*", "");
//                                                                                System.out.println("CHILLRRRR:Online " + onlinetrans);
//                                                                                String resultOnline = onlinetrans.substring(0, onlinetrans.indexOf("_"));
//
//
//                                                                                if (!alreadyExecuted) {
//                                                                                    GetAmountfromserverNew(resultCardSerial, resultOnline);
//                                                                                } else {
//                                                                                    Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                                }
//                                                                            }
//
//
//                                                                        } else {
//
//                                                                            String online_transid = "0000000000000000";
//                                                                            char[] online1 = online_transid.toCharArray();
//
//                                                                            authZeroSectWrite = apis1.tama_authenticate_mifare(TARGET_NUM, AUTH_FIRST_SECT, oldkey1, KEYA);
//                                                                            if (authZeroSectWrite == 0) {
//
//                                                                                wrOnlineTransId = apis1.tama_write_target(WRITE_ONLINE_TRANS_ID,
//                                                                                        online1, online1.length);
//
//
//                                                                                if (wrOnlineTransId == 0) {
//
//
//                                                                                    if (!alreadyExecuted) {
//                                                                                        GetAmountfromserverNew(resultCardSerial, "");
//                                                                                    } else {
//                                                                                        Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                                    }
//
//                                                                                } else {
//
//                                                                                    Toast.makeText(context, "Error 600!", Toast.LENGTH_SHORT).show();
//                                                                                    finish();
//
//                                                                                }
//
//
//                                                                            } else {
//
//                                                                                Toast.makeText(context, "Error 601!", Toast.LENGTH_SHORT).show();
//                                                                                finish();
//
//                                                                            }
//
//                                                                        }
//
//
//                                                                    } catch (Exception e) {
//
//
//                                                                        e.printStackTrace();
//
//                                                                        //Block NOT initialised card - First time New Online recharging
//                                                                        String online_transid = "0000000000000000";
//                                                                        char[] online1 = online_transid.toCharArray();
//
//                                                                        authZeroSectWrite = apis1.tama_authenticate_mifare(TARGET_NUM, AUTH_FIRST_SECT, oldkey1, KEYA);
//
//                                                                        if (authZeroSectWrite == 0) {
//
//
//                                                                            wrOnlineTransId = apis1.tama_write_target(WRITE_ONLINE_TRANS_ID,
//                                                                                    online1, online1.length);
//
//
//                                                                            if (wrOnlineTransId == 0) {
//
//
//                                                                                if (!alreadyExecuted) {
//                                                                                    GetAmountfromserverNew(resultCardSerial, "");
//                                                                                } else {
//                                                                                    Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
//                                                                                }
//
//                                                                            } else {
//
//                                                                                Toast.makeText(context, "Error 602!", Toast.LENGTH_SHORT).show();
//                                                                                finish();
//
//                                                                            }
//
//
//                                                                        } else {
//
//                                                                            Toast.makeText(context, "Error 603!", Toast.LENGTH_SHORT).show();
//                                                                            finish();
//
//                                                                        }
//
//
//                                                                    }
//
//
//                                                                } else {
//
//                                                                    Toast.makeText(context, "Sorry, Card doesn't belong to this school!", Toast.LENGTH_SHORT).show();
//                                                                    finish();
//
//                                                                }
//
//
//                                                            } else {
//
//                                                                finish();
//
//                                                            }
//
//
//                                                        } else {
//
//                                                            Toast.makeText(context, "Sorry, Card Not Active!", Toast.LENGTH_SHORT).show();
//                                                            finish();
//
//                                                        }
//
//
//                                                    } else {
//
//                                                        Toast.makeText(context, "Error 801!", Toast.LENGTH_SHORT).show();
//                                                        finish();
//
//                                                    }
//
//
//                                                } else {
//
//                                                    Toast.makeText(context, "Error 800!", Toast.LENGTH_SHORT).show();
//                                                    finish();
//
//                                                }
//
//                                            } else {
//
//                                                Toast.makeText(context, "Error 901!", Toast.LENGTH_SHORT).show();
//                                                finish();
//
//                                            }
//
//                                        } else{
//
//                                            Toast.makeText(context, "Error 700!", Toast.LENGTH_SHORT).show();
//                                            finish();
//                                        }
//
//
//
//
//
//                                    }catch(NumberFormatException e){
//
//                                        tap_the_card.setEnabled(false);
//                                        tap_the_card.setClickable(false);
//
//                                        e.printStackTrace();
//                                        Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                                .show();
//                                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                                OnlineRechargeNewActivity.this);
//                                        alert.setTitle("RFID");
//
//                                        alert.setCancelable(false);
//                                        alert.setMessage("Target Moved.Try again.");
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
//
//                                                        new FieldRestartTask().execute();
//
//                                                    }
//                                                });
//                                        alert.show();
//
//                                        System.out.println("Target detect Failed");
//                                    }catch(NullPointerException e){
//
//                                        Toast.makeText(context, "Card Error!!",
//                                                Toast.LENGTH_LONG).show();
//                                        finish();
//                                    }
//
//
//                                } else {
//                                    tap_the_card.setEnabled(false);
//                                    tap_the_card.setClickable(false);
//                                    Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                            .show();
//                                    AlertDialog.Builder alert = new AlertDialog.Builder(
//                                            OnlineRechargeNewActivity.this);
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
//
//                                                    new FieldRestartTask().execute();
//
//
//                                                }
//                                            });
//                                    alert.show();
//
//                                    System.out.println("Target detect Failed");
//                                }
//
//
//                            }
//
//
//
//                        }catch (NumberFormatException e){
//
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//                            e.printStackTrace();
//                            System.out.println("CODMOB: number format exception" + e.toString());
//                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    OnlineRechargeNewActivity.this);
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
//                                            Constants.sales_items.clear();
//
//                                            dialog.dismiss();
//
//
//                                            new FieldRestartTask().execute();
//
//
//                                        }
//                                    });
//                            alert.show();
//
//                            System.out.println("Target detect Failed");
//                        }
//
//
//
//                    } else {
//                        tap_the_card.setEnabled(false);
//                        tap_the_card.setClickable(false);
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                OnlineRechargeNewActivity.this);
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
//
//                                        new FieldRestartTask().execute();
//
//
//                                    }
//                                });
//                        alert.show();
//
//                        System.out.println("Target detect Failed");
//                    }
//                }
//
//            }
//
//        });
//




    }

    private void initialise() {

        context = getApplicationContext();

        db = DatabaseHandler.getInstance(getApplicationContext());

        tap_the_card = (Button) findViewById(R.id.tap);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid= editor.getInt("userid",0);
        schooid = editor.getString("schoolId", "");
        schoomachcode= editor.getString("schlMachCode", "");
        machineserial= editor.getString("machineserial", "");


        Bundle b=getIntent().getExtras();
        paymentTypeId = b.getInt("paymenttypeId");
        paytypename=b.getString("paytypename");


        machine_id= db.getMachineID();
        rech_id= String.valueOf(db.lastRechID());
        transtype_id=db.getTransTypID("RECHARGE");
        mSchoolId= db.getSchoolId();





        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);


        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        Date today11 = Calendar.getInstance().getTime();
        currentDateTimeString1 = df1.format(today11);

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


    private boolean cardExpired(String expDate){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            expirydate = sdf.parse(expDate);
            today1 = sdf.parse(currentDateTimeString1);

        } catch (ParseException ex) {

            ex.printStackTrace();
        }

        if(today1.after(expirydate)){

            return true;

        }else{

            return false;

        }

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


    private void GetAmountfromserverNew(String cardSerial, String onlinetrans) {

        String tag_string_req="NewOnlineRecharge";


        if(onlinetrans.equals("")){

            System.out.println("GetAmountfromserverNew"+" firsttime ");
            URL= Constants.APP_URL+"r_next_online_recharge.php?machineUserName="+User_name.replace(" ","%20")+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode+"&cardSerial="+ cardSerial;

        }else{

            System.out.println("GetAmountfromserverNew"+ " Next time " );
            URL= Constants.APP_URL+"r_next_online_recharge.php?machineUserName="+User_name.replace(" ","%20")+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode+"&cardSerial="+ cardSerial+"&onlineTransactionID="+onlinetrans;

        }

        System.out.println("URL New Online " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());

                JSONObject jsobj;
                String server_respose;
                try {

                    jsobj = new JSONObject(response);
                    server_respose=jsobj.getString("transaction_id");
                    String amount=jsobj.getString("Amount");
                    tranact_id=server_respose;

                    System.out.println("GetAmountfromserver  statuscode "+server_respose+ " amount "+amount);
                    if (server_respose.length()>0)
                    {
                        total= Float.valueOf(amount);
                        if (!alreadyExecuted) {

                            if (db.CheckIsDataAlreadyInDBorNot(tranact_id)) {
                                Toast.makeText(context, "Online Transaction already in DB", Toast.LENGTH_SHORT).show();
                            } else {
                               // WriteToCard(total, tranact_id);
                                Toast.makeText(context, "--------OnlineRechargeNewActy", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            Toast.makeText(context, "Already downloaded to card.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    try {
                        jsobj = new JSONObject(response);
                        server_respose=jsobj.getString("status");
                        JSONObject jsonObject=new JSONObject(server_respose);

                        String code=jsonObject.getString("code");
                        String msg=jsonObject.getString("message");
                        if(code.equals("error")){
                            if(msg.equals("No Pending Recharges")){
                                Toast.makeText(getApplicationContext(),"Recharge Failed. No Pending Recharges.",Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();
                                finish();
                            }else if(msg.equals("Authentication Failed")){
                                Toast.makeText(getApplicationContext(),"Authentication Failed.",Toast.LENGTH_SHORT).show();
                                finish();
                            }else if(msg.equals("Required parameters were not found")){
                                Toast.makeText(getApplicationContext(),"Required parameters were not found.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("CODMOB: Error in Online Recharge"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network not available.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);




    }


//    private void WriteToCard(Float total1, String tranact_id1) {
//
//
//        try {
//            tardtc2 = apis2.tama_detectTarget(TARGET_NUM,
//                    target_detected, target_id);
//
//            if (tardtc2 == 0) {
//
//                authZeroSectRead = -1;
//                authFirstSectRead = -1;
//                authZeroSectRead = apis2.tama_authenticate_mifare(TARGET_NUM, AUTH_FIRST_SECT, keyNew, KEYA);
//                authFirstSectRead = apis2.tama_authenticate_mifare(TARGET_NUM, AUTH_SECND_SECT, key2, KEYA);
//
//                if(authZeroSectRead==0 && authFirstSectRead==0){
//
//                    rdCardStatus=-1;
//                    rdCardSerial=-1;
//                    rdCardStatus = apis2.tama_read_target(
//                            READ_STATUS, read_status1);
//                    rdCardSerial = apis2.tama_read_target(
//                            READ_CARD_SERIAL, read_serial1);
//
//                    if (rdCardStatus==0 && rdCardSerial==0 ) {
//
//                        resultCardSerial1 = String.valueOf(read_serial1);
//
//                        if (resultCardSerial.equals(resultCardSerial1)) {
//
//                            authSecondSectRead = apis2.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                            if (authSecondSectRead == 0) {
//                                rdCurrentBal = apis2.tama_read_target(
//                                        READ_CURR_BAL, read_current);
//
//
//                                if (rdCurrentBal == 0) {
//
//
//                                    String prev = String.valueOf(read_current).replaceFirst("^0+(?!$)", "");
//                                    Float Prev = Float.valueOf(prev);
//                                    previousbalnce = Prev;
//                                    Float Current =  (Prev + total1);
//
//                                    char[] curBal = fillerFn(String.valueOf(Current));
//                                    char[] prevBal= String.valueOf(read_current).toCharArray();
//
//
//                                    authSecondSectWrite = apis2.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_THIRD_SECT, key3, KEYA);
//
//
//
//                                    if (authSecondSectWrite == 0) {
//
//
//                                        wrCurrBal = apis2.tama_write_target(WRITE_CURR_BAL,
//                                                curBal, curBal.length);
//
//                                        wrPrevBal = apis2.tama_write_target(WRITE_PREV_BAL,
//                                                prevBal, prevBal.length);
//
//
//
//
//                                        if(wrCurrBal==0) {
//
//
//                                            /*Write new Transaction ID to card*/
//                                            char[] online1 = fillerFn(tranact_id1);
//                                            System.out.println("Chillar: online1: " + online1.length);
//
//                                            authZeroSectWrite = -1;
//                                            authZeroSectWrite = apis2.tama_authenticate_mifare(TARGET_NUM, AUTH_FIRST_SECT, keyNew, KEYA);
//                                            if (authZeroSectWrite == 0) {
//
//                                                wrOnlineTransId = -1;
//                                                wrOnlineTransId = apis2.tama_write_target(WRITE_ONLINE_TRANS_ID,
//                                                        online1, online1.length);
//
//                                                System.out.println("Chillar: Eldho: " + wrOnlineTransId);
//                                                System.out.println("Chillar: data: " + tranact_id1);
//
//
//                                                if (wrOnlineTransId == 0) {
//
//
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Error 807!", Toast.LENGTH_SHORT).show();
//
//                                                }
//
//
//                                            } else {
//
//                                                Toast.makeText(context, "Error 806!", Toast.LENGTH_SHORT).show();
//
//                                            }
//
//
//
//                                            WriteToDB();
//
//                                            authSecondSectRead=-1;
//                                            authSecondSectRead = apis2.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_THIRD_SECT, key3, KEYA);
//
//                                            if (authSecondSectRead == 0) {
//
//                                                rdCurrentBal=-1;
//                                                rdPrevBal=-1;
//
//                                                rdCurrentBal = apis2.tama_read_target(
//                                                        READ_CURR_BAL, read_current1);
//                                                rdPrevBal = apis2.tama_read_target(
//                                                        READ_PREV_BAL, read_data5);
//
//
//                                                if (rdCurrentBal == 0 && rdPrevBal == 0) {
//
//                                                    resultCurrenBal = String.valueOf(read_current1);
//                                                    resultPrevbal = String.valueOf(read_data5);
//
//
//                                                    progressBar.setVisibility(View.GONE);
//
//                                                    Intent i = new Intent(getApplicationContext(), BlockTransferNFC.class);
//                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    Bundle b = new Bundle();
//                                                    b.putString("previobal", resultPrevbal);
//                                                    b.putString("currentbal", resultCurrenBal);
//                                                    b.putString("check","1");
//                                                    b.putString("flag", "online");
//                                                    i.putExtras(b);
//                                                    startActivity(i);
//
//                                                    finish();
//
//
//                                                } else {
//                                                    Toast.makeText(context, "Recharge Successfull!!", Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                }
//
//                                            } else {
//
//                                                Toast.makeText(context, "Recharge Successfull!!", Toast.LENGTH_SHORT).show();
//                                                finish();
//                                            }
//
//
//                                        }else{
//
//                                            Toast.makeText(context, "Error 808! Recharge Failed!", Toast.LENGTH_SHORT).show();
//                                            finish();
//
//                                        }
//
//
//
//
//                                    } else {
//
//
//                                        Toast.makeText(context, "Error 805!", Toast.LENGTH_SHORT).show();
//                                        finish();
//
//                                    }
//
//
//
//
//                                } else {
//
//                                    Toast.makeText(context, "Error 803!", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }
//
//                            } else {
//
//                                Toast.makeText(context, "Error 804!", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//
//
//
//
//                        } else {
//
//                            Toast.makeText(getApplicationContext(), "Card mismatch error!  ", Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//
//
//                    } else {
//
//                        Toast.makeText(context, "Error 802!", Toast.LENGTH_SHORT).show();
//                        finish();
//
//                    }
//
//
//
//                }else{
//                    Toast.makeText(context, "Error 801!", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//
//
//            }
//
//
//        }catch (NumberFormatException e){
//
//            tap_the_card.setClickable(false);
//            tap_the_card.setEnabled(false);
//            e.printStackTrace();
//            System.out.println("CODMOB: number format exception" + e.toString());
//            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                    .show();
//            AlertDialog.Builder alert = new AlertDialog.Builder(
//                    OnlineRechargeNewActivity.this);
//            alert.setTitle("RFID");
//
//            alert.setCancelable(false);
//            alert.setMessage("Target Moved.Try again.");
//
//            DialogInterface.OnClickListener listener;
//            alert.setPositiveButton(
//                    "OK",
//                    new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(
//                                DialogInterface dialog,
//                                int which) {
//
//                            Constants.sales_items.clear();
//
//                            dialog.dismiss();
//
//                            new FieldRestartTask().execute();
//
//                        }
//                    });
//            alert.show();
//
//            System.out.println("Target detect Failed");
//        }
//
//
//    }

    @Override
    public void onBackPressed() {

        if( Constants.AsyncFlag==0){
            Toast.makeText(getApplicationContext(),"Please Wait..",Toast.LENGTH_SHORT).show();
        }else {
            Constants.AsyncFlag=-1;
            super.onBackPressed();
        }
    }

    private void WriteToDB() {


        String cardserialnumber= resultCardSerial;

        currentbalance= previousbalnce+total;


        trans_id=db.lastTransID();
        System.out.println("ChilTransID "+trans_id);

        System.out.println("ChilOnline TransID "+tranact_id);

        Online_Recharge online_recharge=new Online_Recharge(trans_id,tranact_id,"");
        db.addOnlineToRech(online_recharge);

        Recharge_Data recharg = new Recharge_Data(trans_id,userid,transtype_id,previousbalnce,(float)currentbalance,cardserialnumber,
                currentDateTimeString,"",trans_id,currentDateTimeString,total,paymentTypeId,"");
        db.addSuccesstransaction(recharg);
        db.addrecharge(recharg);


        List<Online_Recharge> online_recharges=db.getAllonline();

        for(Online_Recharge recharge:online_recharges){
            System.out.println("Chillllrr: OnlineRechID : "+recharge.getId()+" TransID "+recharge.getTransID()+" OnlineTransID "+recharge.getOnlineTransID()+" ServerTS "+recharge.getServerTimestamp());
        }

        alreadyExecuted = true;



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
//
//                    ret_fieldon = SecondActivity.rfidService.rfid_rffield_on();
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

}
