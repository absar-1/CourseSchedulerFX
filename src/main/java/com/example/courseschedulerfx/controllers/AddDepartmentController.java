package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.DepartmentDAO;
import com.example.courseschedulerfx.model.Department;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddDepartmentController {

    @FXML
    private TextField nameField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    private DepartmentManagementController departmentManagementController;

    @FXML
    public void initialize() {
        messageLabel.setText("");
    }

    public void setDepartmentManagementController(DepartmentManagementController controller) {
        this.departmentManagementController = controller;
    }

    @FXML
    private void handleSave() {
        String departmentName = nameField.getText().trim();

        // Validation checks
        if (departmentName.isEmpty()) {
            showError("Please enter a department name");
            return;
        }

        if (departmentName.length() < 2) {
            showError("Department name must be at least 2 characters long");
            return;
        }

        if (departmentName.length() > 100) {
            showError("Department name must be less than 100 characters");
            return;
        }

        // Check if department already exists
        if (departmentExists(departmentName)) {
            showError("A department with this name already exists");
            return;
        }

        try {
            // Create new department
            Department newDepartment = new Department(departmentName);
            boolean success = DepartmentDAO.addDepartment(newDepartment);

            if (success) {
                showSuccess("Department added successfully!");

                // Refresh parent table
                if (departmentManagementController != null) {
                    departmentManagementController.refreshData();
                }

                // Close dialog after short delay
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000);
                        closeDialog();
                    } catch (InterruptedException e) {
                        closeDialog();
                    }
                });
            } else {
                showError("Failed to add department. Please try again.");
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private boolean departmentExists(String departmentName) {
        try {
            var departments = DepartmentDAO.getAllDepartments();
            return departments.stream()
                    .anyMatch(dept -> dept.getDepartmentName().equalsIgnoreCase(departmentName));
        } catch (Exception e) {
            return false; // If we can't check, assume it doesn't exist
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
    }
}


