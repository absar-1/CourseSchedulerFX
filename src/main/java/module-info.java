module com.example.courseschedulerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.courseschedulerfx to javafx.fxml;
    opens com.example.courseschedulerfx.controllers to javafx.fxml;

    exports com.example.courseschedulerfx;
    exports com.example.courseschedulerfx.controllers;
}