package com.application.bookshelf.bookshelfapplication;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class BookshelfController {
    // One HashMap to control the bookName - File Path connection
    // HashMap handles duplicates, so we don't have to check for that.
    HashMap<String, File> bookList = new HashMap<>(); //String is book's name - File is file location
    final int bsaHEIGHT = 150;
    final int bsaWIDTH = 220;
    private final String IMP_PATH = "./saves/";
    private final String IMG_TYPE = ".png";
    private final String LOCAL_SAVE_SEPARATOR = "[-bsa-]";
    Image[] thumbnailImages = new Image[15];

    // Proposed Size Change for the project: half of 1920 (w) by 1080 (h) - 960 x 810
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

    public void initialize() {

    }

    @FXML
    void textSearched(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println(searchField.getText());
        }
    }

    @FXML
    void addAllBooks(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Discover a Book!");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File[] selectedFile = fileChooser.showOpenMultipleDialog(new Stage()).toArray(new File[0]);
        if (selectedFile != null) {
            Thread updateImages = new Thread(() -> {
                for (int i = 0; i < selectedFile.length; i++) {
                    saveBook(selectedFile[i].getName(), selectedFile[i]);
                }
            });
            updateImages.setDaemon(true);
            updateImages.start();
        }
    }

    @FXML
    void importBooks(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load A Saved Book Shelf!");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BSA files", "*.bsa"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Thread importBooks = new Thread( () -> importBookList(selectedFile.toPath()));
            importBooks.setDaemon(true);
            importBooks.start();
        }
    }

    /* TODO: Make the code here cleaner in a later version - try making saveBookList it's own function to be called
     when necessary for imports and such */
    @FXML
    void addBook(MouseEvent event) {
        //Incorporate FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Discover a Book!");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());  // new Stage() also works here.
        // Also add the file path to a HashMap with all our book objects.
         if (selectedFile != null) {
             Thread updateImages = new Thread( () -> saveBook(selectedFile.getName(), selectedFile));
             updateImages.setDaemon(true);
             updateImages.start();
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
            writeBookList( savedBooks.toPath());
            //Now update the images in the gridpane
        // TODO: Make catches create a popup dialog that informs the issue to the user.
        } catch(SecurityException se) {
            System.out.println("Save file not created. Check your security manager to allow this program to work.");
        } catch(IOException ioe) {
            System.out.println("I/O Exception has occurred. Not sure why this happened.");
        }

    }

    private void writeBookList( Path localSave) throws IOException {
        //bookInformation will hold each entry of our hash after it's manipulated.
        String bookEntry;
        List <String> bookInformation = new ArrayList<String>();
        for (Map.Entry<String, File> books : bookList.entrySet()) {
            //Must add "f" to the end of the key because my Regex string excludes regular pdf
            bookEntry = books.getKey() +"\n" + books.getValue().toString() + "\n";
            bookInformation.add(bookEntry);
        }
        Files.write(localSave, bookInformation, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void importBookList(Path localSave) {
        try {
            //Pattern fileMatch = Pattern.compile(".\w(\[-bsa-\])\.*"); //Closest I've gotten \w(\[-bsa-\]) gives .pd no f
            //Scanner input = new Scanner(localSave).useDelimiter("\\w(\\[-bsa-\\])"); //.useDelimiter("\\w(\\[-bsa-\\])");

            BufferedReader reader = new BufferedReader(new FileReader(localSave.toFile()));
            List <String> importedInfo =reader.lines().filter(e -> !(e.equals(""))).toList();
            int file = 0;
            System.out.println("Size = " + importedInfo.size());
            //System.out.println(importedInfo);
            while(file < importedInfo.size()) {
                //Currently what it's pointing at
                bookList.put(importedInfo.get(file), new File(importedInfo.get(file+1).trim()));
                file+=2;
                printBookList();
                //updateImages(bookList);

            }
            //printBookList();
        } catch (IOException fnf) {
            System.out.println("Failed at Import Detection. Try not to delete the file while using this process.");
            fnf.printStackTrace();
        }
        updateImages(bookList);
    }

    public void printBookList() {
        for (Map.Entry<String, File> books : bookList.entrySet()) {
            System.out.println("Book Name = " + books.getKey() +
                    ", Path to file = " + books.getValue());
        }
    }

    private void updateImages(HashMap<String, File> books) {
        //This should update the image windwow for ever new picture added
        //First we must loop through the HashMap proportional to the size
        Image pdfThumbnail = null;

        // This handles updating the List into all the files and in their respective order.

        List<File> files = new ArrayList<>();
        for (File file : books.values()) {
            files.add(file);
            //System.out.println(file);
        }
        File[] paths = files.toArray(new File[0]);
        Iterator loop = files.iterator();
        int count = 0;
        while ((loop.hasNext()) && (count < 15)) { //Create Images of what's in the array.
            // Bookshelf image targets the correct book in the HashMap.
            BookshelfImage imageCreation = new BookshelfImage(paths[count], paths[count].toPath());
            try {
                imageCreation.thumbnail(); //Writes the image to a file in format .png
                //Next line creates an imageview object that can be placed into the gridpane

                //Updates the gridpane with the image of the targeted BookshelfImage
                //Get Directory of where the program launched
                String dir = System.getProperty("user.dir");
                System.out.println("Image path = " + imageCreation.getBookName().substring(0, imageCreation.getBookName().length()-4) + ".png");
                pdfThumbnail = loadImage(new FileInputStream(dir + "\\images\\" + imageCreation.getBookName()
                        .substring(0, imageCreation.getBookName().length()-4) + ".png"));
                thumbnailImages[count] = pdfThumbnail; //Add it to our overall imageview

                updateGridPane(count);
            } catch (IOException ioe2) {
                System.out.println("Failed at image creation.");
                ioe2.printStackTrace();
            }

            loop.next(); //used for moving the pointer - avoids inf loop
            count++; // avoids inf loop

        }
        updateGridPane(count);
        //System.out.println(thumbnail.toString());
    }

    private Image loadImage(InputStream pathToThumbnail) {
        return new Image(pathToThumbnail, bsaWIDTH, bsaHEIGHT, false, true);
    }

    private void updateGridPane(int count) {
        /*
        I want to ONLY update the first 15 images if size of the HashMap is larger than 15.
        I want to ONLY update the necessary images, when size is less than 15.
         -This means I want to know the size and only go out to that picture.
         -IF the size is 12, then we only go to the 12th picture.
         */
        int updateUntil = 0; //The amount of images to update in the gridpane
        if (count+1 < 15 ) {
            //Row and columns of 5 and 3
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 3; j++) {
                    if (updateUntil == count+1) {
                        return;
                    }
                    // do something here
                    // We want to get the ImageView at the Row and Column of our loop and set the image view to that from the
                    // overall ImageView array.
                    // use something.setImage here
                    selectViewInGrid(i, j, gridPane).setImage(thumbnailImages[updateUntil]);
                    updateUntil++;

                }
            }
        } else { // When updateUntil is 15 or greater.
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 3; j++) {
                    selectViewInGrid(i,j,gridPane).setImage(thumbnailImages[updateUntil]);
                    updateUntil++;
                }
            }
        }

    }

    private ImageView selectViewInGrid(int row, int column, GridPane gridPane) {
        Node imageResult = (ImageView) null;
        for (Node image : gridPane.getChildren()) {
            if (gridPane.getRowIndex(image) == row && gridPane.getColumnIndex(image) == column) {
                imageResult = (ImageView) image;
                break;
            }
        }
        return (ImageView) imageResult;
    }

}
