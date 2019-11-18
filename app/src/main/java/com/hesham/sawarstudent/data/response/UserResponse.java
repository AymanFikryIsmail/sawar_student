package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawarstudent.data.model.FacultyPojo;
import com.hesham.sawarstudent.data.model.UserPojo;

import java.util.ArrayList;

public class UserResponse {
    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final int data;

    @Nullable
    private final String message;

    private UserResponse(@NonNull boolean status, @Nullable int data, @Nullable String message) {
        this.status = status;
        this.data = data;
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
