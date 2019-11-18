package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawarstudent.data.model.FacultyPojo;

import java.util.ArrayList;

public class FacultyResponse {

    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final ArrayList<FacultyPojo> cc_id;

    @Nullable
    private final String message;

    private FacultyResponse(@NonNull boolean status, @Nullable ArrayList<FacultyPojo> data, @Nullable String message) {
        this.status = status;
        this.cc_id = data;
        this.message = message;
    }
    public boolean getSuccess() {
        return status;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}