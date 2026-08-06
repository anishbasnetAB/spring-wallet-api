package com.anish.wallet_api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> messages;
    private String path;

    public ApiError(){

    }

    public int getStatus() {
        return status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }


}
