package com.application.bookshelf.bookshelfapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookshelfApp extends Application {
    BookshelfController control = new BookshelfController();
    //This line makes initialize function call
    // the default constructor - which is called on start when making this object.

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookshelfApp.class.getResource("bookshelfApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bookshelf Application");
        stage.setScene(scene);
        stage.setResizable(false);

        //control.initialize(); -- not needed because it already initializes when making a bookshelf controller obj
        stage.show();
        // Call initialize so the program sets up necessary requirements to function

    }

    public static void main(String[] args) {
        launch(args);

    }
}