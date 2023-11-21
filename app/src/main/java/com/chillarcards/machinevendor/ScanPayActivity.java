package com.chillarcards.machinevendor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.Printer.CommonPrinter;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanPayActivity extends AppCompatActivity {
    private CodeScanner codeScanner;
    private ProgressDialog dialog;
    String userID, schoomachcode;
    ArrayList<Sales_Item> fetchList = new ArrayList<Sales_Item>();
    private final ArrayList<String> item = new ArrayList<String>();
    private final ArrayList<String> price = new ArrayList<String>();
    private final ArrayList<String> quantity = new ArrayList<String>();
    private final ArrayList<String> totamt = new ArrayList<String>();
    String id;
    String pric;
    String qty;
    String amount;
    String name;
    int transtype_id,useridinteger;
    String transtype_name,trans_id ;
    Float total = (float) 0;
    String versionName ="";
    DatabaseHandler db;
    String print = "", print2 = "", print3;
    private static final int REQUEST_CAMERA_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scan);

        dialog = new ProgressDialog(this);
        db = DatabaseHandler.getInstance(getApplicationContext());

        Bundle b=getIntent().getExtras();
        fetchList = getIntent().getParcelableArrayListExtra("salesset");
        transtype_id= Integer.parseInt(b.getString("typeId"));
        transtype_name= b.getString("typeName");

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        userID = String.valueOf(editor.getInt("userid", 0));
        schoomachcode = editor.getString("schlMachCode", "");
        versionName = editor.getString("appVersion", "");
        useridinteger = Integer.parseInt(userID);


        for (int i = 0; i < Constants.sales_items.size(); i++) {

            id = String.valueOf(Constants.sales_items.get(i).getitem_id());
            pric = String.valueOf(Constants.sales_items.get(i).getamount());
            qty = String.valueOf(Constants.sales_items.get(i).getitem_quantity());

            amount = String.valueOf(Float.parseFloat(Constants.sales_items.get(i).getamount()) * Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()));

            name = db.getItemNamebyID(id);


            item.add(name);
            price.add(pric);
            quantity.add(qty);
            totamt.add(amount);

            total = total + Float.parseFloat(amount);
        }

        checkCameraPermission();

        findViewById(R.id.upload_btn).setOnClickListener(v -> onUploadCreateDialog());
    }

    private void onUploadCreateDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Upload Dialog")
                .setView(R.layout.popup_barcode)
                .setCancelable(false)
                .create();
        dialog.show();

        EditText mBarcodeEt = dialog.findViewById(R.id.barcode_et);
        Button yesBtn = dialog.findViewById(R.id.saveBtn);
        Button noBtn = dialog.findViewById(R.id.cancelBtn);

        yesBtn.setOnClickListener(v -> {
            onScannedComplete(mBarcodeEt.getText().toString());
            dialog.dismiss();
        });

        noBtn.setOnClickListener(v -> dialog.dismiss());
    }

    private void setUpQRScanner() {
        codeScanner = new CodeScanner(this, findViewById(R.id.scanner_view));
        codeScanner.setDecodeCallback(result -> {
            runOnUiThread(() -> onScannedComplete(result.getText()));
        });
        // Set up codeScanner as you have in Kotlin
        findViewById(R.id.scanner_view).setOnClickListener(v -> codeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (codeScanner != null) {
            codeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if (codeScanner != null) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }

    private void onScannedComplete(String scannedValue) {
        Log.d("abc_scan", "onScannedComplete: " + scannedValue);
        // Handle the scanned value as needed
        try {
            // Navigate to the next fragment with the scanned value
            Toast.makeText(this, "onScannedComplete: " + scannedValue, Toast.LENGTH_SHORT).show();
            PhpLoader(scannedValue, "222", "1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.d("abc_scan", "reqPerm: " + isGranted);
                if (isGranted) {
                    setUpQRScanner();
                    refreshFragment();
                } else {
                    // Permission denied. Handle accordingly.
                    showAlert();
                  // this.onBackPressed();
                }
            });

    private void refreshFragment() {
        try {
            Toast.makeText(this, "refesh Page", Toast.LENGTH_SHORT).show();
//            Integer id = NavHostFragment.findNavController(this).getCurrentDestination().getId();
//            NavHostFragment.findNavController(this).popBackStack(id, true);
//            NavHostFragment.findNavController(this).navigate(id);
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showAlert();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } else {
            setUpQRScanner();
        }

    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need permission(s)");
        builder.setMessage("Some permissions are required to do the task.");
        builder.setPositiveButton("OK", (dialog, which) -> goToSettings());
        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void PhpLoader(final String code, String pin, final String userType) {

        dialog.setMessage("Please Wait..");
        dialog.setCancelable(false);
        dialog.show();
        final String tag_string_req = "pinauth";

        String type = "";

        if (userType.equals("1")) {
            type = "student";
        } else if (userType.equals("2")) {
            type = "staff";
        }

        String URL = Constants.APP_URL + "r_user_pin.php?" +
                "machineCode=" + schoomachcode +
                "&user_id=" + userID +
                "&code=" + code +
                "&userPin=" + pin +
                "&userType=" + type;

        System.out.println("Urlsuccesstransaction " + URL);

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, response -> {
            Log.d("TESTT", response.toString());

            dialog.dismiss();
            try {

                JSONObject jsobj;
                jsobj = new JSONObject(response);
                JSONObject jsonObject = jsobj.getJSONObject("status");
                String server_respose = jsonObject.getString("code");

                System.out.println("PhpLoader1  code " + server_respose);

                switch (server_respose) {
                    case "200":

                        JSONObject jsonObject1 = jsobj.getJSONObject("code");
                        String cardserial = jsonObject1.getString("cardSerial");

                        //Pass card_serial details for the transaction success
                        PhpLoaderTest1(cardserial,"");

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", cardserial);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                        break;
                    case "201":

                        Toast.makeText(getApplicationContext(), "Pin not matched!", Toast.LENGTH_SHORT).show();

                        break;
                    case "202":
                        //Pin not set. Set new Pin
                        Toast.makeText(getApplicationContext(), "Set New Pin", Toast.LENGTH_SHORT).show();

                        break;
                    case "error":

                        String server_respose1 = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(), server_respose1, Toast.LENGTH_SHORT).show();

                        break;
                }


            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }


        }, error -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

            finish();

        });


        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    
    //PASS DATA TO SERVER


    private void PhpLoaderTest1(final String cardSerial, String dataExtra) throws UnsupportedEncodingException {

        dialog.setMessage("Please Wait..");
        dialog.setCancelable(false);
        dialog.show();
        final String tag_string_req = "transSuccess";

        trans_id = db.lastTransID();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTimeString = df.format(new Date());

        String encodedDate = URLEncoder.encode(currentDateTimeString.trim(), "UTF-8").trim();


        System.out.println("Urlsuccesstransaction Date " + currentDateTimeString);
        System.out.println("Urlsuccesstransaction Date encode" + encodedDate);

//        if(dataExtra.equals("")){
//
//
//        }else{
//            String URL  = Constants.APP_URL + "c_successTransaction.php?" +
//                    "machineCode=" + schoomachcode +
//                    "&current_user=" + useridinteger +
//                    "&user_id=" + userID +
//                    "&transaction_id=" + trans_id +
//                    "&card_serial=" + cardSerial +
//                    "&transaction_type_id=" + transtype_id +
//                    "&amount=" + total +
//                    "&time_stamp=" + encodedDate +
//                    "&current_version=" + versionName+
//                    "&score="+dataExtra;
//        }

        String apiUrl  = Constants.APP_URL + "c_successTransaction.php?" +
                "machineCode=" + schoomachcode +
                "&current_user=" + useridinteger +
                "&user_id=" + userID +
                "&transaction_id=" + trans_id +
                "&card_serial=" + cardSerial +
                "&transaction_type_id=" + transtype_id +
                "&amount=" + total +
//                    "&time_stamp=2023-11-18 03:10:17";
                "&time_stamp=" + URLEncoder.encode(currentDateTimeString, "UTF-8") +
//                "&time_stamp=" + currentDateTimeString +
                "&current_version=" + versionName;


        System.out.println("Urlsuccesstransaction " + apiUrl);

        final StringRequest strReq = new StringRequest(Request.Method.POST, apiUrl.trim(), response -> {

            Log.d("Urlsuccesstransaction ScanPay", response);

            try {
                JSONObject jsobj;
                jsobj = new JSONObject(response);
                JSONObject jsonObject = jsobj.getJSONObject("status");
                String server_response = jsonObject.getString("code");

                System.out.println("PhpLoader1  code " + server_response);

                if (server_response.equals("success")) {
                    String currBal = jsobj.getString("current_balance");
                    String prevBal = jsobj.getString("prev_balance");

                    PhpUpdate(cardSerial, trans_id, dialog);
                } else if (server_response.equals("error")) {
                    dialog.dismiss();
                    String message = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            finish();
        });
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void PhpUpdate(final String cardSerial, String trans_id, final ProgressDialog dialog) {

        String tag_string_req = "update";

        String URL = Constants.APP_URL + "u_successTransaction.php?" +
                "machineCode=" + schoomachcode +
                "&user_id=" + userID +
                "&transaction_id=" + trans_id +
                "&card_serial=" + cardSerial;

        System.out.println("Urlsuccesstransaction " + URL);

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, response -> {
                    Log.d("Urlsuccesstransaction ScanPay 2", response);

                    dialog.dismiss();

                    try {

                        JSONObject jsobj;
                        jsobj = new JSONObject(response);
                        JSONObject jsonObject = jsobj.getJSONObject("status");
                        String server_respose = jsonObject.getString("code");

                        System.out.println("PhpLoader1  code " + server_respose);

                        if (server_respose.equals("success")) {

                            String currBal = jsobj.getString("current_balance");
                            String prevBal = jsobj.getString("prev_balance");

                            DBReadWrite(cardSerial, currBal, prevBal);

                        }
                        else if (server_respose.equals("error")) {

                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();

                finish();

            }
        });


        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                2500,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void DBReadWrite(String cardSerial, String currBal, String prevBal) throws UnsupportedEncodingException {


        Float Prev = Float.valueOf(prevBal);
        Float Current = Float.valueOf(currBal);

        String billno = trans_id.substring(12, 16);


        System.out.println("CHILLER: STORECARDPAYMENT: Cardserial " + cardSerial + " PreviousBAl: " + "tOTAL:" + total + Prev + " BillNo: " + billno);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String encodedDate = df.format(new Date());

        String currentDateTimeString = URLEncoder.encode(encodedDate, "UTF-8");


        Item_Sale item_sale = new Item_Sale(trans_id, transtype_id, useridinteger, Prev, Current, cardSerial, currentDateTimeString, "", trans_id, billno, String.valueOf(total), "");

        db.addSuccesstransaction(item_sale);
        db.addsaleItem(item_sale);


        for (int i = 0; i < Constants.sales_items.size(); i++) {

            System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(i).getitem_id());


            String id = String.valueOf(Constants.sales_items.get(i).getitem_id());
            String pric = String.valueOf(Constants.sales_items.get(i).getamount());
            String qty = String.valueOf(Constants.sales_items.get(i).getitem_quantity());

            Sales_Item sales_item = new Sales_Item(trans_id, id, qty, pric, "");

            db.addsale(sales_item);

        }

        List<Sales_Item> sales_item = db.getAllsale();
        for (Sales_Item slesitm : sales_item) {

            System.out.println("ELDHOSSSSSS::SalesItemID:" + slesitm.getitem_id() + " SalesTransID: " + slesitm.getsales_trans_id() + " ServerTS:  " + slesitm.getserver_timestamp());

        }


        for (int j = 0; j < Constants.sales_items.size(); j++) {
            System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(j).getitem_id());

            String test = "Item ID : " + Constants.sales_items.get(j).getitem_id() + " Price : " + Constants.sales_items.get(j).getamount() + " Quantity : " + Constants.sales_items.get(j).getitem_quantity();

            String id = String.valueOf(Constants.sales_items.get(j).getitem_id());
            String name = db.getItemNamebyID(id);
            String pric = String.valueOf(Constants.sales_items.get(j).getamount());
            String qty = String.valueOf(Constants.sales_items.get(j).getitem_quantity());

            String amount = String.valueOf(Float.parseFloat(Constants.sales_items.get(j).getamount()) *
                    Integer.parseInt(Constants.sales_items.get(j).getitem_quantity()));

            if (name.length() > 6) {
                name = name.substring(0, 6);
            }

            print = print + ", " + name + "*" + qty + "*" + pric + "*" + amount  + "," + "\n";
            System.out.println("CHILLER: STORECARDPAYMENT print : 1 " + print);


        }

        System.out.println("CHILLER: STORECARDPAYMENT print : " + print);

        billno = trans_id.substring(12, 16);

        print2 = "  Balance : " + Float.valueOf(Current) + "\n";
        print3 = "  UserSer    " + cardSerial + "\n" +
                "   PrevBal    " + Float.parseFloat(prevBal) + "\n";


        Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), CommonPrinter.class);
        Bundle printbundle = new Bundle();
        printbundle.putString("print", print);
        printbundle.putString("print2", print2);
        printbundle.putString("print3", print3);
        printbundle.putString("billno", trans_id);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtras(printbundle);
        startActivity(i);
        finish();

    }

}
