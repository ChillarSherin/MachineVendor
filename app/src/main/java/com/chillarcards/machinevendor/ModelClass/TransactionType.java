package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 10-05-16.
 */
public class TransactionType {
    int trans_type_id;
    String transaction_type_name;
    String server_timestamp;

    public TransactionType(int trans_type_id, String transaction_type_name, String server_timestamp) {
        this.trans_type_id = trans_type_id;
        this.transaction_type_name = transaction_type_name;
        this.server_timestamp = server_timestamp;
    }


    public TransactionType(int trans_type_id, String transaction_type_name) {
        this.trans_type_id = trans_type_id;
        this.transaction_type_name = transaction_type_name;

    }

    public TransactionType() {

    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    public int getTrans_type_id() {
        return trans_type_id;
    }

    public void setTrans_type_id(int trans_type_id) {
        this.trans_type_id = trans_type_id;
    }

    public String getTransaction_type_name() {
        return transaction_type_name;
    }

    public void setTransaction_type_name(String transaction_type_name) {
        this.transaction_type_name = transaction_type_name;
    }
}
