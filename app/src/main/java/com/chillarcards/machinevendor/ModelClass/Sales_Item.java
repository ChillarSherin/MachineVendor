package com.chillarcards.machinevendor.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by test on 02-07-2016.
 */

public class Sales_Item implements Parcelable{

    int sales_item_id;
    String sales_trans_id;
    String item_id;
    String item_quantity;
    String amount;
    String server_timestamp;

    public Sales_Item(int sales_item_id, String sales_trans_id, String item_id, String item_quantity, String amount, String server_timestamp) {
        this.sales_item_id = sales_item_id;
        this.sales_trans_id = sales_trans_id;
        this.item_id = item_id;
        this.item_quantity = item_quantity;
        this.amount = amount;
        this.server_timestamp = server_timestamp;
    }

    public Sales_Item(String sales_trans_id,String item_id,String item_quantity, String amount,  String server_timestamp) {
        this.amount = amount;
        this.item_quantity = item_quantity;
        this.item_id = item_id;
        this.sales_trans_id = sales_trans_id;
        this.server_timestamp = server_timestamp;
    }


//    public Sales_Item( String trans_id, int tarans_type_id, int user_id, int previous_balnce, int current_balance,
//                       int card_serial, String time_stamp, String server_timestamp, int sale_trans_id, String transaction_id,
//                       String bill_no, String tot_amount, String server_timestamp1, int sales_item_id,
//                       String item_id, String item_quantity, String amount, String server_timestamp2) {
//        super( trans_id, tarans_type_id, user_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp, sale_trans_id, transaction_id, bill_no, tot_amount, server_timestamp1);
//        this.sales_item_id = sales_item_id;
//        this.sales_trans_id = super.sale_trans_id;
//        this.item_id = item_id;
//        this.item_quantity = item_quantity;
//        this.amount = amount;
//        this.server_timestamp = server_timestamp2;
//    }

    public Sales_Item() {

    }

    public Sales_Item(String sales_trans_id, String item_id, String item_quantity, String amount) {
        this.sales_trans_id = sales_trans_id;
        this.item_id = item_id;
        this.item_quantity = item_quantity;
        this.amount = amount;
    }

    protected Sales_Item(Parcel in) {
        sales_item_id = in.readInt();
        sales_trans_id = in.readString();
        item_id = in.readString();
        item_quantity = in.readString();
        amount = in.readString();
        server_timestamp = in.readString();
    }

    public static final Creator<Sales_Item> CREATOR = new Creator<Sales_Item>() {
        @Override
        public Sales_Item createFromParcel(Parcel in) {
            return new Sales_Item(in);
        }

        @Override
        public Sales_Item[] newArray(int size) {
            return new Sales_Item[size];
        }
    };

    public int getSales_item_id() {
        return sales_item_id;
    }

    public void setSales_item_id(int sales_item_id) {
        this.sales_item_id = sales_item_id;
    }

    public String getsales_trans_id() {
        return sales_trans_id;
    }

    public void setsales_trans_id(String sales_trans_id) {
        this.sales_trans_id = sales_trans_id;
    }

    public String getitem_id() {
        return item_id;
    }

    public void setitem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getitem_quantity() {
        return item_quantity;
    }

    public void setitem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getamount() {
        return amount;
    }

    public void setamount(String amount) {
        this.amount = amount;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sales_item_id);
        dest.writeString(sales_trans_id);
        dest.writeString(item_id);
        dest.writeString(item_quantity);
        dest.writeString(amount);
        dest.writeString(server_timestamp);
    }
}