package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 * Created by Codmob on 31-07-16.
 */
public class BlockTransferNFC extends Activity {

    static {

        System.loadLibrary("printerfinal");
        System.out.println("Printer Library Loaded Successfully");
    }

    TextView prevbal, currnbal, title;
    Button backbtn, printbtn;
    
    //    ProgressDialog progressDialog;
    String times, formattedDate;
    int pb;

    String schoolname, schoolplace;
    String trans_id, total;

    String flag;
    Float prev, curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocktransfer_nfc);


        prevbal = (TextView) findViewById(R.id.prevbal);
        currnbal = (TextView) findViewById(R.id.currnbal);
        backbtn = (Button) findViewById(R.id.btnback);
        printbtn = (Button) findViewById(R.id.btn_print);
        title = (TextView) findViewById(R.id.titletext);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        schoolname = editor.getString("schoolname", "");
        schoolname = "Cafe " + schoolname;
        schoolplace = editor.getString("schoolplace", "");

        Bundle b = getIntent().getExtras();
        String prevbal1 = b.getString("previobal").replaceFirst("^0+(?!$)", "");
        ;
        String currnbal1 = b.getString("currentbal").replaceFirst("^0+(?!$)", "");
        ;
        flag = b.getString("flag");
        trans_id = b.getString("transid");
        final String check = b.getString("check");
        if (flag.equals("online")) {
            title.setText("Online Recharge Successfull!");
        } else if (flag.equals("cardtransfer")) {
            title.setText("Card Transfer Successfull!");
        }

        prev = Float.parseFloat(prevbal1);
        curr = Float.parseFloat(currnbal1);

        total = String.valueOf(curr - prev);

        prevbal.setText("Rs." + prev + "");
        currnbal.setText("Rs." + curr + "");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

//        Toast.makeText(getApplicationContext(), "date : " + formattedDate, Toast.LENGTH_SHORT).show();


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        times = hour + ":" + minute;


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check.equals("1")) {
                    Intent i = new Intent(getApplicationContext(), Payment_type_names.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else if (check.equals("0")) {
                    Intent i = new Intent(getApplicationContext(), CheckoutActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });


        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printBill(curr, prev);
            }
        });


    }


    public void printBill(Float curr, Float prev) {

        try {
            int pt = 0;
            //int po = ap.prn_open();

            // printText();

            // printing format
            // TODO Auto-generated method stub
            //  print_bmp();
            String str = "     " + schoolname + "\n"
                    + "           " + schoolplace + "\n";
            String str1 = "    DATE " + formattedDate + ""
                    + " TIME " + times + "\n\n"
                    + "    TRANS ID: " + trans_id + "\n";
            String str2 = "";
            if (flag.equals("online")) {
                str2 = "   " + "ONLINE RECHARGE" + "\n";
            } else {
                str2 = "   " + "CARD TRANSFER" + "\n";
            }

            String str3 = "--------------------------------------";


//            String str4 = "" + "";
            String str5 = "Curr Bal: " + (float) curr + "\n";
            String str6 = "     Total    " + total + "\n";
            String str7 = "Prev Bal: " + (float) prev + "\n";
            String str8 = "                  \n"
                    + "--------------------------------------\n" + "Thank You...\n\n" + "Powered By\n" + "Chillar Payment Solns Pvt Ltd\n";


            System.out.println("Printing Text");

//            if (ap.prn_paperstatus() == 0) {
//                // for setting printer font and size
//                if (ap.prn_lid_status() == 0) {
//
//                    int ret_str = ap.prn_write_text(str, 7, 1);
//                    int ret_str1 = ap.prn_write_text(str1, 7, 1);
//                    int ret_str2 = ap.prn_write_text(str2, 7, 2);
//                    int ret_str3 = ap.prn_write_text(str3, 7, 1);
////                int ret_str4 = ap.prn_write_text(str4, 6, 1);
//                    int ret_str7 = ap.prn_write_text(str7, 7, 1);
//                    int ret_str5 = ap.prn_write_text(str5, 7, 2);
//                    int ret_str6 = ap.prn_write_text(str6, 7, 2);
//                    int ret_str8 = ap.prn_write_text(str8, 7, 1);
//
//                    ap.prn_paper_feed(5);
//
//                    System.out.println("prn write text " + ret_str);
//
//                    if (ret_str == 0 && ret_str1 == 0 && ret_str2 == 0 && ret_str3 == 0 && ret_str5 == 0 && ret_str6 == 0 || ret_str7 == 0 || ret_str8 == 0
//                            ) {
//
//
//                    } else if (ret_str == -8 || ret_str1 == -8
//                            || ret_str2 == -8 || ret_str3 == -8
//                            || ret_str5 == -8 || ret_str6 == -8 || ret_str7 == -8 || ret_str8 == -8) {
//
//                        Toast.makeText(getApplicationContext(),
//                                "Max Temperature Reached ",
//                                Toast.LENGTH_SHORT).show();
//
//                    } else if (ret_str == -7 || ret_str1 == -7
//                            || ret_str2 == -7 || ret_str3 == -7
//                            || ret_str5 == -7 || ret_str6 == -7 || ret_str7 == -7 || ret_str8 == -7) {
//                        Toast.makeText(getApplicationContext(),
//                                "Low Voltage.Connect Adapter ",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Lid Not Closed ", Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Paper Not Present ", Toast.LENGTH_SHORT).show();
//            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Print Error!", Toast.LENGTH_SHORT).show();
        }
    }

}




