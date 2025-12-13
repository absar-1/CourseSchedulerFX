package com.example.courseschedulerfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;

public class HomeController {

    @FXML
    private Label headlineLabel;

    @FXML
    private Label totalTeachersLabel;

    @FXML
    private Label totalCoursesLabel;

    @FXML
    private Label totalDepartmentsLabel;

    @FXML
    private Label totalClassroomsLabel;

    private String[] headlines = {
        "📅 Welcome to Course Scheduler - Manage your courses efficiently and stay organized!",
        "🎓 Stay organized with our advanced scheduling system - Plan ahead for success!",
        "⚡ Quick view of all your courses and resources - Everything at your fingertips!",
        "✨ Optimize your course management workflow - Streamline your administrative tasks!",
        "🚀 Streamline your administrative tasks today - Experience seamless scheduling!"
    };

    private Timeline headlineAnimation;

    @FXML
    public void initialize() {
        // Concatenate all headlines with separator
        String allHeadlines = String.join("     |     ", headlines);
        headlineLabel.setText(allHeadlines);

        // Initialize with placeholder values
        totalTeachersLabel.setText("0");
        totalCoursesLabel.setText("0");
        totalDepartmentsLabel.setText("0");
        totalClassroomsLabel.setText("0");

        // Start scrolling animation after a short delay to allow label to render
        javafx.application.Platform.runLater(this::startHeadlineAnimation);
    }

    public void setHeadline(String headline) {
        headlineLabel.setText(headline);
    }

    private void startHeadlineAnimation() {
        // Stop any existing animation
        if (headlineAnimation != null) {
            headlineAnimation.stop();
        }

        // Create a smooth continuous scrolling animation
        // Duration increased to 60 seconds for smooth continuous scrolling of all 5 headlines
        headlineAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(headlineLabel.translateXProperty(), 1200.0)
            ),
            new KeyFrame(Duration.seconds(60), 
                new KeyValue(headlineLabel.translateXProperty(), -1200.0)
            )
        );

        // Set to loop infinitely
        headlineAnimation.setCycleCount(Timeline.INDEFINITE);
        headlineAnimation.setAutoReverse(false);
        headlineAnimation.play();
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





