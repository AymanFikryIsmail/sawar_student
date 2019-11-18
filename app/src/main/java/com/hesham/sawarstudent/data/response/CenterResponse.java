package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hesham.sawarstudent.data.model.CenterPojo;

import java.util.List;

public class CenterResponse {
    public final boolean status;

    @Nullable
    public final List<CenterPojo> data;

    @Nullable
    private final String message;
    private final String token;


    private CenterResponse(@NonNull boolean status, @Nullable List<CenterPojo> data, @Nullable String message , @Nullable String token) {
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
