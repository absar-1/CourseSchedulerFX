package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.ClassSlotDAO;
import com.example.courseschedulerfx.model.ClassSlot;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ClassSlotManagementController {

    @FXML
    private Button addSlotBtn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<ClassSlot> slotTable;

    @FXML
    private TableColumn<ClassSlot, Integer> idColumn;

    @FXML
    private TableColumn<ClassSlot, String> startTimeColumn;

    @FXML
    private TableColumn<ClassSlot, String> endTimeColumn;

    @FXML
    private TableColumn<ClassSlot, String> durationColumn;

    @FXML
    private TableColumn<ClassSlot, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label countLabel;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private ProgressIndicator loadingIndicator;

    private ObservableList<ClassSlot> slotList = FXCollections.observableArrayList();
    private ObservableList<ClassSlot> filteredList = FXCollections.observableArrayList();

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        // Set column resize policy
        slotTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        loadSlotDataAsync();

        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });
    }

    private void setupTableColumns() {
        // ID Column
        idColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getSlotID()).asObject());

        // Start Time Column
        startTimeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getStartTime().format(TIME_FORMATTER)));

        // End Time Column
        endTimeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getEndTime().format(TIME_FORMATTER)));

        // Duration Column
        durationColumn.setCellValueFactory(cellData -> {
            LocalTime startTime = cellData.getValue().getStartTime();
            LocalTime endTime = cellData.getValue().getEndTime();
            long minutes = java.time.temporal.ChronoUnit.MINUTES.between(startTime, endTime);
            long hours = minutes / 60;
            long mins = minutes % 60;
            String duration = String.format("%d hr %d min", hours, mins);
            return new SimpleStringProperty(duration);
        });

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
                    ClassSlot slot = getTableView().getItems().get(getIndex());
                    handleEditSlot(slot);
                });

                deleteBtn.setOnAction(event -> {
                    ClassSlot slot = getTableView().getItems().get(getIndex());
                    handleDeleteSlot(slot);
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

    private void loadSlotDataAsync() {
        statusLabel.setText("Loading...");
        // Show loading overlay
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<List<ClassSlot>> task = new Task<>() {
            @Override
            protected List<ClassSlot> call() {
                return ClassSlotDAO.getAllSlots();
            }
        };

        task.setOnSucceeded(event -> {
            List<ClassSlot> slots = task.getValue();
            Platform.runLater(() -> {
                slotList.clear();
                slotList.addAll(slots);
                filteredList.setAll(slotList);
                slotTable.setItems(filteredList);
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
                statusLabel.setText("Error loading slots");
                if (loadingOverlay != null) {
                    loadingOverlay.setVisible(false);
                    loadingOverlay.setManaged(false);
                }
            });
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    private void handleAddSlot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_class_slot.fxml"));
            Parent addSlotContent = loader.load();

            AddClassSlotController controller = loader.getController();
            controller.setManagementController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Class Slot");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(addSlotContent, 800, 600));

            // Apply CSS styling
            var css = getClass().getResource("/css/class_slot_form.css");
            if (css != null) {
                addSlotContent.getStylesheets().add(css.toExternalForm());
            }

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Slot dialog: " + e.getMessage());
        }
    }

    private void filterData(String searchText) {
        searchText = searchText.toLowerCase().trim();

        if (searchText.isEmpty()) {
            filteredList.setAll(slotList);
        } else {
            filteredList.clear();
            for (ClassSlot slot : slotList) {
                String slotInfo = slot.getSlotID() + " " +
                                slot.getStartTime().format(TIME_FORMATTER) + " " +
                                slot.getEndTime().format(TIME_FORMATTER);
                if (slotInfo.toLowerCase().contains(searchText)) {
                    filteredList.add(slot);
                }
            }
        }
        updateCount();
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadSlotDataAsync();
    }

    private void handleEditSlot(ClassSlot slot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_class_slot.fxml"));
            Parent editSlotContent = loader.load();

            EditClassSlotController controller = loader.getController();
            controller.setManagementController(this);
            controller.setSlotData(slot);

            Stage stage = new Stage();
            stage.setTitle("Edit Class Slot");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(editSlotContent, 800, 600));

            // Apply CSS styling
            var css = getClass().getResource("/css/class_slot_form.css");
            if (css != null) {
                editSlotContent.getStylesheets().add(css.toExternalForm());
            }

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Edit Slot dialog: " + e.getMessage());
        }
    }

    private void handleDeleteSlot(ClassSlot slot) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Class Slot");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete this time slot?\n" +
                "ID: " + slot.getSlotID() + "\n" +
                "Time: " + slot.getStartTime().format(TIME_FORMATTER) + " - " +
                slot.getEndTime().format(TIME_FORMATTER));

        // Apply CSS styling to the confirmation dialog
        var appCss = getClass().getResource("/css/app.css");
        if (appCss != null) {
            alert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String deleteResult = ClassSlotDAO.deleteSlot(slot.getSlotID());
            if (deleteResult == null) {
                // Success - slot was deleted
                slotList.remove(slot);
                filteredList.remove(slot);
                updateCount();
                statusLabel.setText("Slot deleted successfully");
                showAlert("Success", "Time slot deleted successfully!");
            } else {
                // Error - show the specific error message
                showAlert("Cannot Delete Time Slot", deleteResult);
            }
        }
    }

    public void refreshData() {
        loadSlotDataAsync();
    }

    private void updateCount() {
        countLabel.setText("Total: " + filteredList.size() + " slots");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);

        var appCss = getClass().getResource("/css/app.css");
        if (appCss != null) {
            alert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
        }

        alert.showAndWait();
    }
}
