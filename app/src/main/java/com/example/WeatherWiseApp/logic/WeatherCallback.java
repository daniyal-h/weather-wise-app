package com.example.WeatherWiseApp.logic;

public interface WeatherCallback {
    void onSuccess(String response);
    void onError(String error);
}