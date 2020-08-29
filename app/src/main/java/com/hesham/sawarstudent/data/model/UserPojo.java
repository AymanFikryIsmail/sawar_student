package com.hesham.sawarstudent.data.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class UserPojo implements Serializable {
    private int id;
    private int stud_id;
    private String name;
    private String email;
    private String password;
    private String gender;

    @SerializedName("univ_id")
    private int univ;
    @SerializedName("f_id")
    private int facultyId;
    private String photo;
    private int points;
    private String token;
    private ArrayList<Integer> departments;

    public UserPojo() {
    }

    public UserPojo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserPojo(String name, String email, String password, String gender,
                    int univ, int facultyId, String photo , String token,  ArrayList<Integer> departments) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.univ = univ;
        this.facultyId = facultyId;
        this.photo = photo;
        this.token = token;
        this.departments = departments;
    }

    // for update

    public UserPojo(int stud_id, String name, String email, String password, int univ, int facultyId,  String photo,  ArrayList<Integer> departments) {
        this.id = stud_id;
        this.stud_id = stud_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.univ = univ;
        this.facultyId = facultyId;
        this.photo = photo;
        this.departments = departments;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public int getUniv() {
        return univ;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPoints() {
        return points;
    }

    public String getToken() {
        return token;
    }
}
