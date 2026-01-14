package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.*;
import com.example.courseschedulerfx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditSpecialScheduleController {

    @FXML
    private ComboBox<String> courseCombo;

    @FXML
    private ComboBox<String> classroomCombo;

    @FXML
    private ComboBox<String> teacherCombo;

    @FXML
    private DatePicker scheduleDatePicker;

    @FXML
    private ComboBox<String> batchCombo;

    @FXML
    private ComboBox<String> dayOfWeekCombo;

    @FXML
    private ComboBox<String> startTimeCombo;

    @FXML
    private ComboBox<String> endTimeCombo;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private Label messageLabel;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    private SpecialSchedule currentSchedule;
    private SpecialScheduleManagementController specialScheduleManagementController;

    // Store actual objects for later use
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private ObservableList<ClassRoom> classrooms = FXCollections.observableArrayList();
    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<Batch> batches = FXCollections.observableArrayList();
    private List<LocalTime> availableTimes = FXCollections.observableArrayList();

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
            // Load courses
            courses.clear();
            List<Course> loadedCourses = getAvailableCourses();
            courses.addAll(loadedCourses);

            // Load classrooms
            classrooms.clear();
            List<ClassRoom> loadedClassrooms = getAvailableClassrooms();
            classrooms.addAll(loadedClassrooms);

            // Load teachers
            teachers.clear();
            List<Teacher> loadedTeachers = getAvailableTeachers();
            teachers.addAll(loadedTeachers);

            // Load batches
            batches.clear();
            List<Batch> loadedBatches = getAvailableBatches();
            batches.addAll(loadedBatches);

            // Load available times (from ClassSlots)
            List<LocalTime> times = getAvailableTimes();
            availableTimes.clear();
            availableTimes.addAll(times);

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading form data: " + e.getMessage(), "error");
        }
    }

    private void setupComboBoxes() {
        // Course combo
        ObservableList<String> courseDisplays = FXCollections.observableArrayList();
        for (Course course : courses) {
            courseDisplays.add(course.getCourseTitle());
        }
        courseCombo.setItems(courseDisplays);

        // Classroom combo
        ObservableList<String> classroomDisplays = FXCollections.observableArrayList();
        for (ClassRoom classroom : classrooms) {
            classroomDisplays.add(classroom.getRoomName());
        }
        classroomCombo.setItems(classroomDisplays);

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

        // Day of week combo
        ObservableList<String> days = FXCollections.observableArrayList(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        );
        dayOfWeekCombo.setItems(days);

        // Time combos - populate with available times
        ObservableList<String> timeDisplays = FXCollections.observableArrayList();
        for (LocalTime time : availableTimes) {
            timeDisplays.add(time.toString());
        }
        startTimeCombo.setItems(timeDisplays);
        endTimeCombo.setItems(FXCollections.observableArrayList(timeDisplays));

        // Type combo
        ObservableList<String> types = FXCollections.observableArrayList("makeup", "extra");
        typeCombo.setItems(types);
    }

    public void setSpecialSchedule(SpecialSchedule schedule) {
        this.currentSchedule = schedule;

        // Populate fields with current data
        // Note: specialScheduleID is NOT shown or editable

        // Set course
        if (schedule.getCourse() != null) {
            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).getCourseID() == schedule.getCourse().getCourseID()) {
                    courseCombo.getSelectionModel().select(i);
                    break;
                }
            }
        }

        // Set classroom
        if (schedule.getClassRoom() != null) {
            for (int i = 0; i < classrooms.size(); i++) {
                if (classrooms.get(i).getRoomID() == schedule.getClassRoom().getRoomID()) {
                    classroomCombo.getSelectionModel().select(i);
                    break;
                }
            }
        }

        // Set teacher
        if (schedule.getTeacher() != null) {
            for (int i = 0; i < teachers.size(); i++) {
                if (teachers.get(i).getTeacherID() == schedule.getTeacher().getTeacherID()) {
                    teacherCombo.getSelectionModel().select(i);
                    break;
                }
            }
        }

        // Set schedule date
        if (schedule.getScheduleDate() != null) {
            scheduleDatePicker.setValue(schedule.getScheduleDate());
        }

        // Set batch
        if (schedule.getBatch() != null) {
            for (int i = 0; i < batches.size(); i++) {
                if (batches.get(i).getBatchID() == schedule.getBatch().getBatchID()) {
                    batchCombo.getSelectionModel().select(i);
                    break;
                }
            }
        }

        // Set day of week
        if (schedule.getDayOfWeek() != null) {
            dayOfWeekCombo.setValue(schedule.getDayOfWeek());
        }

        // Set start time
        if (schedule.getStartTime() != null) {
            startTimeCombo.setValue(schedule.getStartTime().toString());
        }

        // Set end time
        if (schedule.getEndTime() != null) {
            endTimeCombo.setValue(schedule.getEndTime().toString());
        }

        // Set type
        if (schedule.getType() != null) {
            typeCombo.setValue(schedule.getType());
        }
    }

    @FXML
    private void handleUpdateSpecialSchedule() {
        // Validate inputs
        if (courseCombo.getValue() == null || courseCombo.getValue().isEmpty()) {
            showMessage("Please select a course", "error");
            return;
        }
        if (classroomCombo.getValue() == null || classroomCombo.getValue().isEmpty()) {
            showMessage("Please select a classroom", "error");
            return;
        }
        if (teacherCombo.getValue() == null || teacherCombo.getValue().isEmpty()) {
            showMessage("Please select a teacher", "error");
            return;
        }
        if (scheduleDatePicker.getValue() == null) {
            showMessage("Please select a schedule date", "error");
            return;
        }
        if (batchCombo.getValue() == null || batchCombo.getValue().isEmpty()) {
            showMessage("Please select a batch", "error");
            return;
        }
        if (dayOfWeekCombo.getValue() == null || dayOfWeekCombo.getValue().isEmpty()) {
            showMessage("Please select a day of week", "error");
            return;
        }
        if (startTimeCombo.getValue() == null || startTimeCombo.getValue().isEmpty()) {
            showMessage("Please select a start time", "error");
            return;
        }
        if (endTimeCombo.getValue() == null || endTimeCombo.getValue().isEmpty()) {
            showMessage("Please select an end time", "error");
            return;
        }
        if (typeCombo.getValue() == null || typeCombo.getValue().isEmpty()) {
            showMessage("Please select a type (makeup/extra)", "error");
            return;
        }

        try {
            // Get selected values
            int courseIndex = courseCombo.getSelectionModel().getSelectedIndex();
            Course course = courses.get(courseIndex);

            int classroomIndex = classroomCombo.getSelectionModel().getSelectedIndex();
            ClassRoom classroom = classrooms.get(classroomIndex);

            int teacherIndex = teacherCombo.getSelectionModel().getSelectedIndex();
            Teacher teacher = teachers.get(teacherIndex);

            LocalDate scheduleDate = scheduleDatePicker.getValue();

            int batchIndex = batchCombo.getSelectionModel().getSelectedIndex();
            Batch batch = batches.get(batchIndex);

            String dayOfWeek = dayOfWeekCombo.getValue();

            LocalTime startTime = LocalTime.parse(startTimeCombo.getValue());
            LocalTime endTime = LocalTime.parse(endTimeCombo.getValue());

            String type = typeCombo.getValue();

            // Validate time logic
            if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                showMessage("Start time must be before end time", "error");
                return;
            }

            // Create updated special schedule object (keep original ID)
            SpecialSchedule updatedSchedule = new SpecialSchedule(
                    currentSchedule.getSpecialScheduleID(), // Keep original ID
                    course,
                    classroom,
                    teacher,
                    scheduleDate,
                    batch,
                    dayOfWeek,
                    startTime,
                    endTime,
                    type
            );

            // Try to update special schedule
            if (SpecialScheduleDAO.updateSpecialSchedule(updatedSchedule)) {
                showMessage("Special schedule updated successfully!", "success");
                // Refresh parent controller
                if (specialScheduleManagementController != null) {
                    specialScheduleManagementController.refreshData();
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
                showMessage("Failed to update special schedule", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error updating special schedule: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        try {
            Stage stage = null;

            if (anchor != null && anchor.getScene() != null) {
                stage = (Stage) anchor.getScene().getWindow();
            } else if (courseCombo != null && courseCombo.getScene() != null) {
                stage = (Stage) courseCombo.getScene().getWindow();
            } else if (messageLabel != null && messageLabel.getScene() != null) {
                stage = (Stage) messageLabel.getScene().getWindow();
            }

            if (stage != null) {
                stage.close();
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

    public void setSpecialScheduleManagementController(SpecialScheduleManagementController controller) {
        this.specialScheduleManagementController = controller;
    }

    // Helper methods to get available data
    private List<Course> getAvailableCourses() {
        try {
            return CourseDAO.getCourseList();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private List<ClassRoom> getAvailableClassrooms() {
        try {
            return ClassRoomDAO.getAllRooms();
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

    // Changeddddd from absar
    private List<LocalTime> getAvailableTimes() {
        try {
            List<ClassSlot> slots = ClassSlotDAO.getAllSlots();
            List<LocalTime> times = new ArrayList<>();

            for (ClassSlot slot : slots) {
                times.add(slot.getStartTime());
                times.add(slot.getEndTime());
            }

            times.sort(LocalTime::compareTo);

            // Remove duplicates
            return times.stream()
                    .distinct()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}

