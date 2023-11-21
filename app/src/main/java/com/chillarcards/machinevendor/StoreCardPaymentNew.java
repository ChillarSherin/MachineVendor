package com.chillarcards.machinevendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chillarcards.machinevendor.ModelClass.Blocked_Cards_Info;
import com.chillarcards.machinevendor.ModelClass.Item_Sale;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.Printer.CommonPrinter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StoreCardPaymentNew extends Activity {

    public static final int AUTH_FIRST_SECT = 3;
    public static final int AUTH_SECND_SECT = 7;
    public static final int AUTH_THIRD_SECT = 11;
    public static final int AUTH_FOURTH_SECT = 15;
    public static final int AUTH_FIFTH_SECT = 19;
    public static final int AUTH_SIXTH_SECT = 23;


    public static final int READ_ONLINE_TRANS_ID = 2;
    public static final int READ_SCHOOL_ID = 4;
    public static final int READ_CARD_SERIAL = 5;
    public static final int READ_STATUS = 6;
    public static final int READ_CURR_BAL = 8;
    public static final int READ_PREV_BAL = 9;
    public static final int READ_EXPIRY = 12;
    public static final int READ_IN_OUT = 13;
    public static final int READ_ATTND_TYPE = 14;
    public static final int READ_LASTTRANS_MC_ID = 16;
    public static final int READ_TRANS_ID = 17;
    public static final int READ_TRANS_AMT = 18;
    public static final int READ_LAST_RECH_MC_ID = 20;
    public static final int READ_LAST_RECH_ID = 21;
    public static final int READ_LAST_RECH_AMT = 22;



    public static final int WRITE_ONLINE_TRANS_ID = 2;
    public static final int WRITE_CURR_BAL = 8;
    public static final int WRITE_PREV_BAL = 9;
    public static final int WRITE_LASTTRANS_MC_ID = 16;
    public static final int WRITE_TRANS_ID = 17;
    public static final int WRITE_TRANS_AMT = 18;

    public static final int WRITE_LAST_RECH_MC_ID = 20;
    public static final int WRITE_LAST_RECH_ID = 21;
    public static final int WRITE_LAST_RECH_AMT = 22;


    public static final int KEYA = 0x0a;
    public static final int KEYB = 0x0b;
    public static final int TARGET_NUM = 1;

    char[] key1 = { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff };
    char[] oldkey1 = { 0x41, 0x26, 0x72, 0x2a, 0x23, 0x3e };
    char[] key2 = { 0x52, 0x34, 0x3f, 0x36, 0x21, 0x37 };
    char[] key3 = { 0x53, 0x23, 0x61, 0x37, 0x5e, 0x39 };
    char[] key4 = { 0x42, 0x21, 0x62, 0x40, 0x49, 0x23 };
    char[] key5 = { 0x50, 0x24, 0x72, 0x40, 0x41, 0x5e };
    char[] key6 = { 0x43, 0x25, 0x63, 0x31, 0x5e, 0x39 };
    char[] keyNew ;

    StringBuffer read_serial = new StringBuffer();
    StringBuffer read_current = new StringBuffer();
    StringBuffer read_school = new StringBuffer();
    StringBuffer read_expiry = new StringBuffer();
    StringBuffer read_status = new StringBuffer();
    StringBuffer target_detected = new StringBuffer();
    StringBuffer target_id = new StringBuffer();

    


    DatabaseHandler db ;

    Button tap_the_card;

    int authZeroSectRead=-1,authFirstSectRead=-1,authSecondSectRead=-1,authThirdSectRead=-1,authZeroSectWrite=-1,authSecondSectWrite=-1,authFourthSectWrite=-1;
    int rdOnlineTransId=-1,rdSchoolId=-1,rdCardSerial=-1,rdCardStatus=-1,rdCurrentBal=-1,rdPrevBal=-1,rdExpiry=-1;
    int wrOnlineTransId=-1,wrCurrBal=-1,wrPrevBal=-1,wrMachineId=-1,wrTransId=-1,wrTransAmt=-1;


    Date expirydate,today1;

    Context context;

    int ret_poweron;
    int ret_fieldon;
    int tardtc;
    int tama_on;

    int transtype_id;
    String transtype_name;
    String machine_id;
    String resultCardSerial,resultPrevbal,resultCurrenBal,resultCardSerial1;
    String User_name,Pass_word;
    int userid;
    String schooid,schoomachcode,machineserial,mSchoolId;
    String currentDateTimeString,currentDateTimeString1;
    ProgressBar progressBar;


    String TAG="StoreCardPayment";
    ArrayList<Sales_Item> fetchList = new ArrayList<Sales_Item>();
    private final List<String> BlkdCards = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfid);
        initialise();

        Float total= (float)0;
        for (int i = 0; i < Constants.sales_items.size(); i++) {
            System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(i).getitem_id());
            String amount = String.valueOf(Float.parseFloat(Constants.sales_items.get(i).getamount()) * Integer.parseInt(Constants.sales_items.get(i).getitem_quantity()));

            total = total + Float.parseFloat(amount);

        }


        final Float finalTotal = total;
//        tap_the_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                findViewById(R.id.incProgressBar).setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.VISIBLE);
//                String trans_id = db.lastTransID();
//                tap_the_card.setEnabled(false);
//                tap_the_card.setClickable(false);
//
//                if (SecondActivity.ret_fieldon == 0) {
//
//                    System.out.println("Tama open :" + tama_on);
//
//                    tardtc = apis.tama_detectTarget(TARGET_NUM,
//                            target_detected, target_id);
//
//                    if (tardtc == 0) {
//
//                        try {
//
//                            authFirstSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                    AUTH_SECND_SECT, key2, KEYA);
//
//                            if (authFirstSectRead == 0) {
//
//                                rdSchoolId = apis.tama_read_target(
//                                        READ_SCHOOL_ID, read_school);
//                                rdCardSerial = apis.tama_read_target(
//                                        READ_CARD_SERIAL, read_serial);
//                                rdCardStatus = apis.tama_read_target(
//                                        READ_STATUS, read_status);
//
//
//                                if (rdSchoolId == 0 && rdCardSerial == 0 && rdCardStatus == 0) {
//
//
//                                    String schId = String.valueOf(read_school);
//                                    resultCardSerial = String.valueOf(read_serial);
//                                    String cardstat = String.valueOf(read_status);
//                                    int cardstat1 = Integer.parseInt(cardstat);
//
//
//
//                                    /* Expiry Date Reading */
//                                    /*authThirdSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                            AUTH_FOURTH_SECT, key4, KEYA);
//
//
//                                    if (authThirdSectRead == 0) {
//                                        rdExpiry = apis.tama_read_target(
//                                                READ_EXPIRY, read_expiry);
//
//                                        if (rdExpiry == 0) {
//
//                                            String expdate = String.valueOf(read_expiry);
//                                            String expdate1 = expdate.replaceFirst("^0*", "");
//
//
//                                            cardExpired(expdate1);
//
//
//                                        }
//
//                                    }*/
//
//
//                                    /* Checking if card is active (0- inactive, 1 - active)*/
//                                    if (cardstat1 == 1) {
//
//                                        /*Check whether the card is blocked*/
//                                        if (!cardBlocked(resultCardSerial)) {
//
//                                            /*Check whether the card belongs to this school*/
//                                            if (schId.replace(" ", "").equalsIgnoreCase(mSchoolId)) {
//
//
//                                                authSecondSectRead = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                        AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                if (authSecondSectRead == 0) {
//                                                    rdCurrentBal = apis.tama_read_target(
//                                                            READ_CURR_BAL, read_current);
//
//
//                                                    if (rdCurrentBal == 0) {
//
//
//                                                        String prev = String.valueOf(read_current).replaceFirst("^0+(?!$)", "");
//                                                        Float Prev = Float.valueOf(prev);
//
//                                                        if(Prev >=finalTotal){
//
//                                                            Float Current = (Prev - finalTotal);
//
//                                                            char[] curBal = fillerFn(String.valueOf(Current));
//                                                            char[] prevBal = String.valueOf(read_current).toCharArray();
//
//
//                                                            authSecondSectWrite = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                                    AUTH_THIRD_SECT, key3, KEYA);
//
//
//                                                            if (authSecondSectWrite == 0) {
//
//
//                                                                wrCurrBal = apis.tama_write_target(WRITE_CURR_BAL,
//                                                                        curBal, curBal.length);
//
//                                                                wrPrevBal = apis.tama_write_target(WRITE_PREV_BAL,
//                                                                        prevBal, prevBal.length);
//
//                                                                if(wrCurrBal==0 && wrPrevBal == 0) {
//
//
//                                                                    char[] machine=fillerFn(machine_id);
//                                                                    char[] transs=fillerFn(trans_id);
//                                                                    char[] tott= fillerFn(String.valueOf(finalTotal));
//
//                                                                    authFourthSectWrite = apis.tama_authenticate_mifare(TARGET_NUM,
//                                                                            AUTH_FIFTH_SECT, key5, KEYA);
//
//
//                                                                    if (authFourthSectWrite == 0) {
//
//                                                                        wrMachineId = apis.tama_write_target(WRITE_LASTTRANS_MC_ID,
//                                                                                machine, machine.length);
//
//                                                                        wrTransId = apis.tama_write_target(WRITE_TRANS_ID,
//                                                                                transs, transs.length);
//                                                                        wrTransAmt = apis.tama_write_target(WRITE_TRANS_AMT,
//                                                                                tott, tott.length);
//
//
//                                                                        if(wrTransId==0){
//
//                                                                            Toast.makeText(context, "SUCCESS!!", Toast.LENGTH_SHORT).show();
//                                                                            progressBar.setVisibility(View.GONE);
//                                                                            findViewById(R.id.incProgressBar).setVisibility(View.GONE);
//
//                                                                            DBReadWrite(trans_id,resultCardSerial,finalTotal,Current,Prev);
//                                                                        }else{
//                                                                            Toast.makeText(context, "Error 107!", Toast.LENGTH_SHORT).show();
//                                                                            finish();
//                                                                        }
//
//                                                                    } else {
//                                                                        Toast.makeText(context, "Error 106!", Toast.LENGTH_SHORT).show();
//                                                                        finish();
//                                                                    }
//
//
//
//
//
//
//
//
//                                                                }else{
//                                                                    Toast.makeText(context, "Error 105!", Toast.LENGTH_SHORT).show();
//                                                                    finish();
//
//                                                                }
//
//                                                            }else{
//                                                                Toast.makeText(context, "Error 104!", Toast.LENGTH_SHORT).show();
//                                                                finish();
//                                                            }
//
//                                                        }else{
//                                                            Toast.makeText(context, "Sorry, Insufficient Balance!", Toast.LENGTH_SHORT).show();
//                                                            finish();
//                                                        }
//
//
//                                                    }else{
//                                                        Toast.makeText(context, "Error 103!", Toast.LENGTH_SHORT).show();
//                                                        finish();
//                                                    }
//                                                }else{
//                                                    Toast.makeText(context, "Error 102!", Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                }
//
//
//
//
//
//                                            } else{
//                                                Toast.makeText(context, "Sorry, Card doesn't belong to this school!", Toast.LENGTH_SHORT).show();
//                                                finish();
//                                            }
//                                        } else{
//                                            finish();
//                                        }
//                                    }else{
//                                        Toast.makeText(context, "Sorry, Card Not Active!", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//                                } else{
//                                    Toast.makeText(context, "Error 101!", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }
//                            }else{
//
//                                Toast.makeText(context, "Error 100!", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//
//
//                        } catch (Exception e) {
//                            tap_the_card.setClickable(false);
//                            tap_the_card.setEnabled(false);
//                            progressBar.setVisibility(View.GONE);
//                            findViewById(R.id.incProgressBar).setVisibility(View.GONE);
//                            e.printStackTrace();
//                            System.out.println("CODMOB: number format exception" + e.toString());
//                            Toast.makeText(context, "Target Moved.Try again.", Toast.LENGTH_SHORT)
//                                    .show();
//                            AlertDialog.Builder alert = new AlertDialog.Builder(
//                                    StoreCardPaymentNew.this);
//                            alert.setTitle("RFID");
//
//                            alert.setCancelable(false);
//                            alert.setMessage("Target Moved.Try again.");
//
//                            DialogInterface.OnClickListener listener;
//                            alert.setPositiveButton(
//                                    "OK",
//                                    new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(
//                                                DialogInterface dialog,
//                                                int which) {
//
//                                            Constants.sales_items.clear();
//
//                                            dialog.dismiss();
//
//
//                                            new FieldRestartTask().execute();
//
//
//                                        }
//                                    });
//                            alert.show();
//
//
//                        }
//                    }else{
//
//                        tap_the_card.setEnabled(false);
//                        tap_the_card.setClickable(false);
//                        progressBar.setVisibility(View.GONE);
//                        findViewById(R.id.incProgressBar).setVisibility(View.GONE);
//
//                        Toast.makeText(context, "Target detect Failed", Toast.LENGTH_SHORT)
//                                .show();
//                        AlertDialog.Builder alert = new AlertDialog.Builder(
//                                StoreCardPaymentNew.this);
//                        alert.setTitle("RFID");
//
//                        alert.setCancelable(false);
//                        alert.setMessage("Target detect Failed");
//
//                        DialogInterface.OnClickListener listener;
//                        alert.setPositiveButton(
//                                "OK",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int which) {
//
//                                        dialog.dismiss();
//
//                                        new FieldRestartTask().execute();
//
//
//                                    }
//                                });
//                        alert.show();
//
//                    }
//                }
//            }
//        });


    }




    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    private void initialise() {

        context = getApplicationContext();

        db = DatabaseHandler.getInstance(getApplicationContext());

        tap_the_card = (Button) findViewById(R.id.tap);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        SharedPreferences editor = getSharedPreferences("Chillar", MODE_PRIVATE);
        User_name = editor.getString("username", "");
        Pass_word= editor.getString("password", "");
        userid= editor.getInt("userid",0);
        schooid = editor.getString("schoolId", "");
        schoomachcode= editor.getString("schlMachCode", "");
        machineserial= editor.getString("machineserial", "");


        Bundle b=getIntent().getExtras();
        fetchList = getIntent().getParcelableArrayListExtra("salesset");
        transtype_id= Integer.parseInt(b.getString("typeId"));
        transtype_name= b.getString("typeName");
        System.out.println("CHILLAR:fetchlist " + fetchList.size()+":"+transtype_id);


        machine_id= db.getMachineID();
        mSchoolId= db.getSchoolId();



        DateFormat df = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);


        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        Date today11 = Calendar.getInstance().getTime();
        currentDateTimeString1 = df1.format(today11);

    }


    private char[] fillerFn(String data){

        StringBuilder filler = new StringBuilder();
        int len = 16 - data.length();

        for (int j = 0; j < len; ++j) {

            filler.append("0");

        }

        String dataResult = filler + data;

        System.out.println("Chillar Filler Data: "+ dataResult);

        char[] result=dataResult.toCharArray();


        return result;
    }

    private boolean cardExpired(String expDate){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            expirydate = sdf.parse(expDate);
            today1 = sdf.parse(currentDateTimeString1);

        } catch (ParseException ex) {

            ex.printStackTrace();
        }

        if(today1.after(expirydate)){

            return true;

        }else{

            return false;

        }

    }


    private void DBReadWrite(String trans_id, String resultCardSerial, Float finalTotal, Float current, Float prev) {

        String billno = trans_id.substring(12, 16);

        System.out.println("CHILLER: STORECARDPAYMENT: Cardserial " + resultCardSerial + " PreviousBAl: " + "tOTAL:" + finalTotal + prev + " BillNo: " + billno);

        Item_Sale item_sale = new Item_Sale(trans_id, transtype_id, userid, prev, current, resultCardSerial, currentDateTimeString, "", trans_id, billno, String.valueOf(finalTotal), "");

        db.addSuccesstransaction(item_sale);
        db.addsaleItem(item_sale);


        System.out.println("MachineTest: size " + Constants.sales_items.size() + "  " + fetchList.size());

        if (Constants.sales_items.size() < fetchList.size()) {

            System.out.println("SalesItemnotcorrect");

            Constants.sales_items = fetchList;

        }


        for (int i = 0; i < Constants.sales_items.size(); i++) {

            System.out.println("CHILLER: STORECARDPAYMENT salesitems: " + Constants.sales_items.get(i).getitem_id());


            String id = String.valueOf(Constants.sales_items.get(i).getitem_id());
            String pric = String.valueOf(Constants.sales_items.get(i).getamount());
            String qty = String.valueOf(Constants.sales_items.get(i).getitem_quantity());

            Sales_Item sales_item = new Sales_Item(trans_id, id, qty, pric, "");
            db.addsale(sales_item);


        }

        printBill(billno,trans_id,current,prev);


    }

    private void printBill(String billno, String trans_id, Float current, Float prev) {


        String print="",print2="",print3="";

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

            print = print + "     " + name + "    " + qty + "    " + pric + "    " + amount + "\n";


        }

        billno = trans_id.substring(12, 16);

        print2 = "  Balance : " + current + "\n";
        print3 = "     CardSer    " + resultCardSerial + "\n" +
                "     PrevBal    " + prev + "\n";

        Intent i = new Intent(getApplicationContext(), CommonPrinter.class);
        Bundle printbundle = new Bundle();
        printbundle.putString("print", print);
        printbundle.putString("print2", print2);
        printbundle.putString("print3", print3);
        printbundle.putString("billno", trans_id);
        printbundle.putString("typeId", String.valueOf(transtype_id));
        printbundle.putString("typeName", transtype_name);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtras(printbundle);
        startActivity(i);
        finish();

    }

    private boolean cardBlocked(String card) {


        List<Blocked_Cards_Info> blocked_cards_infos = db.getAllBlockCardInfo();
        for (Blocked_Cards_Info cn : blocked_cards_infos) {
            String log = "Id: " + cn.getBlocked_cards_id() + " ,Name: " + cn.getCard_serial() ;

            String cardSerial=cn.getCard_serial();
            BlkdCards.add(cardSerial);

            System.out.println("CHILLAR Blocked: "+log);
        }


        for(int i=0;i<BlkdCards.size();i++)
        {

            System.out.println("Card serial  "+BlkdCards.get(i));
            if (card.equalsIgnoreCase(BlkdCards.get(i)))
            {
                System.out.println("Card serial Blkdcard exiting ");
                Toast.makeText(getApplicationContext(), "Sorry, This card is blocked", Toast.LENGTH_SHORT).show();
                return true;
            }

        }

        return false;
    }


//    private class FieldRestartTask extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            apis.tama_close();
//
//
//            try {
//                int ret_fieldoff = SecondActivity.rfidService.rfid_rffield_off();
//
//                System.out.println("Field Off:" + ret_fieldoff);
//
//                int ret_poweroff = SecondActivity.rfidService.rfid_poweroff();
//
//                System.out.println("PowerOFF :" + ret_poweroff);
//
//                ret_poweron = SecondActivity.rfidService.rfid_poweron();
//                System.out.println("RFID PowerOn :" + ret_poweron);
//
//                if (ret_poweron == 0 || ret_poweron == 2) {
//
//
//                    ret_fieldon = SecondActivity.rfidService.rfid_rffield_on();
//
//                    System.out.println("RFID FieldOn :" + ret_fieldon);
//
//                    tama_on = apis.tama_open();
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Module open Failed", Toast.LENGTH_SHORT).show();
//                    System.out.println("RFID Module open Failed");
//                }
//
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Constants.AsyncFlag=0;
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            Constants.AsyncFlag=1;
//            progressBar.setVisibility(View.GONE);
//            tap_the_card.setClickable(true);
//            tap_the_card.setEnabled(true);
//        }
//    }

    @Override
    public void onBackPressed() {

        if (Constants.AsyncFlag == 0) {
            Toast.makeText(getApplicationContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
        } else {
            Constants.AsyncFlag = -1;
            super.onBackPressed();
        }
    }


}
