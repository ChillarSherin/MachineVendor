package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;



/**
 * Created by Codmob on 02-08-16.
 */
public class AdminSettingsActivity extends Activity {

    Button display_tables, school_logout, CardinitButton, app_exit;
    Dialog PopupConfrm;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int tama_on;
    DatabaseHandler db;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        db = DatabaseHandler.getInstance(getApplicationContext());


        linearLayout = (LinearLayout) findViewById(R.id.linear);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);


        PopupConfrm = new Dialog(this);
        drawerArrow();

        display_tables = (Button) findViewById(R.id.disp_table);
        school_logout = (Button) findViewById(R.id.school_logout);
        CardinitButton = (Button) findViewById(R.id.cardinit);
        app_exit = (Button) findViewById(R.id.exitapp);


        app_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllsuccesstoup().size() > 0 ||
                        db.getAllsxstoupNew().size() > 0 ||
                        db.getAllitemsaletoUp().size() > 0 ||
                        db.getAllitemsaletoUpNew().size() > 0 ||
                        db.getAllsaletoUp().size() > 0 ||
                        db.getAllsaletoUpNew().size() > 0 ||
                        db.getAlllibtoUp().size() > 0 ||
                        db.getAllAttendancedatatoUP().size() > 0 ||
                        db.getAllrechtoUp().size() > 0 ||
                        db.getAllfeetranstoUp().size() > 0 ||
                        db.getAllpaytransactiontoUp().size() > 0) {

                    Toast.makeText(getApplicationContext(), "Database Not uploaded completely. Please upload and then try again.", Toast.LENGTH_LONG).show();

                } else {
                    finishAffinity();
                }
            }
        });


        display_tables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), AllTablesDisplay.class);
                startActivity(i);

            }
        });




        /*
         * School Logout- Logout from the present school .
         * This option is only activated after successfull complete server updation
         * On logout,all the existing data in the tables are deleted by calling #clearDatabase()
         * */
        school_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupConfrm.setContentView(R.layout.layout_confrmpopup);
                PopupConfrm.setTitle("Are you Sure Want To Logout?");
                PopupConfrm.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Button OkButton = (Button) PopupConfrm.findViewById(R.id.btnok);
                Button NoButton = (Button) PopupConfrm.findViewById(R.id.btnno);
                PopupConfrm.show();

                OkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupConfrm.cancel();
                        if (db.getAllsuccesstoup().size() > 0 ||
                                db.getAllsxstoupNew().size() > 0 ||
                                db.getAllitemsaletoUp().size() > 0 ||
                                db.getAllitemsaletoUpNew().size() > 0 ||
                                db.getAllsaletoUp().size() > 0 ||
                                db.getAllsaletoUpNew().size() > 0 ||
                                db.getAlllibtoUp().size() > 0 ||
                                db.getAllAttendancedatatoUP().size() > 0 ||
                                db.getAllteacherAttendancedatatoUP().size() > 0 ||
                                db.getAllrechtoUp().size() > 0 ||
                                db.getAllfeetranstoUp().size() > 0 ||
                                db.getAllpaytransactiontoUp().size() > 0 ||
                                db.getAllpaytransactiontoUpNew().size() > 0 ||
                                db.getAllOnlineToUp().size() > 0 ||
                                db.getAllRefundtoUp().size() > 0 ||
                                db.getAllCreateLeaveToUp().size() > 0) {

                            Toast.makeText(getApplicationContext(), "Database Not uploaded completely. Please upload and then try again.", Toast.LENGTH_LONG).show();

                        } else {
                            SharedPreferences preferences = getSharedPreferences("Chillar", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            Constants.machineID = "";


                            /* Clear all data in all tables */
                            clearDatabase(getApplicationContext());

                            Intent i = new Intent(getApplicationContext(), LogIn.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                        finish();

                    }
                });


                NoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupConfrm.cancel();


                    }
                });


            }
        });

        //Card initialization

        CardinitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.adminFlag = 1;
                Intent ini = new Intent(getApplicationContext(), StudentCodeInitialisation.class);
                ini.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ini);
                finish();


            }
        });

    }

    public void clearDatabase(Context context) {
        DatabaseHandler helper = new DatabaseHandler(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete("success_transaction", null, null); //erases everything in the table.
        database.delete("refund_transaction", null, null);
        database.delete("sales_item_list", null, null);
        database.delete("item_sale_transaction", null, null);
        database.delete("fees_transactions", null, null);
        database.delete("recharge_data", null, null);
        database.delete("library_book_transaction", null, null);
        database.delete("payment_transaction", null, null);
        database.delete("attendence_data", null, null);
        database.delete("student_list", null, null);
        database.delete("student_parent", null, null);
        database.delete("refund_error", null, null);
        database.delete("parent_details", null, null);
        database.delete("reason_types", null, null);
        database.delete("create_parent_leave", null, null);
        database.delete("student_class_div", null, null);
        database.delete("teacher_details", null, null);
        database.delete("online_to_recharge", null, null);


        database.close();
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
