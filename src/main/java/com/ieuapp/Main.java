package com.ieuapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1200, 750);

                Controller controller = fxmlLoader.getController();
                controller.setPrimaryStage(stage);
                DataExchange.getInstance().setController(controller);

                stage.setTitle("İEU App");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }





    public static void main(String[] args) {
        launch();
        System.out.println("deneme");
    }
}