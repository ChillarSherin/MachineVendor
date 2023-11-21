package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Parent;
import com.chillarcards.machinevendor.ModelClass.Parent_Student;
import com.chillarcards.machinevendor.ModelClass.Reason;
import com.chillarcards.machinevendor.ModelClass.StudentList;

/**
 * Created by Codmob on 27-09-17.
 */

public class ParentDetailsDownloadActivity extends Activity {

    ProgressBar progressBar;
    String User_name,Pass_word,userID,schooid,schoomachcode,machineserial,machineID,machineSL;
    int useridinteger;
    String versionName="";

    int statusCount=0;
    int totalCount=0;

    DatabaseHandler db ;

    ArrayList<StudentList> stdListArray=new ArrayList<StudentList>();
    ArrayList<Parent> parentArray=new ArrayList<Parent>();
    ArrayList<Parent_Student> parentStdtArray=new ArrayList<Parent_Student>();
    ArrayList<Reason> reasonTypeArray=new ArrayList<Reason>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_parent_details_dld);

        db = DatabaseHandler.getInstance(getApplicationContext());

        intialiseUIElements();
        sharedPrefs();

        download2();
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


/*    private void download1() {
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
                finish();

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
                finish();
            }


        } catch (Exception e) {

            e.printStackTrace();

        }
    }*/


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
                finish();

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
                finish();
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
                finish();

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
                finish();
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
                finish();

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
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                finish();
            }



        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
