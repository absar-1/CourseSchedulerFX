package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.*;
import com.example.courseschedulerfx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class AddScheduleController {

    @FXML
    private ComboBox<String> dayCombo;

    @FXML
    private ComboBox<String> slotCombo;

    @FXML
    private ComboBox<String> courseCombo;

    @FXML
    private ComboBox<String> teacherCombo;

    @FXML
    private ComboBox<String> batchCombo;

    @FXML
    private ComboBox<String> roomCombo;

    @FXML
    private Label messageLabel;

    @FXML
    private AnchorPane anchor;

    private ScheduleManagementController scheduleManagementController;

    // Store actual objects for later use
    private ObservableList<ClassSlot> slots = FXCollections.observableArrayList();
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<Batch> batches = FXCollections.observableArrayList();
    private ObservableList<ClassRoom> rooms = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            loadAllData();
            setupComboBoxes();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error initializing form: " + e.getMessage(), "error");
        }
    }

    private void loadAllData() {
        try {
            // Load all class slots
            slots.clear();
            List<ClassSlot> loadedSlots = getAvailableSlots();
            slots.addAll(loadedSlots);

            // Load courses
            courses.clear();
            List<Course> loadedCourses = getAvailableCourses();
            courses.addAll(loadedCourses);

            // Load teachers
            teachers.clear();
            List<Teacher> loadedTeachers = getAvailableTeachers();
            teachers.addAll(loadedTeachers);

            // Load batches
            batches.clear();
            List<Batch> loadedBatches = getAvailableBatches();
            batches.addAll(loadedBatches);

            // Load rooms
            rooms.clear();
            List<ClassRoom> loadedRooms = getAvailableRooms();
            rooms.addAll(loadedRooms);

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading form data: " + e.getMessage(), "error");
        }
    }

    private void setupComboBoxes() {
        // Day combo - populate with days of week
        ObservableList<String> days = FXCollections.observableArrayList(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        );
        dayCombo.setItems(days);

        // Slot combo
        ObservableList<String> slotDisplays = FXCollections.observableArrayList();
        for (ClassSlot slot : slots) {
            slotDisplays.add(slot.getStartTime() + " - " + slot.getEndTime());
        }
        slotCombo.setItems(slotDisplays);

        // Course combo
        ObservableList<String> courseDisplays = FXCollections.observableArrayList();
        for (Course course : courses) {
            courseDisplays.add(course.getCourseTitle());
        }
        courseCombo.setItems(courseDisplays);

        // Teacher combo
        ObservableList<String> teacherDisplays = FXCollections.observableArrayList();
        for (Teacher teacher : teachers) {
            teacherDisplays.add(teacher.getTeacherName());
        }
        teacherCombo.setItems(teacherDisplays);

        // Batch combo
        ObservableList<String> batchDisplays = FXCollections.observableArrayList();
        for (Batch batch : batches) {
            batchDisplays.add(batch.getBatchName());
        }
        batchCombo.setItems(batchDisplays);

        // Room combo
        ObservableList<String> roomDisplays = FXCollections.observableArrayList();
        for (ClassRoom room : rooms) {
            roomDisplays.add(room.getRoomName());
        }
        roomCombo.setItems(roomDisplays);
    }

    @FXML
    private void handleCreateSchedule() {
        // Validate inputs
        if (dayCombo.getValue() == null || dayCombo.getValue().isEmpty()) {
            showMessage("Please select a day", "error");
            return;
        }
        if (slotCombo.getValue() == null || slotCombo.getValue().isEmpty()) {
            showMessage("Please select a time slot", "error");
            return;
        }
        if (courseCombo.getValue() == null || courseCombo.getValue().isEmpty()) {
            showMessage("Please select a course", "error");
            return;
        }
        if (teacherCombo.getValue() == null || teacherCombo.getValue().isEmpty()) {
            showMessage("Please select a teacher", "error");
            return;
        }
        if (batchCombo.getValue() == null || batchCombo.getValue().isEmpty()) {
            showMessage("Please select a batch", "error");
            return;
        }
        if (roomCombo.getValue() == null || roomCombo.getValue().isEmpty()) {
            showMessage("Please select a classroom", "error");
            return;
        }

        try {
            // Get selected values
            String day = dayCombo.getValue();
            int slotIndex = slotCombo.getSelectionModel().getSelectedIndex();
            ClassSlot slot = slots.get(slotIndex);

            int courseIndex = courseCombo.getSelectionModel().getSelectedIndex();
            Course course = courses.get(courseIndex);

            int teacherIndex = teacherCombo.getSelectionModel().getSelectedIndex();
            Teacher teacher = teachers.get(teacherIndex);

            int batchIndex = batchCombo.getSelectionModel().getSelectedIndex();
            Batch batch = batches.get(batchIndex);

            int roomIndex = roomCombo.getSelectionModel().getSelectedIndex();
            ClassRoom room = rooms.get(roomIndex);

            // Create schedule object
            Schedule schedule = new Schedule(
                    0, // ID will be auto-generated
                    day,
                    course,
                    room,
                    slot,
                    teacher,
                    batch
//                    true // isScheduled
            );

            // Try to add schedule (clash detection happens in DAO)
            try {
                if (ScheduleDAO.addSchedule(schedule)) {
                    showMessage("Schedule created successfully!", "success");
                    // Refresh parent controller
                    if (scheduleManagementController != null) {
                        scheduleManagementController.refreshData();
                    }
                    // Close window after 1 second
                    javafx.application.Platform.runLater(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        closeWindow();
                    });
                } else {
                    showMessage("Failed to create schedule", "error");
                }
            } catch (RuntimeException e) {
                // Clash detection error
                showMessage(e.getMessage(), "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error creating schedule: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        try {
            // Try getting stage from any button (they're all in the same window)
            Stage stage = null;

            if (anchor != null && anchor.getScene() != null) {
                stage = (Stage) anchor.getScene().getWindow();
            } else if (dayCombo != null && dayCombo.getScene() != null) {
                stage = (Stage) dayCombo.getScene().getWindow();
            } else if (messageLabel != null && messageLabel.getScene() != null) {
                stage = (Stage) messageLabel.getScene().getWindow();
            }

            if (stage != null) {
                stage.close();
            } else {
                // Could not find stage to close
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        if ("error".equals(type)) {
            messageLabel.getStyleClass().removeAll("success");
            messageLabel.getStyleClass().removeAll("warning");
            messageLabel.getStyleClass().add("error");
        } else if ("success".equals(type)) {
            messageLabel.getStyleClass().removeAll("error");
            messageLabel.getStyleClass().removeAll("warning");
            messageLabel.getStyleClass().add("success");
        } else if ("warning".equals(type)) {
            messageLabel.getStyleClass().removeAll("error");
            messageLabel.getStyleClass().removeAll("success");
            messageLabel.getStyleClass().add("warning");
        }
    }

    public void setScheduleManagementController(ScheduleManagementController controller) {
        this.scheduleManagementController = controller;
    }

    // Helper methods to get available data
    private List<ClassSlot> getAvailableSlots() {
        List<ClassSlot> slots = new java.util.ArrayList<>();
        try {
            // Get all slots directly from the ClassSlots table
            slots = ClassSlotDAO.getAllSlots();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }

    private List<Course> getAvailableCourses() {
        try {
            return CourseDAO.getCourseList();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<Teacher> getAvailableTeachers() {
        try {
            return TeacherDAO.getAllTeachers();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<Batch> getAvailableBatches() {
        try {
            return BatchDAO.getAllBatches();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<ClassRoom> getAvailableRooms() {
        try {
            return ClassRoomDAO.getAllRooms();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
}

