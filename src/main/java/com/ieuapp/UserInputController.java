package com.ieuapp;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserInputController {

    public RadioButton radioNew;
    public RadioButton radioImport;
    public VBox importConfigInfo;
    public VBox newConfigInfo;
    public TextField configFilePath;
    public TextField projectName;
    public TextField zipFilePath;
    public ChoiceBox languageChoice;
    public TextField projectArguments;
    public TextField projectDestinationPath;
    public Button configFilePathButton;
    public Button projectDestinationPathButton;
    public Button zipFilePathButton;
    public TextArea expectedOutput;
    public TextField configFileName;
    public TextField destinationPath;
    public Button destinationPathButton;
    public VBox editConfigVBox;
    public TextField compCommand;
    public TextField runCommand;
    public Button configFilePathEditButton;
    public TextField configFilePathForDelete;
    public Button configFilePathDeleteButton;
