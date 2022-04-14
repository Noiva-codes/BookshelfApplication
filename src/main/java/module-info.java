module com.application.bookshelf.bookshelfapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires pdfbox.app;
    requires java.desktop;


    opens com.application.bookshelf.bookshelfapplication to javafx.fxml;
    exports com.application.bookshelf.bookshelfapplication;
}