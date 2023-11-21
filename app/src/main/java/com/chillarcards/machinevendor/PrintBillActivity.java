package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Item_Type;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.Printer.CommonPrinter;
import com.chillarcards.machinevendor.Printer.PaymentPrinter;

/**
 * Created by Codmob on 16-09-16.
 */
public class PrintBillActivity extends Activity {

    String User_name, Pass_word, userID;
    int useridinteger;
    String schooid, schoomachcode, machineserial;
    EditText billnum;
    TextView header;
    String billnumber, trans_id, cardserial;
    Float prevbal, curbal;
    Float amount = Float.valueOf(0);
    int sale_transid;
    Button refund;
    int type_id;
    DatabaseHandler db;
    ProgressBar progressBar;
    String print = "", print2 = "", print3;

    //    DatabaseHandler db = new DatabaseHandler(this);
    Float total = (float) 0;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_refund_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());

        billnum = (EditText) findViewById(R.id.billno);
        refund = (Button) findViewById(R.id.btn_refund);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        refund = (Button) findViewById(R.id.btn_refund);
        header = (TextView) findViewById(R.id.textView15);
        header.setText("Print Bill");
        refund.setText("Print");

//        refund.setEnabled(false);
//        refund.setClickable(false);
//        new FieldOnTask().execute();

        drawerArrow();

//        String itemname="";
//
//        if(Constants.flagTrans==1){
//
//            itemname="STORE";
//
//        }else if(Constants.flagTrans==2){
//
//            itemname="CANTEEN";
//
//        }else if(Constants.flagTrans==3){
//
//            itemname="SNACKS BAR";
//
//        }
//
//        Item_Type item_type=db.getitemtypebyname(itemname);
//        type_id=item_type.getitem_type_id();


        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userID = String.valueOf(editor.getInt("userid", 0));
        schooid = editor.getString("schoolId", "");
        schoomachcode = editor.getString("schlMachCode", "");
        machineserial = editor.getString("machineserial", "");

        System.out.println("CODMOB: PrintBillActivity " + schooid + schoomachcode + machineserial);


        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billnumber = String.valueOf(billnum.getText());
                String filler = "";
                String bill;
                int len = 10 - billnumber.length();

                for (int i = 0; i < len; ++i) {
                    filler = filler + "0";
                }

                bill = filler + billnumber;
                System.out.println("CHILLR_PRINT_BILL_NO " + bill);


                trans_id = schoomachcode + bill;

                System.out.println("CODMOB: PrintBillActivity- TransID " + trans_id);

                String serverts = db.getSTSbytrnsID(trans_id, type_id);

                System.out.println("CODMOB: PrintBillActivity- ServerTS " + serverts);

                if (serverts.equals("-1") || serverts.equals("-2")) {

                    Toast.makeText(getApplicationContext(), "Sorry. Refund already processed. ", Toast.LENGTH_LONG).show();

                } else if (serverts.equals("0")) {

                    Toast.makeText(getApplicationContext(), "Sorry. No Transaction found. ", Toast.LENGTH_LONG).show();

                } else {


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

                    List<Item_Sale> success11 = db.getAllitemsalebyBill(trans_id);
                    for (Item_Sale usp1 : success11) {
                        String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
                                "   total:   " + usp1.gettot_amount() +
                                "  Server timestamp  " + usp1.getserver_timestamp() +
                                "  SaleTransID " + usp1.getsale_trans_id();
                        System.out.println("CODMOB: PrintBillActivity ITEMSALE: " + testpermission);

                        sale_transid = Integer.parseInt(usp1.getsale_trans_id());
                        amount = Float.valueOf(usp1.gettot_amount());
                    }

                    print = print + "     TransID    " + trans_id + "\n" +
                            "     Username   " + User_name + "\n" +
                            "     CardSer    " + cardserial + "\n" +
                            "     Amount     " + (prevbal - curbal) + "\n" +
                            "     PrevBal    " + prevbal + "\n" +
                            "     CurrBal    " + curbal + "\n";

                    Intent i = new Intent(getApplicationContext(), PaymentPrinter.class);
                    Bundle printbundle = new Bundle();
                    printbundle.putString("print", print);
                    printbundle.putString("Amount", String.valueOf(total));
                    i.putExtras(printbundle);
                    startActivity(i);


//                    print2 = "  Balance : " + curbal + "\n";
//                    print3 = "     CardSer    " + cardserial + "\n" +
//                            "     PrevBal    " + prevbal + "\n";


//
//                    Intent i = new Intent(getApplicationContext(), CommonPrinter.class);
//                    Bundle printbundle = new Bundle();
//                    printbundle.putString("print", print);
//                    printbundle.putString("print2", print2);
//                    printbundle.putString("print3", print3);
//                    printbundle.putString("billno", trans_id);
//                    i.putExtras(printbundle);
//                    startActivity(i);

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
    public void onResume() {
        print = "";
        super.onResume();
    }
}
