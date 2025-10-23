package com.example.tellem.launcher.models;

public enum ApiEndpoint {
    LOGIN("/login"),
    LOGINTOKEN("/loginToken"),
    REGISTER("/register"),
    REFRESH("/refresh");

    private final String path;

    ApiEndpoint(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
