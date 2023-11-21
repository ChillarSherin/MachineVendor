package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by test on 27-08-2016.
 */
public class Payment_Main extends Activity {

    Button paybutton;
    EditText amountedt;
    Float amountString;
    String typeName,typeId;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;


    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment_main);

        drawerArrow();

        Bundle b = getIntent().getExtras();
        typeName = b.getString("typeName");
        typeId = b.getString("typeId");

        paybutton = (Button) findViewById(R.id.buttonpay);
        amountedt = (EditText) findViewById(R.id.editTextamount);

        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (amountedt.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "please fill the fields to continue", Toast.LENGTH_SHORT).show();

                } else {

                    Intent mIntent = new Intent(getApplicationContext(), AmountNew.class);
                    Bundle b1 = new Bundle();
                    b1.putString("typeId",typeId);
                    b1.putString("typeName",typeName);
                    b1.putFloat("amount", Float.parseFloat(amountedt.getText().toString()));
                    mIntent.putExtras(b1);
                    startActivity(mIntent);
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


}
