package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.data.model.SubjectPojo;

import java.util.ArrayList;

public class PaperResponse {

    public final boolean status;

    @Nullable
    public final ArrayList<PaperPojo> data;

    @Nullable
    private final String message;

    private PaperResponse(@NonNull boolean status, @Nullable ArrayList<PaperPojo> data, @Nullable String message) {
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