package com.application.bookshelf.bookshelfapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BookshelfController {
    // One HashMap to control the bookName - File Path connection
    // HashMap handles duplicates, so we don't have to check for that.
    HashMap<String, File> bookList = new HashMap<>(); //String is book's name - File is file location

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


    /* TODO: Make the code here cleaner in a later version - try making saveBookList it's own function to be called
     when necessary for imports and such */
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
        updateImages(bookList);
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
            //Now update the images in the gridpane
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

    private void updateImages(HashMap<String, File> books) {
        // TODO: Updates images in the Gridpane
        //This should update the image windwow for ever new picture added
        //First we must loop through the HashMap proportional to the size
        ImageView thumbnail = null;

        /* Try this implementation later
        Set bookImages = (Set) books.entrySet();
        Iterator loop = bookImages.iterator();
        Map booksMap = (Map) books.clone();
        Iterator loopOver = booksMap.iterator();

        int count = 0;
        while ( (loop.hasNext()) && ( count < 15 ) ) {
            Map.Entry image = booksMap.next();

        } */
        List<File> files = new ArrayList<>();
        for (File file : books.values()) {
            files.add(file);
            System.out.println(file);
        }
        File[] paths = files.toArray(new File[0]);
        Iterator loop = files.iterator();
        int count = 0;
        while ((loop.hasNext()) && (count < 15)) {
            //Create Images of what's in the array.
            BookshelfImage imageCreation = new BookshelfImage(paths[count], paths[count].toPath());
            try {
                imageCreation.thumbnail();
            } catch (IOException ioe2) {
                System.out.println("Failed at image creation.");
                ioe2.printStackTrace();
            }

            loop.next();
            count++;

        }

        //System.out.println(thumbnail.toString());
    }


}
