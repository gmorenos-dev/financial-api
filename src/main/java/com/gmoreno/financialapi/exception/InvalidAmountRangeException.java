package com.gmoreno.financialapi.exception;

public class InvalidAmountRangeException extends RuntimeException {

    private final String field;

    public InvalidAmountRangeException(String message) {
        this("minAmount", message);
    }

    public InvalidAmountRangeException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
