package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 11-03-17.
 */

public class Reason {

    String reasonId,reasonType;

    public Reason(String reasonId, String reasonType) {
        this.reasonId = reasonId;
        this.reasonType = reasonType;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public Reason() {
    }
}
