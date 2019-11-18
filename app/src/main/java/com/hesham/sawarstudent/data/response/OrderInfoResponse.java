package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hesham.sawarstudent.data.model.OrderInfo;
import com.hesham.sawarstudent.data.model.OrderPojo;

import java.util.ArrayList;

public class OrderInfoResponse {

    public final boolean status;

    @Nullable
    public final ArrayList<OrderInfo> data;

    @Nullable
    private final String message;

    private OrderInfoResponse(@NonNull boolean status, @Nullable ArrayList<OrderInfo> data, @Nullable String message) {
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