package com.chillarcards.machinevendor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Adapters.HomePageAdapter;
import com.chillarcards.machinevendor.ModelClass.TransactionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SecondActivity extends Activity {


    Context mContext;
    private static final int REQUEST_STORAGE_WRITE_SDCARD_open = 90;


    DatabaseHandler db;


    private HomePageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private final List<String> trans_id = new ArrayList<>();
    private final List<String> trans_type_name = new ArrayList<>();
    Activity activity;
    TextView schoolTitleTxt;
    String schoolName;
    ProgressBar progressBar;

    int userIdInt;
    String userName, schMachCode;
    TextView sDateToday;
    ImageView menuIcon;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.new_layout);

        db = DatabaseHandler.getInstance(getApplicationContext());
        activity = this;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE_SDCARD_open);

        mContext = this;

        initialiseViews();
        sharedPrefs();
        logUser();
        handler = new Handler();
        // Start the clock update task
        updateClock();

        schoolName = db.getSchoolName();
        schoolTitleTxt.setText(schoolName);
        menuIcon.setOnClickListener(v -> {
            LayoutInflater layoutInflater
                    = (LayoutInflater) getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup_menu, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.showAsDropDown(menuIcon, 50, -30);

            LinearLayout inviteLay = popupView.findViewById(R.id.invite);

            TextView versiontext = popupView.findViewById(R.id.version);
            versiontext.setText(Checkversion() + "");

            inviteLay.setOnClickListener(v1 -> {

                SharedPreferences.Editor editor = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
                editor.remove("username");
                editor.remove("password");
                editor.remove("userid");
                editor.putString("logout", "true");
                editor.commit();

                popupWindow.dismiss();

                Intent in = new Intent(getApplicationContext(), LogIn.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            });
        });

        try {
            new MenuTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String Checkversion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return info.versionName;
    }

    private void sharedPrefs() {

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        userName = editor.getString("username", "");
        userIdInt = editor.getInt("userid", 0);
        schMachCode = editor.getString("schlMachCode", "");
    }

    private void initialiseViews() {

        sDateToday = findViewById(R.id.today_date);
        schoolTitleTxt = findViewById(R.id.textView16);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = findViewById(R.id.list);
        menuIcon = findViewById(R.id.menu);

    }

    private void logUser() {

    }


    @Override
    protected void onResume() {

        String appVersion = Checkversion();

        SharedPreferences.Editor editor1 = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
        editor1.putString("appVersion", appVersion);
        editor1.commit();

        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MenuTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {


            try {
                List<TransactionType> device = db.getAllToDosByTag(userIdInt);

                for (TransactionType us : device) {
                    String test = "Trans ID : " + us.getTrans_type_id() + " Trans Type Nme : " + us.getTransaction_type_name();
                    String id = String.valueOf(us.getTrans_type_id());

                    trans_id.add(id);
                    trans_type_name.add(us.getTransaction_type_name());
                    System.out.println("CHILLAR Todos of user1: " + test);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new HomePageAdapter(trans_id, trans_type_name, activity, R.layout.list_item, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);

            progressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

private void updateClock() {
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            updateTime();

            handler.postDelayed(this, 1000);
        }
    }, 1000); // 1000 milliseconds = 1 second
}

    private void updateTime() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(currentDate);

        sDateToday.setText(formattedTime);
    }
    @Override
    protected void onRestart() {
        System.out.println("********************onRestart");
        super.onRestart();
    }
}
