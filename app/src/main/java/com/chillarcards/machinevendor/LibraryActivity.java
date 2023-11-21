package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



/**
 * Created by Codmob on 12-07-16.
 */
public class LibraryActivity extends Activity {

    EditText bukid;
    Button lib_btn;
    String trans_id;
    int transtype_id;
    String localTime, formattedDate;
    int userid;
    String User_name, Pass_word;
    RadioButton radioButton;
    RadioGroup radioGroup;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int tama_on;
    ProgressBar progressBar;
    DatabaseHandler db;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;


    //    DatabaseHandler db = new DatabaseHandler(this);
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_libraryactivity);

        db = DatabaseHandler.getInstance(getApplicationContext());

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        lib_btn = (Button) findViewById(R.id.libbutn);
//        lib_btn.setEnabled(false);
//        lib_btn.setClickable(false);
//        new FieldOnTask().execute();

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = editor.getInt("userid", 0);


        drawerArrow();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

//        Toast.makeText(getApplicationContext(), "date : "+formattedDate, Toast.LENGTH_SHORT).show();


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        localTime = date.format(currentLocalTime);
//        Toast.makeText(getApplicationContext(), "time : "+localTime, Toast.LENGTH_SHORT).show();

//        userid=db.getUserID(User_name);

//        Toast.makeText(getApplicationContext(), "user id : "+userid, Toast.LENGTH_SHORT).show();
        System.out.println("CHILLAR userid" + userid);

        trans_id = db.lastTransID();


        bukid = (EditText) findViewById(R.id.bukid);


        lib_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bukid.getText().length() > 15 || bukid.getText().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Enter valid BookId (Max 15 chars.)", Toast.LENGTH_SHORT).show();

                } else {

                    int issueReturn = 2;
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (radioGroup.getCheckedRadioButtonId() == -1) {

                        Toast.makeText(getApplicationContext(), "Select an option!", Toast.LENGTH_SHORT).show();

                    } else {

                        radioButton = (RadioButton) findViewById(selectedId);


                        if (radioButton.getText().equals("ISSUE")) {

                            issueReturn = 1;

                        } else if (radioButton.getText().equals("RETURN")) {

                            issueReturn = 0;

                        }

                        String bookid = String.valueOf(bukid.getText());

                        transtype_id = db.getTransTypID("LIBRARY");


                        Intent i = new Intent(getApplicationContext(), LibraryNFC.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("bookid", bookid);
                        bundle.putInt("issuereturn", issueReturn);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
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
    public void onBackPressed() {
//        new FieldOffTask().execute();
        super.onBackPressed();
    }

//    private class FieldOnTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            try {
//                ret_poweron = SecondActivity.rfidService.rfid_poweron();
//                System.out.println("RFID PowerOn :" + ret_poweron);
//
//                if (ret_poweron == 0 || ret_poweron == 2) {
//
////                Toast.makeText(getApplicationContext(), "Module open success", Toast.LENGTH_SHORT).show();
//
//                    ret_fieldon = SecondActivity.rfidService.rfid_rffield_on();
//
//                    System.out.println("RFID FieldOn :" + ret_fieldon);
//
//                    tama_on = apis.tama_open();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Module open Failed", Toast.LENGTH_SHORT).show();
//                    System.out.println("RFID Module open Failed");
//                }
//
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            progressBar.setVisibility(View.GONE);
//            lib_btn.setEnabled(true);
//            lib_btn.setClickable(true);
//
//        }
//    }
//
//    private class FieldOffTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            apis.tama_close();
//
//            try {
//                int ret_fieldoff = SecondActivity.rfidService.rfid_rffield_off();
//
//                System.out.println("Field Off:" + ret_fieldoff);
//
//                int ret_poweroff = SecondActivity.rfidService.rfid_poweroff();
//
//                System.out.println("PowerOFF :" + ret_poweroff);
//
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
////        @Override
////        protected void onPreExecute() {
////
////            progressBar.setVisibility(View.VISIBLE);
////        }
//
//    }


}
