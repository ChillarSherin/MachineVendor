package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Codmob on 18-07-16.
 */
public class SplashScreen extends Activity {


    private static final int TIME = 1000;// 1 seconds
    String date = "", daytime = "", tt = "";
    Date strDate, strDate1;
    Date strTime, strTime1;
    String s1, s2;
    long mills;
    Dialog PopupConfrm;
    Button OkButton, NoButton;

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
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_splashscreen);

        PopupConfrm = new Dialog(this);

        new Handler().postDelayed(() -> fn_call(), TIME);


    }

    private void fn_call() {
        SharedPreferences prefs = getSharedPreferences("Chillar", MODE_PRIVATE);
        String registerFlag = prefs.getString("initialised", "");
        String logFlag = prefs.getString("logout", "");
        if (registerFlag.equals("true")) {
            if (logFlag.equals("true")) {
                System.out.println("CODMOB: Logout true");
                Intent intent = new Intent(SplashScreen.this, LogIn.class);
                startActivity(intent);
                finish();
            }
            else {
                System.out.println("CODMOB: Initialisation-TRUE");
                Intent intent = new Intent(SplashScreen.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        } else if (registerFlag.equals("")) {
            System.out.println("CODMOB: Initialisation-FALSE");
            Intent intent = new Intent(SplashScreen.this, LogIn.class);
            startActivity(intent);
            finish();

        }
    }

    private void php() {


        String tag_string_req = "otp return";

//		r_otp.php?receiverPhoneNo=9633356661&otp=930856
        String url = Constants.APP_URL + "r_server_datetime.php";
        System.out.println("Url Call_2 " + url);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
//				progressBar.setVisibility(View.GONE);
                System.out.println("Url Call_2 res " + response);
                try {
                    JSONObject jsobj = new JSONObject(response);
//					String idparse=jsobj.getString("status");
                    JSONObject sts = jsobj.getJSONObject("status");

                    String code = sts.getString("code");
                    daytime = sts.getString("daytime");


//					System.out.println("status : "+idparse);
                    System.out.println("code : " + code);
                    System.out.println("daytime : " + daytime);


                    if (code.equalsIgnoreCase("success")) {

//                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        date = getcurrentdatetime();

                        System.out.println("ijaz  :  sys date  " + date);

                        check();

                    } else if (code.equalsIgnoreCase("error")) {
//                        progressBar.setVisibility(View.GONE);
                        String msg = sts.getString("message");
                        System.out.println("message : " + msg);
                        Toast.makeText(SplashScreen.this, "Error " + msg, Toast.LENGTH_SHORT).show();
                        System.out.println("Error : " + msg);

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("ErrorVolleyTrans in 2 " + error.getMessage());

                //On Error - No Internet- Proceed without correcting time


                fn_call();


//                Toast.makeText(SplashScreen.this, "some error occurred in 2 " + error, Toast.LENGTH_LONG).show();

            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue

        App.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    public String getcurrentdatetime() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }

    private void check() {

        String CurrentString = date;
        String[] separated = CurrentString.split(" ");
        s1 = separated[1]; // this will contain "Fruit"
        String CurrentString1 = daytime;
        String[] separated1 = CurrentString1.split(" ");
        s2 = separated1[1];


        System.out.println("ijaz  :  s1 " + s1);
        System.out.println("ijaz  :  s2 " + s2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        try {
            strDate = format.parse(date);
            strDate1 = format.parse(daytime);
            strTime = format1.parse(s1);
            strTime1 = format1.parse(s2);


            mills = Math.abs(strTime1.getTime() - strTime.getTime());


            System.out.println("ijaz  :  time diff " + mills);


            System.out.println("ijaz  :  strDate " + strDate);
            System.out.println("ijaz  :  strDate1 " + strDate1);
            System.out.println("ijaz  :  strTime " + strTime);
            System.out.println("ijaz  :  strTime1 " + strTime1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (strDate.after(strDate1) || strDate.before(strDate1)) {

            //  PopupConfrm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            PopupConfrm.setContentView(R.layout.layout_timecheck);
            PopupConfrm.setTitle("Time mismatch, Please Reset");
            PopupConfrm.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            OkButton = (Button) PopupConfrm.findViewById(R.id.btnok);
            PopupConfrm.setCanceledOnTouchOutside(false);
            PopupConfrm.setCancelable(false);

            PopupConfrm.show();


            OkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("ijaz : else  : date  :" + date + "   daytime   :" + daytime);
                    startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                    PopupConfrm.dismiss();
                }
            });


//


        } else if ((mills > 1800000)) {


            PopupConfrm.setContentView(R.layout.layout_timecheck);
            PopupConfrm.setTitle("Time mismatch, Please Reset");
            PopupConfrm.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            OkButton = (Button) PopupConfrm.findViewById(R.id.btnok);
            PopupConfrm.setCanceledOnTouchOutside(false);
            PopupConfrm.setCancelable(false);
            PopupConfrm.show();


            OkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("ijaz : else  : date  :" + date + "   daytime   :" + daytime);
                    startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                    PopupConfrm.dismiss();
                }
            });
        } else {
            System.out.println("ijaz : else if : date  :" + date + "   daytime   :" + daytime);
            fn_call();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                php();



            } else {

                fn_call();
            }
        }
    }
}
