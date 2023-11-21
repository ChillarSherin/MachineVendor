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
import com.chillarcards.machinevendor.ModelClass.Device_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by test on 05-08-2016.
 */
public class AdmincardInitialisation extends Activity {

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    Button button;
    EditText editText;
    TextView textView1,textView2,textView3;

    String schoolname,schoolid,cardnumbers;
    DatabaseHandler db ;
    String User_name,Pass_word;
    int userid;
    int useridinteger;
    String schooid,schoomachcode,machineserial;

    
    int ret_poweron;
    int ret_fieldon;

    int tardtc;
    int tama_on;
    ProgressBar progressBar;
    Dialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState)  throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admincardinit);

        //admin Flag
        db = DatabaseHandler.getInstance(getApplicationContext());

        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        button= (Button) findViewById(R.id.adminwritebtn);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid= editor.getInt("userid",0);
        schooid = editor.getString("schoolId", "");
        schoomachcode= editor.getString("schlMachCode", "");
        machineserial= editor.getString("machineserial", "");

        drawerArrow();

        editText= (EditText) findViewById(R.id.admincardserial);
        textView1= (TextView) findViewById(R.id.adminschoolid);
        textView2= (TextView) findViewById(R.id.admincardnumber);
        textView3=(TextView)findViewById(R.id.adminschoolname);

        List<Device_Info> device_infos = db.getAlldevInfo();

        for (Device_Info us : device_infos) {
            String test = "machineid: " + us.getMachine_id() + " serialno : " + us.getSerial_no() + " school : " + us.getSchool_id() +
                    " mainurl : " + us.getMain_server_url() + " mainupload : " + us.getMain_upload_path();
            System.out.println("Device Info: " + test);

            schoolname=us.getSchoolname();
            schoolid=us.getSchool_id();
            cardnumbers=us.getSerial_no();

        }

        textView1.setText(schoolid);
        textView2.setText(cardnumbers);
        textView3.setText(schoolname);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setClickable(false);
                button.setEnabled(false);
                progressBar.setVisibility(View.GONE);
                String serialno= String.valueOf(editText.getText());



                if(serialno.length()<6||serialno.length()>6){

                    Toast.makeText(getApplicationContext(),"6 digits only!",Toast.LENGTH_SHORT).show();

                }else{

                    new reachableURL().execute();

                }


            }
        });


    }

    private class reachableURL extends AsyncTask<Void, String, String> {

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
                Constants.SerialNo=String.valueOf(editText.getText());

                String cardserial=machineserial+String.valueOf(editText.getText());

                progressBar.setVisibility(View.VISIBLE);


                CheckAlreadyInitialised(cardserial);
            }else{
                button.setClickable(true);
                button.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Network not availbale!",Toast.LENGTH_SHORT).show();
            }
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
//        new FieldOffTask().execute();

        super.onBackPressed();
    }



    private void CheckAlreadyInitialised(String cardserial) {
        System.out.println("checkinit");

        String tag_string_req="checkinitialised";


        String URL=Constants.APP_URL+"r_card_initialisation.php?machineCode="+schoomachcode+"&user_id="+userid+"&cardSerial="+cardserial;

        System.out.println("URL initialied " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());
                System.out.println("CODMOB: TESTS "+response);
                System.out.println("testf"+response.toString());
                progressBar.setVisibility(View.GONE);

                // progressDialog.dismiss();
                try {
                    JSONObject jsobj=new JSONObject(response);
                    String server_respose;
                    JSONObject jsobj1=jsobj.getJSONObject("status");
                    String code= jsobj1.getString("code");
                    if(code.equals("success")){
                        String name=jsobj.getString("user");

                        dlg.setContentView(R.layout.layout_confrmpopup);
                        dlg.setTitle("Name: "+name);
                        dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        Button OkButton = (Button) dlg.findViewById(R.id.btnok);
                        Button NoButton = (Button) dlg.findViewById(R.id.btnno);


                        dlg.show();

                        OkButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dlg.cancel();

                                Intent intent=new Intent(getApplicationContext(),AdminCardInitProccess.class);
                                startActivity(intent);


                            }
                        });


                        NoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dlg.cancel();


                            }
                        });



                    }else if(code.equals("error")){
                        String message=jsobj1.getString("message");
                        Toast.makeText(AdmincardInitialisation.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                System.out.println("CODMOB: Error in Online Recharge"+error.getMessage());
                Toast.makeText(getApplicationContext(), "Some error occurred . please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        // Adding request to request queue

        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }



}
