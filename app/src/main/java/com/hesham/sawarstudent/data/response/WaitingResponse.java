package com.hesham.sawarstudent.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WaitingResponse {
    public final boolean status;

    @Nullable
    public final Waiting data;
    @Nullable
    private final String message;


    private WaitingResponse(@NonNull boolean status, @Nullable Waiting data, @Nullable String message ) {
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

    public class Waiting{

        private int orders;

        public int getOrders() {
            return orders;
        }
    }
}
