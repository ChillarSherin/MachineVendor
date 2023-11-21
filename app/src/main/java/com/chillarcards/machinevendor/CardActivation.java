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
public class CardActivation extends Activity {
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
    public static final int READ_KEY5 = 19;
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
    public static final int WRITE_KEY5 = 19;
    public static final int WRITE_KEY6 = 23;

    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;
    public Button read;
    public Button tap_the_card, go_back;
    //    char[] key1 = { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff };
    //    char[] key1 = { 0x30, 0x30, 0x30, 0x30, 0x30, 0x30 };
//    char[] key2 = { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff };
    char[] key2 = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
    char[] key3 = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
    char[] key4 = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
    char[] key5 = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
    char[] key6 = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
    char[] Newkey1 = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
    char[] Oldkey1 = {0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e};
    char[] Newkey2 = {0x52, 0x34, 0x3f, 0x36, 0x21, 0x37};
    char[] Newkey3 = {0x53, 0x23, 0x61, 0x37, 0x5e, 0x39};
    char[] Newkey4 = {0x42, 0x21, 0x62, 0x40, 0x49, 0x23};
    char[] Newkey5 = {0x50, 0x24, 0x72, 0x40, 0x41, 0x5e};
    char[] Newkey6 = {0x43, 0x25, 0x63, 0x31, 0x5e, 0x39};

    byte[] bytekey2 = new byte[] {(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff};
    byte[] bytekey3 = new byte[] {(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff};
    byte[] bytekey4 = new byte[] {(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff};
    byte[] bytekey5 = new byte[] {(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff};
    byte[] bytekey6 = new byte[] {(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff};
    byte[] byteNewkey1 = new byte[] {(byte) 0xff,(byte)  0xff,(byte)  0xff,(byte)  0xff,(byte)  0xff,(byte)  0xff};
    byte[] byteNewkey2 = new byte[] {(byte) 0x52,(byte)  0x34,(byte)  0x3f,(byte)  0x36,(byte)  0x21,(byte)  0x37};
    byte[] byteNewkey3 = new byte[] {(byte) 0x53,(byte)  0x23,(byte)  0x61,(byte)  0x37,(byte)  0x5e,(byte)  0x39};
    byte[] byteNewkey4 = new byte[] {(byte) 0x42,(byte)  0x21,(byte)  0x62,(byte)  0x40,(byte)  0x49,(byte)  0x23};
    byte[] byteNewkey5 = new byte[] {(byte) 0x50,(byte)  0x24,(byte)  0x72,(byte)  0x40,(byte)  0x41,(byte)  0x5e};
    byte[] byteNewkey6 = new byte[] {(byte) 0x43,(byte)  0x25,(byte)  0x63,(byte)  0x31,(byte)  0x5e,(byte)  0x39};

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
    String resultCardSerial, resultPrevbal, resultCurrenBal;



    int ret_poweron;
    int ret_fieldon;

    int tardtc, tardtc1;
    int newauthsuc1 = -2, newauthsuc2 = -2, newauthsuc3 = -2, newauthsuc4 = -2, newauthsuc5 = -2, newauthsuc6 = -2;
    int authsuc1 = -2, authsuc2 = -2, authsuc3 = -2, authsuc4 = -2, authsuc5 = -2, authsuc6 = -2, readauthsuc1 = -2, readauthsuc2 = -2, readauthsuc3 = -2,
            readauthsuc4 = -2, readauthsuc5 = -2, readauthsuc6 = -2;
    int writesuc1 = -2, writesuc2 = -2, writesuc3 = -2, writesuc4 = -2, writesuc5 = -2, writesuc6 = -2, writesuc7 = -2, writesuc8 = -2, writesuc9 = -2, writesuc10 = -2, writesuc11 = -2, writesuc12 = -2, writesuc13 = -2;
    boolean writeSuccess1 = false, writeSuccess2 = false, writeSuccess3 = false, writeSuccess4 = false, writeSuccess5 = false, writeSuccess6 = false, writeSuccess7 = false, writeSuccess8 = false, writeSuccess9 = false, writeSuccess10 = false, writeSuccess11 = false, writeSuccess12 = false, writeSuccess13 = false;
    int readsuc0, readsuc1, readsuc2, readsuc3, readsuc4, readsuc5, readsuc6, readsuc7, readsuc8, readsuc9, readsuc10, readsuc11, readsuc12, readsuc13;
    int tama_on;

    int flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag5 = 0, flag6 = 0;

    TextView cardsl, schIdd, balan, expiry1, statustxt, prevbal, attype, attinout, lasttrnsmcid, lasttransid, lasttransamt, lastrechmcid, lastrechid, lastrechamt;

    ProgressBar progressBar;
    Context currentActivityContext;
    boolean verifyResult;
    int sectorIndex = 1;
    int blockIndex = 0;
    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        context = getApplicationContext();
        currentActivityContext = CardActivation.this;
        tap_the_card = (Button) findViewById(R.id.tap);
//        tap_the_card.setClickable(false);
//        tap_the_card.setEnabled(false);


        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

//        new FieldOnTask().execute();

        //To Write New Key
//        final char[] chillkey1={ 0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey2={ 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37, 0xFF, 0x07, 0x80, 0x69, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11  };
//        final char[] chillkey3={ 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0x22, 0x22, 0x22, 0x22, 0x22, 0x22  };
//        final char[] chillkey4={ 0x42, 0x21, 0x62, 0x40, 0x49, 0x23, 0xFF, 0x07, 0x80, 0x69, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33  };
//        final char[] chillkey5={ 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e, 0xFF, 0x07, 0x80, 0x69, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44  };
//        final char[] chillkey6={ 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55 };


        final char[] chillkey2 = {0x52, 0x34, 0x3f, 0x36, 0x21, 0x37, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        final char[] chillkey3 = {0x53, 0x23, 0x61, 0x37, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        final char[] chillkey4 = {0x42, 0x21, 0x62, 0x40, 0x49, 0x23, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        final char[] chillkey5 = {0x50, 0x24, 0x72, 0x40, 0x41, 0x5e, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        final char[] chillkey6 = {0x43, 0x25, 0x63, 0x31, 0x5e, 0x39, 0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};

        final byte[] bytechillkey2 = new byte[] {(byte) 0x52,(byte) 0x34,(byte) 0x3f,(byte) 0x36,(byte) 0x21,(byte) 0x37,(byte) 0xFF,(byte) 0x07,(byte) 0x80,(byte) 0x69,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};
        final byte[] bytechillkey3 = new byte[] {(byte) 0x53,(byte) 0x23,(byte) 0x61,(byte) 0x37,(byte) 0x5e,(byte) 0x39,(byte) 0xFF,(byte) 0x07,(byte) 0x80,(byte) 0x69,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};
        final byte[] bytechillkey4 = new byte[] {(byte) 0x42,(byte) 0x21,(byte) 0x62,(byte) 0x40,(byte) 0x49,(byte) 0x23,(byte) 0xFF,(byte) 0x07,(byte) 0x80,(byte) 0x69,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};
        final byte[] bytechillkey5 = new byte[] {(byte) 0x50,(byte) 0x24,(byte) 0x72,(byte) 0x40,(byte) 0x41,(byte) 0x5e,(byte) 0xFF,(byte) 0x07,(byte) 0x80,(byte) 0x69,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};
        final byte[] bytechillkey6 = new byte[] {(byte) 0x43,(byte) 0x25,(byte) 0x63,(byte) 0x31,(byte) 0x5e,(byte) 0x39,(byte) 0xFF,(byte) 0x07,(byte) 0x80,(byte) 0x69,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF};


        //To write Old Key
//        final char[] chillkey1={ 0x41, 0x26, 0x72, 0x2a, 0x25, 0x3e ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF };
//        final char[] chillkey2={ 0x52, 0x34, 0x3f, 0x35, 0x21, 0x36 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey3={ 0x53, 0x23, 0x61, 0x38, 0x5e, 0x39 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey4={ 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey5={ 0x50, 0x24, 0x72, 0x25, 0x41, 0x5e ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };
//        final char[] chillkey6={ 0x43, 0x25, 0x63, 0x31, 0x5e, 0x37 ,0xFF, 0x07, 0x80, 0x69, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  };


        //ACTIVATE THE CARD USING CHILLAR KEY

        tap_the_card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Checking if the RFID module field is ON
//                if (SecondActivity.ret_fieldon == 0) {


            }
        });

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

        if (Constants.AsyncFlag == 0) {
            Toast.makeText(getApplicationContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
        } else {
            Constants.AsyncFlag = -1;
            super.onBackPressed();
        }
    }


    private void showToast(String msg) {
        Toast.makeText(currentActivityContext, msg, Toast.LENGTH_SHORT).show();
    }
}
