package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.Adapters.AttendenceAdapter;
import com.chillarcards.machinevendor.ModelClass.Attendance_Type;


/**
 * Created by test on 11-07-2016.
 */

public class AttendanceActivity extends Activity {

    private final List<String> attendance_type_name = new ArrayList<>();
    private final List<String> attendance_id = new ArrayList<>();
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int tama_on;
    ProgressBar progressBar;
    AttendenceAdapter mAdapter;
    DatabaseHandler db;
    private RecyclerView mRecyclerView;
    private Activity activity;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_attendance);

        db = DatabaseHandler.getInstance(getApplicationContext());

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        drawerArrow();


        mRecyclerView = (RecyclerView) findViewById(R.id.list2);


        new FieldOnTask().execute();
        //LISTING ATTENDENCE TYPE NAME AND ATTENDEBCE TYPE ID


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

    private class FieldOnTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            List<Attendance_Type> attype = db.getAllAttendance();
            for (Attendance_Type us : attype) {
                String attypename = "id : " + us.getId() + "attendance_type_name : " + us.getAttendance_type_name();
                String id = String.valueOf(us.getId());

                attendance_id.add(id);
                attendance_type_name.add(us.getAttendance_type_name());
                System.out.println("CHILLAR Todos : " + attypename);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);

            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new AttendenceAdapter(attendance_id, attendance_type_name, activity, R.layout.attendencelitsitem, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);


        }
    }


}

