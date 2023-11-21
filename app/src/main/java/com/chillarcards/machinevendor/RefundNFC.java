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

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Payment_Type;
import com.chillarcards.machinevendor.ModelClass.Refund;
import com.chillarcards.machinevendor.ModelClass.Refund_Error;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;


public class RefundNFC extends Activity {

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
    private final List<String> BlkdCards = new ArrayList<>();
    public Button tap_the_card, go_back;
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
    Date expirydate, today1;

    //    static {
//
//        System.loadLibrary("gl11rfid");
//        System.out.println("Library Loaded");
//
//        rfidService = IRfidService.Stub.asInterface(ServiceManager
//                .getService("rfid"));
//        System.out.println("CHILLR: "+rfidService);
//    }
    //    
    char[] write_data = {'0', '0', '0', '0', '4', '1', '2', '6', '7', '2', '2', 'a', '2', '3',
            '3', 'e'};
    Context context;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int authsuc1, authsuc2, authsuc3, authsuc4, authsuc5, authsuc6, readauthsuc1, readauthsuc2, readauthsuc3, readauthsuc4, readauthsuc5, readauthsuc6;
    int writesuc1, writesuc2, writesuc3, writesuc4, writesuc5, writesuc6, writesuc7, writesuc8, writesuc9, writesuc10, writesuc11, writesuc12, writesuc13;
    int readsuc0, readsuc1, readsuc2, readsuc3, readsuc4, readsuc5, readsuc6, readsuc7, readsuc8, readsuc9, readsuc10, readsuc11, readsuc12, readsuc13;
    int tama_on;
    DatabaseHandler db;
    int transtype_id, sale_transid;
    String trans_id, machine_id, rech_id;
    String transId;
    Float total = (float) 0;
    String resultCardSerial, resultPrevbal, resultCurrenBal;
    String cardSl;
    String User_name, Pass_word;
    int userid;
    Float currentbalance;
    int paymentTypeId;
    Float previousbalnce;
    String currentDateTimeString, currentDateTimeString1;
    String print = "", paytypename, print2 = "";
    ProgressBar progressBar;


    //THIS ACTIVITY IS USING FOR REFUND  TRANSACTION

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        db = DatabaseHandler.getInstance(getApplicationContext());

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        context = getApplicationContext();
        tap_the_card = (Button) findViewById(R.id.tap);


        Bundle b = getIntent().getExtras();
        total = b.getFloat("amount");
        trans_id = b.getString("transid1");
        sale_transid = b.getInt("saletrans");
        cardSl = b.getString("cardserial");

        System.out.println("CODMOBTECH: amount " + total + " transid " + trans_id + " cardserail " + cardSl);


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);


        transtype_id = db.getTransTypID("REFUND");
        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");


        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
//        currentDateTimeString=currentDateTimeString.replace(" ","%20");

        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

// Get the date today using Calendar object.
        Date today11 = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        currentDateTimeString1 = df1.format(today11);


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
//                    if (tardtc == 0) {
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
//
//                            String schId = String.valueOf(read_school);
//                            String serial = String.valueOf(read_serial);
//                            resultCardSerial = serial;
//
//
//                            String cardstat = String.valueOf(read_status);
//
//                            int cardstat1 = Integer.parseInt(cardstat);
//
//                            if (cardstat1 == 0) {
//
//                                finish();
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
////                                        if(today1.after(expirydate)){
////
////                                            System.out.println("ELDHO: card expired");
////                                            Toast.makeText(getApplicationContext(),"Card already expired. ",Toast.LENGTH_LONG).show();
////                                            finish();
////                                        }else {
//
//                                        if (resultCardSerial.equals(cardSl)) {
//
//
//                                            readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                            if (readauthsuc2 == 0) {
//                                                readsuc1 = apis.tama_read_target(
//                                                        READ_CURR_BAL, read_current);
//
//
//                                                if (readsuc1 == 0) {
//
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
//                                            Float Prev = Float.valueOf(prev);
//                                            previousbalnce = Prev;
//                                            Float Current = (Prev + total);
//                                            String filler = "";
//
//                                            String curbal = String.valueOf(Current);
//                                            int len = 16 - curbal.length();
//                                            for (int j = 0; j < len; ++j) {
//
//                                                filler = filler + "0";
//
//                                            }
//
//                                            String data = filler + curbal;
//
//                                            System.out.println("CHILLAR: Filler: " + data);
//
//                                            char[] curren = data.toCharArray();
//                                            char[] prevv = prev.toCharArray();
//
//
//                                            ///////////////////////////////////////////
//
//                                            if (total == (float) 0 || trans_id.equals(null) || trans_id.equals("")) {
//
//
//                                                //Error Condition
//
//                                                Toast.makeText(getApplicationContext(), "Sorry, Refund Failed. Try Again.", Toast.LENGTH_SHORT).show();
//                                                finish();
//
//
//                                            } else {
//
//
//                                                authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                System.out.println("Authentication Success");
//
//
//                                                if (authsuc1 == 0) {
//
//
//                                                    writesuc1 = apis.tama_write_target(WRITE_CURR_BAL,
//                                                            curren, curren.length);
//
//                                                    writesuc2 = apis.tama_write_target(WRITE_PREV_BAL,
//                                                            prevv, prevv.length);
//
//
//                                                } else {
//
//                                                    Toast.makeText(context, "Authentication FAILED2",
//                                                            Toast.LENGTH_SHORT).show();
//
//                                                }
//
//                                                if (authsuc1 == 0 && writesuc1 == 0 && writesuc2 == 0) {
//
//                                                    Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
//
//                                                    GetRecharge();
//
//
//                                                    readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                            AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                    if (readauthsuc2 == 0) {
//                                                        readsuc2 = apis.tama_read_target(
//                                                                READ_CURR_BAL, read_current);
//                                                        readsuc5 = apis.tama_read_target(
//                                                                READ_PREV_BAL, read_data5);
//
//
//                                                        if (readsuc2 == 0) {
//
//                                                            System.out.println("read Success current bal");
//
//                                                            String curr = String.valueOf(read_current);
//                                                            resultCurrenBal = curr;
////                                                                int currnntt = Integer.parseInt(curr);
//
//                                                            String prevv1 = String.valueOf(read_data5);
//                                                            resultPrevbal = prevv1;
////                                                                int previo = Integer.parseInt(prevv1);
//
//
//                                                        } else {
//                                                            Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                                    .show();
//                                                            System.out.println("Read Failed");
//                                                        }
//
//                                                    } else {
//                                                        Toast.makeText(context, "Read Authentication Failed current bal",
//                                                                Toast.LENGTH_SHORT).show();
//                                                        System.out.println("Authentication Failed");
//                                                    }
//
//
//                                                    readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                            AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                                    if (readauthsuc3 == 0) {
//                                                        readsuc3 = apis.tama_read_target(
//                                                                READ_EXPIRY, read_expiry);
//                                                        readsuc7 = apis.tama_read_target(
//                                                                READ_IN_OUT, read_data7);
//                                                        readsuc8 = apis.tama_read_target(
//                                                                READ_ATTND_TYPE, read_data8);
//
//
//                                                        if (readsuc3 == 0) {
//
//                                                            System.out.println("read Success expiry");
//
//                                                            String expir = String.valueOf(read_expiry);
//                                                            String inoutatt = String.valueOf(read_data7);
//                                                            String atttyp = String.valueOf(read_data8);
//
//                                                            int inout1 = Integer.parseInt(inoutatt);
//                                                            int atttyp1 = Integer.parseInt(atttyp);
//
////
//
//                                                        } else {
//                                                            Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                                    .show();
//                                                            System.out.println("Read Failed");
//                                                        }
//
//                                                    } else {
//                                                        Toast.makeText(context, "Read Authentication Failed expiry",
//                                                                Toast.LENGTH_SHORT).show();
//                                                        System.out.println("Authentication Failed");
//                                                    }
//
//
//                                                    Intent i = new Intent(getApplicationContext(), RefundDetails.class);
//                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    Bundle b = new Bundle();
//                                                    b.putString("previobal", resultPrevbal);
//                                                    b.putString("currentbal", resultCurrenBal);
//                                                    i.putExtras(b);
//
//                                                    startActivity(i);
//                                                    finish();
//
//
//                                                } else {
//
//                                                    Toast.makeText(getApplicationContext(), "Refund Failure!", Toast.LENGTH_SHORT).show();
//
//                                                }
//
//
//                                            }
//
//
//                                        } else {
//
//                                            Toast.makeText(getApplicationContext(), "Card mismatch error!  ", Toast.LENGTH_LONG).show();
//
//                                            finish();
//                                        }
////                                        }
//                                    } else {
//                                        Toast.makeText(context, "Card not belonging to this school!!",
//                                                Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//
//                                }
//
//                            }
//
//
//                        } catch (NumberFormatException e) {
//
//
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    RefundNFC.this);
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
////                                            Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                            startActivity(i);
////                                            finish();
//
//                                            new FieldRestartTask().execute();
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
//                                RefundNFC.this);
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


    /*
     * Function to write the transaction to Database
     */


    private void GetRecharge() {


        String cardserialnumber = resultCardSerial;

        currentbalance = previousbalnce + total;
        transId = db.lastTransID();


//        List<Refund> refunds=db.getAllRefund();
//        for (Refund refund1 : refunds){
//
//            String refundtrans="RefundID: "+refund1.getId()+ " New TransID: "+refund1.getTrans_id()+" Original TransID: "+refund1.getOrig_trans_id()+" ServerTS: "+refund1.getServer_timestamp() ;
//            System.out.println("CHILLAR REFUNDTABLE: "+refundtrans);
//
//        }


        System.out.println("ChillarPay: TxnID: " + trans_id);


        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int RetryCount1 = 10;
        int RetryCount2 = 10;
        int RetryCount3 = 10;


        int saleItemSize = db.getAllsalebyBill(trans_id).size();


        System.out.println("ChillarPay: saleItemSize: " + saleItemSize);


        while (count1 == 0 && RetryCount1 >= 0) {
            count1 = db.updatesuccessRefund1(trans_id);
            --RetryCount1;
            System.out.println("ChillarPay: Count1: " + count1 + " RetryCount1: " + RetryCount1);
        }


        while (count2 == 0 && RetryCount2 >= 0) {
            count2 = db.updateitemssRefund1(trans_id);
            --RetryCount2;
            System.out.println("ChillarPay: Count2: " + count2 + " RetryCount2: " + RetryCount2);
        }


        while (count3 < saleItemSize && RetryCount3 >= 0) {
            count3 = db.updatesaleRefund1(trans_id);
            --RetryCount3;
            System.out.println("ChillarPay: Count3: " + count3 + " RetryCount3: " + RetryCount3);
        }


//        Refund_Error refund_error1=new Refund_Error(trans_id,cardserialnumber,"");
//        db.addRefundError(refund_error1);


//        List<Refund_Error> refunds=db.getAllRefundError();
//        for (Refund_Error refund1 : refunds){
//
//            String refundtrans="RefundErrorID: "+refund1.getId()+ " Refund TransID: "+refund1.getTrans_id()+" CardSerial: "+refund1.getCardserial()+" ServerTS: "+refund1.getServerTimestamp() ;
//            System.out.println("CHILLAR REFUNDERRORTABLE: "+refundtrans);
//
//        }

        if (RetryCount1 == 0 || RetryCount2 == 0 || RetryCount3 == 0) {

            Refund_Error refund_error = new Refund_Error(trans_id, cardserialnumber, "");
            db.addRefundError(refund_error);

        }


        Refund refund = new Refund(transId, userid, transtype_id, previousbalnce, (float) currentbalance, cardserialnumber, currentDateTimeString, "", transId, trans_id, total, "");

        db.addSuccesstransaction(refund);
        db.addRefund(refund);


//        List<Success_Transaction> successtransactions = db.getAllsuccessbyBill(trans_id);
//        for (Success_Transaction usp : successtransactions) {
//            String testpermissionnew = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
//                    "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
//                    + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
//                    "  Server timestamp  " + usp.getserver_timestamp();
//            System.out.println("CODMOB: RefundNFC SxsTrnsxn " + testpermissionnew);
//
//
////            Success_Transaction success=new Success_Transaction(usp.gettrans_id(),usp.getuser_id(),usp.gettranstypeid(), usp.getprevious_balnce(),
////                    usp.getcurrent_balance(),usp.getcard_serial(),usp.gettime_stamp(),"-1");
//            System.out.println("ChillarPay: TxnID1: "+usp.gettrans_id());
//            db.updatesuccessRefund1(usp.gettrans_id());
////            db.close();
//
//        }
//
//        ////////////////////////////////////
//
//
////        List<Payment_Transaction> pay = db.getallPaymentbyBill(trans_id);
////        for (Payment_Transaction usp1 : pay) {
////            String testpermission = " transaction id  " + usp1.gettrans_id() + " billno1 :" + usp1.getbillno() +
////                    "   total:   " + usp1.getamount() +
////                    "  Server timestamp  " + usp1.getserver_timestamp() ;
////            System.out.println("CODMOB: RefundNFC Payment: " + testpermission);
////
//////            Item_Sale itemsale = new Item_Sale(usp1.getsale_trans_id(),usp1.gettransaction_id(), usp1.getbill_no(), usp1.gettot_amount(), "-1");
////            db.updatePaymentRefund1(usp1.gettrans_id());
////            db.close();
////
////
////        }
//
//
//        List<Item_Sale> success11 = db.getAllitemsalebyBill(trans_id);
//        for (Item_Sale usp1 : success11) {
//            String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
//                    "   total:   " + usp1.gettot_amount() +
//                    "  Server timestamp  " + usp1.getserver_timestamp() +
//                    "  SaleTransID " + usp1.getsale_trans_id();
//            System.out.println("CODMOB: RefundNFC ITEMSALE: " + testpermission);
//
//            Item_Sale itemsale = new Item_Sale(usp1.getsale_trans_id(),usp1.gettransaction_id(), usp1.getbill_no(), usp1.gettot_amount(), "-1");
//            System.out.println("ChillarPay: TxnID2: "+usp1.gettransaction_id());
//            db.updateitemssRefund1(usp1.gettransaction_id());
////            db.close();
//
//
//        }
//
//
//        List<Sales_Item> sales_item11 = db.getAllsalebyBill(trans_id);
//        for (Sales_Item slesitm : sales_item11) {
//            String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
//                    + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();
//
//
//            System.out.println("CODMOB: RefundNFC SALESITEM : " + testsalesitem);
//
//
//            Sales_Item salesitems = new Sales_Item(slesitm.getSales_item_id(), slesitm.getsales_trans_id(), slesitm.getitem_id(), slesitm.getitem_quantity(), slesitm.getamount(), "-1");
//
//            System.out.println("ChillarPay: TxnID3: "+slesitm.getsales_trans_id());
//            db.updatesaleRefund1(slesitm.getsales_trans_id());
////            db.close();
//
//
//        }


    }


    /*
     * Restart The NFC field.
     *
     * */

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
//
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