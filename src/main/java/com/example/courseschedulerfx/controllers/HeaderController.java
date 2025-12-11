package com.example.courseschedulerfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.example.courseschedulerfx.utils.WindowManager;

import java.util.Objects;
import java.util.Optional;

public class HeaderController {

    @FXML
    private ImageView logoIcon;

    @FXML
    private Label userLabel;

    @FXML
    private ImageView logoutIcon;

    @FXML
    public void initialize() {
        if (userLabel != null) {
            userLabel.setText("Welcome Admin");
        } else {
            System.out.println("HeaderController: userLabel is null.");
        }

        try {
            Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
            logoIcon.setImage(logoImage);
        } catch (NullPointerException e) {
            System.out.println("Logo image not found at /images/logo.png");
        }

        try {
            Image logoutImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout.png")));
            logoutIcon.setImage(logoutImage);
        } catch (NullPointerException e) {
            System.out.println("Logout image not found at /images/logout.png");
        }
    }

    public void setUserLabel(String userName) {
        if (userLabel == null) {
            System.out.println("HeaderController: userLabel is null; cannot set text.");
            return;
        }
        if (userName != null && !userName.isEmpty()) {
            userLabel.setText("Welcome, " + userName + " 👋");
        } else {
            userLabel.setText("Welcome, Guest 👋");
        }
    }

    @FXML
    public void onLogoClick() {
        System.out.println("Logo clicked");
    }

    @FXML
    public void onHomeClick() {
        System.out.println("Home clicked");
    }

    @FXML
    public void onDashboardClick() {
        System.out.println("Dashboard clicked");
    }

    @FXML
    public void onLogoutClick() {

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout Confirmation");
        confirmAlert.setHeaderText("Are you sure you want to logout?");
        confirmAlert.setContentText("You will be redirected to the login page.");


        var appCss = getClass().getResource("/css/app.css");
        if (appCss != null) {
            confirmAlert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
        }

        // Show confirmation and wait for response
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Parent loginRoot = loader.load();

                // Get current stage
                Stage currentStage = (Stage) logoutIcon.getScene().getWindow();

                // Create new scene without fixed dimensions - let it size naturally
                Scene loginScene = new Scene(loginRoot);

                var loginCss = getClass().getResource("/css/login.css");
                if (loginCss != null) {
                    loginScene.getStylesheets().add(loginCss.toExternalForm());
                }

                // Set scene
                currentStage.setTitle("Login - Course Scheduler");
                currentStage.setScene(loginScene);

                // Apply normal window settings using WindowManager
                WindowManager.applyNormalWindow(currentStage, 800, 600);

                // Show the stage
                currentStage.show();


                System.out.println("User logged out successfully.");

            } catch (Exception e) {
                System.err.println("Error loading login page: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Label getUserLabel() {
        return userLabel;
    }

    public ImageView getLogoIcon() {
        return logoIcon;
    }

    public ImageView getLogoutIcon() {
        return logoutIcon;
    }
}
