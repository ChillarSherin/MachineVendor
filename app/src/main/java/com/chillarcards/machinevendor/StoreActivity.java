package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Adapters.StoreAdapter;
import com.chillarcards.machinevendor.ModelClass.CategoryList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Codmob on 05-07-16.
 */
public class StoreActivity extends Activity {


    private final List<String> category = new ArrayList<>();
    private final List<Integer> categoryID = new ArrayList<>();
    LinearLayout checkout;
    StoreAdapter mAdapter;
    LinearLayout listAll, refund, report;
    Button OkButton, NoButton;
    Dialog PopupConfrm;
    DatabaseHandler db;
    ProgressBar progressBar;
    EditText search;
    TextView menuName;


    private Activity activity;
    private RecyclerView mRecyclerView;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    String typeId, typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_store_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());
        activity = this;

        Bundle b = getIntent().getExtras();
        typeId = b.getString("typeId");
        typeName = b.getString("typeName");

        initialiseViews();
        menuName.setText(typeName);

        System.out.println("CHILLAR: typeid " + typeId);

        List<CategoryList> contacts1 = db.getAllCategoryItems(Integer.parseInt(typeId));
        try {
            new StoreAsyncTask(contacts1).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        refund.setOnClickListener(v -> {
            Intent i1 = new Intent(getApplicationContext(), RefundActivity.class);
            Bundle b1 = new Bundle();
            b1.putString("typeId", typeId);
            b1.putString("typeName", typeName);
            i1.putExtras(b1);
            startActivity(i1);

        });

        report.setOnClickListener(v -> {
            Intent i2 = new Intent(getApplicationContext(), ReportActivity.class);
            Bundle b12 = new Bundle();
            b12.putString("activity", "store");
            i2.putExtras(b12);
            startActivity(i2);
        });

        listAll.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), StoreActivityListAllItems.class);
            Bundle b14 = new Bundle();
            b14.putString("typeId", typeId);
            b14.putString("typeName", typeName);
            i.putExtras(b14);
            startActivity(i);
        });


        checkout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
            Bundle b13 = new Bundle();
            b13.putString("typeId", typeId);
            b13.putString("typeName", typeName);
            intent.putExtras(b13);
            startActivity(intent);
        });


    }

    private void initialiseViews() {

        progressBar = findViewById(R.id.prog);
        progressBar.setVisibility(View.VISIBLE);
        PopupConfrm = new Dialog(this);
        mRecyclerView = findViewById(R.id.listStore);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = findViewById(R.id.searchbar);
        checkout = findViewById(R.id.imaglayout);
        listAll = findViewById(R.id.listall);
        refund = findViewById(R.id.refund);
        report = findViewById(R.id.report);
        menuName = findViewById(R.id.button_item);

        drawerArrow();
    }


    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<String> filteredList = new ArrayList<>();
                final List<Integer> filteredList1 = new ArrayList<>();

                for (int i = 0; i < category.size(); i++) {

                    final String text = category.get(i).toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(category.get(i));
                        filteredList1.add(categoryID.get(i));
                    }
                }

                mRecyclerView.setLayoutManager(new LinearLayoutManager(StoreActivity.this));
                mAdapter = new StoreAdapter(typeId, typeName, filteredList, filteredList1, activity, R.layout.list_store_category_item, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("CHILLER:onResume");

        for (int i = 0; i < Constants.sales_items.size(); i++) {
            System.out.println("CHILLER: salesitems: " + Constants.sales_items.get(i).getitem_id());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor1 = getSharedPreferences("Chillar", MODE_PRIVATE).edit();
        editor1.putString("ITEMID", "");
        editor1.putString("ITEM", "");
        editor1.commit();
    }

    @Override
    public void onBackPressed() {


        if (Constants.sales_items.size() > 0) {


            PopupConfrm.setContentView(R.layout.layout_confrmpopup);
            PopupConfrm.setTitle("Are you Sure Want To Go Back?");
            PopupConfrm.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView message = PopupConfrm.findViewById(R.id.textpopup);
            OkButton = (Button) PopupConfrm.findViewById(R.id.btnok);
            NoButton = (Button) PopupConfrm.findViewById(R.id.btnno);
            PopupConfrm.show();

            message.setText("Are you Sure Want To Go Back?");
            OkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupConfrm.cancel();
                    Constants.sales_items.clear();
                    finish();

                }
            });


            NoButton.setOnClickListener(v -> PopupConfrm.cancel());


        } else {

            finish();
        }


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
            }


        });

    }

    private class StoreAsyncTask extends AsyncTask<Void, Void, String> {

        List<CategoryList> contacts1 = new ArrayList<>();

        public StoreAsyncTask(List<CategoryList> contacts1) {
            this.contacts1 = contacts1;

        }

        @Override
        protected String doInBackground(Void... params) {

            for (CategoryList cn : contacts1) {
                String log = "Name: " + cn.getcategory_name() + " ,ShortName: " + cn.getcategory_shortname();

                String cat_name = cn.getcategory_name();

                int catId = cn.getcategory_id();
                category.add(cat_name);
                categoryID.add(catId);
            }

            for (int i = 0; i < category.size(); i++) {

                System.out.println("CHILLAR CategoryArray:" + category.get(i));
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new StoreAdapter(typeId, typeName, category, categoryID, activity, R.layout.list_store_category_item, getApplicationContext());
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
