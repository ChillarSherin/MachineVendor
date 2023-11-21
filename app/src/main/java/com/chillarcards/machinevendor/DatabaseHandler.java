package com.chillarcards.machinevendor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.chillarcards.machinevendor.ModelClass.Parent;
import com.chillarcards.machinevendor.ModelClass.Parent_Student;
import com.chillarcards.machinevendor.ModelClass.Payment_Transaction;
import com.chillarcards.machinevendor.ModelClass.Payment_Type;
import com.chillarcards.machinevendor.ModelClass.Reason;
import com.chillarcards.machinevendor.ModelClass.Recharge_Data;
import com.chillarcards.machinevendor.ModelClass.Refund;
import com.chillarcards.machinevendor.ModelClass.Refund_Error;
import com.chillarcards.machinevendor.ModelClass.Sales_Item;
import com.chillarcards.machinevendor.ModelClass.StudentClassDiv;
import com.chillarcards.machinevendor.ModelClass.StudentList;
import com.chillarcards.machinevendor.ModelClass.Success_Transaction;
import com.chillarcards.machinevendor.ModelClass.Teacher_Details;
import com.chillarcards.machinevendor.ModelClass.TransactionType;
import com.chillarcards.machinevendor.ModelClass.User;
import com.chillarcards.machinevendor.ModelClass.User_Permission_Data;

public class DatabaseHandler extends SQLiteOpenHelper {


    // Database Name
    public static final String DATABASE_NAME = "chillarMachine";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 98;
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_OPERATIONAL_DEVICE_DATA = "operational_device_data";
    private static final String TABLE_DEVICE_INFO = "device_info";
    private static final String TABLE_USER = "user";
    private static final String TABLE_BLOCKED_CARDS_INFO = "blocked_cards_info";
    private static final String TABLE_BLOCKED_CARDS_INFO_TEMP = "blocked_cards_info_temp";
    private static final String TABLE_USER_PERMISSION_DATAA = "user_permission_dataa";
    private static final String TABLE_ATTENDENCE_TYPE = "attendence_type";
    private static final String TABLE_ATTENDENCE_DATA = "attendence_data";
    private static final String TABLE_TEACHER_ATTENDENCE_DATA = "teacher_attendence_data";
    private static final String TABLE_TRANSACTION_TYPE = "transaction_type";
    private static final String TABLE_SUCCESS_TRANSACTION = "success_transaction";
    private static final String TABLE_PAYMENT_TRANSACTION = "payment_transaction";
    private static final String TABLE_PAYMENT_TYPE = "payment_type";
    private static final String TABLE_RECHARGE_DATA = "recharge_data";
    private static final String TABLE_CATEGORY_LIST = "category_list";
    private static final String TABLE_ITEM_STOCK = "item_stock";
    private static final String TABLE_ITEM_LIST = "item_list";
    private static final String TABLE_SALES_ITEM_LIST = "sales_item_list";
    private static final String TABLE_ITEM_SALE_TRANSACTION = "item_sale_transaction";
    private static final String TABLE_FEES_TRANSACTIONS = "fees_transactions";
    private static final String TABLE_FEES_LIST = "fees_list";
    private static final String TABLE_FEES_STRUCTURE = "fees_structure";
    private static final String TABLE_LIBRARY_BOOK_TRANSACTION = "library_book_transaction";
    private static final String TABLE_ITEM_TYPE = "item_type";
    private static final String TABLE_REFUND_TRANSACTION = "refund_transaction";
    private static final String TABLE_ONLINE_TO_RECHARGE = "online_to_recharge";
    private static final String TABLE_STUDENT_LIST = "student_list";
    private static final String TABLE_REFUND_ERROR = "refund_error";
    private static final String TABLE_PARENT_DETAILS = "parent_details";
    private static final String TABLE_STUDENT_PARENT = "student_parent";
    private static final String TABLE_REASON_TYPES = "reason_types";
    private static final String TABLE_CREATE_PARENT_LEAVE = "create_parent_leave";
    private static final String TABLE_STUDENT_CLASS_DIV = "student_class_div";
    private static final String TABLE_TEACHER_DETAILS = "teacher_details";
    // Common Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MACHINE_ID = "machine_id";
    private static final String KEY_SERVER_TIMESTAMP = "server_timestamp";
    private static final String KEY_TRANSACTION_TYPE_ID = "transaction_type_id";
    private static final String KEY_TRANSACTION_ID = "transaction_id";
    private static final String KEY_CARD_SERIAL = "card_serial";
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_SALES_TRANS_ID = "sales_trans_id";
    private static final String KEY_BILL_NO = "bill_no";
    private static final String KEY_TOTAL_AMOUNT = "total_amount";
    private static final String KEY_FEES_TRANS_ID = "fees_trans_id";
    private static final String KEY_FEES_ID = "fees_id";
    private static final String KEY_REASON_TYPE_ID = "reason_type_id";
    private static final String KEY_REASON_TYPE = "reason_type";
    private static final String KEY_REASON_COMMENT = "reason_comment";
    private static final String KEY_STUDENT_CLASS_NAME = "student_class_name";
    private static final String KEY_STUDENT_DIV = "student_div";
    private static final String KEY_STUDENT_CLASS_DIV = "student_class_div";
    // Contacts Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    //Operational Device Data Columns names
    private static final String KEY_LAST_TRANS_ID = "last_trans_id";
    private static final String KEY_LAST_MACHINE_TRANS_TIME = "last_machine_trans_time";
    private static final String KEY_LAST_SERVER_TRANS_TIME = "last_server_trans_time";
    private static final String KEY_LAST_BLOCKCARDS_UPDATED = "last_blockcards_updated";
    //Device info columns names
    private static final String KEY_SERIAL_NO = "serial_no";
    private static final String KEY_SCHOOL_ID = "school_id";
    private static final String KEY_SCHOOL_NAME = "school_name";
    private static final String KEY_SCHOOL_PLACE = "school_place";
    private static final String KEY_DEVICE_LAST_TRANS_ID = "device_last_trans_id";
    private static final String KEY_MAIN_SERVER_URL = "main_server_url";
    private static final String KEY_MAIN_UPLOAD_PATH = "main_upload_path";
    //User table Columns names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_STUDENT_USER_ID = "student_user_id";
    private static final String KEY_PARENT_USER_ID = "parent_user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_PASSWORD = "password";
    //Blocked cards info table Columns names
    private static final String KEY_BLOCKED_CARDS_ID = "blocked_cards_id";
    //User permission data table Column names
    private static final String KEY_PERMISSION_ID = "permission_id";
    private static final String KEY_PERMISSION = "permission";
    //Attendence Type table Column names
    private static final String KEY_ATTENDENCE_TYPE_NAME = "attendence_type_name";
    //Attendence data table column names
    private static final String KEY_ATTENDENCE_DATA_ID = "attendence_data_id";
    private static final String KEY_IN_OUT = "in_out";
    private static final String KEY_ATTENDENCE_TYPE_ID = "attendence_type_id";
    //Transaction type table column names
    private static final String KEY_TRANSACTION_TYPE_NAME = "transaction_type_name";
    //Success transaction table column names
    private static final String KEY_PREV_BALANCE = "prev_balance";
    private static final String KEY_CURRENT_BALANCE = "current_balance";
    private static final String KEY_TIME_STAMP = "time_stamp";
    //Payment transaction table column names
    private static final String KEY_AMOUNT = "amount";
    //PAyment type table column names
    private static final String KEY_PAYMENT_TYPE_NAME = "payment_type_name";
    //Recharge data table column names
    private static final String KEY_RECHARGE_ID = "recharge_id";
    private static final String KEY_RECHARGE_AMOUNT = "recharge_amt";
    private static final String KEY_RECHARGE_TIME = "recharge_time";
    private static final String KEY_PAYMENT_TYPE_ID = "payment_type_id";
    private static final String KEY_CATEGORY_NAME = "category_name";

    //Category list table column names
    private static final String KEY_CATEGORY_SHORT_NAME = "category_short_name";
    //Item stock table column names
    private static final String KEY_STOCK_ID = "stock_id";
    private static final String KEY_ITEM_STOCK = "item_stock";
    private static final String KEY_REORDER_WARNING = "reorder_warning";
    //Item list table column names
    private static final String KEY_ITEM_CODE = "item_code";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_SHORTNAME = "item_shortname";
    private static final String KEY_PRICE = "price";
    private static final String KEY_STOCK_STATUS = "stock_status";
    private static final String KEY_ITEM_TYPE_ID = "item_type_id";
    //sales item list table column names
    private static final String KEY_SALES_ITEM_ID = "_id";
    private static final String KEY_ITEM_QUANTITY = "item_quantity";
    //Item sale transaction table column names
    private static final String KEY_PAYMENT_TYPE = "payment_type";
    //Library book transaction
    private static final String KEY_ISSUE_RETURN = "issue_return";
    private static final String KEY_BOOK_ID = "book_id";
    private static final String KEY_ISSUE_TIME = "issue_time";
    //Fees transactions table column names
    private static final String KEY_PAYMENT_SOURCE = "payment_source";
    //item type
    private static final String KEY_ITEM_TYPE_NAME = "item_type_name";
    private static final String KEY_ORIGINAL_TRANSACTION_ID = "original_transaction_id";
    private static final String KEY_ONLINE_TRANSACTION_ID = "original_transaction_id";
    //Fees structure table column names
    private static final String KEY_FEES_STRUCTURE_NAME = "fees_structure_name";

    //Fees list table column names
    //Student list table column names
    private static final String KEY_STUDENT_ID = "student_id";
    private static final String KEY_STUDENT_NAME = "student_name";
    private static final String KEY_STUDENT_CLASS = "student_class";
    private static final String KEY_STUDENT_DIVISION = "student_division";
    private static final String KEY_STUDENT_CARD_SERIAL = "student_card_serial";
    private static final String KEY_PARENT_NAME = "parent_name";
    private static final String KEY_TEACHER_NAME = "teacher_name";


    //Teacher list table
    private static final String KEY_TEACHER_USER_ID = "teacher_user_id";
    // Table Create Statements
    // Operational device data table create statement
    private static final String CREATE_TABLE_OPERATIONAL_DEVICE_DATA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_OPERATIONAL_DEVICE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAST_TRANS_ID
            + " TEXT," + KEY_MACHINE_ID + " TEXT," + KEY_LAST_MACHINE_TRANS_TIME
            + " DATETIME," + KEY_LAST_SERVER_TRANS_TIME + " DATETIME," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_DEVICE_INFO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DEVICE_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MACHINE_ID
            + " TEXT," + KEY_SERIAL_NO + " TEXT," + KEY_SCHOOL_ID
            + " TEXT," + KEY_SCHOOL_NAME + " TEXT," + KEY_SCHOOL_PLACE + " TEXT," + KEY_DEVICE_LAST_TRANS_ID + " TEXT," + KEY_MAIN_SERVER_URL + " TEXT," + KEY_MAIN_UPLOAD_PATH + " TEXT," + KEY_LAST_BLOCKCARDS_UPDATED + " VARCHAR" + ")";
    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
            + KEY_USER_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"
            + KEY_PASSWORD + " TEXT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_BLOCKED_CARDS_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_BLOCKED_CARDS_INFO + "("
            + KEY_BLOCKED_CARDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CARD_SERIAL + " TEXT,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_BLOCKED_CARDS_INFO_NEW = "CREATE TABLE IF NOT EXISTS " + TABLE_BLOCKED_CARDS_INFO + "("
            + KEY_BLOCKED_CARDS_ID + " INTEGER," + KEY_CARD_SERIAL + " TEXT PRIMARY KEY,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_USER_PERMISSION_DATAA = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_PERMISSION_DATAA + "("
            + KEY_PERMISSION_ID + " INTEGER PRIMARY KEY," + KEY_TRANSACTION_TYPE_ID + " INTEGER,"
            + KEY_USER_ID + " INTEGER," + KEY_PERMISSION + " INTEGER," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_ATTENDENCE_TYPE = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDENCE_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ATTENDENCE_TYPE_NAME + " TEXT,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_ATTENDENCE_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDENCE_DATA + "("
            + KEY_ATTENDENCE_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_IN_OUT + " INTEGER," + KEY_ATTENDENCE_TYPE_ID + " INTEGER," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_TRANSACTION_TYPE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTION_TYPE + "("
            + KEY_TRANSACTION_TYPE_ID + " INTEGER PRIMARY KEY," + KEY_TRANSACTION_TYPE_NAME + " TEXT,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_SUCCESS_TRANSACTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SUCCESS_TRANSACTION + "(" + KEY_TRANSACTION_ID + " VARCHAR PRIMARY KEY," + KEY_USER_ID
            + " INTEGER," + KEY_TRANSACTION_TYPE_ID + " INTEGER," + KEY_PREV_BALANCE
            + " FLOAT," + KEY_CURRENT_BALANCE + " FLOAT," + KEY_CARD_SERIAL + " TEXT," + KEY_TIME_STAMP + " DATETIME," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_PAYMENT_TRANSACTION = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENT_TRANSACTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_BILL_NO + " TEXT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_AMOUNT + " FLOAT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_PAYMENT_TYPE = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENT_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PAYMENT_TYPE_NAME + " TEXT,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_RECHARGE_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_RECHARGE_DATA + "("
            + KEY_RECHARGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_RECHARGE_TIME + " DATETIME," + KEY_RECHARGE_AMOUNT + " FLOAT," + KEY_PAYMENT_TYPE_ID + " INTEGER," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_CATEGORY_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_LIST + "("
            + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_TYPE_ID + " INTEGER," + KEY_CATEGORY_NAME + " TEXT,"
            + KEY_CATEGORY_SHORT_NAME + " TEXT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_ITEM_STOCK = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_STOCK + "("
            + KEY_STOCK_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_ID + " INTEGER,"
            + KEY_ITEM_STOCK + " INTEGER," + KEY_REORDER_WARNING + " TEXT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_ITEM_LIST = "CREATE TABLE " + TABLE_ITEM_LIST + "("
            + KEY_ITEM_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_CODE + " TEXT,"
            + KEY_ITEM_NAME + " TEXT," + KEY_ITEM_SHORTNAME + " TEXT," + KEY_CATEGORY_ID + " INTEGER,"
            + KEY_PRICE + " FLOAT," + KEY_STOCK_STATUS + " TEXT, " + KEY_ITEM_TYPE_ID + " INTEGER," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_SALES_ITEM_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_SALES_ITEM_LIST + "("
            + KEY_SALES_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SALES_TRANS_ID + " TEXT,"
            + KEY_ITEM_ID + " INTEGER," + KEY_ITEM_QUANTITY + " INTEGER," + KEY_AMOUNT + " FLOAT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_ITEM_SALE_TRANSACTION = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_SALE_TRANSACTION + "("
            + KEY_SALES_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_BILL_NO + " TEXT," + KEY_TOTAL_AMOUNT + " FLOAT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_FEES_TRANSACTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_FEES_TRANSACTIONS + "("
            + KEY_FEES_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_BILL_NO + " TEXT," + KEY_TOTAL_AMOUNT + " FLOAT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_FEES_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_FEES_LIST + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FEES_ID + " TEXT,"
            + KEY_FEES_TRANS_ID + " INTEGER," + KEY_TOTAL_AMOUNT + " FLOAT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_FEES_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_FEES_STRUCTURE + "("
            + KEY_FEES_ID + " INTEGER PRIMARY KEY," + KEY_FEES_STRUCTURE_NAME + " TEXT,"
            + KEY_TOTAL_AMOUNT + " FLOAT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_LIBRARY_BOOK_TRANSACTION = "CREATE TABLE IF NOT EXISTS " + TABLE_LIBRARY_BOOK_TRANSACTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_ISSUE_RETURN + " TEXT," + KEY_BOOK_ID + " INTEGER," + KEY_ISSUE_TIME + " DATETIME," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_ITEM_TYPE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_TYPE + "("
            + KEY_ITEM_TYPE_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_TYPE_NAME + " INTEGER,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_REFUND_TRANSACTION = "CREATE TABLE IF NOT EXISTS " + TABLE_REFUND_TRANSACTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR," + KEY_ORIGINAL_TRANSACTION_ID + " VARCHAR,"
            + KEY_AMOUNT + " FLOAT," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_ONLINE_TO_RECHARGE = "CREATE TABLE IF NOT EXISTS " + TABLE_ONLINE_TO_RECHARGE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR," + KEY_ONLINE_TRANSACTION_ID + " VARCHAR,"
            + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_STUDENT_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENT_LIST + "("
            + KEY_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STUDENT_CARD_SERIAL + " VARCHAR," + KEY_STUDENT_CLASS + " VARCHAR," + KEY_STUDENT_DIVISION + " VARCHAR," +
            KEY_USER_ID + " VARCHAR," + KEY_STUDENT_NAME + " VARCHAR" + ")";
    private static final String CREATE_TABLE_REFUND_ERROR = "CREATE TABLE IF NOT EXISTS " + TABLE_REFUND_ERROR + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_CARD_SERIAL + " VARCHAR," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_TABLE_PARENT_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_PARENT_DETAILS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID + " VARCHAR," + KEY_PARENT_NAME + " VARCHAR,"
            + KEY_CARD_SERIAL + " VARCHAR" + ")";
    private static final String CREATE_STUDENT_PARENT = "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENT_PARENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STUDENT_USER_ID + " VARCHAR,"
            + KEY_PARENT_USER_ID + " VARCHAR" + ")";
    private static final String CREATE_TABLE_REASON_TYPES = "CREATE TABLE IF NOT EXISTS " + TABLE_REASON_TYPES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_REASON_TYPE_ID + " VARCHAR,"
            + KEY_REASON_TYPE + " VARCHAR" + ")";
    private static final String CREATE_CREATE_PARENT_LEAVE = "CREATE TABLE IF NOT EXISTS " + TABLE_CREATE_PARENT_LEAVE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STUDENT_USER_ID + " VARCHAR,"
            + KEY_PARENT_USER_ID + " VARCHAR," + KEY_REASON_TYPE_ID + " VARCHAR," + KEY_REASON_COMMENT + " VARCHAR," + KEY_TIME_STAMP + " DATETIME," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static final String CREATE_STUDENT_CLASS_DIV = "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENT_CLASS_DIV + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STUDENT_CLASS_NAME + " VARCHAR,"
            + KEY_STUDENT_DIV + " VARCHAR," + KEY_STUDENT_CLASS_DIV + " VARCHAR" + ")";
    private static final String CREATE_TABLE_TEACHER_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_TEACHER_DETAILS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TEACHER_USER_ID + " VARCHAR," + KEY_TEACHER_NAME + " VARCHAR,"
            + KEY_SCHOOL_ID + " VARCHAR," + KEY_CARD_SERIAL + " VARCHAR" + ")";
    private static final String CREATE_TABLE_TEACHER_ATTENDENCE_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_TEACHER_ATTENDENCE_DATA + "("
            + KEY_ATTENDENCE_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TRANSACTION_ID + " VARCHAR,"
            + KEY_IN_OUT + " INTEGER," + KEY_ATTENDENCE_TYPE_ID + " INTEGER," + KEY_CARD_SERIAL + " VARCHAR," + KEY_SERVER_TIMESTAMP + " DATETIME" + ")";
    private static DatabaseHandler mInstance = null;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHandler getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHandler(ctx);
        }
        return mInstance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_TABLE_OPERATIONAL_DEVICE_DATA);
        db.execSQL(CREATE_TABLE_DEVICE_INFO);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_BLOCKED_CARDS_INFO_NEW);
        db.execSQL(CREATE_TABLE_USER_PERMISSION_DATAA);
        db.execSQL(CREATE_TABLE_ATTENDENCE_TYPE);
        db.execSQL(CREATE_TABLE_ATTENDENCE_DATA);
        db.execSQL(CREATE_TABLE_TRANSACTION_TYPE);
        db.execSQL(CREATE_TABLE_SUCCESS_TRANSACTION);
        db.execSQL(CREATE_TABLE_PAYMENT_TRANSACTION);
        db.execSQL(CREATE_TABLE_PAYMENT_TYPE);
        db.execSQL(CREATE_TABLE_RECHARGE_DATA);
        db.execSQL(CREATE_TABLE_CATEGORY_LIST);
        db.execSQL(CREATE_TABLE_ITEM_STOCK);
        db.execSQL(CREATE_TABLE_ITEM_LIST);
        db.execSQL(CREATE_TABLE_SALES_ITEM_LIST);
        db.execSQL(CREATE_TABLE_ITEM_SALE_TRANSACTION);
        db.execSQL(CREATE_TABLE_FEES_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_FEES_LIST);
        db.execSQL(CREATE_TABLE_FEES_STRUCTURE);
        db.execSQL(CREATE_TABLE_ITEM_TYPE);
        db.execSQL(CREATE_TABLE_LIBRARY_BOOK_TRANSACTION);
        db.execSQL(CREATE_TABLE_REFUND_TRANSACTION);
        db.execSQL(CREATE_TABLE_ONLINE_TO_RECHARGE);
        db.execSQL(CREATE_TABLE_STUDENT_LIST);
        db.execSQL(CREATE_TABLE_REFUND_ERROR);
        db.execSQL(CREATE_TABLE_PARENT_DETAILS);
        db.execSQL(CREATE_STUDENT_PARENT);
        db.execSQL(CREATE_TABLE_REASON_TYPES);
        db.execSQL(CREATE_CREATE_PARENT_LEAVE);
        db.execSQL(CREATE_STUDENT_CLASS_DIV);
        db.execSQL(CREATE_TABLE_TEACHER_DETAILS);
        db.execSQL(CREATE_TABLE_TEACHER_ATTENDENCE_DATA);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        System.out.println("CHILLAR: EXISTING DB");

        if (oldVersion < 89) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_LIST);
            db.execSQL(CREATE_TABLE_STUDENT_LIST);
            db.execSQL(CREATE_TABLE_REFUND_ERROR);
            db.execSQL(CREATE_TABLE_PARENT_DETAILS);
            db.execSQL(CREATE_STUDENT_PARENT);
            db.execSQL(CREATE_TABLE_REASON_TYPES);
            db.execSQL(CREATE_CREATE_PARENT_LEAVE);
            db.execSQL(CREATE_STUDENT_CLASS_DIV);
            db.execSQL(CREATE_TABLE_TEACHER_DETAILS);
            db.execSQL(CREATE_TABLE_TEACHER_ATTENDENCE_DATA);
        }


        if (oldVersion < 98) {


            db.execSQL("ALTER TABLE " + TABLE_DEVICE_INFO + " ADD COLUMN " + KEY_LAST_BLOCKCARDS_UPDATED + " VARCHAR;");


            System.out.println("ChillarTest:");

        }
        if (oldVersion < 98) {

            System.out.println("ChillarTest: 96 ");

            db.execSQL("ALTER TABLE " + TABLE_BLOCKED_CARDS_INFO + " RENAME TO " + TABLE_BLOCKED_CARDS_INFO_TEMP);
            db.execSQL(CREATE_BLOCKED_CARDS_INFO_NEW);

            db.execSQL("INSERT OR REPLACE INTO " + TABLE_BLOCKED_CARDS_INFO + " SELECT * FROM " + TABLE_BLOCKED_CARDS_INFO_TEMP);
            db.execSQL("DROP TABLE " + TABLE_BLOCKED_CARDS_INFO_TEMP);


        }


    }

    // Adding new User................................................................................................................
    int addUser(ArrayList<User> user1) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();


        for (int i = 0; i < user1.size(); ++i) {

            User user = user1.get(i);

            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, user.getUser_id());
            values.put(KEY_USER_NAME, user.getUser_name()); // User Name
            values.put(KEY_PASSWORD, user.getPassword()); // User Password
            values.put(KEY_SERVER_TIMESTAMP, user.getServer_timestamp()); //server timestamp

            // Inserting Row
            db.insert(TABLE_USER, null, values);

        }

        db.setTransactionSuccessful();
        db.endTransaction();


        return 0;

    }


    // Getting All Conta
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query


        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUser_id(Integer.parseInt(cursor.getString(0)));
                user.setUser_name(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }


        cursor.close();          // Dont forget to close your cursor

        return userList;
    }




    public void deleteAllUser() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        db.execSQL("delete from " + TABLE_USER);
        db.setTransactionSuccessful();
        db.endTransaction();

    }


    // USER PERMISSION DATA ...........................................................................................................


    int addUserpermission(ArrayList<User_Permission_Data> user_permission_data) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < user_permission_data.size(); ++i) {

            User_Permission_Data user_permission_data1 = user_permission_data.get(i);

            ContentValues valuespermission = new ContentValues();
            valuespermission.put(KEY_PERMISSION_ID, user_permission_data1.getPermission_id()); // User PAssword
            valuespermission.put(KEY_TRANSACTION_TYPE_ID, user_permission_data1.getTransaction_type_id()); // User Name5
            valuespermission.put(KEY_USER_ID, user_permission_data1.getuser_id()); // User PAssword
            valuespermission.put(KEY_PERMISSION, user_permission_data1.getPermission());
            valuespermission.put(KEY_SERVER_TIMESTAMP, user_permission_data1.getServer_timestamp());

            // Inserting Row
            db.insert(TABLE_USER_PERMISSION_DATAA, null, valuespermission);

        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;

    }


    public List<User_Permission_Data> getAllUserspermission() {
        List<User_Permission_Data> permissionList = new ArrayList<User_Permission_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_PERMISSION_DATAA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User_Permission_Data userpermission = new User_Permission_Data();
                userpermission.setPermission_id(Integer.parseInt(cursor.getString(0)));
                userpermission.setTransaction_type_id(Integer.parseInt(cursor.getString(1)));
                userpermission.setuser_id(Integer.parseInt(cursor.getString(2)));
                userpermission.setPermission(Integer.parseInt(cursor.getString(3)));
                userpermission.setServer_timestamp(cursor.getString(4));

                // Adding contact to list
                permissionList.add(userpermission);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor

        // return contact list
        return permissionList;
    }


    public void deleteAllUserpermission() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_USER_PERMISSION_DATAA);
        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close();
    }




    /*
     * getting all todos under single tag
     * */
    public List<TransactionType> getAllToDosByTag(int user_id) {
        List<TransactionType> todos = new ArrayList<TransactionType>();

        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION_TYPE + " INNER JOIN "
                + TABLE_USER_PERMISSION_DATAA + " ON " + TABLE_TRANSACTION_TYPE + "." + KEY_TRANSACTION_TYPE_ID + " = " + TABLE_USER_PERMISSION_DATAA + "." + KEY_TRANSACTION_TYPE_ID + " WHERE " + TABLE_USER_PERMISSION_DATAA + "." + KEY_USER_ID + " = " + user_id;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransactionType opdatadev = new TransactionType();
                System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE_NAME)));
                opdatadev.setTransaction_type_name(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE_NAME)));
                opdatadev.setTrans_type_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE_ID))));
                // Adding contact to list
                todos.add(opdatadev);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return todos;


    }



    public List<CategoryList> getAllCategoryItems(int item_id) {
        List<CategoryList> todos = new ArrayList<CategoryList>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_LIST + " WHERE " + KEY_ITEM_TYPE_ID + " = " + item_id;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CategoryList opdatadev = new CategoryList();
                opdatadev.setcategory_name(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME)));
                opdatadev.setcategory_id(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                // Adding contact to list
                todos.add(opdatadev);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor


        return todos;


    }

    public List<ItemList> getAllListItemsByCatId(int cat_id) {
        List<ItemList> todos = new ArrayList<ItemList>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEM_LIST + " WHERE " + KEY_CATEGORY_ID + " = " + cat_id;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemList opdatadev = new ItemList();
                opdatadev.setItem_name(cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)));
                opdatadev.setItem_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ITEM_ID))));
                opdatadev.setItem_code(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE)));
                opdatadev.setprice(Float.valueOf(cursor.getString(cursor.getColumnIndex(KEY_PRICE))));
                opdatadev.setstock_status(cursor.getString(cursor.getColumnIndex(KEY_STOCK_STATUS)));
                // Adding contact to list
                todos.add(opdatadev);
            } while (cursor.moveToNext());
        }


        cursor.close();          // Dont forget to close your cursor
        return todos;


    }

    public List<ItemList> getAllListItems(int typeId) {
        List<ItemList> todos = new ArrayList<ItemList>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEM_LIST + " WHERE " + KEY_ITEM_TYPE_ID + " = " + typeId;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemList opdatadev = new ItemList();
                opdatadev.setItem_name(cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)));
                opdatadev.setItem_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ITEM_ID))));
                opdatadev.setItem_code(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE)));
                opdatadev.setprice(Float.valueOf(cursor.getString(cursor.getColumnIndex(KEY_PRICE))));
                opdatadev.setstock_status(cursor.getString(cursor.getColumnIndex(KEY_STOCK_STATUS)));
                // Adding contact to list
                todos.add(opdatadev);
            } while (cursor.moveToNext());
        }


        cursor.close();          // Dont forget to close your cursor

        return todos;


    }


//Attendance Type..................................................................................................................

    public int addUserAttendance(ArrayList<Attendance_Type> attndance) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        for (int i = 0; i < attndance.size(); ++i) {

            Attendance_Type attendance_type = attndance.get(i);

            ContentValues valuesattendance = new ContentValues();
            valuesattendance.put(KEY_ATTENDENCE_TYPE_NAME, attendance_type.getAttendance_type_name()); // User Name

            valuesattendance.put(KEY_SERVER_TIMESTAMP, attendance_type.getServer_timestamp());

            // Inserting Row
            db.insert(TABLE_ATTENDENCE_TYPE, null, valuesattendance);

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close(); // Closing database connection

        return 0;

    }


    public void addOnlineToRech(Online_Recharge online) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues onlineRech = new ContentValues();
        onlineRech.put(KEY_TRANSACTION_ID, online.getTransID()); // User Name
        onlineRech.put(KEY_ONLINE_TRANSACTION_ID, online.getOnlineTransID());
        onlineRech.put(KEY_SERVER_TIMESTAMP, online.getServerTimestamp());

        // Inserting Row
        db.insert(TABLE_ONLINE_TO_RECHARGE, null, onlineRech);
        ////db.close(); // Closing database connection
    }

    public List<Online_Recharge> getAllonline() {
        List<Online_Recharge> onlineRech = new ArrayList<Online_Recharge>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ONLINE_TO_RECHARGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Online_Recharge onlin1 = new Online_Recharge();
                onlin1.setId(Integer.parseInt(cursor.getString(0)));
                onlin1.setTransID(cursor.getString(1));
                onlin1.setOnlineTransID((cursor.getString(2)));
                onlin1.setServerTimestamp(cursor.getString(3));

                // Adding contact to list
                onlineRech.add(onlin1);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        // return contact list
        return onlineRech;
    }

    // Getting All Contacts
    public List<Attendance_Type> getAllAttendance() {
        List<Attendance_Type> attndncList = new ArrayList<Attendance_Type>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDENCE_TYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Attendance_Type attndnc = new Attendance_Type();
                attndnc.setId(Integer.parseInt((cursor.getString(0))));
                attndnc.setAttendance_type_name((cursor.getString(1)));
                attndnc.setServer_timestamp(cursor.getString(2));

                // Adding contact to list
                attndncList.add(attndnc);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        // return contact list
        return attndncList;
    }



    public void deleteAllAttendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_ATTENDENCE_TYPE);
        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close();
    }


    //Attendance data.............................................................................................................


    void addUserAttendancedata(Attendance_Data attndancedata) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesattndancedata = new ContentValues();
        valuesattndancedata.put(KEY_TRANSACTION_ID, attndancedata.getTransaction_id());
        valuesattndancedata.put(KEY_IN_OUT, attndancedata.getIn_out());
        valuesattndancedata.put(KEY_ATTENDENCE_TYPE_ID, attndancedata.getAttendance_type_id());
        valuesattndancedata.put(KEY_SERVER_TIMESTAMP, attndancedata.getServer_timestamp());

        // Inserting Row
        db.insert(TABLE_ATTENDENCE_DATA, null, valuesattndancedata);
        ////db.close(); // Closing database connection
    }


    void addTeacherAttendancedata(Attendance_Data attndancedata) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesattndancedata = new ContentValues();
        valuesattndancedata.put(KEY_TRANSACTION_ID, attndancedata.getTransaction_id());
        valuesattndancedata.put(KEY_IN_OUT, attndancedata.getIn_out());
        valuesattndancedata.put(KEY_ATTENDENCE_TYPE_ID, attndancedata.getAttendance_type_id());
        valuesattndancedata.put(KEY_CARD_SERIAL, attndancedata.getcard_serial());
        valuesattndancedata.put(KEY_SERVER_TIMESTAMP, attndancedata.getServer_timestamp());

        // Inserting Row
        db.insert(TABLE_TEACHER_ATTENDENCE_DATA, null, valuesattndancedata);
        ////db.close(); // Closing database connection
    }

    // Getting single contact
//    Attendance_Data getAttendancedata(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_ATTENDENCE_DATA, new String[] {KEY_ATTENDENCE_DATA_ID,
//                        KEY_TRANSACTION_ID, KEY_IN_OUT,KEY_ATTENDENCE_TYPE_ID,KEY_SERVER_TIMESTAMP }, KEY_ATTENDENCE_DATA_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Attendance_Data contact = new Attendance_Data(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),cursor.getString(5));
//        // return contact
//        return contact;
//    }

    // Getting All Contacts
    public List<Attendance_Data> getAllAttendancedata() {
        List<Attendance_Data> attndncdataList = new ArrayList<Attendance_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDENCE_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Attendance_Data attndncdata = new Attendance_Data();
                attndncdata.setAttendance_data_id(Integer.parseInt(cursor.getString(0)));
                attndncdata.setTransaction_id(cursor.getString(1));
                attndncdata.setIn_out(Integer.parseInt((cursor.getString(2))));
                attndncdata.setAttendance_type_id(Integer.parseInt((cursor.getString(3))));
                attndncdata.setServer_timestamp(cursor.getString(4));

                // Adding contact to list
                attndncdataList.add(attndncdata);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return attndncdataList;
    }

    public List<Attendance_Data> getAllAttendancedataDESC() {
        List<Attendance_Data> attndncdataList = new ArrayList<Attendance_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDENCE_DATA + " ORDER BY " + KEY_ATTENDENCE_DATA_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Attendance_Data attndncdata = new Attendance_Data();
                attndncdata.setAttendance_data_id(Integer.parseInt(cursor.getString(0)));
                attndncdata.setTransaction_id(cursor.getString(1));
                attndncdata.setIn_out(Integer.parseInt((cursor.getString(2))));
                attndncdata.setAttendance_type_id(Integer.parseInt((cursor.getString(3))));
                attndncdata.setServer_timestamp(cursor.getString(4));

                // Adding contact to list
                attndncdataList.add(attndncdata);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////////db.close();
        // return contact list
        return attndncdataList;
    }

    // Updating single user
    public int updateAttendancedata(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues attndnc = new ContentValues();

        attndnc.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_ATTENDENCE_DATA, attndnc, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        System.out.println("ELDDD: update count  " + count);
        ////db.close();
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

    public int updateTchrAttnd(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues attndnc = new ContentValues();

        attndnc.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_TEACHER_ATTENDENCE_DATA, attndnc, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        System.out.println("ELDDD: update count  " + count);
        ////db.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        // updating row
        return count;
    }

//    // Deleting single contact
//    public void deleteAttendancedata(Attendance_Data attndata) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_ATTENDENCE_DATA, KEY_ATTENDENCE_DATA_ID + " = ?",
//                new String[] { String.valueOf(attndata.getAttendance_data_id()) });
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getUserAttendancedata() {
//        String countQuery = "SELECT  * FROM " + TABLE_ATTENDENCE_DATA;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAllAttendancedata(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_ATTENDENCE_DATA);
//        //////db.close();
//    }
//
//
//
//
//
////operational device data............................................................................................................
//
//
//
//    void addUseroperationaldevice(Operational_Device_Data opdevice) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues valuesattndancedata = new ContentValues();
//       // valuesattndancedata.put(KEY_ID, opdevice.getId());
//        valuesattndancedata.put(KEY_LAST_TRANS_ID, opdevice.getLast_trans_id());
//        valuesattndancedata.put(KEY_MACHINE_ID, opdevice.getMachine_id());
//        valuesattndancedata.put(KEY_LAST_MACHINE_TRANS_TIME, opdevice.getLast_machine_trans_time());
//        valuesattndancedata.put(KEY_LAST_SERVER_TRANS_TIME, opdevice.getLast_server_trans_time());
//        valuesattndancedata.put(KEY_SERVER_TIMESTAMP,opdevice.getServer_timestamp());
//
//        // Inserting Row
//        db.insert(TABLE_OPERATIONAL_DEVICE_DATA, null, valuesattndancedata);
//        //////db.close(); // Closing database connection
//    }
//
//    // Getting single contact
//    Operational_Device_Data getoperationaldevice(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_OPERATIONAL_DEVICE_DATA, new String[] {KEY_ID,
//                        KEY_LAST_TRANS_ID, KEY_MACHINE_ID,KEY_LAST_MACHINE_TRANS_TIME,KEY_LAST_SERVER_TRANS_TIME,KEY_SERVER_TIMESTAMP }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Operational_Device_Data contact = new Operational_Device_Data(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4),cursor.getString(5));
//        // return contact
//        return contact;
//    }
//
//    // Getting All Contacts
//    public List<Operational_Device_Data> getAlloperationaldevice() {
//        List<Operational_Device_Data> devicelist = new ArrayList<Operational_Device_Data>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_OPERATIONAL_DEVICE_DATA;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Operational_Device_Data opdatadev = new Operational_Device_Data();
//                opdatadev.setId(Integer.parseInt((cursor.getString(0))));
//                opdatadev.setLast_trans_id((cursor.getString(1)));
//                opdatadev.setMachine_id((cursor.getString(2)));
//                opdatadev.setLast_machine_trans_time((cursor.getString(3)));
//                opdatadev.setLast_server_trans_time(cursor.getString(4));
//                opdatadev.setServer_timestamp(cursor.getString(5));
//
//                // Adding contact to list
//                devicelist.add(opdatadev);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();          // Dont forget to close your cursor
//        //////db.close();
//        // return contact list
//        return devicelist;
//    }
//
//    // Updating single user
//    public int updateoperationaldevice(Operational_Device_Data opdatadevice) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues attndnc = new ContentValues();
//        attndnc.put(KEY_ID, opdatadevice.getId());
//        attndnc.put(KEY_LAST_TRANS_ID, opdatadevice.getLast_trans_id());
//        attndnc.put(KEY_MACHINE_ID, opdatadevice.getMachine_id());
//        attndnc.put(KEY_LAST_MACHINE_TRANS_TIME, opdatadevice.getLast_machine_trans_time());
//        attndnc.put(KEY_LAST_SERVER_TRANS_TIME, opdatadevice.getLast_server_trans_time());
//        attndnc.put(KEY_TIME_STAMP, opdatadevice.getServer_timestamp());
//
//        // updating row
//        return db.update(TABLE_ATTENDENCE_DATA, attndnc, KEY_ATTENDENCE_DATA_ID+ " = ?",
//                new String[] { String.valueOf(opdatadevice.getLast_trans_id()) });
//    }
//
//    // Deleting single contact
//    public void deleteoperationaldevice(Operational_Device_Data attndata) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_OPERATIONAL_DEVICE_DATA, KEY_ID + " = ?",
//                new String[] { String.valueOf(attndata.getLast_trans_id()) });
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getoperationaldevice() {
//        String countQuery = "SELECT  * FROM " + TABLE_OPERATIONAL_DEVICE_DATA;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteoperationaldevice(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_OPERATIONAL_DEVICE_DATA);
//        //////db.close();
//    }


    //TRANSACTION TYPE................................................................................................................


    int addTransaction(ArrayList<TransactionType> transtype) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        for (int i = 0; i < transtype.size(); ++i) {

            TransactionType transactionType = transtype.get(i);

            ContentValues valuesattndancedata = new ContentValues();
            valuesattndancedata.put(KEY_TRANSACTION_TYPE_ID, transactionType.getTrans_type_id());
            valuesattndancedata.put(KEY_TRANSACTION_TYPE_NAME, transactionType.getTransaction_type_name());

            valuesattndancedata.put(KEY_SERVER_TIMESTAMP, transactionType.getServer_timestamp());

            // Inserting Row
            db.insert(TABLE_TRANSACTION_TYPE, null, valuesattndancedata);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;

        ////db.close(); // Closing database connection
    }

//    // Getting single contact
//    TransactionType gettrantype(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_TRANSACTION_TYPE, new String[] {KEY_ID,
//                        KEY_TRANSACTION_TYPE_ID, KEY_TRANSACTION_TYPE_NAME,KEY_SERVER_TIMESTAMP }, KEY_TRANSACTION_TYPE_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        TransactionType contact = new TransactionType(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return contact;
//    }

    // Getting All Contacts
    public List<TransactionType> getAlltrancttype() {
        List<TransactionType> translist = new ArrayList<TransactionType>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION_TYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransactionType opdatadev = new TransactionType();
                opdatadev.setTrans_type_id(Integer.parseInt((cursor.getString(0))));
                opdatadev.setTransaction_type_name((cursor.getString(1)));

                opdatadev.setServer_timestamp(cursor.getString(2));

                // Adding contact to list
                translist.add(opdatadev);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        //////db.close();
        // return contact list
        return translist;
    }

//    // Updating single user
//    public int updatetranstype(TransactionType trnsType) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues trns = new ContentValues();
//        trns.put(KEY_TRANSACTION_TYPE_ID, trnsType.getTrans_type_id());
//        trns.put(KEY_TRANSACTION_TYPE_NAME, trnsType.getTransaction_type_name());
//
//        trns.put(KEY_SERVER_TIMESTAMP, trnsType.getServer_timestamp());
//
//        System.out.println("CODMOB : return Stmnt"+String.valueOf(trnsType.getTrans_type_id()));
//
//        // updating row
//        return db.update(TABLE_TRANSACTION_TYPE, trns, KEY_TRANSACTION_TYPE_ID+ " = ?",
//                new String[] { String.valueOf(trnsType.getTrans_type_id()) });
//
//
//
//    }

//    // Deleting single contact
//    public void deleteTranstype(TransactionType trnTyp) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_TRANSACTION_TYPE, KEY_TRANSACTION_TYPE_ID + " = ?",
//                new String[] { String.valueOf(trnTyp.getTrans_type_id()) });
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getTranstype() {
//        String countQuery = "SELECT  * FROM " + TABLE_TRANSACTION_TYPE;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }


    public void deletetranstype() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_TRANSACTION_TYPE);
        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close();
    }

    ///Device info................................................................................................................

    int addDevInfo(Device_Info devinfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues valuesattndancedata = new ContentValues();
        // valuesattndancedata.put(KEY_ID, devinfo.getId());
        valuesattndancedata.put(KEY_MACHINE_ID, devinfo.getMachine_id());
        valuesattndancedata.put(KEY_SERIAL_NO, devinfo.getSerial_no());
        valuesattndancedata.put(KEY_SCHOOL_ID, devinfo.getSchool_id());
        valuesattndancedata.put(KEY_SCHOOL_NAME, devinfo.getSchoolname());
        valuesattndancedata.put(KEY_SCHOOL_PLACE, devinfo.getSchoolplace());
        valuesattndancedata.put(KEY_DEVICE_LAST_TRANS_ID, devinfo.getLasttransid());
        valuesattndancedata.put(KEY_MAIN_SERVER_URL, devinfo.getMain_server_url());
        valuesattndancedata.put(KEY_MAIN_UPLOAD_PATH, devinfo.getMain_upload_path());
        valuesattndancedata.put(KEY_LAST_BLOCKCARDS_UPDATED, devinfo.getLastblockupdated());


        // Inserting Row
        db.insert(TABLE_DEVICE_INFO, null, valuesattndancedata);

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;
        ////db.close(); // Closing database connection
    }

//    // Getting single contact
//    Device_Info getdevinfo(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_DEVICE_INFO, new String[] {KEY_ID,
//                        KEY_MACHINE_ID, KEY_SERIAL_NO,KEY_SCHOOL_ID,KEY_SCHOOL_NAME,KEY_SCHOOL_PLACE,KEY_DEVICE_LAST_TRANS_ID,
//                KEY_MAIN_SERVER_URL,KEY_MAIN_UPLOAD_PATH }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Device_Info contact = new Device_Info(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2),cursor.getString(3),
//        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
//        // return contact
//        return contact;
//    }

    // Getting All Contacts
    public List<Device_Info> getAlldevInfo() {
        List<Device_Info> devicelist = new ArrayList<Device_Info>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DEVICE_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Device_Info opdatadev = new Device_Info();
                opdatadev.setId(Integer.parseInt((cursor.getString(0))));
                opdatadev.setMachine_id((cursor.getString(1)));
                opdatadev.setSerial_no((cursor.getString(2)));
                opdatadev.setSchool_id((cursor.getString(3)));
                opdatadev.setSchoolname((cursor.getString(4)));
                opdatadev.setSchoolplace((cursor.getString(5)));
                opdatadev.setLasttransid((cursor.getString(6)));
                opdatadev.setMain_server_url((cursor.getString(7)));
                opdatadev.setMain_upload_path(cursor.getString(8));

                // Adding contact to list
                devicelist.add(opdatadev);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        //////db.close();

        // return contact list
        return devicelist;
    }


    public List<Teacher_Details> getAllteacher() {
        List<Teacher_Details> teacherlist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Teacher_Details opdatadev = new Teacher_Details();
                opdatadev.setTeacher_userID((cursor.getString(1)));
                opdatadev.setTeacherName((cursor.getString(2)));
                opdatadev.setSchoolID((cursor.getString(3)));
                opdatadev.setCardSerial((cursor.getString(4)));


                // Adding contact to list
                teacherlist.add(opdatadev);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        //////db.close();

        // return contact list
        return teacherlist;
    }

    //    // Updating single user
//    public int updatedevInfo(Device_Info devinfo) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues attndnc = new ContentValues();
//        attndnc.put(KEY_ID, devinfo.getId());
//        attndnc.put(KEY_MACHINE_ID, devinfo.getMachine_id());
//        attndnc.put(KEY_SERIAL_NO, devinfo.getSerial_no());
//        attndnc.put(KEY_SCHOOL_NAME, devinfo.getSerial_no());
//        attndnc.put(KEY_SCHOOL_PLACE, devinfo.getSerial_no());
//        attndnc.put(KEY_DEVICE_LAST_TRANS_ID, devinfo.getSerial_no());
//        attndnc.put(KEY_SCHOOL_ID, devinfo.getSchool_id());
//        attndnc.put(KEY_MAIN_SERVER_URL, devinfo.getMain_server_url());
//        attndnc.put(KEY_MAIN_UPLOAD_PATH, devinfo.getMain_upload_path());
//
//        // updating row
//        return db.update(TABLE_DEVICE_INFO, attndnc, KEY_ID+ " = ?",
//                new String[] { String.valueOf(devinfo.getId()) });
//    }
//
//    // Deleting single contact
//    public void deletedevInfo(Device_Info trnTyp) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_DEVICE_INFO, KEY_ID + " = ?",
//                new String[] { String.valueOf(trnTyp.getId()) });
//
//        System.out.println("CODMOB: ID delete "+String.valueOf(trnTyp.getId()));
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getDevinfo() {
//        String countQuery = "SELECT  * FROM " + TABLE_DEVICE_INFO;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
    public void deleteDeviceinfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_DEVICE_INFO);
        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close();
    }

    //Blocked Cards Info


    ///Device info................................................................................................................

    int addBlockCardInfo(ArrayList<Blocked_Cards_Info> blkinfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < blkinfo.size(); ++i) {

            Blocked_Cards_Info blocked_cards_info = blkinfo.get(i);

            ContentValues valuesattndancedata = new ContentValues();
            valuesattndancedata.put(KEY_BLOCKED_CARDS_ID, blocked_cards_info.getBlocked_cards_id());
            valuesattndancedata.put(KEY_CARD_SERIAL, blocked_cards_info.getCard_serial());
            valuesattndancedata.put(KEY_SERVER_TIMESTAMP, blocked_cards_info.getServer_timestamp());


            // Inserting Row
            db.insertWithOnConflict(TABLE_BLOCKED_CARDS_INFO, null, valuesattndancedata, SQLiteDatabase.CONFLICT_REPLACE);

        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;
//        ////db.close(); // Closing database connection
    }

//    // Getting single contact
//    Blocked_Cards_Info getBlockCardInfo(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_BLOCKED_CARDS_INFO, new String[] {KEY_BLOCKED_CARDS_ID,
//                        KEY_CARD_SERIAL,KEY_SERVER_TIMESTAMP }, KEY_BLOCKED_CARDS_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Blocked_Cards_Info contact = new Blocked_Cards_Info(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        // return contact
//        cursor.close();          // Dont forget to close your cursor
//        //////db.close();
//        return contact;
//    }

    // Getting All Contacts
    public List<Blocked_Cards_Info> getAllBlockCardInfo() {
        System.out.println("CODMOB: main block");
        List<Blocked_Cards_Info> devicelist = new ArrayList<Blocked_Cards_Info>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BLOCKED_CARDS_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Blocked_Cards_Info opdatadev = new Blocked_Cards_Info();
                opdatadev.setBlocked_cards_id(Integer.parseInt((cursor.getString(0))));
                opdatadev.setCard_serial((cursor.getString(1)));
                opdatadev.setServer_timestamp((cursor.getString(2)));


                // Adding contact to list
                devicelist.add(opdatadev);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        //////db.close();
        // return contact list
        return devicelist;
    }

    //    // Updating single user
//    public int updatBlockInfo(Blocked_Cards_Info devinfo) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues attndnc = new ContentValues();
//        attndnc.put( KEY_BLOCKED_CARDS_ID, devinfo.getBlocked_cards_id());
//        attndnc.put(KEY_CARD_SERIAL, devinfo.getCard_serial());
//        attndnc.put(KEY_SERVER_TIMESTAMP, devinfo.getServer_timestamp());
//
//        // updating row
//        return db.update(TABLE_BLOCKED_CARDS_INFO, attndnc, KEY_BLOCKED_CARDS_ID+ " = ?",
//                new String[] { String.valueOf(devinfo.getBlocked_cards_id()) });
//    }
//
//    // Deleting single contact
//    public void deleteBlockinfo(Blocked_Cards_Info trnTyp) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_BLOCKED_CARDS_INFO, KEY_BLOCKED_CARDS_ID + " = ?",
//                new String[] { String.valueOf(trnTyp.getBlocked_cards_id())});
//
//        System.out.println("CODMOB: ID delete "+String.valueOf(trnTyp.getBlocked_cards_id()));
//        //////db.close();
//    }
//
////
//    // Getting contacts Count
//    public int getBlockedInfo() {
//        String countQuery = "SELECT  * FROM " + TABLE_BLOCKED_CARDS_INFO;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }

    public void deleteBlockedInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_BLOCKED_CARDS_INFO);
        ////db.close();
    }


    ///ITEM STOCK.....................................................................................................................


    int addItem(ArrayList<ItemStock> itemStock) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        for (int i = 0; i < itemStock.size(); ++i) {

            ItemStock itemStock1 = itemStock.get(i);

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_ID, itemStock1.getItem_id()); // User Name
            values.put(KEY_ITEM_STOCK, itemStock1.getItem_stock());
            values.put(KEY_REORDER_WARNING, itemStock1.getReorder_warning());// User PAssword
            values.put(KEY_SERVER_TIMESTAMP, itemStock1.getServer_timestamp()); //server timestamp

            // Inserting Row
            db.insert(TABLE_ITEM_STOCK, null, values);

        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;

        ////db.close(); // Closing database connection
    }

//
//    ItemStock getitemStock(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_ITEM_STOCK, new String[] { KEY_STOCK_ID,
//                        KEY_ITEM_ID, KEY_ITEM_STOCK,KEY_REORDER_WARNING,KEY_SERVER_TIMESTAMP }, KEY_STOCK_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        ItemStock itemStock = new ItemStock(Integer.parseInt(cursor.getString(0)),
//                Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)), cursor.getString(3),cursor.getString(4));
//        // return contact
//        cursor.close();          // Dont forget to close your cursor
//        //////db.close();
//        return itemStock;
//    }

    // Getting All users
    public List<ItemStock> getAllItems() {
        List<ItemStock> itemStocks = new ArrayList<ItemStock>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_STOCK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemStock itemStock = new ItemStock();
                itemStock.setStock_id(Integer.parseInt(cursor.getString(0)));
                itemStock.setItem_id(Integer.parseInt(cursor.getString(1)));
                itemStock.setItem_stock(Integer.parseInt(cursor.getString(2)));
                itemStock.setReorder_warning(cursor.getString(3));
                itemStock.setServer_timestamp(cursor.getString(4));
                // Adding contact to list
                itemStocks.add(itemStock);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();
        // return contact list
        return itemStocks;
    }
//
//    // Updating single user
//    public int updateItemStock(ItemStock itemStock) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_ITEM_ID, itemStock.getItem_id()); // User Name
//        values.put(KEY_ITEM_STOCK, itemStock.getItem_stock());
//        values.put(KEY_REORDER_WARNING, itemStock.getReorder_warning());// User PAssword
//        values.put(KEY_SERVER_TIMESTAMP,itemStock.getServer_timestamp()); //server timestamp
//
//        // updating row
//        return db.update(TABLE_ITEM_STOCK, values, KEY_STOCK_ID + " = ?",
//                new String[] { String.valueOf(itemStock.getStock_id()) });
//    }
//
//    // Deleting single contact
//    public void deleteUser(ItemStock itemStock) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_ITEM_STOCK, KEY_STOCK_ID + " = ?",
//                new String[] { String.valueOf(itemStock.getStock_id()) });
//        //////db.close();
//    }


    // Getting contacts Count
    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEM_STOCK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_ITEM_STOCK);
        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close();
    }

    public void deleteAllteacher() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_TEACHER_DETAILS);
        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close();
    }


    //Success Transaction........................................................................................................


    public long addSuccesstransaction(Success_Transaction successtrans) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRANSACTION_ID, successtrans.gettrans_id());
        System.out.println("ijaz  :  " + "OKtransid" + successtrans.gettrans_id());
        values.put(KEY_USER_ID, successtrans.getuser_id());
        System.out.println("ijaz  :  " + "OKuserid" + successtrans.getuser_id());
        values.put(KEY_TRANSACTION_TYPE_ID, successtrans.gettranstypeid());
        System.out.println("ijaz  :  " + "OKtransidtype" + successtrans.gettranstypeid());
        values.put(KEY_PREV_BALANCE, successtrans.getprevious_balnce());
        System.out.println("ijaz  :  " + "OKprevbalnce" + successtrans.getprevious_balnce());
        values.put(KEY_CURRENT_BALANCE, successtrans.getcurrent_balance());
        System.out.println("ijaz  :  " + "OKcurrentbalnce" + successtrans.getcurrent_balance());
        values.put(KEY_CARD_SERIAL, successtrans.getcard_serial());
        System.out.println("ijaz  :  " + "OKcardserial" + successtrans.getcard_serial());
        values.put(KEY_TIME_STAMP, successtrans.gettime_stamp());
        System.out.println("ijaz  :  " + "OKtimestamp" + successtrans.gettime_stamp());
        values.put(KEY_SERVER_TIMESTAMP, successtrans.getserver_timestamp());
        System.out.println("ijaz  :  " + "OKservertimestamb" + successtrans.getserver_timestamp());

        // Inserting Row
        long value =db.insert(TABLE_SUCCESS_TRANSACTION, null, values);
        return value;
        ////db.close(); // Closing database connection
    }


//    Success_Transaction getsuccess(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_SUCCESS_TRANSACTION, new String[] { KEY_TRANSACTION_ID,
//                        KEY_USER_ID, KEY_TRANSACTION_TYPE_ID,KEY_PREV_BALANCE,KEY_CURRENT_BALANCE,KEY_CARD_SERIAL,KEY_TIME_STAMP,KEY_SERVER_TIMESTAMP
//                }, KEY_TRANSACTION_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Success_Transaction successtrans = new Success_Transaction(
//                cursor.getString(0),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
//                Float.parseFloat(cursor.getString(3)),
//                Float.parseFloat(cursor.getString(4)),cursor.getString(5), cursor.getString(6),cursor.getString(7));
//        // return contact
//        return successtrans;
//    }

    // Getting All users
    public List<Success_Transaction> getAllSuccess() {
        List<Success_Transaction> successtrans = new ArrayList<Success_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Success_Transaction successss = new Success_Transaction();
                successss.settrans_id(cursor.getString(0));
                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
                successss.setcard_serial(cursor.getString(5));
                successss.settime_stamp(cursor.getString(6));
                successss.setserver_timestamp(cursor.getString(7));
                // Adding contact to list
                successtrans.add(successss);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        //////db.close();

        // return contact list
        return successtrans;
    }


    public List<Success_Transaction> getAllSuccessDESC() {
        List<Success_Transaction> successtrans = new ArrayList<Success_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION + " ORDER BY " + KEY_TRANSACTION_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Success_Transaction successss = new Success_Transaction();
                successss.settrans_id(cursor.getString(0));
                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
                successss.setcard_serial(cursor.getString(5));
                successss.settime_stamp(cursor.getString(6));
                successss.setserver_timestamp(cursor.getString(7));
                // Adding contact to list
                successtrans.add(successss);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        //////db.close();

        // return contact list
        return successtrans;
    }

    public String getcurrentdate() {
        String currentDateTimeString = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        currentDateTimeString = df.format(today);
        return currentDateTimeString;
    }

    // Updating single user
    public int updatesuccess(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_SUCCESS_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        // updating row
        ////db.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

    public int updatesuccessRefund1(String id/*,Float prev, Float Curr*/) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_TIMESTAMP, "-1");

//        values.put(KEY_PREV_BALANCE,prev);
//        values.put(KEY_CURRENT_BALANCE,Curr);

        int count = db.update(TABLE_SUCCESS_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        ////db.close();
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }


    public int updatesuccessRefund2(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_TIMESTAMP, "-2");


        int count = db.update(TABLE_SUCCESS_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        ////db.close();
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

    // Deleting single contact
    public void deletesuccess(Success_Transaction successstrns) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUCCESS_TRANSACTION, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(successstrns.gettrans_id())});
        //////db.close();
    }
//
//
//    // Getting contacts Count
//    public int getSucces() {
//        String countQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAllSussecc(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_SUCCESS_TRANSACTION);
//        //////db.close();
//    }
//


    //Payment Transaction............................................................................................................


    void addPaymenttransaction(Payment_Transaction paytrans) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, paytrans.getId());
        values.put(KEY_BILL_NO, paytrans.getbillno());
        values.put(KEY_TRANSACTION_ID, paytrans.gettrans_id());
        values.put(KEY_AMOUNT, paytrans.getamount());
        values.put(KEY_SERVER_TIMESTAMP, paytrans.getserver_timestamp());

        // Inserting Row
        db.insert(TABLE_PAYMENT_TRANSACTION, null, values);
        ////db.close(); // Closing database connection
    }


//    Payment_Transaction getpay(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_PAYMENT_TRANSACTION, new String[] { KEY_ID,
//                        KEY_TRANSACTION_ID, KEY_AMOUNT,KEY_SERVER_TIMESTAMP
//                }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Payment_Transaction paytransactions = new Payment_Transaction(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
//                cursor.getString(2),Float.parseFloat(cursor.getString(3)),cursor.getString(4));
//        // return contact
//        return paytransactions;
//    }

    // Getting All users
    public List<Payment_Transaction> getAllpaytransaction() {
        List<Payment_Transaction> paytrans = new ArrayList<Payment_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment_Transaction paytranss = new Payment_Transaction();

                paytranss.setId(Integer.parseInt(cursor.getString(0)));
                paytranss.setbillno(cursor.getString(1));
                paytranss.settrans_id(cursor.getString(2));

                paytranss.setamount(Float.parseFloat(cursor.getString(3)));


                paytranss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                paytrans.add(paytranss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();
        // return contact list
        return paytrans;
    }

    public List<Payment_Transaction> getAllpaytransactionDESC() {
        List<Payment_Transaction> paytrans = new ArrayList<Payment_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_TRANSACTION + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment_Transaction paytranss = new Payment_Transaction();

                paytranss.setId(Integer.parseInt(cursor.getString(0)));
                paytranss.setbillno(cursor.getString(1));
                paytranss.settrans_id(cursor.getString(2));

                paytranss.setamount(Float.parseFloat(cursor.getString(3)));


                paytranss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                paytrans.add(paytranss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();
        // return contact list
        return paytrans;
    }

    // Updating single user
    public int updatepayment(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_PAYMENT_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        ////db.close();
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

//
//    // Deleting single contact
//    public void deletepay(Payment_Transaction paytransact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_PAYMENT_TRANSACTION, KEY_ID + " = ?",
//                new String[] { String.valueOf(paytransact.gettrans_id()) });
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getpay() {
//        String countQuery = "SELECT  * FROM " + TABLE_PAYMENT_TRANSACTION;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAllPay(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_PAYMENT_TRANSACTION);
//        //////db.close();
//    }


    //Fee Transaction.................................................................................................................


    void addFee(Fee_Transaction feetrans) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BILL_NO, feetrans.getbill_no());
        values.put(KEY_TOTAL_AMOUNT, feetrans.gettot_am());
        values.put(KEY_TRANSACTION_ID, feetrans.gettrans_id());
        values.put(KEY_SERVER_TIMESTAMP, feetrans.getserver_timestamp());

        // Inserting Row
        db.insert(TABLE_FEES_TRANSACTIONS, null, values);
        ////db.close(); // Closing database connection
    }


//    Fee_Transaction getfee(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_FEES_TRANSACTIONS, new String[] { KEY_FEES_TRANS_ID,
//                        KEY_BILL_NO, KEY_TOTAL_AMOUNT,KEY_TRANSACTION_ID,KEY_SERVER_TIMESTAMP
//                }, KEY_FEES_TRANS_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Fee_Transaction paytransactions = new Fee_Transaction(cursor.getString(0),cursor.getString(1),
//                cursor.getString(2),Float.parseFloat(cursor.getString(3)),cursor.getString(4));
//        // return contact
//        return paytransactions;
//    }

    // Getting All users
    public List<Fee_Transaction> getAllfeetrans() {
        List<Fee_Transaction> feetransact = new ArrayList<Fee_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEES_TRANSACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Fee_Transaction paytrans = new Fee_Transaction();
                paytrans.setfee_tran_id(Integer.parseInt(cursor.getString(0)));
                paytrans.setbill_no(cursor.getString(1));
                paytrans.settot_am(Float.parseFloat(cursor.getString(2)));
                paytrans.settrans_id(cursor.getString(3));
                paytrans.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(paytrans);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();

        // return contact list
        return feetransact;
    }


    // Updating single user
    public int updatefeetran(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());


        int count = db.update(TABLE_FEES_TRANSACTIONS, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        ////db.close();
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();

        return count;
    }

//    // Deleting single contact
//    public void deletefee(Fee_Transaction feetrns) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_FEES_TRANSACTIONS, KEY_FEES_TRANS_ID + " = ?",
//                new String[] { String.valueOf(feetrns.gettrans_id()) });
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getfee() {
//        String countQuery = "SELECT  * FROM " + TABLE_FEES_TRANSACTIONS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAllfee(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_FEES_TRANSACTIONS);
//        //////db.close();
//    }


    //item sale transaction...........................................................................................................


    void addsaleItem(Item_Sale itemsale) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_SALES_TRANS_ID, itemsale.getsale_trans_id());
        values.put(KEY_TRANSACTION_ID, itemsale.gettransaction_id());
        values.put(KEY_BILL_NO, itemsale.getbill_no());
        values.put(KEY_TOTAL_AMOUNT, itemsale.gettot_amount());
        values.put(KEY_SERVER_TIMESTAMP, itemsale.getserver_timestamp());

        System.out.println("CHILLAR CHECKOUT amount 1" + itemsale.gettot_amount());
        System.out.println("CHILLAR CHECKOUT Billno 1" + itemsale.getbill_no());
        System.out.println("CHILLAR CHECKOUT ServerTime 1" + itemsale.getserver_timestamp());
        // Inserting Row
        db.insert(TABLE_ITEM_SALE_TRANSACTION, null, values);
        ////db.close(); // Closing database connection
    }


//    Item_Sale getitemsale(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_ITEM_SALE_TRANSACTION, new String[] { KEY_SALES_TRANS_ID,
//                        KEY_TRANSACTION_ID, KEY_BILL_NO,KEY_TOTAL_AMOUNT,KEY_SERVER_TIMESTAMP
//                }, KEY_SALES_TRANS_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Item_Sale paytransactions = new Item_Sale(cursor.getString(0),
//                cursor.getString(1),cursor.getString(2), cursor.getString(3),cursor.getString(4));
//        // return contact
//        return paytransactions;
//    }

    // Getting All users
    public List<Item_Sale> getAllitemsale() {
        List<Item_Sale> feetransact = new ArrayList<Item_Sale>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_SALE_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item_Sale itemsss = new Item_Sale();
                itemsss.setsale_trans_id(cursor.getString(0));
                itemsss.settransaction_id(cursor.getString(1));
                itemsss.setbill_no(cursor.getString(2));
                itemsss.settot_amount(cursor.getString(3));
                itemsss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(itemsss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();
        // return contact list
        return feetransact;
    }

    public List<Item_Sale> getAllitemsaleDESC() {
        List<Item_Sale> feetransact = new ArrayList<Item_Sale>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_SALE_TRANSACTION + " ORDER BY " + KEY_SALES_TRANS_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item_Sale itemsss = new Item_Sale();
                itemsss.setsale_trans_id(cursor.getString(0));
                itemsss.settransaction_id(cursor.getString(1));
                itemsss.setbill_no(cursor.getString(2));
                itemsss.settot_amount(cursor.getString(3));
                itemsss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(itemsss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();
        // return contact list
        return feetransact;
    }

    // Updating single user
    public int updateitemss(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_ITEM_SALE_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        ////db.close();
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();

        return count;
    }

    public int updateitemssRefund1(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, "-1");


        int count = db.update(TABLE_ITEM_SALE_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        ////db.close();
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }


    public int updateitemssRefund2(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, "-2");


        int count = db.update(TABLE_ITEM_SALE_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.setTransactionSuccessful();
        db.endTransaction();
        ////db.close();
        // updating row
        return count;
    }


    public int updatePaymentRefund1(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, "-1");


        // updating row
        int count = db.update(TABLE_PAYMENT_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.setTransactionSuccessful();
        db.endTransaction();

        return count;
    }

    public int updatePaymentRefund2(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, "-2");


        // updating row
        int count = db.update(TABLE_PAYMENT_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }


//    // Deleting single contact
//    public void deleteitemsale(Item_Sale itemsales) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_ITEM_SALE_TRANSACTION, KEY_SALES_TRANS_ID + " = ?",
//                new String[] { String.valueOf(itemsales.getsale_trans_id()) });
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getitemsale() {
//        String countQuery = "SELECT  * FROM " + TABLE_ITEM_SALE_TRANSACTION;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAllItemssale(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_ITEM_SALE_TRANSACTION);
//        //////db.close();
//    }
//


    //Recharge Data .................................................................................................................


    void addrecharge(Recharge_Data rechrg) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_RECHARGE_ID, rechrg.getrecharge_id());
        values.put(KEY_TRANSACTION_ID, rechrg.gettransaction_id());
        values.put(KEY_RECHARGE_TIME, rechrg.getrecharge_time());
        values.put(KEY_RECHARGE_AMOUNT, rechrg.getRech_amt());
//        System.out.println("OKCODMOB"+rechrg.getRech_amt());
        values.put(KEY_PAYMENT_TYPE_ID, rechrg.getpayment_type_id());
        values.put(KEY_SERVER_TIMESTAMP, rechrg.getserver_timestamp());

        // Inserting Row
        db.insert(TABLE_RECHARGE_DATA, null, values);
        ////db.close(); // Closing database connection
    }


//    Recharge_Data getrechrg(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_RECHARGE_DATA, new String[] { KEY_RECHARGE_ID,
//                        KEY_TRANSACTION_ID, KEY_RECHARGE_TIME,KEY_RECHARGE_AMOUNT,KEY_PAYMENT_TYPE_ID,KEY_SERVER_TIMESTAMP
//                }, KEY_RECHARGE_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Recharge_Data rechrge = new Recharge_Data(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1),cursor.getString(2),Float.parseFloat(cursor.getString(3)), Integer.parseInt(cursor.getString(4)),cursor.getString(5));
//        // return contact
//        return rechrge;
//    }


    // Getting All users
    public List<Recharge_Data> getAllrech() {
        List<Recharge_Data> rechrgelist = new ArrayList<Recharge_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recharge_Data rechargedetails = new Recharge_Data();
                rechargedetails.setrecharge_id(Integer.parseInt(cursor.getString(0)));
                rechargedetails.settransaction_id(cursor.getString(1));
                rechargedetails.setRech_amt(cursor.getFloat(3));
                rechargedetails.setrecharge_time(cursor.getString(2));

                rechargedetails.setpayment_type_id(Integer.parseInt(cursor.getString(4)));
                rechargedetails.setserver_timestamp(cursor.getString(5));

                // Adding contact to list
                rechrgelist.add(rechargedetails);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();

        // return contact list
        return rechrgelist;
    }

    public List<Recharge_Data> getAllrechDESC() {
        List<Recharge_Data> rechrgelist = new ArrayList<Recharge_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_DATA + " ORDER BY " + KEY_RECHARGE_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recharge_Data rechargedetails = new Recharge_Data();
                rechargedetails.setrecharge_id(Integer.parseInt(cursor.getString(0)));
                rechargedetails.settransaction_id(cursor.getString(1));
                rechargedetails.setRech_amt(cursor.getFloat(3));
                rechargedetails.setrecharge_time(cursor.getString(2));

                rechargedetails.setpayment_type_id(Integer.parseInt(cursor.getString(4)));
                rechargedetails.setserver_timestamp(cursor.getString(5));

                // Adding contact to list
                rechrgelist.add(rechargedetails);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        //////db.close();

        // return contact list
        return rechrgelist;
    }

    // Updating single user
    public int updaterechrge(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_RECHARGE_DATA, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }


    public int updateOnline(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_ONLINE_TO_RECHARGE, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        // updating row
        db.setTransactionSuccessful();
        db.endTransaction();

        return count;
    }

//    // Deleting single contact
//    public void deleterecharge(Recharge_Data recharges) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_RECHARGE_DATA, KEY_RECHARGE_ID + " = ?",
//                new String[] { String.valueOf(recharges.getrecharge_id()) });
//        //////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getrechrg() {
//        String countQuery = "SELECT  * FROM " + TABLE_RECHARGE_DATA;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAllrecharge(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_RECHARGE_DATA);
//        ////db.close();
//    }

    //Refund Transaction Table

    public long addRefund(Refund refund) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TRANSACTION_ID, refund.getTrans_id()); // User Name
        values.put(KEY_ORIGINAL_TRANSACTION_ID, refund.getOrig_trans_id()); // User PAssword
        values.put(KEY_AMOUNT, refund.getAmount());
        values.put(KEY_SERVER_TIMESTAMP, refund.getServer_timestamp()); //server timestamp

        // Inserting Row
        long value= db.insert(TABLE_REFUND_TRANSACTION, null, values);
        return value;
        //db.close(); // Closing database connection
    }

    public List<Refund> getAllRefund() {
        List<Refund> refund = new ArrayList<Refund>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REFUND_TRANSACTION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Refund refunddata = new Refund();
                refunddata.setId(Integer.parseInt(cursor.getString(0)));
                refunddata.setTrans_id(cursor.getString(1));
                refunddata.setOrig_trans_id(cursor.getString(2));
                refunddata.setAmount(cursor.getFloat(3));
                refunddata.setServer_timestamp(cursor.getString(4));

                // Adding contact to list
                refund.add(refunddata);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        // return contact list
        return refund;
    }

    // Updating single user
    public int updaterefund(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());


        int count = db.update(TABLE_REFUND_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        // updating row
        //db.close();
        db.setTransactionSuccessful();
        db.endTransaction();

        return count;
    }


    //Payment Type............................................................................................................


    int addpaytype(ArrayList<Payment_Type> paytyp) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < paytyp.size(); ++i) {

            Payment_Type payment_type = paytyp.get(i);

            ContentValues values = new ContentValues();
            values.put(KEY_ID, payment_type.getid());
            values.put(KEY_PAYMENT_TYPE_NAME, payment_type.getpayment_type_name());
            values.put(KEY_SERVER_TIMESTAMP, payment_type.getserver_timestamp());

            // Inserting Row
            db.insert(TABLE_PAYMENT_TYPE, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;
        //db.close(); // Closing database connection
    }


//    Payment_Type getpaytyp(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_PAYMENT_TYPE, new String[] { KEY_ID,
//                        KEY_PAYMENT_TYPE_NAME, KEY_SERVER_TIMESTAMP
//                }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Payment_Type rechrge = new Payment_Type(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1),cursor.getString(2));
//        // return contact
//        return rechrge;
//    }

    // Getting All users
    public List<Payment_Type> getAllpay() {
        System.out.println("CODMOB: tesstt ");
        List<Payment_Type> pay_typelist = new ArrayList<Payment_Type>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_TYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment_Type paytype = new Payment_Type();
                paytype.setid(Integer.parseInt(cursor.getString(0)));
                paytype.setpayment_type_name(cursor.getString(1));
                paytype.setserver_timestamp(cursor.getString(2));

                // Adding contact to list
                pay_typelist.add(paytype);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return pay_typelist;
    }

    // Updating single user
//    public int updatepaytype(Payment_Type patype) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_ID, patype.getid());
//        values.put(KEY_PAYMENT_TYPE_NAME, patype.getpayment_type_name());
//        values.put(KEY_SERVER_TIMESTAMP,patype.getserver_timestamp());
//
//
//
//        // updating row
//        return db.update(TABLE_PAYMENT_TYPE, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(patype.getid()) });
//    }

    // Deleting single contact
//    public void deletepaytype(Payment_Type paytype) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_PAYMENT_TYPE, KEY_ID + " = ?",
//                new String[] { String.valueOf(paytype.getid()) });
//        ////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getpaytpe() {
//        String countQuery = "SELECT  * FROM " + TABLE_PAYMENT_TYPE;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
    public void deleteAllpaymentType() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_PAYMENT_TYPE);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }


    //Library Book Transaction......................................................................................................


    void addlib(Library_book_transaction lib) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, lib.getid());
        values.put(KEY_TRANSACTION_ID, lib.gettransaction_id());
        values.put(KEY_ISSUE_RETURN, lib.getissue_return());
        values.put(KEY_BOOK_ID, lib.getbook_id());
        values.put(KEY_ISSUE_TIME, lib.getissue_time());
        values.put(KEY_SERVER_TIMESTAMP, lib.getserver_timestamp());

        // Inserting Row
        db.insert(TABLE_LIBRARY_BOOK_TRANSACTION, null, values);
        //db.close(); // Closing database connection
    }


//    Library_book_transaction getlib(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_LIBRARY_BOOK_TRANSACTION, new String[] { KEY_ID,
//                        KEY_TRANSACTION_ID, KEY_ISSUE_RETURN,KEY_BOOK_ID,KEY_ISSUE_TIME,KEY_ISSUE_TIME
//                }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Library_book_transaction lib = new Library_book_transaction(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1),cursor.getString(2),cursor.getString(3),
//                cursor.getString(4),cursor.getString(5));
//        // return contact
//        return lib;
//    }

    // Getting All users
    public List<Library_book_transaction> getAlllib() {
        List<Library_book_transaction> liblist = new ArrayList<Library_book_transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LIBRARY_BOOK_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Library_book_transaction libr = new Library_book_transaction();
                libr.setid(Integer.parseInt(cursor.getString(0)));
                libr.settransaction_id(cursor.getString(1));
                libr.setissue_return(cursor.getString(2));
                libr.setbook_id(cursor.getString(3));
                libr.setissue_time(cursor.getString(4));
                libr.setserver_timestamp(cursor.getString(5));


                // Adding contact to list
                liblist.add(libr);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        // return contact list
        return liblist;
    }

    // Updating single user
    public int updatelibr(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());


        int count = db.update(TABLE_LIBRARY_BOOK_TRANSACTION, values, KEY_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.setTransactionSuccessful();
        db.endTransaction();

        //db.close();
        // updating row
        return count;
    }

//    // Deleting single contact
//    public void deletelibr(Library_book_transaction librar) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_LIBRARY_BOOK_TRANSACTION, KEY_ID + " = ?",
//                new String[] { String.valueOf(librar.getid()) });
//        ////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getlib() {
//        String countQuery = "SELECT  * FROM " + TABLE_LIBRARY_BOOK_TRANSACTION;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAlllibrary(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_LIBRARY_BOOK_TRANSACTION);
//        ////db.close();
//    }


//Sales Item.....................................................................................................................

    void addsale(Sales_Item sale) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_SALES_ITEM_ID, sale.getSales_item_id());
        values.put(KEY_SALES_TRANS_ID, sale.getsales_trans_id());
        values.put(KEY_ITEM_ID, sale.getitem_id());
        values.put(KEY_ITEM_QUANTITY, sale.getitem_quantity());
        values.put(KEY_AMOUNT, sale.getamount());
        values.put(KEY_SERVER_TIMESTAMP, sale.getserver_timestamp());

        // Inserting Row
        db.insert(TABLE_SALES_ITEM_LIST, null, values);
        //db.close(); // Closing database connection
    }

//    Sales_Item getsale(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_SALES_ITEM_LIST, new String[] { KEY_SALES_ITEM_ID,
//                        KEY_SALES_TRANS_ID, KEY_ITEM_ID,KEY_ITEM_QUANTITY,KEY_AMOUNT,KEY_SERVER_TIMESTAMP
//                }, KEY_SALES_ITEM_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Sales_Item sales= new Sales_Item(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1),cursor.getString(2),cursor.getString(3),
//                cursor.getString(4),cursor.getString(5));
//        // return contact
//        cursor.close();          // Dont forget to close your cursor
//        ////db.close();
//        return sales;
//    }

    // Getting All users
    public List<Sales_Item> getAllsale() {
        List<Sales_Item> liblist = new ArrayList<Sales_Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SALES_ITEM_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales_Item sl = new Sales_Item();
                sl.setSales_item_id(Integer.parseInt(cursor.getString(0)));
                sl.setsales_trans_id(cursor.getString(1));
                sl.setitem_id(cursor.getString(2));
                sl.setitem_quantity(cursor.getString(3));
                sl.setamount(cursor.getString(4));
                sl.setserver_timestamp(cursor.getString(5));

                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }

    // Updating single user
    public int updatesale(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());

        int count = db.update(TABLE_SALES_ITEM_LIST, values, KEY_SALES_ITEM_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
        // updating row
        return count;
    }


    public int updatesaleRefund1(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, "-1");


        int count = db.update(TABLE_SALES_ITEM_LIST, values, KEY_SALES_TRANS_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
        // updating row
        return count;
    }

    public int updatesaleRefund2(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, "-2");


        // updating row
        int count = db.update(TABLE_SALES_ITEM_LIST, values, KEY_SALES_TRANS_ID + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }


//    // Deleting single contact
//    public void deletesales(Sales_Item sales) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_SALES_ITEM_LIST, KEY_SALES_ITEM_ID + " = ?",
//                new String[] { String.valueOf(sales.getitem_id()) });
//        ////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getsale() {
//        String countQuery = "SELECT  * FROM " + TABLE_SALES_ITEM_LIST;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    public  void deleteAllSAle(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TABLE_SALES_ITEM_LIST);
//        ////db.close();
//    }


    //Category List..................................................................................................................


    int addcateg(ArrayList<CategoryList> cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < cat.size(); ++i) {

            CategoryList categoryList = cat.get(i);

            ContentValues values = new ContentValues();

            values.put(KEY_CATEGORY_ID, categoryList.getcategory_id());
            values.put(KEY_CATEGORY_NAME, categoryList.getcategory_name());
            values.put(KEY_CATEGORY_SHORT_NAME, categoryList.getcategory_shortname());
            values.put(KEY_ITEM_TYPE_ID, categoryList.getItem_type_id());

            values.put(KEY_SERVER_TIMESTAMP, categoryList.getserver_timestamp());

            // Inserting Row
            db.insert(TABLE_CATEGORY_LIST, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;
        //db.close(); // Closing database connection
    }

//    CategoryList getcate(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_CATEGORY_LIST, new String[] { KEY_CATEGORY_ID,
//                        KEY_CATEGORY_NAME, KEY_CATEGORY_SHORT_NAME,KEY_SERVER_TIMESTAMP
//                }, KEY_CATEGORY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        CategoryList cat= new CategoryList(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),
//                cursor.getString(2),cursor.getString(3),cursor.getString(4));
//        // return contact
//        cursor.close();          // Dont forget to close your cursor
//        ////db.close();
//        return cat;
//    }

    // Getting All users
    public List<CategoryList> getAllcat() {
        List<CategoryList> catlist = new ArrayList<CategoryList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CategoryList category = new CategoryList();
                category.setcategory_id(Integer.parseInt(cursor.getString(0)));
                category.setItem_type_id(Integer.parseInt(cursor.getString(1)));
                category.setcategory_name(cursor.getString(2));
                category.setcategory_shortname(cursor.getString(3));
                category.setserver_timestamp(cursor.getString(4));


                // Adding contact to list
                catlist.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return catlist;
    }

    // Updating single user
//    public int updatecategory(CategoryList cat) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_CATEGORY_ID, cat.getcategory_id());
//        values.put(KEY_ITEM_TYPE_ID, cat.getItem_type_id());
//        values.put(KEY_CATEGORY_NAME, cat.getcategory_name());
//        values.put(KEY_CATEGORY_SHORT_NAME,cat.getcategory_shortname());
//        values.put(KEY_SERVER_TIMESTAMP,cat.getserver_timestamp());
//
//
//
//        // updating row
//        return db.update(TABLE_CATEGORY_LIST, values, KEY_CATEGORY_ID + " = ?",
//                new String[] { String.valueOf(cat.getcategory_id()) });
//    }
//
//    // Deleting single contact
//    public void deletecatgrs(CategoryList cats) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CATEGORY_LIST, KEY_CATEGORY_ID + " = ?",
//                new String[] { String.valueOf(cats.getcategory_id()) });
//        ////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getcat() {
//        String countQuery = "SELECT  * FROM " + TABLE_CATEGORY_LIST;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
    public void deleteAllcat() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_CATEGORY_LIST);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }


//ITEM LIST...........................................................................................................................


    int addNewItem(ArrayList<ItemList> itemList) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < itemList.size(); ++i) {
            ContentValues values = new ContentValues();

            ItemList itemList1 = itemList.get(i);

            values.put(KEY_ITEM_ID, itemList1.getItem_id()); // User Name
            values.put(KEY_ITEM_CODE, itemList1.getItem_code()); // User Name
            values.put(KEY_ITEM_NAME, itemList1.getItem_name());
            values.put(KEY_ITEM_SHORTNAME, itemList1.getItem_shortname());// User PAssword
            values.put(KEY_CATEGORY_ID, itemList1.getCategory_id()); //server timestamp
            values.put(KEY_PRICE, itemList1.getprice()); //server timestamp
            values.put(KEY_STOCK_STATUS, itemList1.getstock_status()); //server timestamp
            values.put(KEY_ITEM_TYPE_ID, itemList1.getitem_type_id()); //server timestamp
//        values.put(KEY_SERVER_TIMESTAMP,itemList.getServer_timestamp()); //server timestamp
            // Inserting Row
            db.insert(TABLE_ITEM_LIST, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close(); // Closing database connection

        return 0;
    }

//    ItemList getitemlist(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_ITEM_LIST, new String[] { KEY_ITEM_ID,KEY_ITEM_CODE,KEY_ITEM_NAME,KEY_ITEM_SHORTNAME,
//                        KEY_CATEGORY_ID,KEY_PRICE,KEY_STOCK_STATUS,KEY_ITEM_TYPE_ID,KEY_SERVER_TIMESTAMP}, KEY_ITEM_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        ItemList itls= new ItemList(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1),cursor.getString(2),cursor.getString(3),
//                Integer.parseInt(cursor.getString(4)),Float.parseFloat(cursor.getString(5)),cursor.getString(6),Integer.parseInt(cursor.getString(7)),cursor.getString(8));
//        // return contact
//        return itls;
//    }

    // Getting All users
    public List<ItemList> getAllitemlist() {
        List<ItemList> itlist = new ArrayList<ItemList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemList itemlists = new ItemList();
                itemlists.setItem_id(Integer.parseInt(cursor.getString(0)));
                itemlists.setItem_code(cursor.getString(1));
                itemlists.setItem_name(cursor.getString(2));
                itemlists.setItem_shortname(cursor.getString(3));
                itemlists.setCategory_id(Integer.parseInt(cursor.getString(4)));
                itemlists.setprice(Float.valueOf(cursor.getString(5)));
                itemlists.setstock_status(cursor.getString(6));
                itemlists.setitem_type_id(cursor.getInt(7));
//                itemlists.setServer_timestamp(cursor.getString(7));


                // Adding contact to list
                itlist.add(itemlists);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return itlist;
    }

    //    // Updating single user
//    public int updateitemlist(ItemList itemlist) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_ITEM_ID, itemlist.getItem_id());
//        values.put(KEY_ITEM_CODE, itemlist.getItem_code());
//        values.put(KEY_ITEM_NAME,itemlist.getItem_name());
//        values.put(KEY_ITEM_SHORTNAME,itemlist.getItem_shortname());
//        values.put(KEY_CATEGORY_ID,itemlist.getCategory_id());
//        values.put(KEY_STOCK_STATUS,itemlist.getstock_status());
//        values.put(KEY_ITEM_TYPE_ID,itemlist.getitem_type_id());
//        values.put(KEY_PRICE,itemlist.getprice());
//        values.put(KEY_SERVER_TIMESTAMP,itemlist.getServer_timestamp());
//
//
//        // updating row
//        return db.update(TABLE_ITEM_LIST, values, KEY_ITEM_ID + " = ?",
//                new String[] { String.valueOf(itemlist.getItem_id()) });
//    }
//
//    // Deleting single contact
//    public void deleteitemlists(ItemList items) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_ITEM_LIST, KEY_ITEM_ID + " = ?",
//                new String[] { String.valueOf(items.getItem_id()) });
//        ////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getitem() {
//        String countQuery = "SELECT  * FROM " + TABLE_ITEM_LIST;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
    public void deleteAllitem() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_ITEM_LIST);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }


//ITEM TYPE......................................................................................................................


    int addNewtype(ArrayList<Item_Type> itemtype) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < itemtype.size(); ++i) {

            Item_Type item_type = itemtype.get(i);
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TYPE_ID, item_type.getitem_type_id()); // User Name
            values.put(KEY_ITEM_TYPE_NAME, item_type.getitem_type_name());

            values.put(KEY_SERVER_TIMESTAMP, item_type.getserver_timestamp()); //server timestamp
            // Inserting Row
            db.insert(TABLE_ITEM_TYPE, null, values);

        }
        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;
        //db.close(); // Closing database connection

    }

//    Item_Type getitemtype(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_ITEM_TYPE, new String[] { KEY_ITEM_TYPE_ID,
//                        KEY_ITEM_TYPE_NAME, KEY_SERVER_TIMESTAMP
//                }, KEY_ITEM_TYPE_ID+ "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Item_Type itls= new Item_Type(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1),cursor.getString(2));
//        // return contact
//        cursor.close();          // Dont forget to close your cursor
//        ////db.close();
//        return itls;
//    }

    Item_Type getitemtypebyname(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEM_TYPE, new String[]{KEY_ITEM_TYPE_ID,
                        KEY_ITEM_TYPE_NAME, KEY_SERVER_TIMESTAMP
                }, KEY_ITEM_TYPE_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Item_Type itls = new Item_Type(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return itls;
    }

    public int getItemTypeId(String name) {

        int typeID = 0;

        String selectQuery = "SELECT * FROM " + TABLE_ITEM_TYPE + " WHERE " + KEY_ITEM_TYPE_NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            cursor.moveToFirst();
            System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_SCHOOL_ID)));
//
            typeID = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_TYPE_ID));

            cursor.close();          // Dont forget to close your cursor
            ////db.close();

        } catch (Exception e) {

//            getSchoolId();
        }
        return typeID;
    }


    CategoryList getCatIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY_LIST, new String[]{KEY_CATEGORY_ID,
                        KEY_ITEM_TYPE_ID, KEY_CATEGORY_NAME, KEY_CATEGORY_SHORT_NAME, KEY_SERVER_TIMESTAMP
                }, KEY_CATEGORY_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CategoryList cat = new CategoryList(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return contact
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return cat;
    }

    // Getting All users
    public List<Item_Type> getAllitemtype() {
        List<Item_Type> itemlist = new ArrayList<Item_Type>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_TYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item_Type ittype = new Item_Type();
                ittype.setitem_type_id(Integer.parseInt(cursor.getString(0)));
                ittype.setitem_type_name(cursor.getString(1));
                ittype.setserver_timestamp(cursor.getString(2));


                // Adding contact to list
                itemlist.add(ittype);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return itemlist;
    }

//    // Updating single user
//    public int updateitemtype(Item_Type itemtype) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_ITEM_TYPE_ID, itemtype.getitem_type_id());
//        values.put(KEY_ITEM_TYPE_NAME, itemtype.getitem_type_name());
//        values.put(KEY_SERVER_TIMESTAMP,itemtype.getserver_timestamp());
//
//
//
//        // updating row
//        return db.update(TABLE_ITEM_TYPE, values, KEY_ITEM_TYPE_ID + " = ?",
//                new String[] { String.valueOf(itemtype.getitem_type_id()) });
//    }

    //    // Deleting single contact
//    public void deleteiitemtypes(Item_Type itemtypesss) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_ITEM_TYPE, KEY_ITEM_TYPE_ID + " = ?",
//                new String[] { String.valueOf(itemtypesss.getitem_type_id()) });
//        ////db.close();
//    }
//
//
//    // Getting contacts Count
//    public int getitemtype() {
//        String countQuery = "SELECT  * FROM " + TABLE_ITEM_TYPE;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
    public void deleteAllitemtype() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_ITEM_TYPE);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }


    //CHILLAR: Get last transaction Id

    public String lastTransID() {
        String trans_id = null;
        String trans_id1 = null;
        String sub1 = null;
        String sub2 = null;
        String mcn_id = null;
        String filler = "";

        try {
            String selectQuery = "SELECT * FROM success_transaction";// ORDER BY transaction_id DESC LIMIT 1";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);


            try {
                cursor.moveToLast();

                Success_Transaction opdatadev = new Success_Transaction();
                System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_ID)));
//
                trans_id = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_ID));
                // Adding contact to list

                System.out.println("CHILLAR: LastTransID : " + trans_id);


                int length = trans_id.length();

                if (length >= 5) {
                    sub1 = trans_id.substring(0, 6);
                    sub2 = trans_id.substring(6, 16);

                }

                System.out.println("c " + length);
                System.out.println("CHILLAR: LastTransID Sub1 : " + sub1);
                System.out.println("CHILLAR: LastTransID Sub2 : " + sub2);

                int sub_int = Integer.parseInt(sub2) + 1;

//            if(String.valueOf(sub_int).length()==1){

//                trans_id1=sub1+"000000000"+sub_int;
//                System.out.println("CHILLAR: LastTransID Lenghth=1 ");
//            }else if(String.valueOf(sub_int).length()>1)
//            {
                int len = 10 - String.valueOf(sub_int).length();

                for (int i = 0; i < len; ++i) {

                    filler = filler + "0";

                }
                System.out.println("CHILLAR: LastTransID Filler " + filler);
                System.out.println("CHILLAR: LastTransID Filler Length " + filler.length());

                trans_id1 = sub1 + filler + sub_int;


                System.out.println("CHILLAR: LastTransID New Inc " + trans_id1);


//            }


            } catch (CursorIndexOutOfBoundsException e) {

                System.out.println("CHILLAR: No Transaction in DB : " + e.toString());

                int last = 0;
                String first = "";
                String laststr = "";
                String fillerr = "";
                String selectQuery1 = "SELECT * FROM " + TABLE_DEVICE_INFO;// ORDER BY transaction_id DESC LIMIT 1";
                SQLiteDatabase db1 = this.getWritableDatabase();
                Cursor cursor1 = db1.rawQuery(selectQuery1, null);

                if (cursor1.moveToFirst()) {
                    do {

                        mcn_id = cursor1.getString(cursor1.getColumnIndex(KEY_DEVICE_LAST_TRANS_ID));

                        System.out.println("CHILLARCODMOB: " + mcn_id);
                        last = Integer.parseInt(mcn_id.substring(12, 16));
                        first = mcn_id.substring(0, 12);
                        last = last + 1;
                        laststr = String.valueOf(last);
                        int len = 4 - laststr.length();

                        for (int i = 0; i < len; ++i) {

                            fillerr = fillerr + 0;

                        }
                        laststr = fillerr + laststr;

                        trans_id1 = first + laststr;


                    } while (cursor1.moveToNext());
                }

                System.out.println("CHILLAR: LastTransID Frem server " + trans_id1);


            }
            cursor.close();          // Dont forget to close your cursor
            ////db.close();
        } catch (IllegalStateException e) {
//            lastTransID();
        }
        return trans_id1;
    }


    public String getBillno() {
        String trans_id = null;
        String billno = null;
        String sub1 = null;
        String sub2 = null;
        String mcn_id = null;
        String filler = "";

        try {

            String selectQuery = "SELECT * FROM success_transaction";// ORDER BY transaction_id DESC LIMIT 1";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);


            try {
                cursor.moveToLast();

                Success_Transaction opdatadev = new Success_Transaction();
                System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_ID)));
//
                trans_id = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_ID));
                // Adding contact to list

                System.out.println("CHILLAR: LastTransID : " + trans_id);


                int length = trans_id.length();

                if (length >= 5) {
                    sub1 = trans_id.substring(0, 6);
                    sub2 = trans_id.substring(6, 16);

                }

                System.out.println("CHILLAR: LastTransID Length : " + length);
                System.out.println("CHILLAR: LastTransID Sub1 : " + sub1);
                System.out.println("CHILLAR: LastTransID Sub2 : " + sub2);

                int sub_int = Integer.parseInt(sub2) + 1;

//            if(String.valueOf(sub_int).length()==1){

//                trans_id1=sub1+"000000000"+sub_int;
//                System.out.println("CHILLAR: LastTransID Lenghth=1 ");
//            }else if(String.valueOf(sub_int).length()>1)
//            {
                int len = 10 - String.valueOf(sub_int).length();

                for (int i = 0; i < len; ++i) {

                    filler = filler + "0";

                }
                System.out.println("CHILLAR: LastTransID Filler " + filler);
                System.out.println("CHILLAR: LastTransID Filler Length " + filler.length());

                billno = filler + sub_int;


                System.out.println("CHILLAR: BillNo New Inc " + billno);


//            }


            } catch (CursorIndexOutOfBoundsException e) {

                System.out.println("CHILLAR: No Transaction in DB : " + e.toString());

//            String selectQuery1 = "SELECT * FROM "+TABLE_DEVICE_INFO;// ORDER BY transaction_id DESC LIMIT 1";
//            SQLiteDatabase db1 = this.getWritableDatabase();
//            Cursor cursor1 = db1.rawQuery(selectQuery1, null);
//
//            if (cursor1.moveToFirst()) {
//                do {
//
//                    mcn_id = cursor1.getString(cursor1.getColumnIndex(KEY_MACHINE_ID));
//
//                    System.out.println("CHILLARCODMOB: "+ mcn_id);
//
//
//                } while (cursor1.moveToNext());
//            }
//

                billno = "0000000000";
                System.out.println("CHILLAR: LastTransID Billnonew " + billno);


            }
            cursor.close();          // Dont forget to close your cursor
            ////db.close();
        } catch (IllegalStateException e) {

//            getBillno();
        }
        return billno;
    }

    public String getSchoolId() {

        String schoolID = "";

        String selectQuery = "SELECT * FROM " + TABLE_DEVICE_INFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            cursor.moveToFirst();
            System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_SCHOOL_ID)));
//
            schoolID = cursor.getString(cursor.getColumnIndex(KEY_SCHOOL_ID));

            cursor.close();          // Dont forget to close your cursor
            ////db.close();

        } catch (Exception e) {

//            getSchoolId();
        }
        return schoolID;
    }


    public String getSchoolName() {

        String schoolID = "";

        String selectQuery = "SELECT * FROM " + TABLE_DEVICE_INFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            cursor.moveToFirst();
            System.out.println("column school name: " + cursor.getString(cursor.getColumnIndex(KEY_SCHOOL_NAME)));

            schoolID = cursor.getString(cursor.getColumnIndex(KEY_SCHOOL_NAME));

            cursor.close();          // Dont forget to close your cursor

        } catch (Exception e) {
            e.printStackTrace();
        }
        return schoolID;
    }


    public int getTransTypID(String trans_name) {
        int TransTypeId;

        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTION_TYPE + " where " + KEY_TRANSACTION_TYPE_NAME + " like " + "'" + trans_name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE_ID)));
//
        TransTypeId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE_ID)));

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return TransTypeId;
    }

    public String getItemNamebyID(String itemId) {
        String ItName = null;

        String selectQuery = "SELECT * FROM " + TABLE_ITEM_LIST + " WHERE " + KEY_ITEM_ID + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{itemId});

        try {
            if (cursor.moveToFirst()) {
                // The cursor is not empty, retrieve the data
                ItName = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME));
            } else {
                // The cursor is empty, handle accordingly (e.g., set a default value or log a message)
                ItName = "Item not found"; // Example default value
            }
        } finally {
            // Always close the cursor when you're done with it
            cursor.close();
        }

        return ItName;
    }

    public String getMachineID() {
        String McID = "";
        String selectQuery = "SELECT * FROM " + TABLE_DEVICE_INFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            cursor.moveToFirst();
            System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_MACHINE_ID)));
//
            McID = cursor.getString(cursor.getColumnIndex(KEY_MACHINE_ID));

            cursor.close();          // Dont forget to close your cursor
            ////db.close();
        } catch (Exception e) {
            cursor.close();          // Dont forget to close your cursor
            ////db.close();
        }
        return McID;
    }

    public int lastRechID() {

        int rech_id;

        String selectQuery = "SELECT * FROM " + TABLE_RECHARGE_DATA;// ORDER BY transaction_id DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        try {
            cursor.moveToLast();

            System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_RECHARGE_ID)));
//
            rech_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_RECHARGE_ID)));
            // Adding contact to list

            System.out.println("CHILLAR: LastTransID : " + rech_id);

        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("CHILLAR: No Transaction in DB : " + e.toString());
            rech_id = 0;
            cursor.close();          // Dont forget to close your cursor
            ////db.close();
            return rech_id;


        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        return rech_id + 1;
    }

    public String getAttTypbyID(String itemId) {
        String AttType;

        String selectQuery = "SELECT * FROM " + TABLE_ATTENDENCE_TYPE + " where " + KEY_ID + " = " + "'" + itemId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_ATTENDENCE_TYPE_NAME)));
//
        AttType = cursor.getString(cursor.getColumnIndex(KEY_ATTENDENCE_TYPE_NAME));
        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        return AttType;
    }

//    public int getUserID(String usrname){
//        int userID;
//
//        String selectQuery = "SELECT * FROM "+TABLE_USER+" where "+KEY_USER_NAME+" = "+"'"+usrname+"'";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//        cursor.moveToFirst();
//        System.out.println("column: " + cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
////
//        userID= cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));
//
//
//        return userID;
//    }

    public List<Success_Transaction> getAllsuccesstoup() {
        List<Success_Transaction> successtrans = new ArrayList<Success_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Success_Transaction successss = new Success_Transaction();
                successss.settrans_id(cursor.getString(0));
                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
                successss.setcard_serial(cursor.getString(5));
                successss.settime_stamp(cursor.getString(6));
                successss.setserver_timestamp(cursor.getString(7));
                // Adding contact to list
                successtrans.add(successss);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return successtrans;
    }


    public List<Attendance_Data> getAllAttendancedatatoUP() {
        List<Attendance_Data> attndncdataList = new ArrayList<Attendance_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDENCE_DATA + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Attendance_Data attndncdata = new Attendance_Data();
                attndncdata.setAttendance_data_id(Integer.parseInt(cursor.getString(0)));
                attndncdata.setTransaction_id(cursor.getString(1));
                attndncdata.setIn_out(Integer.parseInt((cursor.getString(2))));
                attndncdata.setAttendance_type_id(Integer.parseInt((cursor.getString(3))));
                attndncdata.setServer_timestamp(cursor.getString(4));

                // Adding contact to list
                attndncdataList.add(attndncdata);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return attndncdataList;
    }


    public List<Attendance_Data> getAllteacherAttendancedatatoUP() {
        List<Attendance_Data> attndncdataList = new ArrayList<Attendance_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ATTENDENCE_DATA + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Attendance_Data attndncdata = new Attendance_Data();
                attndncdata.setAttendance_data_id(Integer.parseInt(cursor.getString(0)));
                attndncdata.setTransaction_id(cursor.getString(1));
                attndncdata.setIn_out(Integer.parseInt((cursor.getString(2))));
                attndncdata.setAttendance_type_id(Integer.parseInt((cursor.getString(3))));
                attndncdata.setcard_serial(cursor.getString(4));
                attndncdata.setServer_timestamp(cursor.getString(5));

                // Adding contact to list
                attndncdataList.add(attndncdata);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return attndncdataList;
    }


    public List<Recharge_Data> getAllrechtoUp() {
        List<Recharge_Data> rechrgelist = new ArrayList<Recharge_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_DATA + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recharge_Data rechargedetails = new Recharge_Data();
                rechargedetails.setrecharge_id(Integer.parseInt(cursor.getString(0)));
                rechargedetails.settransaction_id(cursor.getString(1));
                rechargedetails.setrecharge_time(cursor.getString(2));
                rechargedetails.setRech_amt(cursor.getFloat(3));
                rechargedetails.setpayment_type_id(Integer.parseInt(cursor.getString(4)));
                rechargedetails.setserver_timestamp(cursor.getString(5));

                // Adding contact to list
                rechrgelist.add(rechargedetails);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return rechrgelist;
    }


    public List<Fee_Transaction> getAllfeetranstoUp() {
        List<Fee_Transaction> feetransact = new ArrayList<Fee_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEES_TRANSACTIONS + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Fee_Transaction paytrans = new Fee_Transaction();
                paytrans.setfee_tran_id(Integer.parseInt(cursor.getString(0)));
                paytrans.setbill_no(cursor.getString(1));
                paytrans.settot_am(Float.parseFloat(cursor.getString(2)));
                paytrans.settrans_id(cursor.getString(3));
                paytrans.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(paytrans);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return feetransact;
    }


    public List<Library_book_transaction> getAlllibtoUp() {
        List<Library_book_transaction> liblist = new ArrayList<Library_book_transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LIBRARY_BOOK_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Library_book_transaction libr = new Library_book_transaction();
                libr.setid(Integer.parseInt(cursor.getString(0)));
                libr.settransaction_id(cursor.getString(1));
                libr.setissue_return(cursor.getString(2));
                libr.setbook_id(cursor.getString(3));
                libr.setissue_time(cursor.getString(4));
                libr.setserver_timestamp(cursor.getString(5));


                // Adding contact to list
                liblist.add(libr);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }


    public List<Payment_Transaction> getAllpaytransactiontoUp() {
        List<Payment_Transaction> paytrans = new ArrayList<Payment_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment_Transaction paytranss = new Payment_Transaction();

                paytranss.setId(Integer.parseInt(cursor.getString(0)));
                paytranss.setbillno(cursor.getString(1));
                paytranss.settrans_id(cursor.getString(2));

                paytranss.setamount(Float.parseFloat(cursor.getString(3)));


                paytranss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                paytrans.add(paytranss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return paytrans;
    }

    public List<Payment_Transaction> getAllpaytransactiontoUpNew() {
        List<Payment_Transaction> paytrans = new ArrayList<Payment_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = '-1'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment_Transaction paytranss = new Payment_Transaction();

                paytranss.setId(Integer.parseInt(cursor.getString(0)));
                paytranss.setbillno(cursor.getString(1));
                paytranss.settrans_id(cursor.getString(2));

                paytranss.setamount(Float.parseFloat(cursor.getString(3)));


                paytranss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                paytrans.add(paytranss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return paytrans;
    }

    public List<Item_Sale> getAllitemsaletoUp() {
        List<Item_Sale> feetransact = new ArrayList<Item_Sale>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_SALE_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item_Sale itemsss = new Item_Sale();
                itemsss.setsale_trans_id(cursor.getString(0));
                itemsss.settransaction_id(cursor.getString(1));
                itemsss.setbill_no(cursor.getString(2));
                itemsss.settot_amount(cursor.getString(3));
                itemsss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(itemsss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return feetransact;
    }

    public List<Sales_Item> getAllsaletoUp() {
        List<Sales_Item> liblist = new ArrayList<Sales_Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SALES_ITEM_LIST + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales_Item sl = new Sales_Item();
                sl.setSales_item_id(Integer.parseInt(cursor.getString(0)));
                sl.setsales_trans_id(cursor.getString(1));
                sl.setitem_id(cursor.getString(2));
                sl.setitem_quantity(cursor.getString(3));
                sl.setamount(cursor.getString(4));
                sl.setserver_timestamp(cursor.getString(5));


                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }

    public List<Refund> getAllRefundtoUp() {
        List<Refund> liblist = new ArrayList<Refund>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REFUND_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Refund sl = new Refund();
                sl.setId(Integer.parseInt(cursor.getString(0)));
                sl.setTrans_id(cursor.getString(1));
                sl.setOrig_trans_id(cursor.getString(2));
                sl.setAmount(cursor.getFloat(3));
                sl.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }

    public List<Success_Transaction> getAllsuccessbyBill(String transid) {
        List<Success_Transaction> successtrans = new ArrayList<Success_Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION + " where " + KEY_TRANSACTION_ID + " = " + "'" + transid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Success_Transaction successss = new Success_Transaction();
                successss.settrans_id(cursor.getString(0));
                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
                successss.setcard_serial(cursor.getString(5));
                successss.settime_stamp(cursor.getString(6));
                successss.setserver_timestamp(cursor.getString(7));
                // Adding contact to list
                successtrans.add(successss);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return successtrans;
    }


    public List<Item_Sale> getAllitemsalebyBill(String transid) {
        List<Item_Sale> feetransact = new ArrayList<Item_Sale>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ITEM_SALE_TRANSACTION + " where " + KEY_TRANSACTION_ID + " = " + "'" + transid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item_Sale itemsss = new Item_Sale();
                itemsss.setsale_trans_id(cursor.getString(0));
                itemsss.settransaction_id(cursor.getString(1));
                itemsss.setbill_no(cursor.getString(2));
                itemsss.settot_amount(cursor.getString(3));
                itemsss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(itemsss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        // return contact list
        return feetransact;
    }

    public List<Payment_Transaction> getallPaymentbyBill(String transid) {
        List<Payment_Transaction> feetransact = new ArrayList<Payment_Transaction>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PAYMENT_TRANSACTION + " where " + KEY_TRANSACTION_ID + " = " + "'" + transid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment_Transaction itemsss = new Payment_Transaction();
                itemsss.setId(Integer.parseInt(cursor.getString(0)));
                itemsss.setbillno(cursor.getString(1));
                itemsss.settrans_id(cursor.getString(2));
                itemsss.setamount(Float.valueOf(cursor.getString(3)));
                itemsss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(itemsss);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        // return contact list
        return feetransact;
    }

    public List<Sales_Item> getAllsalebyBill(String trans_id) {
        List<Sales_Item> liblist = new ArrayList<Sales_Item>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SALES_ITEM_LIST + " where " + KEY_SALES_TRANS_ID + " = " + "'" + trans_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales_Item sl = new Sales_Item();
                sl.setSales_item_id(Integer.parseInt(cursor.getString(0)));
                sl.setsales_trans_id(cursor.getString(1));
                sl.setitem_id(cursor.getString(2));
                sl.setitem_quantity(cursor.getString(3));
                sl.setamount(cursor.getString(4));
                sl.setserver_timestamp(cursor.getString(5));


                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();


        // return contact list
        return liblist;
    }

//    public int updatesalebyBill(Sales_Item lib) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_SALES_ITEM_ID, lib.getSales_item_id());
//        values.put(KEY_SALES_TRANS_ID, lib.getsales_trans_id());
//        values.put(KEY_ITEM_ID,lib.getitem_id());
//        values.put(KEY_ITEM_QUANTITY, lib.getitem_quantity());
//        values.put(KEY_AMOUNT, lib.getamount());
//        values.put(KEY_SERVER_TIMESTAMP,lib.getserver_timestamp());
//
//
//
//        // updating row
//        return db.update(TABLE_SALES_ITEM_LIST, values, KEY_SALES_TRANS_ID + " = ?",
//                new String[] { String.valueOf(lib.getsales_trans_id()) });
//
//    }


    public List<Success_Transaction> getAllsxstoupNew() {
        List<Success_Transaction> successtrans = new ArrayList<Success_Transaction>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SUCCESS_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = '-1'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Success_Transaction successss = new Success_Transaction();
                successss.settrans_id(cursor.getString(0));
                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
                successss.setcard_serial(cursor.getString(5));
                successss.settime_stamp(cursor.getString(6));
                successss.setserver_timestamp(cursor.getString(7));
                // Adding contact to list
                successtrans.add(successss);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return successtrans;
    }


    public List<Item_Sale> getAllitemsaletoUpNew() {
        List<Item_Sale> feetransact = new ArrayList<Item_Sale>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ITEM_SALE_TRANSACTION + " where " + KEY_SERVER_TIMESTAMP + " = '-1'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item_Sale itemsss = new Item_Sale();
                itemsss.setsale_trans_id(cursor.getString(0));
                itemsss.settransaction_id(cursor.getString(1));
                itemsss.setbill_no(cursor.getString(2));
                itemsss.settot_amount(cursor.getString(3));
                itemsss.setserver_timestamp(cursor.getString(4));

                // Adding contact to list
                feetransact.add(itemsss);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return feetransact;
    }

    public List<Sales_Item> getAllsaletoUpNew() {
        List<Sales_Item> liblist = new ArrayList<Sales_Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SALES_ITEM_LIST + " where " + KEY_SERVER_TIMESTAMP + " = '-1'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales_Item sl = new Sales_Item();
                sl.setSales_item_id(Integer.parseInt(cursor.getString(0)));
                sl.setsales_trans_id(cursor.getString(1));
                sl.setitem_id(cursor.getString(2));
                sl.setitem_quantity(cursor.getString(3));
                sl.setamount(cursor.getString(4));
                sl.setserver_timestamp(cursor.getString(5));


                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }

    public List<Online_Recharge> getAllOnlineToUp() {
        List<Online_Recharge> liblist = new ArrayList<Online_Recharge>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ONLINE_TO_RECHARGE + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Online_Recharge sl = new Online_Recharge();
                sl.setId(Integer.parseInt(cursor.getString(0)));
                sl.setTransID(cursor.getString(1));
                sl.setOnlineTransID(cursor.getString(2));
                ;
                sl.setServerTimestamp(cursor.getString(3));

                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }

//    public List<Success_Transaction> getAllSuccessbyDate(String stdt,String enddt) {
//        List<Success_Transaction> successtrans = new ArrayList<Success_Transaction>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION +" WHERE "+ KEY_TIME_STAMP +" BETWEEN "+"'"+ stdt +"'"+ " AND " +"'"+ enddt+"'";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Success_Transaction successss = new Success_Transaction();
//                successss.settrans_id(cursor.getString(0));
//                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
//                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
//                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
//                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
//                successss.setcard_serial(cursor.getString(5));
//                successss.settime_stamp(cursor.getString(6));
//                successss.setserver_timestamp(cursor.getString(7));
//                // Adding contact to list
//                successtrans.add(successss);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();          // Dont forget to close your cursor
////        ////db.close();
//        // return contact list
//        return successtrans;
//    }

    public String getSTSbytrnsID(String trnsid, int type_id) {
        String serverts = "0";

//        String selectQuery = "SELECT * FROM "+TABLE_SUCCESS_TRANSACTION+" where "+KEY_TRANSACTION_ID+" = "+"'"+trnsid+"'"/*+" AND "+KEY_TRANSACTION_TYPE_ID+" = "+type_id*/;

//        String selectQuery = "SELECT * from "+TABLE_SUCCESS_TRANSACTION+" where "+KEY_TRANSACTION_ID+"='"+trnsid+"' AND ( "+KEY_TRANSACTION_TYPE_ID+" != 4 OR "+KEY_TRANSACTION_TYPE_ID+" != 5 OR "+KEY_TRANSACTION_TYPE_ID+" != 6 OR "+KEY_TRANSACTION_TYPE_ID
//                +" != 9 OR "+KEY_TRANSACTION_TYPE_ID+" != 15 OR "+KEY_TRANSACTION_TYPE_ID+" != 16)";
        String selectQuery = "SELECT * from " + TABLE_SUCCESS_TRANSACTION + " where " + KEY_TRANSACTION_ID + "='" + trnsid + "'";/* AND "+KEY_TRANSACTION_TYPE_ID+" = "+type_id*/
        ;
//                != 15 AND "+KEY_TRANSACTION_TYPE_ID+" != 6 )";/*or "+KEY_TRANSACTION_TYPE_ID+" != 5 or "+KEY_TRANSACTION_TYPE_ID+" != 6 or "+KEY_TRANSACTION_TYPE_ID
//                +" != 9 or "+KEY_TRANSACTION_TYPE_ID+" != 15 or "+KEY_TRANSACTION_TYPE_ID+" != 16)";*/
        System.out.println("Queeryyyyy: " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        System.out.println("Cursor:" + cursor);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {

            System.out.println("column: timestamp getSTSbytrnsID " + cursor.getString(cursor.getColumnIndex(KEY_SERVER_TIMESTAMP)));
//
            serverts = cursor.getString(cursor.getColumnIndex(KEY_SERVER_TIMESTAMP));
        } else {
            System.out.println("ChillarApp: cursor exception");
        }
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return serverts;
    }

    public List<Success_Transaction> getSuccessdatewise(String date1, String trans_type_id) {
        List<Success_Transaction> successtransdates = new ArrayList<Success_Transaction>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION+" where "+KEY_TIME_STAMP+" like '%"+date1+"%'"+" and "
//                +KEY_SERVER_TIMESTAMP+" !='-1' or "+KEY_SERVER_TIMESTAMP+" !='-2' "+" and "+TABLE_SUCCESS_TRANSACTION+"."+KEY_TRANSACTION_TYPE_ID+
//                " !=(SELECT "+KEY_TRANSACTION_TYPE_ID+" FROM "+TABLE_TRANSACTION_TYPE+" where "+KEY_TRANSACTION_TYPE_NAME+" like "+"'REFUND')";

        /*String selectQuery="SELECT  * FROM "+TABLE_SUCCESS_TRANSACTION+
                " WHERE time_stamp LIKE "+"'%"+date1+"%' AND "+TABLE_SUCCESS_TRANSACTION+"."+KEY_TRANSACTION_TYPE_ID+
                " !=(SELECT "+KEY_TRANSACTION_TYPE_ID+" FROM "+
                TABLE_TRANSACTION_TYPE+ " WHERE "+KEY_TRANSACTION_TYPE_NAME+" LIKE 'REFUND%'"+" OR "+KEY_TRANSACTION_TYPE_NAME+" LIKE 'BALANCE RETURN%') AND "+TABLE_SUCCESS_TRANSACTION+"."+
                KEY_SERVER_TIMESTAMP+" NOT LIKE '-1%' AND "+TABLE_SUCCESS_TRANSACTION+"."+KEY_SERVER_TIMESTAMP+" NOT LIKE '-2%'";
*/
        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION +
                " WHERE time_stamp LIKE " + "'%" + date1 + "%' AND " + TABLE_SUCCESS_TRANSACTION + "." + KEY_TRANSACTION_TYPE_ID +
                " = '" + trans_type_id + "' AND " + TABLE_SUCCESS_TRANSACTION + "." +
                KEY_SERVER_TIMESTAMP + " NOT LIKE '-1%' AND " + TABLE_SUCCESS_TRANSACTION + "." + KEY_SERVER_TIMESTAMP + " NOT LIKE '-2%'";


        System.out.println("success datewise result " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Success_Transaction successss = new Success_Transaction();
                successss.settrans_id(cursor.getString(0));
                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
                successss.setcard_serial(cursor.getString(5));
                successss.settime_stamp(cursor.getString(6));
                successss.setserver_timestamp(cursor.getString(7));
                // Adding contact to list
                successtransdates.add(successss);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return successtransdates;
    }

    public List<Success_Transaction> getSuccessdatewise(String date1) {
        List<Success_Transaction> successtransdates = new ArrayList<Success_Transaction>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION+" where "+KEY_TIME_STAMP+" like '%"+date1+"%'"+" and "
//                +KEY_SERVER_TIMESTAMP+" !='-1' or "+KEY_SERVER_TIMESTAMP+" !='-2' "+" and "+TABLE_SUCCESS_TRANSACTION+"."+KEY_TRANSACTION_TYPE_ID+
//                " !=(SELECT "+KEY_TRANSACTION_TYPE_ID+" FROM "+TABLE_TRANSACTION_TYPE+" where "+KEY_TRANSACTION_TYPE_NAME+" like "+"'REFUND')";

        String selectQuery = "SELECT  * FROM " + TABLE_SUCCESS_TRANSACTION +
                " WHERE time_stamp LIKE " + "'%" + date1 + "%' AND " + TABLE_SUCCESS_TRANSACTION + "." + KEY_TRANSACTION_TYPE_ID +
                " !=(SELECT " + KEY_TRANSACTION_TYPE_ID + " FROM " +
                TABLE_TRANSACTION_TYPE + " WHERE " + KEY_TRANSACTION_TYPE_NAME + " LIKE 'REFUND%'" + " OR " + KEY_TRANSACTION_TYPE_NAME + " LIKE 'BALANCE RETURN%') AND " + TABLE_SUCCESS_TRANSACTION + "." +
                KEY_SERVER_TIMESTAMP + " NOT LIKE '-1%' AND " + TABLE_SUCCESS_TRANSACTION + "." + KEY_SERVER_TIMESTAMP + " NOT LIKE '-2%'";
//        String selectQuery="SELECT  * FROM "+TABLE_SUCCESS_TRANSACTION+
//                " WHERE time_stamp LIKE "+"'%"+date1+"%' AND "+TABLE_SUCCESS_TRANSACTION+"."+KEY_TRANSACTION_TYPE_ID+
//                " = '"+trans_type_id+"' AND "+TABLE_SUCCESS_TRANSACTION+"."+
//                KEY_SERVER_TIMESTAMP+" NOT LIKE '-1%' AND "+TABLE_SUCCESS_TRANSACTION+"."+KEY_SERVER_TIMESTAMP+" NOT LIKE '-2%'";


        System.out.println("success datewise result " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Success_Transaction successss = new Success_Transaction();
                successss.settrans_id(cursor.getString(0));
                successss.setuser_id(Integer.parseInt(cursor.getString(1)));
                successss.settarans_type_id(Integer.parseInt(cursor.getString(2)));
                successss.setprevious_balnce(Float.parseFloat(cursor.getString(3)));
                successss.setcurrent_balance(Float.parseFloat(cursor.getString(4)));
                successss.setcard_serial(cursor.getString(5));
                successss.settime_stamp(cursor.getString(6));
                successss.setserver_timestamp(cursor.getString(7));
                // Adding contact to list
                successtransdates.add(successss);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return successtransdates;
    }

//    public String userLogin(String uName,String uPass){
//
////        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_USER + " WHERE "
////                        + KEY_USER_NAME + "='" + uName +"'AND "+KEY_PASSWORD+"='"+uPass+"'" ,  null);
////
//        String selectQuery ="SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USER_NAME + "='" + uName +"'AND "+KEY_PASSWORD+"='"+uPass+"'";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor!=null&&cursor.moveToFirst()) {
//
////            String uID= String.valueOf(getUserID(uName));
//
//            String uID=cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
//
//            System.out.println("LOGIN USER ID : "+uID);
//
//            cursor.close();
//            return uID;
//        }
//        cursor.close();
//        return "false";
//
//
//    }

    public String getSinlgeEntry(String userName) {
        String password = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, KEY_USER_NAME + "=?", new String[]{userName}, null, null, null);
        try {
            if (cursor.getCount() < 1) // UserName Not Exist
            {
                cursor.close();
                ////db.close();
                return "NOT EXIST";
            }
            cursor.moveToFirst();
            password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            cursor.close();
            ////db.close();
        } catch (Exception e) {
            cursor.close();
            ////db.close();
            return "exception";
        }
        return password;
    }


    public String getUIDbyName(String itemId) {
        String ItName;

        String selectQuery = "SELECT * FROM " + TABLE_USER + " where " + KEY_USER_NAME + " = " + "'" + itemId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        System.out.println("column UID: " + cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
//
        ItName = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        return ItName;
    }


//    public String getissueRetStatus(String bookId){
//        String issRet="-1";
//
//        String selectQuery = "SELECT * FROM "+TABLE_LIBRARY_BOOK_TRANSACTION+" where "+KEY_BOOK_ID+" = "+"'"+bookId+"'"/*+" AND "+KEY_TRANSACTION_TYPE_ID+" = "+type_id*/;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        System.out.println("Cursor:" +cursor);
//        cursor.moveToFirst();
//        if(cursor != null && cursor.getCount()>0) {
//
//            System.out.println("column: issueReturnStatus " + cursor.getString(cursor.getColumnIndex(KEY_ISSUE_RETURN)));
////
//            issRet = cursor.getString(cursor.getColumnIndex(KEY_ISSUE_RETURN));
//        }else{
//            System.out.println("ChillarApp: cursor exception");
//        }
//        cursor.close();          // Dont forget to close your cursor
//        ////db.close();
//        System.out.println("StatusELD: "+issRet );
//        return issRet;
//    }


    public int getContact() {
        String countQuery = "SELECT  * FROM " + TABLE_STUDENT_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();

        // return count
        return cnt;
    }

    public List<StudentList> getstud_class() {
        List<StudentList> s_class_list = new ArrayList<StudentList>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT " + KEY_STUDENT_CLASS + " FROM " + TABLE_STUDENT_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentList sl = new StudentList();

//               sl.setStudent_class(cursor.getString(0));
                sl.setStudent_class(cursor.getString(0));
                //  sl.setStudent_class(cursor.getString(2));
                //sl.setStudent_division(cursor.getString(3));


                // Adding contact to list
                s_class_list.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return s_class_list;
    }

    public List<StudentList> getstud_div(String s_class) {
        List<StudentList> s_div_list = new ArrayList<StudentList>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT " + KEY_STUDENT_DIVISION + " FROM " + TABLE_STUDENT_LIST + " where " + KEY_STUDENT_CLASS + " = " + "'" + s_class + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentList sl = new StudentList();
                sl.setStudent_division(cursor.getString(0));


                // Adding contact to list
                s_div_list.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return s_div_list;
    }

    public List<StudentList> getstud_cardserial(String s_class, String s_div) {
        List<StudentList> s_card_serial_list = new ArrayList<StudentList>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT " + KEY_STUDENT_CARD_SERIAL + " FROM " + TABLE_STUDENT_LIST + " where " + KEY_STUDENT_CLASS + " = " + "'" + s_class + "'"
                + " AND " + KEY_STUDENT_DIVISION + " = " + "'" + s_div + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentList sl = new StudentList();
                sl.setStudent_card_serial(cursor.getString(0));


                // Adding contact to list
                s_card_serial_list.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return s_card_serial_list;
    }

    public void deleteAllStudentlist() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_STUDENT_LIST);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }

    int addStudentlist(ArrayList<StudentList> stdList) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < stdList.size(); ++i) {

            StudentList student_list = stdList.get(i);

            ContentValues values = new ContentValues();

            values.put(KEY_STUDENT_CARD_SERIAL, student_list.getStudent_card_serial());
            values.put(KEY_STUDENT_CLASS, student_list.getStudent_class()); // User Name
            values.put(KEY_STUDENT_DIVISION, student_list.getStudent_division()); // User Password
            values.put(KEY_STUDENT_NAME, student_list.getStudent_name()); // User Password
            values.put(KEY_USER_ID, student_list.getUser_id()); // User Password

            // Inserting Row
            db.insert(TABLE_STUDENT_LIST, null, values);

        }
        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;


    }


    public List<StudentList> getAllStudents() {
        List<StudentList> liblist = new ArrayList<StudentList>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentList sl = new StudentList();
                sl.setStudent_card_serial(cursor.getString(1));
                sl.setStudent_class(cursor.getString(2));
                sl.setStudent_division(cursor.getString(3));
                sl.setUser_id(cursor.getString(4));
                sl.setStudent_name(cursor.getString(5));


                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }


    public void deleteAllParentdetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_PARENT_DETAILS);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }

    int addparentDetails(ArrayList<Parent> parent) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < parent.size(); ++i) {

            Parent parent1 = parent.get(i);

            ContentValues values = new ContentValues();

            values.put(KEY_USER_ID, parent1.getUser_id());
            values.put(KEY_PARENT_NAME, parent1.getParent_name());
            values.put(KEY_CARD_SERIAL, parent1.getCard_serial()); // User Name


            // Inserting Row
            db.insert(TABLE_PARENT_DETAILS, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;

        //db.close(); // Closing database connection
    }


    int addteacherDetails(ArrayList<Teacher_Details> teacher) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        for (int i = 0; i < teacher.size(); ++i) {

            Teacher_Details teacher_details = teacher.get(i);

            ContentValues values = new ContentValues();

            values.put(KEY_TEACHER_USER_ID, teacher_details.getTeacher_userID());
            values.put(KEY_TEACHER_NAME, teacher_details.getTeacherName());
            values.put(KEY_SCHOOL_ID, teacher_details.getSchoolID());
            values.put(KEY_CARD_SERIAL, teacher_details.getCardSerial()); // User Name


            // Inserting Row
            db.insert(TABLE_TEACHER_DETAILS, null, values);


        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;

        //db.close(); // Closing database connection
    }


    public List<Parent> getAllParents() {
        List<Parent> liblist = new ArrayList<Parent>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PARENT_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Parent sl = new Parent();
                sl.setUser_id(cursor.getString(1));
                sl.setParent_name(cursor.getString(2));
                sl.setCard_serial(cursor.getString(3));


                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }

    public List<String> getAllParentsCardSl() {
        List<String> liblist = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_CARD_SERIAL + " FROM " + TABLE_PARENT_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                if (cursor != null && cursor.getCount() > 0) {

                    String sl = cursor.getString(cursor.getColumnIndex(KEY_CARD_SERIAL));
                    System.out.println("Chillar: Card Serial " + sl);

                    liblist.add(sl);
                } else {
                    System.out.println("Exception in Parent Card list");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }


    public void deleteAllParentStudent() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_STUDENT_PARENT);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }

    int addParentStudent(ArrayList<Parent_Student> parent_student) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < parent_student.size(); ++i) {

            Parent_Student parent_student1 = parent_student.get(i);

            ContentValues values = new ContentValues();

            values.put(KEY_PARENT_USER_ID, parent_student1.getParent_user_id());
            values.put(KEY_STUDENT_USER_ID, parent_student1.getStd_user_id()); // User Name

            // Inserting Row
            db.insert(TABLE_STUDENT_PARENT, null, values);

        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;
    }


    public List<Parent_Student> getAllParentStudent() {
        List<Parent_Student> liblist = new ArrayList<Parent_Student>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT_PARENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Parent_Student sl = new Parent_Student();

                sl.setStd_user_id(cursor.getString(1));
                sl.setParent_user_id(cursor.getString(2));


                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }


    public void deleteAllReasonType() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from " + TABLE_REASON_TYPES);
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }

    int addreasonType(ArrayList<Reason> reason) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < reason.size(); ++i) {

            Reason reason1 = reason.get(i);

            ContentValues values = new ContentValues();

            values.put(KEY_REASON_TYPE_ID, reason1.getReasonId());
            values.put(KEY_REASON_TYPE, reason1.getReasonType()); // User Name

            // Inserting Row
            db.insert(TABLE_REASON_TYPES, null, values);


        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return 0;
    }


    public List<Reason> getAllReason() {
        List<Reason> liblist = new ArrayList<Reason>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_REASON_TYPES;


        System.out.println("ParentSystem: getAllReason: " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Reason sl = new Reason();
                sl.setReasonId(cursor.getString(1));
                sl.setReasonType(cursor.getString(2));


                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor

        return liblist;
    }


    public String getReasonId(String reason) {

        String parentUserID = "";

        String selectQuery = "SELECT " + KEY_REASON_TYPE_ID + " FROM " + TABLE_REASON_TYPES + " where " + KEY_REASON_TYPE + " = " + "'" + reason + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        parentUserID = cursor.getString(cursor.getColumnIndex(KEY_REASON_TYPE_ID));
        cursor.close();          // Dont forget to close your cursor


        return parentUserID;
    }

    public void deleteAllCreateLeave() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CREATE_PARENT_LEAVE);
        ////db.close();
    }

    public long addCreateLeave(Create_Leave create_leave) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PARENT_USER_ID, create_leave.getParent_userId());
        values.put(KEY_STUDENT_USER_ID, create_leave.getStud_userId()); // User Name
        values.put(KEY_REASON_TYPE_ID, create_leave.getReasonTypId()); // User Password
        values.put(KEY_REASON_COMMENT, create_leave.getReasonComment()); // User Password
        values.put(KEY_TIME_STAMP, create_leave.getTime_stamp()); // User Password
        values.put(KEY_SERVER_TIMESTAMP, create_leave.getServertimestamp()); // User Password

        // Inserting Row
        long count = db.insert(TABLE_CREATE_PARENT_LEAVE, null, values);

        //db.close(); // Closing database connection

        return count;

    }


    public List<Create_Leave> getAllCreateLeave() {
        List<Create_Leave> liblist = new ArrayList<Create_Leave>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CREATE_PARENT_LEAVE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Create_Leave sl = new Create_Leave();
                sl.setParent_userId(cursor.getString(2));
                sl.setStud_userId(cursor.getString(1));
                sl.setReasonTypId(cursor.getString(3));
                sl.setReasonComment(cursor.getString(4));
                sl.setTime_stamp(cursor.getString(5));
                sl.setServertimestamp(cursor.getString(6));

                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }


    public void deleteAllStudentClass() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_STUDENT_CLASS_DIV);
        //db.close();
    }

    public void dltAllStudClassbyClsDiv(String classdiv) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        System.out.println("DELETEQUERRY: " + "delete from " + TABLE_STUDENT_CLASS_DIV + " where " + KEY_STUDENT_CLASS_DIV + " = '" + classdiv + "'");
        db.execSQL("delete from " + TABLE_STUDENT_CLASS_DIV + " where " + KEY_STUDENT_CLASS_DIV + " = '" + classdiv + "'");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void addStudentClass(StudentClassDiv studentClassDiv) {
        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("AttendanceSystem:sdsa: " + studentClassDiv.getStud_class() + "  " + studentClassDiv.getStud_div());

        ContentValues values = new ContentValues();

        values.put(KEY_STUDENT_CLASS_NAME, studentClassDiv.getStud_class());
        values.put(KEY_STUDENT_DIV, studentClassDiv.getStud_div()); // User Name
        values.put(KEY_STUDENT_CLASS_DIV, studentClassDiv.getClass_div()); // User Name

        // Inserting Row
        db.insert(TABLE_STUDENT_CLASS_DIV, null, values);

    }


    public List<StudentClassDiv> getAllStudentClass() {
        List<StudentClassDiv> liblist = new ArrayList<StudentClassDiv>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT_CLASS_DIV;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentClassDiv sl = new StudentClassDiv();
                sl.setId(Integer.parseInt(cursor.getString(0)));
                sl.setStud_class(cursor.getString(1));
                sl.setStud_div(cursor.getString(2));
                sl.setClass_div(cursor.getString(3));

                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor

        // return contact list
        return liblist;
    }


    public List<Sales_Item> getAllsalebyId(int itemid, String formatedDate, String trans_type_id) {
        List<Sales_Item> liblist = new ArrayList<Sales_Item>();
        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_SALES_ITEM_LIST+" where "+KEY_ITEM_ID+" = "+itemid;

        String selectQuery = "SELECT * FROM " + TABLE_SALES_ITEM_LIST + " INNER JOIN "
                + TABLE_SUCCESS_TRANSACTION + " ON " + TABLE_SALES_ITEM_LIST + "." + KEY_SALES_TRANS_ID + " LIKE "
                + TABLE_SUCCESS_TRANSACTION + "." + KEY_TRANSACTION_ID + " WHERE " + TABLE_SUCCESS_TRANSACTION + "."
                + KEY_TIME_STAMP + " LIKE " + "'%" + formatedDate + "%' AND " + TABLE_SUCCESS_TRANSACTION + "." + KEY_TRANSACTION_TYPE_ID + " = '" + trans_type_id + "'"
                + " AND (" + TABLE_SALES_ITEM_LIST + "." + KEY_SERVER_TIMESTAMP + " NOT LIKE '-1%' AND "
                + TABLE_SALES_ITEM_LIST + "." + KEY_SERVER_TIMESTAMP + " NOT LIKE '-2%')" + " AND " + KEY_ITEM_ID + " = " + itemid;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales_Item sl = new Sales_Item();
                sl.setSales_item_id(Integer.parseInt(cursor.getString(0)));
                sl.setsales_trans_id(cursor.getString(1));
                sl.setitem_id(cursor.getString(2));
                sl.setitem_quantity(cursor.getString(3));
                sl.setamount(cursor.getString(4));
                sl.setserver_timestamp(cursor.getString(5));

                // Adding contact to list
                liblist.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }


    public List<String> getdistinctItemid(String date, String type) {
        List<String> liblist = new ArrayList<String>();
        // Select All Query

        String selectQuery = "SELECT DISTINCT " + KEY_ITEM_ID + " FROM " + TABLE_SALES_ITEM_LIST + " INNER JOIN "
                + TABLE_SUCCESS_TRANSACTION + " ON " + TABLE_SALES_ITEM_LIST + "." + KEY_SALES_TRANS_ID + " LIKE "
                + TABLE_SUCCESS_TRANSACTION + "." + KEY_TRANSACTION_ID + " WHERE " + TABLE_SUCCESS_TRANSACTION + "."
                + KEY_TIME_STAMP + " LIKE " + "'%" + date + "%' AND " + TABLE_SUCCESS_TRANSACTION + "." + KEY_TRANSACTION_TYPE_ID + " = '" + type + "'"
                + " AND (" + TABLE_SALES_ITEM_LIST + "." + KEY_SERVER_TIMESTAMP + " NOT LIKE '-1%' AND "
                + TABLE_SALES_ITEM_LIST + "." + KEY_SERVER_TIMESTAMP + " NOT LIKE '-2%')";


        System.out.println("CHILLAR: Queery: " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                liblist.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return liblist;
    }


    /////// REFUND ERROR TABLE  ////////

    void addRefundError(Refund_Error refund_error) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

//        values.put(KEY_USER_ID,user.getUser_id());
        values.put(KEY_TRANSACTION_ID, refund_error.getTrans_id()); // User Name
        values.put(KEY_CARD_SERIAL, refund_error.getCardserial()); // User Password
        values.put(KEY_SERVER_TIMESTAMP, refund_error.getServerTimestamp()); //server timestamp

        // Inserting Row
        db.insert(TABLE_REFUND_ERROR, null, values);
        //db.close(); // Closing database connection
    }


    public List<Refund_Error> getAllRefundError() {
        List<Refund_Error> refundErrors = new ArrayList<Refund_Error>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REFUND_ERROR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Refund_Error onlin1 = new Refund_Error();
                onlin1.setId(Integer.parseInt(cursor.getString(0)));
                onlin1.setTrans_id(cursor.getString(1));
                onlin1.setCardserial((cursor.getString(2)));
                onlin1.setServerTimestamp(cursor.getString(3));

                // Adding contact to list
                refundErrors.add(onlin1);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        // return contact list
        return refundErrors;
    }


    public String getParentUserId(String CardSl) {

        String parentUserID = "";

        String selectQuery = "SELECT " + KEY_USER_ID + " FROM " + TABLE_PARENT_DETAILS + " where " + KEY_CARD_SERIAL + " = " + "'" + CardSl + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        System.out.println("columndsasdea: " + cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
//
        parentUserID = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
        cursor.close();          // Dont forget to close your cursor
        ////db.close();

        return parentUserID;
    }


    public List<StudentList> getAllStudsByParent(int p_user_id) {
        List<StudentList> todos = new ArrayList<StudentList>();

        String selectQuery = "SELECT * FROM " + TABLE_STUDENT_LIST + " INNER JOIN "
                + TABLE_STUDENT_PARENT + " ON " + TABLE_STUDENT_LIST + "." + KEY_USER_ID + " = " + TABLE_STUDENT_PARENT + "." + KEY_STUDENT_USER_ID + " WHERE " + TABLE_STUDENT_PARENT + "." + KEY_PARENT_USER_ID + " = " + p_user_id;

        System.out.println("ParentSystem: Querry:: " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentList sl = new StudentList();
                sl.setStudent_card_serial(cursor.getString(1));
                sl.setStudent_class(cursor.getString(2));
                sl.setStudent_division(cursor.getString(3));
                sl.setUser_id(cursor.getString(4));
                sl.setStudent_name(cursor.getString(5));


                // Adding contact to list
                todos.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        return todos;


    }

    public List<Create_Leave> getAllCreateLeaveToUp() {
        List<Create_Leave> paytrans = new ArrayList<Create_Leave>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CREATE_PARENT_LEAVE + " where " + KEY_SERVER_TIMESTAMP + " = ''";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Create_Leave sl = new Create_Leave();
                sl.setId(Integer.parseInt(cursor.getString(0)));
                sl.setParent_userId(cursor.getString(2));
                sl.setStud_userId(cursor.getString(1));
                sl.setReasonTypId(cursor.getString(3));
                sl.setReasonComment(cursor.getString(4));
                sl.setTime_stamp(cursor.getString(5));
                sl.setServertimestamp(cursor.getString(6));

                // Adding contact to list
                paytrans.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();          // Dont forget to close your cursor
        ////db.close();
        // return contact list
        return paytrans;
    }


    public int updateCreateLeave(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_SERVER_TIMESTAMP, getcurrentdate());


        // updating row
        int count = db.update(TABLE_CREATE_PARENT_LEAVE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }


    public String getLastServerupdate() {

        String selectQuery = "SELECT * FROM " + TABLE_SUCCESS_TRANSACTION + " ORDER BY " + KEY_SERVER_TIMESTAMP + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        cursor.moveToFirst();
        System.out.println("columndsasdea: " + cursor.getString(cursor.getColumnIndex(KEY_SERVER_TIMESTAMP)));

        String date = cursor.getString(cursor.getColumnIndex(KEY_SERVER_TIMESTAMP));
        cursor.close();          // Dont forget to close your cursor


        return date;
    }


    public int updateLastBlockCard(String schoomachcode) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        ContentValues values = new ContentValues();

        values.put(KEY_LAST_BLOCKCARDS_UPDATED, getcurrentdate());


        // updating row
        int count = db.update(TABLE_DEVICE_INFO, values, KEY_MACHINE_ID + " = ?",
                new String[]{schoomachcode});

        db.setTransactionSuccessful();
        db.endTransaction();

        return count;
    }



    public String getLastBlockCardsupdate() {

        String schoolID = "";

        try {
            String selectQuery = "SELECT * FROM " + TABLE_DEVICE_INFO;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();

            System.out.println("Chillar dsfsdf");

            if (cursor.isNull(cursor.getColumnIndex(KEY_LAST_BLOCKCARDS_UPDATED))) {

                schoolID = "";
                System.out.println("Chillar Null");
                cursor.close();
            } else {


                schoolID = cursor.getString(cursor.getColumnIndex(KEY_LAST_BLOCKCARDS_UPDATED));

                cursor.close();          // Dont forget to close your cursor
                ////db.close();
            }

        } catch (Exception e) {

//            getSchoolId();
        }
        return schoolID;
    }


    public boolean CheckIsDataAlreadyInDBorNot(String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_ONLINE_TO_RECHARGE + " where " + KEY_ONLINE_TRANSACTION_ID + " = '" + fieldValue + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}