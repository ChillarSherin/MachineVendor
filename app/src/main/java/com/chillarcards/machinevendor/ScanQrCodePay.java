//package com.chillarcards.machinevendor;
//
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.hardware.Camera;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.provider.Settings;
//import android.util.Log;
//import android.util.SparseArray;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.toolbox.StringRequest;
//import com.chillarcards.machinevendor.ModelClass.Sales_Item;
//import com.google.android.gms.vision.CameraSource;
//import com.google.android.gms.vision.Detector;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//public class ScanQrCodePay extends Activity {
//
//    private static final String TAG = "abc_ScannerActivity";
//    private static final int REQUEST_PERMISSION_CAMERA = 400;
//
//    private CameraSource mCameraSource;
//    private Camera mCamera;
//    private boolean mFlashMode = false;
//    private SurfaceView svCameraView;
//    ArrayList<Sales_Item> fetchList = new ArrayList<Sales_Item>();
//    int transtype_id;
//    String transtype_name;
//    private static final int REQUEST_CAMERA_PERMISSION = 100;
//    private ProgressDialog dialog;
//    String userID, schoomachcode;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_scan);
//
//        Bundle b=getIntent().getExtras();
//        fetchList = getIntent().getParcelableArrayListExtra("salesset");
//        transtype_id= Integer.parseInt(b.getString("typeId"));
//        transtype_name= b.getString("typeName");
////        userType = b.getString("type"); //type=1-> Student ,type=2-> Staff
//
//        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
//        userID = String.valueOf(editor.getInt("userid", 0));
//        schoomachcode = editor.getString("schlMachCode", "");
//
//        System.out.println("CHILLAR:fetchlist " + fetchList.size()+":"+transtype_id);
//
//        dialog = new ProgressDialog(this);
//
//        svCameraView = (SurfaceView) findViewById(R.id.sv_camera_view);
//        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
//        ImageView imgFlash = (ImageView) findViewById(R.id.img_flash);
//
//        checkCameraPermission();
//
//        imgClose.setOnClickListener(view -> finish());
//        imgFlash.setOnClickListener(view -> flashOnButton());
//
//    }
//
//    private void flashOnButton() {
//        mCamera = getCamera(mCameraSource);
//        if (mCamera != null) {
//            try {
//                Camera.Parameters param = mCamera.getParameters();
//                param.setFlashMode(!mFlashMode ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
//                mCamera.setParameters(param);
//                mFlashMode = !mFlashMode;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private Camera getCamera(@NonNull CameraSource cameraSource) {
//        java.lang.reflect.Field[] declaredFields = CameraSource.class.getDeclaredFields();
//        for (java.lang.reflect.Field field : declaredFields) {
//            if (field.getType() == Camera.class) {
//                field.setAccessible(true);
//                try {
//                    return (Camera) field.get(cameraSource);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//        }
//        return null;
//    }
//
//    private void setUpQRScanner() {
//        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
//                .setBarcodeFormats(Barcode.ALL_FORMATS).build();
//
//        mCameraSource = new CameraSource.Builder(this, barcodeDetector)
//                .setAutoFocusEnabled(true).build();
//
//        Log.d(TAG, "+---------> setUpQRScanner <---------+");
//        SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                try {
//                    Log.d(TAG, "+---------> surfaceCreated <---------+");
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    mCameraSource.start(surfaceHolder);
//                } catch (IOException e) {
//                    Log.e(TAG, "surfaceCreated: ", e);
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//                Log.d(TAG, "surfaceChanged: ");
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//                Log.d(TAG, "surfaceDestroyed: ");
//                mCameraSource.stop();
//            }
//        };
//
//        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                SparseArray<Barcode> barcodes = detections.getDetectedItems();
//
//                if (barcodes.size() != 0) {
//                    Log.d(TAG, "Scanned value: " + barcodes.valueAt(0).displayValue);
//                    doOps(barcodes);
//                }
//            }
//        });
//
//        svCameraView.getHolder().addCallback(surfaceCallback);
//    }
//
//
//    private void doOps(SparseArray<Barcode> barcodes) {
//        try {
//            clearScannedData();
//            String scannedValue = barcodes.valueAt(0).displayValue;
//            Toast.makeText(this, "Scanned Barcode: " + scannedValue, Toast.LENGTH_SHORT).show();
//            PhpLoader(scannedValue, "xxx", "1");
//
//            Log.d(TAG, "Scanned Barcode: " + scannedValue);
//            onScannedComplete(scannedValue != null ? scannedValue : "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void clearScannedData() throws IOException {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mCameraSource.start(svCameraView.getHolder());
//    }
//
//    private void onScannedComplete(String scannedValue) {
//        mCameraSource.stop();
//        runOnUiThread(() -> {
//            Toast.makeText(this, "----------> exiting screen now", Toast.LENGTH_SHORT).show();
//        });
//
//        Log.d(TAG, "onScannedComplete: " + scannedValue);
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            try {
//                finish();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, 3000);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                setUpQRScanner();
//            } else {
//                showAlert();
//            }
//        }
//    }
//
//    private void checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
//                PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                showAlert();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//            }
//        } else {
//            setUpQRScanner();
//        }
//    }
//
//    private void refreshActivity() {
//        try {
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void PhpLoader(final String code, String pin, final String userType) {
//
//        dialog.setMessage("Please Wait..");
//        dialog.setCancelable(false);
//        dialog.show();
//        final String tag_string_req = "pinauth";
//
//        String type = "";
//
//        if (userType.equals("1")) {
//            type = "student";
//        } else if (userType.equals("2")) {
//            type = "staff";
//        }
//
//        String URL = Constants.APP_URL + "r_user_pin.php?machineCode=" + schoomachcode + "&user_id=" + userID + "&code=" + code + "&userPin=" + pin + "&userType=" + type;
//
//        System.out.println("Urlsuccesstransaction " + URL);
//
//        final StringRequest strReq = new StringRequest(Request.Method.POST,
//                URL, response -> {
//                    Log.d("TESTT", response.toString());
//
//                    dialog.dismiss();
//                    try {
//
//                        JSONObject jsobj;
//                        jsobj = new JSONObject(response);
//                        JSONObject jsonObject = jsobj.getJSONObject("status");
//                        String server_respose = jsonObject.getString("code");
//
//
//                        System.out.println("PhpLoader1  code " + server_respose);
//
//                        switch (server_respose) {
//                            case "200":
//
//                                JSONObject jsonObject1 = jsobj.getJSONObject("code");
//                                String cardserial = jsonObject1.getString("cardSerial");
//                                Intent returnIntent = new Intent();
//                                returnIntent.putExtra("result", cardserial);
//                                setResult(Activity.RESULT_OK, returnIntent);
//                                finish();
//
//                                break;
//                            case "201":
//
//                                Toast.makeText(getApplicationContext(), "Pin not matched!", Toast.LENGTH_SHORT).show();
//
//                                break;
//                            case "202":
//                                //Pin not set. Set new Pin
//                                Toast.makeText(getApplicationContext(), "Set New Pin", Toast.LENGTH_SHORT).show();
//
//                                break;
//                            case "error":
//
//                                String server_respose1 = jsonObject.getString("message");
//
//                                Toast.makeText(getApplicationContext(), server_respose1, Toast.LENGTH_SHORT).show();
//
//                                break;
//                        }
//
//
//                    } catch (Exception e) {
//
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//
//                    }
//
//
//                }, error -> {
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_SHORT).show();
//
//                    finish();
//
//                });
//
//
//        // Adding request to request queue
//        App.getInstance().addToRequestQueue(strReq, tag_string_req);
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                2500,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//
//    }
//
//    private void showAlert() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Need permission(s)");
//        builder.setMessage("Some permissions are required to do the task.");
//        builder.setPositiveButton("OK", (dialog, which) -> goToSettings());
//        builder.setNeutralButton("Cancel", null);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    private void goToSettings() {
//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivity(intent);
//    }
//}
