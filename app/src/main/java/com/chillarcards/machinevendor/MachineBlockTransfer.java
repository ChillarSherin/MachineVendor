package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;

/**
 * Created by Codmob on 27-10-16.
 */
public class MachineBlockTransfer extends Activity {

    Button confirm;
    EditText std_code;
    TextView titleText;
    String User_name, Pass_word, userid, schoomachcode;
    Dialog dlg;
    ProgressBar progressBar;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    String studentCode = "";
    String cardserial = "";
    String studentID = "";
    DatabaseHandler db;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    LinearLayout linear1;
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
        setContentView(R.layout.layout_mcn_blk_transfer);

        dlg = new Dialog(this);
        db = DatabaseHandler.getInstance(getApplicationContext());

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = String.valueOf(editor.getInt("userid", 0));
        schoomachcode = editor.getString("schlMachCode", "");

        progressBar = (ProgressBar) findViewById(R.id.progress);

        drawerArrow();


        std_code = (EditText) findViewById(R.id.stdnt_code);
        confirm = (Button) findViewById(R.id.cnfrm_code);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        titleText = (TextView) findViewById(R.id.titleText);
        linear1 = (LinearLayout) findViewById(R.id.linear1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {

                    linear1.setVisibility(View.VISIBLE);
                    titleText.setText("Std Code");


                } else if (checkedId == R.id.radioButton2) {

                    linear1.setVisibility(View.GONE);

                } else {

                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == R.id.radioButton1) {

                    String stdnt = std_code.getText().toString().replace(" ", "%20");


                    if (!stdnt.equals("")) {
                        progressBar.setVisibility(View.VISIBLE);

                        GetDetailsPHP(stdnt);

                    } else {

                        Toast.makeText(MachineBlockTransfer.this, "Please enter student code!", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Intent i = new Intent(getApplicationContext(), OtherUserEnrollActivity.class);
                    startActivity(i);

                }


            }
        });

    }

    private void GetDetailsPHP(String student) {

        String tag_string_req = "getdetails";


        String URL = Constants.APP_URL + "r_student_info.php?machineCode=" + schoomachcode + "&user_id=" + userid + "&studentCode=" + student;
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
                    studentID = jsonObject.getString("studentID");
                    String standard = jsonObject.getString("standard");
                    String division = jsonObject.getString("division");
                    studentCode = jsonObject.getString("studentCode");
                    cardserial = jsonObject.getString("cardSerial");

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
                            if (cardserial != null) {

                                if (!cardserial.equals("")) {

                                    initiatePHP(studentCode, cardserial);

                                } else {

                                    initiatePHP(studentCode, "");
                                }
                            } else {

                                initiatePHP(studentCode, "");
                            }

//                            ConfirmPHP(studentID);
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
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error . Please connect to Internet.", Toast.LENGTH_SHORT).show();
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

    private void ConfirmPHP(String studentID) {


        String tag_string_req = "confirmphp";


        String URL = Constants.APP_URL + "c_block_transfer.php?machineCode=" + schoomachcode + "&user_id=" + userid + "&studentID=" + studentID;
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

                    JSONObject jsonObject = new JSONObject(response);
                    String amt = jsonObject.getString("amount");
                    String new_card = jsonObject.getString("new_cardSerial");

                    Intent i = new Intent(getApplicationContext(), MachineBlkTrnsferNFC.class);
                    Bundle b = new Bundle();
                    b.putString("amount", amt);
                    b.putString("cardsl", new_card);
                    b.putString("stdCode", studentCode);
                    i.putExtras(b);
                    startActivity(i);


                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("status");
                        String code = jsonObject1.getString("code");
                        if (code.equals("error")) ;
                        {
                            String message = jsonObject1.getString("message");

                            if (message.equals("Please update all transactions")) {
                                Toast.makeText(getApplicationContext(), "No success transactions found for this student. Please update the database and then try.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Some error occurred . please try again", Toast.LENGTH_SHORT).show();
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

    private void initiatePHP(String studentCode, final String cardserial) {

        String tag_string_req = "initiatePHP";

        String URL = Constants.APP_URL + "c_initiate_card_transfer.php?machineCode=" + schoomachcode + "&user_id=" + userid + "&studentCode=" + studentCode;

        System.out.println("URL  " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT initiatePHP ", response.toString());
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());

//{"status":{"code":"success","message":"Successfully initiated card transfer"}}
//{"pending_machines":[{"MachineCode":"CST050","machineSerial":"000000000002"},{"MachineCode":"CST076","machineSerial":"111608169717"},{"MachineCode":"CST077","machineSerial":"111608169718"}],"status":{"code":"success","message":"Waiting for updating all the machines"}}
//{"status":{"code":"success","message":"Ready for card transfer"}}

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("status");

                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");

                    if (code.equals("success")) {

                        if (message.equals("Successfully initiated card transfer")) {


                            if (!cardserial.equals("")) {

                                ArrayList<Blocked_Cards_Info> blocked_cards_infos = new ArrayList<Blocked_Cards_Info>();

                                blocked_cards_infos.clear();

                                Blocked_Cards_Info blockcards = new Blocked_Cards_Info(0, cardserial);

                                blocked_cards_infos.add(blockcards);
                                int status = db.addBlockCardInfo(blocked_cards_infos);

                                if (status == 0) {

                                    Toast.makeText(MachineBlockTransfer.this, "Successfully initiated card transfer", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to write to db", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(MachineBlockTransfer.this, "ERROR- Userserial not Attached!", Toast.LENGTH_SHORT).show();
                            }

                        } else if (message.equals("Waiting for updating all the machines")) {

                            Toast.makeText(MachineBlockTransfer.this, "Waiting for updating all the machines to be server updated.", Toast.LENGTH_SHORT).show();


                            JSONArray pendingMachines = jsonObject.getJSONArray("pending_machines");

                            String machines = "";

                            for (int i = 0; i < pendingMachines.length(); ++i) {

                                JSONObject jsonObject2 = pendingMachines.getJSONObject(i);
                                String MachineCode = jsonObject2.getString("MachineCode");
                                String machineSerial = jsonObject2.getString("machineSerial");

//                                Toast.makeText(MachineBlockTransfer.this, MachineCode+" - "+machineSerial, Toast.LENGTH_SHORT).show();

                                machines = machines + MachineCode + " - " + machineSerial + "\n";

                            }

                            alertbox1(machines);

                        } else if (message.equals("Ready for card transfer")) {

                            ConfirmPHP(studentID);

                        }

                    } else {


                    }


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
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
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

    public void alertbox1(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                MachineBlockTransfer.this);
        alert.setTitle("Machines to be updated : ");

        alert.setCancelable(true);
        alert.setMessage(msg);

        DialogInterface.OnClickListener listener;
        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        dialog.dismiss();


                    }
                });

        alert.show();
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
                try {
                    String stdnt = std_code.getText().toString().replace(" ", "%20");


                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == radioButton1.getId()) {

                        GetDetailsPHP(stdnt);

                    } else if (selectedId == radioButton2.getId()) {


                    } else {


                    }


                } catch (Exception e) {
                    System.out.println("CHILLERe: Exceptions here");
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Network not availbale!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
