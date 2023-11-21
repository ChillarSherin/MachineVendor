package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 01-07-2016.
 */

public class Payment_Type {
    int  id;
    String payment_type_name;
    String server_timestamp;

    public Payment_Type(int id, String payment_type_name, String server_timestamp) {
        this.id = id;
        this.payment_type_name = payment_type_name;
        this.server_timestamp = server_timestamp;

    }


    public Payment_Type(String id, String payment_type_name) {
        this.id= Integer.parseInt(id);
        this.payment_type_name = payment_type_name;
        this.server_timestamp = server_timestamp;
    }





    public Payment_Type() {

    }


    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getpayment_type_name() {
        return payment_type_name;
    }

    public void setpayment_type_name(String payment_type_name) {
        this.payment_type_name = payment_type_name;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }


}