//package com.chillarcards.machinevendor;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.graphics.pdf.PdfDocument;
//import android.os.Bundle;
//import android.os.CancellationSignal;
//import android.os.ParcelFileDescriptor;
//import android.print.PageRange;
//import android.print.PrintAttributes;
//import android.print.PrintDocumentAdapter;
//import android.print.PrintDocumentInfo;
//import android.print.PrintManager;
//import android.util.Log;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//public class PrintActivity extends AppCompatActivity {
//
//    private static final int REQUEST_PERMISSION_CODE = 100;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_print);
//
//        // Check if the necessary permissions are granted
//        if (checkPermissions()) {
//            // If permissions are granted, proceed with printing
//            printDocument(findViewById(R.id.ac));
//        } else {
//            // If permissions are not granted, request them
//            requestPermissions();
//        }
//    }
//
//    private boolean checkPermissions() {
//        // Check if the necessary permissions are granted
//        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissions() {
//        // Request the necessary permissions
//        ActivityCompat.requestPermissions(this,
//                new String[]{
//                        android.Manifest.permission.BLUETOOTH,
//                        android.Manifest.permission.BLUETOOTH_ADMIN,
//                        android.Manifest.permission.INTERNET,
//                        android.Manifest.permission.ACCESS_NETWORK_STATE,
//                        android.Manifest.permission.ACCESS_WIFI_STATE
//                },
//                REQUEST_PERMISSION_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        // Handle the result of the permission request
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            boolean allPermissionsGranted = true;
//
//            for (int result : grantResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    allPermissionsGranted = false;
//                    break;
//                }
//            }
//
//            if (allPermissionsGranted) {
//                // If all permissions are granted, proceed with printing
//                printDocument(findViewById(R.id.your_relative_layout_id));
//            } else {
//                // If any permission is not granted, handle accordingly (e.g., show a message)
//                Log.e("PrintActivity", "Permission not granted. Cannot proceed with printing.");
//            }
//        }
//    }
//
//    private void printDocument(View viewToPrint) {
//        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
//
//        PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
//            @Override
//            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
//                try (OutputStream outputStream = new FileOutputStream(destination.getFileDescriptor())) {
//                    convertViewToPdf(viewToPrint, outputStream);
//                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
//                } catch (IOException e) {
//                    Log.e("PrintActivity", "Error writing to output stream", e);
//                }
//            }
//
//            @Override
//            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
//                if (cancellationSignal.isCanceled()) {
//                    callback.onLayoutCancelled();
//                    return;
//                }
//
//                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Print Job")
//                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                        .build();
//
//                callback.onLayoutFinished(pdi, true);
//            }
//        };
//
//        printManager.print("Print Job", printAdapter, null);
//    }
//
//    private void convertViewToPdf(View view, OutputStream outputStream) {
//        // Convert the RelativeLayout view to a printable format (e.g., PDF)
//        // In this example, it uses PdfDocument for simplicity
//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(view.getWidth(), view.getHeight(), 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//
//        // Draw the view on the PDF page
//        view.draw(page.getCanvas());
//
//        pdfDocument.finishPage(page);
//
//        // Write the PDF to the output stream
//        try {
//            pdfDocument.writeTo(outputStream);
//        } catch (IOException e) {
//            Log.e("PrintActivity", "Error writing PDF to output stream", e);
//        }
//
//        pdfDocument.close();
//    }
//}
