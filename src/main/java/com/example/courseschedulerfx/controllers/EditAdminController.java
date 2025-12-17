package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.AdminDAO;
import com.example.courseschedulerfx.model.Admin;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditAdminController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    private Admin currentAdmin;
    private UserManagementController userManagementController;

    @FXML
    public void initialize() {
        messageLabel.setText("");
    }

    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;

        // Populate fields with current data
        nameField.setText(admin.getAdminName());
        emailField.setText(admin.getEmail());
        // Password fields left empty
    }

    public void setUserManagementController(UserManagementController controller) {
        this.userManagementController = controller;
    }

    @FXML
    private void handleSave() {
        // Clear previous messages
        messageLabel.setText("");
        messageLabel.getStyleClass().removeAll("error", "success");

        // Validate fields
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation checks
        if (name.isEmpty()) {
            showError("Please enter admin name");
            return;
        }

        if (email.isEmpty()) {
            showError("Please enter email address");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        // Check if email is changed and already exists for another user
        if (!email.equals(currentAdmin.getEmail()) && AdminDAO.emailExists(email)) {
            showError("An admin with this email already exists");
            return;
        }

        // Password validation (only if password is being changed)
        String newPassword = currentAdmin.getPassword(); // Keep existing password by default
        if (!password.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }

            if (!Admin.isValidPassword(password)) {
                showError("Password must be at least 8 characters with uppercase, lowercase, digit, and special character");
                return;
            }
            newPassword = password;
        }

        try {
            // Update admin
            Admin updatedAdmin = new Admin(currentAdmin.getAdminID(), name, email, newPassword);
            boolean success = AdminDAO.updateAdmin(updatedAdmin);

            if (success) {
                showSuccess("Admin updated successfully!");

                // Refresh parent table
                if (userManagementController != null) {
                    userManagementController.refreshData();
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
                showError("Failed to update admin. Please try again.");
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
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

    private boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailPattern);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply CSS styling to alert dialog
        var appCss = getClass().getResource("/css/app.css");
        var adminFormCss = getClass().getResource("/css/admin_form.css");
        if (alert.getDialogPane() != null) {
            if (appCss != null) {
                alert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (adminFormCss != null) {
                alert.getDialogPane().getStylesheets().add(adminFormCss.toExternalForm());
            }
        }

        alert.showAndWait();
    }
}
