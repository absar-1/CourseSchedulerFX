package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.BatchDAO;
import com.example.courseschedulerfx.model.Batch;
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

public class BatchManagementController {

    @FXML
    private Button addBatchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Batch> batchTable;

    @FXML
    private TableColumn<Batch, Integer> idColumn;

    @FXML
    private TableColumn<Batch, String> nameColumn;

    @FXML
    private TableColumn<Batch, Integer> yearColumn;

    @FXML
    private TableColumn<Batch, String> departmentColumn;

    @FXML
    private TableColumn<Batch, Integer> studentColumn;

    @FXML
    private TableColumn<Batch, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label countLabel;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private ProgressIndicator loadingIndicator;

    private ObservableList<Batch> batchList = FXCollections.observableArrayList();
    private ObservableList<Batch> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set column resize policy
        batchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        loadBatchDataAsync();
    }

    private void setupTableColumns() {
        // ID Column
        idColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getBatchID()).asObject());

        // Name Column
        nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getBatchName()));

        // Year Column
        yearColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getYearOfAdmission()).asObject());

        // Department Column
        departmentColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDepartment().getDepartmentName()));

        // Student Column
        studentColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getTotalStudents()).asObject());

        // Actions Column with Edit and Delete buttons
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox container = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("edit-btn");
                deleteBtn.getStyleClass().add("delete-btn");
                container.setAlignment(Pos.CENTER);
                editBtn.setMinWidth(50);
                deleteBtn.setMinWidth(55);

                editBtn.setOnAction(event -> {
                    Batch batch = getTableView().getItems().get(getIndex());
                    handleEditBatch(batch);
                });

                deleteBtn.setOnAction(event -> {
                    Batch batch = getTableView().getItems().get(getIndex());
                    handleDeleteBatch(batch);
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

    private void loadBatchDataAsync() {
        statusLabel.setText("Loading...");
        // Show loading overlay
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<List<Batch>> task = new Task<>() {
            @Override
            protected List<Batch> call() {
                return BatchDAO.getAllBatches();
            }
        };

        task.setOnSucceeded(event -> {
            List<Batch> batches = task.getValue();
            Platform.runLater(() -> {
                batchList.clear();
                batchList.addAll(batches);
                filteredList.setAll(batchList);
                batchTable.setItems(filteredList);
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
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load batch data: " + task.getException().getMessage());
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

    private void updateCount() {
        countLabel.setText("Total: " + filteredList.size() + " batch(es)");
    }

    @FXML
    private void handleAddBatch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_batch.fxml"));
            Parent root = loader.load();

            AddBatchController controller = loader.getController();
            controller.setBatchManagementController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Batch");

            Scene scene = new Scene(root, 800, 600);
            if (getClass().getResource("/css/batch_form.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/batch_form.css").toExternalForm());
            }

            root.applyCss();
            root.layout();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add batch form: " + e.getMessage());
        }
    }

    private void handleEditBatch(Batch batch) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_batch.fxml"));
            Parent root = loader.load();

            EditBatchController controller = loader.getController();
            controller.setBatch(batch);
            controller.setBatchManagementController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Batch");

            Scene scene = new Scene(root, 800, 600);
            if (getClass().getResource("/css/batch_form.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/batch_form.css").toExternalForm());
            }

            root.applyCss();
            root.layout();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit batch form: " + e.getMessage());
        }
    }

    private void handleDeleteBatch(Batch batch) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Batch");
        confirmAlert.setContentText("Are you sure you want to delete batch '" + batch.getBatchName() + "'?\nThis action cannot be undone.");

        // Apply CSS styling to confirmation dialog
        var appCss = getClass().getResource("/css/app.css");
        var batchFormCss = getClass().getResource("/css/batch_form.css");
        if (confirmAlert.getDialogPane() != null) {
            if (appCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (batchFormCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(batchFormCss.toExternalForm());
            }
        }

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleted = BatchDAO.deleteBatch(batch.getBatchID());
                if (deleted) {
                    statusLabel.setText("Batch deleted successfully");
                    refreshData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Batch deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete batch.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting batch: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            filteredList.setAll(batchList);
        } else {
            filteredList.clear();
            for (Batch batch : batchList) {
                if (batch.getBatchName().toLowerCase().contains(searchText) ||
                    String.valueOf(batch.getYearOfAdmission()).contains(searchText) ||
                    batch.getDepartment().getDepartmentName().toLowerCase().contains(searchText)) {
                    filteredList.add(batch);
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
        loadBatchDataAsync();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply CSS styling to alert dialog
        var appCss = getClass().getResource("/css/app.css");
        var batchFormCss = getClass().getResource("/css/batch_form.css");
        if (alert.getDialogPane() != null) {
            if (appCss != null) {
                alert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (batchFormCss != null) {
                alert.getDialogPane().getStylesheets().add(batchFormCss.toExternalForm());
            }
        }

        alert.showAndWait();
    }
}

