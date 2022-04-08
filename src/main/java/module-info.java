module com.application.bookshelf.bookshelfapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.application.bookshelf.bookshelfapplication to javafx.fxml;
    exports com.application.bookshelf.bookshelfapplication;
}