package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {
    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final String data;

    @Nullable
    private final String message;

    private ImageResponse(@NonNull boolean status, @Nullable String data, @Nullable String message) {
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
