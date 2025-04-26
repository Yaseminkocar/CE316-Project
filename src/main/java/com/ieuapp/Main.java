package com.ieuapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("ieuApp successfully complated !");
        Scene scene = new Scene(label, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EduApp - Main Window");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
