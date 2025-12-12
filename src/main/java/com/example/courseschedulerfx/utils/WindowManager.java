package com.example.courseschedulerfx.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class WindowManager {
    public WindowManager() {}

    public static void applyDefaultWindow(Stage stage) {
        if (stage == null) return;

        if (stage.isMaximized()) {
            stage.setMaximized(false);
        }
        
        stage.setResizable(true);
        
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        // Set the stage to the visual bounds so taskbar space is respected
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        
        // Center on screen before maximizing
        stage.centerOnScreen();

        stage.setMaximized(true);
    }

    // for opening window with custom size (not maximized) - using for login window
    public static void applyNormalWindow(Stage stage, double width, double height) {
        if (stage == null) return;
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        }
        stage.setResizable(true);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }
}
