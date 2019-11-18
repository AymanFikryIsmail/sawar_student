package com.hesham.sawarstudent.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hesham.sawarstudent.data.model.CenterPojo;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.data.model.UserPojo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences constants
    private static final String PREF_NAME = "MyPreference";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public int getCenterId() {
        return pref.getInt("centerId", 0);
    }

    public void setCenterId(int centerId) {
        editor.putInt("centerId", centerId);
        editor.commit();
    }

    public int getSubjectId() {
        return pref.getInt("SubjectId", 0);
    }

    public void setSubjectId(int SubjectId) {
        editor.putInt("SubjectId", SubjectId);
        editor.commit();
    }
    public String getSubjectName() {
        return pref.getString("SubjectName", "");
    }

    public void setSubjectName(String SubjectName) {
        editor.putString("SubjectName", SubjectName);
        editor.commit();
    }
    public int getUniversityId() {
        return pref.getInt("niversityId", 0);
    }

    public void setUniversityId(int niversityId) {
        editor.putInt("niversityId", niversityId);
        editor.commit();
    }
    public int getFacultyId() {
        return pref.getInt("FacultyId", 0);
    }

    public void setFacultyId(int FacultyId) {
        editor.putInt("FacultyId", FacultyId);
        editor.commit();
    }

    public int getType() {
        return pref.getInt("type", 0);
    }

    public void setType(int type) {
        editor.putInt("type", type);
        editor.commit();
    }
    public String getToken() {
        return pref.getString("Token", "");
    }

    public void setToken(String token) {
        editor.putString("Token", token);
        editor.commit();
    }

    public String getNotificationToken() {
        return pref.getString("NotificationToken", "");
    }

    public void setNotificationToken(String token) {
        editor.putString("NotificationToken", token);
        editor.commit();
    }

    public boolean getHasPromo() {
        return pref.getBoolean("hasPromo", false);
    }

    public void sethasPromo(boolean hasPromo) {
        editor.putBoolean("hasPromo", hasPromo);
        editor.commit();
    }
    public void removeAll() {
        editor.remove("UserPojo");
        editor.remove("student");
//        editor.remove("orderCenterId");
//        editor.remove("CartPapers");

        editor.apply();
    }


//    public String getPasswordToken() {
//        return pref.getString("passwordToken", "");
//    }
//
//    public void setPasswordToken(String passwordToken) {
//        editor.putString("passwordToken", passwordToken);
//        editor.commit();
//    }


    public UserPojo getStudentData() {
        Gson gson = new Gson();
        String json = pref.getString("student", "");
        UserPojo centerData = gson.fromJson(json, UserPojo.class);
        return centerData;
    }

    public void setStudentData(UserPojo centerData) {
        Gson gson = new Gson();
        String json = gson.toJson(centerData);
        editor.putString("student", json);
        editor.commit();
    }



    public CenterPojo getCenterData() {
        Gson gson = new Gson();
        String json = pref.getString("centerData", "");
        CenterPojo centerData = gson.fromJson(json, CenterPojo.class);
        return centerData;
    }

    public void setCenterData(CenterPojo centerData) {
        Gson gson = new Gson();
        String json = gson.toJson(centerData);
        editor.putString("centerData", json);
        editor.commit();
    }





    public UserPojo getUserPojo() {
        Gson gson = new Gson();
        String json = pref.getString("UserPojo", "");
        UserPojo association = gson.fromJson(json, UserPojo.class);
        return association;
    }

    public void setUserPojo(UserPojo association) {
        Gson gson = new Gson();
        String json = gson.toJson(association);
        editor.putString("UserPojo", json);
        editor.commit();
    }
    public String getImageProfile() {
        return pref.getString("ImageProfile", "");
    }

    public void setImageProfile(String ImageProfile) {
        editor.putString("ImageProfile", ImageProfile);
        editor.commit();
    }

    public int getOrderCenterId() {
        return pref.getInt("orderCenterId", 0);
    }

    public void setOrderCenterId(int orderCenterId) {
        editor.putInt("orderCenterId", orderCenterId);
        editor.commit();
    }


    public int getCartCenterId() {
        return pref.getInt("cartCenterId", 0);
    }

    public void setCartCenterId(int centerId) {
        editor.putInt("cartCenterId", centerId);
        editor.commit();
    }

    public HashMap<Integer ,PaperPojo> getCartPapers() {
        Gson gson = new Gson();
        String json = pref.getString("CartPapers", "");
        Type type = new TypeToken<HashMap<Integer ,PaperPojo>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void setCartPapers(HashMap<Integer ,PaperPojo> cartPapers) {
        Gson gson = new Gson();
        String json = gson.toJson(cartPapers);
        editor.putString("CartPapers", json);
        editor.commit();
    }
    public int getOrderNumber() {
        return pref.getInt("OrderNumber", 0);
    }

    public void setOrderNumber(int OrderNumber) {
        editor.putInt("OrderNumber", OrderNumber);
        editor.commit();
    }

    public void removeCartPapers() {
        editor.remove("CartPapers");
        editor.putInt("cartCenterId", 0);
        editor.apply();
    }

}
