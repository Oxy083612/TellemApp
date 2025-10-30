package com.example.tellem;

import com.example.tellem.application.controllers.MainAppController;
import com.example.tellem.controllers.ScreenController;
import com.example.tellem.launcher.controllers.LauncherController;
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
        Scene scene = new Scene(new Pane());
        ScreenController screenController = new ScreenController(scene);

        FXMLLoader loaderLauncher = new FXMLLoader(getClass().getResource("launcher-view.fxml"));
        Pane launcher = loaderLauncher.load();
        LauncherController launcherController = loaderLauncher.getController();
        launcherController.setScreenController(screenController);

        FXMLLoader loaderApp = new FXMLLoader(getClass().getResource("app-view.fxml"));
        Pane app = loaderApp.load();
        MainAppController appController = loaderApp.getController();
        appController.setScreenController(screenController);

        screenController.addScreen("launcher", launcher);
        screenController.addScreen("app", app);

        screenController.activate("launcher");

        stage.setScene(screenController.getScene());
        stage.setTitle("Tellem");
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
