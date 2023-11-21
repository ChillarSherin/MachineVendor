package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 01-07-2016.
 */

public class Recharge_Data extends Success_Transaction{

    int recharge_id;
    String trans_id;
    String recharge_time;
    Float rech_amt;
    int payment_type_id;
    String server_timestamp;

    public Recharge_Data(String trans_id, int tarans_type_id, int user_id, Float previous_balnce, Float current_balance,
                         String card_serial, String time_stamp, String server_timestamp,
                         String transaction_id, String recharge_time,Float rech_amt, int payment_type_id,
                         String server_timestamp1) {
        super( trans_id, tarans_type_id, user_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp);
        this.trans_id = transaction_id;
        this.recharge_time = recharge_time;
        this.payment_type_id = payment_type_id;
        this.rech_amt=rech_amt;
        this.server_timestamp = server_timestamp1;
    }

    public Float getRech_amt() {
        return rech_amt;
    }

    public void setRech_amt(Float rech_amt) {
        this.rech_amt = rech_amt;
    }

    public Recharge_Data(int recharge_id, String trans_id,  String recharge_time,Float rech_amt, int payment_type_id, String server_timestamp) {
        this.recharge_id = recharge_id;
        this.trans_id = trans_id;
        this.rech_amt=rech_amt;
        this.recharge_time = recharge_time;
        this.payment_type_id = payment_type_id;
        this.server_timestamp=server_timestamp;
    }




    public Recharge_Data() {

    }


    public int getrecharge_id() {
        return recharge_id;
    }

    public void setrecharge_id(int recharge_id) {
        this.recharge_id = recharge_id;
    }

    public String gettransaction_id() {
        return trans_id;
    }

    public void settransaction_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getrecharge_time() {
        return recharge_time;
    }

    public void setrecharge_time(String recharge_time) {
        this.recharge_time = recharge_time;
    }

    public int getpayment_type_id() {
        return payment_type_id;
    }

    public void setpayment_type_id(int payment_type_id) {
        this.payment_type_id = payment_type_id;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }



}
