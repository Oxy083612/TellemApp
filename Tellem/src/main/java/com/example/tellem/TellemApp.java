package com.example.tellem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TellemApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Tellem");
        stage.setScene(scene);
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
