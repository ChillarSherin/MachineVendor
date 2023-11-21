package com.chillarcards.machinevendor.Printer;

import android.app.Activity;

/**
 * Created by Codmob on 19-09-16.
 */


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
import java.util.Calendar;
import java.util.List;

import com.chillarcards.machinevendor.Adapters.PrinterAdapter;
import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.DatabaseHandler;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.R;


public class SaleItemPrinter extends Activity {


    static {

        System.loadLibrary("printerfinal");
        System.out.println("Printer Library Loaded Successfully");
    }

    int pb;
    PrinterAdapter mAdapter;
    String total;
    Button printbill;
    String billno;
    String CommonprintString = "";
    String localTime, formattedDate;
    TextView totalamtButton, printtext;
    int seconds;
    String times;
    ;
    String paymenttypename, balanceamount, cardetails;
    String schoolname, schoolplace;
    DatabaseHandler db;
    String trans_id, cardserial;
    Float prevbal, curbal;
    Float amount = Float.valueOf(0);
    int sale_transid;
    
    private RecyclerView mRecyclerView;
    private Activity activity;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

        db = DatabaseHandler.getInstance(getApplicationContext());

        drawerArrow();
        Bundle printbundle = getIntent().getExtras();
//        CommonprintString = printbundle.getString("print");
        trans_id = printbundle.getString("trans_id");
//        cardetails=printbundle.getString("print3");

//        billno=printbundle.getString("billno");
//        


//        if(Constants.flagTrans==1){
//
//            paymenttypename="STORE";
//
//            System.out.println("CHILL TransTypeID"+paymenttypename);
//
//        }else if(Constants.flagTrans==2){
//
//            paymenttypename="CAFETERIA BILL";
//
//            System.out.println("CHILL TransTypeID"+paymenttypename);
//
//        }else if(Constants.flagTrans==3){
//
//            paymenttypename="SNACKS BAR";
//
//            System.out.println("CHILL TransTypeID"+paymenttypename);
//
//        }


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        schoolname = editor.getString("schoolname", "");
        schoolname = "Cafe " + schoolname;
        schoolplace = editor.getString("schoolplace", "");


        System.out.println("CODMOBTECH: " + schoolname + " " + schoolplace);


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

            if (usp.gettranstypeid() == 1) {
                //store
                paymenttypename = "STORE";
            } else if (usp.gettranstypeid() == 2) {
                //canteen
                paymenttypename = "CAFETERIA BILL";
            } else if (usp.gettranstypeid() == 3) {
                //snacksbar
                paymenttypename = "SNACKSBAR";
            }


            System.out.println("CODMOB: SaleItemPrinter CARDSERIAL " + cardserial);


        }

        List<Item_Sale> success11 = db.getAllitemsalebyBill(trans_id);
        for (Item_Sale usp1 : success11) {
            String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
                    "   total:   " + usp1.gettot_amount() +
                    "  Server timestamp  " + usp1.getserver_timestamp() +
                    "  SaleTransID " + usp1.getsale_trans_id();
            System.out.println("CODMOB: SaleItemPrinter ITEMSALE: " + testpermission);

            sale_transid = Integer.parseInt(usp1.getsale_trans_id());
            total = usp1.gettot_amount();
            billno = usp1.getbill_no();
        }

        List<Sales_Item> sales_item11 = db.getAllsalebyBill(trans_id);
        for (Sales_Item slesitm : sales_item11) {
            String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
                    + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


            System.out.println("CODMOB: SaleItemPrinter SALESITEM : " + testsalesitem);

            String id = String.valueOf(slesitm.getitem_id());
            String name = db.getItemNamebyID(id);
            String pric = String.valueOf(slesitm.getamount());
            String qty = String.valueOf(slesitm.getitem_quantity());

            String amount = String.valueOf(Float.parseFloat(slesitm.getamount()) *
                    Integer.parseInt(slesitm.getitem_quantity()));


//            total = total + Float.parseFloat(amount);
            if (name.length() > 6) {
                name = name.substring(0, 6);
            }

//                                            qty=qty.substring(0,6);
//                                            pric=pric.substring(0,6);
//                                            amount=amount.substring(0,6);


            CommonprintString = CommonprintString + "     " + name + "    " + qty + "    " + pric + "    " + amount + "\n";


        }

        cardetails = "     CardSer    " + cardserial + "\n" +
                "     PrevBal    " + prevbal + "\n";
        balanceamount = "  Balance : " + curbal + "\n";


        printtext = (TextView) findViewById(R.id.totalpriceID);

        totalamtButton = (TextView) findViewById(R.id.totalamount);
        printtext.setText(CommonprintString);


//        total = Constants.TotalAmount;
        totalamtButton.setText(total);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

//        Toast.makeText(getApplicationContext(), "date : " + formattedDate, Toast.LENGTH_SHORT).show();


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        times = hour + ":" + minute;
//        Toast.makeText(getApplicationContext(), "" +times, Toast.LENGTH_SHORT).show();


        printbill = (Button) findViewById(R.id.print_bill);
        printbill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pt = 0;

                // printText();

                // printing format
                // TODO Auto-generated method stub
                //  print_bmp();
                String str = "     " + schoolname + "\n";
//                            + "           "+schoolplace+"\n";
                String str1 = "    DATE " + formattedDate + ""
                        + " TIME " + times + "\n"
                        + "    TRANS ID: " + trans_id + "\n";
                String str2 = "   " + paymenttypename + " \n\n";
                String str3 = "     ITEM    QTY    PRICE    TOTAL    \n--------------------------------------";


                String str4 = CommonprintString + "\n";
                String str5 = balanceamount + "\n";
                String str6 = "     Total    " + total + "\n";
                String str7 = cardetails + "\n";
                String str8 = "                  \n"
                        + "--------------------------------------\n" + "*Inclusive of all taxes\n" + "Thank You...\n\n" + "Powered By\n" + "Chillar Payment Solns Pvt Ltd\n";


                System.out.println("Printing Text");
//
//                if (ap.prn_paperstatus() == 0) {
//                    // for setting printer font and size
//                    if (ap.prn_lid_status() == 0) {
//
//                        int ret_str = ap.prn_write_text(str, 7, 1);
//                        int ret_str1 = ap.prn_write_text(str1, 7, 1);
//                        int ret_str2 = ap.prn_write_text(str2, 7, 2);
//                        int ret_str3 = ap.prn_write_text(str3, 7, 1);
//                        int ret_str4 = ap.prn_write_text(str4, 6, 1);
//                        int ret_str7 = ap.prn_write_text(str7, 7, 1);
//                        int ret_str5 = ap.prn_write_text(str5, 7, 2);
//                        int ret_str6 = ap.prn_write_text(str6, 7, 2);
//                        int ret_str8 = ap.prn_write_text(str8, 7, 1);
//
//                        ap.prn_paper_feed(5);
//
//                        System.out.println("prn write text " + ret_str);
//
//                        if (ret_str == 0 && ret_str1 == 0 && ret_str2 == 0 && ret_str3 == 0 && ret_str4 == 0 && ret_str5 == 0 && ret_str6 == 0 || ret_str7 == 0 || ret_str8 == 0
//                                ) {
//
//
//                            Constants.sales_items.clear();
////                            Intent inte=new Intent(CommonPrinter.this, StoreActivity.class);
////                            inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(inte);
////                            finish();
////                            onBackPressed();
//
//                        } else if (ret_str == -8 || ret_str1 == -8
//                                || ret_str2 == -8 || ret_str3 == -8 || ret_str4 == -8
//                                || ret_str5 == -8 || ret_str6 == -8 || ret_str7 == -8 || ret_str8 == -8) {
//
//                            Toast.makeText(getApplicationContext(),
//                                    "Max Temperature Reached ",
//                                    Toast.LENGTH_SHORT).show();
//
//                        } else if (ret_str == -7 || ret_str1 == -7
//                                || ret_str2 == -7 || ret_str3 == -7 || ret_str4 == -7
//                                || ret_str5 == -7 || ret_str6 == -7 || ret_str7 == -7 || ret_str8 == -7) {
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
//        Constants.sales_items.clear();
//        Intent inte=new Intent(SaleItemPrinter.this, StoreActivity.class);
//        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(inte);
//        finish();
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
