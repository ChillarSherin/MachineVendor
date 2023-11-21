package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chillarcards.machinevendor.DisplayAdapters.AttendenceAdapterDisplay;
import com.chillarcards.machinevendor.DisplayAdapters.AttendenceTypeAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.BlockedCardsAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.CategoryAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.DeviceInfoAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.FeeAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.ItemStockAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.ItemTypeAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.ItemlistAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.ItemsaleAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.LibraryAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.OnlineTransAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.ParentLeaveAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.PaymentTransactionAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.PaymentTypeAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.RechargeAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.SuccessTransactionAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.TransactionTypeAdapter;
import com.chillarcards.machinevendor.DisplayAdapters.UserAdapter;
import com.chillarcards.machinevendor.ModelClass.Attendance_Data;
import com.chillarcards.machinevendor.ModelClass.Attendance_Type;
import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.CategoryList;
import com.chillarcards.machinevendor.ModelClass.Create_Leave;
import com.chillarcards.machinevendor.ModelClass.Device_Info;
import com.chillarcards.machinevendor.ModelClass.Fee_Transaction;
import com.chillarcards.machinevendor.ModelClass.ItemList;
import com.chillarcards.machinevendor.ModelClass.ItemStock;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Item_Type;
import com.chillarcards.machinevendor.ModelClass.Library_book_transaction;
import com.chillarcards.machinevendor.ModelClass.Online_Recharge;
import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Payment_Type;
import com.chillarcards.machinevendor.ModelClass.Recharge_Data;
import com.chillarcards.machinevendor.ModelClass.Refund;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.ModelClass.TransactionType;
import com.chillarcards.machinevendor.ModelClass.User;

/**
 * Created by Codmob on 02-08-16.
 */
public class TablesToUpdateActivity extends Activity {
    String action;
    TextView title;

    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    private final List<String> OnlineTransID = new ArrayList<>();
    private final List<String> TransID = new ArrayList<>();
    private final List<String> OrigTransID = new ArrayList<>();
    private final List<String> UserID = new ArrayList<>();
    private final List<String> TransTypeID = new ArrayList<>();
    private final List<String> PrevBal = new ArrayList<>();
    private final List<String> CurrBal = new ArrayList<>();
    private final List<String> CardSerial = new ArrayList<>();
    private final List<String> TimeStamp = new ArrayList<>();
    private final List<String> ServerTS = new ArrayList<>();
    private final List<String> ID = new ArrayList<>();
    private final List<String> Amount = new ArrayList<>();
    private final List<String> rechrgeID = new ArrayList<>();
    private final List<String> TransactionID = new ArrayList<>();
    private final List<String> RechargeAmount = new ArrayList<>();
    private final List<String> RechargeTime = new ArrayList<>();
    private final List<String> PayTypID = new ArrayList<>();
    private final List<String> issuereturn = new ArrayList<>();
    private final List<String> BookID = new ArrayList<>();
    private final List<String> IssueTime = new ArrayList<>();
    private final List<String> Attnddata = new ArrayList<>();
    private final List<String> Inout = new ArrayList<>();
    private final List<String> attndtype = new ArrayList<>();
    private final List<String> Paysourse = new ArrayList<>();
    private final List<String> BillNo = new ArrayList<>();
    private final List<String> TotalAmount = new ArrayList<>();
    private final List<String> SalesTransID = new ArrayList<>();
    private final List<String> ParUserID = new ArrayList<>();
    private final List<String> StudUserID = new ArrayList<>();
    private final List<String> ReasonTypeID = new ArrayList<>();
    private final List<String> ReasonComment = new ArrayList<>();




    OnlineTransAdapter mOnline;
    SuccessTransactionAdapter mSuccess;
    PaymentTransactionAdapter mPayment;
    RechargeAdapter mRecharge;
    LibraryAdapter mlibrary;
    AttendenceAdapterDisplay mAttndance;
    FeeAdapter mFee;
    ItemsaleAdapter mItemsale;
    ParentLeaveAdapter mParent;


    private RecyclerView mRecyclerView;
    private Activity activity;


    DatabaseHandler db ;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)  throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tables_disp_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());

        title= (TextView) findViewById(R.id.textView15);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_tables);

        progressBar= (ProgressBar) findViewById(R.id.progbar1);
        progressBar.setVisibility(View.VISIBLE);

        drawerArrow();

        Bundle b=getIntent().getExtras();
        action=b.getString("action");


        try {

            new displayAsyncTask().execute();

        }catch(Exception e){
            new displayAsyncTask().execute();
        }


//        DisplayTables();






    }

    //Displaying all tables in asynctassk

    private class displayAsyncTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {
            DisplayTables();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void DisplayTables() {

        if(action.equals("success")){

            title.setText("Success Transaction Table");

            TransID.clear();
            UserID.clear();
            TransTypeID.clear();
            PrevBal.clear();
            CurrBal.clear();
            CardSerial.clear();
            TimeStamp.clear();
            ServerTS.clear();

            List<Success_Transaction> successtransaction7 = db.getAllsuccesstoup();
            List<Success_Transaction> successtransaction = db.getAllsxstoupNew();

            successtransaction7.addAll(successtransaction);

            for (Success_Transaction usp : successtransaction7) {

                String testpermission = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                        "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                        + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                        "  Server timestamp  " + usp.getserver_timestamp();
                System.out.println("CHILLAR TablesDispActivity SxsTrnxn " + testpermission);

                TransID.add(usp.gettrans_id());
                UserID.add(String.valueOf(usp.getuser_id()));
                TransTypeID.add(String.valueOf(usp.gettranstypeid()));
                PrevBal.add(String.valueOf(usp.getprevious_balnce()));
                CurrBal.add(String.valueOf(usp.getcurrent_balance()));
                CardSerial.add(usp.getcard_serial());
                TimeStamp.add(usp.gettime_stamp());
                ServerTS.add(usp.getserver_timestamp());

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mSuccess = new SuccessTransactionAdapter(TransID,UserID,TransTypeID,PrevBal,CurrBal,CardSerial,TimeStamp,ServerTS, activity,R.layout.table_item_success ,getApplicationContext());
                    mRecyclerView.setAdapter(mSuccess);

                    mRecyclerView.setVisibility(View.VISIBLE);

                }
            });


        }else if(action.equals("payment")){

            title.setText("Payment Transaction Table");

            TransID.clear();
            ServerTS.clear();
            ID.clear();
            Amount.clear();

            List<Payment_Transaction> paytransaction = db.getAllpaytransactiontoUp();
            List<Payment_Transaction> paytransaction1 = db.getAllpaytransactiontoUpNew();

            paytransaction.addAll(paytransaction1);


            for (Payment_Transaction uspaytrans : paytransaction) {
                String payString = " ID " + uspaytrans.getId() + "trans_id1 "
                        + uspaytrans.gettrans_id() + " Amount " + uspaytrans.getamount() + " Server_time_timestamp " + uspaytrans.getserver_timestamp();
                System.out.println("CHILLAR TablesDispActivity Payment " + payString);

                TransID.add(uspaytrans.gettrans_id());
                Amount.add(String.valueOf(uspaytrans.getamount()));
                ServerTS.add(uspaytrans.getserver_timestamp());
                ID.add(String.valueOf(uspaytrans.getId()));


            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mPayment = new PaymentTransactionAdapter(ID,TransID,Amount,ServerTS, activity,R.layout.table_item_payment ,getApplicationContext());
                    mRecyclerView.setAdapter(mPayment);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });




        }else if(action.equals("recharge")){
            title.setText("RECHARGE");
            rechrgeID.clear();
            TransactionID.clear();
            RechargeAmount.clear();
            RechargeTime.clear();
            PayTypID.clear();
            ServerTS.clear();

            List<Recharge_Data> recharge = db.getAllrechtoUp();
            for (Recharge_Data cn : recharge) {
                String log = "RECHId: " + cn.getrecharge_id() + " ,TransID: " + cn.gettransaction_id()+" Recharge Time "+cn.getrecharge_time()
                        +" RECHRGE ID : "+cn.getRech_amt()+" PayType : "+cn.getpayment_type_id()+" Servertime : "+cn.getserver_timestamp();
                // Writing Contacts to log
                System.out.println("CHILLAR Recharge: "+log);
                rechrgeID.add(String.valueOf(cn.getrecharge_id()));
                TransactionID.add(cn.gettransaction_id());
                RechargeAmount.add(String.valueOf(cn.getRech_amt()));
                RechargeTime.add(cn.getrecharge_time());
                PayTypID.add(String.valueOf(cn.getpayment_type_id()));
                ServerTS.add(cn.getserver_timestamp());

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecharge = new RechargeAdapter(rechrgeID,TransactionID,RechargeAmount,RechargeTime,PayTypID,ServerTS, activity,R.layout.table_item_recharge ,getApplicationContext());
                    mRecyclerView.setAdapter(mRecharge);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });


        }
        else if(action.equals("library")){
            title.setText("LIBRARY");
            ID.clear();
            TransactionID.clear();
            issuereturn.clear();
            BookID.clear();
            IssueTime.clear();
            ServerTS.clear();

            List<Library_book_transaction> library = db.getAlllibtoUp();
            for (Library_book_transaction cn : library) {
                String log = "ID: " + cn.getid() + " ,TransID: " + cn.gettransaction_id()+" Issue return "+cn.getissue_return()
                        +" Book ID : "+cn.getbook_id()+" issue time"+cn.getissue_time()+ " servertiumestamp : "+cn.getserver_timestamp();
                // Writing Contacts to log
                System.out.println("CHILLAR lib: "+log);
                ID.add(String.valueOf(cn.getid()));
                TransactionID.add(String.valueOf(cn.gettransaction_id()));
                issuereturn.add(String.valueOf(cn.getissue_return()));
                BookID.add(cn.getbook_id());
                IssueTime.add(String.valueOf(cn.getissue_time()));
                ServerTS.add(String.valueOf(cn.getserver_timestamp()));


            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mlibrary = new LibraryAdapter(ID,TransactionID,issuereturn,BookID,IssueTime,ServerTS, activity,R.layout.table_item_recharge ,getApplicationContext());
                    mRecyclerView.setAdapter(mlibrary);

                    mRecyclerView.setVisibility(View.VISIBLE);

                }
            });




        } else if(action.equals("attendence")){
            title.setText("ATTENDANCE");
            Attnddata.clear();
            TransactionID.clear();
            Inout.clear();
            attndtype.clear();

            ServerTS.clear();

            List<Attendance_Data> attnd = db.getAllAttendancedatatoUP();
            for (Attendance_Data cn : attnd) {
                String log = "attendence Data : " + cn.getAttendance_data_id() + " ,TransID: " + cn.getTransaction_id()+" INOUT "+cn.getIn_out()
                        +" Attendence Type : "+cn.getAttendance_type_id()+" servertiumestamp : "+cn.getServer_timestamp();
                // Writing Contacts to log
                System.out.println("CHILLAR lib: "+log);
                Attnddata.add(String.valueOf(cn.getAttendance_data_id()));
                TransactionID.add(String.valueOf(cn.getTransaction_id()));
                Inout.add(String.valueOf(cn.getIn_out()));
                attndtype.add(String.valueOf(cn.getAttendance_type_id()));

                ServerTS.add(String.valueOf(cn.getServer_timestamp()));


            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mAttndance = new AttendenceAdapterDisplay(Attnddata,TransactionID,Inout,attndtype,ServerTS, activity,R.layout.table_item_attendancedata ,getApplicationContext());
                    mRecyclerView.setAdapter(mAttndance);

                    mRecyclerView.setVisibility(View.VISIBLE);

                }
            });



        }

        else if(action.equals("fee")){
            title.setText("FEE");
            TransactionID.clear();
            Paysourse.clear();
            BillNo.clear();
            TotalAmount.clear();

            ServerTS.clear();

            List<Fee_Transaction> feeeeee = db.getAllfeetranstoUp();
            for (Fee_Transaction cn : feeeeee) {
                String log = "Trans ID : " + cn.gettrans_id() +" BiLL "+cn.getbill_no()
                        +" Total Amount : "+cn.gettot_am()+" servertiumestamp : "+cn.getserver_timestamp();
                // Writing Contacts to log
                System.out.println("CHILLAR lib: "+log);
                TransactionID.add(String.valueOf(cn.gettrans_id()));
                BillNo.add(String.valueOf(cn.getbill_no()));
                TotalAmount.add(String.valueOf(cn.gettot_am()));

                ServerTS.add(String.valueOf(cn.getserver_timestamp()));


            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mFee = new FeeAdapter(TransactionID,BillNo,TotalAmount,ServerTS, activity,R.layout.table_item_fee ,getApplicationContext());
                    mRecyclerView.setAdapter(mFee);
                    mRecyclerView.setVisibility(View.VISIBLE);


                }
            });



        }

        else if(action.equals("itemsale")){
            title.setText("ITEM SALE");
            SalesTransID.clear();
            TransactionID.clear();
            BillNo.clear();
            TotalAmount.clear();

            ServerTS.clear();

            List<Item_Sale> itemsale2 = db.getAllitemsaletoUpNew();
            List<Item_Sale> itemsale = db.getAllitemsaletoUp();

            itemsale.addAll(itemsale2);

            for (Item_Sale cn : itemsale) {
                String log = "Sales trans ID : " + cn.getsale_trans_id() + " ,Transid : " + cn.gettransaction_id()+" BiLL "+cn.getbill_no()
                        +" Total Amount : "+cn.gettot_amount()+" servertiumestamp : "+cn.getserver_timestamp();
                // Writing Contacts to log
                System.out.println("CHILLAR lib: "+log);
                SalesTransID.add(String.valueOf(cn.getsale_trans_id()));
                TransactionID.add(String.valueOf(cn.gettransaction_id()));
                BillNo.add(String.valueOf(cn.getbill_no()));
                TotalAmount.add(String.valueOf(cn.gettot_amount()));
                ServerTS.add(cn.getserver_timestamp());



            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mItemsale = new ItemsaleAdapter(SalesTransID,TransactionID,BillNo,TotalAmount,ServerTS, activity,R.layout.table_item_itemsale ,getApplicationContext());
                    mRecyclerView.setAdapter(mItemsale);

                    mRecyclerView.setVisibility(View.VISIBLE);

                }
            });




        }else if(action.equals("online")){
            title.setText("Online Transaction");
            TransID.clear();
            OnlineTransID.clear();
            ServerTS.clear();



            List<Online_Recharge> itemlist = db.getAllOnlineToUp();
            for (Online_Recharge cn : itemlist) {


                TransID.add(String.valueOf(cn.getTransID()));
                OnlineTransID.add(cn.getOnlineTransID());
                ServerTS.add(cn.getServerTimestamp());

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mOnline = new OnlineTransAdapter(TransID,OnlineTransID,ServerTS, activity,R.layout.table_item_online_trans ,getApplicationContext());
                    mRecyclerView.setAdapter(mOnline);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });

        }else if(action.equals("refund")){
            title.setText("Refund Transaction");
            TransID.clear();
            OrigTransID.clear();
            ServerTS.clear();



            List<Refund> itemlist = db.getAllRefundtoUp();
            for (Refund cn : itemlist) {


                TransID.add(String.valueOf(cn.getTrans_id()));
                OrigTransID.add(cn.getOrig_trans_id());
                ServerTS.add(cn.getServer_timestamp());

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mOnline = new OnlineTransAdapter(TransID,OrigTransID,ServerTS, activity,R.layout.item_refund_trans ,getApplicationContext());
                    mRecyclerView.setAdapter(mOnline);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });

        }
        else if(action.equals("parent")){
            title.setText("Parent Leave");
            ParUserID.clear();
            ServerTS.clear();
            ReasonComment.clear();
            ReasonTypeID.clear();
            StudUserID.clear();
            TimeStamp.clear();

            List<Create_Leave> itemlist = db.getAllCreateLeaveToUp();
            for (Create_Leave cn : itemlist) {


//                ID.add(String.valueOf(cn.getId()));
                ParUserID.add(cn.getParent_userId());
                ServerTS.add(cn.getServertimestamp());
                ReasonComment.add(cn.getReasonComment());
                ReasonTypeID.add(cn.getReasonTypId());
                StudUserID.add(cn.getStud_userId());
                TimeStamp.add(cn.getTime_stamp());

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mParent = new ParentLeaveAdapter(StudUserID,ParUserID,ReasonTypeID,ReasonComment,TimeStamp,ServerTS, activity,R.layout.table_item_online_trans ,getApplicationContext());
                    mRecyclerView.setAdapter(mParent);

                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });

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
