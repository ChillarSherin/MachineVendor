package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RefundScanPay extends Activity {

    private final List<String> BlkdCards = new ArrayList<>();
    public Button tap_the_card, go_back;

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
        setContentView(R.layout.fragment_scan);

        db = DatabaseHandler.getInstance(getApplicationContext());

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
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        Date today11 = Calendar.getInstance().getTime();
        currentDateTimeString1 = df1.format(today11);
        
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

}