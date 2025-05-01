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

    @FXML
    protected void onRadioButtonClicked(ActionEvent event){
        if (event.getSource().equals(radioNew)) {
            radioNew.setSelected(true);
            radioImport.setSelected(false);
            importConfigInfo.setVisible(false);
            newConfigInfo.setVisible(true);
        }
        else {
            radioImport.setSelected(true);
            radioNew.setSelected(false);
            newConfigInfo.setVisible(false);
            importConfigInfo.setVisible(true);
        }
    }
    @FXML
    protected void onCreateButtonClicked() throws IOException {
        if (radioNew.isSelected()) {
            if (checkInputAreas(false)) {
                DataExchange messageExchangePoint = DataExchange.getInstance();
                messageExchangePoint.getController().closePopUp();
                messageExchangePoint.getController().createNewProject(projectDestinationPath.getText(),projectName.getText(),false,configFileName.getText(),languageChoice.getValue().toString(),zipFilePath.getText(),null,projectArguments.getText(),expectedOutput.getText());
            }
        }
        else if (radioImport.isSelected()) {
            if (checkInputAreas(true)) {
                DataExchange messageExchangePoint = DataExchange.getInstance();
                messageExchangePoint.getController().createNewProject(projectDestinationPath.getText(),projectName.getText(),true,null,null,zipFilePath.getText(),configFilePath.getText(),null,null);
                messageExchangePoint.getController().closePopUp();
            }
        }
    }
    @FXML
    protected void onCreateButtonClickedNewConfig() throws IOException {
        if (checkInputAreasForCreateConfigFile()) {
            DataExchange messageExchangePoint = DataExchange.getInstance();
            messageExchangePoint.getController()
                    .saveFileToGivenDirectory(messageExchangePoint.getController()
                            .createJsonConfiguration(configFileName.getText(),languageChoice.getValue().toString(),projectArguments.getText(),expectedOutput.getText()),destinationPath.getText());
            messageExchangePoint.getController().closePopUp();
        }
        // TODO: Add something in here maybe later
    }

    @FXML
    protected void onSaveButtonClicked() throws IOException {
        if (checkInputAreasForEditConfigFile()) {
            DataExchange messageExchangePoint = DataExchange.getInstance();
            messageExchangePoint.getController().editJsonConfiguration(configFilePath.getText(),languageChoice.getValue().toString(),projectArguments.getText(),expectedOutput.getText());
            messageExchangePoint.getController().closePopUp();
        }
    }

    @FXML
    protected void onDeleteButtonClicked(){
        if (!configFilePathForDelete.getText().isEmpty()) {
            File file = new File(configFilePathForDelete.getText());
            DataExchange messageExchangePoint = DataExchange.getInstance();
            messageExchangePoint.getController().deleteFileOrDirectory(file);
            messageExchangePoint.getController().closePopUp();
        }
    }

