package com.chillarcards.machinevendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chillarcards.machinevendor.ModelClass.Attendance_Type;
import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.CategoryList;
import com.chillarcards.machinevendor.ModelClass.Device_Info;
import com.chillarcards.machinevendor.ModelClass.ItemList;
import com.chillarcards.machinevendor.ModelClass.ItemStock;
import com.chillarcards.machinevendor.ModelClass.Item_Type;
import com.chillarcards.machinevendor.ModelClass.Payment_Type;
import com.chillarcards.machinevendor.ModelClass.Teacher_Details;
import com.chillarcards.machinevendor.ModelClass.TransactionType;
import com.chillarcards.machinevendor.ModelClass.User;
import com.chillarcards.machinevendor.ModelClass.User_Permission_Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


// INITIALIZING ALL TABLES HERE..............................


public class MainActivity extends AppCompatActivity {

    Button button;
    DatabaseHandler db;

    ProgressBar progressBar;

    TextView initTXTV;
    String machinSerial, schoolId, schoolmachinecode, User_name, Pass_word, machineID, machineSL;

    String schoolname, schoolplace, lasttransid;


    ArrayList<User> userArray = new ArrayList<User>();
    ArrayList<TransactionType> transTypeArray = new ArrayList<TransactionType>();
    ArrayList<User_Permission_Data> userPermArray = new ArrayList<User_Permission_Data>();
    ArrayList<Attendance_Type> attTypeArray = new ArrayList<Attendance_Type>();
    ArrayList<Payment_Type> payTypeArray = new ArrayList<Payment_Type>();
    ArrayList<Item_Type> itemTypeArray = new ArrayList<Item_Type>();
    ArrayList<CategoryList> catListArray = new ArrayList<CategoryList>();
    ArrayList<ItemList> itemList = new ArrayList<ItemList>();
    ArrayList<ItemStock> itemStockArray = new ArrayList<ItemStock>();
    ArrayList<Teacher_Details> tchrDetailsArray = new ArrayList<Teacher_Details>();
    ArrayList<Blocked_Cards_Info> blkCardsArray = new ArrayList<Blocked_Cards_Info>();


    @Override
    public void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DatabaseHandler.getInstance(getApplicationContext());


        progressBar = (ProgressBar) findViewById(R.id.progbar);

        initTXTV = (TextView) findViewById(R.id.initID);
        Bundle bundle = getIntent().getExtras();
        User_name = bundle.getString("usernamebundle");
        Pass_word = bundle.getString("passwordbundle");
        machineID = bundle.getString("machinID");
        machineSL = bundle.getString("machinSL");
        System.out.println("usernamebundle " + machineID);


        DeviceInfo();


    }

    //DEVICE INFO.......................................................................................................................

    private void DeviceInfo() {

        String tag_string_req = "deviceinfo";

        String url = Constants.APP_URL + "r_device_data.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&machineID=" + machineID + "&machineSerialNo=" + machineSL;

        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, response -> {

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

                    if (messge.equalsIgnoreCase("Authentication failed.")) {


                        Toast.makeText(getApplicationContext(), "Error! " + messge, Toast.LENGTH_SHORT).show();

                        Intent ine = new Intent(MainActivity.this, LogIn.class);
                        startActivity(ine);
                        finish();


                    } else {
                        db.deleteDeviceinfo();
                        parseJSON(response);

                        initTXTV.setText("DeviceINFO");

                        System.out.println("json_parse " + response);
                    }
                }, error -> {

            System.out.println("Sher MAin " + error.getMessage());

            Toast.makeText(getApplicationContext(), "Network Error. Please try again"+error.getMessage(), Toast.LENGTH_SHORT).show();

            finish();
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSON(String json) {

        try {

            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);


            String s = jsar.getString(0);
            JSONObject jsobj;
            jsobj = new JSONObject(s);

            machinSerial = jsobj.getString("machineSerialNo");
            schoolId = jsobj.getString("schoolID");

            schoolmachinecode = jsobj.getString("schoolMachineCode");

            schoolname = jsobj.getString("schoolName");
            schoolplace = jsobj.getString("place");
            lasttransid = jsobj.getString("lastTransactionID");


            System.out.println("CODMOB: serial " + machinSerial + schoolId + schoolmachinecode + schoolname + schoolplace + lasttransid);
            SharedPreferences.Editor editor1 = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
            editor1.putString("schoolId", schoolId);
            editor1.putString("schlMachCode", schoolmachinecode);
            editor1.putString("machineserial", machinSerial);
            editor1.putString("schoolname", schoolname);
            editor1.putString("schoolplace", schoolplace);
            editor1.putString("lasttransid", lasttransid);
            editor1.commit();


            Device_Info device_info = new Device_Info(schoolmachinecode, machinSerial, schoolId, schoolname, schoolplace, lasttransid, "", "", getcurrentdate());

            int status = db.addDevInfo(device_info);


            if (status == 0) {

                UserDetails();

            } else {
                finish();

            }


        } catch (Exception e) {

            e.printStackTrace();
            finish();

        }
    }


    //USERDETAILS...........................................................................................................

    private void UserDetails() {

        String tag_string_req = "userdetails";

        String url = Constants.APP_URL + "r_user.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("CODMOB:UserDetailsResp " + response);

                db.deleteAllUser();
                parseJSONUser(response);
                initTXTV.setText("USERDETAILS");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSONUser(String json) {

        try {

            userArray.clear();

            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);


            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int uid = Integer.parseInt(jsobj.getString("user_id"));
                String uname = jsobj.getString("user_name");
                String password = jsobj.getString("password");

                System.out.println("CODMOB: serial " + uid + uname + password);


                try {

                    User userdetails = new User(uid, uname, password);

                    userArray.add(userdetails);


                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }
            }

            //dB handler invoke
            int status = db.addUser(userArray);

            if (status == 0) {

                TransType();

            } else {

                finish();
            }

        } catch (Exception e) {

            e.printStackTrace();
            finish();
        }
    }

    //TRANSACTION TYPE........................................................................................................

    private void TransType() {

        String tag_string_req = "transtype";

        String url = Constants.APP_URL + "r_transaction_types.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, response -> {
                    System.out.println("CODMOB:Addmessages " + response);
                    if (response.equalsIgnoreCase("NetworkError")) {
                        Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                    } else {
                        db.deletetranstype();
                        parseJSONTransType(response);
                        initTXTV.setText("TRANSACTIONTYPE");

                        System.out.println("json_parse " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSONTransType(String response) {

        System.out.println("CODMOB : resp " + response);
        transTypeArray.clear();

        try {

            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: obj " + jsonObject);

                String transtypId = jsonObject.getString("trans_type_id");
                String transtypeName = jsonObject.getString("transacton_type_name");

                int transId = Integer.parseInt(transtypId);

                System.out.println("CODMOB:-- " + transId);


                TransactionType transactionType = new TransactionType(transId, transtypeName);

                transTypeArray.add(transactionType);


            }

            int status = db.addTransaction(transTypeArray);

            if (status == 0) {

                PermisData();

                System.out.println("TEST SUCCESS!");

            } else {

                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();

            finish();
        }


    }

    //PERMISSION DATA.......................................................................................................

    private void PermisData() {

        try {

            String tag_string_req = "permisdata";

            String url = Constants.APP_URL + "r_permission_data.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
            System.out.println("UrlNotice " + url);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    System.out.println("CODMOB:Addmessages " + response);

                    db.deleteAllUserpermission();
                    parseJSONPermis(response);
                    initTXTV.setText("PERMISSION DATA");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                    finish();
                }
            });
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);


        } catch (Exception e) {
            PermisData();
        }
    }

    private void parseJSONPermis(String response) {

        userPermArray.clear();

        System.out.println("CODMOB:ELDHO -" + response);


        try {


            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: obj " + jsonObject);

                int permissId = Integer.parseInt(jsonObject.getString("permission_id"));
                int transtypeId = Integer.parseInt(jsonObject.getString("transacton_type_id"));
                int userID = Integer.parseInt(jsonObject.getString("userID"));
                int permission = Integer.parseInt(jsonObject.getString("permission"));


                System.out.println("CODMOB:-- " + permissId);


                User_Permission_Data transactionType = new User_Permission_Data(permissId, transtypeId, userID, permission);

                userPermArray.add(transactionType);


//                db.close();


            }

            int status = db.addUserpermission(userPermArray);

            if (status == 0) {
                AttendanceType();
            } else {

                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();


            finish();


        }


    }


    //Attendance type..................................................................................................................

    private void AttendanceType() {

        String tag_string_req = "list_announce";

        String url = Constants.APP_URL + "r_attendance_type.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages " + response);

                db.deleteAllAttendance();
                parseJSONAttndance(response);
                initTXTV.setText("ATTENDENCE TYPE");
//                    try {
//                        PaymentType();
//                    } catch (IllegalStateException e) {
//                        PaymentType();
//                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSONAttndance(String json) {

        attTypeArray.clear();

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int attendanceid = jsobj.getInt("id");
                String attndanctypename = jsobj.getString("attendance_type_name");
//                String password=jsobj.getString("password");


                System.out.println("CODMOB: serial " + attendanceid + attndanctypename);


                try {


                    Attendance_Type atttndanncedetails = new Attendance_Type(attendanceid, attndanctypename);

                    attTypeArray.add(atttndanncedetails);


                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }


            }
            int status = db.addUserAttendance(attTypeArray);
            if (status == 0) {
                PaymentType();
            } else {

                finish();
            }


        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();


            finish();


        }
    }


    //payment type.................................................................................................................

    private void PaymentType() {

        String tag_string_req = "list_announce";

        String url = Constants.APP_URL + "r_payment_type.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages " + response);

                db.deleteAllpaymentType();
                parseJSONPAymentType(response);
                initTXTV.setText("PAYMENT TYPE");
//                    try {
//                        ItemType();
//                    } catch (IllegalStateException e) {
//                        ItemType();
//                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSONPAymentType(String json) {


        try {

            payTypeArray.clear();

            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String paymentId = jsobj.getString("id");
                String paymenttype = jsobj.getString("payment_type_name");
//                String password=jsobj.getString("password");


                System.out.println("CODMOB: serial " + paymentId + paymenttype);


                Payment_Type payment_type = new Payment_Type(paymentId, paymenttype);

                payTypeArray.add(payment_type);


            }

            int status = db.addpaytype(payTypeArray);

            if (status == 0) {

                ItemType();

            } else {

                finish();
            }

        } catch (Exception e) {

            e.printStackTrace();

            finish();


        }
    }


    //item type.........................................................................................................................

    private void ItemType() {

        String tag_string_req = "list_announce";

        String url = Constants.APP_URL + "r_item_type.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages1 " + response);
                System.out.println("CODMOB: ItemType Success  ");
                db.deleteAllitemtype();
                parseJSONItemType(response);
                initTXTV.setText("ITEM TYPE");
//                        try {
//                            CategorList();
//                        } catch (IllegalStateException e) {
//                            CategorList();
//                        }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSONItemType(String json) {

        try {

            itemTypeArray.clear();

            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int id = jsobj.getInt("id");
                String itemname = jsobj.getString("item_type_name");


                System.out.println("CODMOB: serial " + id + itemname);


                try {


                    Item_Type itemtyp = new Item_Type(id, itemname);

                    itemTypeArray.add(itemtyp);


                } catch (Exception e) // no guruji
                {

                    e.printStackTrace();

                }
            }

            int status = db.addNewtype(itemTypeArray);
            ;

            if (status == 0) {

                CategorList();

            } else {

                finish();
            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();


            finish();

        }
    }


    //CATEGORY LIST........................................................................................................

    private void CategorList() {

        String tag_string_req = "categorylist";

        String url = Constants.APP_URL + "r_category_list.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages " + response);

                db.deleteAllcat();
                parseJSONCategor(response);
                initTXTV.setText("CATEGORY LIST");
//                        try {
//                            Itemlist();
//                        } catch (IllegalStateException e) {
//                            Itemlist();
//                        }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSONCategor(String response) {

        System.out.println("CODMOB:parseJSONCategor " + response);

        try {
            catListArray.clear();

            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: obj " + jsonObject);

                int categoryId = Integer.parseInt(jsonObject.getString("category_id"));
                int typeid = Integer.parseInt(jsonObject.getString("item_type_id"));
                String categorname = jsonObject.getString("category_name");
                String shortname = jsonObject.getString("category_shortname");


                System.out.println("CODMOB:categoryId-- " + categoryId);


                CategoryList transactionType = new CategoryList(categoryId, typeid, categorname, shortname);

                catListArray.add(transactionType);


            }

            int status = db.addcateg(catListArray);
            if (status == 0) {
                Itemlist();
            } else {

                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();

            finish();
        }


    }


    //ITEM LIST.........................................................................................................................

    private void Itemlist() {

        String tag_string_req = "list_announce";

        String url = Constants.APP_URL + "r_item_list.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages " + response);

                db.deleteAllitem();
                parseJSONItemlist(response);
                initTXTV.setText("ITEM LIST");
//
//                        try {
//                            Item_stock();
//                        } catch (IllegalStateException e) {
//                            Item_stock();
//                        }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void parseJSONItemlist(String json) {

        try {

            itemList.clear();

            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                int itemId = jsobj.getInt("item_id");
                String itemCode = jsobj.getString("item_code");
                String itemName = jsobj.getString("item_name");
                String itemShortname = jsobj.getString("item_shortname");
                int categoryId = jsobj.getInt("category_id");
                Float price = Float.valueOf(jsobj.getString("price"));
                String stockStatus = jsobj.getString("stock_status");
                int item_type_id = Integer.parseInt(jsobj.getString("item_type_id"));


                System.out.println("CODMOB: serial " + itemId + itemCode + itemName + itemShortname + categoryId + price + stockStatus + item_type_id);


                try {


                    ItemList itemlist = new ItemList(itemId, itemCode, itemName, itemShortname, categoryId, price, stockStatus, item_type_id);

                    itemList.add(itemlist);


                } catch (Exception e) // no guruji
                {
                    e.printStackTrace();

                }
            }

            int status = db.addNewItem(itemList);
            if (status == 0) {
                Item_stock();
            } else {

                finish();
            }

        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

            finish();

        }
    }


    //ITEM STOCK.......................................................................................................


    private void Item_stock() {

        String tag_string_req = "itemstock";

        String url = Constants.APP_URL + "r_item_stock.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages " + response);

                db.deleteAllItems();
                parseJSONItemStock(response);
                initTXTV.setText("ITEM STOCK");
//                        try {
//                            TeacherDeatails();
//                        } catch (IllegalStateException e) {
//
//                            TeacherDeatails();
//                        }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);




    }

    private void parseJSONItemStock(String response) {
        System.out.println("CODMOB:parseJSONItemStock " + response);

        try {

            itemStockArray.clear();
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                System.out.println("CODMOB: " + jsonObject);

                int stock_id = Integer.parseInt(jsonObject.getString("stock_id"));
                int item_id = Integer.parseInt(jsonObject.getString("item_id"));
                int item_stock = Integer.parseInt(jsonObject.getString("item_stock"));
                String reorder = jsonObject.getString("reorder_warning");


                System.out.println("CODMOB:-- " + item_id);


                ItemStock transactionType = new ItemStock(stock_id, item_id, item_stock, reorder);
                itemStockArray.add(transactionType);


            }

            int status = db.addItem(itemStockArray);

            if (status == 0) {
                TeacherDeatails();
            } else {

                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();

            finish();
        }


    }


    private void TeacherDeatails() {

        String tag_string_req = "TeacherDeatails";

        String url = Constants.APP_URL + "r_read_all_teachers.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode;
        System.out.println("UrlNotice " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                System.out.println("CODMOB:Addmessages " + response);

                db.deleteAllteacher();
                parseJSONTeacherDeatails(response);
                initTXTV.setText("TEACHER DETAILS");
//                        try {
//                            BlockedCards();
//                        } catch (IllegalStateException e) {
//                            BlockedCards();
//                        }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);




    }

    private void parseJSONTeacherDeatails(String json) {

        try {

            tchrDetailsArray.clear();

            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String schoolID = jsobj.getString("schoolID");
                String cardSerial = jsobj.getString("cardSerial");
                String teacher_userID = jsobj.getString("teacher_userID");
                String teacherName = jsobj.getString("teacherName");


                System.out.println("CODMOB: serial " + schoolID + cardSerial);
                System.out.println("CODMOB: teacher_userID " + teacher_userID);
                System.out.println("CODMOB: teacherName " + teacherName);


                try {

//                    Toast.makeText(getApplicationContext(), "id "+blockedcardsId+" cardSerial "+cardSerial, Toast.LENGTH_LONG).show();


                    Teacher_Details teacherdetails = new Teacher_Details(schoolID, cardSerial, teacher_userID, teacherName);

                    tchrDetailsArray.add(teacherdetails);


                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            int status = db.addteacherDetails(tchrDetailsArray);

            if (status == 0) {
                BlockedCards();

            } else {

                finish();
            }

        } catch (Exception e) {

            e.printStackTrace();


            finish();

        }
    }


//BLOCKED CARDS..................................................................................


    private void BlockedCards() {

        try {

            String tag_string_req = "list_announce";

            String lastUpdated = db.getLastBlockCardsupdate();

            String url = "";

            if (lastUpdated.equals("")) {

                url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode).replace(" ", "%20");

            } else {

                url = Constants.APP_URL + "r_blocked_cards_info.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoolmachinecode).replace(" ", "%20") + "&blockFromDate=" + lastUpdated.replace(" ", "%20");

            }

            System.out.println("UrlNotice " + url);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                    System.out.println("CODMOB:Addmessages " + response);

                    parseJSONBlockedcards(response);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                    finish();
                }
            });

            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);

        } catch (Exception e) {
            BlockedCards();
        }

    }


    public String getcurrentdate() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }


    private void parseJSONBlockedcards(String json) {
        try {


            blkCardsArray.clear();

            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);


            try {


                for (int i = 0; i < jsar.length(); i++) {

                    String s = jsar.getString(i);
                    JSONObject jsobj;
                    jsobj = new JSONObject(s);

                    int blockedcardsId = jsobj.getInt("blocked_cards_id");
                    String cardSerial = jsobj.getString("card_serial");


                    System.out.println("CODMOB: serial " + blockedcardsId + cardSerial);


                    try {


                        Blocked_Cards_Info blockcards = new Blocked_Cards_Info(blockedcardsId, cardSerial);

                        blkCardsArray.add(blockcards);


                    } catch (Exception e) // no guruji
                    {

                        e.printStackTrace();

                    }
                }

                int status = db.addBlockCardInfo(blkCardsArray);

                if (status == 0) {


                    int count = db.updateLastBlockCard(schoolmachinecode);

                    System.out.println("Count11: " + count);

                    initTXTV.setText("BLOCKED CARD");
                    progressBar.setVisibility(View.GONE);
                    System.out.println("CHILLAR CAlling intent");


                    SharedPreferences.Editor editor = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
                    editor.putString("machineflag", "true");
                    editor.commit();
                    Constants.machineID = machineID;

                    Intent i = new Intent(getApplicationContext(), LogIn.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                } else {

                    finish();
                }

            } finally {


            }


        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


}
