package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by test on 02-07-2016.
 */

public class Library_book_transaction extends Success_Transaction{
    int  id;
    String transaction_id;
    String issue_return;
    String book_id;
    String issue_time;
    String server_timestamp;

    public Library_book_transaction(String  trans_id, int tarans_type_id, int user_id, Float previous_balnce, Float current_balance, String card_serial, String time_stamp, String server_timestamp, String transaction_id, String issue_return, String book_id, String issue_time, String server_timestamp1) {
        super( trans_id, tarans_type_id, user_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp);
        this.transaction_id = transaction_id;
        this.issue_return = issue_return;
        this.book_id = book_id;
        this.issue_time = issue_time;
        this.server_timestamp = server_timestamp1;
    }

    public Library_book_transaction(int id, String transaction_id, String issue_return, String book_id, String issue_time, String server_timestamp) {
        this.id = id;
        this.transaction_id = transaction_id;
        this.issue_return = issue_return;
        this.book_id = book_id;
        this.issue_time=issue_time;
        this.server_timestamp = server_timestamp;
    }




    public Library_book_transaction() {

    }


    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String gettransaction_id() {
        return transaction_id;
    }

    public void settransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }


    public String getissue_return() {
        return issue_return;
    }

    public void setissue_return(String issue_return) {
        this.issue_return = issue_return;
    }


    public String getbook_id() {
        return book_id;
    }

    public void setbook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getissue_time() {
        return issue_time;
    }

    public void setissue_time(String issue_time) {
        this.issue_time = issue_time;
    }

    public String getserver_timestamp() {
        return server_timestamp;
    }

    public void setserver_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }


}