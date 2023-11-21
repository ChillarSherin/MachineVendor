package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 09-05-16.
 */
public class Device_Info {
    int id;
    String machine_id;
    String school_id;
    String serial_no;
    String schoolname;
    String schoolplace;
    String lasttransid;

    public String getLastblockupdated() {
        return lastblockupdated;
    }

    public void setLastblockupdated(String lastblockupdated) {
        this.lastblockupdated = lastblockupdated;
    }

    String lastblockupdated;

    public String getLasttransid() {
        return lasttransid;
    }

    public void setLasttransid(String lasttransid) {
        this.lasttransid = lasttransid;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getSchoolplace() {
        return schoolplace;
    }

    public void setSchoolplace(String schoolplace) {
        this.schoolplace = schoolplace;
    }

    String main_server_url;
    String main_upload_path;


    public Device_Info(int id, String machine_id, String school_id,String schoolname,String schoolplace,String lasttransid, String serial_no, String main_server_url, String main_upload_path) {
        this.id = id;
        this.machine_id = machine_id;
        this.schoolname=schoolname;
        this.schoolplace=schoolplace;
        this.lasttransid=lasttransid;
        this.school_id = school_id;
        this.serial_no = serial_no;
        this.main_server_url = main_server_url;
        this.main_upload_path = main_upload_path;

    }

    public Device_Info(String machine_id, String serial_no, String school_id,String schoolname,String schoolplace,String lasttransid,String main_server_url, String main_upload_path,String lastblockupdated) {
        this.machine_id = machine_id;
        this.schoolname=schoolname;
        this.schoolplace=schoolplace;
        this.lasttransid=lasttransid;
        this.main_server_url = main_server_url;
        this.main_upload_path = main_upload_path;
        this.school_id = school_id;
        this.serial_no = serial_no;
        this.lastblockupdated = lastblockupdated;
    }

    public Device_Info(int id,String machine_id, String serial_no, String school_id) {
        this.id = id;
        this.machine_id = machine_id;
        this.serial_no = serial_no;
        this.school_id = school_id;
    }

    public Device_Info() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getMain_server_url() {
        return main_server_url;
    }

    public void setMain_server_url(String main_server_url) {
        this.main_server_url = main_server_url;
    }

    public String getMain_upload_path() {
        return main_upload_path;
    }

    public void setMain_upload_path(String main_upload_path) {
        this.main_upload_path = main_upload_path;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }
}
