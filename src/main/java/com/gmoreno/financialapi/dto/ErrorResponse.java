package com.gmoreno.financialapi.dto;

import java.util.Map;

public class ErrorResponse {

    private int status;
    private String message;
    private Map<String, String> errors;
    private String path;

    public ErrorResponse(
            int status,
            String message,
            Map<String, String> errors,
            String path) {

        this.status = status;
        this.message = message;
        this.errors = errors;
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
    public String getPath() {
        return path;
    }
}