package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.AdminDAO;
import com.example.courseschedulerfx.model.Admin;
import com.example.courseschedulerfx.utils.WindowManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private ImageView loginImage;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private StackPane loaderOverlay;

    @FXML
    private ProgressIndicator loader;

    @FXML
    private void initialize() {
        try {
            var imageStream = getClass().getResourceAsStream("/images/login.png");
            if (imageStream != null) {
                Image loginImageFile = new Image(imageStream);
                loginImage.setImage(loginImageFile);
            } else {

            }
        } catch (Exception e) {

        }

        loginButton.setOnAction(this::handleLoginButtonAction);

        errorLabel.setText("");
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        errorLabel.setText("");

        String email = emailTextField.getText().trim();
        String password = passwordTextField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password");
            return;
        }


        loaderOverlay.setVisible(true);
        loginButton.setDisable(true);

        // Run database validation in background thread to not block UI
        new Thread(() -> {
            try {
                Admin admin = AdminDAO.validateAdmin(email, password);

                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    if (admin != null) {
                        errorLabel.setStyle("-fx-text-fill: green;");
                        errorLabel.setText("Login successful! Welcome " + admin.getAdminName());

                        navigateToAdminDashboard(admin);
                    } else {
                        loaderOverlay.setVisible(false);
                        loginButton.setDisable(false);
                        errorLabel.setText("Invalid email or password");
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    loaderOverlay.setVisible(false);
                    loginButton.setDisable(false);
                    errorLabel.setText("Login failed: " + e.getMessage());

                });
            }
        }).start();
    }

    private void navigateToAdminDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);

            var headerCssResource = getClass().getResource("/css/header.css");
            if (headerCssResource != null) {
                dashboardScene.getStylesheets().add(headerCssResource.toExternalForm());
            }

            Stage currentStage = (Stage) loginButton.getScene().getWindow();

            AdminDashBoardController dashboardController = loader.getController();
            HeaderController headerController = dashboardController.getHeaderController();

            if (headerController != null) {
                headerController.setUserLabel(admin.getAdminName());
            } else {

            }

            // Unmaximize current stage first if it was maximized
            if (currentStage.isMaximized()) {
                currentStage.setMaximized(false);
            }

            currentStage.setTitle("Admin Dashboard");
            currentStage.setScene(dashboardScene);

            dashboardRoot.applyCss();
            dashboardRoot.layout();

            currentStage.show();

            // Use Platform.runLater to maximize after the scene is fully rendered
            Platform.runLater(() -> {
                dashboardRoot.applyCss();
                dashboardRoot.layout();
                
                // Now maximize the window
                WindowManager.applyDefaultWindow(currentStage);
                
                // Final layout pass after maximizing
                Platform.runLater(() -> {
                    dashboardRoot.applyCss();
                    dashboardRoot.layout();
                    
                    // Hide loader overlay after everything is complete
                    loaderOverlay.setVisible(false);
                });
            });

        } catch (Exception e) {
            errorLabel.setText("Error loading dashboard: " + e.getMessage());

        }
    }
}

