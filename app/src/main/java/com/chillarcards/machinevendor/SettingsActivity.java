package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

/**
 * Created by Codmob on 31-10-16.
 */
public class SettingsActivity extends Activity {

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;


    CheckBox check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_activity);
        check= (CheckBox) findViewById(R.id.check);
        drawerArrow();

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check.isChecked())
                {

                    SharedPreferences.Editor editor1 = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
                    editor1.putString("isonline","true");
                    System.out.println("Check box checked");
                    editor1.commit();

                    check.setChecked(true);

                    SharedPreferences editor2 = getSharedPreferences("Chillar", MODE_PRIVATE);
                    String sd=editor2.getString("isonline","");
                    System.out.println("Check box checked test"+sd);
                }
                else
                {
                    SharedPreferences.Editor editor1 = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
                    editor1.putString("isonline","false");
                    System.out.println("Check box not checked");
                    editor1.commit();
                    check.setChecked(false);


                    SharedPreferences editor2 = getSharedPreferences("Chillar", MODE_PRIVATE);
                    String sd=editor2.getString("isonline","");

                    System.out.println("Check box checked test"+sd);
                }
            }
        });

        try {
            SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
            String s = editor.getString("isonline", "");
            if(s.equals("true")||s.equals("false"))
            {
                if (s.equals("true"))
                {
                    check.setChecked(true);
                }
                else
                {
                    check.setChecked(false);
                }
            }
            else
            {
                check.setChecked(false);
            }
        }
        catch (Exception e)
        {
            check.setChecked(false);
        }
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
