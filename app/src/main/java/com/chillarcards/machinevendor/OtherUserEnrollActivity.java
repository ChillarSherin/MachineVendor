package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Codmob on 15-05-18.
 */

public class OtherUserEnrollActivity extends Activity {

    RecyclerView mRecyclerView;
    ProgressBar progressBar;

    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<String> typeList = new ArrayList<String>();

    Activity activity;
    OtherUserAdapter mAdapter;

    String User_name, Pass_word, userid, schoomachcode;
    String TAG = "OtherUserEnrollActivity";
    DatabaseHandler db;
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private int FINGERID_1 = 1;
    private int FINGERID_2 = 2;
    private int MATCH_FINGER_1 = 3;
    private int MATCH_FINGER_2 = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_other_user_enroll);

        activity = this;
        db = DatabaseHandler.getInstance(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progbar);

        drawerArrow();

        progressBar.setVisibility(View.GONE);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userid = String.valueOf(editor.getInt("userid", 0));
        schoomachcode = editor.getString("schlMachCode", "");

        GetAllUserTypes();
    }


    private void GetAllUserTypes() {

        String tag_string_req = "getusertypes";

        idList.clear();
        typeList.clear();

        String URL = Constants.APP_URL + "r_user_types.php?machineUserName=" + User_name + "&machineUserPassword=" + Pass_word + "&schoolMachineCode=" + schoomachcode;

        System.out.println("URL Blocked " + URL);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TESTT", response.toString());

                System.out.println("CODMOB: TESTS " + response);
                System.out.println("testf" + response.toString());

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); ++i) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String user_type_name = jsonObject.getString("user_type_name");

                        idList.add(id);
                        typeList.add(user_type_name);


                    }


                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mAdapter = new OtherUserAdapter(schoomachcode, userid, idList, typeList, activity, R.layout.other_user_types, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   progressDialog.dismiss();
                System.out.println("CODMOB: Error in AnnouncementView" + error.getMessage());
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


    public void drawerArrow() {
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
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


    public void alertbox(String msg, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                OtherUserEnrollActivity.this);
        alert.setTitle(title);

        alert.setCancelable(false);
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


}
