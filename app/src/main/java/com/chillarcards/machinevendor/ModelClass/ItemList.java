package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 01-07-16.
 */
public class ItemList {

    int item_id,category_id,item_type_id;
    String item_code,item_name,item_shortname,server_timestamp,stock_status;
    Float price;

    public ItemList() {
    }

    public ItemList(String item_code,String item_name,String item_shortname,int category_id,float price,String stock_status,int item_type_id,String server_timestamp) {

        this.category_id = category_id;
        this.item_code = item_code;
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_shortname = item_shortname;
        this.price = price;
        this.stock_status=stock_status;
        this.item_type_id=item_type_id;
        this.server_timestamp = server_timestamp;
    }

    
    public ItemList(int item_id, String item_code, String item_name, String item_shortname, int category_id, Float price, String stock_status, int item_type_id, String server_timestamp) {
        this.item_id = item_id;
        this.category_id = category_id;
        this.item_type_id = item_type_id;
        this.item_code = item_code;
        this.item_name = item_name;
        this.item_shortname = item_shortname;
        this.server_timestamp = server_timestamp;
        this.stock_status = stock_status;
        this.price = price;
    }

    public ItemList( int item_id,String item_code, String item_name, String item_shortname,int category_id, Float price, String stock_status,int item_type_id) {
        this.category_id = category_id;
        this.item_code = item_code;
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_shortname = item_shortname;
        this.item_type_id = item_type_id;
        this.price = price;
        this.stock_status = stock_status;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_shortname() {
        return item_shortname;
    }

    public void setItem_shortname(String item_shortname) {
        this.item_shortname = item_shortname;
    }

    public Float getprice() {
        return price;
    }

    public void setprice(Float price) {
        this.price = price;
    }

    public String getstock_status() {
        return stock_status;
    }

    public void setstock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public int getitem_type_id() {
        return item_type_id;
    }

    public void setitem_type_id(int item_type_id) {
        this.item_type_id = item_type_id;
    }



    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }
}
