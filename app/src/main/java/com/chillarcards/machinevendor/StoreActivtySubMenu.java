package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Adapters.StoreSubmenuAdapter;
import com.chillarcards.machinevendor.ModelClass.ItemList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Codmob on 07-07-16.
 */
public class StoreActivtySubMenu extends Activity implements View.OnKeyListener {
    private final List<String> ListItemName = new ArrayList<>();
    private final List<String> ListItemPrice = new ArrayList<>();
    private final List<String> ListItemID = new ArrayList<>();
    private final List<String> ListItemCode = new ArrayList<>();
    public EditText search;
    StoreSubmenuAdapter mAdapter;
    ImageView checkout;
    ProgressBar progressBar;

    TextView menuName;
    String id,itemname;
    String typeId,typeName;
    DatabaseHandler db;
    List<ItemList> contacts1 = new ArrayList<ItemList>();
    private Activity activity;
    private RecyclerView mRecyclerView;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_storeactivity_submenu);

        db = DatabaseHandler.getInstance(getApplicationContext());
        activity=this;

        initialiseViews();

        Bundle b = getIntent().getExtras();
        typeId = b.getString("typeId");
        typeName = b.getString("typeName");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        id = editor.getString("ITEMID", "0");
        itemname = editor.getString("ITEM", "");

        menuName.setText(typeName+" - "+itemname );
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                Bundle b = new Bundle();
                b.putString("typeId",typeId);
                b.putString("typeName",typeName);
                b.putParcelableArrayList("salesset", (ArrayList<? extends Parcelable>) Constants.sales_items);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        try {
            if (!id.isEmpty()) {
                contacts1 = db.getAllListItemsByCatId(Integer.parseInt(id));
            } else {
                onBackPressed();
                Toast.makeText(activity, "Try Again! Network Error", Toast.LENGTH_SHORT).show();

            }
        } catch (NumberFormatException e) {
            // Handle the case where 'id' is not a valid integer
            // For example, you can set a default value or show an error message
            e.printStackTrace();
        }

        try {
            new ItemMenuAsync().execute();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private void initialiseViews() {

        drawerArrow();

        progressBar = findViewById(R.id.prog);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = findViewById(R.id.listStoreSub);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = findViewById(R.id.searchbar);
        checkout = findViewById(R.id.checkout2);
        menuName = findViewById(R.id.button_item);

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

                mRecyclerView.setLayoutManager(new LinearLayoutManager(StoreActivtySubMenu.this));
                mAdapter = new StoreSubmenuAdapter(typeId,typeName,filteredList1, filteredList2, filteredList3, filteredList4, StoreActivtySubMenu.this, activity, R.layout.ist_storesubmenu_item, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    public void drawerArrow() {
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.luc_black));
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
    protected void onResume() {
        super.onResume();

        search.setText("");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        System.out.println("TRESATTT onKeyUp tttttttt :" + keyCode);

        if (keyCode == 66) //keyCode for 'Enter' in keyboard
        {
            Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
            Bundle b = new Bundle();
            b.putString("typeId",typeId);
            b.putString("typeName",typeName);
            b.putParcelableArrayList("salesset", (ArrayList<? extends Parcelable>) Constants.sales_items);
            intent.putExtras(b);
            startActivity(intent);
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

                // Writing Contacts to log
                System.out.println("CHILLARItemList : " + log);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new StoreSubmenuAdapter(typeId,typeName,ListItemName, ListItemPrice, ListItemID, ListItemCode, StoreActivtySubMenu.this, activity, R.layout.ist_storesubmenu_item, getApplicationContext());
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
