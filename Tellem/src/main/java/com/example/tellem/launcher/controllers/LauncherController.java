package com.example.tellem.launcher.controllers;

import com.example.tellem.controllers.ScreenController;
import com.example.tellem.launcher.models.ApiClient;
import com.example.tellem.launcher.models.AuthService;
import com.example.tellem.launcher.models.ResponseResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LauncherController {

    @FXML
    private VBox blackLine;

    @FXML
    private VBox communicationMenu;

    @FXML
    private Label errorLabelL;

    @FXML
    private Label errorLabelR;

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

    @FXML
    private Text info;

    private final AuthService authService = new AuthService(new ApiClient("http://localhost:3000"));
    private ScreenController screenController;

    @FXML
    void exit() {
        Stage stage = (Stage) loginMenu.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onLogInClick(){
        if (!authService.accessToken.isEmpty() && !authService.refreshToken.isEmpty()){
            ResponseResult result = authService.loginWithTokens();
            if(result.status){                  // sprawdź access token
                changeViewToApp();
                System.out.println("AccessToken ważny");
            } else {
                System.out.println("AccessToken nie ważny");
                ResponseResult refreshResult = authService.refresh(); //odnów access token
                if(refreshResult.status){              //jeśli jest ważny zaloguj
                    System.out.println("RefreshToken ważny");
                    changeViewToApp();
                } else {
                    System.out.println("RefreshToken nie ważny");
                    mainMenu.setVisible(false);
                    loginMenu.setVisible(true);
                }
            }
        } else {
            mainMenu.setVisible(false);
            loginMenu.setVisible(true);
        }
    }

    @FXML
    private void onLoggingInClick(){
        try {
            hideErrors();
            ResponseResult result = authService.login(loginL.getText(), passwordL.getText());
            if(result.status){
                changeViewToApp();
            } else {
                errorLabelL.setText(result.message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabelL.setText("Unidentified error");
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
            errorLabelR.setText("No field cannot be empty.");
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!pass.equals(passConfirm)) {
            errorLabelR.setText("Password and confirm password fields are not matching.");
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$")){
            errorLabelR.setText("Wrong e-mail format.");
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!pass.matches("^(?=.*[A-Z]).{8,}$")){
            errorLabelR.setText("Password must contain at least one big case letter and be at least 8 characters long.");
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else if (!login.matches("^.{8,}$")){
            errorLabelR.setText("Username has to be at least 8 characters long.");
            passwordR.setText("");
            passwordConfirmR.setText("");
        } else {
            try {
                hideErrors();

                ResponseResult result = authService.register(login, pass, email);
                errorLabelR.setText(result.message);
            } catch (Exception e) {
                e.printStackTrace();
                errorLabelR.setText("Unidentified error");
            }
        }


    }

    @FXML
    private void onReturnClick(){
        mainMenu.setVisible(true);
        loginMenu.setVisible(false);
        registerMenu.setVisible(false);
        communicationMenu.setVisible(false);
        hideErrors();

        loginL.setText("");
        passwordL.setText("");

        loginR.setText("");
        passwordR.setText("");
        passwordConfirmR.setText("");
        emailR.setText("");
    }

    @FXML
    void onOptionsClick() {

    }

    public void setScreenController(ScreenController screenController){
        this.screenController = screenController;
    }

    void changeViewToApp(){
        screenController.activate("launcher");
    }

    private void hideErrors(){
        errorLabelL.setText("");
        errorLabelR.setText("");
    }
/*
    public void handleResponse(int status, JsonObject response, String endpoint, String method){
        if (status >= 200 && status < 300){

            if (endpoint.equals("/register") && method.equals("POST")){
                System.out.println("Status: " + status + "\n" + response);
                hideErrors();
                registerMenu.setVisible(false);
                communicationMenu.setVisible(true);
                info.setText("Account created successfully. Please Verify your account using link sent in your e-mail inbox before logging in.");
            }

            if (endpoint.equals("/login") && method.equals("POST")){
                System.out.println("Status: " + status + "\n" + response);

                this.accessToken = String.valueOf(response.get("access"));
                this.refreshToken = String.valueOf(response.get("refresh"));

                System.out.println(accessToken + "\n" + refreshToken);

                hideErrors();

                // should change view to proper app
                loginMenu.setVisible(false);
                communicationMenu.setVisible(true);
                info.setText("You logged in successfully");


            }

        } else if ( status > 300 ){

            hideErrors();

            if (endpoint.equals("/register")){
                errorLabelR.setText(String.valueOf(response));
            }

            System.out.println("Status: " + status + "\n" + response);

        }
    }

 */
}