package com.chillarcards.machinevendor.Printer;

/**
 * Created by Codmob on 19-09-16.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.chillarcards.machinevendor.Adapters.PrinterAdapter;
import com.chillarcards.machinevendor.DatabaseHandler;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.R;


public class PaymentBillPrinter extends Activity {


    static {

        System.loadLibrary("printerfinal");
        System.out.println("Printer Library Loaded Successfully");
    }

    int pb;
    PrinterAdapter mAdapter;
    String total;
    Button printbill;
    DatabaseHandler db;
    String CommonprintString, cardserial;
    Float prevbal, curbal;
    String localTime, formattedDate;
    TextView totalamtButton, printtext;
    int seconds;
    String times;
    String trans_id;
    String schoolname, schoolplace, User_name;
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> quantity = new ArrayList<String>();
    private ArrayList<String> price = new ArrayList<String>();
    private ArrayList<String> amount = new ArrayList<String>();
    private RecyclerView mRecyclerView;
    private Activity activity;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment_printer);

        db = DatabaseHandler.getInstance(getApplicationContext());

        drawerArrow();


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        schoolname = editor.getString("schoolname", "");
        schoolplace = editor.getString("schoolplace", "");
        User_name = editor.getString("username", "");


        Bundle printbundle = getIntent().getExtras();
//        CommonprintString = printbundle.getString("print");
        trans_id = printbundle.getString("trans_id");

        List<Success_Transaction> successtransactions = db.getAllsuccessbyBill(trans_id);
        for (Success_Transaction usp : successtransactions) {
            String testpermissionnew = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                    "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                    + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                    "  Server timestamp  " + usp.getserver_timestamp();
            System.out.println("CODMOB: PrintBillActivity SxsTrnsxn " + testpermissionnew);

            cardserial = usp.getcard_serial();
            prevbal = usp.getprevious_balnce();
            curbal = usp.getcurrent_balance();


            System.out.println("CODMOB: PrintBillActivity CARDSERIAL " + cardserial);


        }

        CommonprintString = "     TransID    " + trans_id + "\n" +
                "     Username   " + User_name + "\n" +
                "     CardSer    " + cardserial + "\n" +
                "     Amount     " + (prevbal - curbal) + "\n" +
                "     PrevBal    " + prevbal + "\n" +
                "     CurrBal    " + curbal + "\n";


        


        printtext = (TextView) findViewById(R.id.totalpriceID);

        totalamtButton = (TextView) findViewById(R.id.totalamount);
        printtext.setText(CommonprintString);
//        totalamtButton.setText(Balance);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

//        Toast.makeText(getApplicationContext(), "date : " + formattedDate, Toast.LENGTH_SHORT).show();


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        times = hour + ":" + minute;
//        Toast.makeText(getApplicationContext(), "" +times, Toast.LENGTH_SHORT).show();

//        totalamtButton.setText(" TOTAL : " + Balance);


        printbill = (Button) findViewById(R.id.print_bill);
        printbill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pt = 0;

                // printText();
                // printing format
                // TODO Auto-generated method stub
                //  print_bmp();
                String str = "        " + schoolname + "\n"
                        + "              " + schoolplace + "\n";
                String str1 = "          DATE      " + formattedDate + "\n"
                        + "          TIME      " + times + "\n";
                String str2 = "    PAYMENT BILL \n" + "---------------------\n";

                String str3 = CommonprintString;
                String str4 = "";
                String str5 = "                  \n"
                        + "--------------------------------------\n" + "*Inclusive of all taxes\n" + "Thank You...\n\n" + "Powered By\n" + "Chillar Payment Solns Pvt Ltd\n";


                System.out.println("Printing Text");

//                if (ap.prn_paperstatus() == 0) {
//
//                    if (ap.prn_lid_status() == 0) {
//
//                        // for setting printer font and size
//                        int ret_str = ap.prn_write_text(str, 7, 1);
//                        int ret_str1 = ap.prn_write_text(str1, 7, 1);
//                        int ret_str2 = ap.prn_write_text(str2, 7, 2);
//                        int ret_str3 = ap.prn_write_text(str3, 7, 1);
//                        int ret_str4 = ap.prn_write_text(str4, 7, 1);
//                        int ret_str5 = ap.prn_write_text(str5, 7, 1);
//
//                        ap.prn_paper_feed(6);
//
//                        System.out.println("prn write text " + ret_str);
//
//                        if (ret_str == 0 && ret_str1 == 0 && ret_str2 == 0 && ret_str3 == 0 && ret_str4 == 0 && ret_str5 == 0
//                                ) {
//
//                            Toast.makeText(getApplicationContext(),
//                                    "Print Text Success ", Toast.LENGTH_SHORT)
//                                    .show();
//
//                        } else if (ret_str == -8 || ret_str1 == -8
//                                || ret_str2 == -8 || ret_str3 == -8 || ret_str4 == -8
//                                || ret_str5 == -8) {
//
//                            Toast.makeText(getApplicationContext(),
//                                    "Max Temperature Reached ",
//                                    Toast.LENGTH_SHORT).show();
//
//                        } else if (ret_str == -7 || ret_str1 == -7
//                                || ret_str2 == -7 || ret_str3 == -7 || ret_str4 == -7
//                                || ret_str5 == -7) {
//
//                            Toast.makeText(getApplicationContext(),
//                                    "Low Voltage.Connect Adapter ",
//                                    Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    } else {
//                        Toast.makeText(getApplicationContext(),
//                                "Lid Not Closed ", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Paper Not Present ", Toast.LENGTH_SHORT).show();
//                }

            }
        });


    }

    protected void printText() {
        // TODO Auto-generated method stub
        AlertDialog.Builder alert = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_DARK);
        alert.setTitle("Printer");
        alert.setMessage("Enter Text :");
        final EditText text = new EditText(getApplicationContext());
        alert.setView(text);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String str = text.getText().toString();
//                ap.prn_write_text(str, str.length(), 1);
//                ap.prn_paper_feed(5);
//                ap.prn_write_text(str, str.length(), 1);
//                ap.prn_paper_feed(5);

            }
        });

        alert.show();

    }

    protected void barcodePrint() {
        // TODO Auto-generated method stub

        AlertDialog.Builder alert = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_DARK);
        alert.setTitle("Barcode");
        alert.setMessage("Enter Text :");
        final EditText text = new EditText(getApplicationContext());
        alert.setView(text);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                ap.prn_code128_barcode(text.getText().toString());
//                ap.prn_paper_feed(5);
            }
        });

        alert.show();

    }

    // bitmap image (hex code converted)
    public int print_bmp() {
        int ret = 0;
        int bmpbuff[] = new int[30000];
        PrintBMP bmp = new PrintBMP();

        ret = Prepare_bmp(bmpbuff, bmp.Vis_bmp, bmp.Vis_bmp.length);

      //  ret = ap.prn_write_bmp(bmpbuff, ret);

        return ret;
    }

    private int Prepare_bmp(int bmpbuff[], int bmpimage[], int length_bmpimage) {
        int bmpimage_index, bmpbuff_index;
        for (bmpimage_index = 0, bmpbuff_index = 0; bmpimage_index < length_bmpimage; bmpimage_index++) {
            if (bmpbuff_index > 29800) {
                System.out.println("********* %d\n" + bmpbuff_index);
                return -1;
            }
            if (bmpimage_index % 48 == 0)
                bmpbuff_index = bmpbuff_index + (48 * 4);

            bmpbuff[bmpbuff_index + (47 - (bmpimage_index % 48))] = (char) ~bmpimage[bmpimage_index];
            bmpbuff[bmpbuff_index + 48 + (47 - (bmpimage_index % 48))] = (char) ~bmpimage[bmpimage_index];
            bmpbuff[bmpbuff_index + 96 + (47 - (bmpimage_index % 48))] = (char) ~bmpimage[bmpimage_index];
            bmpbuff[bmpbuff_index + 48 + 96 + (47 - (bmpimage_index % 48))] = (char) ~bmpimage[bmpimage_index];
        }
        return bmpbuff_index;
    }

    public int print_blocks() {
        int ret = 0;
        int bmpbuff[] = new int[30000];
        PrintBMP bmp = new PrintBMP();

        ret = Prepare_bmp(bmpbuff, bmp.block_bmp, bmp.block_bmp.length);

      //  ret = ap.prn_write_bmp(bmpbuff, ret);

        return ret;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
              // ap.prn_close();

        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent inte=new Intent(PaymentPrinter.this, PaymentActivity.class);
//        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(inte);
        finish();
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
