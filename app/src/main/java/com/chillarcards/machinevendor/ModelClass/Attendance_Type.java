package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 09-05-16.
 */
public class Attendance_Type {
    int id;
    String attendance_type_name,server_timestamp;

    public Attendance_Type(int id, String attendance_type_name, String server_timestamp) {
       this.id = id;
        this.attendance_type_name = attendance_type_name;
        this.server_timestamp = server_timestamp;
    }

    public Attendance_Type(int id, String attendance_type_name) {
        this.id = id;
        this.attendance_type_name = attendance_type_name;
    }

    public Attendance_Type() {

    }

    public String getAttendance_type_name() {
        return attendance_type_name;
    }

    public void setAttendance_type_name(String attendance_type_name) {
        this.attendance_type_name = attendance_type_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }
}
