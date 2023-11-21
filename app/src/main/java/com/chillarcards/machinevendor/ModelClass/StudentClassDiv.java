package com.chillarcards.machinevendor.ModelClass;

/**
 * Created by Codmob on 13-03-17.
 */

public class StudentClassDiv {

    int id;
    String stud_class;
    String stud_div;



    String class_div;

    public StudentClassDiv( String stud_class, String stud_div,String class_div) {
        this.stud_class = stud_class;
        this.stud_div = stud_div;
        this.class_div=class_div;
    }

    public StudentClassDiv() {
    }

    public String getStud_class() {
        return stud_class;
    }

    public void setStud_class(String stud_class) {
        this.stud_class = stud_class;
    }

    public String getStud_div() {
        return stud_div;
    }

    public void setStud_div(String stud_div) {
        this.stud_div = stud_div;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClass_div() {
        return class_div;
    }

    public void setClass_div(String class_div) {
        this.class_div = class_div;
    }
}
