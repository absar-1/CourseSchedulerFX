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
    private VBox departmentsCard;

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
    private VBox createSpecialScheduleCard;


    @FXML
    public void initialize() {
    }

    // User Management
    @FXML
    public void onAddAdminClick() {
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
            } else {
                showFeatureAlert("Error", "Could not navigate to User Management.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load User Management: " + e.getMessage());
        }
    }

    // Academic Structure

    @FXML
    public void onCoursesClick() {
        showFeatureAlert("Courses", "Add and manage courses with credit hours.");
    }

    @FXML
    public void onBatchesClick() {
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
            } else {
                showFeatureAlert("Error", "Could not navigate to Batch Management.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load Batch Management: " + e.getMessage());
        }
    }

    // Department Management
    @FXML
    public void onDepartmentsClick() {
        navigateToDepartmentManagement();
    }

    private void navigateToDepartmentManagement() {
        try {
            // Find the mainContainer from the scene
            VBox parentContainer = (VBox) departmentsCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = departmentsCard.getParent();
                while (current != null) {
                    if (current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer")) {
                        parentContainer = (VBox) current;
                        break;
                    }
                    current = current.getParent();
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/department_management.fxml"));
                Parent departmentManagementContent = loader.load();

                // Apply stylesheet to the loaded content
                var departmentCss = getClass().getResource("/css/department_management.css");
                if (departmentCss != null) {
                    departmentManagementContent.getStylesheets().add(departmentCss.toExternalForm());
                }

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add department management content
                parentContainer.getChildren().add(departmentManagementContent);
                VBox.setVgrow(departmentManagementContent, Priority.ALWAYS);
            } else {
                showFeatureAlert("Error", "Could not navigate to Department Management.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load Department Management: " + e.getMessage());
        }
    }

    // People Management
    @FXML
    public void onTeachersClick() {
        showFeatureAlert("Teachers", "Manage faculty members and their department assignments.");
    }


    // Facility Management
    @FXML
    public void onClassroomsClick() {
        navigateToClassroomManagement();
    }

    private void navigateToClassroomManagement() {
        try {
            // Find the mainContainer from the scene
            VBox parentContainer = (VBox) classroomsCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = classroomsCard.getParent();
                while (current != null) {
                    if (current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer")) {
                        parentContainer = (VBox) current;
                        break;
                    }
                    current = current.getParent();
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/classroom_management.fxml"));
                Parent classroomManagementContent = loader.load();

                // Apply stylesheet to the loaded content
                var classroomCss = getClass().getResource("/css/classroom_management.css");
                if (classroomCss != null) {
                    classroomManagementContent.getStylesheets().add(classroomCss.toExternalForm());
                }

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add classroom management content
                parentContainer.getChildren().add(classroomManagementContent);
                VBox.setVgrow(classroomManagementContent, Priority.ALWAYS);
            } else {
                showFeatureAlert("Error", "Could not navigate to Classroom Management.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load Classroom Management: " + e.getMessage());
        }
    }

    @FXML
    public void onClassSlotsClick() {
        navigateToClassSlotManagement();
    }

    private void navigateToClassSlotManagement() {
        try {
            // Find the mainContainer from the scene
            VBox parentContainer = (VBox) classSlotsCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = classSlotsCard.getParent();
                while (current != null) {
                    if (current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer")) {
                        parentContainer = (VBox) current;
                        break;
                    }
                    current = current.getParent();
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/class_slot_management.fxml"));
                Parent classSlotManagementContent = loader.load();

                // Apply stylesheet to the loaded content
                var classSlotCss = getClass().getResource("/css/class_slot_management.css");
                if (classSlotCss != null) {
                    classSlotManagementContent.getStylesheets().add(classSlotCss.toExternalForm());
                }

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add class slot management content
                parentContainer.getChildren().add(classSlotManagementContent);
                VBox.setVgrow(classSlotManagementContent, Priority.ALWAYS);
            } else {
                showFeatureAlert("Error", "Could not navigate to Class Slot Management.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load Class Slot Management: " + e.getMessage());
        }
    }

    // Scheduling
    @FXML
    public void onCreateScheduleClick() {
        navigateToScheduleManagement();
    }

    @FXML
    public void onViewScheduleClick() {
        navigateToScheduleManagement();
    }

    @FXML
    public void onCreateSpecialScheduleClick() {
        navigateToSpecialScheduleManagement();
    }

    private void navigateToScheduleManagement() {
        try {
            // Find the mainContainer from the scene
            VBox parentContainer = (VBox) createScheduleCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = createScheduleCard.getParent();
                while (current != null) {
                    if (current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer")) {
                        parentContainer = (VBox) current;
                        break;
                    }
                    current = current.getParent();
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/schedule_management.fxml"));
                Parent scheduleManagementContent = loader.load();

                // Apply stylesheet to the loaded content
                var scheduleCss = getClass().getResource("/css/schedule_management.css");
                if (scheduleCss != null) {
                    scheduleManagementContent.getStylesheets().add(scheduleCss.toExternalForm());
                }

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add schedule management content
                parentContainer.getChildren().add(scheduleManagementContent);
                VBox.setVgrow(scheduleManagementContent, Priority.ALWAYS);
            } else {
                showFeatureAlert("Error", "Could not navigate to Schedule Management.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load Schedule Management: " + e.getMessage());
        }
    }

    private void navigateToSpecialScheduleManagement() {
        try {
            // Find the mainContainer from the scene
            VBox parentContainer = (VBox) createSpecialScheduleCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = createSpecialScheduleCard.getParent();
                while (current != null) {
                    if (current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer")) {
                        parentContainer = (VBox) current;
                        break;
                    }
                    current = current.getParent();
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/special_schedule_management.fxml"));
                Parent specialScheduleManagementContent = loader.load();

                // Apply stylesheet to the loaded content
                var specialScheduleCss = getClass().getResource("/css/special_schedule_management.css");
                if (specialScheduleCss != null) {
                    specialScheduleManagementContent.getStylesheets().add(specialScheduleCss.toExternalForm());
                }

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add special schedule management content
                parentContainer.getChildren().add(specialScheduleManagementContent);
                VBox.setVgrow(specialScheduleManagementContent, Priority.ALWAYS);
            } else {
                showFeatureAlert("Error", "Could not navigate to Special Schedule Management.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showFeatureAlert("Error", "Failed to load Special Schedule Management: " + e.getMessage());
        }
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


