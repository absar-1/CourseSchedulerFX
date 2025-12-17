package com.example.courseschedulerfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private VBox addAdminCard;

    @FXML
    private VBox coursesCard;

    @FXML
    private VBox batchesCard;

    @FXML
    private VBox teachersCard;

    @FXML
    private VBox classroomsCard;

    @FXML
    private VBox classSlotsCard;

    @FXML
    private VBox createScheduleCard;

    @FXML
    private VBox viewScheduleCard;


    @FXML
    public void initialize() {
        System.out.println("Dashboard initialized successfully");
    }

    // User Management
    @FXML
    public void onAddAdminClick() {
        System.out.println("Manage Admins clicked");
        navigateToUserManagement();
    }

    private void navigateToUserManagement() {
        try {
            // Find the mainContainer from the scene
            VBox parentContainer = (VBox) addAdminCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = addAdminCard.getParent();
                while (current != null) {
                    if (current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer")) {
                        parentContainer = (VBox) current;
                        break;
                    }
                    current = current.getParent();
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user_management.fxml"));
                Parent userManagementContent = loader.load();

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add user management content
                parentContainer.getChildren().add(userManagementContent);
                VBox.setVgrow(userManagementContent, Priority.ALWAYS);
                System.out.println("User Management page loaded successfully");
            } else {
                System.err.println("Could not find mainContainer");
                showFeatureAlert("Error", "Could not navigate to User Management.");
            }

        } catch (Exception e) {
            System.err.println("Error loading user management: " + e.getMessage());
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load User Management: " + e.getMessage());
        }
    }

    // Academic Structure

    @FXML
    public void onCoursesClick() {
        showFeatureAlert("Courses", "Add and manage courses with credit hours.");
        System.out.println("Courses clicked");
    }

    @FXML
    public void onBatchesClick() {
        System.out.println("Batches clicked");
        navigateToBatchManagement();
    }

    private void navigateToBatchManagement() {
        try {
            // Find the mainContainer from the scene
            VBox parentContainer = (VBox) batchesCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = batchesCard.getParent();
                while (current != null) {
                    if (current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer")) {
                        parentContainer = (VBox) current;
                        break;
                    }
                    current = current.getParent();
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/batch_management.fxml"));
                Parent batchManagementContent = loader.load();

                // Apply stylesheet to the loaded content
                var batchCss = getClass().getResource("/css/batch_management.css");
                if (batchCss != null) {
                    batchManagementContent.getStylesheets().add(batchCss.toExternalForm());
                }

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add batch management content
                parentContainer.getChildren().add(batchManagementContent);
                VBox.setVgrow(batchManagementContent, Priority.ALWAYS);
                System.out.println("Batch Management page loaded successfully");
            } else {
                System.err.println("Could not find mainContainer");
                showFeatureAlert("Error", "Could not navigate to Batch Management.");
            }

        } catch (Exception e) {
            System.err.println("Error loading batch management: " + e.getMessage());
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load Batch Management: " + e.getMessage());
        }
    }

    // People Management
    @FXML
    public void onTeachersClick() {
        showFeatureAlert("Teachers", "Manage faculty members and their department assignments.");
        System.out.println("Teachers clicked");
    }


    // Facility Management
    @FXML
    public void onClassroomsClick() {
        showFeatureAlert("Classrooms", "Manage classrooms and their seating capacities.");
        System.out.println("Classrooms clicked");
    }

    @FXML
    public void onClassSlotsClick() {
        showFeatureAlert("Time Slots", "Configure class timing slots for scheduling.");
        System.out.println("Class Slots clicked");
    }

    // Scheduling
    @FXML
    public void onCreateScheduleClick() {
        showFeatureAlert("Create Schedule", "Create weekly class schedules for batches.");
        System.out.println("Create Schedule clicked");
    }

    @FXML
    public void onViewScheduleClick() {
        showFeatureAlert("View Schedule", "View and manage existing class schedules.");
        System.out.println("View Schedule clicked");
    }


    // Helper method to show alerts (placeholder for actual navigation)
    private void showFeatureAlert(String title, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(description + "\n\nThis feature will be implemented soon!");

        var appCss = getClass().getResource("/css/app.css");
        if (appCss != null) {
            alert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
        }

        alert.showAndWait();
    }
}