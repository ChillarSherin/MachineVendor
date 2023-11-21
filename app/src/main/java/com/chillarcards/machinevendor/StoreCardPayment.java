package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.ViewGroup;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.Printer.CommonPrinter;



public class StoreCardPayment extends Activity {

    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;

    public static final int READ_SCHOOL_ID = 4;
    public static final int READ_CARD_SERIAL = 5;
    public static final int READ_CURR_BAL = 8;
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
    StringBuffer read_school = new StringBuffer();
    StringBuffer read_status = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();
    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();
    String print = "", print2 = "", print3, billno;
    Context context;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int authsuc1, authsuc2, authsuc3, authsuc4, authsuc5, readauthsuc1, readauthsuc2, readauthsuc3;
    int writesuc1, writesuc2, writesuc3, writesuc4, writesuc5;
    int readsuc0, readsuc1, readsuc2, readsuc3, readsuc4, readsuc5, readsuc6;


    int tama_on;
    DatabaseHandler db;
    int transtype_id, userid;
    String trans_id, machine_id, currentDateTimeString, currentDateTimeString1;
    Float total = (float) 0;
    String resultCardSerial, resultPrevbal/*,formattedDate*/;
    String User_name, Pass_word, schoomachcode;
    Date expirydate, today1;
    ArrayList<Sales_Item> fetchList = new ArrayList<Sales_Item>();

    Dialog PopupConfrm;
    Button OkButton, NoButton;

    String tranact_id;

    ProgressBar popupProg;
    ProgressBar progressBar;

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        db = DatabaseHandler.getInstance(getApplicationContext());
        context = getApplicationContext();

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        tap_the_card = (Button) findViewById(R.id.tap);

        machine_id = db.getMachineID();

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);


        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        final Date today11 = Calendar.getInstance().getTime();
        currentDateTimeString1 = df1.format(today11);

        fetchList = getIntent().getParcelableArrayListExtra("salesset");
        Bundle b=getIntent().getExtras();
        transtype_id= Integer.parseInt(b.getString("typeId"));
        System.out.println("CHILLAR:fetchlist " + fetchList.size()+":"+transtype_id);




        for (int i = 0; i < Constants.sales_items.size(); i++) {
            System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(i).getitem_id());

            String test = "Item ID : " + Constants.sales_items.get(i).getitem_id() + " Price : " + Constants.sales_items.get(i).getamount() + " Quantity : " + Constants.sales_items.get(i).getitem_quantity();

            String id = String.valueOf(Constants.sales_items.get(i).getitem_id());
            String pric = String.valueOf(Constants.sales_items.get(i).getamount());
            String qty = String.valueOf(Constants.sales_items.get(i).getitem_quantity());

            String amount = String.valueOf(Float.parseFloat(Constants.sales_items.get(i).getamount()) * Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()));


            total = total + Float.parseFloat(amount);

        }


        PopupConfrm = new Dialog(this);

//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//                if (transtype_id == 0) {
//                    Intent i = new Intent(getApplicationContext(), SecondActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//                } else {
//
//                    findViewById(R.id.incProgressBar).setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.VISIBLE);
//                    trans_id = db.lastTransID();
//                    tap_the_card.setEnabled(false);
//                    tap_the_card.setClickable(false);
//
//
//                    try {
//                        if (SecondActivity.ret_fieldon == 0) {
//
//
//                            System.out.println("Tama open :" + tama_on);
//
//                            tardtc = apis.tama_detectTarget(TARGET_NUM,
//                                    target_detected, target_id);
//
//                            if (tardtc == 0) {
//
//                                try {
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
//
//                                        readsuc3 = apis.tama_read_target(
//                                                READ_STATUS, read_status);
//
//
//                                        if (readsuc1 == 0) {
//
//                                            System.out.println("read Success");
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
//                                    String schId = String.valueOf(read_school);
//
//                                    String serial = String.valueOf(read_serial);
//                                    resultCardSerial = serial;
//
//                                    String cardstat = String.valueOf(read_status);
//
//                                    int cardstat1 = Integer.parseInt(cardstat);
//
//                                    if (cardstat1 == 0) {
//                                        Toast.makeText(context, "Card not active.",
//                                                Toast.LENGTH_SHORT).show();
//                                        findViewById(R.id.incProgressBar).setVisibility(View.GONE);
//                                        progressBar.setVisibility(View.GONE);
//                                        finish();
//                                    } else {
//
//
//                                        readauthsuc3 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                        if (readauthsuc3 == 0) {
//                                            readsuc4 = apis.tama_read_target(
//                                                    READ_EXPIRY, read_expiry);
//
//                                            if (readsuc4 == 0) {
//
//                                                System.out.println("read Success expiry");
//
//                                            } else {
//                                                Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                                        .show();
//                                                System.out.println("Read Failed");
//                                            }
//
//                                        } else {
//                                            Toast.makeText(context, "Read Authentication Failed expiry",
//                                                    Toast.LENGTH_SHORT).show();
//                                            System.out.println("Authentication Failed");
//                                        }
//
//                                        String expdate = String.valueOf(read_expiry);
//                                        String expdate1 = expdate.replaceFirst("^0*", "");
//                                        System.out.println("CHILLRRRR: " + expdate1);
//
//                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                                        try {
//                                            expirydate = sdf.parse(expdate1);
//                                            today1 = sdf.parse(currentDateTimeString1);
//
//
//                                            System.out.println("CHILLRRRR: " + expirydate);
//                                            System.out.println("CHILLRRRR: today " + today1);
//                                        } catch (ParseException ex) {
//                                            System.out.println("Parse exception");
//                                        }
//
//
//                                        if (BlocardsCheck(serial)) {
//
//                                            finish();
//
//                                        } else {
//
//
//                                            if (schId.replace(" ", "").equalsIgnoreCase(db.getSchoolId())) {
//                                                try {
////                                                if (today1.after(expirydate)) {
////                                                    findViewById(R.id.incProgressBar).setVisibility(View.GONE);
////                                                    progressBar.setVisibility(View.GONE);
////                                                    System.out.println("ELDHO: card expired");
////                                                    Toast.makeText(getApplicationContext(), "Card already expired. ", Toast.LENGTH_LONG).show();
////                                                    finish();
////                                                } else {
//
//                                                    System.out.println("ELDHO: card active");
//
//                                                    readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                            AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                    if (readauthsuc2 == 0) {
//                                                        readsuc2 = apis.tama_read_target(
//                                                                READ_CURR_BAL, read_current);
//
//
//                                                        if (readsuc2 == 0) {
//
//                                                            System.out.println("read Success current bal");
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
//                                                    String prev = String.valueOf(read_current).replaceFirst("^0+(?!$)", "");
//                                                    resultPrevbal = prev;
//
//
//                                                    Float Prev = Float.valueOf(prev);
//
//                                                    if (Prev >= total) {
//
//
//                                                        String filler1 = "";
//
//
//                                                        int len1 = 16 - trans_id.length();
//                                                        for (int j = 0; j < len1; ++j) {
//
//                                                            filler1 = filler1 + "0";
//
//                                                        }
//
//
//                                                        String data1 = filler1 + trans_id;
//
//                                                        System.out.println("CHILLAR: Filler transid: " + data1);
//
//                                                        String filler2 = "";
//
//
//                                                        int len2 = 16 - machine_id.length();
//                                                        for (int j = 0; j < len2; ++j) {
//
//                                                            filler2 = filler2 + "0";
//
//                                                        }
//
//
//                                                        String data2 = filler2 + machine_id;
//
//                                                        System.out.println("CHILLAR: Filler machinid: " + data2);
//
//
//                                                        char[] transs = data1.toCharArray();
//                                                        char[] machine = data2.toCharArray();
//
//                                                        String totall = String.valueOf(total);
//
//                                                        String filler3 = "";
//
//
//                                                        int len3 = 16 - totall.length();
//                                                        for (int j = 0; j < len3; ++j) {
//
//                                                            filler3 = filler3 + "0";
//
//                                                        }
//
//
//                                                        String data3 = filler3 + totall;
//
//                                                        System.out.println("CHILLAR: Filler totalamt: " + data3);
//                                                        char[] tott = data3.toCharArray();
//
//
//                                                        authsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                                AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                                        if (authsuc2 == 0) {
//
//                                                            writesuc3 = apis.tama_write_target(WRITE_LASTTRANS_MC_ID,
//                                                                    machine, machine.length);
//
//                                                            writesuc4 = apis.tama_write_target(WRITE_TRANS_ID,
//                                                                    transs, transs.length);
//                                                            writesuc5 = apis.tama_write_target(WRITE_TRANS_AMT,
//                                                                    tott, tott.length);
//                                                        } else {
//
//                                                            Toast.makeText(context, "Authentication FAILED3",
//                                                                    Toast.LENGTH_SHORT).show();
//
//                                                        }
//
//
//                                                        Float Current = Prev - total;
//                                                        String filler = "";
//
//                                                        String curbal = Float.toString(Current);
//                                                        int len = 16 - curbal.length();
//                                                        for (int j = 0; j < len; ++j) {
//
//                                                            filler = filler + "0";
//
//                                                        }
//
//
//                                                        String data = filler + curbal;
//
//                                                        System.out.println("CHILLAR: Filler: " + data);
//
//                                                        char[] curren = data.toCharArray();
//                                                        char[] prevv = String.valueOf(read_current).toCharArray();
//
//
//                                                        authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                                AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                        System.out.println("Authentication Success");
//
//
//                                                        if (authsuc1 == 0) {
//
//
//                                                            writesuc1 = apis.tama_write_target(WRITE_CURR_BAL,
//                                                                    curren, curren.length);
//
//                                                            writesuc2 = apis.tama_write_target(WRITE_PREV_BAL,
//                                                                    prevv, prevv.length);
//
//
//                                                        } else {
//
//                                                            Toast.makeText(context, "Authentication FAILED2",
//                                                                    Toast.LENGTH_SHORT).show();
//
//                                                        }
//
//
////
//
//                                                        if ((authsuc1 == 0) && (authsuc2 == 0) && (writesuc1 == 0) && (writesuc2 == 0) && (writesuc3 == 0) && (writesuc4 == 0) && (writesuc5 == 0)) {
//
//                                                            Toast.makeText(context, "SUCCESSSSS!!!!!!", Toast.LENGTH_SHORT).show();
//                                                            progressBar.setVisibility(View.GONE);
//                                                            DBReadWrite();
//
//                                                            findViewById(R.id.incProgressBar).setVisibility(View.GONE);
//
//
//                                                            for (int j = 0; j < Constants.sales_items.size(); j++) {
//                                                                System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(j).getitem_id());
//
//                                                                String test = "Item ID : " + Constants.sales_items.get(j).getitem_id() + " Price : " + Constants.sales_items.get(j).getamount() + " Quantity : " + Constants.sales_items.get(j).getitem_quantity();
//
//                                                                String id = String.valueOf(Constants.sales_items.get(j).getitem_id());
//                                                                String name = db.getItemNamebyID(id);
//                                                                String pric = String.valueOf(Constants.sales_items.get(j).getamount());
//                                                                String qty = String.valueOf(Constants.sales_items.get(j).getitem_quantity());
//
//                                                                String amount = String.valueOf(Float.parseFloat(Constants.sales_items.get(j).getamount()) *
//                                                                        Integer.parseInt(Constants.sales_items.get(j).getitem_quantity()));
//
//
////                                                                total = total + Float.parseFloat(amount);
//                                                                if (name.length() > 6) {
//                                                                    name = name.substring(0, 6);
//                                                                }
//
//                                                                print = print + "     " + name + "    " + qty + "    " + pric + "    " + amount + "\n";
//
//
//                                                            }
//
//                                                            billno = trans_id.substring(12, 16);
//
//                                                            print2 = "  Balance : " + Float.valueOf(Current) + "\n";
//                                                            print3 = "     CardSer    " + resultCardSerial + "\n" +
//                                                                    "     PrevBal    " + Float.parseFloat(prev) + "\n";
//
//                                                            Intent i = new Intent(getApplicationContext(), CommonPrinter.class);
//                                                            Bundle printbundle = new Bundle();
//                                                            printbundle.putString("print", print);
//                                                            printbundle.putString("print2", print2);
//                                                            printbundle.putString("print3", print3);
//                                                            printbundle.putString("billno", trans_id);
//                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                            i.putExtras(printbundle);
//                                                            startActivity(i);
//                                                            finish();
//
//
//                                                        }
//
//
//                                                    } else {
//
//                                                        String s;
//                                                        try {
//                                                            SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
//                                                            s = editor.getString("isonline", "");
//                                                            if (s.equals("true") || s.equals("false")) {
//                                                                if (s.equals("true")) {
//                                                                    onlinerechargecheck();
//                                                                } else {
//                                                                    Toast.makeText(getApplicationContext(), "Sorry,Low balance. Please recharge.", Toast.LENGTH_SHORT).show();
//                                                                    finish();
//                                                                }
//                                                            } else {
//                                                                Toast.makeText(getApplicationContext(), "Sorry,Low balance. Please recharge.", Toast.LENGTH_SHORT).show();
//                                                                finish();
//                                                            }
//                                                        } catch (Exception e) {
//                                                            Toast.makeText(getApplicationContext(), "Sorry,Low balance. Please recharge.", Toast.LENGTH_SHORT).show();
//                                                            finish();
//                                                        }
//
//                                                    }
//
//
////                                                }
//
//                                                } catch (Exception e) {
//
//                                                    tap_the_card.setClickable(false);
//                                                    tap_the_card.setEnabled(false);
//                                                    System.out.println("CODMOB: number format exception" + e.toString());
//                                                    Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                                            .show();
//                                                    AlertDialog.Builder alert = new AlertDialog.Builder(
//                                                            StoreCardPayment.this);
//                                                    alert.setTitle("RFID");
//
//                                                    alert.setCancelable(false);
//                                                    alert.setMessage("Target Moved.Try again.");
//
//                                                    DialogInterface.OnClickListener listener;
//                                                    alert.setPositiveButton(
//                                                            "OK",
//                                                            new DialogInterface.OnClickListener() {
//
//                                                                @Override
//                                                                public void onClick(
//                                                                        DialogInterface dialog,
//                                                                        int which) {
//
////                                                                Constants.sales_items.clear();
//
//                                                                    dialog.dismiss();
//
//                                                                    new FieldRestartTask().execute();
//                                                                }
//                                                            });
//                                                    alert.show();
//
//                                                    System.out.println("Target detect Failed");
//                                                }
//                                            } else {
//                                                Toast.makeText(context, "Card not belonging to this school!!",
//                                                        Toast.LENGTH_SHORT).show();
//                                                finish();
//                                            }
//                                        }
//
//                                    }
//
//
//                                } catch (NumberFormatException e) {
//
//                                    tap_the_card.setClickable(false);
//                                    tap_the_card.setEnabled(false);
//                                    System.out.println("CODMOB: number format exception" + e.toString());
//                                    Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                            .show();
//                                    AlertDialog.Builder alert = new AlertDialog.Builder(
//                                            StoreCardPayment.this);
//                                    alert.setTitle("RFID");
//
//                                    alert.setCancelable(false);
//                                    alert.setMessage("Target Moved.Try again.");
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
////                                                Constants.sales_items.clear();
//
//                                                    dialog.dismiss();
//
//                                                    new FieldRestartTask().execute();
//
//                                                }
//                                            });
//                                    alert.show();
//
//                                    System.out.println("Target detect Failed");
//
//
//                                }
//
//                            } else {
//
//                                tap_the_card.setClickable(false);
//                                tap_the_card.setEnabled(false);
//                                Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                        .show();
//                                AlertDialog.Builder alert = new AlertDialog.Builder(
//                                        StoreCardPayment.this);
//                                alert.setTitle("RFID");
//
//                                alert.setCancelable(false);
//                                alert.setMessage("Target detect Failed");
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
////                                            Constants.sales_items.clear();
//                                                dialog.dismiss();
//                                                new FieldRestartTask().execute();
//
//                                            }
//                                        });
//                                alert.show();
//
//                                System.out.println("Target detect Failed");
//                            }
//
//                        } else {
//                            Toast.makeText(context, "Card open Failed", Toast.LENGTH_SHORT).show();
//                            System.out.println("Card open Field");
//                        }
//
//                    } catch (CursorIndexOutOfBoundsException e) {
//                        System.out.println("EXcepitonCursor Index");
//                    }
//                }
//            }
//        });


    }


    //checking the card is blocked or not before payment

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
                findViewById(R.id.incProgressBar).setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "This card is blocked", Toast.LENGTH_SHORT).show();
                return true;
            }

        }


        return false;
    }

//updating SQLite database with current values in thge card

    private void DBReadWrite() {


        String cardserial = resultCardSerial;
        Float Prev = Float.valueOf(resultPrevbal);


        Float Current = Float.valueOf(resultPrevbal) - total;

        String billno = trans_id.substring(12, 16);


        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        currentDateTimeString = df.format(today);


//
        System.out.println("CHILLER: STORECARDPAYMENT: Cardserial " + cardserial + " PreviousBAl: " + "tOTAL:" + total + Prev + " BillNo: " + billno);

        Item_Sale item_sale = new Item_Sale(trans_id, transtype_id, userid, Prev, Current, cardserial, currentDateTimeString, "", trans_id, billno, String.valueOf(total), "");

        db.addSuccesstransaction(item_sale);
        db.addsaleItem(item_sale);


        System.out.println("MachineTest: size " + Constants.sales_items.size() + "  " + fetchList.size());

        if (Constants.sales_items.size() < fetchList.size()) {

            System.out.println("SalesItemnotcorrect");

            Constants.sales_items = fetchList;

        }


        for (int i = 0; i < Constants.sales_items.size(); i++) {

            System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(i).getitem_id());

//            String test= "Item ID : "+Constants.sales_items.get(i).getitem_id()+" Price : "+Constants.sales_items.get(i).getamount()+" Quantity : "+Constants.sales_items.get(i).getitem_quantity();

            String id = String.valueOf(Constants.sales_items.get(i).getitem_id());
            String pric = String.valueOf(Constants.sales_items.get(i).getamount());
            String qty = String.valueOf(Constants.sales_items.get(i).getitem_quantity());
//            String transid=Constants.sales_items.get(i).getsales_trans_id();
//
//            String amount= String.valueOf(Float.parseFloat(Constants.sales_items.get(i).getamount())*Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()));

//                    Sales_Item sales_item=new Sales_Item(transid,id,qty,pric);
//
//                    db.addsale(sales_item);
            // db.deleteAllItemssale();
            Sales_Item sales_item = new Sales_Item(trans_id, id, qty, pric, "");
//                    System.out.println("CHILLAR CHECKOUT amount " + sales_item.getamount());
//                    System.out.println("CHILLAR CHECKOUT ItemId " + sales_item.getitem_id());
//                    System.out.println("CHILLAR CHECKOUT ItemQty " + sales_item.getitem_quantity());
//                    System.out.println("CHILLAR CHECKOUT SalesItemId " + sales_item.getSales_item_id());

            db.addsale(sales_item);

//                    Constants.sales_items.clear();
//
//                    Intent intent=new Intent(getApplicationContext(),SecondActivity.class);
//                    startActivity(intent);
//

        }

        List<Sales_Item> sales_item = db.getAllsale();
        for (Sales_Item slesitm : sales_item) {

            System.out.println("CHILLERSSSSS::SalesItemID:" + slesitm.getitem_id() + " SalesTransID: " + slesitm.getsales_trans_id() + " ServerTS:  " + slesitm.getserver_timestamp());

        }


//        List<Sales_Item> contacts1 = db.getAllsale();
//        for (Sales_Item cn : contacts1) {
//            String log = "SalesItemId: " + cn.getSales_item_id() + " ,Qty: " + cn.getitem_quantity() +" ,ItemId: " + cn.getitem_id()+ " ,Amt: " + cn.getamount()+" ,TransID: " + cn.getsales_trans_id() ;
//            // Writing Contacts to log
//            System.out.println("CHILLAR STORECARDPAYMENT SalesTable: "+log);
//        }
//        List<Success_Transaction> successtransaction = db.getAllSuccess();
//        for (Success_Transaction usp : successtransaction) {
//            String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
//                    "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
//                    + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
//                    "  Server timestamp  " + usp.getserver_timestamp();
//            System.out.println("CHILLAR STORECARDPAYMENT success transaction table " + testpermission);
//        }
//        List<Item_Sale> success = db.getAllitemsale();
//        for (Item_Sale usp1 : success) {
//            String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
//                    "   total:   " + usp1.gettot_amount() +
//                    "  Server timestamp  " + usp1.getserver_timestamp()+
//                    "  SaleTransID " + usp1.getsale_trans_id();
//            System.out.println("CHILLAR STORECARDPAYMENT ITEMSALE:" + testpermission);
//        }

    }

    private void onlinerechargecheck() {

        PopupConfrm.setContentView(R.layout.layout_confirm_popup2);
//        PopupConfrm.setTitle("Are you Sure Want To Go Back?");
        PopupConfrm.setTitle("Low Balance!");
        PopupConfrm.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        OkButton = (Button) PopupConfrm.findViewById(R.id.btnok);
        NoButton = (Button) PopupConfrm.findViewById(R.id.btnno);

        TextView text = (TextView) PopupConfrm.findViewById(R.id.textpopup);
        text.setVisibility(View.VISIBLE);
        text.setText("Want To Check Online Recharge?");

        PopupConfrm.show();

        popupProg = (ProgressBar) PopupConfrm.findViewById(R.id.prog);


        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                popupProg.setVisibility(View.VISIBLE);

                onlinecheckcall();
//
//
//
//
//
//
//                PopupConfrm.cancel();
//                Constants.sales_items.clear();
//                finish();

            }
        });


        NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupConfrm.cancel();
                finish();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);


    }

    private void onlinecheckcall() {

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        schoomachcode = editor.getString("schlMachCode", "");


        System.out.println("GetAmountFromServer");

//        Toast.makeText(getApplicationContext(),"GetAmountFromServer",Toast.LENGTH_SHORT).show();
        String tag_string_req = "onlinerecharge";


        String URL = Constants.APP_URL + "r_onlineRecharge.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode + "&cardSerial=" + resultCardSerial;
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URL Blocked " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                popupProg.setVisibility(View.INVISIBLE);
                Log.d("TESTT", response.toString());
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());
                // progressDialog.dismiss();
                JSONObject jsobj;
                String server_respose;
                try {

                    jsobj = new JSONObject(response);
                    server_respose = jsobj.getString("status");
                    JSONObject jsonObject = new JSONObject(server_respose);
                    String msg = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    tranact_id = server_respose;

//                    System.out.println("GetAmountfromserver  statuscode "+server_respose+ " amount "+amount);
                    if (code.equals("success")) {
                        PopupConfrm.cancel();
                        Intent i = new Intent(getApplicationContext(), OlineRechargeCheck.class);
                        startActivity(i);
                        finish();
//                        NextPHPExecute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry,No Pending Recharges.", Toast.LENGTH_SHORT).show();
                        PopupConfrm.cancel();
                        Constants.sales_items.clear();
                        Intent i = new Intent(getApplicationContext(), StoreActivityMenu.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
//                    try {
//                        jsobj = new JSONObject(response);
//                        server_respose=jsobj.getString("status");
//                        JSONObject jsonObject=new JSONObject(server_respose);
//                        String msg=jsonObject.getString("message");
//                        String code=jsonObject.getString("code");
//                        if(code.equals("error")){
//                            if(msg.equals("No Pending Recharges")){
//                                Toast.makeText(getApplicationContext(),"Recharge Failed. No Pending Recharges.",Toast.LENGTH_SHORT).show();
//                                finish();
//                            }else if(msg.equals("Authentication Failed")){
//                                Toast.makeText(getApplicationContext(),"Authentication Failed.",Toast.LENGTH_SHORT).show();
//                                finish();
//                            }else if(msg.equals("Required parameters were not found")){
//                                Toast.makeText(getApplicationContext(),"Required parameters were not found.",Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        }
//
//                    } catch (JSONException e1) {
//                        e1.printStackTrace();
//                    }
                    System.out.println("Error:" + e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                popupProg.setVisibility(View.GONE);
                System.out.println("CODMOB: Error in Online Recharge" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Some error occurred . Please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


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