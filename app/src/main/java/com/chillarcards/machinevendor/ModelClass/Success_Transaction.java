package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 01-07-2016.
 */

public class Success_Transaction {



    String trans_id;
    int tarans_type_id;
    int user_id;
    Float previous_balnce;
    Float current_balance;
    String card_serial;
    String time_stamp;
    String server_timestamp;

    public Success_Transaction(String trans_id, int user_id, int tarans_type_id, Float previous_balnce,
                               Float current_balance, String card_serial, String time_stamp, String server_timestamp) {

        this.trans_id = trans_id;
        this.tarans_type_id = tarans_type_id;
        this.user_id = user_id;
        this.previous_balnce = previous_balnce;
        this.current_balance = current_balance;
        this.card_serial = card_serial;
        this.time_stamp = time_stamp;
        this.server_timestamp = server_timestamp;
    }

    public Success_Transaction() {

    }


    public String gettrans_id() {
        return trans_id;
    }

    public void settrans_id(String trnsid) {
        this.trans_id = trnsid;
    }

    public int gettranstypeid() {
        return tarans_type_id;
    }

    public void settarans_type_id(int tarans_type_id) {
        this.tarans_type_id = tarans_type_id;
    }

    public int getuser_id() {
        return user_id;
    }

    public void setuser_id(int user_id) {
        this.user_id = user_id;
    }

    public Float getprevious_balnce() {
        return previous_balnce;
    }

    public void setprevious_balnce(Float previous_balnce) {
        this.previous_balnce = previous_balnce;
    }

    public Float getcurrent_balance() {
        return current_balance;
    }

    public void setcurrent_balance(Float current_balance) {
        this.current_balance = current_balance;
    }


    public String getcard_serial() {
        return card_serial;
    }

    public void setcard_serial(String card_serial) {
        this.card_serial = card_serial;
    }

    public String gettime_stamp() {
        return time_stamp;
    }

    public void settime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }


}