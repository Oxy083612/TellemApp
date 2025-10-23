module com.example.tellem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires annotations;

    opens com.example.tellem to javafx.fxml, com.google.gson;
    exports com.example.tellem;
    exports com.example.tellem.controllers;
    opens com.example.tellem.controllers to com.google.gson, javafx.fxml;
    exports com.example.tellem.application.models;
    opens com.example.tellem.application.models to com.google.gson, javafx.fxml;
    exports com.example.tellem.application.controllers;
    opens com.example.tellem.application.controllers to com.google.gson, javafx.fxml;
    exports com.example.tellem.launcher.controllers;
    opens com.example.tellem.launcher.controllers to com.google.gson, javafx.fxml;
}