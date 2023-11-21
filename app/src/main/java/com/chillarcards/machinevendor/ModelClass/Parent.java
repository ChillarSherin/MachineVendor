package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 11-03-17.
 */

public class Parent {

    String user_id,card_serial,Parent_name;

    public Parent() {
    }

    public Parent(String user_id, String parent_name, String card_serial) {
        this.user_id = user_id;
        this.card_serial = card_serial;
        this.Parent_name=parent_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_serial() {
        return card_serial;
    }

    public void setCard_serial(String card_serial) {
        this.card_serial = card_serial;
    }

    public String getParent_name() {
        return Parent_name;
    }

    public void setParent_name(String parent_name) {
        Parent_name = parent_name;
    }
}
