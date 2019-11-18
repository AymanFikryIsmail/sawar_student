package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawarstudent.data.model.LoginPojo;
import com.hesham.sawarstudent.data.model.UserPojo;

public class LoginResponse {
    public final boolean status;

    @Nullable
    public final UserPojo data;

    @Nullable
    private final String message;
    private final String token;


    private LoginResponse(@NonNull boolean status, @Nullable UserPojo data, @Nullable String message ,@Nullable String token) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.token = token;

    }

    public String getToken() {
        return token;
    }

    public boolean getSuccess() {
        return status;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
