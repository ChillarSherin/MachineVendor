package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Attendance_Data;
import com.chillarcards.machinevendor.ModelClass.Attendance_Type;
import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.CategoryList;
import com.chillarcards.machinevendor.ModelClass.Create_Leave;
import com.chillarcards.machinevendor.ModelClass.Device_Info;
import com.chillarcards.machinevendor.ModelClass.Fee_Transaction;
import com.chillarcards.machinevendor.ModelClass.ItemList;
import com.chillarcards.machinevendor.ModelClass.ItemStock;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Item_Type;
import com.chillarcards.machinevendor.ModelClass.Library_book_transaction;
import com.chillarcards.machinevendor.ModelClass.Online_Recharge;
import com.chillarcards.machinevendor.ModelClass.Parent;
import com.chillarcards.machinevendor.ModelClass.Parent_Student;
import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Payment_Type;
import com.chillarcards.machinevendor.ModelClass.Reason;
import com.chillarcards.machinevendor.ModelClass.Recharge_Data;
import com.chillarcards.machinevendor.ModelClass.Refund;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.StudentList;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.ModelClass.Teacher_Details;
import com.chillarcards.machinevendor.ModelClass.TransactionType;
import com.chillarcards.machinevendor.ModelClass.User;
import com.chillarcards.machinevendor.ModelClass.User_Permission_Data;


/**
 * Created by Codmob on 22-07-16.
 */
public class DatabaseActivity extends Activity {

    Button carddetails, update, serverup, app_restart, disp_tables, apk_update, parentcard, tables_to_update, dl_parent, app_exit, blockCards;
    String servertimestamp = "";
    int useridinteger;
    String User_name, Pass_word, userID, schooid, schoomachcode, machineserial, machineID, machineSL;
    TextView initTXTV;
    String schoolname, schoolpalce, lasttransid;
    Dialog dlg;
    ProgressBar pbar;
    String serversuccess, serverattdata, serverrecharg, serverfee, serverlibrar, serverpay, serveritemsale, serversaleitem, serversuccessnew, serveritemsalenew, serversaleitemnew;
    //    DatabaseHandler db = new DatabaseHandler(this);
    DatabaseHandler db;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int tama_on;
    String uriString = "";
    LinearLayout linearLayout;
    ProgressBar progressBar, progressBar1;
    String versionName = "";
    int flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag5 = 0, flag6 = 0, flag7 = 0, flag8 = 0, flag9 = 0,
            flag10 = 0, flag11 = 0, flag12 = 0, flag13 = 0, flag14 = 0, flag15 = 0, flag16 = 0;
    List<Success_Transaction> successtransaction7 = new ArrayList<>();
    List<Success_Transaction> sxstrnxn = new ArrayList<>();
    List<Attendance_Data> attdatas = new ArrayList<>();
    List<Attendance_Data> teacher_attdatas = new ArrayList<>();
    List<Recharge_Data> rechrgedata = new ArrayList<>();
    List<Fee_Transaction> contacts3 = new ArrayList<>();
    List<Library_book_transaction> contacts1 = new ArrayList<>();
    List<Payment_Transaction> paytransaction = new ArrayList<>();
    List<Item_Sale> success = new ArrayList<>();
    List<Sales_Item> sales_item = new ArrayList<>();
    List<Item_Sale> itsale = new ArrayList<>();
    List<Refund> refunds = new ArrayList<>();
    List<Online_Recharge> online = new ArrayList<>();
    List<Sales_Item> salesitm = new ArrayList<>();
    List<Payment_Transaction> paytransaction1 = new ArrayList<>();
    List<Create_Leave> createleave = new ArrayList<>();
    int statusCount = 0;
    int totalCount = 0;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private DownloadManager dm1;
    private long enqueue1;
    private BroadcastReceiver reciever1;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    static public boolean isURLReachable(Context context) {
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
                System.out.println("CHILLERS: malformed");
                return false;
            } catch (IOException e) {
                System.out.println("CHILLERS: IOException");
                return false;
            }
        }
        System.out.println("CHILLERS: out");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_database);

        db = DatabaseHandler.getInstance(getApplicationContext());

        carddetails = (Button) findViewById(R.id.carddetails);
        update = (Button) findViewById(R.id.update);
        serverup = (Button) findViewById(R.id.updatetoserver);
        app_restart = (Button) findViewById(R.id.app_restart);
        disp_tables = (Button) findViewById(R.id.disp_table);
        tables_to_update = (Button) findViewById(R.id.tables_to_update);
        apk_update = (Button) findViewById(R.id.apk_update);
        parentcard = (Button) findViewById(R.id.parent);
        dl_parent = (Button) findViewById(R.id.parentDetails);
        blockCards = (Button) findViewById(R.id.blockCards);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar1 = (ProgressBar) findViewById(R.id.progress1);
        progressBar.setVisibility(View.GONE);
        progressBar1.setVisibility(View.GONE);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        linearLayout.setVisibility(View.VISIBLE);


        SharedPreferences editor1 = getSharedPreferences("Chillar", MODE_PRIVATE);
        versionName = editor1.getString("appVersion", "");


        app_exit = (Button) findViewById(R.id.exitapp);

        app_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllsuccesstoup().size() > 0 ||
                        db.getAllsxstoupNew().size() > 0 ||
                        db.getAllitemsaletoUp().size() > 0 ||
                        db.getAllitemsaletoUpNew().size() > 0 ||
                        db.getAllsaletoUp().size() > 0 ||
                        db.getAllsaletoUpNew().size() > 0 ||
                        db.getAlllibtoUp().size() > 0 ||
                        db.getAllAttendancedatatoUP().size() > 0 ||
                        db.getAllteacherAttendancedatatoUP().size() > 0 ||
                        db.getAllrechtoUp().size() > 0 ||
                        db.getAllfeetranstoUp().size() > 0 ||
                        db.getAllpaytransactiontoUp().size() > 0 ||
                        db.getAllpaytransactiontoUpNew().size() > 0 ||
                        db.getAllOnlineToUp().size() > 0 ||
                        db.getAllRefundtoUp().size() > 0 ||
                        db.getAllCreateLeaveToUp().size() > 0) {

                    Toast.makeText(getApplicationContext(), "Database Not uploaded completely. Please upload and then try again.", Toast.LENGTH_LONG).show();

                } else {
                    finishAffinity();
                }
            }
        });


        //RESTARTING APP
        app_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


            }
        });


        blockCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BlockCardsNewActivity.class);
                startActivity(i);
            }
        });


        drawerArrow();

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userID = String.valueOf(editor.getInt("userid", 0));
        schooid = editor.getString("schoolId", "");
        schoomachcode = editor.getString("schlMachCode", "");
        machineserial = editor.getString("machineserial", "");
        machineID = editor.getString("machineid", "");
        machineSL = editor.getString("machineSL", "");
        System.out.println("CODMOB: 123  serial " + schooid + schoomachcode + machineserial);

        useridinteger = Integer.parseInt(userID);


        parentcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SharedPreferences[] preferences = {getSharedPreferences("Chillar", MODE_PRIVATE)};
                String download_status = preferences[0].getString("download_parent", "");

                System.out.println("SharedPref: " + download_status);

                if (download_status.equals("true")) {
                    Intent i = new Intent(getApplicationContext(), ParentsMeetingActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Download Details first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        carddetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CardDetailsActivity.class);
                startActivity(intent);

            }
        });


        apk_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                updateApk();

            }
        });


        dl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*progressBar1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                new reachableURL3().execute();*/


                Intent i = new Intent(getApplicationContext(), InternetCheckActivity.class);
                Bundle b = new Bundle();
                b.putString("type", "4");
                i.putExtras(b);
                startActivity(i);


            }
        });

        //RE INITIALIZE ALL TABLES TO SERVER

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if(db.getAllsuccesstoup().size()>0||
                        db.getAllsxstoupNew().size()>0||
                        db.getAllitemsaletoUp().size()>0||
                        db.getAllitemsaletoUpNew().size()>0 ||
                        db.getAllsaletoUp().size()>0||
                        db.getAllsaletoUpNew().size()>0||
                        db.getAlllibtoUp().size()>0||
                        db.getAllAttendancedatatoUP().size()>0 ||
                        db.getAllteacherAttendancedatatoUP().size()>0||
                        db.getAllrechtoUp().size()>0||
                        db.getAllfeetranstoUp().size()>0||
                        db.getAllpaytransactiontoUp().size()>0||
                        db.getAllpaytransactiontoUpNew().size()>0||
                        db.getAllOnlineToUp().size()>0 ||
                        db.getAllRefundtoUp().size()>0 ||
                        db.getAllCreateLeaveToUp().size()>0){

                    Toast.makeText(getApplicationContext(),"Database Not uploaded completely. Please upload and then try again.",Toast.LENGTH_LONG).show();

                }else {

                    new reachableURL2().execute();
//                    if (isURLReachable(getApplicationContext())) {
//
//                        dlg = new Dialog(v.getContext());
//                        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dlg.setContentView(R.layout.activity_main);
//                        dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        dlg.setCancelable(true);
//                        dlg.setCanceledOnTouchOutside(false);
//                        dlg.show();
//
////                ProgressBar pbar= (ProgressBar) dlg.findViewById(R.id.progbar);
//                        initTXTV = (TextView) dlg.findViewById(R.id.initID);
//                        pbar = (ProgressBar) dlg.findViewById(R.id.progbar);
//                        pbar.setVisibility(View.VISIBLE);
//
//                        SecondActivity.stoptimertask();
//
//
//                        try {
//                            DeviceInfo();
//                        }catch (IllegalStateException e){
//                        e.printStackTrace();
//                        DeviceInfo();
//                    }
//
//
//                    } else {
//
//                        Toast.makeText(DatabaseActivity.this, "Network is not Connected", Toast.LENGTH_SHORT).show();
//
//                    }
                }*/

                Intent i = new Intent(getApplicationContext(), InternetCheckActivity.class);
                Bundle b = new Bundle();
                b.putString("type", "2");
                i.putExtras(b);
                startActivity(i);


            }
        });


        disp_tables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AllTablesDisplay.class);
                startActivity(i);
            }
        });
        tables_to_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TablesToUpdate.class);
                startActivity(i);
            }
        });


        reciever1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String action = intent.getAction();
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                        long downloadId = intent.getLongExtra(
                                DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(enqueue1);
                        Cursor c = dm1.query(query);
                        if (c.moveToFirst()) {
                            int columnIndex = c
                                    .getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                                Toast.makeText(getApplicationContext(), "Apk download successfull. ", Toast.LENGTH_LONG).show();

//                            ImageView view = (ImageView) findViewById(R.id.imageView1);

                                try {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            DatabaseActivity.this);

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

                                uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//

                                System.out.println("CODMOB:Download callback: " + uriString);
                            } else if (DownloadManager.STATUS_RUNNING == c.getInt(columnIndex)) {

                                System.out.println("CODMOB:Download callback: Running");

                            } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {


                                dm1.remove(c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));

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

        registerReceiver(reciever1, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        // UPDATE ALL TABLES TO SERVER
        serverup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                progressBar1.setVisibility(View.VISIBLE);

//                new reachableURL().execute();

                Intent i = new Intent(getApplicationContext(), InternetCheckActivity.class);
                Bundle b = new Bundle();
                b.putString("type", "1");
                i.putExtras(b);
                startActivity(i);

//
//                if (isURLReachable(getApplicationContext())){
////                            Toast.makeText(SecondActivity.this, "Network is connected", Toast.LENGTH_SHORT).show();
//                    dlg = new Dialog(v.getContext());
//                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dlg.setContentView(R.layout.layout_server_update);
//                    dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    dlg.setCancelable(true);
//                    dlg.setCanceledOnTouchOutside(false);
//                    dlg.show();
//
//                    pbar= (ProgressBar) dlg.findViewById(R.id.progbar);
//                    pbar.setVisibility(View.VISIBLE);
//
////                    do {
////                        updateurl();
////
////                    }while(db.getAllsuccesstoup().size()>0||
////                            db.getAllsxstoupNew().size()>0||
////                            db.getAllitemsaletoUp().size()>0||
////                            db.getAllitemsaletoUpNew().size()>0 ||
////                            db.getAllsaletoUp().size()>0||
////                            db.getAllsaletoUpNew().size()>0||
////                            db.getAlllibtoUp().size()>0||
////                            db.getAllAttendancedatatoUP().size()>0 ||
////                            db.getAllrechtoUp().size()>0||
////                            db.getAllfeetranstoUp().size()>0||
////                            db.getAllpaytransactiontoUp().size()>0);
//                    try {
//                        updateurl1();
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(),"Sorry Please Try again. ",Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//
//
////                    while (db.getAllsuccesstoup().size()>0)
////                    {
//
//
//
////                    }
//
////                    while(db.getAllsuccesstoup().size()>0||db.getAllsxstoupNew().size()>0||db.getAllitemsaletoUp().size()>0||db.getAllitemsaletoUpNew().size()>0
////                            ||db.getAllsaletoUp().size()>0||db.getAllsaletoUpNew().size()>0||db.getAlllibtoUp().size()>0||db.getAllAttendancedatatoUP().size()>0
////                            ||db.getAllrechtoUp().size()>0||db.getAllfeetranstoUp().size()>0||db.getAllpaytransactiontoUp().size()>0){
////                        updateurl();
////                    }
//
//
//
//                    dlg.cancel();
//                }
//                else {
//                            Toast.makeText(DatabaseActivity.this, "Network is not Connected", Toast.LENGTH_SHORT).show();
//                }


            }
        });


    }
    //

    public void UpdateAPP() {

        Constants.dialogShown = false;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        System.out.println("CHILLR: " + uriString);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/app-debug.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);

    }

    public void DownloadAPK1() {

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        String User_name1 = editor.getString("username", "");
        String Pass_word1 = editor.getString("password", "");
        String userID1 = String.valueOf(editor.getInt("userid", 0));
        String schooid1 = editor.getString("schoolId", "");
        String schoomachcode1 = editor.getString("schlMachCode", "");

        String apklink = Constants.APP_URL + "r_download_new_apk.php?machineUserName=" + User_name1 + "&machineUserPassword=" + Pass_word1 + "&schoolMachineCode=" + schoomachcode1;

        System.out.println("apklink : " + apklink);
//
//        String apklink="http://192.168.0.223/campuswallettest/test_apk/app-debug.apk";

        dm1 = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apklink));

        if (isFileExists("app-debug.apk")) {
            deleteFile1("app-debug.apk");
        }

        request/*.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)*/
                .setDestinationInExternalPublicDir("/DCIM", "app-debug.apk");
        enqueue1 = dm1.enqueue(request);


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

    private void updateApk() {
        String tag_string_req = "updateAppPhp";

        String url = Constants.APP_URL + "r_new_apk.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
        System.out.println("UrlUpdatePhp " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());

                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
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
                                        db.getAllteacherAttendancedatatoUP().size() > 0 ||
                                        db.getAllrechtoUp().size() > 0 ||
                                        db.getAllfeetranstoUp().size() > 0 ||
                                        db.getAllpaytransactiontoUp().size() > 0 ||
                                        db.getAllpaytransactiontoUpNew().size() > 0 ||
                                        db.getAllOnlineToUp().size() > 0 ||
                                        db.getAllRefundtoUp().size() > 0 ||
                                        db.getAllCreateLeaveToUp().size() > 0) {


                                    try {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                                DatabaseActivity.this);

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

                                                    }
                                                });

                                        alert.show();
                                    } catch (WindowManager.BadTokenException e) {
                                        System.out.println("AlertDialog exception catched");
                                    }

                                } else {

                                    try {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                                DatabaseActivity.this);

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

                                                        DownloadAPK1();

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
                            } else {
                                Toast.makeText(getApplicationContext(), "No New Version Available!", Toast.LENGTH_SHORT).show();
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
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                System.out.println("CODMOB: Error in updateAPk" + error.getMessage());


//                Toast.makeText(getApplicationContext(), "some error occurred in Blocked cards info. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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


/*
    public void updateurl1() throws Exception{

        progressBar.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);


        statusCount=0;
        totalCount=0;
        successtransaction7 = db.getAllsuccesstoup();
        sxstrnxn = db.getAllsxstoupNew();
        attdatas = db.getAllAttendancedatatoUP();
        teacher_attdatas = db.getAllteacherAttendancedatatoUP();
        rechrgedata = db.getAllrechtoUp();
        contacts3 = db.getAllfeetranstoUp();
        contacts1 = db.getAlllibtoUp();
        paytransaction = db.getAllpaytransactiontoUp();
        success = db.getAllitemsaletoUp();
        sales_item = db.getAllsaletoUp();
        itsale = db.getAllitemsaletoUpNew();
        refunds = db.getAllRefundtoUp();
        online = db.getAllOnlineToUp();
        salesitm = db.getAllsaletoUpNew();
        paytransaction1 = db.getAllpaytransactiontoUpNew();
        createleave = db.getAllCreateLeaveToUp();

        totalCount+=successtransaction7.size()+sxstrnxn.size()+attdatas.size()+teacher_attdatas.size()+rechrgedata.size()+
                contacts3.size()+
                contacts1.size()+
                paytransaction.size()+
                success.size()+
                sales_item.size()+
                itsale.size()+
                refunds.size()+
                online.size()+
                salesitm.size()+
                paytransaction1.size()+
                createleave.size();

        progressBar.setMax(totalCount);



        if(successtransaction7.size()>0) {
            PhpLoaderTest1(successtransaction7, 0);
        }else if(sxstrnxn.size()>0){
            PhpLoaderNewTest1(sxstrnxn,0);
        }else if(attdatas.size()>0 ){
            PhpLoaderTest2(attdatas,0);
        }else if(teacher_attdatas.size()>0){
            PhpLoaderteacheratteTest(teacher_attdatas,0);
        }else if(rechrgedata.size()>0){
            PhpLoaderTest3(rechrgedata,0);
        }else if(contacts3.size()>0){
            PhpLoaderTest4(contacts3,0);
        }else if(contacts1.size()>0){
            PhpLoaderTest5(contacts1,0);
        }else if(paytransaction.size()>0){
            PhpLoaderTest6(paytransaction,0);
        }else if(success.size()>0){
            PhpLoaderTest7(success,0);
        }else if(sales_item.size()>0){
            PhpLoaderTest8(sales_item,0);
        }else if(itsale.size()>0){
            PhpLoaderNewTest2(itsale,0);
        }else if(refunds.size()>0){
            PhpLoaderTest9(refunds,0);
        }else if(online.size()>0){
            PhpLoaderTest10(online,0);
        }else if(salesitm.size()>0){
            PhpLoaderNewTest3(salesitm,0);
        }else if(paytransaction1.size()>0){
            PhpLoaderTest11(paytransaction1,0);
        }else if(createleave.size()>0){
            PhpLoaderTest12(createleave,0);
        }else {
            System.out.println("FU1");
//            Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
            progressBar1.setVisibility(View.VISIBLE);
            BlockedCards1();
        }


    }

    private void PhpLoaderTest1(final List<Success_Transaction> successtransaction7, final int i) {


        final String tag_string_req="successtrnxn";

        Success_Transaction usp=successtransaction7.get(i);

        String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                "  Server timestamp  " + usp.getserver_timestamp();
        System.out.println("CHILLAR secact success transaction table " + testpermission);
        final String id =usp.gettrans_id();

        String URL=Constants.APP_URL+"c_successTransaction.php?machineCode="+schoomachcode+"&current_user="+useridinteger+"&user_id="+usp.getuser_id()+"&transaction_id="+usp.gettrans_id()+"&card_serial="+usp.getcard_serial()+"&transaction_type_id="+usp.gettranstypeid()+"&current_balance="+usp.getcurrent_balance()+"&prev_balance="+usp.getprevious_balnce()+"&time_stamp="+usp.gettime_stamp()+"&current_version="+versionName;

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                System.out.println("CODMOB: TESTS "+response);
                System.out.println("testf"+response.toString());

                ++statusCount;
                progressBar.setProgress(statusCount);


                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoader1  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updatesuccess(id);
//                            db.close();
                        }else{
                            ++flag1;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                    }


                int count=i;
                ++count;

                if(db.getAllsuccesstoup().size()>0){

                    PhpLoaderTest1(successtransaction7,count);

                }else if(sxstrnxn.size()>0){
                    PhpLoaderNewTest1(sxstrnxn,0);
                }else if(attdatas.size()>0 ){
                    PhpLoaderTest2(attdatas,0);
                }else if(teacher_attdatas.size()>0){
                    PhpLoaderteacheratteTest(teacher_attdatas,0);
                }else if(rechrgedata.size()>0){
                    PhpLoaderTest3(rechrgedata,0);
                }else if(contacts3.size()>0){
                    PhpLoaderTest4(contacts3,0);
                }else if(contacts1.size()>0){
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU2");
//                    Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.VISIBLE);
                    BlockedCards1();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversuccess="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

            }
        });


// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void PhpLoaderNewTest1(final List<Success_Transaction> sxstrnxn, final int i) {


        Success_Transaction usp=sxstrnxn.get(i);
        final String id =usp.gettrans_id();
        final String  tag_string_req = "successtransactionnew";

        String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                "  Server timestamp  " + usp.getserver_timestamp();
        System.out.println("CHILLAR secact Refund success transaction table " + testpermission);

        String currbal = "",prevbal="";

        if(usp.getcurrent_balance()%1==0){

            int current= (int) (usp.getcurrent_balance()+0);
            currbal= String.valueOf(current);
        }else{
            currbal= String.valueOf(usp.getcurrent_balance());
        }
        if(usp.getprevious_balnce()%1==0){

            int previous= (int) (usp.getprevious_balnce()+0);
            prevbal= String.valueOf(previous);
        }else{
            prevbal= String.valueOf(usp.getprevious_balnce());
        }

        String URL=Constants.APP_URL+"c_success_transaction_refund.php?machineCode="+schoomachcode+"&current_user="+useridinteger+"&user_id="+usp.getuser_id()+"&transaction_id="+usp.gettrans_id()+"&card_serial="+usp.getcard_serial()+"&transaction_type_id="+usp.gettranstypeid()+"&current_balance="+currbal+"&prev_balance="+prevbal+"&time_stamp="+usp.gettime_stamp();

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());

                ++statusCount;
                progressBar.setProgress(statusCount);

                System.out.println("CODMOB:successtransaction " + response);

                try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);

                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoaderNew1  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updatesuccessRefund2(id);
//                            db.close();
                        }else{
                            ++flag12;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }

                    int count=i;
                    ++count;

                        if(db.getAllsxstoupNew().size()>0){

                            PhpLoaderNewTest1(sxstrnxn,count);

                        }else if(attdatas.size()>0 ){
                            PhpLoaderTest2(attdatas,0);
                        }else if(teacher_attdatas.size()>0){
                            PhpLoaderteacheratteTest(teacher_attdatas,0);
                        }else if(rechrgedata.size()>0){
                            PhpLoaderTest3(rechrgedata,0);
                        }else if(contacts3.size()>0){
                            PhpLoaderTest4(contacts3,0);
                        }else if(contacts1.size()>0){
                            PhpLoaderTest5(contacts1,0);
                        }else if(paytransaction.size()>0){
                            PhpLoaderTest6(paytransaction,0);
                        }else if(success.size()>0){
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU3");
//                            Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.VISIBLE);
                            BlockedCards1();
                        }



                    } catch (Exception e) {

                        e.printStackTrace();

                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serversuccessnew="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void PhpLoaderTest2(final List<Attendance_Data> attdatas, final int i) {


        Attendance_Data uspaytrans=attdatas.get(i);

        String payString = " attendencedataID : " + uspaytrans.getAttendance_data_id() + " attendencedataTypeID : " + uspaytrans.getAttendance_type_id()
                + " INOUT : " + uspaytrans.getIn_out() +" transactionID : " + uspaytrans.getTransaction_id() +" Servertimestamp" + uspaytrans.getServer_timestamp();
        System.out.println("CHILLAR secact attdata" + payString);


        String URL = Constants.APP_URL+"c_attendance_transaction.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+uspaytrans.getTransaction_id()+"&attendance_type_id="+uspaytrans.getAttendance_type_id()+"&in_out="+uspaytrans.getIn_out();

        String  tag_string_req = "attendence";
        final String id =uspaytrans.getTransaction_id();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);

                ++statusCount;
                progressBar.setProgress(statusCount);

                try {

                    JSONObject jsobj;
                    jsobj = new JSONObject(response);
                    JSONObject jsonObject=jsobj.getJSONObject("status");
                    String server_respose=jsonObject.getString("code");



                    System.out.println("PhpLoader2  code "+server_respose);
                    if (server_respose.equals("success"))
                    {
                        System.out.println("ELDDD: update att ");
                        db.updateAttendancedata(id);
//                            db.close();
                    }else{
                        ++flag2;
                        Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllAttendancedatatoUP().size()>0){

                    PhpLoaderTest2(attdatas,count);

                }else if(sxstrnxn.size()>0){
                    PhpLoaderNewTest1(sxstrnxn,0);
                }else if(teacher_attdatas.size()>0){
                    PhpLoaderteacheratteTest(teacher_attdatas,0);
                }else if(rechrgedata.size()>0){
                    PhpLoaderTest3(rechrgedata,0);
                }else if(contacts3.size()>0){
                    PhpLoaderTest4(contacts3,0);
                }else if(contacts1.size()>0){
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    PhpLoaderTest12(createleave,0);
                }else {System.out.println("FU4");
//                    Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.VISIBLE);
                    BlockedCards1();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                serverattdata="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());

                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void PhpLoaderteacheratteTest(final List<Attendance_Data> teacher_attdatas, final int i) {


        Attendance_Data uspaytrans=teacher_attdatas.get(i);

        String payString = " attendencedataID : " + uspaytrans.getAttendance_data_id() + " attendencedataTypeID : " + uspaytrans.getAttendance_type_id()
                + " INOUT : " + uspaytrans.getIn_out() +" transactionID : " + uspaytrans.getTransaction_id()+"CardSerial  "+uspaytrans.getcard_serial() +" Servertimestamp" + uspaytrans.getServer_timestamp();
        System.out.println("CHILLAR secact attdata" + payString);


        String URL = Constants.APP_URL+"c_teacher_attendance.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+uspaytrans.getTransaction_id()+"&in_out="+uspaytrans.getIn_out();

        String  tag_string_req = "attendence";
        final String id =uspaytrans.getTransaction_id();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);


                try {

                    JSONObject jsobj;
                    jsobj = new JSONObject(response);
                    JSONObject jsonObject=jsobj.getJSONObject("status");
                    String server_respose=jsonObject.getString("code");



                    System.out.println("PhpLoader2  code "+server_respose);
                    if (server_respose.equals("success"))
                    {
                        System.out.println("ELDDD: update att ");
                        db.updateAttendancedata(id);
//                            db.close();
                    }else{
                        ++flag3;
                        Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllteacherAttendancedatatoUP().size()>0){

                    PhpLoaderteacheratteTest(teacher_attdatas,count);

                }else if(sxstrnxn.size()>0){
                    PhpLoaderNewTest1(sxstrnxn,0);
                }else if(rechrgedata.size()>0){
                    PhpLoaderTest3(rechrgedata,0);
                }else if(contacts3.size()>0){
                    PhpLoaderTest4(contacts3,0);
                }else if(contacts1.size()>0){
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU16");
//                    Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.VISIBLE);
                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serverattdata="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void PhpLoaderTest3(final List<Recharge_Data> rechrgedata, final int i) {

        Recharge_Data rech=rechrgedata.get(i);

        String payString = " recharge_ID : " + rech.getrecharge_id() + " transaction_ID : " + rech.gettransaction_id()
                + " RechAMount : " + rech.getRech_amt() + " recharge_TIME  : " + rech.getrecharge_time() + " PaymentType_ID : " + rech.getpayment_type_id() +
                " Servertimestamp" + rech.getserver_timestamp();
        System.out.println("CHILLAR secact recharge3" + payString);
        String  tag_string_req = "recharge";
        String URL = Constants.APP_URL+"c_card_recharge.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&payment_type_id="+rech.getpayment_type_id()+"&transaction_id="+rech.gettransaction_id()+"&amount="+rech.getRech_amt()+"&recharge_time="+rech.getrecharge_time();

        final String id =rech.gettransaction_id();
        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");



                        System.out.println("PhpLoader3  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updaterechrge(id);
//                            db.close();
                        }else{
                            ++flag4;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }


//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction "+response);

                    int count=i;
                    ++count;

                    if(db.getAllrechtoUp().size()>0){

                        PhpLoaderTest3(rechrgedata,count);

                    }else if(contacts3.size()>0){
                        PhpLoaderTest4(contacts3,0);
                    }else if(contacts1.size()>0){
                        PhpLoaderTest5(contacts1,0);
                    }else if(paytransaction.size()>0){
                        PhpLoaderTest6(paytransaction,0);
                    }else if(success.size()>0){
                        PhpLoaderTest7(success,0);
                    }else if(sales_item.size()>0){
                        PhpLoaderTest8(sales_item,0);
                    }else if(itsale.size()>0){
                        PhpLoaderNewTest2(itsale,0);
                    }else if(refunds.size()>0){
                        PhpLoaderTest9(refunds,0);
                    }else if(online.size()>0){
                        PhpLoaderTest10(online,0);
                    }else if(salesitm.size()>0){
                        PhpLoaderNewTest3(salesitm,0);
                    }else if(paytransaction1.size()>0){
                        PhpLoaderTest11(paytransaction1,0);
                    }else if(createleave.size()>0){
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU5");
//                        Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                        BlockedCards1();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serverrecharg="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void PhpLoaderTest4(final List<Fee_Transaction> contacts3, final int i) {

        Fee_Transaction cn=contacts3.get(i);

        String log = " ,billno: " + cn.getbill_no() + " ,TotalAmt:" + cn.gettot_am() + ",TransId:" + cn.gettrans_id() + " ,ServerTimeStamp:" + cn.getserver_timestamp();
        // Writing Contacts to log
        System.out.println("CHILLAR secact FEETRANS : " + log);
        String URL = Constants.APP_URL+"c_fee_transaction.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+cn.gettrans_id()+"&bill_no="+cn.getbill_no()+"&total_amount="+cn.gettot_am();

        final String id=cn.gettrans_id();
        String  tag_string_req = "fees";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);

                ++statusCount;
                progressBar.setProgress(statusCount);

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

                        System.out.println("PhpLoader4  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updatefeetran(id);
//                            db.close();
                        }else{
                            ++flag5;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction "+response);

                    int count=i;
                    ++count;

                    if(db.getAllfeetranstoUp().size()>0){

                        PhpLoaderTest4(contacts3,count);

                    }else if(contacts1.size()>0){
                        PhpLoaderTest5(contacts1,0);
                    }else if(paytransaction.size()>0){
                        PhpLoaderTest6(paytransaction,0);
                    }else if(success.size()>0){
                        PhpLoaderTest7(success,0);
                    }else if(sales_item.size()>0){
                        PhpLoaderTest8(sales_item,0);
                    }else if(itsale.size()>0){
                        PhpLoaderNewTest2(itsale,0);
                    }else if(refunds.size()>0){
                        PhpLoaderTest9(refunds,0);
                    }else if(online.size()>0){
                        PhpLoaderTest10(online,0);
                    }else if(salesitm.size()>0){
                        PhpLoaderNewTest3(salesitm,0);
                    }else if(paytransaction1.size()>0){
                        PhpLoaderTest11(paytransaction1,0);
                    }else if(createleave.size()>0){
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU6");
//                        Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                        BlockedCards1();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serverfee="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void PhpLoaderTest5(final List<Library_book_transaction> contacts1, final int i) {

        Library_book_transaction cnlib=contacts1.get(i);

        String log = "Id: " + cnlib.getid() + " ,BookId: " + cnlib.getbook_id() + " ,Issued:" + cnlib.getissue_time() + ",TransId:" + cnlib.gettransaction_id() + " ,ServerTimeStamp:" + cnlib.getserver_timestamp() + " ,Return:" + cnlib.getissue_return();
        // Writing Contacts to log
        System.out.println("CHILLAR secact LIBRARY : " + log);
        String URL = Constants.APP_URL+"c_library_book_transaction.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+cnlib.gettransaction_id()+"&issue_return="+cnlib.getissue_return()+"&book_id="+cnlib.getbook_id()+"&issue_time="+cnlib.getissue_time();

        final String id=cnlib.gettransaction_id();

        String  tag_string_req = "library";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

                        System.out.println("PhpLoader5  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updatelibr(id);
//                            db.close();
                        }else{
                            ++flag6;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction "+response);

                    int count=i;
                    ++count;

                    if(db.getAlllibtoUp().size()>0){

                        PhpLoaderTest5(contacts1,count);

                    }else{


                        List<Payment_Transaction> paytransaction = db.getAllpaytransactiontoUp();

                        PhpLoaderTest6(paytransaction,0);

                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serverlibrar="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void PhpLoaderTest6(final List<Payment_Transaction> paytransaction, final int i) {

        Payment_Transaction uspaytrans=paytransaction.get(i);

        String payString = " ID " + uspaytrans.getId() + "trans_id1 "
                + uspaytrans.gettrans_id() + " Amount " + uspaytrans.getamount() + " Server_time_timestamp " + uspaytrans.getserver_timestamp();
        System.out.println("CHILLAR secact paytransaction3" + payString);

        String URL = Constants.APP_URL+"c_payment_transaction.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+uspaytrans.gettrans_id()+"&bill_no="+uspaytrans.getbillno()+"&amount="+uspaytrans.getamount();
        System.out.println("CODMOB: URRLLL" + URL);
        String  tag_string_req = "payment";
        final String id=uspaytrans.gettrans_id();

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);
                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

                        System.out.println("PhpLoader6  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updatepayment(id);
//                            db.close();
                        }else{
                            ++flag7;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction "+response);


                    int count=i;
                    ++count;

                    if(db.getAllpaytransactiontoUp().size()>0){

                        PhpLoaderTest6(paytransaction,count);

                    }else if(success.size()>0){
                        PhpLoaderTest7(success,0);
                    }else if(sales_item.size()>0){
                        PhpLoaderTest8(sales_item,0);
                    }else if(itsale.size()>0){
                        PhpLoaderNewTest2(itsale,0);
                    }else if(refunds.size()>0){
                        PhpLoaderTest9(refunds,0);
                    }else if(online.size()>0){
                        PhpLoaderTest10(online,0);
                    }else if(salesitm.size()>0){
                        PhpLoaderNewTest3(salesitm,0);
                    }else if(paytransaction1.size()>0){
                        PhpLoaderTest11(paytransaction1,0);
                    }else if(createleave.size()>0){
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU7");
//                        Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                        BlockedCards1();
                    }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serverpay="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void PhpLoaderTest7(final List<Item_Sale> success, final int i) {


        Item_Sale usp1=success.get(i);

        String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
                "   total:   " + usp1.gettot_amount() +
                "  Server timestamp  " + usp1.getserver_timestamp() +
                "  SaleTransID " + usp1.getsale_trans_id();
        System.out.println("CHILLAR secact Sales ITEMSALE:" + testpermission);


        String URL = Constants.APP_URL+"c_item_sale_transaction.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+usp1.gettransaction_id()+"&bill_no="+usp1.getbill_no()+"&total_amount="+usp1.gettot_amount();

        final String id=usp1.gettransaction_id();
        String  tag_string_req = "itemsale";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                String resp=response.toString();
                System.out.println("CODMOB:CHILLERE " + resp);
                ++statusCount;
                progressBar.setProgress(statusCount);

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

                        System.out.println("PhpLoader7  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updateitemss(id);
//                            db.close();
                        }else{
                            ++flag8;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction "+response);


                    int count=i;
                    ++count;

                    if(db.getAllitemsaletoUp().size()>0){

                        PhpLoaderTest7(success,count);

                    }else if(sales_item.size()>0){
                        PhpLoaderTest8(sales_item,0);
                    }else if(itsale.size()>0){
                        PhpLoaderNewTest2(itsale,0);
                    }else if(refunds.size()>0){
                        PhpLoaderTest9(refunds,0);
                    }else if(online.size()>0){
                        PhpLoaderTest10(online,0);
                    }else if(salesitm.size()>0){
                        PhpLoaderNewTest3(salesitm,0);
                    }else if(paytransaction1.size()>0){
                        PhpLoaderTest11(paytransaction1,0);
                    }else if(createleave.size()>0){
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU8");
//                        Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                        BlockedCards1();
                    }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serveritemsale="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    private void PhpLoaderTest8(final List<Sales_Item> sales_item, final int i) {


        Sales_Item slesitm=sales_item.get(i);

        String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
                + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


        System.out.println("CHILLAR secact Sales ITEM:" + testsalesitem);
        String URL = Constants.APP_URL+"c_sales_item_list.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+slesitm.getsales_trans_id()+"&item_id="+slesitm.getitem_id()+"&amount="+slesitm.getamount()+"&item_quantity="+slesitm.getitem_quantity();

        final String id= String.valueOf(slesitm.getSales_item_id());
        String  tag_string_req = "salesitem";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

                        System.out.println("PhpLoader8  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updatesale(id);
//                            db.close();

                        }else{
                            ++flag9;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

//                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction "+response);


                    int count=i;
                    ++count;

                    if(db.getAllsaletoUp().size()>0){

                        PhpLoaderTest8(sales_item ,count);

                    }else if(itsale.size()>0){
                        PhpLoaderNewTest2(itsale,0);
                    }else if(refunds.size()>0){
                        PhpLoaderTest9(refunds,0);
                    }else if(online.size()>0){
                        PhpLoaderTest10(online,0);
                    }else if(salesitm.size()>0){
                        PhpLoaderNewTest3(salesitm,0);
                    }else if(paytransaction1.size()>0){
                        PhpLoaderTest11(paytransaction1,0);
                    }else if(createleave.size()>0){
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU9");
//                        Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                        BlockedCards1();
                    }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serversaleitem="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void PhpLoaderNewTest2(final List<Item_Sale> itsale, final int i) {

        Item_Sale usp1=itsale.get(i);

        String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
                "   total:   " + usp1.gettot_amount() +
                "  Server timestamp  " + usp1.getserver_timestamp() +
                "  SaleTransID " + usp1.getsale_trans_id();
        System.out.println("CHILLAR secact Refund Sales ITEMSALE:" + testpermission);


        String URL = Constants.APP_URL+"c_item_sale_transaction_refund.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+usp1.gettransaction_id()+"&bill_no="+usp1.getbill_no()+"&total_amount="+usp1.gettot_amount();

        final String id=usp1.gettransaction_id();

        String  tag_string_req = "itemsalenew";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("Urlsuccesstransaction " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);
                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);

                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoadernew2  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updateitemssRefund2(id);
//                            db.close();
                        }else{
                            ++flag13;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    //                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_successtransaction "+response);

                    int count=i;
                    ++count;

                    if(db.getAllitemsaletoUpNew().size()>0){

                        PhpLoaderNewTest2(itsale ,count);

                    }else if(refunds.size()>0){
                        PhpLoaderTest9(refunds,0);
                    }else if(online.size()>0){
                        PhpLoaderTest10(online,0);
                    }else if(salesitm.size()>0){
                        PhpLoaderNewTest3(salesitm,0);
                    }else if(paytransaction1.size()>0){
                        PhpLoaderTest11(paytransaction1,0);
                    }else if(createleave.size()>0){
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU10");
//                        Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                        BlockedCards1();
                    }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serversuccessnew="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void PhpLoaderTest9(final List<Refund> refunds, final int i) {

        Refund slesitm =refunds.get(i);

        String testsalesitem = "Refund Id : " + slesitm.getId() + " TransID : " + slesitm.getTrans_id() + " Original TransID: " + slesitm.getOrig_trans_id() +" Servertimestamp : " + slesitm.getserver_timestamp();


        System.out.println("CHILLAR RefundTAble:" + testsalesitem);
        final String URL = Constants.APP_URL+"c_refund_transaction.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+slesitm.getTrans_id()+"&refund_transaction_id="+slesitm.getOrig_trans_id()+"&amount="+slesitm.getAmount();
        final String id=slesitm.getTrans_id();
        String  tag_string_req = "refundtable";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLREFUND " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:REFUND " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);

                try {

                    JSONObject jsobj;
                    jsobj = new JSONObject(response);
                    JSONObject jsonObject=jsobj.getJSONObject("status");
                    String server_respose=jsonObject.getString("code");

                    System.out.println("PhpLoader9  code "+server_respose);
                    if (server_respose.equals("success"))
                    {
                        db.updaterefund(id);
//                        db.close();
                    }else{
                        ++flag10;
                        Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);



                int count=i;
                ++count;

                if(db.getAllRefundtoUp().size()>0){

                    PhpLoaderTest9(refunds ,count);

                }else if(online.size()>0){
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU11");
//                    Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.VISIBLE);
                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                serversaleitem="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void PhpLoaderTest10(final List<Online_Recharge> online1, final int i) {

        final Online_Recharge online =online1.get(i);

        String testsalesitem = "Online Id : " + online.getId() + " TransID : " + online.getTransID() + " Original TransID: " + online.getOnlineTransID() +" Servertimestamp : " + online.getServerTimestamp();


        System.out.println("CHILLAR OnlineTAble:" + testsalesitem);
        String URL = Constants.APP_URL+"c_online_to_recharge.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&online_transaction_id="+online.getOnlineTransID()+"&transaction_id="+online.getTransID();
        final String id=online.getTransID();
        String  tag_string_req = "onlineTable";

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLONLINE " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:ONLINE " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);

                try {

                    JSONObject jsobj;
                    jsobj = new JSONObject(response);
                    JSONObject jsonObject=jsobj.getJSONObject("status");
                    String server_respose=jsonObject.getString("code");

                    System.out.println("PhpLoader10  code "+server_respose);
                    if (server_respose.equals("success"))
                    {
                        db.updateOnline(id);
//                        db.close();
                    }else{
                        ++flag11;
                        Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);


                int count=i;
                ++count;

                if(db.getAllOnlineToUp().size()>0){

                    PhpLoaderTest10(online1 ,count);

                }else if(salesitm.size()>0){
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU12");
//                    Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.VISIBLE);
                    BlockedCards1();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);
                serversaleitem="error";
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    private void PhpLoaderNewTest3(final List<Sales_Item> salesitm, final int i) {

        Sales_Item slesitm=salesitm.get(i);

        String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
                    + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


            System.out.println("CHILLAR secact  Refund Sales ITEM:" + testsalesitem);
            String URL = Constants.APP_URL+"c_sales_item_list_refund.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+slesitm.getsales_trans_id()+"&item_id="+slesitm.getitem_id()+"&amount="+slesitm.getamount()+"&item_quantity="+slesitm.getitem_quantity();

            String  tag_string_req = "salesitemnew";
            final String id=slesitm.getsales_trans_id();

            // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
            System.out.println("Urlsuccesstransaction " + URL);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //                Log.d(TAG, response.toString());
                    System.out.println("CODMOB:successtransaction " + response);
                    ++statusCount;
                    progressBar.setProgress(statusCount);

                        try {

                            JSONObject jsobj;
                            jsobj = new JSONObject(response);


                            JSONObject jsonObject=jsobj.getJSONObject("status");
                            String server_respose=jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                            System.out.println("PhpLoadernew3  code "+server_respose);
                            if (server_respose.equals("success"))
                            {
                                db.updatesaleRefund2(id);
//                            db.close();
                            }else{
                                ++flag16;
                                Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                        //                     progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse_successtransaction "+response);


                        int count=i;
                        ++count;

                        if(db.getAllsaletoUpNew().size()>0){

                            PhpLoaderNewTest3(salesitm ,count);

                        }if(paytransaction1.size()>0){
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU13");
//                            Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                            BlockedCards1();
                        }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    serversaleitemnew="error";
                    Constants.AsyncFlag=1;

                    progressBar.setVisibility(View.GONE);
                    progressBar1.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);

                    carddetails.setVisibility(View.VISIBLE);
                    update.setVisibility(View.VISIBLE);
                    app_restart.setVisibility(View.VISIBLE);
                    disp_tables.setVisibility(View.VISIBLE);
                    tables_to_update.setVisibility(View.VISIBLE);
                    apk_update.setVisibility(View.VISIBLE);
                    parentcard.setVisibility(View.VISIBLE);
                    dl_parent.setVisibility(View.VISIBLE);
                    app_exit.setVisibility(View.VISIBLE);
                    System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                    Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                }
            });

            // Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        }

    private void PhpLoaderTest11(final List<Payment_Transaction> paytransaction1, final int i) {

        Payment_Transaction slesitm=paytransaction1.get(i);


        String testsalesitem = "PayID : " + slesitm.getId() + " TransId : " + slesitm.gettrans_id() +
                "BillNo : " + slesitm.getbillno() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


        System.out.println("CHILLAR secact  Refund Payment" + testsalesitem);
        String URL = Constants.APP_URL+"c_payment_transaction_refund.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&transaction_id="+slesitm.gettrans_id()+"&bill_no="+slesitm.getbillno()+"&amount="+slesitm.getamount();

        String  tag_string_req = "payrefund";
        final String id=slesitm.gettrans_id();

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLPayRefund " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);


                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoader11  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updatePaymentRefund2(id);
//                            db.close();
                        }else{
                            ++flag14;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    //                     progressBar.setVisibility(View.GONE);
                    System.out.println("json_parse_payrefundtransaction "+response);


                    int count=i;
                    ++count;

                    if(db.getAllpaytransactiontoUpNew().size()>0){

                        PhpLoaderTest11(paytransaction1 ,count);

                    }else if(createleave.size()>0){
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU14");
//                        Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.VISIBLE);
                        BlockedCards1();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serversaleitemnew="error";
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void PhpLoaderTest12(final List<Create_Leave> createleave, final int i) {

        Create_Leave slesitm=createleave.get(i);


        String testsalesitem = "StudUserId : " + slesitm.getStud_userId() + " ParentUserId : " + slesitm.getParent_userId() +" IDD : "+slesitm.getId()+
                "reasonTypeID : " + slesitm.getReasonTypId() + " reason : " + slesitm.getReasonComment() + " dateTime : " + slesitm.getTime_stamp();


        //machineCode=CH0004&user_id=1&student_userID=541&parent_userID=542&reasonTypeID=1&reason=fever&dateTime=2017-03-11

        //campuswalletdev.chillarcards.com/machine_api/api_1.7.9/c_student_leave.php?machineCode=CH0004&user_id=1&student_userID=541&parent_userID=542&reasonTypeID=1&reason=fever&dateTime=2017-03-11


        System.out.println("CHILLAR ParentSym CreateLeave" + testsalesitem);
        String URL="";
        if(slesitm.getReasonComment().equals("")){
            URL = Constants.APP_URL+"c_student_leave.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&student_userID="+slesitm.getStud_userId()+"&parent_userID="+slesitm.getParent_userId()+"&reasonTypeID="+slesitm.getReasonTypId()+"&dateTime="+slesitm.getTime_stamp();

        }else{
            URL = Constants.APP_URL+"c_student_leave.php?machineCode="+schoomachcode+"&user_id="+useridinteger+"&student_userID="+slesitm.getStud_userId()+"&parent_userID="+slesitm.getParent_userId()+"&reasonTypeID="+slesitm.getReasonTypId()+"&reason="+slesitm.getReasonComment().replace("\n", "%0A").replace(" ","%20")+"&dateTime="+slesitm.getTime_stamp();

        }


        String  tag_string_req = "create_leave";
        final String id= String.valueOf(slesitm.getId());

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URLCreateLeave " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //                Log.d(TAG, response.toString());
                System.out.println("CODMOB:successtransaction " + response);
                ++statusCount;
                progressBar.setProgress(statusCount);


                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);


                        JSONObject jsonObject=jsobj.getJSONObject("status");
                        String server_respose=jsonObject.getString("code");

//                       serversuccess=jsobj.getString("serverUpdateTime");

                        System.out.println("PhpLoader11  code "+server_respose);
                        if (server_respose.equals("success"))
                        {
                            db.updateCreateLeave(id);
//                            db.close();
                        }else{
                            ++flag15;
                            Toast.makeText(DatabaseActivity.this, id, Toast.LENGTH_LONG).show();
                        }






                    } catch (Exception e) {

                        e.printStackTrace();

                    }


                    System.out.println("json_parse_payrefundtransaction "+response);

                    int count=i;
                    ++count;

                    if(db.getAllCreateLeaveToUp().size()>0){

                        PhpLoaderTest12(createleave ,count);

                    }else{

                        if(db.getAllsuccesstoup().size()>0||
                                db.getAllsxstoupNew().size()>0||
                                db.getAllitemsaletoUp().size()>0||
                                db.getAllitemsaletoUpNew().size()>0 ||
                                db.getAllsaletoUp().size()>0||
                                db.getAllsaletoUpNew().size()>0||
                                db.getAlllibtoUp().size()>0||
                                db.getAllAttendancedatatoUP().size()>0 ||
                                db.getAllteacherAttendancedatatoUP().size()>0||
                                db.getAllrechtoUp().size()>0||
                                db.getAllfeetranstoUp().size()>0||
                                db.getAllpaytransactiontoUp().size()>0||
                                db.getAllpaytransactiontoUpNew().size()>0||
                                db.getAllOnlineToUp().size()>0 ||
                                db.getAllRefundtoUp().size()>0 ||
                                db.getAllCreateLeaveToUp().size()>0){

                            progressBar.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);


                            carddetails.setVisibility(View.GONE);
                            update.setVisibility(View.GONE);
                            serverup.setVisibility(View.VISIBLE);
                            app_restart.setVisibility(View.GONE);
                            disp_tables.setVisibility(View.GONE);
                            tables_to_update.setVisibility(View.GONE);
                            apk_update.setVisibility(View.GONE);
                            parentcard.setVisibility(View.GONE);
                            dl_parent.setVisibility(View.GONE);
                            app_exit.setVisibility(View.GONE);


                            Constants.AsyncFlag=3;
                            Toast.makeText(getApplicationContext(),"Not uploaded completely. Please Try Again.",Toast.LENGTH_LONG).show();

                        }else{

                            progressBar1.setVisibility(View.VISIBLE);

                          BlockedCards1();


                        }


                    }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serversaleitemnew="error";
                Constants.AsyncFlag=1;

                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                carddetails.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                app_restart.setVisibility(View.VISIBLE);
                disp_tables.setVisibility(View.VISIBLE);
                tables_to_update.setVisibility(View.VISIBLE);
                apk_update.setVisibility(View.VISIBLE);
                parentcard.setVisibility(View.VISIBLE);
                dl_parent.setVisibility(View.VISIBLE);
                app_exit.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred in Item Stock. please try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/

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


    //DEVICE INFO.......................................................................................................................

/*
    private void DeviceInfo() {
        try {
            String tag_string_req = "deviceinfo";

//        String url = Constants.APP_URL+"r_device_info.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&machineID="+machineID;
            String url = Constants.APP_URL + "r_device_data.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&machineID=" + machineID + "&machineSerialNo=" + machineSL;

            System.out.println("UrlNotice " + url);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                    String messge = "success";
                    System.out.println("CODMOB:Addmessages " + response);
                    try {
                        JSONObject jobjmsg = new JSONObject(response);

                        JSONObject jsonObject = jobjmsg.getJSONObject("status");
                        System.out.println("message output" + messge);
                        messge = jsonObject.getString("message");
                        System.out.println("message output" + messge);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // progressDialog.dismiss();
                    if (messge.equalsIgnoreCase("Authentication failed.")) {
//                    progressBar.setVisibility(View.GONE);


                        Toast.makeText(getApplicationContext(), "Error!" + messge, Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteDeviceinfo();
                        parseJSON(response);
                        try {
                            UserDetails();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            UserDetails();
                        }
                        initTXTV.setText("DeviceINFO");

                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
//                Intent ine=new Intent(MainActivity.this,LogIn.class);
//                startActivity(ine);
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in DeviceInfo. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }catch(Exception e){
            DeviceInfo();
        }
    }
    private void parseJSON(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                machineserial=jsobj.getString("machineSerialNo");
                schooid=jsobj.getString("schoolID");

                schoomachcode=jsobj.getString("schoolMachineCode");

                schoolname=jsobj.getString("schoolName");
                schoolpalce=jsobj.getString("place");
                lasttransid=jsobj.getString("lastTransactionID");



                System.out.println("CODMOB: serial "+machineserial+schooid+schoomachcode+schoolname+schoolpalce+lasttransid);
                SharedPreferences.Editor editor1 = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
                editor1.putString("schoolId",schooid);
                editor1.putString("schlMachCode",schoomachcode);
                editor1.putString("machineserial",machineserial);
                editor1.putString("schoolname",schoolname);
                editor1.putString("schoolplace",schoolpalce);
                editor1.putString("lasttransid",lasttransid);
                editor1.commit();





                Device_Info device_info=new Device_Info(schoomachcode,machineserial,schooid,schoolname,schoolpalce,lasttransid,"","",getcurrentdate());

                db.addDevInfo(device_info);

//                List<Device_Info> device_infos = db.getAlldevInfo();
////
//                for(Device_Info us: device_infos){
//                    String test= "machineid: "+us.getMachine_id()+" serialno : "+us.getSerial_no()+" school : "+us.getSchool_id()+" mainurl : "+us.getMain_server_url()
//                            +" mainupload : "+us.getMain_upload_path()+" schoolname : "+us.getSchoolname()+
//                            " schoolplace : "+us.getSchoolplace()+" lasttransid : "+us.getLasttransid();
//                    System.out.println("CODMOB: "+test );
//
//                }




            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

*/

    public void drawerArrow() {
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.white));
        imageView.setImageDrawable(drawerArrowDrawable);

        offset = 0;
        flipped = false;
        drawerArrowDrawable.setFlip(flipped);
        drawerArrowDrawable.setParameter(offset);

        offset = 1;
        flipped = true;
        drawerArrowDrawable.setFlip(flipped);
        drawerArrowDrawable.setParameter(offset);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }


/*
    //USERDETAILS...........................................................................................................

    private void UserDetails() {
        try {
            String tag_string_req = "userdetails";

            String url = Constants.APP_URL + "r_user.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
            System.out.println("UrlNotice " + url);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                    System.out.println("CODMOB:UserDetailsResp " + response);
                    // progressDialog.dismiss();
                    if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllUser();
                        parseJSONUser(response);
                        initTXTV.setText("USERDETAILS");

                        try {
                            TransType();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            TransType();
                        }


                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in User Details. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch(Exception e){
            UserDetails();
        }

    }
    private void parseJSONUser(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int uid= Integer.parseInt(jsobj.getString("user_id"));
                String uname=jsobj.getString("user_name");
                String password=jsobj.getString("password");

                System.out.println("CODMOB: serial "+uid+uname+password);



                try {

//                    Toast.makeText(getApplicationContext(), "user id "+uid+" username "+uname+" password"+password, Toast.LENGTH_LONG).show();



                    User userdetails=new User(uid,uname,password);

                    db.addUser(userdetails);



//                    Device_Info device_info=new Device_Info()

                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }
            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    //TRANSACTION TYPE........................................................................................................

    private void TransType() {
        try {
            String tag_string_req = "transtype";

            String url = Constants.APP_URL + "r_transaction_types.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deletetranstype();
                        parseJSONTransType(response);
                        initTXTV.setText("TRANSACTIONTYPE");
                        try {
                            PermisData();
                        } catch (IllegalStateException e) {
                            PermisData();
                        }

                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in Transaction Type. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }catch(Exception e){
            TransType();
        }

    }

    private void parseJSONTransType(String response) {

        System.out.println("CODMOB : resp "+ response);

        try {


            JSONArray jsonArray=new JSONArray(response);

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: obj "+jsonObject);

                String transtypId=jsonObject.getString("trans_type_id");
                String transtypeName=jsonObject.getString("transacton_type_name");

                int transId= Integer.parseInt(transtypId);

                System.out.println("CODMOB:-- "+transId);


                TransactionType transactionType=new TransactionType(transId,transtypeName);

                db.addTransaction(transactionType);


//                List<TransactionType> device_infos = db.getAlltrancttype();
////
//                for(TransactionType us: device_infos){
//                    String test="value of i "+i+ "Trans ID : "+us.getTrans_type_id()+" Trans Type Nme : "+us.getTransaction_type_name();
//
//                    System.out.println("CODMOB: Transaction  "+test);
//                }



            }

//            List<TransactionType> device_infos = db.getAlltrancttype();
////
//            System.out.println("CODMOB: Transaction size "+device_infos.size());
//            for(TransactionType us: device_infos){
//                String test= "Trans ID : "+us.getTrans_type_id()+" Trans Type Nme : "+us.getTransaction_type_name();
//
//                System.out.println("CODMOB: Transaction 01 "+test);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

//PERMISSION DATA.......................................................................................................

    private void PermisData() {

        try {

            String tag_string_req = "permisdata";

            String url = Constants.APP_URL + "r_permission_data.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllUserpermission();
                        parseJSONPermis(response);
                        initTXTV.setText("PERMISSION DATA");

                        try {
                            AttendanceType();
                        } catch (IllegalStateException e) {
                            AttendanceType();
                        }


                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in PermissionData. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }catch (Exception e){
            PermisData();
        }
    }

    private void parseJSONPermis(String response) {


        System.out.println("CODMOB:ELDHO -"+ response);
        try {


            JSONArray jsonArray=new JSONArray(response);

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: obj "+jsonObject);

                int permissId= Integer.parseInt(jsonObject.getString("permission_id"));
                int transtypeId= Integer.parseInt(jsonObject.getString("transacton_type_id"));
                int userID= Integer.parseInt(jsonObject.getString("userID"));
                int permission= Integer.parseInt(jsonObject.getString("permission"));


                System.out.println("CODMOB:-- "+permissId);


                User_Permission_Data transactionType=new User_Permission_Data(permissId,transtypeId,userID,permission);

                db.addUserpermission(transactionType);


                db.close();


//                List<User_Permission_Data> device_infos = db.getAllUserspermission();
////
//                for(User_Permission_Data us: device_infos){
//                    String test= "permissionid : "+us.getPermission_id()+" TransTypeId : "+us.getTransaction_type_id()+" userID : "+us.getuser_id()+" permission : "+us.getPermission();
//
//                    System.out.println("ELDHO"+test);
//
//
//
//                }
//
//                List<TransactionType> table = db.getAlltrancttype();
////
//                System.out.println("CODMOB: Transaction size 1 "+table.size());
//                for(TransactionType ter: table){
//                    String test= "Trans ID : "+ter.getTrans_type_id()+" Trans Type Nme : "+ter.getTransaction_type_name();
//
//                    System.out.println("CODMOB: Transaction 1  "+test);
//                }


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //Attendance type..................................................................................................................

    private void AttendanceType() {
        try {
            String tag_string_req = "list_announce";

            String url = Constants.APP_URL + "r_attendance_type.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllAttendance();
                        parseJSONAttndance(response);
                        initTXTV.setText("ATTENDENCE TYPE");
                        try {
                            PaymentType();
                        } catch (IllegalStateException e) {
                            PaymentType();
                        }

                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in Attendance Type" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in Attendance Type. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch (Exception e){
            AttendanceType();
        }

    }
    private void parseJSONAttndance(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int attendanceid=jsobj.getInt("id");
                String attndanctypename=jsobj.getString("attendance_type_name");
//                String password=jsobj.getString("password");



                System.out.println("CODMOB: serial "+attendanceid+attndanctypename);




                try {

//                    Toast.makeText(getApplicationContext(), " id "+attendanceid+" atttendance type name "+attndanctypename, Toast.LENGTH_LONG).show();


                    Attendance_Type atttndanncedetails=new Attendance_Type(attendanceid,attndanctypename);

                    db.addUserAttendance(atttndanncedetails);




                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }
            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }


    //payment type.................................................................................................................

    private void PaymentType() {
        try {
            String tag_string_req = "list_announce";

            String url = Constants.APP_URL + "r_payment_type.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllpaymentType();
                        parseJSONPAymentType(response);
                        initTXTV.setText("PAYMENT TYPE");
                        try {
                            ItemType();
                        } catch (IllegalStateException e) {
                            ItemType();
                        }


                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in Payment Type. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch (Exception e){
            PaymentType();
        }

    }
    private void parseJSONPAymentType(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String paymentId=jsobj.getString("id");
                String paymenttype=jsobj.getString("payment_type_name");
//                String password=jsobj.getString("password");



                System.out.println("CODMOB: serial "+paymentId+paymenttype);


                Payment_Type payment_type=new Payment_Type(paymentId,paymenttype);

                db.addpaytype(payment_type);


            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }



    //item type.........................................................................................................................

    private void ItemType() {
        try {
            String tag_string_req = "list_announce";

            String url = Constants.APP_URL + "r_item_type.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
            System.out.println("UrlNotice " + url);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                    System.out.println("CODMOB:Addmessages1 " + response);
                    // progressDialog.dismiss();
                    if (response.equalsIgnoreCase("NetworkError")) {
//                    progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("CODMOB: ItemType Success  ");
                        db.deleteAllitemtype();
                        parseJSONItemType(response);
                        initTXTV.setText("ITEM TYPE");
                        try {
                            CategorList();
                        } catch (IllegalStateException e) {
                            CategorList();
                        }

                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in ItemType. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch (Exception e){
            ItemType();
        }

    }
    private void parseJSONItemType(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int id=jsobj.getInt("id");
                String itemname=jsobj.getString("item_type_name");
//                String password=jsobj.getString("password");



                System.out.println("CODMOB: serial "+id+itemname);




                try {

//                    Toast.makeText(getApplicationContext(), " id "+id+" item name "+itemname, Toast.LENGTH_LONG).show();


                    Item_Type itemtyp=new Item_Type(id,itemname);

                    db.addNewtype(itemtyp);



//                    Device_Info device_info=new Device_Info()

                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }
            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }



    //CATEGORY LIST........................................................................................................

    private void CategorList() {
        try {
            String tag_string_req = "categorylist";

            String url = Constants.APP_URL + "r_category_list.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllcat();
                        parseJSONCategor(response);
                        initTXTV.setText("CATEGORY LIST");
                        try {
                            Itemlist();
                        } catch (IllegalStateException e) {
                            Itemlist();
                        }

                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in CategoryList. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);

            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch(Exception e){
            CategorList();
        }

    }

    private void parseJSONCategor(String response) {

        System.out.println("CODMOB:parseJSONCategor "+ response);

        try {


            JSONArray jsonArray=new JSONArray(response);

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: obj "+jsonObject);

                int categoryId= Integer.parseInt(jsonObject.getString("category_id"));
                int typeid= Integer.parseInt(jsonObject.getString("item_type_id"));
                String categorname= jsonObject.getString("category_name");
                String shortname= jsonObject.getString("category_shortname");



                System.out.println("CODMOB:categoryId-- "+categoryId);


                CategoryList transactionType=new CategoryList(categoryId,typeid,categorname,shortname);

                db.addcateg(transactionType);


//                List<CategoryList> device_infos = db.getAllcat();
////
//                for(CategoryList us: device_infos){
//                    String test= "Itemtypeid : "+us.getItem_type_id()+" CategorName : "+us.getcategory_name()+" shortname : "+us.getcategory_shortname();
////                    System.out.println("CODMOB: "+test );
//
//                    System.out.println("Category  "+test);
//                }



            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



    //ITEM LIST.........................................................................................................................

    private void Itemlist() {

        try {
            String tag_string_req = "list_announce";

            String url = Constants.APP_URL + "r_item_list.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllitem();
                        parseJSONItemlist(response);
                        initTXTV.setText("ITEM LIST");

                        try {
                            Item_stock();
                        } catch (IllegalStateException e) {
                            Item_stock();
                        }

                        // progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
                    System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                    Toast.makeText(getApplicationContext(), "some error occurred in Item List. please try again", Toast.LENGTH_SHORT).show();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }catch(Exception e){
            Itemlist();
        }

    }
    private void parseJSONItemlist(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int itemId=jsobj.getInt("item_id");
                String itemCode=jsobj.getString("item_code");
                String itemName=jsobj.getString("item_name");
                String itemShortname=jsobj.getString("item_shortname");
                int categoryId=jsobj.getInt("category_id");
                Float price= Float.valueOf(jsobj.getString("price"));
                String stockStatus=jsobj.getString("stock_status");
                int item_type_id= Integer.parseInt(jsobj.getString("item_type_id"));



                System.out.println("CODMOB: serial "+itemId+itemCode+itemName+itemShortname+categoryId+price+stockStatus+item_type_id);





                try {

//                    Toast.makeText(getApplicationContext(), itemId+itemCode+itemName+itemShortname+categoryId+price+stockStatus, Toast.LENGTH_LONG).show();


                    ItemList itemlist=new ItemList(itemId,itemCode,itemName,itemShortname,categoryId,price,stockStatus,item_type_id);

                    db.addNewItem(itemlist);


//                    Device_Info device_info=new Device_Info()

                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }
            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }


    //ITEM STOCK.......................................................................................................


    private void Item_stock() {

        try {
            String tag_string_req = "itemstock";

            String url = Constants.APP_URL + "r_item_stock.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllItems();
                        parseJSONItemStock(response);
                        initTXTV.setText("ITEM STOCK");
                        try {
                            TeacherDeatails();
                        } catch (IllegalStateException e) {

                            TeacherDeatails();
                        }

//                     progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
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
        }catch (Exception e){
            Item_stock();
        }

    }

    private void parseJSONItemStock(String response) {
        System.out.println("CODMOB:parseJSONItemStock "+ response);

        try {


            JSONArray jsonArray=new JSONArray(response);

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: "+jsonObject);

                int stock_id= Integer.parseInt(jsonObject.getString("stock_id"));
                int item_id=  Integer.parseInt(jsonObject.getString("item_id"));
                int item_stock= Integer.parseInt(jsonObject.getString("item_stock"));
                String reorder=jsonObject.getString("reorder_warning");



                System.out.println("CODMOB:-- "+item_id);


                ItemStock transactionType=new ItemStock(stock_id,item_id,item_stock,reorder);

                db.addItem(transactionType);

//                List<ItemStock> device_infos = db.getAllItems();
////
//                for(ItemStock us: device_infos){
//                    String test= "Stockid : "+us.getStock_id()+" itemId : "+us.getItem_id()+" Stock : "+us.getItem_stock()+" Reorder warning "+us.getReorder_warning();
////                    System.out.println("CODMOB: "+test );
//
//                    System.out.println("CODMOB: item Stock  "+test);
//                }



            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
*/


//BLOCKED CARDS..................................................................................

//
//    private void BlockedCards() {
//
//        try {
//
//            String tag_string_req = "list_announce";
//
//            String lastUpdated = db.getLastBlockCardsupdate();
//
//            String url="";
//
//            if(lastUpdated.equals("")){
//
//                url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20");
//
//            }else{
//
//                url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20")+"&blockFromDate="+lastUpdated.replace(" ","%20");
//
//            }
//
//            System.out.println("UrlNotice " + url);
//
//            StringRequest strReq = new StringRequest(Request.Method.POST,
//                    url, new Response.Listener<String>() {
//
//                @Override
//                public void onResponse(String response) {
////                Log.d(TAG, response.toString());
//                    System.out.println("CODMOB:Addmessages " + response);
//                    // progressDialog.dismiss();
//                    if (response.equalsIgnoreCase("NetworkError")) {
////                    progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
//                    } else {
//
////                        db.deleteBlockedInfo();
//                        parseJSONBlockedcards(response);
//
//
//
//
//                        // progressBar.setVisibility(View.GONE);
//                        System.out.println("json_parse " + response);
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
////                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                    //   progressDialog.dismiss();
//                    System.out.println("CODMOB: Error in Blockedcards" + error.getMessage());
//
//
//                    Toast.makeText(getApplicationContext(), "Some error occurred in Blocked cards info. please try again", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//// Adding request to request queue
//            App.getInstance().addToRequestQueue(strReq, tag_string_req);
//            strReq.setRetryPolicy(new DefaultRetryPolicy(
//                    2500,
//                    3,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        }catch(Exception e){
//            BlockedCards();
//        }
//
//    }
//
//    private void parseJSONBlockedcards(String json) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//
//
//            SQLiteDatabase dh=db.getWritableDatabase();
//
//            try {
//
//                dh.beginTransaction();
//
//                for (int i = 0; i < jsar.length(); i++) {
//
//                    String s = jsar.getString(i);
//                    JSONObject jsobj;
//                    jsobj = new JSONObject(s);
//
//                    int blockedcardsId = jsobj.getInt("blocked_cards_id");
//                    String cardSerial = jsobj.getString("card_serial");
//
//
//                    System.out.println("CODMOB: serial " + blockedcardsId + cardSerial);
//
//
//                    try {
//
//
//                        Blocked_Cards_Info blockcards = new Blocked_Cards_Info(blockedcardsId, cardSerial);
//
//                        db.addBlockCardInfo(blockcards);
//
//                    } catch (Exception e) // no guruji
//                    {
//
//                        e.printStackTrace();
//
//                    }
//                }
//
//                dh.setTransactionSuccessful();
//
//            }finally {
//
//                dh.endTransaction();
//
//                int count=db.updateLastBlockCard(schoomachcode);
//
//                System.out.println("Count1: "+count);
//
//                pbar.setVisibility(View.GONE);
//                initTXTV.setText("BLOCKEDCARDS");
//                Toast.makeText(getApplicationContext(), "SUCCESSFULLY RE INITIALIZED", Toast.LENGTH_SHORT).show();
//                dlg.cancel();
//
//
//
//
//
//                Intent i = new Intent(getApplicationContext(), LogIn.class);
////                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                finish();
//                SharedPreferences.Editor editor = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
//                editor.remove("username");
//                editor.remove("password");
//                editor.remove("userid");
//                editor.commit();
//
//
//                System.out.println("CHILLAR CAlling intent");
//
//
//            }
//
//
//
//
//
//        } catch (Exception e) {
//
//            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//
//        }
//    }
//
//
//
//    private void BlockedCards1() {
//
//        try {
//
//            String tag_string_req = "list_announce";
//
//            String lastUpdated = db.getLastBlockCardsupdate();
//
//            String url="";
//
//            if(lastUpdated.equals("")){
//
//                 url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20");
//
//            }else{
//
//                url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20")+"&blockFromDate="+lastUpdated.replace(" ","%20");
//
//            }
//
//            System.out.println("UrlNotice " + url);
//
//            StringRequest strReq = new StringRequest(Request.Method.POST,
//                    url, new Response.Listener<String>() {
//
//                @Override
//                public void onResponse(String response) {
////                Log.d(TAG, response.toString());
//                    System.out.println("CODMOB:Addmessages " + response);
//                    // progressDialog.dismiss();
//                    if (response.equalsIgnoreCase("NetworkError")) {
////                    progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
//                    } else {
//
////                        db.deleteBlockedInfo();
//                        parseJSONBlockedcards1(response);
//
//
//
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
////                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                    //   progressDialog.dismiss();
//                    System.out.println("CODMOB: Error in Blockedcards" + error.getMessage());
//
//                    Constants.AsyncFlag=1;
//
//                    progressBar.setVisibility(View.GONE);
//                    progressBar1.setVisibility(View.GONE);
//                    linearLayout.setVisibility(View.VISIBLE);
//
//                    carddetails.setVisibility(View.VISIBLE);
//                    update.setVisibility(View.VISIBLE);
//                    app_restart.setVisibility(View.VISIBLE);
//                    disp_tables.setVisibility(View.VISIBLE);
//                    tables_to_update.setVisibility(View.VISIBLE);
//                    apk_update.setVisibility(View.VISIBLE);
//                    parentcard.setVisibility(View.VISIBLE);
//                    dl_parent.setVisibility(View.VISIBLE);
//                    app_exit.setVisibility(View.VISIBLE);
//
//
//                    Toast.makeText(getApplicationContext(), "Some error occurred in Blocked cards info. please try again", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//// Adding request to request queue
//            App.getInstance().addToRequestQueue(strReq, tag_string_req);
//            strReq.setRetryPolicy(new DefaultRetryPolicy(
//                    2500,
//                    3,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        }catch(Exception e){
//            BlockedCards();
//        }
//
//    }
//
//    private void parseJSONBlockedcards1(String json) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//
//
//            SQLiteDatabase dh=db.getWritableDatabase();
//
//            try {
//
//                dh.beginTransaction();
//
//                for (int i = 0; i < jsar.length(); i++) {
//
//                    String s = jsar.getString(i);
//                    JSONObject jsobj;
//                    jsobj = new JSONObject(s);
//
//                    int blockedcardsId = jsobj.getInt("blocked_cards_id");
//                    String cardSerial = jsobj.getString("card_serial");
//
//
//                    System.out.println("CODMOB: serial " + blockedcardsId + cardSerial);
//
//
//                    try {
//
//
//                        Blocked_Cards_Info blockcards = new Blocked_Cards_Info(blockedcardsId, cardSerial);
//
//                        db.addBlockCardInfo(blockcards);
//
//                    } catch (Exception e) // no guruji
//                    {
//
//                        e.printStackTrace();
//
//                    }
//                }
//
//                dh.setTransactionSuccessful();
//
//            }finally {
//
//                dh.endTransaction();
//
//                int count=db.updateLastBlockCard(schoomachcode);
//
//                System.out.println("Count: "+count);
//
//                Constants.AsyncFlag=1;
//
//                progressBar.setVisibility(View.GONE);
//                progressBar1.setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);
//
//                carddetails.setVisibility(View.VISIBLE);
//                update.setVisibility(View.VISIBLE);
//                app_restart.setVisibility(View.VISIBLE);
//                disp_tables.setVisibility(View.VISIBLE);
//                tables_to_update.setVisibility(View.VISIBLE);
//                apk_update.setVisibility(View.VISIBLE);
//                parentcard.setVisibility(View.VISIBLE);
//                dl_parent.setVisibility(View.VISIBLE);
//                app_exit.setVisibility(View.VISIBLE);
//
//                System.out.println("FU15");
//
//                Toast.makeText(getApplicationContext(), "Fully Uploaded.", Toast.LENGTH_SHORT).show();
//
//
//            }
//
//
//
//
//
//        } catch (Exception e) {
//
//            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//
//        }
//    }
//


//    public Boolean isNetworkConnected1() {
//        try {
//
//
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal==0);
//            return reachable;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return false;
//    }


   /* private class reachableURL extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            if(isURLReachable(getApplicationContext())){

                System.out.println("CHILLERe: Url reachable");
                return "true";

            }else{
                System.out.println("CHILLERe: Url not reachable");
                return "false";
            }

        }

        @Override
        protected void onPostExecute(String s) {


            if(s.equals("true")) {
                try {
                    Constants.AsyncFlag=0;
                    updateurl1();
                } catch (Exception e) {
                    System.out.println("CHILLERe: Exceptions here");
                    e.printStackTrace();
                }
            }else{

                progressBar1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Network not availbale!",Toast.LENGTH_SHORT).show();
            }
        }
    }*/

//    private class reachableURL3 extends AsyncTask<Void, String, String> {
//
//        @Override
//        protected String doInBackground(Void... params) {
//            if(isURLReachable(getApplicationContext())){
//
//                System.out.println("CHILLERe: Url reachable");
//                return "true";
//
//            }else{
//                System.out.println("CHILLERe: Url not reachable");
//                return "false";
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
////            progressBar.setVisibility(View.GONE);
////            linearLayout.setVisibility(View.VISIBLE);
//            if(s.equals("true")) {
//                try {
//                    download1();
//                } catch (Exception e) {
//                    System.out.println("CHILLERe: Exceptions here");
//                    e.printStackTrace();
//                }
//            }else{
//                Toast.makeText(getApplicationContext(),"Network not availbale!",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private class reachableURL2 extends AsyncTask<Void, String, String> {
//
//        @Override
//        protected String doInBackground(Void... params) {
//            if(isURLReachable(getApplicationContext())){
//
//                System.out.println("CHILLERe: Url2 reachable");
//                return "true";
//
//            }else{
//                System.out.println("CHILLERe: Url2 not reachable");
//                return "false";
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//            try {
//                if (s.equals("true")) {
//
//
//                    dlg = new Dialog(DatabaseActivity.this);
//                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dlg.setContentView(R.layout.activity_main);
//                    dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    dlg.setCancelable(true);
//                    dlg.setCanceledOnTouchOutside(false);
//                    dlg.show();
//
////                ProgressBar pbar= (ProgressBar) dlg.findViewById(R.id.progbar);
//                    initTXTV = (TextView) dlg.findViewById(R.id.initID);
//                    pbar = (ProgressBar) dlg.findViewById(R.id.progbar);
//                    pbar.setVisibility(View.VISIBLE);
//
////                    SecondActivity.stoptimertask();
//
//
////                    try {
////                        DeviceInfo();
////                    } catch (IllegalStateException e) {
////                        e.printStackTrace();
////                        DeviceInfo();
////                    }
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Network not availbale!", Toast.LENGTH_SHORT).show();
//                }
//            }catch (Exception e){
//                Toast.makeText(getApplicationContext(), "Please Try Again ", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

    public String getcurrentdate() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciever1);
    }

    @Override
    public void onBackPressed() {


        if (Constants.AsyncFlag == 0 || Constants.AsyncFlag == 3) {

            System.out.println("Flags: " + flag1 + "," + flag2 + "," + flag3 + "," + flag4 + "," + flag5 + "," + flag6 + "," + flag7 + "," + flag8 + "," + flag9 + ","
                    + flag10 + "," + flag11 + "," + flag12 + "," + flag13 + "," + flag14 + "," + flag15 + "," + flag16);

            if (flag1 >= 15 || flag2 >= 15 || flag3 >= 15 || flag4 >= 15 || flag5 >= 15 || flag6 >= 15 || flag7 >= 15 || flag8 >= 15 ||
                    flag9 >= 15 || flag10 >= 15 || flag11 >= 15 || flag12 >= 15 || flag13 >= 15 || flag14 >= 15 || flag15 >= 15 || flag16 >= 15) {

                Constants.AsyncFlag = -1;

                super.onBackPressed();

            } else if (Constants.AsyncFlag == 3) {
                Toast.makeText(this, "Please tap the server update option again.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getApplicationContext(), "Please Wait..Server update in progress", Toast.LENGTH_SHORT).show();
            }
        } else {
            Constants.AsyncFlag = -1;
//            new FieldOffTask().execute();

            super.onBackPressed();
        }

    }


//    private class FieldOnTask extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            try {
//                ret_poweron = SecondActivity.rfidService.rfid_poweron();
//                System.out.println("RFID PowerOn :" + ret_poweron);
//
//                if (ret_poweron == 0 || ret_poweron == 2) {
//
////                Toast.makeText(getApplicationContext(), "Module open success", Toast.LENGTH_SHORT).show();
//
//                    ret_fieldon =SecondActivity.rfidService.rfid_rffield_on();
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
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            progressBar.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
//
//        }
//    }

    class deliverReceiver1 extends BroadcastReceiver {
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
//    private class FieldOffTask extends AsyncTask<Void,Void,Void> {
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
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
////        @Override
////        protected void onPreExecute() {
////
////            progressBar.setVisibility(View.VISIBLE);
////        }
//
//    }


//
//    private void download1() {
//        String  tag_string_req = "download student list";
//
//        String url=Constants.APP_URL+"r_all_students.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
//        System.out.println("Url Call_1 " + url);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                url, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String response) {
////				progressBar.setVisibility(View.GONE);
//                System.out.println("Url Call_1 " + response);
//
//                db.deleteAllStudentlist();
//                parseJSONDownload1(response);
//
//                download2();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("ErrorVolleyTrans " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred"+error, Toast.LENGTH_LONG).show();
//
//            }
//        });
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//// Adding request to request queue
//
//        App.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
//
//    private void parseJSONDownload1(String json) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//
//            for (int i = 0; i < jsar.length(); i++) {
//
//                String s = jsar.getString(i);
//                JSONObject jsobj;
//                jsobj = new JSONObject(s);
//
//                String schoolId=jsobj.getString("schoolID");
//                String studentID=jsobj.getString("student_userID");
//                String cardSerial=jsobj.getString("cardSerial");
//                String standardID=jsobj.getString("standardID");
//                String standardName=jsobj.getString("standardName");
//                String standardDivisionID=jsobj.getString("standardDivisionID");
//                String standardDivisionName=jsobj.getString("standardDivisionName");
////                String password=jsobj.getString("password");
//
//                String student_name=jsobj.getString("studentName");;
//
//
//
////                System.out.println("CODMOB: schoolId "+paymentId+paymenttype);
//
//
//                StudentList studentList=new StudentList(cardSerial,studentID,student_name,standardName,standardDivisionName);
//
//                db.addStudentlist(studentList);
//
//
////                List <StudentList> test1=db.getAllStudents();
////
////                for (StudentList studentList1:test1){
////                    System.out.println("ParentSystem: StdDetails CardSl: "+ studentList1.getStudent_card_serial()+" Name: "+studentList1.getStudent_name()
////                            +" StudId "+studentList1.getUser_id()+ " Class "+studentList1.getStudent_class()+" "+studentList1.getStudent_division());
////                }
//
//
//
//            }
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//    }
//
//
//    private void download2() {
//        String  tag_string_req = "download parent details";
//
//        String url=Constants.APP_URL+"r_all_parents.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
//        System.out.println("Url Call_2 " + url);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                url, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String response) {
////				progressBar.setVisibility(View.GONE);
//                System.out.println("Url Call_1 " + response);
//
//                db.deleteAllParentdetails();
//                parseJSONDownload2(response);
//
//                download3();
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("ErrorVolleyTrans " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred"+error, Toast.LENGTH_LONG).show();
//
//            }
//        });
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//// Adding request to request queue
//
//        App.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
//    private void parseJSONDownload2(String json) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//
//            for (int i = 0; i < jsar.length(); i++) {
//
//                String s = jsar.getString(i);
//                JSONObject jsobj;
//                jsobj = new JSONObject(s);
//
//                String parent_userID=jsobj.getString("parent_userID");
//                String parentName=jsobj.getString("parentName");
//                String cardSerial=jsobj.getString("cardSerial");
////                String standardID=jsobj.getString("standardID");
////                String standardName=jsobj.getString("standardName");
////                String standardDivisionID=jsobj.getString("standardDivisionID");
////                String standardDivisionName=jsobj.getString("standardDivisionName");
////                String password=jsobj.getString("password");
//
////                String student_name="";
//
//
//
////                System.out.println("CODMOB: schoolId "+paymentId+paymenttype);
//
//
//                Parent parent=new Parent(parent_userID,parentName,cardSerial);
//
//                db.addparentDetails(parent);
//
////                List <Parent> test1=db.getAllParents();
////
////                for (Parent studentList1:test1){
////                    System.out.println("ParentSystem: parentDetails CardSl: "+ studentList1.getCard_serial()+" Name: "+studentList1.getParent_name()
////                            +" ParentId "+studentList1.getUser_id());
////                }
//
//
//            }
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//    }
//
//
//    private void download3() {
//        String  tag_string_req = "download parent student";
//
//        String url=Constants.APP_URL+"r_parent_students.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
//        System.out.println("Url Call_3 " + url);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                url, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String response) {
////				progressBar.setVisibility(View.GONE);
//                System.out.println("Url Call_3 " + response);
//
//                db.deleteAllParentStudent();
//                parseJSONDownload3(response);
//
//                download4();
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("ErrorVolleyTrans " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred"+error, Toast.LENGTH_LONG).show();
//
//            }
//        });
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//// Adding request to request queue
//
//        App.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
//    private void parseJSONDownload3(String json) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//
//            for (int i = 0; i < jsar.length(); i++) {
//
//                String s = jsar.getString(i);
//                JSONObject jsobj;
//                jsobj = new JSONObject(s);
//
//                String parent_userID=jsobj.getString("parent_userID");
//                String student_userID=jsobj.getString("student_userID");
////                String cardSerial=jsobj.getString("cardSerial");
////                String standardID=jsobj.getString("standardID");
////                String standardName=jsobj.getString("standardName");
////                String standardDivisionID=jsobj.getString("standardDivisionID");
////                String standardDivisionName=jsobj.getString("standardDivisionName");
//////                String password=jsobj.getString("password");
////
////                String student_name="";
//
//
//
////                System.out.println("CODMOB: schoolId "+paymentId+paymenttype);
//
//
//                Parent_Student studentList=new Parent_Student(student_userID,parent_userID);
//
//                db.addParentStudent(studentList);
//
//
////                List <Parent_Student> test1=db.getAllParentStudent();
////
////                for (Parent_Student studentList1:test1){
////                    System.out.println("ParentSystem: ParentStudent ParntId: "+ studentList1.getParent_user_id()+" StudId: "+studentList1.getStd_user_id());
////                }
//
//
//            }
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//    }
//
//
//    private void download4() {
//        String  tag_string_req = "download reason type";
//
//        String url=Constants.APP_URL+"r_reason_type.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
//        System.out.println("Url Call_3 " + url);
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                url, new Response.Listener<String>() {
//
//
//            @Override
//            public void onResponse(String response) {
////				progressBar.setVisibility(View.GONE);
//                System.out.println("Url Call_3 " + response);
//
//                db.deleteAllReasonType();
//                parseJSONDownload4(response);
//
//
//                preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
//                editor= preferences.edit();
//                editor.putString("download_parent","true");
//                System.out.println("Student list downloaded");
//                editor.commit();
//
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("ErrorVolleyTrans " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "some error occurred"+error, Toast.LENGTH_LONG).show();
//
//            }
//        });
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//// Adding request to request queue
//
//        App.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
//    private void parseJSONDownload4(String json) {
//
//        try {
//
//
//            System.out.println("JSON--" + json);
//            JSONArray jsar = new JSONArray(json);
//            System.out.println("JSRRRRRR--" + jsar);
//
//            for (int i = 0; i < jsar.length(); i++) {
//
//                String s = jsar.getString(i);
//                JSONObject jsobj;
//                jsobj = new JSONObject(s);
//
//                String id=jsobj.getString("id");
//                String reason_type_name=jsobj.getString("reason_type_name");
////                String cardSerial=jsobj.getString("cardSerial");
////                String standardID=jsobj.getString("standardID");
////                String standardName=jsobj.getString("standardName");
////                String standardDivisionID=jsobj.getString("standardDivisionID");
////                String standardDivisionName=jsobj.getString("standardDivisionName");
//////                String password=jsobj.getString("password");
////
////                String student_name="";
//
//
//
////                System.out.println("CODMOB: schoolId "+paymentId+paymenttype);
//
//
//                Reason studentList=new Reason(id,reason_type_name);
//
//                db.addreasonType(studentList);
//
//                List <Reason> test1=db.getAllReason();
//
//                for (Reason studentList1:test1){
//                    System.out.println("ParentSystem: Reason ReasonId: "+ studentList1.getReasonId()+" reasontype: "+studentList1.getReasonType());
//                }
//
//
//            }
//            progressBar.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "Download Completed", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//    }

/*    private void TeacherDeatails() {

        try {


            String tag_string_req = "TeacherDeatails";

            String url = Constants.APP_URL + "r_read_all_teachers.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;
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
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllteacher();
                        parseJSONTeacherDeatails(response);
                        initTXTV.setText("TEACHER DETAILS");
                        try {
                            BlockedCards();
                        } catch (IllegalStateException e) {
                            BlockedCards();
                        }


//                     progressBar.setVisibility(View.GONE);
                        System.out.println("json_parse " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   progressDialog.dismiss();
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

        }
        catch (Exception e)
        {
            TeacherDeatails();
        }
    }
    private void parseJSONTeacherDeatails(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String schoolID=jsobj.getString("schoolID");
                String cardSerial=jsobj.getString("cardSerial");
                String teacher_userID=jsobj.getString("teacher_userID");
                String teacherName=jsobj.getString("teacherName");



                System.out.println("CODMOB: serial "+schoolID+cardSerial);
                System.out.println("CODMOB: teacher_userID "+teacher_userID);
                System.out.println("CODMOB: teacherName "+teacherName);



                try {

//                    Toast.makeText(getApplicationContext(), "id "+blockedcardsId+" cardSerial "+cardSerial, Toast.LENGTH_LONG).show();


                    Teacher_Details teacherdetails=new Teacher_Details(schoolID,cardSerial,teacher_userID,teacherName);

                    db.addteacherDetails(teacherdetails);


                    List<Teacher_Details> teacher = db.getAllteacher();
//
                    for(Teacher_Details us: teacher){
                        String test= "teacher id: "+us.getTeacher_userID()+" serialno : "+us.getCardSerial()+" school id : "+us.getSchoolID()+" teacher name  : "+us.getTeacherName();
                        System.out.println("CODMOB: "+test );

                    }

//                    Device_Info device_info=new Device_Info()

                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }
            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }*/
}
