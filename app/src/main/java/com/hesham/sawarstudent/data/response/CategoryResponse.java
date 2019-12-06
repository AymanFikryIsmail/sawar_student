package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.hesham.sawarstudent.data.model.CategoryPojo;

import java.util.ArrayList;

public class CategoryResponse {

    public final boolean status;

    @Nullable
    public final ArrayList<CategoryPojo> data;

    @Nullable
    private final String message;

    private CategoryResponse(@NonNull boolean status, @Nullable ArrayList<CategoryPojo> data, @Nullable String message) {
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