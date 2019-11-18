package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class CustomResponse {
    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final Object data;

    @Nullable
    private final String message;

    private CustomResponse(@NonNull boolean status, @Nullable Object data, @Nullable String message) {
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
