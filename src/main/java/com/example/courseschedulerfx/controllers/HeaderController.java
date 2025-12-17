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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.courseschedulerfx.utils.WindowManager;

import java.io.IOException;
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
        System.out.println("Logo clicked - navigating to home");
        onHomeClick();
    }

    @FXML
    public void onHomeClick() {
        System.out.println("Home clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent homeContent = loader.load();

            Parent root = logoIcon.getScene().getRoot();

            VBox mainContainer = null;
            if (root instanceof javafx.scene.layout.AnchorPane) {
                for (javafx.scene.Node child : ((javafx.scene.layout.AnchorPane) root).getChildren()) {
                    if (child instanceof VBox) {
                        mainContainer = (VBox) child;
                        break;
                    }
                }
            }

            if (mainContainer != null) {
                if (mainContainer.getChildren().size() > 1) {
                    mainContainer.getChildren().remove(1, mainContainer.getChildren().size());
                }

                mainContainer.getChildren().add(homeContent);
                javafx.scene.layout.VBox.setVgrow(homeContent, javafx.scene.layout.Priority.ALWAYS);

                System.out.println("Home page loaded successfully");
            } else {
                System.err.println("Could not find mainContainer VBox");
            }

        } catch (IOException e) {
            System.err.println("Error loading home page: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Home");
            alert.setContentText("An error occurred while loading the home page: " + e.getMessage());
            alert.showAndWait();
        }
    }

@FXML
    public void onDashboardClick() {
        System.out.println("Dashboard clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent dashboardContent = loader.load();

            Parent root = logoIcon.getScene().getRoot();

            VBox mainContainer = null;
            if (root instanceof javafx.scene.layout.AnchorPane) {
                for (javafx.scene.Node child : ((javafx.scene.layout.AnchorPane) root).getChildren()) {
                    if (child instanceof VBox) {
                        mainContainer = (VBox) child;
                        break;
                    }
                }
            }

            if (mainContainer != null) {
                if (mainContainer.getChildren().size() > 1) {
                    mainContainer.getChildren().remove(1, mainContainer.getChildren().size());
                }

                mainContainer.getChildren().add(dashboardContent);
                javafx.scene.layout.VBox.setVgrow(dashboardContent, javafx.scene.layout.Priority.ALWAYS);

                System.out.println("Dashboard loaded successfully");
            } else {
                System.err.println("Could not find mainContainer VBox");
            }

        } catch (IOException e) {
            System.err.println("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Dashboard");
            alert.setContentText("An error occurred while loading the dashboard: " + e.getMessage());
            alert.showAndWait();
        }
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

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Parent loginRoot = loader.load();

                Stage currentStage = (Stage) logoutIcon.getScene().getWindow();

                Scene loginScene = new Scene(loginRoot);

                var loginCss = getClass().getResource("/css/login.css");
                if (loginCss != null) {
                    loginScene.getStylesheets().add(loginCss.toExternalForm());
                }

                currentStage.setTitle("Login - Course Scheduler");
                currentStage.setScene(loginScene);

                // Apply normal window settings using WindowManager
                WindowManager.applyNormalWindow(currentStage, 800, 600);

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
