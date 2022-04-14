package com.application.bookshelf.bookshelfapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class BookshelfController {


    @FXML
    private Button addButton;

    @FXML
    private AnchorPane basePane;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView img1;

    @FXML
    private Button importButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchField;

    @FXML
    void addBook(MouseEvent event) {
        // FileChooser beginnings right there - review FileChooser when stuck
        //Stage mainStage = (Stage) basePane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                // TODO: Change Extension filter to pdf files only
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
         // new Stage() also works here.
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        // TODO: If statement - should get the path of the PDF file when selected, if not null;
        // Also add the file path to a list with all our book objects.
        /* if (selectedFile != null) {
            mainStage.display(selectedFile);
        } */
    }

}
