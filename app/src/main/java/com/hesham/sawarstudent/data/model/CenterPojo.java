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
    private boolean notification_flag;
    private int notification_year;
    private Integer notification_dep;




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

    public boolean getIsNotification_flag() {
        return notification_flag;
    }

    public void setNotification_flag(boolean notification_flag) {
        this.notification_flag = notification_flag;
    }


    public int getNotification_year() {
        return notification_year;
    }

    public void setNotification_year(int notification_year) {
        this.notification_year = notification_year;
    }

    public Integer getNotification_dep() {
        return notification_dep;
    }

    public void setNotification_dep(Integer notification_dep) {
        this.notification_dep = notification_dep;
    }
}
