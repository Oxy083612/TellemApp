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
    requires kotlin.stdlib;

    exports tellem.app;

    opens tellem.ui.controllers to javafx.fxml;
    opens tellem.app to javafx.fxml;
    opens tellem.launcher.ui to javafx.fxml;

    opens tellem.model to com.google.gson;
}