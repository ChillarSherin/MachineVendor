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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;



/**
 * Created by Codmob on 12-07-16.
 */
public class FeeActivity extends Activity {

    //    DatabaseHandler db = new DatabaseHandler(this);
    DatabaseHandler db;
    EditText editText;
    Button button;
    Float amt;
    String amount;
    ProgressBar progressBar;
    
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int tama_on;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fee_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());


        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        button = (Button) findViewById(R.id.getamt);
//        button.setEnabled(false);
//        button.setClickable(false);
//        new FieldOnTask().execute();

        drawerArrow();

        final String trans_id = db.lastTransID();


        editText = (EditText) findViewById(R.id.amt);

        // PASSING THE FEE AMOUNT TO NFCPAY ACTIVITY

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = String.valueOf(editText.getText());

//
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "please fill the amount", Toast.LENGTH_SHORT).show();
                } else {
                    Intent mIntent = new Intent(getApplicationContext(), NfcPay.class);
                    Bundle b = new Bundle();

                    b.putFloat("amount", Float.parseFloat(amount));
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.putExtras(b);
                    getApplicationContext().startActivity(mIntent);
                }


                System.out.println("CHILLAR BillNo" + db.getBillno());
                System.out.println("CHILLAR Amt" + amt);


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
//        new FieldOffTask().execute();
        super.onBackPressed();
    }

//    private class FieldOnTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            try {
//                ret_poweron = SecondActivity.rfidService.rfid_poweron();
//                System.out.println("RFID PowerOn :" + ret_poweron);
//
//                if (ret_poweron == 0 || ret_poweron == 2) {
//
////                Toast.makeText(getApplicationContext(), "Module open success", Toast.LENGTH_SHORT).show();
//
//                    ret_fieldon = SecondActivity.rfidService.rfid_rffield_on();
//
//                    System.out.println("RFID FieldOn :" + ret_fieldon);
//
//                    tama_on = apis.tama_open();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Module open Failed", Toast.LENGTH_SHORT).show();
//                    System.out.println("RFID Module open Failed");
//                }
//
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            progressBar.setVisibility(View.GONE);
//            button.setEnabled(true);
//            button.setClickable(true);
//
//        }
//    }
//
//    private class FieldOffTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            apis.tama_close();
//
//            try {
//                int ret_fieldoff = SecondActivity.rfidService.rfid_rffield_off();
//
//                System.out.println("Field Off:" + ret_fieldoff);
//
//                int ret_poweroff = SecondActivity.rfidService.rfid_poweroff();
//
//                System.out.println("PowerOFF :" + ret_poweroff);
//
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
////        @Override
////        protected void onPreExecute() {
////
////            progressBar.setVisibility(View.VISIBLE);
////        }
//
//    }

}
