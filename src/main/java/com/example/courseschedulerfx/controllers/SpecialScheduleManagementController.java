package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.SpecialScheduleDAO;
import com.example.courseschedulerfx.DAO.TeacherDAO;
import com.example.courseschedulerfx.DAO.CourseDAO;
import com.example.courseschedulerfx.DAO.BatchDAO;
import com.example.courseschedulerfx.DAO.ClassRoomDAO;
import com.example.courseschedulerfx.model.SpecialSchedule;
import com.example.courseschedulerfx.model.Teacher;
import com.example.courseschedulerfx.model.Course;
import com.example.courseschedulerfx.model.Batch;
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

public class SpecialScheduleManagementController {

    @FXML
    private Button addSpecialScheduleBtn;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private ComboBox<String> filterValueCombo;

    @FXML
    private TableView<SpecialSchedule> specialScheduleTable;

    @FXML
    private TableColumn<SpecialSchedule, String> courseColumn;

    @FXML
    private TableColumn<SpecialSchedule, String> classroomColumn;

    @FXML
    private TableColumn<SpecialSchedule, String> teacherColumn;

    @FXML
    private TableColumn<SpecialSchedule, String> batchColumn;

    @FXML
    private TableColumn<SpecialSchedule, String> dateColumn;

    @FXML
    private TableColumn<SpecialSchedule, String> dayColumn;

    @FXML
    private TableColumn<SpecialSchedule, String> timeColumn;

    @FXML
    private TableColumn<SpecialSchedule, String> typeColumn;

    @FXML
    private TableColumn<SpecialSchedule, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label countLabel;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private ProgressIndicator loadingIndicator;

    private ObservableList<SpecialSchedule> scheduleList = FXCollections.observableArrayList();
    private ObservableList<SpecialSchedule> filteredList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set column resize policy
        specialScheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupFilterCombo();
        setupTableColumns();
        loadSpecialScheduleDataAsync();

        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });
    }

    private void setupFilterCombo() {
        ObservableList<String> filterOptions = FXCollections.observableArrayList(
                "None",
                "Type",
                "Day",
                "Course",
                "Teacher",
                "Batch",
                "Classroom"
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

        if ("Type".equals(selectedFilter)) {
            ObservableList<String> types = FXCollections.observableArrayList("makeup", "extra");
            filterValueCombo.setItems(types);
            filterValueCombo.setPromptText("Select type...");
        } else if ("Day".equals(selectedFilter)) {
            ObservableList<String> days = FXCollections.observableArrayList(
                    "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
            );
            filterValueCombo.setItems(days);
            filterValueCombo.setPromptText("Select day...");
        } else if ("Course".equals(selectedFilter)) {
            try {
                List<Course> courses = getAvailableCourses();
                ObservableList<String> courseNames = FXCollections.observableArrayList();
                for (Course c : courses) {
                    courseNames.add(c.getCourseTitle() + " (ID: " + c.getCourseID() + ")");
                }
                filterValueCombo.setItems(courseNames);
                filterValueCombo.setPromptText("Select course...");
            } catch (Exception e) {
                filterValueCombo.setPromptText("Error loading courses...");
            }
        } else if ("Teacher".equals(selectedFilter)) {
            try {
                List<Teacher> teachers = getAvailableTeachers();
                ObservableList<String> teacherNames = FXCollections.observableArrayList();
                for (Teacher t : teachers) {
                    teacherNames.add(t.getTeacherName() + " (ID: " + t.getTeacherID() + ")");
                }
                filterValueCombo.setItems(teacherNames);
                filterValueCombo.setPromptText("Select teacher...");
            } catch (Exception e) {
                filterValueCombo.setPromptText("Error loading teachers...");
            }
        } else if ("Batch".equals(selectedFilter)) {
            try {
                List<Batch> batches = getAvailableBatches();
                ObservableList<String> batchNames = FXCollections.observableArrayList();
                for (Batch b : batches) {
                    batchNames.add(b.getBatchName() + " (ID: " + b.getBatchID() + ")");
                }
                filterValueCombo.setItems(batchNames);
                filterValueCombo.setPromptText("Select batch...");
            } catch (Exception e) {
                filterValueCombo.setPromptText("Error loading batches...");
            }
        } else if ("Classroom".equals(selectedFilter)) {
            try {
                List<ClassRoom> rooms = getAvailableClassrooms();
                ObservableList<String> roomNames = FXCollections.observableArrayList();
                for (ClassRoom r : rooms) {
                    roomNames.add(r.getRoomName() + " (ID: " + r.getRoomID() + ")");
                }
                filterValueCombo.setItems(roomNames);
                filterValueCombo.setPromptText("Select classroom...");
            } catch (Exception e) {
                filterValueCombo.setPromptText("Error loading classrooms...");
            }
        } else {
            filterValueCombo.getItems().clear();
            filterValueCombo.setPromptText("Select...");
        }
    }

    private void setupTableColumns() {
        // Course Column
        courseColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getCourse().getCourseTitle()));

        // Classroom Column
        classroomColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getClassRoom().getRoomName()));

        // Teacher Column
        teacherColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getTeacher().getTeacherName()));

        // Batch Column
        batchColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getBatch().getBatchName()));

        // Date Column
        dateColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getScheduleDate().toString()));

        // Day Column
        dayColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDayOfWeek()));

        // Time Column
        timeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                cellData.getValue().getStartTime() + " - " + cellData.getValue().getEndTime()
            ));

        // Type Column
        typeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getType()));

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
                    SpecialSchedule schedule = getTableView().getItems().get(getIndex());
                    handleEditSchedule(schedule);
                });

                deleteBtn.setOnAction(event -> {
                    SpecialSchedule schedule = getTableView().getItems().get(getIndex());
                    handleDeleteSchedule(schedule);
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

    private void loadSpecialScheduleDataAsync() {
        statusLabel.setText("Loading...");
        // Show loading overlay
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(true);
            loadingOverlay.setManaged(true);
        }

        Task<List<SpecialSchedule>> task = new Task<>() {
            @Override
            protected List<SpecialSchedule> call() {
                return SpecialScheduleDAO.getAllSpecialSchedules();
            }
        };

        task.setOnSucceeded(event -> {
            List<SpecialSchedule> schedules = task.getValue();
            Platform.runLater(() -> {
                scheduleList.clear();
                scheduleList.addAll(schedules);
                specialScheduleTable.setItems(scheduleList);
                filteredList.setAll(scheduleList);
                statusLabel.setText("Loaded successfully");
                countLabel.setText("Total: " + schedules.size());
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
                countLabel.setText("Total: 0");
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

    private void filterData(String searchTerm) {
        String filterType = filterCombo.getValue();
        String filterValue = filterValueCombo.getValue();

        filteredList.setAll(scheduleList.filtered(schedule -> {
            boolean matchesSearch = true;
            boolean matchesFilter = true;

            // Search text matching
            if (!searchTerm.isEmpty()) {
                matchesSearch = schedule.getCourse().getCourseTitle().toLowerCase().contains(searchTerm) ||
                        schedule.getTeacher().getTeacherName().toLowerCase().contains(searchTerm) ||
                        schedule.getBatch().getBatchName().toLowerCase().contains(searchTerm) ||
                        schedule.getClassRoom().getRoomName().toLowerCase().contains(searchTerm) ||
                        schedule.getType().toLowerCase().contains(searchTerm) ||
                        schedule.getDayOfWeek().toLowerCase().contains(searchTerm);
            }

            // Filter matching
            if (filterValue != null && !filterValue.isEmpty() && !"None".equals(filterType)) {
                if ("Type".equals(filterType)) {
                    matchesFilter = schedule.getType().equalsIgnoreCase(filterValue);
                } else if ("Day".equals(filterType)) {
                    matchesFilter = schedule.getDayOfWeek().equalsIgnoreCase(filterValue);
                } else if ("Course".equals(filterType)) {
                    matchesFilter = schedule.getCourse().getCourseTitle().contains(filterValue.split("\\(")[0].trim());
                } else if ("Teacher".equals(filterType)) {
                    matchesFilter = schedule.getTeacher().getTeacherName().contains(filterValue.split("\\(")[0].trim());
                } else if ("Batch".equals(filterType)) {
                    matchesFilter = schedule.getBatch().getBatchName().contains(filterValue.split("\\(")[0].trim());
                } else if ("Classroom".equals(filterType)) {
                    matchesFilter = schedule.getClassRoom().getRoomName().contains(filterValue.split("\\(")[0].trim());
                }
            }

            return matchesSearch && matchesFilter;
        }));

        specialScheduleTable.setItems(filteredList);
        countLabel.setText("Total: " + filteredList.size());
        statusLabel.setText("Search completed - " + filteredList.size() + " result(s)");
    }

    @FXML
    private void handleAddSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_special_schedule.fxml"));
            Parent root = loader.load();

            AddSpecialScheduleController controller = loader.getController();
            controller.setSpecialScheduleManagementController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Special Schedule");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addSpecialScheduleBtn.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Special Schedule form: " + e.getMessage());
        }
    }

    private void handleEditSchedule(SpecialSchedule schedule) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_special_schedule.fxml"));
            Parent root = loader.load();

            EditSpecialScheduleController controller = loader.getController();
            controller.setSpecialSchedule(schedule);
            controller.setSpecialScheduleManagementController(this);

            Stage stage = new Stage();
            stage.setTitle("Edit Special Schedule");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(specialScheduleTable.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Edit Special Schedule form: " + e.getMessage());
        }
    }

    private void handleDeleteSchedule(SpecialSchedule schedule) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Special Schedule");
        confirmDialog.setContentText("Are you sure you want to delete this special schedule?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (SpecialScheduleDAO.deleteSpecialSchedule(schedule.getSpecialScheduleID())) {
                    showAlert("Success", "Special schedule deleted successfully!");
                    refreshData();
                } else {
                    showAlert("Error", "Failed to delete special schedule");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Error deleting special schedule: " + e.getMessage());
            }
        }
    }

    public void refreshData() {
        loadSpecialScheduleDataAsync();
        searchField.clear();
        filterCombo.setValue("None");
        filterValueCombo.getItems().clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<Teacher> getAvailableTeachers() {
        try {
            return com.example.courseschedulerfx.DAO.TeacherDAO.getAllTeachers();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<Course> getAvailableCourses() {
        try {
            return com.example.courseschedulerfx.DAO.CourseDAO.getAllCourses();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<Batch> getAvailableBatches() {
        try {
            return com.example.courseschedulerfx.DAO.BatchDAO.getAllBatches();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<ClassRoom> getAvailableClassrooms() {
        try {
            return com.example.courseschedulerfx.DAO.ClassRoomDAO.getAllRooms();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
}
