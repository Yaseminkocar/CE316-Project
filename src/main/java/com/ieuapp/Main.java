package com.ieuapp;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student App");


        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newProject = new MenuItem("New Project");
        MenuItem openProject = new MenuItem("Open Project");
        MenuItem closeProject = new MenuItem("Close Project");
        MenuItem createConfig = new MenuItem("Create Config");
        MenuItem editConfig = new MenuItem("Edit Config");
        MenuItem exportConfig = new MenuItem("Export Config");
        MenuItem deleteConfig = new MenuItem("Delete Config");
        MenuItem quit = new MenuItem("Quit");

        fileMenu.getItems().addAll(newProject, openProject, closeProject, createConfig, editConfig, exportConfig, deleteConfig, new SeparatorMenuItem(), quit);

        Menu helpMenu = new Menu("Help");
        menuBar.getMenus().addAll(fileMenu, helpMenu);


        Label label = new Label("Start a new project or open an existing one.");
        label.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        Button openBtn = new Button("Open");
        Button createBtn = new Button("Create");
        Button closeBtn = new Button("Close");

        openBtn.setPrefSize(120, 40);
        createBtn.setPrefSize(120, 40);
        closeBtn.setPrefSize(120, 40);

        HBox buttonBox = new HBox(30, openBtn, createBtn, closeBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 30, 0));

        VBox centerBox = new VBox(20, label, buttonBox);
        centerBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();


        newProject.setOnAction(e -> showNewProjectWindow());
    }

    private void showNewProjectWindow() {
        Stage newProjectStage = new Stage();
        newProjectStage.setTitle("Create New Project");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(15);

        // Project Name
        Label nameLabel = new Label("Project Name:");
        TextField nameField = new TextField();
        nameField.setPrefWidth(300);

        // Configuration Radio Buttons
        Label configLabel = new Label("Configuration:");
        ToggleGroup configGroup = new ToggleGroup();
        RadioButton newRadio = new RadioButton("New");
        RadioButton importRadio = new RadioButton("Import");
        newRadio.setToggleGroup(configGroup);
        importRadio.setToggleGroup(configGroup);

        HBox configBox = new HBox(20, newRadio, importRadio);

        // Save To
        Label saveToLabel = new Label("Save To");
        TextField saveToField = new TextField("../Projects/");
        Button saveToExplore = new Button("Explore");

        HBox saveToBox = new HBox(10, saveToField, saveToExplore);

        // Zip Folder
        Label zipLabel = new Label("Choose Folder That Contains Zip Files");
        TextField zipField = new TextField("Projects");
        Button zipExplore = new Button("Explore");

        HBox zipBox = new HBox(10, zipField, zipExplore);


        Button createBtn = new Button("Create");
        createBtn.setPrefWidth(100);


        grid.add(new Label("Create New Project"), 0, 0, 2, 1);

        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(configLabel, 0, 2);
        grid.add(configBox, 1, 2);

        grid.add(saveToLabel, 0, 3);
        grid.add(saveToBox, 1, 3);

        grid.add(zipLabel, 0, 4);
        grid.add(zipBox, 1, 4);

        grid.add(createBtn, 1, 5);
        GridPane.setMargin(createBtn, new Insets(20, 0, 0, 0));
        GridPane.setHalignment(createBtn, HPos.RIGHT);

        Scene scene = new Scene(grid, 550, 400);
        newProjectStage.setScene(scene);
        newProjectStage.initModality(Modality.APPLICATION_MODAL);
        newProjectStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
