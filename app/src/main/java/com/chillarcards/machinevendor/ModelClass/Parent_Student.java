package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 11-03-17.
 */

public class Parent_Student {

    String std_user_id,parent_user_id;

    public Parent_Student() {
    }

    public Parent_Student(String std_user_id, String parent_user_id) {
        this.std_user_id = std_user_id;
        this.parent_user_id = parent_user_id;
    }

    public String getStd_user_id() {
        return std_user_id;
    }

    public void setStd_user_id(String std_user_id) {
        this.std_user_id = std_user_id;
    }

    public String getParent_user_id() {
        return parent_user_id;
    }

    public void setParent_user_id(String parent_user_id) {
        this.parent_user_id = parent_user_id;
    }
}
