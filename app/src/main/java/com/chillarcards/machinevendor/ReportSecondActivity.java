package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.Printer.ReportItemWisePrinter;
import com.chillarcards.machinevendor.Printer.ReportPrinter;

/**
 * Created by Codmob on 03-03-17.
 */

public class ReportSecondActivity extends Activity {

    Button Item_wise,total;
    String Date;

    private List<String> SaleItemId  = new ArrayList<>();
    private final List<String> SaleItemPrint = new ArrayList<>();

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    String successPrint="";
    String transactionID,transactionTypeID,successcardSerial;

    int translength;
    int tot_qty;
    Float tot_amt;


//    DatabaseHandler db=new DatabaseHandler(this);

    DatabaseHandler db ;
    Float TotalAmount;
    String trans_type_id;
    String formatedDate = "",activityname="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_report_second_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());

        drawerArrow();

        Item_wise= (Button) findViewById(R.id.item_wise);
        total= (Button) findViewById(R.id.total);



        total.setOnClickListener(v -> {

            List<Success_Transaction> successtransactions= new ArrayList<Success_Transaction>();
            if(activityname.equals("tabledisplay")){
                successtransactions = db.getSuccessdatewise(formatedDate);
                translength = db.getSuccessdatewise(formatedDate).size();
            }else{
                successtransactions = db.getSuccessdatewise(formatedDate,trans_type_id);
                translength = db.getSuccessdatewise(formatedDate,trans_type_id).size();
            }


            TotalAmount = (float) 0;
            for (Success_Transaction usp : successtransactions) {
                String testpermissionnew = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                        "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                        + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                        "  Server timestamp  " + usp.getserver_timestamp();


                if (usp.getprevious_balnce() > usp.getcurrent_balance()) {
                    TotalAmount = TotalAmount + (usp.getprevious_balnce() - usp.getcurrent_balance());
                } else {
                    TotalAmount = TotalAmount + (usp.getcurrent_balance() - usp.getprevious_balnce());
                }


                System.out.println("CHILLAR Total Amount" + TotalAmount);
                System.out.println("CHILLAR secact success transaction datewise" + testpermissionnew);

                transactionID = usp.gettrans_id();
                transactionTypeID = String.valueOf(usp.gettranstypeid());
                successcardSerial = usp.getcard_serial();
                successPrint = successPrint +"   "+ transactionID  +"  "+ transactionTypeID +"    "+ successcardSerial + "\n";


                System.out.println("CHILLAR Success transaction details" + transactionID + "  " + transactionTypeID + "   " + successcardSerial);
                System.out.println("CHILLAR Success transaction details" +successPrint);

            }

            System.out.println("CHILLAR Success final" +successPrint);
            System.out.println("CHILLAR Final TotalAmount" + TotalAmount);
            System.out.println("CHILLAR Final TransLength" + translength);

            Intent i = new Intent(getApplicationContext(), ReportPrinter.class);
            Bundle printbundle = new Bundle();
            //  printbundle.putString("successprint", successPrint);
            printbundle.putInt("TotalTransaction", translength);
            printbundle.putFloat("TotalAmount", TotalAmount);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtras(printbundle);
            startActivity(i);
            finish();


        });


        Item_wise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleItemId.clear();
                SaleItemPrint.clear();

                SaleItemId = db.getdistinctItemid(formatedDate, trans_type_id);

                if(SaleItemId.size()>0){


                    for(int i=0;i<SaleItemId.size();++i){

                        tot_qty=0;
                        tot_amt=(float)0;

                        System.out.println("Chillar NamebyID: "+db.getItemNamebyID(SaleItemId.get(i)) );

                        List<Sales_Item> sale1 =  db.getAllsalebyId(Integer.parseInt(SaleItemId.get(i)),formatedDate, trans_type_id);



                        for (Sales_Item cn : sale1) {

                            String log = "Sales Item ID : " + cn.getitem_id()+".... "+cn.getamount()+".... "+cn.getitem_quantity()+".... "+cn.getsales_trans_id();
                            // Writing Contacts to log
                            System.out.println("CHILLAR Sales111: "+log);

                            tot_qty= tot_qty+ Integer.valueOf(cn.getitem_quantity());
                            tot_amt= tot_amt+ Float.valueOf(cn.getamount())*Integer.valueOf(cn.getitem_quantity());

                        }


                        System.out.println("CHILLAR Qty n Amt: "+tot_qty+" ... "+tot_amt);

                        String ItemName=db.getItemNamebyID(SaleItemId.get(i));

                        if(ItemName.length()>20){
                            System.out.println("CHILLAR: Substrng >20");
                            ItemName=ItemName.substring(0, 20);
                        }else{

                            System.out.println("CHILLAR: Substrng <20");
                            String filler = "";

                            int len = 20 - ItemName.length();
                            for (int j = 0; j < len; ++j) {

                                filler = filler + " ";

                            }

                            ItemName = ItemName+filler;
                        }


                        SaleItemPrint.add(ItemName+"         "+tot_qty);

                    }

                    String print="";

                    for (int i=0;i<SaleItemPrint.size();++i){

                        print=print+SaleItemPrint.get(i)+"\n";


                    }

                    System.out.println("CHILLAR : PRint: "+print);

                    Intent i = new Intent(getApplicationContext(), ReportItemWisePrinter.class);
                    Bundle printbundle = new Bundle();
                    printbundle.putString("Print", print);
                    printbundle.putString("tot_items", String.valueOf(SaleItemPrint.size()));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtras(printbundle);
                    startActivity(i);
                    finish();


                }else{

                    Toast.makeText(ReportSecondActivity.this, "No Sale Transactions in DB!", Toast.LENGTH_SHORT).show();

                }



            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        Bundle b=getIntent().getExtras();
        formatedDate=b.getString("Date");
        activityname=b.getString("activity");

        trans_type_id = String.valueOf(db.getTransTypID(Constants.Category));

        if(!activityname.equals("store")) {


            Item_wise.setVisibility(View.GONE);

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
