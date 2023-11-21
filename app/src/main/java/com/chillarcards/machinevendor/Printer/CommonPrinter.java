package com.chillarcards.machinevendor.Printer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chillarcards.machinevendor.Adapters.BillitemAdapter;
import com.chillarcards.machinevendor.Adapters.PrinterAdapter;
import com.chillarcards.machinevendor.Constants;
import com.chillarcards.machinevendor.DatabaseHandler;
import com.chillarcards.machinevendor.R;
import com.chillarcards.machinevendor.SecondActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by test on 22-07-2016.
 */

public class CommonPrinter extends Activity {

    int pb;
    PrinterAdapter mAdapter;
    String total;
    Button printbill;
    String billno;
    String CommonprintString;
    String localTime, formattedDate;
    TextView totalamtButton;
    int seconds;
    String times;
    ;
    String paymenttypename,paymenttypeId, balanceamount, cardetails;
    String schoolname, schoolplace;
    DatabaseHandler db = new DatabaseHandler(this);
    
    private RecyclerView mRecyclerView;
    private Activity activity;
    private com.chillarcards.machinevendor.Widgets.DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private static final int REQUEST_PERMISSION_CODE = 100;
    CardView mPrintView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_printer);

        drawerArrow();
        Bundle printbundle = getIntent().getExtras();
        CommonprintString = printbundle.getString("print");
        balanceamount = printbundle.getString("print2");
        cardetails = printbundle.getString("print3");

        billno = printbundle.getString("billno");
        paymenttypename = printbundle.getString("typeName");
        paymenttypeId = printbundle.getString("typeId");
        

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        schoolname = editor.getString("schoolname", "");
        schoolname = "Cafe " + schoolname;
        schoolplace = editor.getString("schoolplace", "");

        System.out.println("CODMOBTECH: " + schoolname + " " + schoolplace);

        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTimeString = dateFormat.format(new Date());

        mPrintView = findViewById(R.id.bill_view);
        TextView mShopName = findViewById(R.id.shop_name);
        TextView mShopPlace = findViewById(R.id.shop_place);
        TextView mBillDate = findViewById(R.id.bill_date);
//        TextView mItemName = findViewById(R.id.item_name);
//        TextView mItemQty = findViewById(R.id.item_qty);
//        TextView mItemPrice = findViewById(R.id.item_price);
//        TextView mItemPayAmnt = findViewById(R.id.item_pay_amnt);
        totalamtButton = (TextView) findViewById(R.id.totalamount);

        RecyclerView mRecyclerView = findViewById(R.id.bill_items);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BillitemAdapter mAdapter = new BillitemAdapter(Constants.sales_items, activity, R.layout.adapter_print_bill, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mShopName.setText(db.getSchoolName());
        mShopPlace.setText(schoolplace);
        mBillDate.setText(currentDateTimeString);

        total = Constants.TotalAmount;
        totalamtButton.setText(total);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        times = hour + ":" + minute;

        printbill = (Button) findViewById(R.id.print_bill);
        printbill.setOnClickListener(v -> {
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

                    Constants.sales_items.clear();

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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//              // ap.prn_close();

        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.sales_items.clear();

        Intent ine = new Intent(getApplicationContext(), SecondActivity.class);
        ine.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ine);
        finish();

        //        Intent inte = new Intent(CommonPrinter.this, StoreActivtySubMenu.class);
//        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Bundle b=new Bundle();
//        b.putString("typeId",paymenttypeId);
//        b.putString("typeName",paymenttypename);
//        inte.putExtras(b);
//        startActivity(inte);
//        finish();
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
