package com.example.courseschedulerfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AdminDashBoardController {
    @FXML
    private HeaderController headerController;

    @FXML
    private VBox mainContainer;

    public HeaderController getHeaderController() {
        return headerController;
    }

    @FXML
    public void initialize() {
        loadHomePage();
    }

    private void loadHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent homeContent = loader.load();

            if (mainContainer.getChildren().size() > 1) {
                mainContainer.getChildren().remove(1, mainContainer.getChildren().size());
            }

            mainContainer.getChildren().add(homeContent);
            VBox.setVgrow(homeContent, Priority.ALWAYS);

        } catch (Exception e) {
            System.err.println("Error loading home page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
