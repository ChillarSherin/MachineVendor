package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import com.chillarcards.machinevendor.Adapters.CheckoutAdapter;
import com.chillarcards.machinevendor.Adapters.PreOrderListAdapter;

/**
 * Created by Codmob on 05-09-17.
 */

public class PreOrderListActivity extends Activity {

    RecyclerView mRecyclerView;
    ProgressBar progressBar;
    Button btn_continue;
    Float total=(float) 0;

    String id;
    String pric;
    String qty;;
    String amount;;
    String name;

    PreOrderListAdapter mAdapter;
    Activity activity;

    DatabaseHandler db ;

    private final ArrayList<String> item = new ArrayList<String>();
    private final ArrayList<String> price = new ArrayList<String>();
    private final ArrayList<String> quantity = new ArrayList<String>();
    private final ArrayList<String> totamt = new ArrayList<String>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_preorder_list);

        db = DatabaseHandler.getInstance(getApplicationContext());

        activity=this;

        mRecyclerView= (RecyclerView) findViewById(R.id.mRecycler);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        btn_continue= (Button) findViewById(R.id.btn_continue);



        for (int i=0;i<Constants.sales_items.size();i++){
            System.out.println("CHILLER: salesitems: "+Constants.sales_items.get(i).getitem_id());

            String test= "Item ID : "+Constants.sales_items.get(i).getitem_id()+" Price : "+Constants.sales_items.get(i).getamount()+" Quantity : "+Constants.sales_items.get(i).getitem_quantity();

            id= String.valueOf(Constants.sales_items.get(i).getitem_id());
            pric= String.valueOf(Constants.sales_items.get(i).getamount());
            qty= String.valueOf(Constants.sales_items.get(i).getitem_quantity());

            amount= String.valueOf(Float.parseFloat(Constants.sales_items.get(i).getamount())*Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()));




            name=db.getItemNamebyID(id);

            System.out.println("CHILL itname"+name);
            item.add(name);
            price.add(pric);
            quantity.add(qty);
            totamt.add(amount);

            total=total+Float.parseFloat(amount);

//            System.out.println("CHILLAR Checkoutlist: "+test);
        }




        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

//                Toast.makeText(getApplicationContext(), "name "+item+" price "+price+" qty "+quantity+" amount "+totamt+" Total "+total, Toast.LENGTH_SHORT).show();


                Constants.TotalAmount= String.valueOf(total);


                if(Constants.sales_items.size()>0) {
                    progressBar.setVisibility(View.GONE);
                    Intent in = new Intent(getApplicationContext(), StoreCardPayment.class);
                    Bundle b = new Bundle();
                    b.putParcelableArrayList("salesset",(ArrayList<? extends Parcelable>) Constants.sales_items);
                    in.putExtras(b);
                    startActivity(in);
                    finish();
                }else
                {
                    Toast.makeText(PreOrderListActivity.this, "You have no items in ur cart for Pay...", Toast.LENGTH_SHORT).show();
                }


            }
        });



        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PreOrderListAdapter(item,quantity,price,totamt, activity,R.layout.item_preorder_list ,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
//        db.deleteAllItemssale();


    }


}
