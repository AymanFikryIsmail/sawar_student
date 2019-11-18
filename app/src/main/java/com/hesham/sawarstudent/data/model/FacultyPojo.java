package com.hesham.sawarstudent.data.model;

import java.util.ArrayList;

public class FacultyPojo {

    private int id;
    private String name;
    private int years;
    private boolean isSelected;
    private int univ_id;

    private int cc_id;
    private ArrayList<Integer> faculties_id;

    public FacultyPojo(int cc_id,  ArrayList<Integer>  faculties_id) {
        this.cc_id = cc_id;
        this.faculties_id = faculties_id;
    }

    public FacultyPojo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public int getYears() {
        return years;
    }

    public int getUniv_id() {
        return univ_id;
    }
}
