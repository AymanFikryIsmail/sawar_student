package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class CheckPromoResponse {
    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final boolean data;

    @Nullable
    private final String message;

    private CheckPromoResponse(@NonNull boolean status, @Nullable boolean data, @Nullable String message) {
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
