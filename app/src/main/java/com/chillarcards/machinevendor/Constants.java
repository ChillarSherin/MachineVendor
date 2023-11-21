package com.chillarcards.machinevendor;

import com.chillarcards.machinevendor.ModelClass.Sales_Item;

import java.util.ArrayList;

/**
 * Created by Codmob on 04-07-16.
 */
public class Constants {

//    public static String APP_URL="http://campuswallet.chillarcards.com/machine_api/api_2.2/";
//    public static String APP_URL="http://campuswallet.chillarcards.com/machine_api/api_2.3/";
//    public static String APP_URL="http://campuswallet.chillarcards.com/machine_api/api_2.3.1/";
//    public static String APP_URL="http://campuswallet.chillarcards.com/machine_api/api_2.5/";
//    public static String APP_URL="http://campuswallet.chillarcards.com/machine_api/api_2.4/";
//    public static String APP_URL="http://192.168.0.223/campuswallet/machine_api/";
//    public static String APP_URL="http://campuswallet.chillarcards.com/machine_api/api_1.7.9/";
//    public static String APP_URL="http://campuswallet.chillarcards.com/machine_api/api_2.1/";
//    public static String APP_URL="http://campuswalletdev.chillarcards.com/machine_api/api_1.7.9/";
//    public static String APP_URL="http://192.168.0.223/campuswallettest/machine_api/api_1.7.9/";
//    public static String APP_URL="http://campuswalletdev.chillarcards.com/machine_api/api_2.2/";
//    public static String APP_URL="http://campuswalletdev.chillarcards.com/machine_api/api_2.3/";
//    public static String APP_URL="http://campuswalletdev.chillarcards.com/machine_api/api_2.4/";
//    public static String APP_URL="http://192.168.0.223/campuswallettest/machine_api/api_1.7.9/";

    //MACHINE API
    //public static String APP_URL = "http://cwo.chillarpayments.com/machine_api/api_2.4/";

  //  public static String APP_URL = "http://cwo.chillarpayments.com/machine_api/online/api_1.0/";
    public static String APP_URL = "http://cwo.chillarpayments.com/machine_api/biometric/api_1.2/";

    public static int Qty;
    public static String SerialNo = "";
    public static String TotalAmount;
    public static ArrayList<Sales_Item> sales_items = new ArrayList<>();
    public static String Category = "";
    public static String machineID = "";
    public static String FromTime = "";
    public static String ToTime = "";
    public static int TimePickerSel;
    public static int AsyncFlag = -1;
    public static int adminFlag = 0;
    public static boolean dialogShown = false;
    public static boolean dialogShown1 = false;

}
