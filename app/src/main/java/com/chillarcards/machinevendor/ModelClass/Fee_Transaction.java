package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 01-07-2016.
 */

public class Fee_Transaction extends Success_Transaction{


    int fee_tran_id;
    String bill_no;
    Float tot_am;
    String paysource;
    String trans_id1;
    String server_timestamp;

    public Fee_Transaction(String  trans_id, int tarans_type_id, int user_id, Float previous_balnce, Float current_balance, String card_serial, String time_stamp, String server_timestamp, String bill_no, Float tot_am, String trans_id1, String server_timestamp1) {
        super( trans_id, tarans_type_id, user_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp);
        this.bill_no = bill_no;
        this.tot_am = tot_am;
        this.trans_id1 = trans_id1;
        this.server_timestamp = server_timestamp1;
    }

    public Fee_Transaction(String trans_id, String paysource, String bill_no, Float tot_am, String server_timestamp) {
        this.bill_no = bill_no;
        this.tot_am = tot_am;
        this.trans_id = trans_id;
        this.server_timestamp=server_timestamp;
    }

    public Fee_Transaction(int fee_tran_id,String trans_id, String bill_no, Float tot_am, String server_timestamp) {
        this.fee_tran_id=fee_tran_id;
        this.bill_no = bill_no;
        this.tot_am = tot_am;
        this.trans_id = trans_id;
        this.server_timestamp=server_timestamp;
    }




    public Fee_Transaction() {

    }




    public int getfee_tran_id() {
        return fee_tran_id;
    }

    public void setfee_tran_id(int id) {
        this.fee_tran_id = id;
    }

    public String getbill_no() {
        return bill_no;
    }

    public void setbill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public Float gettot_am() {
        return tot_am;
    }

    public void settot_am(Float tot_am) {
        this.tot_am = tot_am;
    }

    public String gettrans_id() {
        return trans_id;
    }

    public void settrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getpaysource() {
        return paysource;
    }

    public void setpaysource(String paysource) {
        this.paysource = paysource;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }



}
