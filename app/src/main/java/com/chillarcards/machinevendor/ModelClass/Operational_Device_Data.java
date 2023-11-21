package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 09-05-16.
 */
public class Operational_Device_Data {

    int id;
    String last_trans_id;
    String machine_id;
    String last_machine_trans_time;
    String last_server_trans_time;
    String server_timestamp;

    public Operational_Device_Data(int i, String string, String string1, String string2, String string3, String string4) {
    }

    public Operational_Device_Data() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast_machine_trans_time() {
        return last_machine_trans_time;
    }

    public void setLast_machine_trans_time(String last_machine_trans_time) {
        this.last_machine_trans_time = last_machine_trans_time;
    }

    public String getLast_server_trans_time() {
        return last_server_trans_time;
    }

    public void setLast_server_trans_time(String last_server_trans_time) {
        this.last_server_trans_time = last_server_trans_time;
    }

    public String getLast_trans_id() {
        return last_trans_id;
    }

    public void setLast_trans_id(String last_trans_id) {
        this.last_trans_id = last_trans_id;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }
}
