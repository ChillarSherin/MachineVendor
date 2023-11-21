package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 11-03-17.
 */

public class Create_Leave {
    int id;

    String stud_userId,parent_userId,reasonTypId,reasonComment,time_stamp,servertimestamp;

    public Create_Leave(String stud_userId, String parent_userId, String reasonTypId, String reasonComment, String time_stamp, String servertimestamp) {
        this.stud_userId = stud_userId;
        this.parent_userId = parent_userId;
        this.reasonTypId = reasonTypId;
        this.reasonComment = reasonComment;
        this.time_stamp = time_stamp;
        this.servertimestamp = servertimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Create_Leave() {
    }

    public String getStud_userId() {
        return stud_userId;
    }

    public void setStud_userId(String stud_userId) {
        this.stud_userId = stud_userId;
    }

    public String getParent_userId() {
        return parent_userId;
    }

    public void setParent_userId(String parent_userId) {
        this.parent_userId = parent_userId;
    }

    public String getReasonTypId() {
        return reasonTypId;
    }

    public void setReasonTypId(String reasonTypId) {
        this.reasonTypId = reasonTypId;
    }

    public String getReasonComment() {
        return reasonComment;
    }

    public void setReasonComment(String reasonComment) {
        this.reasonComment = reasonComment;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getServertimestamp() {
        return servertimestamp;
    }

    public void setServertimestamp(String servertimestamp) {
        this.servertimestamp = servertimestamp;
    }
}
