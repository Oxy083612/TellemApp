package com.example.tellem;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class TellemController {

    @FXML
    private VBox blackLine;

    @FXML
    private VBox communicationMenu;

    @FXML
    private StackPane errorLabelsL;

    @FXML
    private StackPane errorLabelsR;

    @FXML
    private StackPane functionalContainer;

    @FXML
    private VBox leftPanel;

    @FXML
    private VBox loginField;

    @FXML
    private VBox loginMenu;

    @FXML
    private TextField loginL;

    @FXML
    private PasswordField passwordL;

    @FXML
    private VBox mainMenu;

    @FXML
    private VBox registerMenu;

    @FXML
    private TextField loginR;

    @FXML
    private PasswordField passwordR;

    @FXML
    private PasswordField passwordConfirmR;

    @FXML
    private TextField emailR;

    @FXML
    private VBox rightPanel;

    @FXML
    private StackPane rootPane;

    private String refreshToken = "";
    private String accessToken = "";

    @FXML
    void exit() {

    }

    @FXML
    private void onLogInClick(){
        mainMenu.setVisible(false);
        loginMenu.setVisible(true);
    }

    @FXML
    private void onReturnClick(){
        mainMenu.setVisible(true);
        loginMenu.setVisible(false);
        registerMenu.setVisible(false);
        hideErrors();

        loginL.setText("");
        passwordL.setText("");

        loginR.setText("");
        passwordR.setText("");
        passwordConfirmR.setText("");
        emailR.setText("");
    }

    @FXML
    private void onLoggingInClick(){
        try {
            hideErrors();
            if (loginL.getText().isEmpty() || passwordL.getText().isEmpty()){
                Label err = (Label) errorLabelsL.getChildren().get(0);
                err.setVisible(true);
            } else if (this.refreshToken.isEmpty()) {
                URL url = new URL("http://localhost:3000/refresh");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "text/plain");

                try (OutputStream os = con.getOutputStream()){



                }

            } else {
                URL url = new URL("http://localhost:3000/login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "text/plain");

                String data = "logger\n" + loginL.getText() + "\n" + passwordL.getText() + "\n";

                try (OutputStream os = con.getOutputStream()) {
                    os.write(data.getBytes());
                    os.flush();
                }
                if (con.getResponseCode() == 200){
                   try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))){
                       String response = new String();
                       String line;
                       while((line = br.readLine()) != null){
                            response = line;
                       }
                       System.out.println("Server response: " + response);
                       if(response.equals("succesful")){

                       }
                   }
                } else {
                    System.out.println("Logging error: kod " + con.getResponseCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label err = (Label) errorLabelsL.getChildren().get(2);
            err.setVisible(true);
        }
    }

    @FXML
    void onRegisterClick() {
        mainMenu.setVisible(false);
        registerMenu.setVisible(true);
    }

    @FXML
    void onRegisterInClick(){
        hideErrors();
        String login = loginR.getText();
        String pass = passwordR.getText();
        String passConfirm = passwordConfirmR.getText();
        String email = emailR.getText();

        if (login.isEmpty() || pass.isEmpty() || passConfirm.isEmpty() || email.isEmpty()){
            hideErrors();
            errorLabelsR.getChildren().get(0).setVisible(true);
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!pass.equals(passwordConfirmR.getText())) {
            hideErrors();
            errorLabelsR.getChildren().get(1).setVisible(true);
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$")){
            hideErrors();
            errorLabelsR.getChildren().get(2).setVisible(true);
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!pass.matches("^(?=.*[A-Z]).{8,}$")){
            hideErrors();
            errorLabelsR.getChildren().get(3).setVisible(true);
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!login.matches("^.{8,}$")){
            hideErrors();
            errorLabelsR.getChildren().get(4).setVisible(true);
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("login", login);
            json.addProperty("password", pass);
            json.addProperty("email", email);
            try {
                URL url = new URL("http://localhost:3000/register");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");

                try (OutputStream os = con.getOutputStream()) {
                    String jsonString = new Gson().toJson(json);
                    byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                String status = con.getResponseMessage();
                System.out.println("Status: " + status);

            } catch (Exception e) {
                e.printStackTrace();
                Label err = (Label) errorLabelsR.getChildren().get(5);
                err.setVisible(true);
            }
        }


    }

    @FXML
    void onOptionsClick() {

    }

    private void hideErrors(){
        for (Node x : errorLabelsL.getChildren()){
            x.setVisible(false);
        }
        for (Node x : errorLabelsR.getChildren()){
            x.setVisible(false);
        }
    }
}