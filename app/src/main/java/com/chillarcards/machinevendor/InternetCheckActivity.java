package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Codmob on 18-09-17.
 */

public class InternetCheckActivity extends Activity {

    ProgressBar progressBar;
    String type;

    DatabaseHandler db ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_check);

        db = DatabaseHandler.getInstance(getApplicationContext());

        Bundle b=getIntent().getExtras();
        type= b.getString("type");



        intialiseUIElements();


        new reachableURL().execute();


    }

    private void intialiseUIElements() {

        progressBar= (ProgressBar) findViewById(R.id.progbar);


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
                try {

                    if(type.equals("1")){

                       /* if(db.getAllsuccesstoup().size()>0||
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
                                db.getAllCreateLeaveToUp().size()>0){*/

                            Intent i=new Intent(getApplicationContext(),ServerUploadActivity.class);
                            startActivity(i);
                            finish();

                        /*}else {

                            Toast.makeText(InternetCheckActivity.this, "Fully Uploaded.", Toast.LENGTH_SHORT).show();
                            finish();
                        }*/



                    }else if(type.equals("2")){

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
                                db.getAllCreateLeaveToUp().size()>0){

                            Toast.makeText(getApplicationContext(),"Database Not uploaded completely. Please upload and then try again.",Toast.LENGTH_LONG).show();

                            finish();

                        }else {

                            Intent i=new Intent(getApplicationContext(),ReInitializeActivity.class);
                            startActivity(i);
                            finish();
                        }




                    }else if(type.equals("3")){

                        Intent i=new Intent(getApplicationContext(),BlockCardsDownloadActivity.class);
                        startActivity(i);
                        finish();

                    }else if(type.equals("4")){

                        Intent i=new Intent(getApplicationContext(),ParentStudentDownloadActivity.class);
                        startActivity(i);
                        finish();

                    }



                } catch (Exception e) {
                    System.out.println("CHILLERe: Exceptions here");
                    e.printStackTrace();
                    finish();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Network not availbale!",Toast.LENGTH_SHORT).show();
                finish();

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

    @Override
    public void onBackPressed() {
        return;
    }
}
