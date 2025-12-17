package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.AdminDAO;
import com.example.courseschedulerfx.model.Admin;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class UserManagementController {

    @FXML
    private Button addAdminBtn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Admin> adminTable;

    @FXML
    private TableColumn<Admin, Integer> idColumn;

    @FXML
    private TableColumn<Admin, String> nameColumn;

    @FXML
    private TableColumn<Admin, String> emailColumn;

    @FXML
    private TableColumn<Admin, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label countLabel;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private ProgressIndicator loadingIndicator;

    private ObservableList<Admin> adminList = FXCollections.observableArrayList();
    private ObservableList<Admin> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set column resize policy
        adminTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        loadAdminDataAsync();
    }

    private void setupTableColumns() {
        // ID Column
        idColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getAdminID()).asObject());

        // Name Column
        nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getAdminName()));

        // Email Column
        emailColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getEmail()));

        // Actions Column with Edit and Delete buttons
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox container = new HBox(8, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("edit-btn");
                deleteBtn.getStyleClass().add("delete-btn");
                container.setAlignment(Pos.CENTER);

                editBtn.setOnAction(event -> {
                    Admin admin = getTableView().getItems().get(getIndex());
                    handleEditAdmin(admin);
                });

                deleteBtn.setOnAction(event -> {
                    Admin admin = getTableView().getItems().get(getIndex());
                    handleDeleteAdmin(admin);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void loadAdminDataAsync() {
        statusLabel.setText("Loading...");
        // Show loading overlay
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<List<Admin>> task = new Task<>() {
            @Override
            protected List<Admin> call() {
                return AdminDAO.getAllAdmins();
            }
        };

        task.setOnSucceeded(event -> {
            List<Admin> admins = task.getValue();
            Platform.runLater(() -> {
                adminList.clear();
                adminList.addAll(admins);
                filteredList.setAll(adminList);
                adminTable.setItems(filteredList);
                updateCount();
                statusLabel.setText("Ready");
                // Hide loading overlay
                if (loadingOverlay != null) {
                    loadingOverlay.setVisible(false);
                    loadingOverlay.setManaged(false);
                }
            });
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                statusLabel.setText("Error loading data");
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load admin data: " + task.getException().getMessage());
                // Hide loading overlay
                if (loadingOverlay != null) {
                    loadingOverlay.setVisible(false);
                    loadingOverlay.setManaged(false);
                }
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void loadAdminData() {
        statusLabel.setText("Loading...");
        try {
            List<Admin> admins = AdminDAO.getAllAdmins();
            adminList.clear();
            adminList.addAll(admins);
            filteredList.setAll(adminList);
            adminTable.setItems(filteredList);
            updateCount();
            statusLabel.setText("Ready");
        } catch (Exception e) {
            statusLabel.setText("Error loading data");
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load admin data: " + e.getMessage());
        }
    }

    private void updateCount() {
        countLabel.setText("Total: " + filteredList.size() + " admin(s)");
    }

    @FXML
    private void handleAddAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_admin.fxml"));
            Parent root = loader.load();

            AddAdminController controller = loader.getController();
            controller.setUserManagementController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Admin");

            Scene scene = new Scene(root, 800, 600);
            if (getClass().getResource("/css/admin_form.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/admin_form.css").toExternalForm());
            }

            root.applyCss();
            root.layout();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add admin form: " + e.getMessage());
        }
    }

    private void handleEditAdmin(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_admin.fxml"));
            Parent root = loader.load();

            EditAdminController controller = loader.getController();
            controller.setAdmin(admin);
            controller.setUserManagementController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Admin");

            Scene scene = new Scene(root, 800, 600);
            if (getClass().getResource("/css/admin_form.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/admin_form.css").toExternalForm());
            }

            root.applyCss();
            root.layout();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit admin form: " + e.getMessage());
        }
    }

    private void handleDeleteAdmin(Admin admin) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Administrator");
        confirmAlert.setContentText("Are you sure you want to delete admin '" + admin.getAdminName() + "'?\nThis action cannot be undone.");

        // Apply CSS styling to confirmation dialog
        var appCss = getClass().getResource("/css/app.css");
        var adminFormCss = getClass().getResource("/css/admin_form.css");
        if (confirmAlert.getDialogPane() != null) {
            if (appCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (adminFormCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(adminFormCss.toExternalForm());
            }
        }

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleted = AdminDAO.deleteAdmin(admin.getAdminID());
                if (deleted) {
                    statusLabel.setText("Admin deleted successfully");
                    refreshData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Admin deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete admin.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting admin: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            filteredList.setAll(adminList);
        } else {
            filteredList.clear();
            for (Admin admin : adminList) {
                if (admin.getAdminName().toLowerCase().contains(searchText) ||
                    admin.getEmail().toLowerCase().contains(searchText)) {
                    filteredList.add(admin);
                }
            }
        }
        updateCount();
        statusLabel.setText("Search completed");
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        refreshData();
    }

    public void refreshData() {
        loadAdminDataAsync();
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

