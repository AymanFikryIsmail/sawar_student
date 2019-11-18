package com.hesham.sawarstudent.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubjectPojo {
    private int id;
    private int cc_id;
    @SerializedName("f_id")
    private int faculty_id;
    private int year;
    private int term;
    private String name;

    private  int sub_id;
    private  Integer d_id;


    // for favourite
    private String sub_name;
    private String type;
    private  int stud_id;

    public SubjectPojo(int cc_id, int faculty_id, int year, int term) {
        this.cc_id = cc_id;
        this.faculty_id = faculty_id;
        this.year = year;
        this.term = term;
    }
    public SubjectPojo(int cc_id, int faculty_id, int year, int term, Integer d_id) {
        this.cc_id = cc_id;
        this.faculty_id = faculty_id;
        this.year = year;
        this.term = term;
        this.d_id = d_id;

    }
    public SubjectPojo(int cc_id, int faculty_id, int year, int term, String name) {
        this.cc_id = cc_id;
        this.faculty_id = faculty_id;
        this.year = year;
        this.term = term;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCc_id() {
        return cc_id;
    }

    public int getFaculty_id() {
        return faculty_id;
    }

    public int getYear() {
        return year;
    }

    public int getTerm() {
        return term;
    }

    public String getSub_name() {
        return sub_name;
    }

    public String getType() {
        return type;
    }

    public int getStud_id() {
        return stud_id;
    }
}
