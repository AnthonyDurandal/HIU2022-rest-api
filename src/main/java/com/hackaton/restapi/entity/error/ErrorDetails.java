package com.hackaton.restapi.entity.error;

import java.sql.Timestamp;

public class ErrorDetails {
    private final boolean success;
    private final String message;
    private final Timestamp timestamp;

    public ErrorDetails(boolean success, String message, Timestamp timestamp) {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

}
