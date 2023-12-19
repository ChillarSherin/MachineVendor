package com.chillarcards.machinevendor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.Printer.ReportItemWisePrinter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportActivity extends Activity {

    Button Submit;

    TextView fromdate;
    Date d1;
    DatabaseHandler db;

    String formatedDate = "", activityname = "";
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private int mYear, mMonth, mDay;

    int translength;
    int tot_qty;
    Float tot_amt;
    Float TotalAmount;
    String transactionID, transactionTypeID, successcardSerial;
    String trans_type_id;
    String successPrint = "";
    private List<String> SaleItemId = new ArrayList<>();
    private final List<String> SaleItemPrint = new ArrayList<>();
    BluetoothAdapter bluetoothAdapter;
    RelativeLayout printFrameView;
    CardView mPrintView;
    private static final int REQUEST_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws CursorIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_report_activity);

        db = DatabaseHandler.getInstance(getApplicationContext());

        Bundle b = getIntent().getExtras();
        activityname = b.getString("activity");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        String schoolPlace = editor.getString("schoolplace", "");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTimeString = df.format(new Date());

        drawerArrow();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        RelativeLayout menuFrameView = findViewById(R.id.menu_frame);
        RelativeLayout dateFrameView = findViewById(R.id.date_frame);
        printFrameView = findViewById(R.id.print_frame);
        mPrintView = findViewById(R.id.print_view);
        fromdate = findViewById(R.id.fromdate);
        Submit = findViewById(R.id.btn_submit);
        Button mPrintReport = findViewById(R.id.btn_print);


        Button Item_wise = findViewById(R.id.item_wise);
        Button total = findViewById(R.id.total);

        TextView mShopName = findViewById(R.id.shop_name);
        TextView mShopPlace = findViewById(R.id.shop_place);
        TextView mBillDate = findViewById(R.id.bill_date);
        TextView mTotalTran = findViewById(R.id.transtotal);
        TextView mTotalTranAmnt = findViewById(R.id.transtotalamount);

        mShopName.setText(db.getSchoolName());
        mShopPlace.setText(schoolPlace);
        mBillDate.setText(currentDateTimeString);

        if (!activityname.equals("store")) {
            Item_wise.setVisibility(View.GONE);
        }

        //SUBMIT DATE
        Submit.setOnClickListener(v -> {
            String from = fromdate.getText().toString();

            if (fromdate.getText().toString().equals("")) {
                Toast.makeText(ReportActivity.this, "Please select Date ", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    d1 = sdf.parse(from);
                    System.out.println("ReportActivity Date: From " + d1);
                } catch (ParseException ex) {
                    Logger.getLogger(ReportActivity.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Converting D1
                String mStringDate = String.valueOf(d1);
                String oldFormat = "E MMM dd HH:mm:ss Z yyyy";
                String newFormat = "yyyy/MM/dd";


                SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
                Date myDate = null;
                try {
                    myDate = dateFormat.parse(mStringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
                formatedDate = timeFormat.format(myDate);

                System.out.println("ReportActivity result " + formatedDate);


                //CHANGE DESIGN VIEW
                dateFrameView.setVisibility(View.GONE);
                menuFrameView.setVisibility(View.VISIBLE);
                printFrameView.setVisibility(View.GONE);
                if (!activityname.equals("store")) {
                    Item_wise.setVisibility(View.GONE);
                }
                trans_type_id = String.valueOf(db.getTransTypID(Constants.Category));

//                formatedDate = b.getString("Date");
//                activityname = b.getString("activity");


//                Intent i = new Intent(getApplicationContext(), ReportSecondActivity.class);
//                Bundle b1 = new Bundle();
//                b1.putString("Date", formatedDate);
//                b1.putString("activity", activityname);
//                i.putExtras(b1);
//                startActivity(i);


            }
        });

        //CHOOSE DATE
        fromdate.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(ReportActivity.this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        // Display Selected date in textbox
                        fromdate.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }, mYear, mMonth, mDay);
            dpd.show();
        });

        //TWO MENU HANDLING
        total.setOnClickListener(v -> {
            System.out.println("CHILLAR sherin trans_type_id" + "sssssssssssssss"+trans_type_id);

            List<Success_Transaction> successtransactions;
            if (activityname.equals("tabledisplay")) {
                successtransactions = db.getSuccessdatewise(formatedDate);
                translength = db.getSuccessdatewise(formatedDate).size();
            } else {
                successtransactions = db.getSuccessdatewise(formatedDate, trans_type_id);
                translength = db.getSuccessdatewise(formatedDate, trans_type_id).size();

            }
            System.out.println("CHILLAR sherin trans_type_id" + "sssssssssssssss"+activityname);

            TotalAmount = (float) 0;
            for (Success_Transaction usp : successtransactions) {
                String testpermissionnew = "   transaction id  " + usp.gettrans_id() + " user_id :" + usp.getuser_id() +
                        "   transaction_type_id:   " + usp.gettranstypeid() + "  previous balance :  " + usp.getprevious_balnce() + "  current balance   "
                        + usp.getcurrent_balance() + "  crad serial " + usp.getcard_serial() + " time stamp " + usp.gettime_stamp() +
                        "  Server timestamp  " + usp.getserver_timestamp();

                if (usp.getprevious_balnce() > usp.getcurrent_balance()) {
                    TotalAmount = TotalAmount + (usp.getprevious_balnce() - usp.getcurrent_balance());
                } else {
                    TotalAmount = TotalAmount + (usp.getcurrent_balance() - usp.getprevious_balnce());
                }


                System.out.println("CHILLAR Total Amount" + TotalAmount);
                System.out.println("CHILLAR secact success transaction datewise" + testpermissionnew);

                transactionID = usp.gettrans_id();
                transactionTypeID = String.valueOf(usp.gettranstypeid());
                successcardSerial = usp.getcard_serial();
                successPrint = successPrint + "   " + transactionID + "  " + transactionTypeID + "    " + successcardSerial + "\n";


                System.out.println("CHILLAR Success transaction details" + transactionID + "  " + transactionTypeID + "   " + successcardSerial);
                System.out.println("CHILLAR Success transaction details" + successPrint);

            }

            System.out.println("CHILLAR Success final" + successPrint);
            System.out.println("CHILLAR Final TotalAmount" + TotalAmount);
            System.out.println("CHILLAR Final TransLength" + translength);

            dateFrameView.setVisibility(View.GONE);
            menuFrameView.setVisibility(View.GONE);
            printFrameView.setVisibility(View.VISIBLE);

            mTotalTran.setText(String.valueOf(translength));
            mTotalTranAmnt.setText(String.valueOf(TotalAmount));
//
//            Intent i = new Intent(getApplicationContext(), ReportPrinter.class);
//            Bundle printbundle = new Bundle();
//            //  printbundle.putString("successprint", successPrint);
//            printbundle.putInt("TotalTransaction", translength);
//            printbundle.putFloat("TotalAmount", TotalAmount);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.putExtras(printbundle);
//            startActivity(i);
//            finish();

        });

        Item_wise.setOnClickListener(v -> {
            SaleItemId.clear();
            SaleItemPrint.clear();

            SaleItemId = db.getdistinctItemid(formatedDate, trans_type_id);

            if (SaleItemId.size() > 0) {

                for (int i = 0; i < SaleItemId.size(); ++i) {

                    tot_qty = 0;
                    tot_amt = (float) 0;

                    System.out.println("Chillar NamebyID: " + db.getItemNamebyID(SaleItemId.get(i)));
                    List<Sales_Item> sale1 = db.getAllsalebyId(Integer.parseInt(SaleItemId.get(i)), formatedDate, trans_type_id);
                    for (Sales_Item cn : sale1) {

                        String log = "Sales Item ID : " + cn.getitem_id() + ".... " + cn.getamount() + ".... " + cn.getitem_quantity() + ".... " + cn.getsales_trans_id();
                        // Writing Contacts to log
                        System.out.println("CHILLAR Sales111: " + log);

                        tot_qty = tot_qty + Integer.valueOf(cn.getitem_quantity());
                        tot_amt = tot_amt + Float.valueOf(cn.getamount()) * Integer.valueOf(cn.getitem_quantity());
                    }


                    System.out.println("CHILLAR Qty n Amt: " + tot_qty + " ... " + tot_amt);
                    String ItemName = db.getItemNamebyID(SaleItemId.get(i));

                    if (ItemName.length() > 20) {
                        System.out.println("CHILLAR: Substrng >20");
                        ItemName = ItemName.substring(0, 20);
                    } else {

                        System.out.println("CHILLAR: Substrng <20");
                        String filler = "";
                        int len = 20 - ItemName.length();
                        for (int j = 0; j < len; ++j) {
                            filler = filler + " ";
                        }
                        ItemName = ItemName + filler;
                    }
                    SaleItemPrint.add(ItemName + "         " + tot_qty);
                }

                String print = "";

                for (int i = 0; i < SaleItemPrint.size(); ++i) {
                    print = print + SaleItemPrint.get(i) + "\n";

                }

                System.out.println("CHILLAR : PRint: " + print);

                Intent i = new Intent(getApplicationContext(), ReportItemWisePrinter.class);
                Bundle printbundle = new Bundle();
                printbundle.putString("Print", print);
                printbundle.putString("tot_items", String.valueOf(SaleItemPrint.size()));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(printbundle);
                startActivity(i);
                finish();

                Toast.makeText(this, "pending UI", Toast.LENGTH_SHORT).show();

                dateFrameView.setVisibility(View.GONE);
                menuFrameView.setVisibility(View.GONE);
                printFrameView.setVisibility(View.VISIBLE);

                mTotalTran.setText(translength);
                mTotalTranAmnt.setText(String.valueOf(translength));


            } else {

                Toast.makeText(ReportActivity.this, "No Sale Transactions in DB!", Toast.LENGTH_SHORT).show();
            }

        });

        //PRINT
        mPrintReport.setOnClickListener(view -> {
            // Check if the necessary permissions are granted
            if (checkPermissions()) {
                // If permissions are granted, proceed with printing
                printDocument(mPrintView);
            } else {
                // If permissions are not granted, request them
                requestPermissions();
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

        imageView.setOnClickListener(v -> {
            onBackPressed();
            finish();

        });
    }


    private boolean checkPermissions() {
        // Check if the necessary permissions are granted
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        // Request the necessary permissions
        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.BLUETOOTH,
                        android.Manifest.permission.BLUETOOTH_ADMIN,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.ACCESS_WIFI_STATE
                },
                REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Handle the result of the permission request
        if (requestCode == REQUEST_PERMISSION_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // If all permissions are granted, proceed with printing
                printDocument(mPrintView);
            } else {
                // If any permission is not granted, handle accordingly (e.g., show a message)
                Log.e("PrintActivity", "Permission not granted. Cannot proceed with printing.");
            }
        }
    }

    private void printDocument(View viewToPrint) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
            @Override
            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                try (OutputStream outputStream = new FileOutputStream(destination.getFileDescriptor())) {
                    convertViewToPdf(viewToPrint, outputStream);
                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } catch (IOException e) {
                    Log.e("PrintActivity", "Error writing to output stream", e);
                }
            }

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }

                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Print Job")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .build();

                callback.onLayoutFinished(pdi, true);
            }
        };

        printManager.print("Print Job", printAdapter, null);
    }

    private void convertViewToPdf(View view, OutputStream outputStream) {
        // Convert the RelativeLayout view to a printable format (e.g., PDF)
        // In this example, it uses PdfDocument for simplicity
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(view.getWidth(), view.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Draw the view on the PDF page
        view.draw(page.getCanvas());

        pdfDocument.finishPage(page);

        // Write the PDF to the output stream
        try {
            pdfDocument.writeTo(outputStream);
        } catch (IOException e) {
            Log.e("PrintActivity", "Error writing PDF to output stream", e);
        }

        pdfDocument.close();
    }

}
