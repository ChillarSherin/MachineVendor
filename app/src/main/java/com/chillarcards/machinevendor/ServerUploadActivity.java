package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Codmob on 18-09-17.
 */

public class ServerUploadActivity extends Activity {


    List<Success_Transaction> successtransaction7 = new ArrayList<>();
    List<Success_Transaction> sxstrnxn= new ArrayList<>();
    List<Attendance_Data> attdatas= new ArrayList<>();
    List<Attendance_Data> teacher_attdatas= new ArrayList<>();
    List<Recharge_Data> rechrgedata= new ArrayList<>();
    List<Fee_Transaction> contacts3= new ArrayList<>();
    List<Library_book_transaction> contacts1= new ArrayList<>();
    List<Payment_Transaction> paytransaction= new ArrayList<>();
    List<Item_Sale> success= new ArrayList<>();
    List<Sales_Item> sales_item= new ArrayList<>();
    List<Item_Sale> itsale= new ArrayList<>();
    List<Refund> refunds= new ArrayList<>();
    List<Online_Recharge> online= new ArrayList<>();
    List<Sales_Item> salesitm= new ArrayList<>();
    List<Payment_Transaction> paytransaction1= new ArrayList<>();
    List<Create_Leave> createleave= new ArrayList<>();



    int statusCount=0;
    int totalCount=0;

    String User_name,Pass_word,userID,schooid,schoomachcode,machineserial,machineID,machineSL;
    int useridinteger;
    String versionName="";

    ProgressBar progressBar;

    DatabaseHandler db ;
    ArrayList<Blocked_Cards_Info> blkCardsArray=new ArrayList<Blocked_Cards_Info>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_server_update);

        System.out.println("Chillarrr: ServerUploadActivity");

        db = DatabaseHandler.getInstance(getApplicationContext());

        intialiseUIElements();

        sharedPrefs();

        startServerUpload();

    }

    private void sharedPrefs() {

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userID= String.valueOf(editor.getInt("userid",0));
        schooid = editor.getString("schoolId", "");
        schoomachcode= editor.getString("schlMachCode", "");
        machineserial= editor.getString("machineserial", "");
        machineID=editor.getString("machineid","");
        machineSL=editor.getString("machineSL","");
        versionName = editor.getString("appVersion", "");

        useridinteger= Integer.parseInt(userID);

        System.out.println("CODMOB: sharedPrefs(): "+schooid+schoomachcode+machineserial);
    }

    private void intialiseUIElements() {

        progressBar= (ProgressBar) findViewById(R.id.progbar);

    }


    private void startServerUpload() {

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

        totalCount += successtransaction7.size()+sxstrnxn.size()+attdatas.size()+teacher_attdatas.size()
                + rechrgedata.size()+ contacts3.size()+ contacts1.size()+ paytransaction.size()
                + success.size()+ sales_item.size()+ itsale.size()+ refunds.size()
                + online.size()+ salesitm.size()+ paytransaction1.size()+ createleave.size();

        progressBar.setMax(totalCount);



        if(successtransaction7.size()>0) {
            System.out.println("Chillar: successtransaction7: ");
            PhpLoaderTest1(successtransaction7, 0);
        }else if(sxstrnxn.size()>0){
            System.out.println("Chillar: sxstrnxn: ");
            PhpLoaderNewTest1(sxstrnxn,0);
        }else if(attdatas.size()>0 ){
            System.out.println("Chillar: attdatas: ");
            PhpLoaderTest2(attdatas,0);
        }else if(teacher_attdatas.size()>0){
            System.out.println("Chillar: teacher_attdatas: ");
            PhpLoaderteacheratteTest(teacher_attdatas,0);
        }else if(rechrgedata.size()>0){
            System.out.println("Chillar: rechrgedata: ");
            PhpLoaderTest3(rechrgedata,0);
        }else if(contacts3.size()>0){
            System.out.println("Chillar: contacts3: ");
            PhpLoaderTest4(contacts3,0);
        }else if(contacts1.size()>0){
            System.out.println("Chillar: contacts1: ");
            PhpLoaderTest5(contacts1,0);
        }else if(paytransaction.size()>0){
            System.out.println("Chillar: paytransaction: ");
            PhpLoaderTest6(paytransaction,0);
        }else if(success.size()>0){
            System.out.println("Chillar: success: ");
            PhpLoaderTest7(success,0);
        }else if(sales_item.size()>0){
            System.out.println("Chillar: sales_item: ");
            PhpLoaderTest8(sales_item,0);
        }else if(itsale.size()>0){
            System.out.println("Chillar: itsale: ");
            PhpLoaderNewTest2(itsale,0);
        }else if(refunds.size()>0){
            System.out.println("Chillar: refunds: ");
            PhpLoaderTest9(refunds,0);
        }else if(online.size()>0){
            System.out.println("Chillar: online: ");
            PhpLoaderTest10(online,0);
        }else if(salesitm.size()>0){
            System.out.println("Chillar: salesitm: ");
            PhpLoaderNewTest3(salesitm,0);
        }else if(paytransaction1.size()>0){
            System.out.println("Chillar: paytransaction1: ");
            PhpLoaderTest11(paytransaction1,0);
        }else if(createleave.size()>0){
            System.out.println("Chillar: createleave: ");
            PhpLoaderTest12(createleave,0);
        }else {
            System.out.println("FU1");

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


                    System.out.println("PhpLoader1  code "+server_respose);
                    if (server_respose.equals("success"))
                    {
                        db.updatesuccess(id);

                    }else{

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                    e.printStackTrace();

                }


                int count=i;
                ++count;

                if(db.getAllsuccesstoup().size()>0){
                    System.out.println("Chillar: 1 ");

                    if(count<successtransaction7.size()) {
                        PhpLoaderTest1(successtransaction7, count);
                    }else{
                        if(sxstrnxn.size()>0){
                            System.out.println("Chillar: 2 ");
                            PhpLoaderNewTest1(sxstrnxn,0);
                        }else if(attdatas.size()>0 ){
                            System.out.println("Chillar: 3 ");
                            PhpLoaderTest2(attdatas,0);
                        }else if(teacher_attdatas.size()>0){
                            System.out.println("Chillar: 4 ");
                            PhpLoaderteacheratteTest(teacher_attdatas,0);
                        }else if(rechrgedata.size()>0){
                            System.out.println("Chillar: 5 ");
                            PhpLoaderTest3(rechrgedata,0);
                        }else if(contacts3.size()>0){
                            System.out.println("Chillar: 6 ");
                            PhpLoaderTest4(contacts3,0);
                        }else if(contacts1.size()>0){
                            System.out.println("Chillar: 7 ");
                            PhpLoaderTest5(contacts1,0);
                        }else if(paytransaction.size()>0){
                            System.out.println("Chillar: 8 ");
                            PhpLoaderTest6(paytransaction,0);
                        }else if(success.size()>0){
                            System.out.println("Chillar: 9 ");
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            System.out.println("Chillar: 10 ");
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 11 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 12 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 13 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 14 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 15 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 16 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU2");
//                    Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                            BlockedCards1();
                        }
                    }

                }else if(sxstrnxn.size()>0){
                    System.out.println("Chillar: 2 ");
                    PhpLoaderNewTest1(sxstrnxn,0);
                }else if(attdatas.size()>0 ){
                    System.out.println("Chillar: 3 ");
                    PhpLoaderTest2(attdatas,0);
                }else if(teacher_attdatas.size()>0){
                    System.out.println("Chillar: 4 ");
                    PhpLoaderteacheratteTest(teacher_attdatas,0);
                }else if(rechrgedata.size()>0){
                    System.out.println("Chillar: 5 ");
                    PhpLoaderTest3(rechrgedata,0);
                }else if(contacts3.size()>0){
                    System.out.println("Chillar: 6 ");
                    PhpLoaderTest4(contacts3,0);
                }else if(contacts1.size()>0){
                    System.out.println("Chillar: 7 ");
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    System.out.println("Chillar: 8 ");
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    System.out.println("Chillar: 9 ");
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 10 ");
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 11 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 12 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 13 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 14 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 15 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 16 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU2");
//                    Toast.makeText(getApplicationContext(), "Fully Uploaded", Toast.LENGTH_SHORT).show();
                    BlockedCards1();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

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


                    System.out.println("PhpLoaderNew1  code "+server_respose);
                    if (server_respose.equals("success"))
                    {
                        db.updatesuccessRefund2(id);
//                            db.close();
                    }else{

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                    int count=i;
                    ++count;

                    if(db.getAllsxstoupNew().size()>0){
                        System.out.println("Chillar: 17 ");

                        if(count<sxstrnxn.size()) {
                            PhpLoaderNewTest1(sxstrnxn,count);
                        }else{
                            if(attdatas.size()>0 ){
                                System.out.println("Chillar: 18 ");
                                PhpLoaderTest2(attdatas,0);
                            }else if(teacher_attdatas.size()>0){
                                System.out.println("Chillar: 19 ");
                                PhpLoaderteacheratteTest(teacher_attdatas,0);
                            }else if(rechrgedata.size()>0){
                                System.out.println("Chillar: 20 ");
                                PhpLoaderTest3(rechrgedata,0);
                            }else if(contacts3.size()>0){
                                System.out.println("Chillar: 21 ");
                                PhpLoaderTest4(contacts3,0);
                            }else if(contacts1.size()>0){
                                System.out.println("Chillar: 22 ");
                                PhpLoaderTest5(contacts1,0);
                            }else if(paytransaction.size()>0){
                                System.out.println("Chillar: 23 ");
                                PhpLoaderTest6(paytransaction,0);
                            }else if(success.size()>0){
                                System.out.println("Chillar: 24 ");
                                PhpLoaderTest7(success,0);
                            }else if(sales_item.size()>0){
                                System.out.println("Chillar: 25 ");
                                PhpLoaderTest8(sales_item,0);
                            }else if(itsale.size()>0){
                                System.out.println("Chillar: 26 ");
                                PhpLoaderNewTest2(itsale,0);
                            }else if(refunds.size()>0){
                                System.out.println("Chillar: 27 ");
                                PhpLoaderTest9(refunds,0);
                            }else if(online.size()>0){
                                System.out.println("Chillar: 28 ");
                                PhpLoaderTest10(online,0);
                            }else if(salesitm.size()>0){
                                System.out.println("Chillar: 29 ");
                                PhpLoaderNewTest3(salesitm,0);
                            }else if(paytransaction1.size()>0){
                                System.out.println("Chillar: 30 ");
                                PhpLoaderTest11(paytransaction1,0);
                            }else if(createleave.size()>0){
                                System.out.println("Chillar: 31 ");
                                PhpLoaderTest12(createleave,0);
                            }else {
                                System.out.println("FU3");

                                BlockedCards1();
                            }
                        }



                    }else if(attdatas.size()>0 ){
                        System.out.println("Chillar: 18 ");
                        PhpLoaderTest2(attdatas,0);
                    }else if(teacher_attdatas.size()>0){
                        System.out.println("Chillar: 19 ");
                        PhpLoaderteacheratteTest(teacher_attdatas,0);
                    }else if(rechrgedata.size()>0){
                        System.out.println("Chillar: 20 ");
                        PhpLoaderTest3(rechrgedata,0);
                    }else if(contacts3.size()>0){
                        System.out.println("Chillar: 21 ");
                        PhpLoaderTest4(contacts3,0);
                    }else if(contacts1.size()>0){
                        System.out.println("Chillar: 22 ");
                        PhpLoaderTest5(contacts1,0);
                    }else if(paytransaction.size()>0){
                        System.out.println("Chillar: 23 ");
                        PhpLoaderTest6(paytransaction,0);
                    }else if(success.size()>0){
                        System.out.println("Chillar: 24 ");
                        PhpLoaderTest7(success,0);
                    }else if(sales_item.size()>0){
                        System.out.println("Chillar: 25 ");
                        PhpLoaderTest8(sales_item,0);
                    }else if(itsale.size()>0){
                        System.out.println("Chillar: 26 ");
                        PhpLoaderNewTest2(itsale,0);
                    }else if(refunds.size()>0){
                        System.out.println("Chillar: 27 ");
                        PhpLoaderTest9(refunds,0);
                    }else if(online.size()>0){
                        System.out.println("Chillar: 28 ");
                        PhpLoaderTest10(online,0);
                    }else if(salesitm.size()>0){
                        System.out.println("Chillar: 29 ");
                        PhpLoaderNewTest3(salesitm,0);
                    }else if(paytransaction1.size()>0){
                        System.out.println("Chillar: 30 ");
                        PhpLoaderTest11(paytransaction1,0);
                    }else if(createleave.size()>0){
                        System.out.println("Chillar: 31 ");
                        PhpLoaderTest12(createleave,0);
                    }else {
                        System.out.println("FU3");

                        BlockedCards1();
                    }



                } catch (Exception e) {

                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllAttendancedatatoUP().size()>0){

                    if(count<attdatas.size()) {
                        PhpLoaderTest2(attdatas,count);
                    }else{
                        if(teacher_attdatas.size()>0){
                            System.out.println("Chillar: 312 ");
                            PhpLoaderteacheratteTest(teacher_attdatas,0);
                        }else if(rechrgedata.size()>0){
                            System.out.println("Chillar: 33 ");
                            PhpLoaderTest3(rechrgedata,0);
                        }else if(contacts3.size()>0){
                            System.out.println("Chillar: 34 ");
                            PhpLoaderTest4(contacts3,0);
                        }else if(contacts1.size()>0){
                            System.out.println("Chillar: 35 ");
                            PhpLoaderTest5(contacts1,0);
                        }else if(paytransaction.size()>0){
                            System.out.println("Chillar: 36 ");
                            PhpLoaderTest6(paytransaction,0);
                        }else if(success.size()>0){
                            System.out.println("Chillar: 37 ");
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            System.out.println("Chillar: 38 ");
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 39 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 40 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 41 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 42 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 43 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 44 ");
                            PhpLoaderTest12(createleave,0);
                        }else {System.out.println("FU4");

                            BlockedCards1();
                        }

                    }



                }else if(teacher_attdatas.size()>0){
                    System.out.println("Chillar: 312 ");
                    PhpLoaderteacheratteTest(teacher_attdatas,0);
                }else if(rechrgedata.size()>0){
                    System.out.println("Chillar: 33 ");
                    PhpLoaderTest3(rechrgedata,0);
                }else if(contacts3.size()>0){
                    System.out.println("Chillar: 34 ");
                    PhpLoaderTest4(contacts3,0);
                }else if(contacts1.size()>0){
                    System.out.println("Chillar: 35 ");
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    System.out.println("Chillar: 36 ");
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    System.out.println("Chillar: 37 ");
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 38 ");
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 39 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 40 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 41 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 42 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 43 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 44 ");
                    PhpLoaderTest12(createleave,0);
                }else {System.out.println("FU4");
                    BlockedCards1();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllteacherAttendancedatatoUP().size()>0){
                    System.out.println("Chillar: 45 ");

                    if(count<teacher_attdatas.size()) {
                        PhpLoaderteacheratteTest(teacher_attdatas,count);
                    }else{
                        if(rechrgedata.size()>0){
                            System.out.println("Chillar: 47 ");
                            PhpLoaderTest3(rechrgedata,0);
                        }else if(contacts3.size()>0){
                            System.out.println("Chillar: 48 ");
                            PhpLoaderTest4(contacts3,0);
                        }else if(contacts1.size()>0){
                            System.out.println("Chillar: 49 ");
                            PhpLoaderTest5(contacts1,0);
                        }else if(paytransaction.size()>0){
                            System.out.println("Chillar: 50 ");
                            PhpLoaderTest6(paytransaction,0);
                        }else if(success.size()>0){
                            System.out.println("Chillar: 51 ");
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            System.out.println("Chillar: 52 ");
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 53 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 54 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 55 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 56 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 57 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 58 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU16");

                            BlockedCards1();
                        }
                    }




                }else if(rechrgedata.size()>0){
                    System.out.println("Chillar: 47 ");
                    PhpLoaderTest3(rechrgedata,0);
                }else if(contacts3.size()>0){
                    System.out.println("Chillar: 48 ");
                    PhpLoaderTest4(contacts3,0);
                }else if(contacts1.size()>0){
                    System.out.println("Chillar: 49 ");
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    System.out.println("Chillar: 50 ");
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    System.out.println("Chillar: 51 ");
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 52 ");
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 53 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 54 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 55 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 56 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 57 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 58 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU16");

                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }


//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllrechtoUp().size()>0){
                    System.out.println("Chillar: 59 ");

                    if(count<rechrgedata.size()) {
                        PhpLoaderTest3(rechrgedata,count);
                    }else{
                        if(contacts3.size()>0){
                            System.out.println("Chillar: 59 ");
                            PhpLoaderTest4(contacts3,0);
                        }else if(contacts1.size()>0){
                            System.out.println("Chillar: 60 ");
                            PhpLoaderTest5(contacts1,0);
                        }else if(paytransaction.size()>0){
                            System.out.println("Chillar: 61 ");
                            PhpLoaderTest6(paytransaction,0);
                        }else if(success.size()>0){
                            System.out.println("Chillar: 62 ");
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            System.out.println("Chillar: 63 ");
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 64 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 65 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 66 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 67 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 68 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 69 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU5");

                            BlockedCards1();
                        }
                    }



                }else if(contacts3.size()>0){
                    System.out.println("Chillar: 59 ");
                    PhpLoaderTest4(contacts3,0);
                }else if(contacts1.size()>0){
                    System.out.println("Chillar: 60 ");
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    System.out.println("Chillar: 61 ");
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    System.out.println("Chillar: 62 ");
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 63 ");
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 64 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 65 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 66 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 67 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 68 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 69 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU5");

                    BlockedCards1();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllfeetranstoUp().size()>0){
                    System.out.println("Chillar: 70 ");

                    if(count<contacts3.size()) {
                        PhpLoaderTest4(contacts3,count);
                    }else{

                        if(contacts1.size()>0){
                            System.out.println("Chillar: 71 ");
                            PhpLoaderTest5(contacts1,0);
                        }else if(paytransaction.size()>0){
                            System.out.println("Chillar: 72 ");
                            PhpLoaderTest6(paytransaction,0);
                        }else if(success.size()>0){
                            System.out.println("Chillar: 73 ");
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            System.out.println("Chillar: 74 ");
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 75 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 76 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 77 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 78 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 79 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 80 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU6");

                            BlockedCards1();
                        }


                    }



                }else if(contacts1.size()>0){
                    System.out.println("Chillar: 71 ");
                    PhpLoaderTest5(contacts1,0);
                }else if(paytransaction.size()>0){
                    System.out.println("Chillar: 72 ");
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    System.out.println("Chillar: 73 ");
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 74 ");
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 75 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 76 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 77 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 78 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 79 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 80 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU6");

                    BlockedCards1();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;
                if(db.getAlllibtoUp().size()>0){
                    System.out.println("Chillar: 81 ");

                    if(count<contacts1.size()) {
                        PhpLoaderTest5(contacts1,count);
                    }else{

                        if(paytransaction.size()>0){
                            System.out.println("Chillar: 82 ");
                            PhpLoaderTest6(paytransaction,0);
                        }else if(success.size()>0){
                            System.out.println("Chillar: 83 ");
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            System.out.println("Chillar: 84 ");
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 85 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 86 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 87 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 88 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 89 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 90 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("Chillar: 91 ");
                            System.out.println("FU16000");

                            BlockedCards1();
                        }

                    }




                }else if(paytransaction.size()>0){
                    System.out.println("Chillar: 82 ");
                    PhpLoaderTest6(paytransaction,0);
                }else if(success.size()>0){
                    System.out.println("Chillar: 83 ");
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 84 ");
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 85 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 86 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 87 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 88 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 89 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 90 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("Chillar: 91 ");
                    System.out.println("FU16000");

                    BlockedCards1();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);


                int count=i;
                ++count;

                if(db.getAllpaytransactiontoUp().size()>0){
                    System.out.println("Chillar: 92 ");

                    if(count<paytransaction.size()) {
                        PhpLoaderTest6(paytransaction,count);
                    }else{
                        if(success.size()>0){
                            System.out.println("Chillar: 92 ");
                            PhpLoaderTest7(success,0);
                        }else if(sales_item.size()>0){
                            System.out.println("Chillar: 93 ");
                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 94 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 95 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 96 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 97 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 98 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 99 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU7");
                            BlockedCards1();
                        }
                    }



                }else if(success.size()>0){
                    System.out.println("Chillar: 92 ");
                    PhpLoaderTest7(success,0);
                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 93 ");
                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 94 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 95 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 96 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 97 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 98 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 99 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU7");
                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);


                int count=i;
                ++count;

                if(db.getAllitemsaletoUp().size()>0){
                    System.out.println("Chillar: 100 ");
                    if(count<success.size()) {
                        PhpLoaderTest7(success,count);
                    }else{
                        if(sales_item.size()>0){
                            System.out.println("Chillar: 101 ");

                            PhpLoaderTest8(sales_item,0);
                        }else if(itsale.size()>0){
                            System.out.println("Chillar: 102 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 103 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 104 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 105 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 106 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 107 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU8");

                            BlockedCards1();
                        }
                    }





                }else if(sales_item.size()>0){
                    System.out.println("Chillar: 101 ");

                    PhpLoaderTest8(sales_item,0);
                }else if(itsale.size()>0){
                    System.out.println("Chillar: 102 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 103 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 104 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 105 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 106 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 107 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU8");

                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);


                int count=i;
                ++count;

                if(db.getAllsaletoUp().size()>0){
                    System.out.println("Chillar: 108 ");

                    if(count<sales_item.size()) {
                        PhpLoaderTest8(sales_item ,count);
                    }else{
                        if(itsale.size()>0){
                            System.out.println("Chillar: 109 ");
                            PhpLoaderNewTest2(itsale,0);
                        }else if(refunds.size()>0){
                            System.out.println("Chillar: 110 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 111 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 112 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 113 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 114 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU9");

                            BlockedCards1();
                        }
                    }




                }else if(itsale.size()>0){
                    System.out.println("Chillar: 109 ");
                    PhpLoaderNewTest2(itsale,0);
                }else if(refunds.size()>0){
                    System.out.println("Chillar: 110 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 111 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 112 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 113 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 114 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU9");

                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

                //                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllitemsaletoUpNew().size()>0){
                    System.out.println("Chillar: 115 ");

                    if(count<itsale.size()) {
                        PhpLoaderNewTest2(itsale ,count);
                    }else{
                        if(refunds.size()>0){
                            System.out.println("Chillar: 116 ");
                            PhpLoaderTest9(refunds,0);
                        }else if(online.size()>0){
                            System.out.println("Chillar: 117 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 118 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 119 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 120 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU10");

                            BlockedCards1();
                        }
                    }




                }else if(refunds.size()>0){
                    System.out.println("Chillar: 116 ");
                    PhpLoaderTest9(refunds,0);
                }else if(online.size()>0){
                    System.out.println("Chillar: 117 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 118 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 119 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 120 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU10");

                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);



                int count=i;
                ++count;

                if(db.getAllRefundtoUp().size()>0){
                    System.out.println("Chillar: 121 ");

                    if(count<refunds.size()) {
                        PhpLoaderTest9(refunds ,count);
                    }else{
                        if(online.size()>0){
                            System.out.println("Chillar: 122 ");
                            PhpLoaderTest10(online,0);
                        }else if(salesitm.size()>0){
                            System.out.println("Chillar: 123 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 124 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 125 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU11");

                            BlockedCards1();
                        }

                    }




                }else if(online.size()>0){
                    System.out.println("Chillar: 122 ");
                    PhpLoaderTest10(online,0);
                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 123 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 124 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 125 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU11");

                    BlockedCards1();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

//                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);


                int count=i;
                ++count;

                if(db.getAllOnlineToUp().size()>0){
                    System.out.println("Chillar: 126 ");

                    if(count<online1.size()) {
                        PhpLoaderTest10(online1 ,count);
                    }else{
                        if(salesitm.size()>0){
                            System.out.println("Chillar: 127 ");
                            PhpLoaderNewTest3(salesitm,0);
                        }else if(paytransaction1.size()>0){
                            System.out.println("Chillar: 128 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 129 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU12");

                            BlockedCards1();
                        }
                    }



                }else if(salesitm.size()>0){
                    System.out.println("Chillar: 127 ");
                    PhpLoaderNewTest3(salesitm,0);
                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 128 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 129 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU12");

                    BlockedCards1();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();

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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

                //                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_successtransaction "+response);


                int count=i;
                ++count;

                if(db.getAllsaletoUpNew().size()>0){
                    System.out.println("Chillar: 130 ");

                    if(count<salesitm.size()) {
                        PhpLoaderNewTest3(salesitm ,count);
                    }else{
                        if(paytransaction1.size()>0){
                            System.out.println("Chillar: 131 ");
                            PhpLoaderTest11(paytransaction1,0);
                        }else if(createleave.size()>0){
                            System.out.println("Chillar: 132 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU13");

                            BlockedCards1();
                        }
                    }



                }else if(paytransaction1.size()>0){
                    System.out.println("Chillar: 131 ");
                    PhpLoaderTest11(paytransaction1,0);
                }else if(createleave.size()>0){
                    System.out.println("Chillar: 132 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU13");

                    BlockedCards1();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

                //                     progressBar.setVisibility(View.GONE);
                System.out.println("json_parse_payrefundtransaction "+response);


                int count=i;
                ++count;

                if(db.getAllpaytransactiontoUpNew().size()>0){
                    System.out.println("Chillar: 133 ");

                    if(count<paytransaction1.size()) {
                        PhpLoaderTest11(paytransaction1 ,count);
                    }else{
                        if(createleave.size()>0){
                            System.out.println("Chillar: 134 ");
                            PhpLoaderTest12(createleave,0);
                        }else {
                            System.out.println("FU14");

                            BlockedCards1();
                        }
                    }



                }else if(createleave.size()>0){
                    System.out.println("Chillar: 134 ");
                    PhpLoaderTest12(createleave,0);
                }else {
                    System.out.println("FU14");

                    BlockedCards1();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                finish();
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

                        Toast.makeText(ServerUploadActivity.this, id, Toast.LENGTH_LONG).show();
                    }






                } catch (Exception e) {

                    e.printStackTrace();

                }


                System.out.println("json_parse_payrefundtransaction "+response);

                int count=i;
                ++count;

                if(db.getAllCreateLeaveToUp().size()>0){
                    System.out.println("Chillar: 135 ");
                    if(count<createleave.size()) {
                        PhpLoaderTest12(createleave ,count);
                    }else{
                        System.out.println("Chillar: FU000 ");
                        BlockedCards1();
                    }


                }else{
                    System.out.println("Chillar: FU000 ");
                    BlockedCards1();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void BlockedCards1() {

        try {

            String tag_string_req = "list_announce";

            String lastUpdated = db.getLastBlockCardsupdate();

            String url="";

            if(lastUpdated.equals("")){

                url = Constants.APP_URL + "r_new_blocked_cards.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20")+"&current_version="+versionName;

            }else{

                url = Constants.APP_URL + "r_new_blocked_cards.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ","%20")+"&blockFromDate="+lastUpdated.replace(" ","%20")+"&current_version="+versionName;

            }

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

//                        db.deleteBlockedInfo();
                        parseJSONBlockedcards1(response);



                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        Toast.makeText(getApplicationContext(),"Server upload in progress, Please Wait",Toast.LENGTH_SHORT);
        return;
    }

    private void parseJSONBlockedcards1(String json) {

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

                int status= db.addBlockCardInfo(blkCardsArray);

                if(status==0){


                    int count=db.updateLastBlockCard(schoomachcode);

                    System.out.println("Count: "+count);



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
                            db.getAllCreateLeaveToUp().size()>0) {

                        Toast.makeText(getApplicationContext(), "Not Fully Uploaded. Please Try Again", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{

                        MachineUpdated();

                    }




                }else {

                    Toast.makeText(this, "Blocked Cards not fully updated.", Toast.LENGTH_SHORT).show();
                }


            }finally {

//
            }





        } catch (Exception e) {

            //  Toast.makeText(getApplicationContext(), "dsnfaj", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }


    private void MachineUpdated() {

        try {

            String tag_string_req = "machine_updated";

            String url = "";

            url = Constants.APP_URL + "u_machine_update.php?machineUserName=" + (User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode).replace(" ", "%20") + "&current_version=" + versionName;


            System.out.println("UrlNotice " + url);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
//                Log.d(TAG, response.toString());
                    System.out.println("CODMOB:Addmessages " + response);


                    try {
                        JSONObject jsobj;
                        jsobj = new JSONObject(response);

                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");


                        System.out.println("PhpLoaderNew1  code " + server_respose);
                        if (server_respose.equals("success")) {

                            Toast.makeText(getApplicationContext(), "Fully Uploaded.", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(), "Error in updation. Please try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

// Adding request to request queue
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    2500,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
