package com.example.courseschedulerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.courseschedulerfx.utils.WindowManager;

public class AdminDashboardTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Optionally attach admin dashboard stylesheet explicitly (FXML already attaches it)
        if (getClass().getResource("/css/admin_dashboard.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/admin_dashboard.css").toExternalForm());
        }

        primaryStage.setTitle("Admin Dashboard - Test Render");
        primaryStage.setScene(scene);

        // Apply consistent window behavior
        WindowManager.applyDefaultWindow(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

