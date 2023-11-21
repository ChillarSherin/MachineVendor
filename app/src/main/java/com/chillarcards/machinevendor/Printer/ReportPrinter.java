package com.chillarcards.machinevendor.Printer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Adapters.PrinterAdapter;
import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.DatabaseHandler;
import com.chillarcards.machinevendor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by test on 22-07-2016.
 */

public class ReportPrinter extends Activity {


//    static {
//
//        System.loadLibrary("printerfinal");
//        System.out.println("Printer Library Loaded Successfully");
//    }

    int pb;
    PrinterAdapter mAdapter;
    String total;
    Button printbill;
    String billno;
    String Successprint, TotalAmountprint, TotalTransprint;
    String localTime, formattedDate;
    TextView totalamtButton, printtext;
    int seconds;
    String times;
    ;
    String paymenttypename, balanceamount, cardetails;
    String schoolname, schoolplace, machCode;
    DatabaseHandler db = new DatabaseHandler(this);
    
    private RecyclerView mRecyclerView;
    private Activity activity;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportprinter);

        drawerArrow();
        Bundle printbundle = getIntent().getExtras();
        // Successprint = printbundle.getString("successprint");
        TotalAmountprint = String.valueOf(printbundle.getFloat("TotalAmount"));
        TotalTransprint = String.valueOf(printbundle.getInt("TotalTransaction"));

//        billno=printbundle.getString("billno");
        


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        schoolname = editor.getString("schoolname", "");
        schoolplace = editor.getString("schoolplace", "");
        machCode = editor.getString("schlMachCode", "");

        System.out.println("CODMOBTECH: " + schoolname + " " + schoolplace);


        printtext = (TextView) findViewById(R.id.transtotal);

        totalamtButton = (TextView) findViewById(R.id.transtotalamount);
        // printtext.setText(CommonprintString);


//        total = Constants.TotalAmount;
        totalamtButton.setText("  TOTAL TRANSACTION AMOUNT  :  " + TotalAmountprint);
        printtext.setText("  TOTAL NUMBER OF TRANSACTIONS   :  " + TotalTransprint);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

//        Toast.makeText(getApplicationContext(), "date : " + formattedDate, Toast.LENGTH_SHORT).show();


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        times = hour + ":" + minute;
        printbill = (Button) findViewById(R.id.print_bill);
        printbill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pt = 0;

                // printText();
                // bitmap image (hex code converted)

                // TODO Auto-generated method stub
                //  print_bmp();
                String str = "        " + schoolname + "\n"
                        + "              " + schoolplace + "\n";
                String str1 = "     DATE " + formattedDate + " "
                        + "TIME " + times + "\n";
                String str2 = "   M/c CODE: " + machCode + " " + Constants.Category + " " + "REPORT ";
                String str3 = "                                          --------------------------------------";
//                String str4 = Successprint;
                String str4 = "";
                String str6 = "Total Amount : " + TotalAmountprint + "\n";
                String str5 = "Total Trans  : " + TotalTransprint + "\n";

                String str8 = "                  \n"
                        + "--------------------------------------\n" + "Thank You...\n\n" + "Powered By\n" + "Chillar Payment Solns Pvt Ltd\n";


                System.out.println("Printing Text");

//                if (ap.prn_paperstatus() == 0) {
//
//                    if (ap.prn_lid_status() == 0) {
//                        // for setting printer font and size
//                        int ret_str = ap.prn_write_text(str, str.length(), 1);
//                        int ret_str1 = ap.prn_write_text(str1, str1.length(), 1);
//                        int ret_str2 = ap.prn_write_text(str2, str2.length(), 1);
//                        int ret_str3 = ap.prn_write_text(str3, str3.length(), 1);
//                        int ret_str4 = ap.prn_write_text(str4, str4.length(), 1);
//
//                        int ret_str5 = ap.prn_write_text(str5, str5.length(), 1);
//                        int ret_str6 = ap.prn_write_text(str6, str6.length(), 1);
//                        int ret_str8 = ap.prn_write_text(str8, str8.length(), 1);
//
//                        ap.prn_paper_feed(5);
//
//                        System.out.println("prn write text " + ret_str);
//
//                        if (ret_str == 0 && ret_str1 == 0 && ret_str2 == 0 && ret_str3 == 0 && ret_str4 == 0 && ret_str5 == 0 && ret_str6 == 0 || ret_str8 == 0
//                                ) {
//
//
//                            Constants.sales_items.clear();
////                            Intent inte=new Intent(ReportPrinter.this, StoreActivityMenu.class);
////                            inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(inte);
//                            onBackPressed();
//
//                        } else if (ret_str == -8 || ret_str1 == -8
//                                || ret_str2 == -8 || ret_str3 == -8 || ret_str4 == -8
//                                || ret_str5 == -8 || ret_str6 == -8 || ret_str8 == -8) {
//
//                            Toast.makeText(getApplicationContext(),
//                                    "Max Temperature Reached ",
//                                    Toast.LENGTH_SHORT).show();
//
//                        } else if (ret_str == -7 || ret_str1 == -7
//                                || ret_str2 == -7 || ret_str3 == -7 || ret_str4 == -7
//                                || ret_str5 == -7 || ret_str6 == -7 || ret_str8 == -7) {
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
        Constants.sales_items.clear();
//        Intent inte=new Intent(ReportPrinter.this, StoreActivityMenu.class);
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
