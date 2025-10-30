package com.example.tellem.launcher.models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;

// nie wiem czy korzystanie z funkcji hasJsonResponse jest optymalne, czy nie można parsować jednorazowo, a nie wiele razy wywoływać tą samą operacje

public class AuthService {
    private final ApiClient client;

    public String refreshToken = "";
    public String accessToken = "";

    public AuthService(ApiClient client) {
        this.client = client;
    }

    public ResponseResult login(String login, String password){
        try {
            String json = "{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}";
            HttpResponse<String> response = client.post(ApiEndpoint.LOGIN, json);

            accessToken = itemJsonResponse(response, "accessToken");
            refreshToken = TokenEncryptor.encrypt(itemJsonResponse(response, "refreshToken"));

            if (response.statusCode() == 200){
                return new ResponseResult(true ,"You logged in successfully.");
            } else {
                return new ResponseResult(false,"Status: " + response.statusCode() + "\n" + itemJsonResponse(response, "message"));
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(false, "App error.");
        }
    }

    public ResponseResult loginWithTokens(){
        try {
            String json = "{\"accessToken\":\"" + accessToken + "\"}";
            HttpResponse<String> response = client.post(ApiEndpoint.LOGINTOKEN, json);
            if (response.statusCode() == 200){
                return new ResponseResult(true ,"You logged in successfully.");
            } else if (response.statusCode() == 300) {
                // zmień kod na expired access token
                refresh();
                return loginWithTokens();
            } else {
                return new ResponseResult(false, "Tokens expired");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseResult register(String login, String password, String email){
        try{
            String json = "{\"login\":\"" + login + "\", \"password\":\"" + password + "\", \"email\":\"" + email +  "\"}";
            HttpResponse<String> response = client.post(ApiEndpoint.REGISTER, json);


            if (response.statusCode() == 201){
                return new ResponseResult(true, "Account is created. Please check your mail inbox for verification link");
            } else {
                return new ResponseResult(false, "Status: " + response.statusCode() + "\n" + itemJsonResponse(response, "message"));
            }

        } catch(Exception e){
            e.printStackTrace();
            return new ResponseResult(false, "App error.");
        }
    }

    public ResponseResult refresh(){
        try {
            String json = "{\"refreshToken\":\"" + TokenEncryptor.decrypt(refreshToken) + "\"}";
            HttpResponse<String> response = client.post(ApiEndpoint.REFRESH, json);
            if (response.statusCode() == 200){
                accessToken = itemJsonResponse(response, "accessToken");
                return new ResponseResult(true, "Access acquired.");
            } else {
                return new ResponseResult(false, "Access denied.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    // zapisuje refreshtoken/accesstoken?
    public void saveRefreshToken() throws Exception {
        if(refreshToken.isEmpty()){
            refreshToken = TokenEncryptor.encrypt(refreshToken);
        }
    }
    */

    // parsuje json z serwera
    public String itemJsonResponse(HttpResponse<String> response, String item){
        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        return jsonResponse.has(item) ? jsonResponse.get(item).getAsString() : "";
    }
}