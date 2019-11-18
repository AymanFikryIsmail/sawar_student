package com.hesham.sawarstudent.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaperPojo implements Serializable{

    private int id;
    private int paper_id;
    @SerializedName("p_id")
    private int p_id;
    private String name;
    private int page;
    private long date;
    private int sub_id;
    private double price;
    private String type;
    private int paper_flag ;
    private int no ;
    private String subName;

    public PaperPojo() {
    }

    public PaperPojo(int p_id, int sub_id, int no, double price ) {
        this.p_id = p_id;
        this.sub_id = sub_id;
        this.price = price;
        this.no = no;
    }

    public PaperPojo(String name, int page, int sub_id, double price, String type) {
        this.name = name;
        this.page = page;
        this.sub_id = sub_id;
        this.price = price;
        this.type = type;
    }

    public int getP_id() {
        return p_id;
    }

    public int getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(int paper_id) {
        this.paper_id = paper_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPage() {
        return page;
    }

    public String getDate() {
        long val = this.date;
        Date date=new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        Date newDate = null;
//        try {
//            newDate = format.parse(strCurrentDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        format = new SimpleDateFormat("yyyy-MM-dd");
//        Date currentDate = new Date(newDate.getTime());
//        String d = format.format(currentDate);
        return dateText;
    }

    public  Long getLongDate(){
        return  date;
    }
    public int getSub_id() {
        return sub_id;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPaper_flag() {
        return paper_flag;
    }

    public int getNo() {
        return no;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPaper_flag(int paper_flag) {
        this.paper_flag = paper_flag;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }


}
