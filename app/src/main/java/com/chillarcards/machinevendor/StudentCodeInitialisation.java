package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

/**
 * Created by Codmob on 27-10-16.
 */
public class StudentCodeInitialisation extends Activity {

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    Button confirm;
    EditText std_code;

    String User_name,Pass_word,userid,schoomachcode;
    Dialog dlg;
    ProgressBar progressBar;
    String studentCode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mcn_blk_transfer);

        dlg = new Dialog(this);

        TextView header= (TextView) findViewById(R.id.textView15);
        header.setText("Card Initialisation");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid= String.valueOf(editor.getInt("userid",0));
        schoomachcode= editor.getString("schlMachCode", "");

        progressBar= (ProgressBar) findViewById(R.id.progress);

        drawerArrow();



        std_code= (EditText) findViewById(R.id.stdnt_code);
        confirm= (Button) findViewById(R.id.cnfrm_code);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stdnt=std_code.getText().toString().replace(" ","%20");
                progressBar.setVisibility(View.VISIBLE);




                GetDetailsPHP(stdnt);

            }
        });

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

    private void GetDetailsPHP(String student) {

        String tag_string_req="getdetails";


        String URL=Constants.APP_URL+"r_student_info.php?machineCode="+schoomachcode+"&user_id="+userid+"&studentCode="+student;
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
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String studentName=jsonObject.getString("studentName");
                    final String studentID=jsonObject.getString("studentID");
                    String standard=jsonObject.getString("standard");
                    String division=jsonObject.getString("division");
                    studentCode=jsonObject.getString("studentCode");

                    dlg.setContentView(R.layout.popup_student_details);
                    dlg.setTitle("Student Details");
                    dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button OkButton = (Button) dlg.findViewById(R.id.btnok);
                    Button NoButton = (Button) dlg.findViewById(R.id.btnno);
                    TextView name= (TextView) dlg.findViewById(R.id.name);
                    TextView std= (TextView) dlg.findViewById(R.id.std);

                    name.setText(studentName);
                    std.setText(standard+" : "+division);

                    try{

                        dlg.show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    OkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.cancel();

                            progressBar.setVisibility(View.VISIBLE);

                            ConfirmPHP(studentID);



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
                        JSONObject jsonObject=new JSONObject(response);

                        JSONObject jsonObject1=jsonObject.getJSONObject("status");
                        String code=jsonObject1.getString("code");
                        if(code.equals("error"));{
                            String message=jsonObject1.getString("message");
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
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

        String tag_string_req="confirmphp1";

        String URL=Constants.APP_URL+"r_student_card_initilization.php?machineCode="+schoomachcode+"&user_id="+userid+"&studentID="+studentID;
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

                    JSONObject jsonObject=new JSONObject(response);
//                    String amt=jsonObject.getString("amount");
                    String new_card=jsonObject.getString("cardSerial");

                    if(Constants.adminFlag==1){
                        Intent intent=new Intent(getApplicationContext(),AdminCardInitProccess.class);
                        Bundle b=new Bundle();
//                    b.putString("amount",amt);
                        b.putString("cardsl",new_card);
                        b.putString("stdCode",studentCode);
                        intent.putExtras(b);
                        startActivity(intent);
                    }else if(Constants.adminFlag==2){
//                        Intent i=new Intent(getApplicationContext(),StudentCodeInitNFC.class);
//                        Bundle b=new Bundle();
////                    b.putString("amount",amt);
//                        b.putString("cardsl",new_card);
//                        b.putString("stdCode",studentCode);
//                        i.putExtras(b);
//                        startActivity(i);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject jsonObject=new JSONObject(response);

                        JSONObject jsonObject1=jsonObject.getJSONObject("status");
                        String code=jsonObject1.getString("code");
                        if(code.equals("error"));{
                            String message=jsonObject1.getString("message");
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                System.out.println("CODMOB: Error in AnnouncementView"+error.getMessage());
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

}
