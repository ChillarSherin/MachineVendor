package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by chillar on 03-Jun-17.
 */

public class Teacher_Details {

    String schoolID,cardSerial,teacher_userID,teacherName;

    public Teacher_Details() {
    }

    public Teacher_Details(String schoolID, String cardSerial, String teacher_userID, String teacherName) {
        this.schoolID=schoolID;
        this.cardSerial=cardSerial;
        this.teacher_userID=teacher_userID;
        this.teacherName=teacherName;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public String getCardSerial() {
        return cardSerial;
    }

    public String getTeacher_userID() {
        return teacher_userID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public void setCardSerial(String cardSerial) {
        this.cardSerial = cardSerial;
    }

    public void setTeacher_userID(String teacher_userID) {
        this.teacher_userID = teacher_userID;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
