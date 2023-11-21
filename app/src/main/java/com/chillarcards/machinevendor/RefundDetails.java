package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Codmob on 31-07-16.
 */
public class RefundDetails extends Activity {

    TextView prevbal, currnbal;
    Button backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_refund_details);

        prevbal = (TextView) findViewById(R.id.prevbal);
        currnbal = (TextView) findViewById(R.id.currnbal);
        backbtn = (Button) findViewById(R.id.btnback);

        Bundle b = getIntent().getExtras();
        String prevbal1 = b.getString("previobal");
        String currnbal1 = b.getString("currentbal");
        final String typeId = b.getString("typeId");
        final String typeName = b.getString("typeName");

        try {
            Float prev = Float.parseFloat(prevbal1);
            prevbal.setText("Rs." + prev + "");

        } catch (NumberFormatException e) {
            prevbal.setText(" ");
        }
        try {
            Float curr = Float.parseFloat(currnbal1);
            currnbal.setText("Rs." + curr + "");
        } catch (NumberFormatException e) {
            currnbal.setText(" ");
        }


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeName.equals("PAYMENT")) {
                    Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle b=new Bundle();
                    b.putString("typeId",typeId);
                    b.putString("typeName",typeName);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), RefundActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle b=new Bundle();
                    b.putString("typeId",typeId);
                    b.putString("typeName",typeName);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }
            }
        });


    }
}




