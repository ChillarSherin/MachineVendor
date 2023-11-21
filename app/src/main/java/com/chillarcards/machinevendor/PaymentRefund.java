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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;

/**
 * Created by test on 27-08-2016.
 */
public class PaymentRefund extends Activity {


    String User_name, Pass_word, userID;
    int useridinteger;
    String schooid, schoomachcode, machineserial;
    EditText billnum;
    String billnumber, trans_id, cardserial;
    int sale_transid;
    Button refund;
    int result_type;
    Dialog dlg;

    DatabaseHandler db;
    String itemname,typeId,typeName;
    String paytransactionid, paybillnumb;
    Float payamounts = Float.valueOf(0);

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_refund);


        drawerArrow();

        initialise();


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
                System.out.println("CHILLRBILLNO " + bill);


                trans_id = schoomachcode + bill;
                System.out.println("CODMOB: RefundActivity- TransID " + trans_id);
                System.out.println("CODMOB: RefundActivity- TypeID " + typeId);

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

                    //////////////////////////////////////


                    List<Payment_Transaction> pay = db.getallPaymentbyBill(trans_id);
                    for (Payment_Transaction usp1 : pay) {
                        String testpermission = " transaction id  " + usp1.gettrans_id() + " billno1 :" + usp1.getbillno() +
                                "   total:   " + usp1.getamount() +
                                "  Server timestamp  " + usp1.getserver_timestamp();
                        System.out.println("CODMOB: RefundNFC Payment: " + testpermission);


                        System.out.println("CODMOB: RefundActivity CARDSERIAL " + cardserial);
                        paybillnumb = usp1.getbillno();
                        payamounts = usp1.getamount();
                        paytransactionid = usp1.gettrans_id();
                        System.out.println("CODMOB: RefundActivity Trans " + paytransactionid);
                        System.out.println("CODMOB: RefundActivity amount " + payamounts);

                    }

                    System.out.println("CODMOB: RefundActivity " + result_type + " ," + typeId + "," + payamounts);

                    if (result_type == Integer.parseInt(typeId)
                            && payamounts > (float) 0) {

                        dlg.setContentView(R.layout.popup_bill_details);
                        dlg.setTitle("Bill Details:");
                        dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        Button OkButton = (Button) dlg.findViewById(R.id.btnok);
                        Button NoButton = (Button) dlg.findViewById(R.id.btnno);
                        TextView cardsl = (TextView) dlg.findViewById(R.id.cardsl1);
                        TextView transId = (TextView) dlg.findViewById(R.id.transID1);
                        TextView amt = (TextView) dlg.findViewById(R.id.amount);

                        cardsl.setText(cardserial);
                        transId.setText(paytransactionid);
                        amt.setText(payamounts + "");
                        dlg.show();


                        OkButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dlg.cancel();

                                Intent intent = new Intent(getApplicationContext(), RefundNFCNew.class);
                                Bundle b2 = new Bundle();
                                b2.putFloat("amount", payamounts);
                                b2.putString("transid1", paytransactionid);
                                b2.putString("cardserial", cardserial);
                                b2.putString("typeId", typeId);
                                b2.putString("typeName", typeName);
                                intent.putExtras(b2);
                                startActivity(intent);


                            }
                        });


                        NoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dlg.cancel();


                            }
                        });


                    } else {
                        Toast.makeText(PaymentRefund.this, "Transaction not in this Outlet.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


    }

    private void initialise() {

        db = DatabaseHandler.getInstance(getApplicationContext());

        billnum = (EditText) findViewById(R.id.billno);
        refund = (Button) findViewById(R.id.btn_refund);
        dlg = new Dialog(PaymentRefund.this);

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

        System.out.println("CODMOB: RefundActivivty " + schooid + schoomachcode + machineserial);

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
