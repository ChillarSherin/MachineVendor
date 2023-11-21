package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Device_Info;


/**
 * Created by Codmob on 14-07-16.
 */
public class CardInitialisationActivity extends Activity {

    Button button;
    EditText editText;
    TextView textView1, textView2, textView3;
    String schoolname, schoolid, cardnumbers;
    DatabaseHandler db;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int tama_on;
    ProgressBar progressBar;
    Dialog dlg;
    String studentCode = "";
    String User_name, Pass_word;
    int userid;
    int useridinteger;
    String schooid, schoomachcode, machineserial;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

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
        setContentView(R.layout.layout_card_initialisation);


        db = DatabaseHandler.getInstance(getApplicationContext());


        dlg = new Dialog(this);
        drawerArrow();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        button = (Button) findViewById(R.id.writebtn);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);
        schooid = editor.getString("schoolId", "");
        schoomachcode = editor.getString("schlMachCode", "");
        machineserial = editor.getString("machineserial", "");

        editText = (EditText) findViewById(R.id.cardserial);
        textView1 = (TextView) findViewById(R.id.schoolid);
        textView2 = (TextView) findViewById(R.id.cardnumber);
        textView3 = (TextView) findViewById(R.id.schoolname);

        List<Device_Info> device_infos = db.getAlldevInfo();
//
        for (Device_Info us : device_infos) {
            String test = "machineid: " + us.getMachine_id() + " serialno : " + us.getSerial_no() + " school : " + us.getSchool_id() +
                    " mainurl : " + us.getMain_server_url() + " mainupload : " + us.getMain_upload_path();
            System.out.println("Device Info: " + test);

            schoolname = us.getSchoolname();
            schoolid = us.getSchool_id();
            cardnumbers = us.getSerial_no();

        }

        textView1.setText(schoolid);
        textView2.setText(cardnumbers);
        textView3.setText(schoolname);

//INITIALIZING NEW CARD

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                button.setClickable(false);
//                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                String serialno = String.valueOf(editText.getText());

                if (serialno.length() < 6 || serialno.length() > 6) {

                    Toast.makeText(getApplicationContext(), "6 digits only!", Toast.LENGTH_SHORT).show();

                } else {

                    new reachableURL().execute();


                }


            }
        });


    }

    private void GetDetailsPHP(final String cardserial) {

        String tag_string_req = "getdetails";


        String URL = Constants.APP_URL + "r_student_details.php?machineCode=" + schoomachcode + "&user_id=" + userid + "&cardSerial=" + cardserial;

        // String url = Constants.APP_URL+"r_item_stock.php?machineUserName="+usernamebundle+"&machineUserPassword="+passwordbundle+"&schoolMachineCode=CH0001";
        System.out.println("URL Blocked " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String studentName = jsonObject.getString("studentName");
                    final String studentID = jsonObject.getString("studentID");
                    String standard = jsonObject.getString("standard");
                    String division = jsonObject.getString("division");
                    studentCode = jsonObject.getString("studentCode");

                    dlg.setContentView(R.layout.popup_student_details);
                    dlg.setTitle("Student Details");
                    dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button OkButton = (Button) dlg.findViewById(R.id.btnok);
                    Button NoButton = (Button) dlg.findViewById(R.id.btnno);
                    TextView name = (TextView) dlg.findViewById(R.id.name);
                    TextView std = (TextView) dlg.findViewById(R.id.std);

                    name.setText(studentName);
                    std.setText(standard + " : " + division);

                    try {

                        dlg.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    OkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.cancel();

                            progressBar.setVisibility(View.VISIBLE);

                            CheckAlreadyInitialised(cardserial);


                        }
                    });


                    NoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.cancel();


                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("status");
                        String code = jsonObject1.getString("code");
                        if (code.equals("error")) ;
                        {
                            String message = jsonObject1.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
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

    private void CheckAlreadyInitialised(String cardserial) {
        System.out.println("checkinit");

        String tag_string_req = "checkinitialised";


        String URL = Constants.APP_URL + "r_card_initialisation.php?machineCode=" + schoomachcode + "&user_id=" + userid + "&cardSerial=" + cardserial;

        System.out.println("URL initialied " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());

                progressBar.setVisibility(View.GONE);
                button.setClickable(true);
                button.setEnabled(true);
                // progressDialog.dismiss();
                try {
                    JSONObject jsobj = new JSONObject(response);
                    String server_respose;

                    JSONObject jsobj1 = jsobj.getJSONObject("status");
                    String code = jsobj1.getString("code");
                    if (code.equals("success")) {

                        String name = jsobj.getString("user");


                        Intent intent = new Intent(getApplicationContext(), CardInitialization.class);
                        Bundle b = new Bundle();
                        b.putString("cardsl", Constants.SerialNo);
                        b.putString("stdCode", studentCode);
                        intent.putExtras(b);
                        startActivity(intent);


                    } else if (code.equals("error")) {
                        String message = jsobj1.getString("message");
                        Toast.makeText(CardInitialisationActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: Error in Card init check" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {

        super.onBackPressed();
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

            if (s.equals("true")) {
                Constants.SerialNo = String.valueOf(editText.getText());

                String cardserial = machineserial + String.valueOf(editText.getText());

                progressBar.setVisibility(View.VISIBLE);


                GetDetailsPHP(cardserial);
            } else {
                button.setClickable(true);
                button.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network not availbale!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
