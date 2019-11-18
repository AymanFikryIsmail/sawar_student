package com.hesham.sawarstudent.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderPojo {
    private int id;
    private String name;
    private String cc;
    private int num;
    private String day;
    private String var;
    private int waiting;
    private int page;
    private long date;
    private long delay_hours;
    private long delay_date;
    private int sub_id;
    private double price;
    private double total_price;
    private double service;

    private int ready;
    private int recieve;
    private int cancel_cc;
    private int cancel_s;
    private int rate;

    private String type;
    private int cc_id;

    public OrderPojo(String name, int page, int sub_id, double price, String type) {
        this.name = name;
        this.page = page;
        this.sub_id = sub_id;
        this.price = price;
        this.type = type;
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

    public long getLongDate(){
        return this.date;

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
    public String getFormattedDate() {

        long val = this.date;
        Date date=new Date(val);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = dateFormatter.format(date);
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

    public long getDelay_hours() {
        return delay_hours;
    }

    public long getDelay_date() {
        return delay_date;
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

    public double getTotal_price() {
        return total_price;
    }

    public double getService() {
        return service;
    }

    public int getReady() {
        return ready;
    }

    public int getCancel_cc() {
        return cancel_cc;
    }

    public int getCancel_s() {
        return cancel_s;
    }

    public int getRate() {
        return rate;
    }

    public int getRecieve() {
        return recieve;
    }

    public String getCc() {
        return cc;
    }

    public int getNum() {
        return num;
    }

    public String getDay() {
        return day;
    }

    public String getVar() {
        return var;
    }

    public int getWaiting() {
        return waiting;
    }

    public int getCc_id() {
        return cc_id;
    }
}
