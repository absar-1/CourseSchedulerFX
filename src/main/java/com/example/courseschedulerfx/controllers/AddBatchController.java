package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.BatchDAO;
import com.example.courseschedulerfx.DAO.DepartmentDAO;
import com.example.courseschedulerfx.model.Batch;
import com.example.courseschedulerfx.model.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AddBatchController {

    @FXML
    private Spinner<Integer> yearSpinner;

    @FXML
    private ComboBox<Department> departmentCombo;

    @FXML
    private Spinner<Integer> studentSpinner;

    @FXML
    private Label messageLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    private BatchManagementController batchManagementController;

    @FXML
    public void initialize() {
        messageLabel.setText("");
        setupSpinners();
        loadDepartments();
    }

    private void setupSpinners() {
        // Year spinner
        int currentYear = java.time.LocalDate.now().getYear();
        SpinnerValueFactory<Integer> yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2015, currentYear + 1, currentYear);
        yearSpinner.setValueFactory(yearValueFactory);
        yearSpinner.setEditable(true);

        yearSpinner.getValueFactory().setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Integer value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    int value = Integer.parseInt(string);
                    if (value >= 2015 && value <= currentYear + 1) {
                        return value;
                    } else {
                        showError("Year must be between 2015 and " + (currentYear + 1));
                        return yearSpinner.getValue();
                    }
                } catch (NumberFormatException e) {
                    showError("Invalid year format");
                    return yearSpinner.getValue();
                }
            }
        });

        // Student spinner
        SpinnerValueFactory<Integer> studentValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 100);
        studentSpinner.setValueFactory(studentValueFactory);
        studentSpinner.setEditable(true);

        // Create a string converter for student spinner
        studentSpinner.getValueFactory().setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    int value = Integer.parseInt(string);
                    if (value >= 0 && value <= 10000) {
                        return value;
                    }
                    return studentSpinner.getValue();
                } catch (NumberFormatException e) {
                    return studentSpinner.getValue();
                }
            }
        });
    }

    private void loadDepartments() {
        try {
            List<Department> departments = DepartmentDAO.getAllDepartments();
            ObservableList<Department> departmentList = FXCollections.observableArrayList(departments);
            departmentCombo.setItems(departmentList);

            // Set custom cell factory to display department name
            departmentCombo.setCellFactory(param -> new ListCell<Department>() {
                @Override
                protected void updateItem(Department item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getDepartmentName());
                    }
                }
            });

            // Set button cell to display department name
            departmentCombo.setButtonCell(new ListCell<Department>() {
                @Override
                protected void updateItem(Department item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getDepartmentName());
                    }
                }
            });

        } catch (Exception e) {
            showError("Failed to load departments: " + e.getMessage());
        }
    }

    public void setBatchManagementController(BatchManagementController controller) {
        this.batchManagementController = controller;
    }

    @FXML
    private void handleSave() {
        // Clear previous messages
        messageLabel.setText("");
        messageLabel.getStyleClass().removeAll("error", "success");

        // Validate fields
        Department selectedDepartment = departmentCombo.getValue();
        Integer year = yearSpinner.getValue();
        Integer students = studentSpinner.getValue();

        // Validation checks
        if (selectedDepartment == null) {
            showError("Please select a department");
            return;
        }

        if (year == null || year < 1900 || year > 2100) {
            showError("Please enter a valid year of admission (between 1900 and 2100)");
            return;
        }

        if (students == null || students < 0) {
            showError("Please enter a valid number of students");
            return;
        }

        // Check if batch already exists
        if (BatchDAO.batchExists(year, selectedDepartment.getDepartmentID())) {
            showError("A batch for this department and year already exists");
            return;
        }

        try {
            // Create new batch
            Batch newBatch = new Batch(year, selectedDepartment, students);
            boolean success = BatchDAO.addBatch(newBatch);

            if (success) {
                showSuccess("Batch added successfully!");

                // Refresh parent table
                if (batchManagementController != null) {
                    batchManagementController.refreshData();
                }

                // Close dialog after short delay
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000);
                        closeDialog();
                    } catch (InterruptedException e) {
                        closeDialog();
                    }
                });
            } else {
                showError("Failed to add batch. Please try again.");
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
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


