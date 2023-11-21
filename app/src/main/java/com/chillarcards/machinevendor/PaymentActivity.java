package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;




/**
 * Created by test on 14-07-2016.
 */

public class PaymentActivity extends Activity {

    Button paymntButton, refundButton;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    String typeId,typeName;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment);

        drawerArrow();

        initialise();

        paymntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Payment_Main.class);
                Bundle b=new Bundle();
                b.putString("typeId",typeId);
                b.putString("typeName",typeName);
                i.putExtras(b);
                startActivity(i);

            }
        });



        refundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), PaymentRefund.class);
                Bundle b=new Bundle();
                b.putString("typeId",typeId);
                b.putString("typeName",typeName);
                i.putExtras(b);
                startActivity(i);

            }
        });


    }

    private void initialise() {

        progressBar = (ProgressBar) findViewById(R.id.progress);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        paymntButton = (Button) findViewById(R.id.buttonpay);
        refundButton = (Button) findViewById(R.id.buttonrefund);

        Bundle b=getIntent().getExtras();
        typeId=b.getString("typeId");
        typeName=b.getString("typeName");

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
