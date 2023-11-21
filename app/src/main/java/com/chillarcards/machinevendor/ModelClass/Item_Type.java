package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 02-07-2016.
 */

public class Item_Type {

    int  item_type_id;

    String server_timestamp,item_type_name;

    public Item_Type(int item_type_id, String item_type_name, String server_timestamp) {
        this.item_type_id = item_type_id;
        this.item_type_name = item_type_name;
        this.server_timestamp = server_timestamp;

    }


    public Item_Type(int item_type_id, String item_type_name) {
        this.item_type_id = item_type_id;
        this.item_type_name = item_type_name;
    }

    public Item_Type() {


    }


    public int getitem_type_id() {
        return item_type_id;
    }

    public void setitem_type_id(int item_type_id) {
        this.item_type_id = item_type_id;
    }

    public String getitem_type_name() {
        return item_type_name;
    }

    public void setitem_type_name(String item_type_name) {
        this.item_type_name = item_type_name;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

}