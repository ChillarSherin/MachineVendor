package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Adapters.CheckoutAdapter;

import java.util.ArrayList;

/**
 * Created by Codmob on 09-07-16.
 */
public class CheckoutActivity extends Activity implements View.OnKeyListener {

    private final ArrayList<String> item = new ArrayList<String>();
    private final ArrayList<String> price = new ArrayList<String>();
    private final ArrayList<String> quantity = new ArrayList<String>();
    private final ArrayList<String> totamt = new ArrayList<String>();
    RecyclerView mRecyclerView;

    DatabaseHandler db;
    Activity activity;
    CheckoutAdapter mAdapter;
    Float total = (float) 0;
    TextView totamount;
    Button paybtn;
    Button OkButton, NoButton;
    ProgressBar progressBar;

    String id;

    String amount;
    String name;

    String typeId,typeName;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_checkout_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());
        activity = this;

        Bundle b=getIntent().getExtras();
        typeId = b.getString("typeId");
        typeName = b.getString("typeName");

        initialiseViews();

        for (int i = 0; i < Constants.sales_items.size(); i++) {

            String id = String.valueOf(Constants.sales_items.get(i).getitem_id());
            String pric = String.valueOf(Constants.sales_items.get(i).getamount());
            String qty = String.valueOf(Constants.sales_items.get(i).getitem_quantity());

            String amount = String.valueOf(Float.parseFloat(Constants.sales_items.get(i).getamount()) * Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()));

            String name = db.getItemNamebyID(id);
            item.add(name);
            price.add(pric);
            quantity.add(qty);
            totamt.add(amount);

            total = total + Float.parseFloat(amount);

        }

        totamount.setText(total + "");


        paybtn.setOnClickListener(v -> {

            Constants.TotalAmount = String.valueOf(total);

            if (Constants.sales_items.size() > 0) {

              //  Intent in = new Intent(getApplicationContext(), ScanQrCodePay.class);
                Intent in = new Intent(getApplicationContext(), ScanPayActivity.class);
                Bundle b1 = new Bundle();
                b1.putString("typeId",typeId);
                b1.putString("typeName",typeName);
                b1.putParcelableArrayList("salesset", (ArrayList<? extends Parcelable>) Constants.sales_items);
                in.putExtras(b1);
                startActivity(in);

//                Intent in = new Intent(getApplicationContext(), StoreCardPaymentNew.class);
//                Bundle b1 = new Bundle();
//                b1.putString("typeId",typeId);
//                b1.putString("typeName",typeName);
//                b1.putParcelableArrayList("salesset", (ArrayList<? extends Parcelable>) Constants.sales_items);
//                in.putExtras(b1);
//                startActivity(in);

            } else {
                Toast.makeText(CheckoutActivity.this, "You have no items in ur cart for Pay...", Toast.LENGTH_SHORT).show();
            }


        });


        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CheckoutAdapter(typeId,typeName,item, quantity, price, totamt, activity, R.layout.checkout_list_item, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);



    }

    private void initialiseViews() {
        progressBar = (ProgressBar) findViewById(R.id.progbar);
        progressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.listCheckout);
        totamount = (TextView) findViewById(R.id.totalamt);
        paybtn = (Button) findViewById(R.id.paybtn);
        drawerArrow();
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        System.out.println("TRESATTT onKeyUp tttttttt :" + keyCode);

        if (keyCode == 66) //keyCode for 'Enter' in keyboard
        {
            Constants.TotalAmount = String.valueOf(total);

            if (Constants.sales_items.size() > 0) {
                Intent in = new Intent(getApplicationContext(), StoreCardPaymentNew.class);
                Bundle b = new Bundle();
                b.putString("typeId",typeId);
                b.putString("typeName",typeName);
                b.putParcelableArrayList("salesset", (ArrayList<? extends Parcelable>) Constants.sales_items);
                in.putExtras(b);
                startActivity(in);

            } else {
                Toast.makeText(CheckoutActivity.this, "You have no items in your cart...", Toast.LENGTH_SHORT).show();
            }
            return false;
        }else if (keyCode == 111){
            onBackPressed();
            finish();
            return false;
        }
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

}
