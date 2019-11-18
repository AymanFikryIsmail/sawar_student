package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hesham.sawarstudent.data.model.OrderPojo;

import java.util.ArrayList;

public class StatusResponse {

    public final boolean status;

    @Nullable
    public final  OrderPojo  data;

    @Nullable
    private final String message;

    private StatusResponse(@NonNull boolean status, @Nullable OrderPojo  data, @Nullable String message) {
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