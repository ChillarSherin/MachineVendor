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

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.Printer.PaymentPrinter;


public class Amount extends Activity {

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
    StringBuffer read_status = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();
    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
    Context context;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    ;
    int authsuc1, authsuc2, authsuc3, authsuc4, authsuc5, readauthsuc1, readauthsuc2, readauthsuc3;
    int writesuc1, writesuc2, writesuc3, writesuc4, writesuc5;
    int readsuc0, readsuc1, readsuc2, readsuc3, readsuc4, readsuc5, readsuc6;
    int tama_on;
    DatabaseHandler db;
    int transtype_id, userid;
    String trans_id, machine_id;
    Float total = (float) 0;
    String resultCardSerial, resultPrevbal;
    Float previousbalnce;
    Float currentbalance;
    String User_name, Pass_word;
    String print = "";
    String currentDateTimeString, currentDateTimeString1;
    Date expirydate, today1;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        db = DatabaseHandler.getInstance(getApplicationContext());


        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        context = getApplicationContext();
        tap_the_card = (Button) findViewById(R.id.tap);
        trans_id = db.lastTransID();
        machine_id = db.getMachineID();

        Bundle b = getIntent().getExtras();
        total = b.getFloat("amount");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);

        System.out.println("CHILL MachineID " + machine_id + " TransId " + trans_id + " Amount " + total);


        DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        currentDateTimeString = df.format(today);

        System.out.println("CurrentDAteTime: " + currentDateTimeString);


        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        // Get the date today using Calendar object.
        Date today11 = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        currentDateTimeString1 = df1.format(today11);

        // textView is the TextView view that should display it


        transtype_id = db.getTransTypID("PAYMENT");

        System.out.println("CHILL TransTypeID" + transtype_id);


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
//
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
//                                        String prev = String.valueOf(read_current);
//
//
//                                        Float Prev = Float.valueOf(prev);
//                                        previousbalnce = Prev;
//
//                                        if (Prev >= total) {
//                                            Float Current = (Prev - total);
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
//
//                                            String data = filler + curbal;
//
//                                            System.out.println("CHILLAR: Filler: " + data);
//
//                                            char[] curren = data.toCharArray();
//                                            char[] prevv = prev.toCharArray();
//
//
//                                            authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                            System.out.println("Authentication Success");
//
//
//                                            if (authsuc1 == 0) {
//
//
//                                                writesuc1 = apis.tama_write_target(WRITE_CURR_BAL,
//                                                        curren, curren.length);
//
//                                                writesuc2 = apis.tama_write_target(WRITE_PREV_BAL,
//                                                        prevv, prevv.length);
//
//
//                                                Toast.makeText(context, "writesuc " + writesuc1,
//                                                        Toast.LENGTH_SHORT).show();
//
//
//                                            } else {
//
//                                                Toast.makeText(context, "Authentication FAILED2",
//                                                        Toast.LENGTH_SHORT).show();
//
//                                            }
//
//
//                                            String filler1 = "";
//
//
//                                            int len1 = 16 - trans_id.length();
//                                            for (int j = 0; j < len1; ++j) {
//
//                                                filler1 = filler1 + "0";
//
//                                            }
//
//
//                                            String data1 = filler1 + trans_id;
//
//                                            System.out.println("CHILLAR: Filler transid: " + data1);
//
//                                            String filler2 = "";
//
//
//                                            int len2 = 16 - machine_id.length();
//                                            for (int j = 0; j < len2; ++j) {
//
//                                                filler2 = filler2 + "0";
//
//                                            }
//
//
//                                            String data2 = filler2 + machine_id;
//
//                                            System.out.println("CHILLAR: Filler machinid: " + data2);
//
//
//                                            char[] transs = data1.toCharArray();
//                                            char[] machine = data2.toCharArray();
//
//                                            String totall = String.valueOf(total);
//
//                                            String filler3 = "";
//
//
//                                            int len3 = 16 - totall.length();
//                                            for (int j = 0; j < len3; ++j) {
//
//                                                filler3 = filler3 + "0";
//
//                                            }
//
//
//                                            String data3 = filler3 + totall;
//
//                                            System.out.println("CHILLAR: Filler totalamt: " + data3);
//                                            char[] tott = data3.toCharArray();
//
//
//                                            authsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                    AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                            if (authsuc2 == 0) {
//
//
//                                                writesuc3 = apis.tama_write_target(WRITE_LASTTRANS_MC_ID,
//                                                        machine, machine.length);
//
//                                                writesuc4 = apis.tama_write_target(WRITE_TRANS_ID,
//                                                        transs, transs.length);
//                                                writesuc5 = apis.tama_write_target(WRITE_TRANS_AMT,
//                                                        tott, tott.length);
//                                            } else {
//
//                                                Toast.makeText(context, "Authentication FAILED3",
//                                                        Toast.LENGTH_SHORT).show();
//
//                                            }
//
//
//
//                                            /*
//                                             * Passing the values to another activity to print the bill
//                                             * */
//                                            if (authsuc1 == 0 && authsuc2 == 0 && (writesuc1 == 0) && (writesuc2 == 0) && (writesuc3 == 0) && (writesuc4 == 0) && (writesuc5 == 0)) {
//
//                                                Constants.sales_items.clear();
//                                                paytransaction();
//
//                                                print = print + "     TransID    " + trans_id + "\n" +
//                                                        "     Username   " + User_name + "\n" +
//                                                        "     CardSer    " + resultCardSerial + "\n" +
//                                                        "     Amount     " + total + "\n" +
//                                                        "     PrevBal    " + Float.parseFloat(prev) + "\n" +
//                                                        "     CurrBal    " + Float.parseFloat(curbal) + "\n";
//                                                Toast.makeText(context, "SUCCESS!!",
//                                                        Toast.LENGTH_SHORT).show();
//
//                                                Intent i = new Intent(getApplicationContext(), PaymentPrinter.class);
//                                                Bundle printbundle = new Bundle();
//                                                printbundle.putString("print", print);
//                                                printbundle.putString("Amount", String.valueOf(total));
//                                                i.putExtras(printbundle);
//                                                startActivity(i);
//                                                finish();
//
//
//                                            }
//                                        } else {
//                                            Toast.makeText(context, "Sorry,You do not have enough money balance in your Wallet.",
//                                                    Toast.LENGTH_SHORT).show();
//                                            finish();
//
//                                        }
////                                            }
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
//                            }
//
//                        } catch (NumberFormatException e) {
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    Amount.this);
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
//
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
//                    } else {
//                        tap_the_card.setClickable(false);
//                        tap_the_card.setEnabled(false);
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                Amount.this);
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


    /*
     * This function returns
     * Boolean #true- if the given card serial is present in the machines' blocked cards table
     * Boolean #false- if the given card serial is NOT present in the machines' blocked cards table
     * */

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
     * This function writes the transaction deatils to the machines' database upon successfull writing to the card.
     * */
    public void paytransaction() {

        String cardserialnumber = resultCardSerial;
        currentbalance = previousbalnce - total;

        String trans_id = db.lastTransID();

        String billno = trans_id.substring(12, 16);

        System.out.println("CODMOB: paymenttable BillNo" + billno);


        Payment_Transaction pay_transaction = new Payment_Transaction(trans_id, userid, transtype_id, previousbalnce, (float) currentbalance, cardserialnumber, currentDateTimeString, "", billno, trans_id, total, "");//,"transid2",7,8,9,5000,6000,55,"timestamp","servertimestamp");

        System.out.println("CODMOB: paymenttable Curren" + pay_transaction.getcurrent_balance());
        System.out.println("CODMOB: paymenttable PRev" + pay_transaction.getprevious_balnce());
        System.out.println("CODMOB: paymenttable Cardserial " + pay_transaction.getcard_serial());


        db.addSuccesstransaction(pay_transaction);
        db.addPaymenttransaction(pay_transaction);


    }



    /*
     * Constants.AsyncFlag is used the determine if the RFID adapter is in restart state.
     * If in restart state super.onBackPressed() is not invoked. It is invoked only after the restart state is completed.
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





    /*
     * Async Task to restart the RFID adapter.
     * */

    @Override
    protected void onDestroy() {
        super.onDestroy();
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