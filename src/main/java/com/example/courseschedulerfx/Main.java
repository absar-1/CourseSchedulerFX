package com.example.courseschedulerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import com.example.courseschedulerfx.utils.WindowManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);

        if (getClass().getResource("/css/login.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
        }
        
        // Force layout calculation
        root.applyCss();
        root.layout();
        
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);

        // Set application icon (use the same logo as the app)
        try {
            var iconStream = getClass().getResourceAsStream("/images/logo.png");
            if (iconStream != null) {
                Image appIcon = new Image(iconStream);
                primaryStage.getIcons().add(appIcon);
            }
        } catch (Exception ignored) {
            // If icon fails to load, proceed without throwing — app still works
        }

        // Apply normal window size for login (not maximized)
        WindowManager.applyNormalWindow(primaryStage, 800, 600);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
