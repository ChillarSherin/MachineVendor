package com.chillarcards.machinevendor.ModelClass;

public class User {


    int user_id;
    String user_name,password,server_timestamp;

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;

    }

    public User(int user_id, String user_name,String password, String server_timestamp) {
        this.password = password;
        this.server_timestamp = server_timestamp;
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public User(int user_id, String user_name, String password) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.password = password;
    }

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
