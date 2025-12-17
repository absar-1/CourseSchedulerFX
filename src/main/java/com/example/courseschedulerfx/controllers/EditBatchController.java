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

public class EditBatchController {


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

    private Batch currentBatch;
    private BatchManagementController batchManagementController;

    @FXML
    public void initialize() {
        messageLabel.setText("");
        setupSpinners();
        loadDepartments();
    }

    private void setupSpinners() {
        // Year spinner
        SpinnerValueFactory<Integer> yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2100, 2024);
        yearSpinner.setValueFactory(yearValueFactory);
        yearSpinner.setEditable(true);

        // Create a string converter for year spinner
        yearSpinner.getValueFactory().setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    int value = Integer.parseInt(string);
                    if (value >= 2000 && value <= 2100) {
                        return value;
                    }
                    return yearSpinner.getValue();
                } catch (NumberFormatException e) {
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

    public void setBatch(Batch batch) {
        this.currentBatch = batch;

        // Populate fields with current data
        yearSpinner.getValueFactory().setValue(batch.getYearOfAdmission());
        studentSpinner.getValueFactory().setValue(batch.getTotalStudents());

        // Set selected department
        for (int i = 0; i < departmentCombo.getItems().size(); i++) {
            if (departmentCombo.getItems().get(i).getDepartmentID() == batch.getDepartment().getDepartmentID()) {
                departmentCombo.getSelectionModel().select(i);
                break;
            }
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

        if (year == null || year < 2000 || year > 2100) {
            showError("Please enter a valid year of admission");
            return;
        }

        if (students == null || students < 0) {
            showError("Please enter a valid number of students");
            return;
        }

        // Check if batch already exists (excluding current batch)
        if (BatchDAO.batchExistsExcept(year, selectedDepartment.getDepartmentID(), currentBatch.getBatchID())) {
            showError("A batch for this department and year already exists");
            return;
        }

        try {
            // Update batch
            Batch updatedBatch = new Batch(currentBatch.getBatchID(), currentBatch.getBatchName(), year, selectedDepartment, students);
            boolean success = BatchDAO.updateBatch(updatedBatch);

            if (success) {
                showSuccess("Batch updated successfully!");

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
                showError("Failed to update batch. Please try again.");
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

