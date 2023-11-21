package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by test on 15-07-2016.
 */

public class AllTablesDisplay extends Activity {

    Button success, payment, deviceinfo, blockcards, recharge, category, librarybook, attendence, fee, itemsale, itemStock, itemlist,
            userbutton, attendencetype, paymenttype, itemtype, transactiontype, report, online;
    DatabaseHandler db;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tables);
        drawerArrow();

        db = DatabaseHandler.getInstance(getApplicationContext());

        success = (Button) findViewById(R.id.success_trans);
        payment = (Button) findViewById(R.id.payment_trans);
        deviceinfo = (Button) findViewById(R.id.dev_info);
        blockcards = (Button) findViewById(R.id.block_cards);
        recharge = (Button) findViewById(R.id.recharg_table);
        category = (Button) findViewById(R.id.category);
        librarybook = (Button) findViewById(R.id.library);
        attendence = (Button) findViewById(R.id.attendence);
        fee = (Button) findViewById(R.id.fee);
        itemsale = (Button) findViewById(R.id.itemsale);
        itemStock = (Button) findViewById(R.id.itemstock);
        itemlist = (Button) findViewById(R.id.itemlist);
        userbutton = (Button) findViewById(R.id.user);
        attendencetype = (Button) findViewById(R.id.attendencetype);
        paymenttype = (Button) findViewById(R.id.paytype);
        itemtype = (Button) findViewById(R.id.itemtype);
        transactiontype = (Button) findViewById(R.id.transtype);
        online = (Button) findViewById(R.id.online);

        report = (Button) findViewById(R.id.report);

        report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                Bundle b = new Bundle();
                b.putString("activity", "tabledisplay");
                i.putExtras(b);
                startActivity(i);
            }
        });

        online.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllonline().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Transactions in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "online");
                    i.putExtras(b);
                    startActivity(i);
                }

            }
        });

        //Displaying success transaction table
        success.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllSuccess().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Transactions in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "success");
                    i.putExtras(b);
                    startActivity(i);
                }

            }
        });

        //Displaying payment table
        payment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getAllpaytransaction().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Transactions in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "payment");
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });

        //Displaying DEVICEINFO table

        deviceinfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAlldevInfo().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No DeviceInfo in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "deviceinfo");
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });

        //Displaying Device info table

        blockcards.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllBlockCardInfo().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Blocked Cards in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "blockcards");
                    i.putExtras(b);
                    startActivity(i);

                }

            }
        });

        //Displaying recharge table

        recharge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllrech().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No RECHARGE in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "recharge");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying Category table

        category.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllcat().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Category list in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "category");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying LIBRARY table

        librarybook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAlllib().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Library in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "library");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying attendence table

        attendence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllAttendancedata().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No attendence in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "attendence");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });


        //Displaying Fee table
        fee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllfeetrans().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Fee Transactions in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "fee");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying itemsale table

        itemsale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllitemsale().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item sale in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "itemsale");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying itemstock table
        itemStock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllItems().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item Stock in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "itemstock");
                    i.putExtras(b);
                    startActivity(i);

                }
            }
        });

        //Displaying itemlist table


        itemlist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllitemlist().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item Stock in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "itemlist");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying user table


        userbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllUsers().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item Stock in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "user");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying attendence type table

        attendencetype.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllAttendance().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item Stock in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "attendencetype");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });


        //Displaying payment type table


        paymenttype.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllpay().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item Stock in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "paymenttype");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying Itemtype table

        itemtype.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllitemtype().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item Stock in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "itemtype");
                    i.putExtras(b);
                    startActivity(i);

                }


            }
        });

        //Displaying Transaction type table

        transactiontype.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAlltrancttype().size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Item Stock in database!", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(getApplicationContext(), TablesDisplayActivity.class);
                    Bundle b = new Bundle();
                    b.putString("action", "transactiontype");
                    i.putExtras(b);
                    startActivity(i);

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

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();

            }
        });
    }
}