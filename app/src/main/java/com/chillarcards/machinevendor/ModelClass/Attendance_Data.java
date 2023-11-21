package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 09-05-16.
 */
public class Attendance_Data extends Success_Transaction {
    int attendance_data_id,attendance_type_id,in_out;
    String transaction_id,server_timestamp;


    public Attendance_Data(String trans_id, int tarans_type_id, int user_id, Float previous_balnce, Float current_balance, String card_serial,
                           String time_stamp, String server_timestamp, int attendance_type_id,
                           int in_out, String transaction_id, String server_timestamp1) {
        super(trans_id, tarans_type_id, user_id, previous_balnce, current_balance, card_serial, time_stamp, server_timestamp);
        this.attendance_type_id = attendance_type_id;
        this.transaction_id=transaction_id;
        this.in_out = in_out;
        this.transaction_id = transaction_id;
        this.server_timestamp = server_timestamp1;
    }

    public Attendance_Data(int attendance_data_id, String trans_id, int in_out, int attendance_type_id , String server_timestamp) {
        this.attendance_data_id = attendance_data_id;
        this.attendance_type_id = attendance_type_id;
        this.in_out = in_out;
        this.transaction_id = trans_id;
        this.server_timestamp = server_timestamp;
    }

    public Attendance_Data() {

    }

    public Attendance_Data(String trans_id, int userid, int transtype_id, Float previousbalnce, float currentbalance, String resultCardSerial, String currentDateTimeString, String server_timestamp, int i, String transaction_id, String server_timestamp1) {

        super(trans_id,  userid , transtype_id, previousbalnce, currentbalance, resultCardSerial, currentDateTimeString, server_timestamp);
        this.attendance_type_id = transtype_id;
        this.transaction_id=transaction_id;
        this.in_out = i;
        this.transaction_id = transaction_id;
        this.server_timestamp = server_timestamp1;
    }


    public int getAttendance_data_id() {
        return attendance_data_id;
    }

    public void setAttendance_data_id(int attendance_data_id) {
        this.attendance_data_id = attendance_data_id;
    }

    public int getAttendance_type_id() {
        return attendance_type_id;
    }

    public void setAttendance_type_id(int attendance_type_id) {
        this.attendance_type_id = attendance_type_id;
    }

    public int getIn_out() {
        return in_out;
    }

    public void setIn_out(int in_out) {
        this.in_out = in_out;
    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
