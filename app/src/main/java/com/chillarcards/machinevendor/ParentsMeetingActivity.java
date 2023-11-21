package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Parent;
import com.chillarcards.machinevendor.ModelClass.Parent_Student;
import com.chillarcards.machinevendor.ModelClass.Reason;
import com.chillarcards.machinevendor.ModelClass.StudentList;

/**
 * Created by Codmob on 17-03-17.
 */

public class ParentsMeetingActivity extends Activity {

    Button p_meeting,others;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    String User_name,Pass_word;
    int userid;
    int useridinteger;
    String schooid,schoomachcode,machineserial,download_status;
    ProgressBar progressBar;
    DatabaseHandler db;

    LinearLayout linearLayout;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    ArrayList<StudentList> stdListArray=new ArrayList<StudentList>();
    ArrayList<Parent> parentArray=new ArrayList<Parent>();
    ArrayList<Parent_Student> parentStdtArray=new ArrayList<Parent_Student>();
    ArrayList<Reason> reasonTypeArray=new ArrayList<Reason>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_parents_meeting_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid= editor.getInt("userid",0);
        schooid = editor.getString("schoolId", "");
        schoomachcode= editor.getString("schlMachCode", "");
        machineserial= editor.getString("machineserial", "");

        drawerArrow();


        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);


        p_meeting= (Button) findViewById(R.id.parentmeet);
        others= (Button) findViewById(R.id.other_reasons);
        linearLayout= (LinearLayout) findViewById(R.id.linear);


        p_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String id=db.getReasonId("Parents Meeting");

                Intent intent=new Intent(getApplicationContext(),ParentCardActivity.class);
                Bundle b=new Bundle();
                b.putString("type","p_meeting");

                intent.putExtras(b);
//                b.putString("typeid",id);.
                startActivity(intent);


            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),ParentCardActivity.class);
                Bundle b=new Bundle();
                b.putString("type","others");

                i.putExtras(b);

                startActivity(i);


            }
        });



    }


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
                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();




        final SharedPreferences[] preferences = {getSharedPreferences("Chillar", MODE_PRIVATE)};
        download_status = preferences[0].getString("download_parent", "");

        System.out.println("SharedPref: "+download_status);

        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        new reachableURL1().execute();
    }

    private class reachableURL1 extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Constants.AsyncFlag=0;
        }

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

            Constants.AsyncFlag=1;
            progressBar.setVisibility(View.GONE);


//            layt_search.setVisibility(View.VISIBLE);
            if(s.equals("true")) {

                if (download_status.equals("false")){

                    finish();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ParentsMeetingActivity.this);
                    builder.setTitle("Student List");
                    builder.setCancelable(false);
                    builder.setMessage("Do you want to want to download student list ?");

                    DialogInterface.OnClickListener listener;
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setVisibility(View.VISIBLE);
                            download1();

//                            preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
//                            editor= preferences.edit();
//                            editor.putString("download_parent","true");
//                            System.out.println("Student list downloaded");
//                            editor.commit();

                            dialog.dismiss();


                        }
                    });
                    builder.show();


                }
                else {


                    call_fn();


                    /*int count=db.getAllStudents().size();
                    int count1=db.getAllParents().size();
                    int count2=db.getAllParentStudent().size();
                    int count3=db.getAllReason().size();

                    if (count==0||count1==0||count2==0||count3==0)
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ParentsMeetingActivity.this);
                        builder.setTitle("Student List");
                        builder.setCancelable(false);
                        builder.setMessage("Do you want to want to download student list ?");

                        DialogInterface.OnClickListener listener;
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                        Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
//                        startActivity(i);

                                progressBar.setVisibility(View.VISIBLE);
                                download1();



                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                    else {

//                stud_class.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
                        call_fn();


                    }*/




                }


            }else{

                call_fn();
                /*int count=db.getAllStudents().size();
                int count1=db.getAllParents().size();
                int count2=db.getAllParentStudent().size();
                int count3=db.getAllReason().size();

                if (count==0||count1==0||count2==0||count3==0){
                    Toast.makeText(ParentsMeetingActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    call_fn();
                }*/
            }
        }
    }

    private void call_fn() {

//        tap_the_card.setEnabled(true);
//        tap_the_card.setClickable(true);

        linearLayout.setVisibility(View.VISIBLE);

        System.out.println("CHillar: Callfn is called");

        List<StudentList> test1=db.getAllStudents();

        for (StudentList studentList1:test1){
            System.out.println("ParentSystem: StdDetails CardSl: "+ studentList1.getStudent_card_serial()+" Name: "+studentList1.getStudent_name()
                    +" StudId "+studentList1.getUser_id()+ " Class "+studentList1.getStudent_class()+" "+studentList1.getStudent_division());
        }

        List <Parent> test12=db.getAllParents();

        for (Parent studentList1:test12){
            System.out.println("ParentSystem: parentDetails CardSl: "+ studentList1.getCard_serial()+" Name: "+studentList1.getParent_name()
                    +" ParentId "+studentList1.getUser_id());
        }

        List <Parent_Student> test11=db.getAllParentStudent();

        for (Parent_Student studentList1:test11){
            System.out.println("ParentSystem: ParentStudent ParntId: "+ studentList1.getParent_user_id()+" StudId: "+studentList1.getStd_user_id());
        }

        List <Reason> test10=db.getAllReason();

        for (Reason studentList1:test10){
            System.out.println("ParentSystem: Reason ReasonId: "+ studentList1.getReasonId()+" reasontype: "+studentList1.getReasonType());
        }





    }

    private void download1() {
        String  tag_string_req = "download student list";

        String url=Constants.APP_URL+"r_all_students.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
        System.out.println("Url Call_1 " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
//				progressBar.setVisibility(View.GONE);
                System.out.println("Url Call_1 " + response);

                db.deleteAllStudentlist();
                parseJSONDownload1(response);

//                download2();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
//                finish();

            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue

        App.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void parseJSONDownload1(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            stdListArray.clear();
            progressBar.setVisibility(View.VISIBLE);


            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String schoolId=jsobj.getString("schoolID");
                String studentID=jsobj.getString("student_userID");
                String cardSerial=jsobj.getString("cardSerial");
                String standardID=jsobj.getString("standardID");
                String standardName=jsobj.getString("standardName");
                String standardDivisionID=jsobj.getString("standardDivisionID");
                String standardDivisionName=jsobj.getString("standardDivisionName");
//                String password=jsobj.getString("password");

                String student_name=jsobj.getString("studentName");;


                StudentList studentList=new StudentList(cardSerial,studentID,student_name,standardName,standardDivisionName);

                stdListArray.add(studentList);





            }

            int status=db.addStudentlist(stdListArray);

            if(status==0){

                download2();

            }else{
                Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
//                finish();
            }


        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    private void download2() {
        String  tag_string_req = "download parent details";

        String url=Constants.APP_URL+"r_all_parents.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
        System.out.println("Url Call_2 " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
//				progressBar.setVisibility(View.GONE);
                System.out.println("Url Call_1 " + response);

                db.deleteAllParentdetails();
                parseJSONDownload2(response);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
//                finish();

            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue

        App.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void parseJSONDownload2(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            parentArray.clear();

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String parent_userID=jsobj.getString("parent_userID");
                String parentName=jsobj.getString("parentName");
                String cardSerial=jsobj.getString("cardSerial");

                Parent parent=new Parent(parent_userID,parentName,cardSerial);


                parentArray.add(parent);




            }

            int status=db.addparentDetails(parentArray);

            if(status==0){

                download3();

            }else{
                Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
//                finish();
            }


        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    private void download3() {
        String  tag_string_req = "download parent student";

        String url=Constants.APP_URL+"r_parent_students.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
        System.out.println("Url Call_3 " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
//				progressBar.setVisibility(View.GONE);
                System.out.println("Url Call_3 " + response);

                db.deleteAllParentStudent();
                parseJSONDownload3(response);

//                download4();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
//                finish();

            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue

        App.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void parseJSONDownload3(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);
            parentStdtArray.clear();

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String parent_userID=jsobj.getString("parent_userID");
                String student_userID=jsobj.getString("student_userID");

                Parent_Student studentList=new Parent_Student(student_userID,parent_userID);

                parentStdtArray.add(studentList);


            }

            int status= db.addParentStudent(parentStdtArray);

            if(status==0){
                download4();

            }else{
                Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
//                finish();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    private void download4() {
        String  tag_string_req = "download reason type";

        String url=Constants.APP_URL+"r_reason_type.php?machineUserName="+User_name+"&machineUserPassword="+Pass_word+"&schoolMachineCode="+schoomachcode;
        System.out.println("Url Call_3 " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
//				progressBar.setVisibility(View.GONE);
                System.out.println("Url Call_3 " + response);

                db.deleteAllReasonType();
                parseJSONDownload4(response);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
//                finish();

            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue

        App.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void parseJSONDownload4(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            reasonTypeArray.clear();

            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String id=jsobj.getString("id");
                String reason_type_name=jsobj.getString("reason_type_name");


                Reason studentList=new Reason(id,reason_type_name);

                reasonTypeArray.add(studentList);




            }

            int status=db.addreasonType(reasonTypeArray);

            if(status==0){
                SharedPreferences preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                editor.putString("download_parent","true");
                System.out.println("Student list downloaded");
                editor.commit();

                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Download Completed", Toast.LENGTH_SHORT).show();

                call_fn();
//                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
//                finish();
            }



        } catch (Exception e) {

            e.printStackTrace();

        }
    }

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

}
