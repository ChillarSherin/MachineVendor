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
import com.chillarcards.machinevendor.ModelClass.Recharge_Data;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;


public class BlockedcardsTransfer extends Activity {

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
    Float total = (float) 0;
    String statuscode;
    String resultCardSerial, resultPrevbal, resultCurrenBal, resultCardSerial1;
    String cardSl;
    String User_name, Pass_word;
    int userid;
    int useridinteger;
    String schooid, schoomachcode, machineserial;
    int currentbalance;
    int paymentTypeId;
    Float previousbalnce;
    String currentDateTimeString, currentDateTimeString1;
    String print = "", paytypename, print2 = "";
    ProgressBar progressBar;

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


    // for card transfer......if the card lost


    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);

        db = DatabaseHandler.getInstance(getApplicationContext());

        context = getApplicationContext();
        tap_the_card = (Button) findViewById(R.id.tap);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);
        schooid = editor.getString("schoolId", "");
        schoomachcode = editor.getString("schlMachCode", "");
        machineserial = editor.getString("machineserial", "");

        System.out.println("CODMOB: 123  serial " + schooid + schoomachcode + machineserial);

        useridinteger = userid;
        System.out.println("CODMOB: 123  user " + useridinteger);

        Bundle b = getIntent().getExtras();
        paymentTypeId = b.getInt("paymenttypeId");
        paytypename = b.getString("paytypename");

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


        machine_id = db.getMachineID();
        rech_id = String.valueOf(db.lastRechID());


        System.out.println("CHILL MachineID " + machine_id + " TransId " + trans_id + " Amount " + total);

        transtype_id = db.getTransTypID("RECHARGE");
        System.out.println("CHILL TransTypeID" + transtype_id);


//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//                trans_id = db.lastTransID();
//
//                tap_the_card.setEnabled(false);
//                tap_the_card.setClickable(false);
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
//
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
//
//
//                            String serial = String.valueOf(read_serial);
//                            resultCardSerial = serial;
//
//
//                            String cardstat = String.valueOf(read_status);
//
//                            int cardstat1 = Integer.parseInt(cardstat);
//
//                            if (cardstat1 == 0) {
//                                finish();
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
//                                        GetAmountfromserver();
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
//                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    BlockedcardsTransfer.this);
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
//                                            Intent i = new Intent(getApplicationContext(), SecondActivity.class);
//                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            startActivity(i);
//                                            finish();
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
//
//                    } else {
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                BlockedcardsTransfer.this);
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
//                                        Intent i = new Intent(getApplicationContext(), SecondActivity.class);
//                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(i);
//                                        finish();
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


    private void GetAmountfromserver() {

        String tag_string_req = "amountfrmserver";


        String URL = Constants.APP_URL + "r_card_transfer.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode + "&cardSerial=" + resultCardSerial;
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URL Blocked " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());
                JSONObject jsobj;
                String server_respose;
                try {

                    jsobj = new JSONObject(response);
                    server_respose = jsobj.getString("status");
                    String amount = jsobj.getString("amount");
                    statuscode = server_respose;

                    System.out.println("GetAmountfromserver  statuscode " + server_respose + " amount " + amount);
                    if (server_respose.equals("success")) {
                        total = Float.valueOf(amount);
                        successTransfer();

                    } else {
                        JSONObject jsonObject = new JSONObject(server_respose);
                        String msg = jsonObject.getString("message");
                        if (msg.equals("Card not blocked")) {
                            Toast.makeText(getApplicationContext(), "This Card is not blocked.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (msg.equals("Wrong card Serial")) {
                            Toast.makeText(getApplicationContext(), "Wrong card serial entered.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (msg.equals("Already Transfered")) {
                            Toast.makeText(getApplicationContext(), "Amount already transfered.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    try {
                        jsobj = new JSONObject(response);
                        server_respose = jsobj.getString("status");
                        JSONObject jsonObject = new JSONObject(server_respose);
                        String msg = jsonObject.getString("message");
                        if (msg.equals("Card not blocked")) {
                            Toast.makeText(getApplicationContext(), "This Card is not blocked.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (msg.equals("Wrong card Serial")) {
                            Toast.makeText(getApplicationContext(), "Wrong card serial enterred.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (msg.equals("Already Transfered")) {
                            Toast.makeText(getApplicationContext(), "Amount already transfered.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                }
                System.out.println("json_parse_blockdcard " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Some error occurred . please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void NextRFIDRead() {
//
//        System.out.println("BlockcardsTrnsfr: NextRFIDRead()");
//
//        tardtc = apis.tama_detectTarget(TARGET_NUM,
//                target_detected, target_id);
//
//        if (tardtc == 0) {
//
//            readauthsuc4 = apis.tama_authenticate_mifare(TARGET_NUM,
//                    AUTH_SECND_SECT, key2, KEYA);
//
//            if (readauthsuc4 == 0) {
//
//                readsuc9 = apis.tama_read_target(
//                        READ_STATUS, read_status);
//
//
//                if (readsuc9 == 0) {
//
//                    System.out.println("read serial Success");
//
//
//                } else {
//                    Toast.makeText(context, "Read Failed serial", Toast.LENGTH_SHORT)
//                            .show();
//                    System.out.println("Read Failed");
//                }
//
//            } else {
//                Toast.makeText(context, "Read Authentication Failed serial",
//                        Toast.LENGTH_SHORT).show();
//                System.out.println("Authentication Failed");
//            }
//
//
//            String serial1 = String.valueOf(read_serial);
//            resultCardSerial1 = serial1;
//
//
//            if (resultCardSerial.equals(resultCardSerial1)) {
//
//
//                readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//                if (readauthsuc2 == 0) {
//                    readsuc1 = apis.tama_read_target(
//                            READ_CURR_BAL, read_current);
//
//
//                    if (readsuc1 == 0) {
//
//                        System.out.println("read Success current bal");
//
//
//                    } else {
//                        Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                .show();
//                        System.out.println("Read Failed");
//                    }
//
//                } else {
//                    Toast.makeText(context, "Read Authentication Failed current bal",
//                            Toast.LENGTH_SHORT).show();
//                    System.out.println("Authentication Failed");
//                }
//
//                String prev = String.valueOf(read_current);
//
//                Float Prev = Float.valueOf(prev);
//                previousbalnce = Prev;
//                int Current = (int) (Prev + total);
//                String filler = "";
//
//                String curbal = String.valueOf(Current);
//                int len = 16 - curbal.length();
//                for (int j = 0; j < len; ++j) {
//
//                    filler = filler + "0";
//
//                }
//
//                String data = filler + curbal;
//
//                System.out.println("CHILLAR: Filler: " + data);
//
//                char[] curren = data.toCharArray();
//                char[] prevv = prev.toCharArray();
//
//
//                authsuc1 = apis.tama_authenticate_mifare(TARGET_NUM,
//                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//                System.out.println("Authentication Success");
//
//
//                if (authsuc1 == 0) {
//
//
//                    writesuc1 = apis.tama_write_target(WRITE_CURR_BAL,
//                            curren, curren.length);
//
//                    writesuc2 = apis.tama_write_target(WRITE_PREV_BAL,
//                            prevv, prevv.length);
//
//
//                } else {
//
//                    Toast.makeText(context, "Authentication FAILED2",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//
//
//                if (authsuc1 == 0 && writesuc1 == 0 && writesuc2 == 0) {
//
//                    Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
//
//
//                    GetRecharge();
//
//                    readauthsuc2 = apis.tama_authenticate_mifare(TARGET_NUM,
//                            AUTH_THIRD_SECT, key3, KEYA);
//
//
//                    if (readauthsuc2 == 0) {
//                        readsuc2 = apis.tama_read_target(
//                                READ_CURR_BAL, read_current);
//                        readsuc5 = apis.tama_read_target(
//                                READ_PREV_BAL, read_data5);
//
//
//                        if (readsuc2 == 0) {
//
//                            System.out.println("read Success current bal");
//
//                            String curr = String.valueOf(read_current);
//                            resultCurrenBal = curr;
//                            int currnntt = Integer.parseInt(curr);
//
//                            String prevv1 = String.valueOf(read_data5);
//                            resultPrevbal = prevv1;
//                            int previo = Integer.parseInt(prevv1);
//
//
//                        } else {
//                            Toast.makeText(context, "Read Failed current bal", Toast.LENGTH_SHORT)
//                                    .show();
//                            System.out.println("Read Failed");
//                        }
//
//                    } else {
//                        Toast.makeText(context, "Read Authentication Failed current bal",
//                                Toast.LENGTH_SHORT).show();
//                        System.out.println("Authentication Failed");
//                    }
//
//
////                    successTransfer();
//
//                    Intent i = new Intent(getApplicationContext(), BlockTransferNFC.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Bundle b = new Bundle();
//                    b.putString("previobal", resultPrevbal);
//                    b.putString("currentbal", resultCurrenBal);
//                    b.putString("flag", "cardtransfer");
//                    b.putString("check", "1");
//                    b.putString("transid", trans_id);
//                    i.putExtras(b);
//
//                    startActivity(i);
//                    finish();
//
//
//                } else {
//                    ErrorCasePhp();
//                    Toast.makeText(getApplicationContext(), "Transfer Failure!", Toast.LENGTH_SHORT).show();
//
//                }
//
//            } else {
//                ErrorCasePhp();
//                Toast.makeText(getApplicationContext(), "Card mismatch error!  ", Toast.LENGTH_LONG).show();
//
//                finish();
//            }
//
//
//        } else {
//            tap_the_card.setClickable(false);
//            tap_the_card.setEnabled(false);
//
//            Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                    .show();
//            AlertDialog.Builder alert = new AlertDialog.Builder(
//                    BlockedcardsTransfer.this);
//            alert.setTitle("RFID");
//
//            alert.setCancelable(false);
//            alert.setMessage("Target detect Failed");
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
//                            dialog.dismiss();
//                            ErrorCasePhp();
//                            new FieldRestartTask().execute();
//
//                        }
//                    });
//            alert.show();
//
//            System.out.println("Target detect Failed");
//        }


    }

    public String getcurrentdate() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }

    private void ErrorCasePhp() {


    }

    private void successTransfer() {
        String tag_string_req = "successtransfer";


        String URL = Constants.APP_URL + "c_card_transfer.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&cardSerial=" + resultCardSerial + "&amount=" + total + "&cardTransferDate=" + getcurrentdate();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URL Blocked 1 :  " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
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
                    String msg = jsonObject.getString("code");
                    if (msg.equals("success")) {
                        NextRFIDRead();

                    } else if (msg.equals("'Authentication failed")) {
                        Toast.makeText(getApplicationContext(), "'Authentication failed.", Toast.LENGTH_SHORT).show();
                    } else if (msg.equals("Wrong card Serial")) {
                        Toast.makeText(getApplicationContext(), "Wrong card serial enterred.", Toast.LENGTH_SHORT).show();
                    } else if (msg.equals("Required parameter(s) not found")) {
                        Toast.makeText(getApplicationContext(), "Required parameter(s) not found.", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("GetAmountfromserver  statuscode " + server_respose);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
                System.out.println("json_parse_blockdcard " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                ErrorCasePhp();

                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Some error occurred . please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void GetRecharge() {


        String cardserialnumber = resultCardSerial;

        currentbalance = (int) ((previousbalnce) + total);

//        db.deleteAllSussecc();
//        db.deleteAllrecharge();


        Recharge_Data recharg = new Recharge_Data(trans_id, userid, transtype_id, previousbalnce, (float) currentbalance, cardserialnumber,
                currentDateTimeString, "", trans_id, currentDateTimeString, total, paymentTypeId, "");
        db.addSuccesstransaction(recharg);
        recharg.gettransaction_id();
        System.out.println("CODMOB: RechAMT blocked" + recharg.getRech_amt());

        db.addrecharge(recharg);

//        Log.d("CODMOB", "User Countrecharge: " + db.getAllrech().size());
//        Log.d("CODMOB", "User listrecharge: " + db.getAllrech());
//
//
//        List<Success_Transaction> successtransaction = db.getAllSuccess();
//        for (Success_Transaction usp : successtransaction) {
//            String testpermission = "   transaction id:  " + usp.gettrans_id() + " transactiontype_id :" + usp.gettranstypeid() +
//                    "   userID:   " + usp.getuser_id() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance :  "
//                    + usp.getcurrent_balance() + "  crad serial : " + usp.getcard_serial() + " time stamp : " + usp.gettime_stamp()+
//                    "  Server timestamp  " + usp.getserver_timestamp();
//            System.out.println("CODMOB: recharg blocked" + testpermission);
//
//
//
//
//            //tv1.setText(testpermission);
//
//        }
//
//        Log.d("CODMOB", "recharge: " + db.getAllrech().size());
//        Log.d("CODMOB", "recharge2 : " + db.getAllrech());
//
//        List<Recharge_Data> rechrgedata = db.getAllrech();
//        for (Recharge_Data uspaytrans : rechrgedata) {
//            String payString = " recharge_ID : "+uspaytrans.getrecharge_id()+" transaction_ID : "+uspaytrans.gettransaction_id()+
//                    " transaction_ID : "+uspaytrans.gettransaction_id()
//                    +" rechAMount  : "+uspaytrans.getRech_amt()+" PaymentType_ID : "+uspaytrans.getpayment_type_id()+
//                    " Servertimestamp"+uspaytrans.getserver_timestamp();
//            System.out.println("CODMOB: recharge blocked1" + payString);
//
//        }


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

    private void ErrorPHP() {


        String tag_string_req = "errorphp";


        String URL = Constants.APP_URL + "c_online_recharge.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" +/*tranact_id+*/"&time_stamp=" + getcurrentdate();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URL ErrorPHP " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
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
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("message");


                } catch (JSONException e) {

                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Some error occurred . Please try again", Toast.LENGTH_SHORT).show();
//                finish();
            }

        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


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