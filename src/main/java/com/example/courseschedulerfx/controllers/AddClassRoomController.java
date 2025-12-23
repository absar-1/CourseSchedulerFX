package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.ClassRoomDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddClassRoomController {

    @FXML
    private TextField roomNameField;

    @FXML
    private Spinner<Integer> capacitySpinner;

    @FXML
    private Label messageLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    private ClassRoomManagementController classRoomManagementController;

    @FXML
    public void initialize() {
        messageLabel.setText("");
        setupSpinner();
    }

    private void setupSpinner() {
        // Capacity spinner
        SpinnerValueFactory<Integer> capacityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 50);
        capacitySpinner.setValueFactory(capacityValueFactory);
        capacitySpinner.setEditable(true);

        capacitySpinner.getValueFactory().setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    int value = Integer.parseInt(string);
                    if (value >= 1 && value <= 500) {
                        return value;
                    }
                    return capacitySpinner.getValue();
                } catch (NumberFormatException e) {
                    return capacitySpinner.getValue();
                }
            }
        });
    }

    public void setClassRoomManagementController(ClassRoomManagementController controller) {
        this.classRoomManagementController = controller;
    }

    @FXML
    private void handleSave() {
        // Clear previous messages
        messageLabel.setText("");
        messageLabel.getStyleClass().removeAll("error", "success");

        // Validate fields
        String roomName = roomNameField.getText().trim();
        Integer capacity = capacitySpinner.getValue();

        if (roomName.isEmpty()) {
            showError("Please enter a classroom name");
            return;
        }

        if (roomName.length() > 50) {
            showError("Classroom name cannot exceed 50 characters");
            return;
        }

        if (capacity == null || capacity < 1 || capacity > 500) {
            showError("Capacity must be between 1 and 500");
            return;
        }

        // Check if classroom name already exists
        if (ClassRoomDAO.classroomExists(roomName)) {
            showError("Classroom with name '" + roomName + "' already exists");
            return;
        }

        try {
            boolean added = ClassRoomDAO.addClassRoom(roomName, capacity);
            if (added) {
                showSuccess("Classroom added successfully!");
                // Close the dialog after a short delay
                javafx.application.Platform.runLater(() -> {
                    Stage stage = (Stage) saveBtn.getScene().getWindow();
                    stage.close();
                    if (classRoomManagementController != null) {
                        classRoomManagementController.refreshData();
                    }
                });
            } else {
                showError("Failed to add classroom");
            }
        } catch (Exception e) {
            showError("Error adding classroom: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("success");
        messageLabel.getStyleClass().add("error");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("error");
        messageLabel.getStyleClass().add("success");
    }
}

