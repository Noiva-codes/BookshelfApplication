package com.application.bookshelf.bookshelfapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookshelfApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookshelfApp.class.getResource("bookshelfApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bookshelf Application");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}