package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;


public class CardDetailsActivity extends Activity {

    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;

    public static final int READ_ONLINE_TRANS_ID = 2;
    public static final int READ_STUDENT_CODE = 1;
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

    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;
    private final List<String> BlkdCards = new ArrayList<>();
    public Button tap_the_card, go_back;
    char[] key1 = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
    char[] key2 = {0x52, 0x34, 0x3f, 0x36, 0x21, 0x37};
    char[] key3 = {0x53, 0x23, 0x61, 0x37, 0x5e, 0x39};
    char[] key4 = {0x42, 0x21, 0x62, 0x40, 0x49, 0x23};
    char[] key5 = {0x50, 0x24, 0x72, 0x40, 0x41, 0x5e};
    char[] key6 = {0x43, 0x25, 0x63, 0x31, 0x5e, 0x39};
    StringBuffer read_serial = new StringBuffer();
    StringBuffer read_current = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
    StringBuffer read_stdCode = new StringBuffer();
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
    StringBuffer read_data15 = new StringBuffer();
    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();
    char[] write_data = {'0', '0', '0', '0', '4', '1', '2', '6', '7', '2', '2', 'a', '2', '3',
            '3', 'e'};
    Context context;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int authsuc1, authsuc2, authsuc3, authsuc4, authsuc5, authsuc6, readauthsuc0, readauthsuc1, readauthsuc2, readauthsuc3, readauthsuc4, readauthsuc5, readauthsuc6;
    int writesuc1, writesuc2, writesuc3, writesuc4, writesuc5, writesuc6, writesuc7, writesuc8, writesuc9, writesuc10, writesuc11, writesuc12, writesuc13;
    int readsuc0, readsuc00, readsuc1, readsuc2, readsuc3, readsuc4, readsuc5, readsuc6, readsuc7, readsuc8, readsuc9, readsuc10, readsuc11, readsuc12, readsuc13;
    int tama_on;
    //    DatabaseHandler db = new DatabaseHandler(this);
    DatabaseHandler db;
    int transtype_id, userid;
    String trans_id, machine_id;
    Float total = (float) 0;
    String resultCardSerial, resultPrevbal, resultCurrenBal;
    String User_name, Pass_word;
    TextView stdCode, cardsl, schIdd, balan, expiry, statustxt, prevbal, attype, attinout, lasttrnsmcid, lasttransid, lasttransamt, lastrechmcid, lastrechid, lastrechamt;
    ProgressBar progressBar;

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_carddetails);

        context = getApplicationContext();

        db = DatabaseHandler.getInstance(getApplicationContext());


        tap_the_card = (Button) findViewById(R.id.tap);
        go_back = (Button) findViewById(R.id.tapback);


        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        cardsl = (TextView) findViewById(R.id.cardser);
        stdCode = (TextView) findViewById(R.id.stdCode);
        schIdd = (TextView) findViewById(R.id.schId);
        balan = (TextView) findViewById(R.id.balance);
        expiry = (TextView) findViewById(R.id.expiry);
        statustxt = (TextView) findViewById(R.id.status);
        prevbal = (TextView) findViewById(R.id.prevbalance);
        attype = (TextView) findViewById(R.id.att_typ);
        attinout = (TextView) findViewById(R.id.inout);
        lasttrnsmcid = (TextView) findViewById(R.id.lst_trnsmcid);
        lasttransid = (TextView) findViewById(R.id.lst_trnsid);
        lasttransamt = (TextView) findViewById(R.id.lst_trnsamt);
        lastrechmcid = (TextView) findViewById(R.id.lst_rechmcid);
        lastrechid = (TextView) findViewById(R.id.lst_rechid);
        lastrechamt = (TextView) findViewById(R.id.lst_rechamt);


        // WHEN TAPPING THE CARD ,IT WILL SHOW ALL THE DETAILS STORED IN THE CARD
//
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
//                            readauthsuc0 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_FIRST_SECT, key1, KEYA);
//
//                            if (readauthsuc0 == 0) {
//
//                                readsuc00 = apis.tama_read_target(READ_STUDENT_CODE
//                                        , read_stdCode);
//
//                                apis.tama_read_target(READ_ONLINE_TRANS_ID
//                                        , read_data15);
//                                System.out.println("Online Code: " + read_data15);
//
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
//                                readsuc4 = apis.tama_read_target(
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
//                            String stdcode = String.valueOf(read_stdCode);
////                        Toast.makeText(context, "SchoolID"+schId+" Length: "+schId.length(),
////                                Toast.LENGTH_LONG).show();
//
//                            String serial = String.valueOf(read_serial);
//                            resultCardSerial = serial;
//
//                            String status = String.valueOf(read_status);
//                            int status1 = Integer.parseInt(status);
//
//                            schIdd.setText(schId);
//                            statustxt.setText(status1 + "");
//                            cardsl.setText(serial);
//                            stdCode.setText(stdcode);
//
//
//                            if (schId.replace(" ", "").equalsIgnoreCase(db.getSchoolId())) {
//
//
////                            if(BlocardsCheck(serial)){
////
////                                finish();
////
////                            }else {
//
//
//                                readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                if (readauthsuc2 == 0) {
//                                    readsuc2 = apis.tama_read_target(
//                                            READ_CURR_BAL, read_current);
//                                    readsuc5 = apis.tama_read_target(
//                                            READ_PREV_BAL, read_data5);
//
//
//                                    if (readsuc2 == 0) {
////                                        Toast.makeText(context, "Read Success current bal" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("read Success current bal");
//
//                                        String curr = String.valueOf(read_current).replaceFirst("^0+(?!$)", "");
//                                        resultCurrenBal = curr;
//                                        Float currnntt = Float.parseFloat(curr);
//
//                                        String prevv = String.valueOf(read_data5).replaceFirst("^0+(?!$)", "");
//                                        ;
//                                        resultPrevbal = prevv;
//                                        Float previo = Float.parseFloat(prevv);
//
//                                        balan.setText(currnntt + "");
//                                        prevbal.setText(previo + "");
//
//
//                                    } else {
//                                        Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                .show();
//                                        System.out.println("Read Failed");
//                                    }
//
//                                } else {
//                                    Toast.makeText(context, "Read Authentication Failed current bal",
//                                            Toast.LENGTH_SHORT).show();
//                                    System.out.println("Authentication Failed");
//                                }
//
//
//                                readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                if (readauthsuc3 == 0) {
//                                    readsuc3 = apis.tama_read_target(
//                                            READ_EXPIRY, read_expiry);
//                                    readsuc7 = apis.tama_read_target(
//                                            READ_IN_OUT, read_data7);
//                                    readsuc8 = apis.tama_read_target(
//                                            READ_ATTND_TYPE, read_data8);
//
//
//                                    if (readsuc3 == 0) {
////                                        Toast.makeText(context, "Read Success Expiry" + read_current,
////                                                Toast.LENGTH_SHORT).show();
//                                        System.out.println("read Success expiry");
//
//                                        String expir = String.valueOf(read_expiry);
//                                        String inoutatt = String.valueOf(read_data7);
//                                        String atttyp = String.valueOf(read_data8);
//
//                                        int inout1 = Integer.parseInt(inoutatt);
//                                        int atttyp1 = Integer.parseInt(atttyp);
//
//                                        expiry.setText(expir);
//                                        attinout.setText(inout1 + "");
//                                        attype.setText(atttyp1 + "");
//
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
//
//                                readauthsuc5 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                if (readauthsuc5 == 0) {
//                                    readsuc9 = apis.tama_read_target(
//                                            READ_LASTTRANS_MC_ID, read_data9);
//                                    readsuc10 = apis.tama_read_target(
//                                            READ_TRANS_ID, read_data10);
//                                    readsuc11 = apis.tama_read_target(
//                                            READ_TRANS_AMT, read_data11);
//
//                                    if (readsuc9 == 0) {
////                                    Toast.makeText(context, "Read Success LAst trans mc ID "+read_data9,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last trans ID "+read_data10,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success Last Trans amt "+read_data11,
////                                            Toast.LENGTH_SHORT).show();
//                                        System.out.println("read Success");
//                                        String lasttransmc = String.valueOf(read_data9);
//                                        String lasttrnsid = String.valueOf(read_data10);
//                                        String lasttrnsamt = String.valueOf(read_data11);
//
//                                        Float lasttrnsamt1 = Float.parseFloat(lasttrnsamt);
//
//                                        lasttrnsmcid.setText(lasttransmc);
//                                        lasttransid.setText(lasttrnsid);
//                                        lasttransamt.setText(lasttrnsamt1 + "");
//
//
//                                    } else {
//                                        Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                                .show();
//                                        System.out.println("Read Failed");
//                                    }
//
//
//                                } else {
//                                    Toast.makeText(context, "Authentication Failed",
//                                            Toast.LENGTH_SHORT).show();
//                                    System.out.println("Authentication Failed");
//                                }
//
//
//                                readauthsuc6 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                        AUTH_SIXTH_SECT, key6, KEYA);
//
//
//                                if (readauthsuc6 == 0) {
//                                    readsuc12 = apis.tama_read_target(
//                                            READ_LAST_RECH_MC_ID, read_data12);
//                                    readsuc13 = apis.tama_read_target(
//                                            READ_LAST_RECH_ID, read_data13);
//                                    readsuc13 = apis.tama_read_target(
//                                            READ_LAST_RECH_AMT, read_data14);
//
//                                    if (readsuc12 == 0) {
////                                    Toast.makeText(context, "Read Success  last rech mc id "+read_data12,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech Id "+read_data13,
////                                            Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(context, "Read Success last rech amt "+read_data14,
////                                            Toast.LENGTH_SHORT).show();
//                                        System.out.println("read Success");
//                                        String lastmc = String.valueOf(read_data12);
//                                        String lastrecid = String.valueOf(read_data13);
//                                        String lastrecamt = String.valueOf(read_data14);
//
//                                        Float lastrecamt1 = Float.parseFloat(lastrecamt);
//
//                                        lastrechmcid.setText(lastmc);
//                                        lastrechid.setText(lastrecid);
//                                        lastrechamt.setText(lastrecamt1 + "");
//
//
//                                    } else {
//                                        Toast.makeText(context, "Read Failed", Toast.LENGTH_SHORT)
//                                                .show();
//                                        System.out.println("Read Failed");
//                                    }
//
//
//                                } else {
//                                    Toast.makeText(context, "Authentication Failed",
//                                            Toast.LENGTH_SHORT).show();
//                                    System.out.println("Authentication Failed");
//                                }
//
//
////                            }
//
//                            } else {
//                                Toast.makeText(context, "Card not belonging to this school!!",
//                                        Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//
//
//                        } catch (NumberFormatException e) {
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//                            e.printStackTrace();
//                            Toast.makeText(context, "Target Moved. Try Again", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    CardDetailsActivity.this);
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
//                                            new FieldRestartTask().execute();
////                                            Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                            startActivity(i);
////                                            finish();
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
//
//                        tap_the_card.setClickable(false);
//                        tap_the_card.setEnabled(false);
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                CardDetailsActivity.this);
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
////                                        Intent i=new Intent(getApplicationContext(),SecondActivity.class);
////                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                        startActivity(i);
////                                        finish();
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


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


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