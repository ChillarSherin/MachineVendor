package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Codmob on 27-10-16.
 */
public class BlockTransferActivity extends Activity {

    Button manual, auto;
    String typename;
    int typid;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blk_trnsfer_activity);

        Bundle b = getIntent().getExtras();
        typid = b.getInt("paymenttypeId");
        typename = b.getString("paytypename");

        drawerArrow();

        manual = (Button) findViewById(R.id.manual);
        auto = (Button) findViewById(R.id.auto);

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent1 = new Intent(getApplicationContext(), BlockedcardsTransfer.class);
                Bundle c2 = new Bundle();
                c2.putInt("paymenttypeId", typid);
                c2.putString("paytypename", typename);
                mIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent1.putExtras(c2);
                startActivity(mIntent1);
            }
        });

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MachineBlockTransfer.class);
                startActivity(i);
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
