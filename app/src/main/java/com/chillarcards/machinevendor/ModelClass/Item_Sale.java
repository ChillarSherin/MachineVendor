package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 01-07-2016.
 */

public class Item_Sale extends Success_Transaction{


    String sale_trans_id;
    String transaction_id;
    String bill_no;
    String tot_amount;
    String server_timestamp;

    public Item_Sale( String trans_id, int user_id, int tarans_type_id, Float previous_balnce, Float current_balance, String card_serial, String time_stamp, String server_timestamp, String transaction_id, String bill_no, String tot_amount, String server_timestamp1) {
        super( trans_id, tarans_type_id, user_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp);
        this.transaction_id = transaction_id;
        this.bill_no = bill_no;
        this.tot_amount = tot_amount;
        this.server_timestamp = server_timestamp1;
    }

    public Item_Sale(String sale_trans_id, String transaction_id, String bill_no, String tot_amount, String server_timestamp) {
        this.sale_trans_id = sale_trans_id;
        this.transaction_id = transaction_id;
        this.bill_no = bill_no;
        this.tot_amount = tot_amount;
        this.server_timestamp=server_timestamp;
    }


    public Item_Sale( String transaction_id, String bill_no, String tot_amount, String server_timestamp) {
        this.transaction_id = transaction_id;
        this.bill_no = bill_no;
        this.tot_amount = tot_amount;
        this.server_timestamp=server_timestamp;
    }

    public Item_Sale() {

    }


    public String getsale_trans_id() {
        return sale_trans_id;
    }

    public void setsale_trans_id(String sale_trans_id) {
        this.sale_trans_id = sale_trans_id;
    }

    public String gettransaction_id() {
        return transaction_id;
    }

    public void settransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getbill_no() {
        return bill_no;
    }

    public void setbill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String gettot_amount() {
        return tot_amount;
    }

    public void settot_amount(String tot_amount) {
        this.tot_amount = tot_amount;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }





}