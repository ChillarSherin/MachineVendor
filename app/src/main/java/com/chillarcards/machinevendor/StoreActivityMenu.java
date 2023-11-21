package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;



/**
 * Created by Codmob on 28-07-16.
 */
public class StoreActivityMenu extends Activity {

    Button items, refund, report;
    ProgressBar progressBar;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_store_menu);

        initialiseViews();


        Bundle b=getIntent().getExtras();
        final String typeId=b.getString("typeId");
        final String typeName=b.getString("typeName");



        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), StoreActivity.class);
                Bundle b=new Bundle();
                b.putString("typeId",typeId);
                b.putString("typeName",typeName);
                i.putExtras(b);
                startActivity(i);

            }
        });

        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(getApplicationContext(), RefundActivity.class);
                Bundle b=new Bundle();
                b.putString("typeId",typeId);
                b.putString("typeName",typeName);
                i1.putExtras(b);
                startActivity(i1);

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i2 = new Intent(getApplicationContext(), ReportActivity.class);
                Bundle b = new Bundle();
                b.putString("activity", "store");
                i2.putExtras(b);
                startActivity(i2);

            }
        });


    }

    private void initialiseViews() {

        items = (Button) findViewById(R.id.itemsmenu);
        refund = (Button) findViewById(R.id.refund);
        report = (Button) findViewById(R.id.report);
        items.setVisibility(View.VISIBLE);
        refund.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        drawerArrow();

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





}



