package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawarstudent.data.model.FacultyPojo;
import com.hesham.sawarstudent.data.model.SubjectPojo;

import java.util.ArrayList;

public class SubjectResponse {

    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final ArrayList<SubjectPojo> cc_id;

    @Nullable
    private final String message;

    private SubjectResponse(@NonNull boolean status, @Nullable ArrayList<SubjectPojo> data, @Nullable String message) {
        this.status = status;
        this.cc_id = data;
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