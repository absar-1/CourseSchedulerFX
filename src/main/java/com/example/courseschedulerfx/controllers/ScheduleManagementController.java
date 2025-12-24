package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.*;
import com.example.courseschedulerfx.model.*;
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

public class ScheduleManagementController {

    @FXML
    private Button addScheduleBtn;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private ComboBox<String> filterValueCombo;

    @FXML
    private TableView<Schedule> scheduleTable;

    @FXML
    private TableColumn<Schedule, Integer> idColumn;

    @FXML
    private TableColumn<Schedule, String> dayColumn;

    @FXML
    private TableColumn<Schedule, String> timeColumn;

    @FXML
    private TableColumn<Schedule, String> courseColumn;

    @FXML
    private TableColumn<Schedule, String> teacherColumn;

    @FXML
    private TableColumn<Schedule, String> batchColumn;

    @FXML
    private TableColumn<Schedule, String> roomColumn;

    @FXML
    private TableColumn<Schedule, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label countLabel;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private ProgressIndicator loadingIndicator;

    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();
    private ObservableList<Schedule> filteredList = FXCollections.observableArrayList();
    private Stage formStage = null;

    @FXML
    public void initialize() {
        // Set column resize policy
        scheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupFilterCombo();
        setupTableColumns();
        loadScheduleDataAsync();

        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });
    }

    private void setupFilterCombo() {
        ObservableList<String> filterOptions = FXCollections.observableArrayList(
                "None",
                "Day",
                "Teacher",
                "Course",
                "Batch"
        );
        filterCombo.setItems(filterOptions);
        filterCombo.setValue("None");

        filterCombo.setOnAction(event -> updateFilterValues());

        // Add listener to filterValueCombo to trigger filtering when a value is selected
        filterValueCombo.setOnAction(event -> filterData(searchField.getText()));
    }

    private void updateFilterValues() {
        filterValueCombo.getItems().clear();
        String selectedFilter = filterCombo.getValue();

        if ("Day".equals(selectedFilter)) {
            ObservableList<String> days = FXCollections.observableArrayList(
                    "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
            );
            filterValueCombo.setItems(days);
            filterValueCombo.setPromptText("Select day...");
        } else if ("Teacher".equals(selectedFilter)) {
            List<Teacher> teachers = getAvailableTeachers();
            ObservableList<String> teacherNames = FXCollections.observableArrayList();
            for (Teacher t : teachers) {
                teacherNames.add(t.getTeacherName() + " (ID: " + t.getTeacherID() + ")");
            }
            filterValueCombo.setItems(teacherNames);
            filterValueCombo.setPromptText("Select teacher...");
        } else if ("Course".equals(selectedFilter)) {
            List<Course> courses = getAvailableCourses();
            ObservableList<String> courseNames = FXCollections.observableArrayList();
            for (Course c : courses) {
                courseNames.add(c.getCourseTitle() + " (ID: " + c.getCourseID() + ")");
            }
            filterValueCombo.setItems(courseNames);
            filterValueCombo.setPromptText("Select course...");
        } else if ("Batch".equals(selectedFilter)) {
            List<Batch> batches = getAvailableBatches();
            ObservableList<String> batchNames = FXCollections.observableArrayList();
            for (Batch b : batches) {
                batchNames.add(b.getBatchName() + " (ID: " + b.getBatchID() + ")");
            }
            filterValueCombo.setItems(batchNames);
            filterValueCombo.setPromptText("Select batch...");
        } else {
            filterValueCombo.getItems().clear();
            filterValueCombo.setPromptText("Select...");
        }
    }

    private List<Teacher> getAvailableTeachers() {
        try {

            List<Schedule> allSchedules = ScheduleDAO.getAllSchedules();

            List<Teacher> teachers = new java.util.ArrayList<>();
            for (Schedule s : allSchedules) {
                if (s.getTeacher() != null) {
                    boolean found = false;
                    for (Teacher t : teachers) {
                        if (t.getTeacherID() == s.getTeacher().getTeacherID()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        teachers.add(s.getTeacher());
                    }
                }
            }

            return teachers;
        } catch (Exception e) {

            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<Course> getAvailableCourses() {
        try {

            List<Schedule> allSchedules = ScheduleDAO.getAllSchedules();
            List<Course> courses = new java.util.ArrayList<>();
            for (Schedule s : allSchedules) {
                if (s.getCourse() != null) {
                    boolean found = false;
                    for (Course c : courses) {
                        if (c.getCourseID() == s.getCourse().getCourseID()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        courses.add(s.getCourse());
                    }
                }
            }

            return courses;
        } catch (Exception e) {

            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<Batch> getAvailableBatches() {
        try {

            List<Schedule> allSchedules = ScheduleDAO.getAllSchedules();
            List<Batch> batches = new java.util.ArrayList<>();
            for (Schedule s : allSchedules) {
                if (s.getBatch() != null) {
                    boolean found = false;
                    for (Batch b : batches) {
                        if (b.getBatchID() == s.getBatch().getBatchID()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        batches.add(s.getBatch());
                    }
                }
            }

            return batches;
        } catch (Exception e) {

            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private void setupTableColumns() {
        // ID Column
        idColumn.setCellValueFactory(cellData -> {
            try {
                Schedule schedule = cellData.getValue();
                if (schedule != null) {
                    return new SimpleIntegerProperty(schedule.getScheduleID()).asObject();
                }
            } catch (Exception e) {

            }
            return new SimpleIntegerProperty(0).asObject();
        });

        // Day Column
        dayColumn.setCellValueFactory(cellData -> {
            try {
                Schedule schedule = cellData.getValue();
                if (schedule != null) {
                    return new SimpleStringProperty(schedule.getDayOfWeek());
                }
            } catch (Exception e) {

            }
            return new SimpleStringProperty("");
        });

        // Time Column
        timeColumn.setCellValueFactory(cellData -> {
            try {
                Schedule schedule = cellData.getValue();
                if (schedule != null && schedule.getClassSlot() != null) {
                    ClassSlot slot = schedule.getClassSlot();
                    return new SimpleStringProperty(slot.getStartTime() + " - " + slot.getEndTime());
                }
            } catch (Exception e) {

            }
            return new SimpleStringProperty("");
        });

        // Course Column
        courseColumn.setCellValueFactory(cellData -> {
            try {
                Schedule schedule = cellData.getValue();
                if (schedule != null && schedule.getCourse() != null) {
                    return new SimpleStringProperty(schedule.getCourse().getCourseTitle());
                }
            } catch (Exception e) {

            }
            return new SimpleStringProperty("");
        });

        // Teacher Column
        teacherColumn.setCellValueFactory(cellData -> {
            try {
                Schedule schedule = cellData.getValue();
                if (schedule != null && schedule.getTeacher() != null) {
                    return new SimpleStringProperty(schedule.getTeacher().getTeacherName());
                }
            } catch (Exception e) {

            }
            return new SimpleStringProperty("");
        });

        // Batch Column
        batchColumn.setCellValueFactory(cellData -> {
            try {
                Schedule schedule = cellData.getValue();
                if (schedule != null && schedule.getBatch() != null) {
                    return new SimpleStringProperty(schedule.getBatch().getBatchName());
                }
            } catch (Exception e) {

            }
            return new SimpleStringProperty("");
        });

        // Room Column
        roomColumn.setCellValueFactory(cellData -> {
            try {
                Schedule schedule = cellData.getValue();
                if (schedule != null && schedule.getClassRoom() != null) {
                    return new SimpleStringProperty(schedule.getClassRoom().getRoomName());
                }
            } catch (Exception e) {

            }
            return new SimpleStringProperty("");
        });


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
                    try {
                        Schedule schedule = getTableView().getItems().get(getIndex());
                        if (schedule != null) {
                            handleEditSchedule(schedule);
                        }
                    } catch (Exception e) {

                    }
                });

                deleteBtn.setOnAction(event -> {
                    try {
                        Schedule schedule = getTableView().getItems().get(getIndex());
                        if (schedule != null) {
                            handleDeleteSchedule(schedule);
                        }
                    } catch (Exception e) {

                    }
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

    private void loadScheduleDataAsync() {
        statusLabel.setText("Loading...");
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<List<Schedule>> task = new Task<>() {
            @Override
            protected List<Schedule> call() {
                try {

                    List<Schedule> schedules = ScheduleDAO.getAllSchedules();

                    return schedules;
                } catch (Exception e) {

                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };

        task.setOnSucceeded(event -> {
            List<Schedule> schedules = task.getValue();
            Platform.runLater(() -> {
                try {
                    scheduleList.clear();
                    scheduleList.addAll(schedules);
                    filteredList.setAll(scheduleList);
                    scheduleTable.setItems(filteredList);
                    updateCount();

                    if (schedules.isEmpty()) {
                        statusLabel.setText("No schedules found in database");
                    } else {
                        statusLabel.setText("Ready - " + schedules.size() + " schedule(s) loaded");
                    }

                    if (loadingOverlay != null) {
                        loadingOverlay.setVisible(false);
                        loadingOverlay.setManaged(false);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    statusLabel.setText("Error updating display");
                }
            });
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                Throwable exception = task.getException();

                exception.printStackTrace();
                statusLabel.setText("Error loading data");
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load schedule data: " + exception.getMessage());
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
        countLabel.setText("Total: " + filteredList.size() + " schedule(s)");
    }

    @FXML
    private void handleAddSchedule() {


        // Show loading overlay
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<Parent> loadTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_schedule.fxml"));
                Parent root = loader.load();


                AddScheduleController controller = loader.getController();

                controller.setScheduleManagementController(ScheduleManagementController.this);


                return root;
            }
        };

        loadTask.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                try {
                    Parent root = loadTask.getValue();

                    formStage = new Stage();
                    formStage.initModality(Modality.APPLICATION_MODAL);
                    formStage.setTitle("Add New Schedule");

                    Scene scene = new Scene(root, 800, 600);
                    if (getClass().getResource("/css/schedule_form.css") != null) {
                        scene.getStylesheets().add(getClass().getResource("/css/schedule_form.css").toExternalForm());
                    }

                    root.applyCss();
                    root.layout();

                    formStage.setScene(scene);
                    formStage.setResizable(false);
                    formStage.centerOnScreen();


                    // Hide loading overlay
                    if (loadingOverlay != null) {
                        loadingOverlay.setVisible(false);
                        loadingOverlay.setManaged(false);
                    }

                    formStage.showAndWait();


                } catch (Exception e) {

                    e.printStackTrace();
                    if (loadingOverlay != null) {
                        loadingOverlay.setVisible(false);
                        loadingOverlay.setManaged(false);
                    }
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add schedule form: " + e.getMessage());
                }
            });
        });

        loadTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                Throwable exception = loadTask.getException();

                exception.printStackTrace();
                if (loadingOverlay != null) {
                    loadingOverlay.setVisible(false);
                    loadingOverlay.setManaged(false);
                }
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load form: " + exception.getMessage());
            });
        });

        Thread thread = new Thread(loadTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleEditSchedule(Schedule schedule) {


        // Show loading overlay
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<Parent> loadTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_schedule.fxml"));
                Parent root = loader.load();


                EditScheduleController controller = loader.getController();

                controller.setSchedule(schedule);
                controller.setScheduleManagementController(ScheduleManagementController.this);


                return root;
            }
        };

        loadTask.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                try {
                    Parent root = loadTask.getValue();

                    formStage = new Stage();
                    formStage.initModality(Modality.APPLICATION_MODAL);
                    formStage.setTitle("Edit Schedule");

                    Scene scene = new Scene(root, 800, 600);
                    if (getClass().getResource("/css/schedule_form.css") != null) {
                        scene.getStylesheets().add(getClass().getResource("/css/schedule_form.css").toExternalForm());
                    }

                    root.applyCss();
                    root.layout();

                    formStage.setScene(scene);
                    formStage.setResizable(false);
                    formStage.centerOnScreen();


                    // Hide loading overlay
                    if (loadingOverlay != null) {
                        loadingOverlay.setVisible(false);
                        loadingOverlay.setManaged(false);
                    }

                    formStage.showAndWait();


                } catch (Exception e) {

                    e.printStackTrace();
                    if (loadingOverlay != null) {
                        loadingOverlay.setVisible(false);
                        loadingOverlay.setManaged(false);
                    }
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit schedule form: " + e.getMessage());
                }
            });
        });

        loadTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                Throwable exception = loadTask.getException();

                exception.printStackTrace();
                if (loadingOverlay != null) {
                    loadingOverlay.setVisible(false);
                    loadingOverlay.setManaged(false);
                }
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load form: " + exception.getMessage());
            });
        });

        Thread thread = new Thread(loadTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleDeleteSchedule(Schedule schedule) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Schedule");
        confirmAlert.setContentText("Are you sure you want to delete this schedule?\n" +
                schedule.getCourse().getCourseTitle() + " on " + schedule.getDayOfWeek() + "\n" +
                "This action cannot be undone.");

        var appCss = getClass().getResource("/css/app.css");
        var scheduleCss = getClass().getResource("/css/schedule_form.css");
        if (confirmAlert.getDialogPane() != null) {
            if (appCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (scheduleCss != null) {
                confirmAlert.getDialogPane().getStylesheets().add(scheduleCss.toExternalForm());
            }
        }

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleted = ScheduleDAO.deleteSchedule(schedule.getScheduleID());
                if (deleted) {
                    statusLabel.setText("Schedule deleted successfully");
                    refreshData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Schedule deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete schedule.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting schedule: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        filterCombo.setValue("None");
        filterValueCombo.getItems().clear();
        refreshData();
    }

    public void refreshData() {
        loadScheduleDataAsync();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        var appCss = getClass().getResource("/css/app.css");
        var scheduleCss = getClass().getResource("/css/schedule_form.css");
        if (alert.getDialogPane() != null) {
            if (appCss != null) {
                alert.getDialogPane().getStylesheets().add(appCss.toExternalForm());
            }
            if (scheduleCss != null) {
                alert.getDialogPane().getStylesheets().add(scheduleCss.toExternalForm());
            }
        }

        alert.showAndWait();
    }

    private void filterData(String searchText) {
        String filterType = filterCombo.getValue();
        String filterValue = filterValueCombo.getValue();

        filteredList.setAll(scheduleList.filtered(schedule -> {
            boolean matchesSearch = true;
            boolean matchesFilter = true;

            // Search text matching
            if (!searchText.isEmpty()) {
                matchesSearch = schedule.getCourse().getCourseTitle().toLowerCase().contains(searchText) ||
                        schedule.getTeacher().getTeacherName().toLowerCase().contains(searchText) ||
                        schedule.getBatch().getBatchName().toLowerCase().contains(searchText) ||
                        schedule.getClassRoom().getRoomName().toLowerCase().contains(searchText) ||
                        schedule.getDayOfWeek().toLowerCase().contains(searchText);
            }

            // Filter matching
            if (filterValue != null && !filterValue.isEmpty() && !"None".equals(filterType)) {
                if ("Day".equals(filterType)) {
                    matchesFilter = schedule.getDayOfWeek().equalsIgnoreCase(filterValue);
                } else if ("Teacher".equals(filterType)) {
                    matchesFilter = schedule.getTeacher().getTeacherName().contains(filterValue.split("\\(")[0].trim());
                } else if ("Course".equals(filterType)) {
                    matchesFilter = schedule.getCourse().getCourseTitle().contains(filterValue.split("\\(")[0].trim());
                } else if ("Batch".equals(filterType)) {
                    matchesFilter = schedule.getBatch().getBatchName().contains(filterValue.split("\\(")[0].trim());
                }
            }

            return matchesSearch && matchesFilter;
        }));

        updateCount();
        statusLabel.setText("Search completed - " + filteredList.size() + " result(s)");
    }
}

