package com.gmoreno.financialapi.dto;

import java.util.Map;

public class ValidationErrorResponse {

    private Map<String, String> errors;

    public ValidationErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
