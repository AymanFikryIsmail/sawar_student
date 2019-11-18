package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hesham.sawarstudent.data.model.UserPojo;

public class SignUpResponse {
    public final boolean status;

    @Nullable
    public final SignUp data;

    @Nullable
    private final String message;
    private final String token;


    private SignUpResponse(@NonNull boolean status, @Nullable SignUp data, @Nullable String message , @Nullable String token) {
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

    public class SignUp{
        private int insertId;

        public int getInsertId() {
            return insertId;
        }
    }
}
