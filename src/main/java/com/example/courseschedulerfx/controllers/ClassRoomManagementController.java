package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.ClassRoomDAO;
import com.example.courseschedulerfx.model.ClassRoom;
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

public class ClassRoomManagementController {

    @FXML
    private Button addClassRoomBtn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<ClassRoom> classroomTable;

    @FXML
    private TableColumn<ClassRoom, Integer> idColumn;

    @FXML
    private TableColumn<ClassRoom, String> nameColumn;

    @FXML
    private TableColumn<ClassRoom, Integer> capacityColumn;

    @FXML
    private TableColumn<ClassRoom, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label countLabel;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private ProgressIndicator loadingIndicator;

    private ObservableList<ClassRoom> classroomList = FXCollections.observableArrayList();
    private ObservableList<ClassRoom> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set column resize policy
        classroomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        loadClassRoomDataAsync();

        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });
    }

    private void setupTableColumns() {
        // ID Column
        idColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getRoomID()).asObject());

        // Name Column
        nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getRoomName()));

        // Capacity Column
        capacityColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject());

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
                    ClassRoom classroom = getTableView().getItems().get(getIndex());
                    handleEditClassRoom(classroom);
                });

                deleteBtn.setOnAction(event -> {
                    ClassRoom classroom = getTableView().getItems().get(getIndex());
                    handleDeleteClassRoom(classroom);
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

    private void loadClassRoomDataAsync() {
        statusLabel.setText("Loading...");
        // Show loading overlay
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<List<ClassRoom>> task = new Task<>() {
            @Override
            protected List<ClassRoom> call() {
                return ClassRoomDAO.getAllRooms();
            }
        };

        task.setOnSucceeded(event -> {
            List<ClassRoom> classrooms = task.getValue();
            Platform.runLater(() -> {
                classroomList.clear();
                classroomList.addAll(classrooms);
                filteredList.setAll(classroomList);
                classroomTable.setItems(filteredList);
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
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load classroom data: " + task.getException().getMessage());
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
        countLabel.setText("Total: " + filteredList.size() + " classroom(s)");
    }

    @FXML
    private void handleAddClassRoom() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_classroom.fxml"));
            Parent root = loader.load();

            AddClassRoomController controller = loader.getController();
            controller.setClassRoomManagementController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Classroom");

            Scene scene = new Scene(root, 800, 600);
            if (getClass().getResource("/css/classroom_form.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/classroom_form.css").toExternalForm());
            }

            root.applyCss();
            root.layout();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add classroom form: " + e.getMessage());
        }
    }

    private void handleEditClassRoom(ClassRoom classroom) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_classroom.fxml"));
            Parent root = loader.load();

            EditClassRoomController controller = loader.getController();
            controller.setClassRoom(classroom);
            controller.setClassRoomManagementController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Classroom");

            Scene scene = new Scene(root, 800, 600);
            if (getClass().getResource("/css/classroom_form.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/classroom_form.css").toExternalForm());
            }

            root.applyCss();
            root.layout();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit classroom form: " + e.getMessage());
        }
    }

    private void handleDeleteClassRoom(ClassRoom classroom) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Classroom");
        confirmAlert.setContentText("Are you sure you want to delete classroom '" + classroom.getRoomName() + "'?\nThis action cannot be undone.");

        // Apply CSS styling to confirmation dialog
        var appCss = getClass().getResource("/css/app.css");
        var classroomFormCss = getClass().getResource("/css/classroom_form.css");
        if (confirmAlert.getDialogPane() != null) {
            if (appCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (classroomFormCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(classroomFormCss.toExternalForm());
            }
        }

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String errorMessage = ClassRoomDAO.deleteClassRoom(classroom.getRoomID());
                if (errorMessage == null) {
                    statusLabel.setText("Classroom deleted successfully");
                    refreshData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Classroom deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", errorMessage);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting classroom: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        refreshData();
    }

    public void refreshData() {
        loadClassRoomDataAsync();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply CSS styling to alert dialog
        var appCss = getClass().getResource("/css/app.css");
        var classroomFormCss = getClass().getResource("/css/classroom_form.css");
        if (alert.getDialogPane() != null) {
            if (appCss != null) {
                alert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (classroomFormCss != null) {
                alert.getDialogPane().getStylesheets().add(classroomFormCss.toExternalForm());
            }
        }

        alert.showAndWait();
    }

    private void filterData(String searchText) {
        String lowerCaseFilter = searchText.toLowerCase();

        filteredList.setAll(classroomList.filtered(classroom ->
            classroom.getRoomName().toLowerCase().contains(lowerCaseFilter) ||
            String.valueOf(classroom.getCapacity()).contains(lowerCaseFilter)
        ));
        updateCount();
    }
}
