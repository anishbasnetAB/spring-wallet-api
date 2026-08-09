package com.anish.wallet_api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> messages;
    private String path;

    public ApiError(
            LocalDateTime timestamp,
            int status,
            String error,
            List<String> messages,
            String path
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.messages = messages;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getPath() {
        return path;
    }
}