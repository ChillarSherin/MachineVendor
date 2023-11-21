package com.chillarcards.machinevendor;

/**
 * Created by Codmob on 11-07-16.
 */

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

import com.chillarcards.machinevendor.Adapters.Payment_Type_Adapter;
import com.chillarcards.machinevendor.ModelClass.Payment_Type;


/**
 * Created by test on 07-07-2016.
 */

public class Payment_type_names extends Activity {


    private final List<String> pay_type = new ArrayList<>();
    private final List<String> pay_id = new ArrayList<>();
    Payment_Type_Adapter mAdapter;
    int transtype_id;

    ProgressBar progressBar;
    DatabaseHandler db;
    private RecyclerView mRecyclerView;
    private Activity activity;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymnttype_names);

        db = DatabaseHandler.getInstance(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.list2);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);


        drawerArrow();

        try {
            new Loader().execute();
        } catch (IllegalStateException e) {
            new Loader().execute();
        }

        //LISTING THE PAYMENT TYPES


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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


    private class Loader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new Payment_Type_Adapter(pay_id, pay_type, activity, R.layout.paymenttype_listitem, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);

            progressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Payment_Type> paytyp = db.getAllpay();
            for (Payment_Type us : paytyp) {
                String paymenttype = "id : " + us.getid() + " pay type name : " + us.getpayment_type_name();
                String id = String.valueOf(us.getid());

                pay_id.add(id);
                pay_type.add(us.getpayment_type_name());
                System.out.println("CHILLAR Todos : " + paymenttype);
            }

            return null;
        }
    }



}






