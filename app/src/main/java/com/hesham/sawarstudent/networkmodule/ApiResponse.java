package com.hesham.sawarstudent.networkmodule;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ayman on 2019-05-13.
 */

public class ApiResponse <T> {

    @NonNull
    public final String status;

    @Nullable
    public final T data;

    @Nullable
    private final String message;

    private ApiResponse(@NonNull String status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
    public String getSuccess() {
        return status;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}