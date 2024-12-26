package com.daniyalh.WeatherWiseApp.logic.exceptions;

public class ParsingException extends RuntimeException {
    public ParsingException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
