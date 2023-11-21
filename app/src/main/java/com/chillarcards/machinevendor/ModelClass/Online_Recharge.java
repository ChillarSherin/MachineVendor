package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 22-10-16.
 */
public class Online_Recharge {

    String transID;
    String OnlineTransID;
    String serverTimestamp;
    int id;

    public Online_Recharge(String transID, String onlineTransID, String serverTimestamp) {
        this.OnlineTransID = onlineTransID;
        this.serverTimestamp = serverTimestamp;
        this.transID = transID;
    }

    public Online_Recharge() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOnlineTransID() {
        return OnlineTransID;
    }

    public void setOnlineTransID(String onlineTransID) {
        OnlineTransID = onlineTransID;
    }

    public String getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(String serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }
}
