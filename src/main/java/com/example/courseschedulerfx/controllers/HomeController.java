package com.example.courseschedulerfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {

    @FXML
    private Label totalTeachersLabel;

    @FXML
    private Label totalCoursesLabel;

    @FXML
    private Label totalDepartmentsLabel;

    @FXML
    private Label totalClassroomsLabel;

    @FXML
    public void initialize() {
        // Initialize with placeholder values
        totalTeachersLabel.setText("0");
        totalCoursesLabel.setText("0");
        totalDepartmentsLabel.setText("0");
        totalClassroomsLabel.setText("0");
    }

    public void setTotalTeachers(int count) {
        totalTeachersLabel.setText(String.valueOf(count));
    }

    public void setTotalCourses(int count) {
        totalCoursesLabel.setText(String.valueOf(count));
    }

    public void setTotalDepartments(int count) {
        totalDepartmentsLabel.setText(String.valueOf(count));
    }

    public void setTotalClassrooms(int count) {
        totalClassroomsLabel.setText(String.valueOf(count));
    }
}





