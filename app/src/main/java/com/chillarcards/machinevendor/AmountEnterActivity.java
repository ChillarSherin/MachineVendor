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

/**
 * Created by test on 14-07-2016.
 */

public class AmountEnterActivity extends Activity {

    EditText amountEdittext;
    Button payButton;
    String amountString, paytypename;
    Float amt;
    int paymentTypeId;
    String paymntnameString;


    int transtype_id;
    DatabaseHandler db;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;


    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_enteramount);

        drawerArrow();

        db = DatabaseHandler.getInstance(getApplicationContext());


        Bundle c2 = getIntent().getExtras();
        paymentTypeId = c2.getInt("paymenttypeId");
        paymntnameString = c2.getString("paytypename");

        System.out.println("CHILLLLL: PAYID2" + paymentTypeId);


        amountEdittext = (EditText) findViewById(R.id.editTextamt);
        payButton = (Button) findViewById(R.id.buttonpayamt);


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountString = String.valueOf(amountEdittext.getText());

                System.out.println("HEREREEE" + amountString);
                if (amountString.equals("")) {

                    System.out.println("HEREREEE");
                } else {


                    amt = Float.parseFloat(amountString);

                    Intent mIntent = new Intent(getApplicationContext(), RechargeNfcNew.class);
                    Bundle b2 = new Bundle();
                    b2.putInt("paymenttypeId", paymentTypeId);
                    b2.putFloat("amount", amt);
                    b2.putString("paytypename", paytypename);
                    mIntent.putExtras(b2);
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
