package com.example.tellem.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.util.HashMap;

public class ScreenController {
    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene main;

    public ScreenController(Scene main) {
        this.main = main;
    }

    public void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        Parent p = screenMap.get(name);
        if (p == null) {
            System.err.println("ScreenController: brak ekranu o nazwie '" + name + "'");
            return;
        }
        main.setRoot(p);
    }

    public Scene getScene() {
        return main;
    }
}
