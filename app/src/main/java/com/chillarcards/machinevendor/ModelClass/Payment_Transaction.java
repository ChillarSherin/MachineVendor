package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 01-07-2016.
 */

public class Payment_Transaction extends Success_Transaction {


    String billno;
    int id;
    String trans_id;
    Float amount;
    String server_timestamp;


    public Payment_Transaction(String trans_id, int tarans_type_id, int user_id, Float previous_balnce, Float current_balance,
                               String card_serial, String time_stamp, String server_timestamp, String billno, String trans_id1,
                               Float amount, String server_timestamp1) {
        super(trans_id, tarans_type_id, user_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp);
        //this.id = id1;
        this.billno = billno;
        this.trans_id = trans_id1;
        this.amount = amount;
        this.server_timestamp = server_timestamp1;


    }

    public Payment_Transaction(int id, String billno, String trans_id, Float amount, String server_timestamp) {

        this.id = id;
        this.billno = billno;
        this.trans_id = trans_id;
        this.amount = amount;
        this.server_timestamp = server_timestamp;
    }

    public Payment_Transaction() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String gettrans_id() {
        return trans_id;
    }

    public void settrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public Float getamount() {
        return amount;
    }

    public void setamount(Float amount) {
        this.amount = amount;
    }

    public String getbillno() {
        return billno;
    }

    public void setbillno(String billno) {
        this.billno = billno;
    }


    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }


}