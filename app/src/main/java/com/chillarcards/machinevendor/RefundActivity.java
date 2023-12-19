package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;

import java.util.List;


/**
 * Created by Codmob on 28-07-16.
 */
public class RefundActivity extends Activity {

    String User_name, Pass_word, userID;
    int useridinteger;
    String schooid, schoomachcode, machineserial,typeId,typeName;
    EditText billnum;
    String billnumber, trans_id, cardserial;
    Float amount = Float.valueOf(0);
    int sale_transid;
    Button refund;
    int type_id, result_type;
    DatabaseHandler db;

    ProgressBar progressBar;
    Dialog dlg;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_refund_activity);

        drawerArrow();
        initialise();

        refund.setOnClickListener(v -> {
            billnumber = String.valueOf(billnum.getText());
            String filler = "";
            String bill;
            int len = 10 - billnumber.length();
            for (int i = 0; i < len; ++i) {
                filler = filler + "0";
            }
            bill = filler + billnumber;
            System.out.println("CHILLRBILLNO " + bill);
            trans_id = schoomachcode + bill;
            System.out.println("CODMOB: RefundActivity- TransID " + trans_id);

            String serverts = db.getSTSbytrnsID(trans_id, Integer.parseInt(typeId));
            System.out.println("CODMOB: RefundActivity- ServerTS " + serverts);
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
                    System.out.println("CODMOB: RefundActivity SxsTrnsxn " + testpermissionnew);

                    cardserial = usp.getcard_serial();
                    result_type = usp.gettranstypeid();

                    System.out.println("CODMOB: RefundActivity CARDSERIAL " + cardserial);

                }

                List<Item_Sale> success11 = db.getAllitemsalebyBill(trans_id);
                for (Item_Sale usp1 : success11) {
                    String testpermission = " transaction id  " + usp1.gettransaction_id() + " billno1 :" + usp1.getbill_no() +
                            "   total:   " + usp1.gettot_amount() +
                            "  Server timestamp  " + usp1.getserver_timestamp() +
                            "  SaleTransID " + usp1.getsale_trans_id();
                    System.out.println("CODMOB: RefundActivity ITEMSALE: " + testpermission);

                    sale_transid = Integer.parseInt(usp1.getsale_trans_id());
                    amount = Float.valueOf(usp1.gettot_amount());
                }

                List<Sales_Item> sales_item11 = db.getAllsalebyBill(trans_id);
                for (Sales_Item slesitm : sales_item11) {
                    String testsalesitem = "salesitem ID : " + slesitm.getSales_item_id() + " sales transID : " + slesitm.getsales_trans_id() + " item ID: " + slesitm.getSales_item_id()
                            + "item quantity : " + slesitm.getitem_quantity() + " amount : " + slesitm.getamount() + " servertimestamp : " + slesitm.getserver_timestamp();


                    System.out.println("CODMOB: RefundActivity SALESITEM : " + testsalesitem);
                }


                System.out.println("CODMOB: RefundActivitydssa " + result_type + " ," + typeId + "," + amount);
                if (result_type == Integer.parseInt(typeId)
                        && amount > (float) 0) {

                    dlg.setContentView(R.layout.popup_bill_details);
                    dlg.setTitle("Bill Details:");
                    dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button OkButton = (Button) dlg.findViewById(R.id.btnok);
                    Button NoButton = (Button) dlg.findViewById(R.id.btnno);
                    TextView cardsl = (TextView) dlg.findViewById(R.id.cardsl1);
                    TextView transId = (TextView) dlg.findViewById(R.id.transID1);
                    TextView amt = (TextView) dlg.findViewById(R.id.amount);

                    cardsl.setText(cardserial);
                    transId.setText(trans_id);
                    amt.setText(amount + "");
                    dlg.show();


                    OkButton.setOnClickListener(v1 -> {
                        dlg.cancel();

                        //TODO REFUND Need to redirect to scan the QR and confirm
                        // -- DEV not completed hide function asp req

                        Intent intent = new Intent(getApplicationContext(), RefundScanPay.class);
                        Bundle b2 = new Bundle();
                        b2.putFloat("amount", amount);
                        b2.putString("transid1", trans_id);
                        b2.putInt("saletrans", sale_transid);
                        b2.putString("cardserial", cardserial);
                        b2.putString("typeId", typeId);
                        b2.putString("typeName", typeName);
                        intent.putExtras(b2);
                        startActivity(intent);

                    });


                    NoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.cancel();


                        }
                    });


                } else {
                    Toast.makeText(RefundActivity.this, "Transaction not in this Outlet.", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void initialise() {
        db = DatabaseHandler.getInstance(getApplicationContext());

        billnum = (EditText) findViewById(R.id.billno);
        refund = (Button) findViewById(R.id.btn_refund);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        dlg = new Dialog(this);

        Bundle b=getIntent().getExtras();
        typeId=b.getString("typeId");
        typeName=b.getString("typeName");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word = editor.getString("password", "");
        userID = String.valueOf(editor.getInt("userid", 0));
        schooid = editor.getString("schoolId", "");
        schoomachcode = editor.getString("schlMachCode", "");
        machineserial = editor.getString("machineserial", "");
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
        super.onBackPressed();
    }





}
