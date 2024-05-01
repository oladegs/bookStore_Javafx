module com.aero.bookstore {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.aero.bookstore to javafx.fxml;
    exports com.aero.bookstore;
}