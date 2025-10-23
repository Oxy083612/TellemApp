package com.example.tellem;

import com.example.tellem.controllers.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class TellemApp extends Application {
    
    private static final boolean is_logged = false;
    
    @Override
    public void start(Stage stage) throws IOException {
        ScreenController screenController = new ScreenController(new Scene(new Pane(), 800, 600));

        screenController.addScreen("launcher", FXMLLoader.load(getClass().getResource("launcher-view.fxml")));
        screenController.addScreen("app", FXMLLoader.load(getClass().getResource("app-view.fxml")));

        screenController.activate("launcher");
        stage.setTitle("Tellem Launcher");
        stage.setScene(screenController.getScene());
        stage.show();
    }
    @Override
    public void stop() throws Exception {
        //TellemController.saveDataBeforeExit();
    }



    public static void main(String[] args) {
        launch();
    }
}
