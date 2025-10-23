package com.example.tellem.launcher.models;

public class ResponseResult {
    public final boolean status;
    public final String message;

    public ResponseResult(boolean status, String message){
        this.status = status;
        this.message = message;
    }
}
