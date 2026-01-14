package com.example.courseschedulerfx.controllers;

import com.example.courseschedulerfx.DAO.AdminDAO;
import com.example.courseschedulerfx.DAO.ClassRoomDAO;
import com.example.courseschedulerfx.DAO.CourseDAO;
import com.example.courseschedulerfx.DAO.CourseDAO;
import com.example.courseschedulerfx.DAO.DepartmentDAO;
import com.example.courseschedulerfx.DAO.SpecialScheduleDAO;
import com.example.courseschedulerfx.DAO.TeacherDAO;
import com.example.courseschedulerfx.datastructures.Stackk;
import com.example.courseschedulerfx.model.SpecialSchedule;
import com.example.courseschedulerfx.utils.HomeDataCache;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import java.time.format.DateTimeFormatter;

public class HomeController {

    @FXML
    private Label headlineLabel;

    @FXML
    private Label totalTeachersLabel;

    @FXML
    private Label totalCoursesLabel;

    @FXML
    private Label totalDepartmentsLabel;

    @FXML
    private Label totalClassroomsLabel;

    @FXML
    private Label totalAdminsLabel;

    @FXML
    private VBox adminCard;

    @FXML
    private VBox teacherCard;

    @FXML
    private VBox courseCard;

    @FXML
    private VBox departmentCard;

    @FXML
    private VBox classroomCard;

    private final String[] headlines = {
        "[INFO] Welcome to Course Scheduler - Manage your courses efficiently and stay organized!",
        "[SYSTEM] Stay organized with our advanced scheduling system - Plan ahead for success!",
        "[ALERT] Quick view of all your courses and resources - Everything at your fingertips!",
        "[NOTICE] Optimize your course management workflow - Streamline your administrative tasks!",
        "[UPDATE] Streamline your administrative tasks today - Experience seamless scheduling!"
    };

    private Timeline headlineAnimation;

    @FXML
    public void initialize() {
        // Set default headlines immediately for fast UI response
        String allHeadlines = String.join("     |     ", headlines);
        headlineLabel.setText("Welcome to the Course Scheduler     |     " + allHeadlines);

        // Set loading placeholders
        totalTeachersLabel.setText("Loading...");
        totalCoursesLabel.setText("Loading...");
        totalDepartmentsLabel.setText("Loading...");
        totalClassroomsLabel.setText("Loading...");
        if (totalAdminsLabel != null) {
            totalAdminsLabel.setText("Loading...");
        }

        // Start headline animation immediately
        Platform.runLater(this::startHeadlineAnimation);

        // Load database data asynchronously
        loadDataAsync();
    }

    private void loadDataAsync() {
        HomeDataCache cache = HomeDataCache.getInstance();

        // Check if cache is valid
        if (cache.isCacheValid()) {

            // Load from cache immediately
            updateUIWithData(cache.getTeachers(), cache.getCourses(),
                            cache.getDepartments(), cache.getClassrooms(),
                            cache.getAdmins(), cache.getSpecialSchedules());
            return;
        }

        // Cache is invalid, fetch from database

        Task<HomeData> dataTask = new Task<>() {
            @Override
            protected HomeData call() {
                // Perform all DB calls in background thread
                int teachers = TeacherDAO.getTeacherCount();
                int courses = CourseDAO.getCourseCount();
                int departments = DepartmentDAO.getTotalDepartments();
                int classrooms = ClassRoomDAO.getTotalClassRooms();
                int admins = AdminDAO.countAdmins();
                Stackk<SpecialSchedule> specialSchedules = SpecialScheduleDAO.getRecentSpecialSchedules();

                return new HomeData(teachers, courses, departments, classrooms, admins, specialSchedules);
            }
        };

        dataTask.setOnSucceeded(event -> {
            HomeData data = dataTask.getValue();

            // Store in cache for future use
            cache.setCache(data.teachers, data.courses, data.departments,
                          data.classrooms, data.admins, data.specialSchedules);

            // Update UI
            updateUIWithData(data.teachers, data.courses, data.departments,
                            data.classrooms, data.admins, data.specialSchedules);

        });

        dataTask.setOnFailed(event -> {
            // Handle error gracefully - show default values
            totalTeachersLabel.setText("0");
            totalCoursesLabel.setText("0");
            totalDepartmentsLabel.setText("0");
            totalClassroomsLabel.setText("0");
            totalAdminsLabel.setText("0");

        });

        Thread thread = new Thread(dataTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void updateUIWithData(int teachers, int courses, int departments,
                                  int classrooms, int admins, Stackk<SpecialSchedule> specialSchedules) {
        // Update UI on JavaFX thread if not already on it
        if (Platform.isFxApplicationThread()) {
            performUIUpdate(teachers, courses, departments, classrooms, admins, specialSchedules);
        } else {
            Platform.runLater(() -> performUIUpdate(teachers, courses, departments, classrooms, admins, specialSchedules));
        }
    }

    private void performUIUpdate(int teachers, int courses, int departments,
                                 int classrooms, int admins, Stackk<SpecialSchedule> specialSchedules) {
        totalTeachersLabel.setText(String.valueOf(teachers));
        totalCoursesLabel.setText(String.valueOf(courses));
        totalDepartmentsLabel.setText(String.valueOf(departments));
        totalClassroomsLabel.setText(String.valueOf(classrooms));
        totalAdminsLabel.setText(String.valueOf(admins));

        // Load headlines asynchronously in background
        loadHeadlinesAsync(specialSchedules);
    }

    private void loadHeadlinesAsync(Stackk<SpecialSchedule> specialSchedules) {
        Task<String> headlineTask = new Task<>() {
            @Override
            protected String call() {
                // Build headlines in background thread
                String specialSchedulesHeadline = buildSpecialSchedulesHeadline(specialSchedules);
                String allHeadlines = String.join("     |     ", headlines);
                return specialSchedulesHeadline.isEmpty() ?
                    allHeadlines :
                    specialSchedulesHeadline + "     |     " + allHeadlines;
            }
        };

        headlineTask.setOnSucceeded(event -> {
            String finalHeadline = headlineTask.getValue();
            headlineLabel.setText(finalHeadline);

        });

        headlineTask.setOnFailed(event -> {
            // Fallback to default headlines if error occurs
            String allHeadlines = String.join("     |     ", headlines);
            headlineLabel.setText("Welcome to the Course Scheduler     |     " + allHeadlines);

        });

        Thread thread = new Thread(headlineTask);
        thread.setDaemon(true);
        thread.start();
    }

    // Inner class to hold all fetched data
    private static class HomeData {
        final int teachers;
        final int courses;
        final int departments;
        final int classrooms;
        final int admins;
        final Stackk<SpecialSchedule> specialSchedules;

        HomeData(int teachers, int courses, int departments, int classrooms, int admins, Stackk<SpecialSchedule> specialSchedules) {
            this.teachers = teachers;
            this.courses = courses;
            this.departments = departments;
            this.classrooms = classrooms;
            this.admins = admins;
            this.specialSchedules = specialSchedules;
        }
    }

    private String buildSpecialSchedulesHeadline(Stackk<SpecialSchedule> stack) {
        if (stack == null || stack.isEmpty()) {
            return "";
        }

        StringBuilder headline = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        headline.append("Welcome to the Course Scheduler");

        // Create a temporary copy to iterate without consuming the original
        Stackk<SpecialSchedule> tempStack = copyStack(stack);
        int count = 0;

        while (!tempStack.isEmpty() && count < 5) {
            SpecialSchedule schedule = tempStack.pop();

            String sessionType = schedule.getType().equalsIgnoreCase("makeup") ?
                "MAKEUP SESSION" : "EXTRA SESSION";

            headline.append(String.format(
                "     |     [%s] %s - %s in %s | %s (%s-%s)",
                sessionType,
                schedule.getCourse().getCourseTitle(),
                schedule.getTeacher().getTeacherName(),
                schedule.getClassRoom().getRoomName(),
                schedule.getScheduleDate().format(dateFormatter),
                schedule.getStartTime().format(timeFormatter),
                schedule.getEndTime().format(timeFormatter)
            ));

            count++;
        }

        return headline.toString();
    }

    private Stackk<SpecialSchedule> copyStack(Stackk<SpecialSchedule> original) {
        if (original == null) {
            return new Stackk<>();
        }

        Stackk<SpecialSchedule> temp = new Stackk<>();
        Stackk<SpecialSchedule> copy = new Stackk<>();

        while (!original.isEmpty()) {
            SpecialSchedule item = original.pop();
            temp.push(item);
        }

        while (!temp.isEmpty()) {
            SpecialSchedule item = temp.pop();
            original.push(item);
            copy.push(item);
        }

        return copy;
    }

    private void startHeadlineAnimation() {
        if (headlineAnimation != null) {
            headlineAnimation.stop();
        }

        // Create a smooth continuous scrolling animation
        headlineAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(headlineLabel.translateXProperty(), 1200.0)
            ),
            new KeyFrame(Duration.seconds(80),
                new KeyValue(headlineLabel.translateXProperty(), -1200.0)
            )
        );

        // Set to loop infinitely
        headlineAnimation.setCycleCount(Timeline.INDEFINITE);
        headlineAnimation.setAutoReverse(false);
        headlineAnimation.play();
    }

    public void setTotalTeachers(int count) {
        totalTeachersLabel.setText(String.valueOf(count));
    }

    public void setTotalCourses(int count) {
        totalCoursesLabel.setText(String.valueOf(count));
    }

    public void setTotalDepartments(int count) {
        totalDepartmentsLabel.setText(String.valueOf(count));
    }

    public void setTotalClassrooms(int count) {
        totalClassroomsLabel.setText(String.valueOf(count));
    }



    public void setTotalAdmins(int count) {
        if (totalAdminsLabel != null) {
            totalAdminsLabel.setText(String.valueOf(count));
        }
    }

    @FXML
    private void handleAdminCardClick(MouseEvent event) {
        loadView("/fxml/user_management.fxml");
    }

    @FXML
    private void handleTeacherCardClick(MouseEvent event) {
        loadView("/fxml/teacher_management.fxml");
    }

    @FXML
    private void handleCourseCardClick(MouseEvent event) {
        loadView("/fxml/course_management.fxml");
    }

    @FXML
    private void handleDepartmentCardClick(MouseEvent event) {
        loadView("/fxml/department_management.fxml");
    }

    @FXML
    private void handleClassroomCardClick(MouseEvent event) {
        loadView("/fxml/schedule_management.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            // Get the parent VBox (mainContainer) from AdminDashboardController
            VBox parentContainer = (VBox) adminCard.getScene().lookup("#mainContainer");

            if (parentContainer == null) {
                // Alternative: navigate up the scene graph to find the main container
                javafx.scene.Node current = adminCard.getParent();
                while (current != null && !(current instanceof VBox && current.getId() != null && current.getId().equals("mainContainer"))) {
                    current = current.getParent();
                }
                if (current instanceof VBox) {
                    parentContainer = (VBox) current;
                }
            }

            if (parentContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent content = loader.load();

                // Determine and apply the appropriate CSS stylesheet based on the FXML path
                String cssFile = determineCSSFile(fxmlPath);
                if (cssFile != null) {
                    var css = getClass().getResource(cssFile);
                    if (css != null) {
                        content.getStylesheets().add(css.toExternalForm());
                    }
                }

                // Remove existing content (keep header at index 0)
                if (parentContainer.getChildren().size() > 1) {
                    parentContainer.getChildren().remove(1, parentContainer.getChildren().size());
                }

                // Add user management content
                parentContainer.getChildren().add(content);
                VBox.setVgrow(content, Priority.ALWAYS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines the appropriate CSS file for the given FXML path
     */
    private String determineCSSFile(String fxmlPath) {
        if (fxmlPath.contains("teacher")) {
            return "/css/teacher_management.css";
        } else if (fxmlPath.contains("course")) {
            return "/css/course_management.css";
        } else if (fxmlPath.contains("department")) {
            return "/css/department_management.css";
        } else if (fxmlPath.contains("schedule") || fxmlPath.contains("classroom")) {
            return "/css/classroom_management.css";
        } else if (fxmlPath.contains("user")) {
            return "/css/user_management.css";
        }
        return null;
    }
}
