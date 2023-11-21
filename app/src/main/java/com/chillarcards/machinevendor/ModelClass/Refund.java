package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 23-08-16.
 */
public class Refund extends Success_Transaction {
    int id;
    String trans_id;
    String orig_trans_id;
    Float amount;
    String server_timestamp;

    public Refund(String trans_id, int user_id, int tarans_type_id, Float previous_balnce, Float current_balance, String card_serial, String time_stamp, String server_timestamp, String trans_id1, String orig_trans_id, Float amount, String server_timestamp1) {
        super(trans_id, user_id, tarans_type_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp);
        this.amount = amount;
        this.orig_trans_id = orig_trans_id;
        this.server_timestamp = server_timestamp1;
        this.trans_id = trans_id1;
    }

    public Refund(String orig_trans_id, String server_timestamp, String trans_id) {
        this.orig_trans_id = orig_trans_id;
        this.server_timestamp = server_timestamp;
        this.trans_id = trans_id;
    }

    public Refund() {
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrig_trans_id() {
        return orig_trans_id;
    }

    public void setOrig_trans_id(String orig_trans_id) {
        this.orig_trans_id = orig_trans_id;
    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }
}
