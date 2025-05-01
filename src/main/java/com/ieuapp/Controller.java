package com.ieuapp;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.TreeViewSkin;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import org.json.JSONArray;
import org.json.JSONObject;

public class Controller {
    @FXML
    public TableView tableView;
    public SplitPane splitPane;
    private Stage popup;
    private Stage primaryStage;
    @FXML
    private TabPane tabPane;
    private File _InitialDirectory;
    private final ArrayList<String> _acceptedExtensions = new ArrayList<>(Arrays.asList("txt", "java", "c", "cpp", "py", "json", "csv"));
    public TreeView<FileItem> treeView;
    private File openWithFilePath;

    @FXML
    protected void onNewProjectButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("createProject.fxml"));
        DataExchange messageExchangePoint = DataExchange.getInstance();

        // Scene
        setPopup(new Stage());
        popup.initOwner(getPrimaryStage());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("New Project");
        popup.getIcons().add(new Image(new FileInputStream("img.png")));
        popup.setResizable(false);
        popup.setScene(fxmlLoader.load());
        messageExchangePoint.setUserInputController(fxmlLoader.getController());
        popup.showAndWait();
    }

    @FXML
    protected void onEditConfigButtonClicked() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editConfig.fxml"));
        DataExchange messageExchangePoint = DataExchange.getInstance();
        if (messageExchangePoint.getUserInputController() != null) {
            messageExchangePoint.setUserInputController(null);
        }

        // Scene
        setPopup(new Stage());
        popup.initOwner(getPrimaryStage());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Edit Config File");
        popup.getIcons().add(new Image(new FileInputStream("img.png")));
        popup.setResizable(false);
        popup.setScene(fxmlLoader.load());
        // This comes after load() function. The reason behind of this, if we set the controller before load it the PopupController will store null
        messageExchangePoint.setUserInputController(fxmlLoader.getController());

        if (openWithFilePath != null) {
            messageExchangePoint.getUserInputController().configFilePath.setText(openWithFilePath.getAbsolutePath());
            messageExchangePoint.getUserInputController().extractJson(openWithFilePath);
        }
        popup.showAndWait();
    }

    @FXML
    protected void onCreateConfigButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("createConfig.fxml"));
        MessageExchangePoint messageExchangePoint = MessageExchangePoint.getInstance();

        // Scene
        setPopup(new Stage());
        popup.initOwner(getPrimaryStage());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Create Configuration File");
        popup.getIcons().add(new Image(new FileInputStream("img.png")));
        popup.setResizable(false);
        popup.setScene(fxmlLoader.load());
        // This comes after load() function. The reason behind of this, if we set the controller before load it the PopupController will store null
        messageExchangePoint.setUserInputController(fxmlLoader.getController());
        popup.showAndWait();
    }

    @FXML
    protected void onDeleteConfigButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("deleteConfig.fxml"));
        MessageExchangePoint messageExchangePoint = MessageExchangePoint.getInstance();

        // Scene
        setPopup(new Stage());
        popup.initOwner(getPrimaryStage());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Delete Configuration File");
        popup.getIcons().add(new Image(new FileInputStream("img.png")));
        popup.setResizable(false);
        popup.setScene(fxmlLoader.load());
        // This comes after load() function. The reason behind of this, if we set the controller before load it the PopupController will store null
        messageExchangePoint.setUserInputController(fxmlLoader.getController());
        popup.showAndWait();
    }

    @FXML
    protected void onCloseButtonClicked() {
        treeView.setRoot(null);
        tabPane.getTabs().clear();
        tableView.getColumns().clear();
        tableView.getItems().clear();
    }

    @FXML
    protected void onQuitButtonClicked() {
        System.exit(0);
    }

    @FXML
    protected void closePopUp(){
        popup.close();// Closes the current active popup
        MessageExchangePoint messageExchangePoint = MessageExchangePoint.getInstance();
        messageExchangePoint.setUserInputController(null); // To avoid any possible conflict
    }

    protected void createNewProject(String projectDirectory, String projectName, boolean importConfig, String customConfigName, String language, String directoryThatContainsProjectZips, String configFilePath, String arguments, String expectedOutput) throws IOException {
        // Create the destination directory if it doesn't exist
        File createNewProjectDirectory = new File(projectDirectory + "\\" + projectName);
        if (!createNewProjectDirectory.exists()) {
            if(createNewProjectDirectory.mkdirs()){
                _InitialDirectory = createNewProjectDirectory;
            }
        }


        // If we have already a JSON Config File then we don't need to create one.
        if (!importConfig) {
            saveFileToGivenDirectory(createJsonConfiguration(customConfigName,language,arguments,expectedOutput),projectDirectory + "\\" + projectName);
        }
        else {
            File configFile = new File(configFilePath);
            Path sourcePath = Path.of(configFilePath);
            Path destinationPath = Path.of(projectDirectory + "\\" + projectName + "\\" + configFile.getName());
            try {
                // Perform the copy operation
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Failed to copy file: " + e.getMessage());
            }
        }

        File relocateFolderThatContainsZipFiles = new File(directoryThatContainsProjectZips);
        File[] files = relocateFolderThatContainsZipFiles.listFiles();
        assert files != null;
        for (File file: files){
            boolean renamed = file.renameTo(new File(projectDirectory + "\\" + projectName + "\\" + file.getName()));
        }

        TreeItem<FileItem> root = new TreeItem<>(new FileItem(createNewProjectDirectory.getAbsoluteFile()));
        root.setExpanded(true);
        treeView.setRoot(root);

        populateTreeView(root);// Adding all other Sub-Items to the TreeView
        addFunctionalityToTreeItems();// Adds the functionality to the TreeItems
    }

    @FXML
    protected void onOpenButtonClicked(){
        DirectoryChooser directoryChooser = new DirectoryChooser(); // To chose only Directories
        directoryChooser.setTitle("Choose Project Directory");
        directoryChooser.setInitialDirectory(new File(Paths.get("").toAbsolutePath() + "/ProjectFiles")); // Initial Path
        File selectedDirectory = directoryChooser.showDialog(new Popup()); // Popup is used to show Dialog
        if (selectedDirectory == null)// Check if any directory is selected
            return;
        _InitialDirectory = selectedDirectory.getAbsoluteFile(); // To store selected root directory

        tableView.getColumns().clear();
        tableView.getItems().clear();

        TreeItem<FileItem> root = new TreeItem<>(new FileItem(selectedDirectory.getAbsoluteFile()));
        root.setExpanded(true);
        treeView.setRoot(root);

        populateTreeView(root);// Adding all other Sub-Items to the TreeView

        addFunctionalityToTreeItems();// Adds the functionality to the TreeItems

    }

    @FXML
    protected void onExportConfigButtonClicked() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(Paths.get("").toAbsolutePath() + "/ConfigFiles"));
    }
    @FXML
    protected void onCheckButtonClicked() throws IOException {
        if (treeView == null)
            return;
        if (_InitialDirectory == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Project Not Selected", "Please open or create a project before check.");
            return;
        }
        checkOutputsOfStudents(_InitialDirectory.getAbsolutePath());
        String csvFilePath = _InitialDirectory.getAbsolutePath() + "/StudentResults.csv";

        tableView.getItems().clear();
        tableView.getColumns().clear();
        TableColumn<Student, String> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Student, Boolean> resultColumn = new TableColumn<>("Result");
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        resultColumn.setCellFactory(column -> new TextFieldTableCell<>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "✔" : "❌");
                }
            }
        });

        tableView.getColumns().addAll(idColumn, resultColumn);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    boolean result = (parts[1].equals("Match"));
                    tableView.getItems().add(new Student(parts[0], result));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshTreeView();
    }

    // ContextMenu that will contain options
    private ContextMenu treeViewContextMenu;
    private void addFunctionalityToTreeItems(){

        // Other functionalities like is clicked element is a file or folder, handled in redFile() function
        // TODO: Selected Files shouldn't be able to open at multiple tabs
        treeView.setOnMouseClicked(event -> {

            if (treeViewContextMenu != null) {
                treeViewContextMenu.hide();
            }

            // Define the regex pattern to match content inside single quotes
            Pattern pattern = Pattern.compile("'null'");

            // Create a matcher object
            Matcher matcher = pattern.matcher(event.getTarget().toString());

            if (matcher.find()) {
                treeView.getSelectionModel().clearSelection();
                return;
            }

            // To detect double-click on TreeView
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                TreeItem<FileItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getValue() != null) {
                    if (readFile(selectedItem.getValue().file()))
                        openTabWithFileData(selectedItem.getValue().toString());
                }
            }

            if (event.getButton() == MouseButton.SECONDARY) {

                TreeItem<FileItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getValue().file().isFile()){
                    ContextMenu contextMenu = contextMenuBuilder(selectedItem.getValue().toString().split("\\.")[1],selectedItem.getValue().file().isFile(),selectedItem);
                    if (contextMenu == null) {
                        return;
                    }
                    treeViewContextMenu = contextMenu;
                    contextMenu.show(treeView, event.getScreenX(), event.getScreenY());
                } else if (selectedItem != null && !selectedItem.getValue().file().isFile()) {
                    ContextMenu contextMenu = contextMenuBuilder(null,selectedItem.getValue().file().isFile(),selectedItem);
                    if (contextMenu == null) {
                        return;
                    }
                    treeViewContextMenu = contextMenu;
                    contextMenu.show(treeView, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }



