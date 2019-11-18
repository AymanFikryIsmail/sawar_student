package com.hesham.sawarstudent.data.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsPojo {

    private int id;
    @SerializedName("paper_name")
    private String name;
    @SerializedName("subject")
    private String subject;
//    @SerializedName("no")
//    private int page;

    @SerializedName("no")
    private int no;
    @SerializedName("paper_num")
    private int page;


    private int sub_id;
    private int p_id;
    private int o_id;
    private double price;

    @SerializedName("paper_type")
    private String type;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public int getPage() {
        return page;
    }

    public int getNo() {
        return no;
    }

    public int getSub_id() {
        return sub_id;
    }

    public int getP_id() {
        return p_id;
    }

    public int getO_id() {
        return o_id;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}