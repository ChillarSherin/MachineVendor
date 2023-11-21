package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Ijaz on 22-11-2016.
 */

public class StudentList {

    String student_card_serial;
    String student_class;
    String student_division;
    String student_name;
    String user_id;

    public StudentList() {

    }

    public StudentList(String cardSerial, String user_id, String student_name, String standardName, String standardDivisionName) {
        this.student_card_serial = cardSerial;
        this.student_class = standardName;
        this.student_division = standardDivisionName;
        this.student_name = student_name;
        this.user_id = user_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStudent_card_serial() {
        return student_card_serial;
    }

    public void setStudent_card_serial(String student_card_serial) {
        this.student_card_serial = student_card_serial;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getStudent_division() {
        return student_division;
    }

    public void setStudent_division(String student_division) {
        this.student_division = student_division;
    }
}
