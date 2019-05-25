package com.rps;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private long timestamp;
    private int status;
    private String message;
    private String path;

    public ErrorResponse(HttpStatus status, String message, String contextPath) {
        this.timestamp = System.currentTimeMillis();
        this.status = status.value();
        this.message = message;
        this.path = contextPath;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
