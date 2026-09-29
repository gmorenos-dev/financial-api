package com.gmoreno.financialapi.exception;

public class InvalidDateRangeException extends RuntimeException {

    public InvalidDateRangeException(String message) {
        super(message);
    }
}