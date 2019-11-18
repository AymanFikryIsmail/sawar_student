package com.hesham.sawarstudent.data.model;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.http.Field;

public class AddOrderPojo implements Serializable {
    int cc_id;
    int stud_id;
    double service;
    double total;
    ArrayList<PaperPojo> papers;


    public AddOrderPojo(int cc_id, int stud_id, double service, double total, ArrayList<PaperPojo> papers) {
        this.cc_id = cc_id;
        this.stud_id = stud_id;
        this.service = service;
        this.total = total;
        this.papers = papers;
    }

    public int getCc_id() {
        return cc_id;
    }

    public void setCc_id(int cc_id) {
        this.cc_id = cc_id;
    }

    public int getStud_id() {
        return stud_id;
    }

    public void setStud_id(int stud_id) {
        this.stud_id = stud_id;
    }

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<PaperPojo> getPapers() {
        return papers;
    }

    public void setPapers(ArrayList<PaperPojo> papers) {
        this.papers = papers;
    }
}
