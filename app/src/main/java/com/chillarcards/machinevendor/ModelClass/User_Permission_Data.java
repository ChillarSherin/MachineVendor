package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 09-05-16.
 */
public class User_Permission_Data  {

    int permission_id,transaction_type_id,user_id,permission;
    String server_timestamp;

    public User_Permission_Data(int transaction_type_id, int user_id,int permission, String server_timestamp ) {
//        this.permission_id = permission_id;
        this.transaction_type_id = transaction_type_id;
        this.user_id = user_id;
        this.permission = permission;
        this.server_timestamp = server_timestamp;
    }

    public User_Permission_Data(int permission_id, int transaction_type_id, int user_id, int permission, String server_timestamp) {
        this.permission_id = permission_id;
        this.transaction_type_id = transaction_type_id;
        this.user_id = user_id;
        this.permission = permission;
        this.server_timestamp = server_timestamp;
    }

    public User_Permission_Data(int permission_id, int transaction_type_id, int user_id,int permission ) {
        this.permission = permission;
        this.permission_id = permission_id;
        this.transaction_type_id = transaction_type_id;
        this.user_id = user_id;
    }

    public User_Permission_Data() {

    }


    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(int permission_id) {
        this.permission_id = permission_id;
    }

    public String getServer_timestamp() {
        return server_timestamp;
    }

    public void setServer_timestamp(String server_timestamp) {
        this.server_timestamp = server_timestamp;
    }

    public int getTransaction_type_id() {
        return transaction_type_id;
    }

    public void setTransaction_type_id(int transaction_type_id) {
        this.transaction_type_id = transaction_type_id;
    }

    public int getuser_id() {
        return user_id;

    }

    public void setuser_id(int user_id) {
        this.user_id = user_id;
    }
}
