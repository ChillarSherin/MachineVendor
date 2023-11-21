package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 02-07-2016.
 */

public class CategoryList {

    int category_id, item_type_id;
    String category_name;
    String category_shortname;
    String server_timestamp;


    public CategoryList(int category_id, String category_name, String category_shortname, String server_timestamp) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_shortname = category_shortname;
        this.server_timestamp = server_timestamp;

    }

    public CategoryList(int category_id, int type_id, String category_name, String category_shortname) {
        this.item_type_id = type_id;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_shortname = category_shortname;
    }

    public CategoryList(int category_id, int item_type_id, String category_name, String category_shortname, String server_timestamp) {
        this.server_timestamp = server_timestamp;
        this.item_type_id = item_type_id;
        this.category_shortname = category_shortname;
        this.category_name = category_name;
        this.category_id = category_id;
    }

    public CategoryList() {

    }

    public int getItem_type_id() {
        return item_type_id;
    }

    public void setItem_type_id(int item_type_id) {
        this.item_type_id = item_type_id;
    }

    public int getcategory_id() {
        return category_id;
    }

    public void setcategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getcategory_name() {
        return category_name;
    }

    public void setcategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getcategory_shortname() {
        return category_shortname;
    }

    public void setcategory_shortname(String category_shortname) {
        this.category_shortname = category_shortname;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }


}
