package com.application.bookshelf.bookshelfapplication;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ListView;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;


public class BookshelfController {
    // One HashMap to control the bookName - File Path connection
    // HashMap handles duplicates, so we don't have to check for that.
    HashMap<String, File> bookList = new HashMap<>(50); //String is book's name - File is file location
    final int BSAHEIGHT = 150;
    final int BSAWIDTH = 220;
    private final String IMP_PATH = "./saves/";
    private final String IMG_TYPE = ".png";
    Image[] thumbnailImages = new Image[15];
    ArrayList<String> suggestions = new ArrayList<String>();
    private AutoCompletionBinding<String> autoCompletionBinding;

    //We can refactor the code later to be more efficient...
    //ListView methods, updating images method

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

    @FXML
    private StackPane stackPane;

    @FXML
    private ListView<String> listViewInStack;

    public void initialize() {
    //TODO: Initialize the file directory for program, otherwise close
        //Initialize text field so it is no longer null

        //set the StackPane to invisible; default is visible.
        stackPane.setVisible(false);
        //String[] wttj = {"Welcome", " To", "The", "Jungle"};
        //listViewInStack.getItems().addAll(wttj);
        searchField.setPromptText("Search for a book you own!");
        bindTextSearched();
    }

    //Get the selection is not working currently, but we can fix this
    @FXML
    void selectBookTitle(MouseEvent event) {
        //if(listViewInStack.getItems().size() != 0)
            searchField.setText(listViewInStack.getSelectionModel().getSelectedItem());

    }

    @FXML
    void textSearched(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            //Open the book if you press enter
            //TODO: OPEN THE BOOK METHOD
            System.out.println(searchField.getText());

            //Clear the text - because we want the dropdown to go away
            searchField.setText("");
        }
    }

    @FXML
    void showDropdown(KeyEvent event) {
        // If there are words in the box, make the dropdown disappear
        if (searchField.getLength() != 0)  {
            stackPane.setVisible(true);
        } else {
            stackPane.setVisible(false);
        }

    }

    // This method will bind our text field to help autocompletion.
    private void bindTextSearched() {
        TextFields.bindAutoCompletion(searchField, suggestions);
    }

    void openBook(String bookTitle) {
        //We take the title of the book and then use that to find the path of the book
        //With this we'll be able to open the book
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

    /* TODO: Make the code here cleaner in a later version - try making saveBookList it's own function to be called
       TODO: when necessary for imports and such */
    // This should write to a file that saves all of our books on our computer.
    void saveBookList() {
        //Write to a file that saves all of our books on our computer.
        // TODO: Improve performance by calling saveBookList in initialize()
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
            // Adds the booknames to the autocomplete as well
            suggestions.add(String.valueOf(books.getValue())); //No clue why it wants me to do valueOf
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
                //Size of the hashMap should always be even, so we are safe to increment by 2 in all cases.
                suggestions.add(String.valueOf(importedInfo.get(file)));
                listViewInStack.getItems().add(importedInfo.get(file));
                file+=2;
                printBookList();
                // Breakpoint to investigate for performance
                //updateImages(bookList);

            }
            //printBookList();
        } catch (IOException fnf) {
            System.out.println("Failed at Import Detection. Do not delete the file while using this process.");
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

                //Print statements might just be the problem.

                System.out.println("Image path = " + imageCreation.getBookName().substring(0, imageCreation.getBookName().length()-4) + ".png");
                pdfThumbnail = loadImage(new FileInputStream(dir + "\\images\\" + imageCreation.getBookName()
                        .substring(0, imageCreation.getBookName().length()-4) + ".png"));
                thumbnailImages[count] = pdfThumbnail; //Add it to our overall imageview

                // If we place update here, performance becomes much slower - updateGridPane(count);
            } catch (IOException ioe2) {
                System.out.println("Failed at image creation.");
                ioe2.printStackTrace();
            }

            loop.next(); //used for moving the pointer - avoids inf loop
            count++; // avoids inf loop

        }
        // Point to investigate for performance
        updateGridPane(count);
        //System.out.println(thumbnail.toString());
    }

    private Image loadImage(InputStream pathToThumbnail) {
        return new Image(pathToThumbnail, BSAWIDTH, BSAHEIGHT, false, true);
    }

    private void updateGridPane(int count) {
        // TODO: Progress bar needs to be added to this function - progress bar incrementing.
        //UPDATED ALGORITHM
        /* Only update when all photos are captured
        Option 1:Change parameter information to included count and make recursive(?)
        Option 2: Only update based on the count provided. When internal counter for
        function equals count in parameter, then print all the images to the screen.
         */
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
