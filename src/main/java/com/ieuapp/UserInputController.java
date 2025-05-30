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

    private boolean checkInputAreas(boolean importConfig) {
        if (importConfig) {
            return !projectName.getText().isEmpty() && !configFilePath.getText().isEmpty() && !projectDestinationPath.getText().isEmpty() && !zipFilePath.getText().isEmpty();
        }
        else return !projectName.getText().isEmpty() && !projectDestinationPath.getText().isEmpty() && !expectedOutput.getText().isEmpty() && !zipFilePath.getText().isEmpty();
    }

    private boolean checkInputAreasForCreateConfigFile() {
        return !configFileName.getText().isEmpty() && !expectedOutput.getText().isEmpty() && !destinationPath.getText().isEmpty();
    }

    private boolean checkInputAreasForEditConfigFile() {
        return !configFilePath.getText().isEmpty() && !expectedOutput.getText().isEmpty();
    }
    @FXML
    protected void onExploreButtonClicked(ActionEvent event){
        if (event.getSource() == configFilePathButton) {
            File file = get_JSONFilePath();
            if (file != null) {
                configFilePath.setText(file.getAbsolutePath());
            }
            else System.out.println("File not found!");
        } else if (event.getSource() == projectDestinationPathButton) {
            File file = get_InitialDirectory("/ProjectFiles");
            if (file != null) {
                projectDestinationPath.setText(file.getAbsolutePath());
            }
            else System.out.println("File not found!");
        } else if (event.getSource() == zipFilePathButton) {
            File file = get_InitialDirectory("");
            if (file != null) {
                zipFilePath.setText(file.getAbsolutePath());
            }
            else System.out.println("File not found!");
        }
        else if (event.getSource() == destinationPathButton) {
            File file = get_InitialDirectory("/ConfigFiles");
            if (file != null) {
                destinationPath.setText(file.getAbsolutePath());
            } else System.out.println("File not found!");
        } else if (event.getSource() == configFilePathEditButton) {
            File file = get_JSONFilePath();
            if (file != null) {
                configFilePath.setText(file.getAbsolutePath());
                extractJson(file);
            }
            else System.out.println("File not found!");
        } else if (event.getSource() == configFilePathDeleteButton) {
            File file = get_JSONFilePath();
            if (file != null) { configFilePathForDelete.setText(file.getAbsolutePath());
            } else System.out.println("File not found!");
        }

    }
    private File get_InitialDirectory(String folderName) {
        DirectoryChooser directoryChooser = new DirectoryChooser(); // To chose only Directories
        directoryChooser.setTitle("Choose Directory");
        // Initial Path
        directoryChooser.setInitialDirectory(new File(Paths.get("").toAbsolutePath() + folderName));
        return directoryChooser.showDialog(new Popup());
    }

    private File get_JSONFilePath() {
        FileChooser fileChooser = new FileChooser(); // To chose only Directories
        fileChooser.setTitle("Choose Configuration File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialDirectory(new File(Paths.get("").toAbsolutePath() + "/ConfigFiles")); // Initial Path
        return fileChooser.showOpenDialog(new Popup());
    }

    private File get_ZipDirectory() {
        FileChooser fileChooser = new FileChooser(); // To chose only Directories
        fileChooser.setTitle("Choose Zip File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip Files", "*.zip"));
        return fileChooser.showOpenDialog(new Popup());
    }

    protected void extractJson(File file){
        if (configFilePath.getText().isEmpty()){
            return;
        }

        String jsonText ;
        try {
            jsonText = new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = new JSONObject(jsonText);

        JSONObject compilerConfig = jsonObject.getJSONObject("compilerConfig");
        String language = compilerConfig.getString("language");
        String compileCommand = compilerConfig.getString("compileCommand");
        String rCommand = compilerConfig.getString("runCommand");

        JSONObject projectConfig = jsonObject.getJSONObject("projectConfig");
        JSONArray arguments = projectConfig.getJSONArray("argument");
        String expectedOut = projectConfig.getString("expectedOutput");

        String argumentsToStr = "";
        for (int i = 0; i < arguments.length(); i++) {
            argumentsToStr += arguments.getString(i);
            if (i != arguments.length()-1)
                argumentsToStr+=",";
        }

        editConfigVBox.setVisible(true);
        languageChoice.setValue(language);
        projectArguments.setText(argumentsToStr);
        expectedOutput.setText(expectedOut);

    }
}