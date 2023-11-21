package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 01-07-16.
 */
public class ItemStock {


    int stock_id,item_id,item_stock;
    String server_timestamp,reorder_warning;

    public ItemStock() {
    }

    public ItemStock(int stock_id, String reorder_warning, int item_stock) {
        this.stock_id = stock_id;
        this.reorder_warning = reorder_warning;
        this.item_stock = item_stock;
    }

    public ItemStock( int stock_id,int item_id,int item_stock, String reorder_warning, String server_timestamp) {
        this.item_id = item_id;
        this.item_stock = item_stock;
        this.reorder_warning = reorder_warning;
        this.server_timestamp = server_timestamp;
        this.stock_id = stock_id;
    }

    public ItemStock(int item_id, int stock_id ,int item_stock, String reorder_warning) {
        this.item_id = item_id;
        this.item_stock = item_stock;
        this.reorder_warning = reorder_warning;
        this.stock_id = stock_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getItem_stock() {
        return item_stock;
    }

    public String getReorder_warning() {
        return reorder_warning;
    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setItem_stock(int item_stock) {
        this.item_stock = item_stock;
    }

    public void setReorder_warning(String reorder_warning) {
        this.reorder_warning = reorder_warning;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }
}
