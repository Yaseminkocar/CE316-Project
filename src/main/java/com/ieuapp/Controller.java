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