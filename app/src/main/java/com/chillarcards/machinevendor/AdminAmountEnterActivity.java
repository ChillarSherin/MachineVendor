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
 * Created by test on 05-08-2016.
 */
public class AdminAmountEnterActivity extends Activity {


    EditText amountEdittext;
    Button payButton;
    String amountString,paytypename;
    Float amt;
    int paymentTypeId=1;
    String paymntnameString="CASH";


    int transtype_id;
    DatabaseHandler db;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;



    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_adminamountenter);
        drawerArrow();

        db = DatabaseHandler.getInstance(getApplicationContext());


        amountEdittext=(EditText)findViewById(R.id.admineditTextamt);
        payButton=(Button)findViewById(R.id.adminbuttonpayamt);

        //Enter amount and pass it to Adminrechargenfc same as recharge activity

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountString= String.valueOf(amountEdittext.getText());

                System.out.println("HEREREEE"+amountString);
                if (amountString.equals(""))
                {

                    System.out.println("HEREREEE");
                }
                else {


                    amt= Float.parseFloat(amountString);

                    Intent mIntent = new Intent(getApplicationContext(), AdminRechargeNFC.class);
                    Bundle b2=new Bundle();
                    b2.putInt("paymenttypeId", paymentTypeId);
                    b2.putFloat("amount", amt);
                    b2.putString("paytypename",paymntnameString);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
