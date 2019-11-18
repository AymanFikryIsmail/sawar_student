package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawarstudent.data.model.DepartmentPojo;

import java.util.ArrayList;

public class DepartmentResponse {

    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final ArrayList<DepartmentPojo> cc_id;

    @Nullable
    private final String message;

    private DepartmentResponse(@NonNull boolean status, @Nullable ArrayList<DepartmentPojo> data, @Nullable String message) {
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