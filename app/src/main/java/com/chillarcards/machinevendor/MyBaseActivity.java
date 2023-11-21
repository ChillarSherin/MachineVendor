package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Attendance_Data;
import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Create_Leave;
import com.chillarcards.machinevendor.ModelClass.Fee_Transaction;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Library_book_transaction;
import com.chillarcards.machinevendor.ModelClass.Online_Recharge;
import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Recharge_Data;
import com.chillarcards.machinevendor.ModelClass.Refund;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;

/**
 * Created by Codmob on 15-09-16.
 */
public class MyBaseActivity extends Activity {


    public static final long DISCONNECT_TIMEOUT = 600000; // 5 min = 5 * 60 * 1000 ms . //Timer for 10 minutes
    String User_name, Pass_word, userID;
    int useridinteger;
    String schooid, schoomachcode, machineserial;
    DatabaseHandler db;
    String uriString = "";
    String versionName = "";
    String serversuccess = "", serverattdata = "", serverrecharg = "", serverfee = "", serverlibrar = "", serverpay = "", serveritemsale = "", serversaleitem = "", serversuccessnew = "", serveritemsalenew = "", serversaleitemnew = "";
    private DownloadManager dm;
    private long enqueue;
    private BroadcastReceiver reciever;
    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println("MyBase: handlemsg " + msg);
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
//            Toast.makeText(getApplicationContext(),"Machine idle state.",Toast.LENGTH_SHORT).show();
            System.out.println("MyBase: disconnect callback");
//            SecondActivity.stoptimertask();

//            SharedPreferences.Editor editor = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
//            editor.remove("username");
//            editor.remove("password");
//            editor.remove("userid");
//            editor.putString("logout","true");
//            editor.commit();
//
//            Intent in=new Intent(getApplicationContext(),LogIn.class);
//            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(in);
//            finish();

            // Perform any required operation on disconnect


            SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
            User_name = editor.getString("username", "");
            Pass_word = editor.getString("password", "");
            userID = String.valueOf(editor.getInt("userid", 0));
            schooid = editor.getString("schoolId", "");
            schoomachcode = editor.getString("schlMachCode", "");
            machineserial = editor.getString("machineserial", "");

            System.out.println("CODMOB: 123  serial " + schooid + schoomachcode + machineserial);

            useridinteger = Integer.parseInt(userID);
            System.out.println("CODMOB: 123  user " + useridinteger);


            reciever = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        String action = intent.getAction();
                        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                            long downloadId = intent.getLongExtra(
                                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(enqueue);
                            Cursor c = dm.query(query);
                            if (c.moveToFirst()) {
                                int columnIndex = c
                                        .getColumnIndex(DownloadManager.COLUMN_STATUS);
                                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                                    Toast.makeText(getApplicationContext(), "Apk download successfull. ", Toast.LENGTH_LONG).show();

//                            ImageView view = (ImageView) findViewById(R.id.imageView1);

                                    try {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                                MyBaseActivity.this);

                                        alert.setTitle("Update");

                                        alert.setCancelable(false);
                                        alert.setMessage("App update downloaded. Update now?");

                                        DialogInterface.OnClickListener listener;
                                        alert.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {

                                                        dialog.dismiss();
//                                                DownloadAPK();


                                                        UpdateAPP();
//

                                                    }
                                                });

//                                alert.setNegativeButton(
//                                        "Cancel",
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//
//                                                dialog.dismiss();
////
//
//                                            }
//                                        });
                                        alert.show();
                                    } catch (WindowManager.BadTokenException e) {

                                    }

                                    uriString = c
                                            .getString(c
                                                    .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//

                                    System.out.println("CODMOB:Download callback: " + uriString);
                                } else if (DownloadManager.STATUS_RUNNING == c.getInt(columnIndex)) {

                                    System.out.println("CODMOB:Download callback: Running");

                                } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {


                                    dm.remove(c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));

                                    Toast.makeText(getApplicationContext(), "Apk download failed. Please try downloading again.", Toast.LENGTH_LONG).show();

                                } else {
                                    System.out.println("CODMOB:Download callback: Running");

                                }

                            }
                        }
                    } catch (Exception e) {
                    }
                }
            };

            registerReceiver(reciever, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            db = DatabaseHandler.getInstance(getApplicationContext());

            new timerUpload().execute();


            resetDisconnectTimer();


        }
    };

    static public boolean isURLReachable1(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://cwo.chillarpayments.com");   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);          // 10 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.wtf("Connection", "Success !");
                    System.out.println("CHILLERS: Success");
                    return true;
                } else {
                    Log.wtf("Connection", "Failed !");
                    System.out.println("CHILLERS: Failure");
                    return false;
                }
            } catch (MalformedURLException e1) {
                System.out.println("CHILLERS: malformed " + e1.toString());
                return false;
            } catch (IOException e) {
                System.out.println("CHILLERS: IOException " + e.toString());
                return false;
            }
        }
        System.out.println("CHILLERS: out");
        return false;
    }

    public void resetDisconnectTimer() {
        System.out.println("MyBase: resetTimer");
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        System.out.println("MyBase: stopTimer");
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        System.out.println("MyBase: UserInteract");
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    public Boolean isNetworkConnected() {
        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //For server updating all tables
    public void updateurl() throws IllegalStateException, RuntimeException {


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        versionName = editor.getString("appVersion", "");


        try {


            System.out.println("schoolmachine code " + schoomachcode);


            List<Success_Transaction> successtransaction7 = db.getAllsuccesstoup();
            for (Success_Transaction usp : successtransaction7) {

                PhpLoader1(usp);
            }


//            List<Success_Transaction> successtransactions = db.getAllSuccess();
//            for (Success_Transaction usp : successtransactions) {
//                String testpermissionnew = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
//                        "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
//                        + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
//                        "  Server timestamp  " + usp.getserver_timestamp();
//                System.out.println("CHILLAR secact success transaction tablenew " + testpermissionnew);
//            }

            //update new success transaction table

            List<Success_Transaction> sxstrnxn = db.getAllsxstoupNew();
            for (Success_Transaction usp : sxstrnxn) {

                PhpLoaderNew1(usp);


            }
//            List<Success_Transaction> sxstrnxn1 = db.getAllSuccess();
//            for (Success_Transaction usp : sxstrnxn1) {
//                String testpermissionnew = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
//                        "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
//                        + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
//                        "  Server timestamp  " + usp.getserver_timestamp();
//                System.out.println("CHILLAR secact Refund success transaction tablenew " + testpermissionnew);
//            }

            //ATTENDENCE TRANSACTION................................................................................
            List<Attendance_Data> attdatas = db.getAllAttendancedatatoUP();
            for (Attendance_Data uspaytrans : attdatas) {

                PhpLoader2(uspaytrans);

            }

//            List<Attendance_Data> attdatas1 = db.getAllAttendancedata();
//            for (Attendance_Data uspaytrans : attdatas1) {
//                String payString = " attendencedataID : " + uspaytrans.getAttendance_data_id() + " attendencedataTypeID : " + uspaytrans.getAttendance_type_id()
//                        + " INOUT : " + uspaytrans.getIn_out() + " transactionID : " + uspaytrans.getTransaction_id() + " Servertimestamp" + uspaytrans.getServer_timestamp();
//                System.out.println("CHILLAR secact attdata NewTable " + payString);
//            }

//        List<Attendance_Data> attdatas2 = db.getAllAttendancedata();
//        for (Attendance_Data uspaytrans : attdatas2) {
//            String payString = " attendencedataID : "+uspaytrans.getAttendance_data_id()+"\n"+" attendencedataTypeID : "+uspaytrans.getAttendance_type_id()
//                    +"\n"+" INOUT : "+uspaytrans.getIn_out()+"\n"+" transactionID : "+uspaytrans.getTransaction_id()+"\n"+" Servertimestamp"+uspaytrans.getServer_timestamp();
//            System.out.println("CODMOB: Attendence2" + payString);

//CARD RECHARGE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            List<Recharge_Data> rechrgedata = db.getAllrechtoUp();
            for (Recharge_Data rech : rechrgedata) {
                PhpLoader3(rech);
            }
//            List<Recharge_Data> rechrgedata1 = db.getAllrech();
//            for (Recharge_Data rech : rechrgedata1) {
//                String payString = " recharge_ID : " + rech.getrecharge_id() + " transaction_ID : " + rech.gettransaction_id() + " rechAmount : " + rech.getRech_amt()
//                        + " recharge_TIME  : " + rech.getrecharge_time() + " PaymentType_ID : " + rech.getpayment_type_id() +
//                        " Servertimestamp" + rech.getserver_timestamp();
//                System.out.println("CHILLAR secact recharge3 NEw Table" + payString);
//            }


            //FEE TRANSACTION.............................................................................................
            List<Fee_Transaction> contacts3 = db.getAllfeetranstoUp();
            for (Fee_Transaction cn : contacts3) {


                String feeStatus = PhpLoader4(cn);


            }

//            List<Fee_Transaction> cont = db.getAllfeetrans();
//            for (Fee_Transaction cn : cont) {
//                String log = " ,billno: " + cn.getbill_no() + " ,TotalAmt:" + cn.gettot_am() + ",TransId:" + cn.gettrans_id() + " ,ServerTimeStamp:" + cn.getserver_timestamp();
//                // Writing Contacts to log
//                System.out.println("CHILLAR secact FEETRANS NEw Table  : NewTable " + log);
//            }

            //Library book transaction....................................................................................
            List<Library_book_transaction> contacts1 = db.getAlllibtoUp();
            for (Library_book_transaction cnlib : contacts1) {

                String librarStatus = PhpLoader5(cnlib);


            }

//            List<Library_book_transaction> contacts11 = db.getAlllib();
//            for (Library_book_transaction cnlib : contacts11) {
//                String log = "Id: " + cnlib.getid() + " ,BookId: " + cnlib.getbook_id() + " ,Issued:" + cnlib.getissue_time() + ",TransId:" + cnlib.gettransaction_id() + " ,ServerTimeStamp:" + cnlib.getserver_timestamp() + " ,Return:" + cnlib.getissue_return();
//                // Writing Contacts to log
//                System.out.println("CHILLAR secact LIBRARY : NewTable " + log);
//                db.close();
//            }

            //Payment Transaction............................................................................................


            List<Payment_Transaction> paytransaction = db.getAllpaytransactiontoUp();
            for (Payment_Transaction uspaytrans : paytransaction) {

                String payStatus = PhpLoader6(uspaytrans);

            }

            List<Payment_Transaction> paytransaction1 = db.getAllpaytransactiontoUpNew();
            for (Payment_Transaction uspaytrans1 : paytransaction1) {

                String payStatus = PhpLoader11(uspaytrans1);

            }
//            List<Payment_Transaction> paytransaction1 = db.getAllpaytransaction();
//            for (Payment_Transaction uspaytrans : paytransaction1) {
//                String payString = " ID " + uspaytrans.getId() + "trans_id1 "
//                        + uspaytrans.gettrans_id() + " Amount " + uspaytrans.getamount() + " Server_time_timestamp " + uspaytrans.getserver_timestamp();
//                System.out.println("CHILLAR secact paytransaction3 NewTable " + payString);
//            }


            //ITEM SALE TRANSACTION................................................................................

            List<Item_Sale> success = db.getAllitemsaletoUp();
            for (Item_Sale usp1 : success) {

                String itemsaleStatus = PhpLoader7(usp1);


            }
//            List<Item_Sale> success11 = db.getAllitemsale();
//            for (Item_Sale usp1 : success11) {
//                String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
//                        "   total:   " + usp1.gettot_amount() +
//                        "  Server timestamp  " + usp1.getserver_timestamp() +
//                        "  SaleTransID " + usp1.getsale_trans_id();
//                System.out.println("CHILLAR secact Sales ITEMSALE: NewTable " + testpermission);
//            }


            //SALES ITEM................................................................................................

            List<Sales_Item> sales_item = db.getAllsaletoUp();
            for (Sales_Item slesitm : sales_item) {

                String saleitemStatus = PhpLoader8(slesitm);


            }

            List<Refund> refunds = db.getAllRefundtoUp();
            for (Refund refund : refunds) {
                PhpLoader9(refund);
            }


//            List<Sales_Item> sales_item11 = db.getAllsale();
//            for (Sales_Item slesitm : sales_item11) {
//                String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
//                        + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();
//
//
//                System.out.println("CHILLAR secact Sales ITEM NewTable:" + testsalesitem);
//            }


            List<Item_Sale> itsale = db.getAllitemsaletoUpNew();
            for (Item_Sale usp1 : itsale) {
                String serveritemsale = PhpLoaderNew2(usp1);


            }
//            List<Item_Sale> itsale1 = db.getAllitemsale();
//            for (Item_Sale usp1 : itsale1) {
//                String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
//                        "   total:   " + usp1.gettot_amount() +
//                        "  Server timestamp  " + usp1.getserver_timestamp() +
//                        "  SaleTransID " + usp1.getsale_trans_id();
//                System.out.println("CHILLAR secact Refund Sales ITEMSALE: NewTable " + testpermission);
//            }


            //SALES ITEM................................................................................................

            List<Sales_Item> salesitm = db.getAllsaletoUpNew();
            for (Sales_Item slesitm : salesitm) {

                String serversaleitem = PhpLoaderNew3(slesitm);

            }

            List<Online_Recharge> online = db.getAllOnlineToUp();
            for (Online_Recharge onlin1 : online) {

                String serversaleitem = PhpLoader10(onlin1);

            }


            List<Create_Leave> createleave = db.getAllCreateLeaveToUp();
            for (Create_Leave uspaytrans1 : createleave) {

                String payStatus = PhpLoader12(uspaytrans1);

            }

//            List<Sales_Item> salesitm1 = db.getAllsale();
//            for (Sales_Item slesitm : salesitm1) {
//                String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
//                        + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();
//
//
//                System.out.println("CHILLAR secact Refund Sales ITEM NewTable:" + testsalesitem);
//            }

            List<Attendance_Data> teacher_attdatas = db.getAllteacherAttendancedatatoUP();
            for (Attendance_Data uspaytrans : teacher_attdatas) {

                PhpLoaderteacheratte(uspaytrans);

            }


            //Updating blocked cards
            BlockedCards();

            //Checking for app update available.
            updateApp();


        } catch (IllegalStateException e) {
            e.printStackTrace();
            updateurl();
        }
    }

    private String PhpLoaderteacheratte(Attendance_Data uspaytrans) {


        String payString = " attendencedataID : " + uspaytrans.getAttendance_data_id() + " attendencedataTypeID : " + uspaytrans.getAttendance_type_id()
                + " INOUT : " + uspaytrans.getIn_out() + " transactionID : " + uspaytrans.getTransaction_id() + "CardSerial  " + uspaytrans.getcard_serial() + " Servertimestamp" + uspaytrans.getServer_timestamp();
        System.out.println("CHILLAR secact attdata" + payString);


        String URL = Constants.APP_URL + "c_teacher_attendance.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + uspaytrans.getTransaction_id() + "&in_out=" + uspaytrans.getIn_out();

        String tag_string_req = "attendence";
        final String id = uspaytrans.getTransaction_id();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);

                try {

                    JSONObject jsobj;
                    jsobj = new JSONObject(response);
                    JSONObject jsonObject = jsobj.getJSONObject("status");
                    String server_respose = jsonObject.getString("code");


                    System.out.println("PhpLoader2  code " + server_respose);
                    if (server_respose.equals("success")) {
                        System.out.println("ELDDD: update att ");
                        db.updateTchrAttnd(id);
//                            db.close();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serverattdata = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serverattdata;

    }

    public void updateApp() {


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        String User_name1 = editor.getString("username", "");
        String Pass_word1 = editor.getString("password", "");
        String userID1 = String.valueOf(editor.getInt("userid", 0));
        String schooid1 = editor.getString("schoolId", "");
        String schoomachcode1 = editor.getString("schlMachCode", "");
        String machineserial1 = editor.getString("machineserial", "");

        String tag_string_req = "updateAppPhp";


        String url = Constants.APP_URL + "r_new_apk.php?machineUserName=" + User_name1 + "&machineUserPassword=" + Pass_word1 + "&schoolMachineCode=" + schoomachcode1;
        System.out.println("UrlUpdatePhp " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:UpdateAPK " + response);
                // progressDialog.dismiss();

                try {

                    System.out.println("Chillr1");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        System.out.println("Chillr2");
                        JSONObject status = jsonObject.getJSONObject("status");

                        String code = status.getString("code");
                        if (code.equals("success")) {

                            System.out.println("Chillr3");
                            String fileVersion = jsonObject.getString("version");
                            System.out.println("VersionName: " + fileVersion);


                            if (compareVersionNames(Checkversion(), fileVersion) < 0) {

//                                Toast.makeText(getApplicationContext(),"App update available. Please Update.",Toast.LENGTH_LONG).show();
                                System.out.println("Chillr: Update Available" + Constants.dialogShown);


                                if (db.getAllsuccesstoup().size() > 0 ||
                                        db.getAllsxstoupNew().size() > 0 ||
                                        db.getAllitemsaletoUp().size() > 0 ||
                                        db.getAllitemsaletoUpNew().size() > 0 ||
                                        db.getAllsaletoUp().size() > 0 ||
                                        db.getAllsaletoUpNew().size() > 0 ||
                                        db.getAlllibtoUp().size() > 0 ||
                                        db.getAllAttendancedatatoUP().size() > 0 ||
                                        db.getAllrechtoUp().size() > 0 ||
                                        db.getAllfeetranstoUp().size() > 0 ||
                                        db.getAllpaytransactiontoUp().size() > 0 ||
                                        db.getAllOnlineToUp().size() > 0 ||
                                        db.getAllRefundtoUp().size() > 0 ||
                                        db.getAllCreateLeaveToUp().size() > 0) {

                                    if (!Constants.dialogShown1) {

                                        try {

                                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                                    MyBaseActivity.this);

                                            alert.setTitle("Update");

                                            alert.setCancelable(false);
                                            alert.setMessage("App update available. Upload Database to update app.");

                                            Constants.dialogShown1 = true;
                                            DialogInterface.OnClickListener listener;
                                            alert.setPositiveButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {

                                                            dialog.dismiss();

                                                            Constants.dialogShown1 = false;
//

                                                        }
                                                    });

                                            alert.show();
                                        } catch (WindowManager.BadTokenException e) {
                                            System.out.println("AlertDialog exception catched");
                                        }
                                    }


                                } else {

                                    if (!Constants.dialogShown) {

                                        try {

                                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                                    MyBaseActivity.this);

                                            alert.setTitle("Update");

                                            alert.setCancelable(false);
                                            alert.setMessage("App update available. Download now?");

                                            Constants.dialogShown = true;
                                            DialogInterface.OnClickListener listener;
                                            alert.setPositiveButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {

                                                            dialog.dismiss();

                                                            DownloadAPK();

                                                        }
                                                    });

                                            alert.setNegativeButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {

                                                            dialog.dismiss();
                                                            Constants.dialogShown = false;
//

                                                        }
                                                    });
                                            alert.show();
                                        } catch (WindowManager.BadTokenException e) {
                                            System.out.println("AlertDialog exception catched");
                                        }
                                    }

                                }
                            }

                        }


                    }
                } catch (JSONException e) {
                    System.out.println("Chillr4");
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in updateAPk" + error.getMessage());


//                Toast.makeText(getApplicationContext(), "some error occurred in Blocked cards info. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void DownloadAPK() {

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        String User_name1 = editor.getString("username", "");
        String Pass_word1 = editor.getString("password", "");
        String userID1 = String.valueOf(editor.getInt("userid", 0));
        String schooid1 = editor.getString("schoolId", "");
        String schoomachcode1 = editor.getString("schlMachCode", "");

        String apklink = Constants.APP_URL + "r_download_new_apk.php?machineUserName=" + User_name1 + "&machineUserPassword=" + Pass_word1 + "&schoolMachineCode=" + schoomachcode1;
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apklink));

        if (isFileExists("app-debug.apk")) {
            deleteFile1("app-debug.apk");
        }

        request/*.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)*/
                .setDestinationInExternalPublicDir("/DCIM", "app-debug.apk");
        enqueue = dm.enqueue(request);


    }

    public boolean isFileExists(String filename) {

        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/" + filename);
        System.out.println("ChillrCodmob: " + folder1.getAbsolutePath());
        return folder1.exists();


    }

    public boolean deleteFile1(String filename) {

        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/" + filename);
        return folder1.delete();


    }

    private void BlockedCards() {
        String tag_string_req = "list_announce";

        String url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + User_name.replace(" ", "%20") + "&machineUserPassword=" + Pass_word.replace(" ", "%20") + "&schoolMachineCode=" + schoomachcode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

//                    db.deleteBlockedInfo();
                    clearDatabase(getApplicationContext());
//                    parseJSONBlockedcards(response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in Blockedcards" + error.getMessage());


//                Toast.makeText(getApplicationContext(), "some error occurred in Blocked cards info. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private String PhpLoader1(Success_Transaction usp) {

        String tag_string_req = "successtrnxn";

        String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                "  Server timestamp  " + usp.getserver_timestamp();
        System.out.println("CHILLAR secact success transaction table " + testpermission);
        final String id = usp.gettrans_id();

        String URL = Constants.APP_URL + "c_successTransaction.php?machineCode=" + schoomachcode + "&current_user=" + useridinteger + "&user_id=" + usp.getuser_id() + "&transaction_id=" + usp.gettrans_id() + "&card_serial=" + usp.getcard_serial() + "&transaction_type_id=" + usp.gettranstypeid() + "&current_balance=" + usp.getcurrent_balance() + "&prev_balance=" + usp.getprevious_balnce() + "&time_stamp=" + usp.gettime_stamp() + "&current_version=" + versionName;

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {


                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoader1  code " + server_respose);
                        if (server_respose.equals("success")) {

                            db.updatesuccess(id);
//                            db.close();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversuccess = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversuccess;

    }

    private String PhpLoader2(Attendance_Data uspaytrans) {


        String payString = " attendencedataID : " + uspaytrans.getAttendance_data_id() + " attendencedataTypeID : " + uspaytrans.getAttendance_type_id()
                + " INOUT : " + uspaytrans.getIn_out() + " transactionID : " + uspaytrans.getTransaction_id() + " Servertimestamp" + uspaytrans.getServer_timestamp();
        System.out.println("CHILLAR secact attdata" + payString);


        String URL = Constants.APP_URL + "c_attendance_transaction.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + uspaytrans.getTransaction_id() + "&attendance_type_id=" + uspaytrans.getAttendance_type_id() + "&in_out=" + uspaytrans.getIn_out();

        String tag_string_req = "attendence";
        final String id = uspaytrans.getTransaction_id();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");


                        System.out.println("PhpLoader2  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updateAttendancedata(id);
//                            db.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serverattdata = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serverattdata;

    }

//    private void parseJSONBlockedcards(String json) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//
//            if(jsar.length()>0) {
//                for (int i = 0; i < jsar.length(); i++) {
//
//                    String s = jsar.getString(i);
//                    JSONObject jsobj;
//                    jsobj = new JSONObject(s);
//
//                    int blockedcardsId = jsobj.getInt("blocked_cards_id");
//                    String cardSerial = jsobj.getString("card_serial");
////                String password=jsobj.getString("password");
//
//
//                    System.out.println("CODMOB: serial " + blockedcardsId + cardSerial);
//
//
//                    try {
//
////                    Toast.makeText(getApplicationContext(), "id "+blockedcardsId+" cardSerial "+cardSerial, Toast.LENGTH_LONG).show();
//
//
//                        Blocked_Cards_Info blockcards = new Blocked_Cards_Info(blockedcardsId, cardSerial);
//
//                        db.addBlockCardInfo(blockcards);
//
//
////                    Device_Info device_info=new Device_Info()
//
//                    } catch (Exception e) // no guruji
//                    {
//
//                        e.printStackTrace();
//
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//
//            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//
//        }
//    }

    private String PhpLoader3(Recharge_Data rech) {

        String payString = " recharge_ID : " + rech.getrecharge_id() + " transaction_ID : " + rech.gettransaction_id()
                + " RechAMount : " + rech.getRech_amt() + " recharge_TIME  : " + rech.getrecharge_time() + " PaymentType_ID : " + rech.getpayment_type_id() +
                " Servertimestamp" + rech.getserver_timestamp();
        System.out.println("CHILLAR secact recharge3" + payString);
        String tag_string_req = "recharge";
        String URL = Constants.APP_URL + "c_card_recharge.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&payment_type_id=" + rech.getpayment_type_id() + "&transaction_id=" + rech.gettransaction_id() + "&amount=" + rech.getRech_amt() + "&recharge_time=" + rech.getrecharge_time();

        final String id = rech.gettransaction_id();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");


                        System.out.println("PhpLoader3  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updaterechrge(id);
//                            db.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serverrecharg = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serverrecharg;

    }
//

    private String PhpLoader4(Fee_Transaction cn) {

        String log = " ,billno: " + cn.getbill_no() + " ,TotalAmt:" + cn.gettot_am() + ",TransId:" + cn.gettrans_id() + " ,ServerTimeStamp:" + cn.getserver_timestamp();
        // Writing Contacts to log
        System.out.println("CHILLAR secact FEETRANS : " + log);
        String URL = Constants.APP_URL + "c_fee_transaction.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + cn.gettrans_id() + "&bill_no=" + cn.getbill_no() + "&total_amount=" + cn.gettot_am();

        final String id = cn.gettrans_id();
        String tag_string_req = "fees";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

                        System.out.println("PhpLoader4  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updatefeetran(id);
//                            db.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serverfee = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serverfee;

    }

    private String PhpLoader5(Library_book_transaction cnlib) {

        String log = "Id: " + cnlib.getid() + " ,BookId: " + cnlib.getbook_id() + " ,Issued:" + cnlib.getissue_time() + ",TransId:" + cnlib.gettransaction_id() + " ,ServerTimeStamp:" + cnlib.getserver_timestamp() + " ,Return:" + cnlib.getissue_return();
        // Writing Contacts to log
        System.out.println("CHILLAR secact LIBRARY : " + log);
        String URL = Constants.APP_URL + "c_library_book_transaction.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + cnlib.gettransaction_id() + "&issue_return=" + cnlib.getissue_return() + "&book_id=" + cnlib.getbook_id() + "&issue_time=" + cnlib.getissue_time();

        final String id = cnlib.gettransaction_id();

        String tag_string_req = "library";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

                        System.out.println("PhpLoader5  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updatelibr(id);
//                            db.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serverlibrar = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serverlibrar;

    }

    private String PhpLoader6(Payment_Transaction uspaytrans) {

        String payString = " ID " + uspaytrans.getId() + "trans_id1 "
                + uspaytrans.gettrans_id() + " Amount " + uspaytrans.getamount() + " Server_time_timestamp " + uspaytrans.getserver_timestamp();
        System.out.println("CHILLAR secact paytransaction3" + payString);

        String URL = Constants.APP_URL + "c_payment_transaction.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + uspaytrans.gettrans_id() + "&bill_no=" + uspaytrans.getbillno() + "&amount=" + uspaytrans.getamount();
        System.out.println("CODMOB: URRLLL" + URL);
        String tag_string_req = "payment";
        final String id = uspaytrans.gettrans_id();

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

                        System.out.println("PhpLoader6  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updatepayment(id);
//                            db.close();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serverpay = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serverpay;

    }

    private String PhpLoader7(Item_Sale usp1) {

        String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
                "   total:   " + usp1.gettot_amount() +
                "  Server timestamp  " + usp1.getserver_timestamp() +
                "  SaleTransID " + usp1.getsale_trans_id();
        System.out.println("CHILLAR secact Sales ITEMSALE:" + testpermission);


        String URL = Constants.APP_URL + "c_item_sale_transaction.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + usp1.gettransaction_id() + "&bill_no=" + usp1.getbill_no() + "&total_amount=" + usp1.gettot_amount();

        final String id = usp1.gettransaction_id();
        String tag_string_req = "itemsale";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                String resp = response.toString();
                System.out.println("CODMOB:CHILLERE " + resp);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

                        System.out.println("PhpLoader7  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updateitemss(id);
//                            db.close();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serveritemsale = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serveritemsale;

    }

    private String PhpLoader8(Sales_Item slesitm) {

        String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
                + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


        System.out.println("CHILLAR secact Sales ITEM:" + testsalesitem);
        String URL = Constants.APP_URL + "c_sales_item_list.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + slesitm.getsales_trans_id() + "&item_id=" + slesitm.getitem_id() + "&amount=" + slesitm.getamount() + "&item_quantity=" + slesitm.getitem_quantity();

        final String id = String.valueOf(slesitm.getSales_item_id());
        String tag_string_req = "salesitem";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

                        System.out.println("PhpLoader8  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updatesale(id);
//                            db.close();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversaleitem = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversaleitem;

    }

    private String PhpLoader9(Refund slesitm) {

        String testsalesitem = "Refund Id : " + slesitm.getId() + " TransID : " + slesitm.getTrans_id() + " Original TransID: " + slesitm.getOrig_trans_id() + " Servertimestamp : " + slesitm.getserver_timestamp();


        System.out.println("CHILLAR RefundTAble:" + testsalesitem);
        String URL = Constants.APP_URL + "c_refund_transaction.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + slesitm.getTrans_id() + "&refund_transaction_id=" + slesitm.getOrig_trans_id() + "&amount=" + slesitm.getAmount();
        final String id = slesitm.getTrans_id();
        String tag_string_req = "refundtable";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLREFUND " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:REFUND " + response);
                // progressDialog.dismiss();
                try {

                    JSONObject jsobj;
                    jsobj = new JSONObject(response);
                    JSONObject jsonObject = jsobj.getJSONObject("status");
                    String server_respose = jsonObject.getString("code");

                    System.out.println("PhpLoader9  code " + server_respose);
                    if (server_respose.equals("success")) {
                        db.updaterefund(id);
//                        db.close();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversaleitem = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversaleitem;

    }

    private String PhpLoader12(Create_Leave slesitm) {


        String testsalesitem = "StudUserId : " + slesitm.getStud_userId() + " ParentUserId : " + slesitm.getParent_userId() + " IDD : " + slesitm.getId() +
                "reasonTypeID : " + slesitm.getReasonTypId() + " reason : " + slesitm.getReasonComment() + " dateTime : " + slesitm.getTime_stamp();


        //machineCode=CH0004&user_id=1&student_userID=541&parent_userID=542&reasonTypeID=1&reason=fever&dateTime=2017-03-11

        //campuswalletdev.chillarcards.com/machine_api/api_1.7.9/c_student_leave.php?machineCode=CH0004&user_id=1&student_userID=541&parent_userID=542&reasonTypeID=1&reason=fever&dateTime=2017-03-11


        System.out.println("CHILLAR ParentSym CreateLeave" + testsalesitem);
        String URL = "";
        if (slesitm.getReasonComment().equals("")) {
            URL = Constants.APP_URL + "c_student_leave.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&student_userID=" + slesitm.getStud_userId() + "&parent_userID=" + slesitm.getParent_userId() + "&reasonTypeID=" + slesitm.getReasonTypId() + "&dateTime=" + slesitm.getTime_stamp();

        } else {
            URL = Constants.APP_URL + "c_student_leave.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&student_userID=" + slesitm.getStud_userId() + "&parent_userID=" + slesitm.getParent_userId() + "&reasonTypeID=" + slesitm.getReasonTypId() + "&reason=" + slesitm.getReasonComment().replace("\n", "%0A").replace(" ", "%20") + "&dateTime=" + slesitm.getTime_stamp();

        }


        String tag_string_req = "create_leave";
        final String id = String.valueOf(slesitm.getId());

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLCreateLeave " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
                    //                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);


                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoader11  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updateCreateLeave(id);
//                            db.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    //                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_payrefundtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serversaleitemnew = "error";
                //                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in AnnouncementVie" +
                        "w" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversaleitemnew;

    }

    private String PhpLoader10(Online_Recharge online) {

        String testsalesitem = "Online Id : " + online.getId() + " TransID : " + online.getTransID() + " Original TransID: " + online.getOnlineTransID() + " Servertimestamp : " + online.getServerTimestamp();


        System.out.println("CHILLAR OnlineTAble:" + testsalesitem);
        String URL = Constants.APP_URL + "c_online_to_recharge.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&online_transaction_id=" + online.getOnlineTransID() + "&transaction_id=" + online.getTransID();
        final String id = online.getTransID();
        String tag_string_req = "onlineTable";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLONLINE " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:ONLINE " + response);
                // progressDialog.dismiss();
                try {

                    JSONObject jsobj;
                    jsobj = new JSONObject(response);
                    JSONObject jsonObject = jsobj.getJSONObject("status");
                    String server_respose = jsonObject.getString("code");

                    System.out.println("PhpLoader10  code " + server_respose);
                    if (server_respose.equals("success")) {
                        db.updateOnline(id);
//                        db.close();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversaleitem = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversaleitem;

    }

    private String PhpLoaderNew1(Success_Transaction usp) {

        final String id = usp.gettrans_id();
        String tag_string_req = "successtransactionnew";

        String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                "  Server timestamp  " + usp.getserver_timestamp();
        System.out.println("CHILLAR secact Refund success transaction table " + testpermission);


        String URL = Constants.APP_URL + "c_success_transaction_refund.php?machineCode=" + schoomachcode + "&current_user=" + useridinteger + "&user_id=" + usp.getuser_id() + "&transaction_id=" + usp.gettrans_id() + "&card_serial=" + usp.getcard_serial() + "&transaction_type_id=" + usp.gettranstypeid() + "&current_balance=" + usp.getcurrent_balance() + "&prev_balance=" + usp.getprevious_balnce() + "&time_stamp=" + usp.gettime_stamp();

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);

                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoaderNew1  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updatesuccessRefund2(id);
//                            db.close();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversuccessnew = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversuccessnew;

    }

    //
    private String PhpLoaderNew2(Item_Sale usp1) {

        String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
                "   total:   " + usp1.gettot_amount() +
                "  Server timestamp  " + usp1.getserver_timestamp() +
                "  SaleTransID " + usp1.getsale_trans_id();
        System.out.println("CHILLAR secact Refund Sales ITEMSALE:" + testpermission);


        String URL = Constants.APP_URL + "c_item_sale_transaction_refund.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + usp1.gettransaction_id() + "&bill_no=" + usp1.getbill_no() + "&total_amount=" + usp1.gettot_amount();

        final String id = usp1.gettransaction_id();

        String tag_string_req = "itemsalenew";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
                    //                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);

                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoadernew2  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updateitemssRefund2(id);
//                            db.close();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    //                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversuccessnew = "error";
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversuccessnew;

    }

    //
    private String PhpLoaderNew3(Sales_Item slesitm) {


        String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
                + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


        System.out.println("CHILLAR secact  Refund Sales ITEM:" + testsalesitem);
        String URL = Constants.APP_URL + "c_sales_item_list_refund.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + slesitm.getsales_trans_id() + "&item_id=" + slesitm.getitem_id() + "&amount=" + slesitm.getamount() + "&item_quantity=" + slesitm.getitem_quantity();

        String tag_string_req = "salesitemnew";
        final String id = slesitm.getsales_trans_id();

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
                    //                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);


                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoadernew3  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updatesaleRefund2(id);
//                            db.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    //                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serversaleitemnew = "error";
                //                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversaleitemnew;

    }

    private String PhpLoader11(Payment_Transaction slesitm) {


        String testsalesitem = "PayID : " + slesitm.getId() + " TransId : " + slesitm.gettrans_id() +
                "BillNo : " + slesitm.getbillno() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


        System.out.println("CHILLAR secact  Refund Payment" + testsalesitem);
        String URL = Constants.APP_URL + "c_payment_transaction_refund.php?machineCode=" + schoomachcode + "&user_id=" + useridinteger + "&transaction_id=" + slesitm.gettrans_id() + "&bill_no=" + slesitm.getbillno() + "&amount=" + slesitm.getamount();

        String tag_string_req = "payrefund";
        final String id = slesitm.gettrans_id();

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLPayRefund " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                // progressDialog.dismiss();
                if (response.equalsIgnoreCase("NetworkError")) {
                    //                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);


                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoader11  code " + server_respose);
                        if (server_respose.equals("success")) {
                            db.updatePaymentRefund2(id);
//                            db.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    //                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_payrefundtransaction " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serversaleitemnew = "error";
                //                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return serversaleitemnew;

    }

    public void UpdateAPP() {

        Constants.dialogShown = false;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        System.out.println("CHILLR: " + uriString);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/app-debug.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);

    }

    public int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }

        return res;
    }

    public String Checkversion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        Toast.makeText(this,
//                "PackageName = " + info.packageName + "\nVersionCode = "
//                        + info.versionCode + "\nVersionName = "
//                        + info.versionName + "\nPermissions = " + info.permissions, Toast.LENGTH_SHORT).show();

        return info.versionName;
    }
    //

    public void clearDatabase(Context context) {
        DatabaseHandler helper = new DatabaseHandler(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete("blocked_cards_info", null, null); //erases everything in the table.

        database.close();
    }

    private class timerUpload extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isURLReachable1(getApplicationContext())) {
                updateurl();
            }
            return null;
        }
    }

    class deliverReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {

            try {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Download finished",
                                Toast.LENGTH_SHORT).show();
                        System.out.println("CODMOB: success dwnld");

                        break;
                    case Activity.RESULT_CANCELED:

                        Toast.makeText(getBaseContext(), "Error downloading file",
                                Toast.LENGTH_SHORT).show();
                        break;

                }

            } catch (Exception e) {


            }

        }
    }
}