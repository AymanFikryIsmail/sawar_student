package com.hesham.sawarstudent.data.model;

import com.google.gson.annotations.SerializedName;

public class CenterPojo {
    private int id;
    private String name;
    private String address;
    private String passwd;
    private String start;
    private String end;
    private int delay_hours;
    private long delay_date;
    private int univ_id;
    private int years;
    private String logo;
    private double rate;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getStart() {
        return start;
    }

    public int getDelay_hours() {
        return delay_hours;
    }

    public long getDelay_date() {
        return delay_date;
    }

    public int getUniv_id() {
        return univ_id;
    }

    public int getYears() {
        return years;
    }

    public String getLogo() {
        return logo;
    }

    public String getEnd() {
        return end;
    }

    public double getRate() {
        return rate;
    }
}
