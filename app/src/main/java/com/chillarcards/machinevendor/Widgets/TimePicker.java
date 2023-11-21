package com.chillarcards.machinevendor.Widgets;

/**
 * Created by Codmob on 30-07-16.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.R;


public class TimePicker extends Activity {
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 1);
        getWindow().setLayout(screenWidth, WindowManager.LayoutParams.WRAP_CONTENT);

        final android.widget.TimePicker Tp = (android.widget.TimePicker) findViewById(R.id.timePicker1);
        Tp.setIs24HourView(false);

        Submit = (Button) findViewById(R.id.btn_tp);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(
//                        TimePicker.this,
//                        (Tp.getCurrentHour() % 12 == 0 ? 12 : Tp
//                                .getCurrentHour() % 12)
//                                + " : "
//                                + Tp.getCurrentMinute()
//                                + " "
//                                + ((Tp.getCurrentHour() > 11 && Tp
//                                .getCurrentHour() < 24) ? "PM" : "AM"),
//                        Toast.LENGTH_SHORT).show();
                System.out.println("ConstantValueofTimePickr = " + Constants.TimePickerSel);

                if (Constants.TimePickerSel == 1) {
                    Constants.FromTime = (Tp.getCurrentHour() % 12 == 0 ? 12 : Tp
                            .getCurrentHour() % 12)
                            + ":"
                            + Tp.getCurrentMinute()
                            + ((Tp.getCurrentHour() > 11 && Tp
                            .getCurrentHour() < 24) ? "PM" : "AM");

                } else if (Constants.TimePickerSel == 2) {

                    Constants.ToTime = (Tp.getCurrentHour() % 12 == 0 ? 12 : Tp
                            .getCurrentHour() % 12)
                            + ":"
                            + Tp.getCurrentMinute()
                            + ((Tp.getCurrentHour() > 11 && Tp
                            .getCurrentHour() < 24) ? "PM" : "AM");
                }

                finish();
                //  onBackPressed();

            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
