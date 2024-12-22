package com.example.WeatherWiseApp.logic.exceptions;

public class InvalidJsonParsingException extends ParsingException {
    private static final String INVALID_JSON_MSG = "Unable to process weather data. Please try again.";
    public InvalidJsonParsingException(Throwable e) {
        super(INVALID_JSON_MSG, e);
    }
}
