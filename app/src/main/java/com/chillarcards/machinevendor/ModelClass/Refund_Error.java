package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 09-03-17.
 */

public class Refund_Error {

    String trans_id;
    int id;
    String cardserial;
    String serverTimestamp;

    public Refund_Error(String trans_id,String cardserial, String serverTimestamp) {
        this.cardserial = cardserial;
        this.serverTimestamp = serverTimestamp;
        this.trans_id = trans_id;
    }

    public Refund_Error() {
    }

    public String getCardserial() {
        return cardserial;
    }

    public void setCardserial(String cardserial) {
        this.cardserial = cardserial;
    }

    public String getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(String serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }
}
