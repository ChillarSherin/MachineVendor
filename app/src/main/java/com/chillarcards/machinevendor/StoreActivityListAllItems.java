package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Adapters.StoreSubmenuListAllAdapter;
import com.chillarcards.machinevendor.ModelClass.ItemList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Codmob on 07-07-16.
 */
public class StoreActivityListAllItems extends Activity {
    private final List<String> ListItemName = new ArrayList<>();
    private final List<String> ListItemPrice = new ArrayList<>();
    private final List<String> ListItemID = new ArrayList<>();
    private final List<String> ListItemCode = new ArrayList<>();
    public EditText search;
    StoreSubmenuListAllAdapter mAdapter;
    ImageView checkout;
    ProgressBar progressBar;

    String typeId,typeName;
    List<ItemList> contacts1 = new ArrayList<ItemList>();
    DatabaseHandler db;
    private Activity activity;
    private RecyclerView mRecyclerView;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override

    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_storeactivity_submenu);

        initialise();


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
//                Bundle b = new Bundle();
//                b.putParcelableArrayList("salesset", (ArrayList<? extends Parcelable>) Constants.sales_items);
//                intent.putExtras(b);
//                startActivity(intent);

                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                Bundle b = new Bundle();
                b.putString("typeId",typeId);
                b.putString("typeName",typeName);
                intent.putExtras(b);
                startActivity(intent);

            }
        });

        contacts1 = db.getAllListItems(Integer.parseInt(typeId));

        try {
            new ItemMenuAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initialise() {

        db = DatabaseHandler.getInstance(getApplicationContext());

        progressBar = (ProgressBar) findViewById(R.id.prog);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.listStoreSub);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = (EditText) findViewById(R.id.searchbar);
        checkout = (ImageView) findViewById(R.id.checkout2);

        Bundle b = getIntent().getExtras();
        String itemname = b.getString("ITEM");
        typeId = b.getString("typeId");
        typeName = b.getString("typeName");

        activity=this;

        drawerArrow();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<String> filteredList1 = new ArrayList<>();
                final List<String> filteredList2 = new ArrayList<>();
                final List<String> filteredList3 = new ArrayList<>();
                final List<String> filteredList4 = new ArrayList<>();


                for (int i = 0; i < ListItemName.size(); i++) {

                    final String text = ListItemName.get(i).toLowerCase();
                    final String code = ListItemCode.get(i).toLowerCase();
                    if (text.contains(query) || code.contains(query)) {

                        filteredList1.add(ListItemName.get(i));
                        filteredList2.add(ListItemPrice.get(i));
                        filteredList3.add(ListItemID.get(i));
                        filteredList4.add(ListItemCode.get(i));

                    }
                }

                mRecyclerView.setLayoutManager(new LinearLayoutManager(StoreActivityListAllItems.this));
                mAdapter = new StoreSubmenuListAllAdapter(filteredList1, filteredList2, filteredList3, filteredList4, StoreActivityListAllItems.this, activity, R.layout.ist_storesubmenu_item, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
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

    private class ItemMenuAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            for (ItemList cn : contacts1) {
                String log = "Name: " + cn.getItem_name() + " ,Code: " + cn.getItem_code() + " ,ItemID: " + cn.getItem_id() + ",Price:" + cn.getprice();

                String cat_name = cn.getItem_name();
                ListItemName.add(cat_name);

                String price = String.valueOf(cn.getprice());
                ListItemPrice.add(price);


                String itid = String.valueOf(cn.getItem_id());
                ListItemID.add(itid);

                String itcode = String.valueOf(cn.getItem_code());
                ListItemCode.add(itcode);


            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new StoreSubmenuListAllAdapter(ListItemName, ListItemPrice, ListItemID, ListItemCode, StoreActivityListAllItems.this, activity, R.layout.ist_storesubmenu_item, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
            progressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            addTextListener();
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
    }




}
