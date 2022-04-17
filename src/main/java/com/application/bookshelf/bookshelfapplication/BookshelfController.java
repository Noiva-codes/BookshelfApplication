package com.application.bookshelf.bookshelfapplication;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BookshelfController {
    // One HashMap to control the bookName - File Path connection
    // HashMap handles duplicates, so we don't have to check for that.
    HashMap<String, File> bookList = new HashMap<>();

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane basePane;

    //gridPane can be interacted typically by calling for the index and rows
    //of the ImageViews it contains
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
        fileChooser.setTitle("Discover a Book!");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());  // new Stage() also works here.
        // TODO: If statement - should get the path of the PDF file when selected, if not null;
        // Also add the file path to a HashMap with all our book objects.
         if (selectedFile != null) {
            saveBook(selectedFile.getName(), selectedFile);
        }
    }

    void saveBook(String bookName, File bookPath) {
        // Save Book to HashMap
        bookList.put(bookName, bookPath); //add in order of key:value;
        saveBookList();
    }

    // This should write to a file that saves all of our books on our computer.
    void saveBookList() {
        //Write to a file that saves all of our books on our computer.
        File saves = new File("/saves/"); //path for creating directory
        File savedBooks = new File("./saves/saves.bsa"); //path for creating file
        try {
            //make a directory, create a new file, and call our function to write to the new file
            saves.mkdir();
            savedBooks.createNewFile();
            //Since all things have worked, write to the file we created.
            writeBookList(savedBooks.toPath());

        // TODO: Make catches create a popup dialog that informs the issue to the user.
        } catch(SecurityException se) {
            System.out.println("Save file not created. Check your security manager to allow this program to work.");
        } catch(IOException ioe) {
            System.out.println("I/O Exception has occurred. Not sure why this happened.");
        }

    }

    private void writeBookList(Path fileToWrite) throws IOException {
        String bookEntry;
        for (Map.Entry<String, File> books : bookList.entrySet()) {
            bookEntry = books.getKey() + ":" + books.getValue().toString();
            Files.write(fileToWrite, bookEntry.getBytes());
        }

    }

    public void printBookList() {
        for (Map.Entry<String, File> books : bookList.entrySet()) {
            System.out.println("Book Name = " + books.getKey() +
                    ", Path to file = " + books.getValue());
        }
    }
}
