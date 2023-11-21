package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import com.chillarcards.machinevendor.ModelClass.StudentClassDiv;
import com.chillarcards.machinevendor.ModelClass.StudentList;

//import codmob.com.eventswallet.ModelClass.StudentList;

public class Student_List extends Activity {

    public ArrayAdapter<String> adapter, adapter1;
    ArrayList<String> st_class = new ArrayList<String>();
    ArrayList<String> st_div = new ArrayList<String>();
    ArrayList<String> st_card_serial = new ArrayList<String>();
    ArrayList<String> STUD_CLASS = new ArrayList<String>();
    ArrayList<String> STUD_DIV = new ArrayList<String>();
    ArrayList<String> STUD_CLASS_DIV = new ArrayList<String>();
    ArrayList<StudentList> stdListArray = new ArrayList<StudentList>();
    Spinner stud_class, stud_div;
    Button search;
    String download_status, attend_typ;
    DatabaseHandler db;
    String User_name, Pass_word, MachineCode;
    ProgressBar progressBar;
    RelativeLayout layt_search;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__list);

        db = DatabaseHandler.getInstance(getApplicationContext());


        stud_class = (Spinner) findViewById(R.id.stud_class);
        stud_div = (Spinner) findViewById(R.id.stud_div);
        search = (Button) findViewById(R.id.search);
        layt_search = (RelativeLayout) findViewById(R.id.Layt_search);
        progressBar = (ProgressBar) findViewById(R.id.progbar);
        drawerArrow();

        preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = preferences.getString("username", "");
        Pass_word = preferences.getString("password", "");
        MachineCode = preferences.getString("schlMachCode", "");

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
    public void onResume() {
        super.onResume();
        layt_search.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final SharedPreferences[] preferences = {getSharedPreferences("Chillar", MODE_PRIVATE)};
        download_status = preferences[0].getString("download", "");

        new reachableURL().execute();


        Bundle b = getIntent().getExtras();
        attend_typ = b.getString("id");
        System.out.println("attend_type : " + attend_typ);


//        if (download_status.equals("false")){
//
//            finish();
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Student List");
//            builder.setCancelable(false);
//            builder.setMessage("Do you want to want to download student list ?");
//
//            DialogInterface.OnClickListener listener;
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    download();
//
//                    preferences[0] = getSharedPreferences("Chillar", MODE_PRIVATE);
//                    editor= preferences[0].edit();
//                    editor.putString("download","true");
//                    System.out.println("Student list downloaded");
//                    editor.commit();
//
//                    dialog.dismiss();
//
//
//                }
//            });
//            builder.show();
//
//
//        }
//        else {
//            int count=db.getContact();
//
//            if (count==0)
//            {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Student List");
//                builder.setCancelable(false);
//                builder.setMessage("Do you want to want to download student list ?");
//
//                DialogInterface.OnClickListener listener;
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
////                        startActivity(i);
//
//                        progressBar.setVisibility(View.VISIBLE);
//                        download();
//
//                        preferences[0] = getSharedPreferences("Chillar", MODE_PRIVATE);
//                        editor= preferences[0].edit();
//                        editor.putString("download","true");
//                        System.out.println("Student list downloaded");
//                        editor.commit();
//
//                        dialog.dismiss();
//                    }
//                });
//                builder.show();
//
//            }
//            else {
//
////                stud_class.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                    }
////                });
//call_fn();
//
//
//            }
//
//
//
//
//        }


    }

    private void call_fn() {
        st_class.clear();

        List<StudentList> contacts = db.getstud_class();
        for (StudentList cn : contacts) {
            String log = "class no: " + cn.getStudent_class();
            // Writing Contacts to log


            Log.d("Name: ", log);
            System.out.println("Details\t\t" + log);
            st_class.add(cn.getStudent_class());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, st_class);
//                adapter.setDropDownViewResource(R.layout.activity_student__list);
        stud_class.setAdapter(adapter);

        stud_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected;
                selected = stud_class.getSelectedItem().toString();
                System.out.println("selected item :" + selected);

                st_div.clear();

                List<StudentList> contacts = db.getstud_div(selected);
                for (StudentList cn : contacts) {
                    String log = "st division: " + cn.getStudent_division();
                    // Writing Contacts to log


                    Log.d("Name: ", log);
                    System.out.println("Details\t\t" + log);
                    st_div.add(cn.getStudent_division());

                    stud_div.setVisibility(View.VISIBLE);
                    adapter1 = new ArrayAdapter<String>(Student_List.this, android.R.layout.simple_spinner_item, st_div);
//                adapter.setDropDownViewResource(R.layout.activity_student__list);
                    stud_div.setAdapter(adapter1);

                }


//                        if (stud_class.getItemAtPosition(position).equals("-1"))
//                        {
//
//                        }
//                        else {
//
//                        }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stud_div.setVisibility(View.GONE);
            }


        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stud_class.getSelectedItem().toString().equals("") || stud_div.getSelectedItem().toString().equals("")) {
                    Toast.makeText(Student_List.this, "Select Class & Division", Toast.LENGTH_SHORT).show();
                } else {

//                    Constants.S_Class.clear();
//                    Constants.S_Div.clear();
                    String s_class = stud_class.getSelectedItem().toString();
                    String s_div = stud_div.getSelectedItem().toString();
                    System.out.println("Student " + "Class " + s_class);
                    System.out.println("Student " + "Division " + s_div);

                    String class_div = (s_class + s_div).replace(" ", "");

                    System.out.println("Student " + "Class_Div " + class_div);

                    STUD_CLASS.clear();
                    STUD_DIV.clear();
                    STUD_CLASS_DIV.clear();

                    List<StudentClassDiv> test11 = db.getAllStudentClass();
                    for (StudentClassDiv studentList1 : test11) {
                        System.out.println("AttendanceSystem111: StudClasTable111 ID: " + studentList1.getId() + " ClassName: " + studentList1.getStud_class()
                                + " DivName " + studentList1.getStud_div());

                        STUD_CLASS.add(studentList1.getStud_class());
                        STUD_DIV.add(studentList1.getStud_div());
                        STUD_CLASS_DIV.add(studentList1.getClass_div());

                    }


                    if (STUD_CLASS_DIV.contains(class_div)) {

                        Toast.makeText(getApplicationContext(), "Already inserted!", Toast.LENGTH_SHORT).show();

                    } else {


                        System.out.println("AttendanceSystem");

                        StudentClassDiv studentClassDiv = new StudentClassDiv(s_class, s_div, class_div);
                        db.addStudentClass(studentClassDiv);

                        List<StudentClassDiv> test1 = db.getAllStudentClass();

                        for (StudentClassDiv studentList1 : test1) {
                            System.out.println("AttendanceSystem: StudClasTable ID: " + studentList1.getId() + " ClassName: " + studentList1.getStud_class()
                                    + " DivName " + studentList1.getStud_div());
                        }


//                        Constants.S_Class.add(s_class);
//                        Constants.S_Div.add(s_div);


//                    st_card_serial.clear();
//
//                    List<StudentList> contacts = db.getstud_cardserial(s_class,s_div);
//                    for (StudentList cn : contacts) {
//                        String log = "st card serial: " + cn.getStudent_card_serial();
//                        // Writing Contacts to log
//
//
//                        Log.d("Name: ", log);
//                        System.out.println("Details\t\t" + log);
//                        st_card_serial.add(cn.getStudent_card_serial());
//                    }


//                    Intent i=new Intent(getApplicationContext(),AttendenceNFC_New.class);
//                    Bundle b=new Bundle();
//                    b.putStringArrayList("card_list", st_card_serial);
//                    b.putString("id",attend_typ);
//                    i.putExtras(b);
//                    startActivity(i);

                        onBackPressed();
                    }
                }
            }
        });
    }

    private void download() {
        String tag_string_req = "download student list";

        String url = Constants.APP_URL + "r_all_students.php?schoolMachineCode=" + MachineCode + "&machineUserName=" + User_name + "&machineUserPassword=" + Pass_word;
        System.out.println("Url Call_1 " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
//				progressBar.setVisibility(View.GONE);
                System.out.println("Url Call_1 " + response);

                db.deleteAllStudentlist();
                parseJSONPAymentType(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("ErrorVolleyTrans " + error.getMessage());
                Toast.makeText(getApplicationContext(), "some error occurred" + error, Toast.LENGTH_LONG).show();

            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue

        App.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void parseJSONPAymentType(String json) {

        try {


            System.out.println("JSON--" + json);
            JSONArray jsar = new JSONArray(json);
            System.out.println("JSRRRRRR--" + jsar);

            stdListArray.clear();


            for (int i = 0; i < jsar.length(); i++) {

                String s = jsar.getString(i);
                JSONObject jsobj;
                jsobj = new JSONObject(s);

                String schoolId = jsobj.getString("schoolID");
                String studentID = jsobj.getString("student_userID");
                String cardSerial = jsobj.getString("cardSerial");
                String standardID = jsobj.getString("standardID");
                String standardName = jsobj.getString("standardName");
                String standardDivisionID = jsobj.getString("standardDivisionID");
                String standardDivisionName = jsobj.getString("standardDivisionName");
//                String password=jsobj.getString("password");

                String student_name = jsobj.getString("studentName");
                ;


                StudentList studentList = new StudentList(cardSerial, studentID, student_name, standardName, standardDivisionName);

                stdListArray.add(studentList);


            }

            int status = db.addStudentlist(stdListArray);

            if (status == 0) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Download Completed", Toast.LENGTH_SHORT).show();
                call_fn();

            } else {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                finish();
            }


        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private class reachableURL extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            if (isURLReachable(getApplicationContext())) {

                System.out.println("CHILLERe: Url reachable");
                return "true";

            } else {
                System.out.println("CHILLERe: Url not reachable");
                return "false";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            layt_search.setVisibility(View.VISIBLE);
            if (s.equals("true")) {

                if (download_status.equals("false")) {

                    finish();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Student_List.this);
                    builder.setTitle("Student List");
                    builder.setCancelable(false);
                    builder.setMessage("Do you want to want to download student list ?");

                    DialogInterface.OnClickListener listener;
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setVisibility(View.VISIBLE);
                            download();

                            preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
                            editor = preferences.edit();
                            editor.putString("download", "true");
                            System.out.println("Student list downloaded");
                            editor.commit();

                            dialog.dismiss();


                        }
                    });
                    builder.show();


                } else {
                    int count = db.getContact();

                    if (count == 0) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Student_List.this);
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
                                download();

                                preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
                                editor = preferences.edit();
                                editor.putString("download", "true");
                                System.out.println("Student list downloaded");
                                editor.commit();

                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    } else {

//                stud_class.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
                        call_fn();


                    }


                }


            } else {
                int count = db.getContact();

                if (count == 0) {
                    Toast.makeText(Student_List.this, "No Internet", Toast.LENGTH_SHORT).show();
                    Student_List.this.finish();
                } else {
                    call_fn();
                }
            }
        }
    }
}
