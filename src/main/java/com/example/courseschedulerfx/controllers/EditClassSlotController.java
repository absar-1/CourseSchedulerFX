package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.ClassSlotDAO;
import com.example.courseschedulerfx.model.ClassSlot;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalTime;

public class EditClassSlotController {

    @FXML
    private Spinner<Integer> startHourSpinner;

    @FXML
    private Spinner<Integer> startMinuteSpinner;

    @FXML
    private Spinner<Integer> endHourSpinner;

    @FXML
    private Spinner<Integer> endMinuteSpinner;

    @FXML
    private TextField durationField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    private ClassSlotManagementController managementController;
    private int slotID;

    @FXML
    public void initialize() {
        messageLabel.setText("");
        setupSpinners();
        attachSpinnerListeners();
    }

    private void setupSpinners() {
        // Start Hour Spinner (0-23)
        SpinnerValueFactory<Integer> startHourFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8);
        startHourSpinner.setValueFactory(startHourFactory);
        startHourSpinner.setEditable(true);

        // Start Minute Spinner (0-59)
        SpinnerValueFactory<Integer> startMinuteFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        startMinuteSpinner.setValueFactory(startMinuteFactory);
        startMinuteSpinner.setEditable(true);

        // End Hour Spinner (0-23)
        SpinnerValueFactory<Integer> endHourFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9);
        endHourSpinner.setValueFactory(endHourFactory);
        endHourSpinner.setEditable(true);

        // End Minute Spinner (0-59)
        SpinnerValueFactory<Integer> endMinuteFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        endMinuteSpinner.setValueFactory(endMinuteFactory);
        endMinuteSpinner.setEditable(true);
    }

    private void attachSpinnerListeners() {
        startHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateDuration());
        startMinuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateDuration());
        endHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateDuration());
        endMinuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateDuration());
    }

    private void updateDuration() {
        try {
            LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
            LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());

            if (startTime.isBefore(endTime)) {
                long minutes = java.time.temporal.ChronoUnit.MINUTES.between(startTime, endTime);
                long hours = minutes / 60;
                long mins = minutes % 60;
                durationField.setText(String.format("%d hr %d min", hours, mins));
            } else {
                durationField.setText("Invalid time range");
            }
        } catch (Exception e) {
            durationField.setText("");
        }
    }

    public void setManagementController(ClassSlotManagementController controller) {
        this.managementController = controller;
    }

    public void setSlotData(ClassSlot slot) {
        this.slotID = slot.getSlotID();
        // slotIdField.setText(String.valueOf(slot.getSlotID()));

        // Set start time
        startHourSpinner.getValueFactory().setValue(slot.getStartTime().getHour());
        startMinuteSpinner.getValueFactory().setValue(slot.getStartTime().getMinute());

        // Set end time
        endHourSpinner.getValueFactory().setValue(slot.getEndTime().getHour());
        endMinuteSpinner.getValueFactory().setValue(slot.getEndTime().getMinute());

        updateDuration();
    }

    @FXML
    private void handleSave() {
        // Clear previous messages
        messageLabel.setText("");
        messageLabel.getStyleClass().removeAll("error", "success");

        try {
            // Get time values
            LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
            LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());

            // Validation checks
            if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                showError("Start time must be before end time");
                return;
            }

            // Check for overlapping time slots (excluding this slot)
            if (!ClassSlotDAO.isTimeSlotAvailable(startTime, endTime, slotID)) {
                showError("This time slot overlaps with an existing slot");
                return;
            }

            // Update the slot
            boolean success = ClassSlotDAO.updateSlot(slotID, startTime, endTime);

            if (success) {
                showSuccess("Time slot updated successfully!");

                // Refresh parent table
                if (managementController != null) {
                    managementController.refreshData();
                }

                // Close the dialog after a short delay
                new Thread(() -> {
                    try {
                        Thread.sleep(800);
                        javafx.application.Platform.runLater(() -> {
                            Stage stage = (Stage) cancelBtn.getScene().getWindow();
                            stage.close();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showError("Failed to update time slot. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().add("error");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().add("success");
    }
}
