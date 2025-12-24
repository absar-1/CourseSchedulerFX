package com.example.courseschedulerfx.model;

public class Course {
    private int courseID;
    private String courseTitle;
    private int creditHours;
    private Department department; // Database will store departmentID

    public Course(int courseID, String courseTitle, int creditHours, Department department) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
        this.department = department;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public Department getDepartment() {
        return department;
    }

    public void setCourseTitle(String courseTitle) {this.courseTitle = courseTitle;}
    public void setCreditHours(int creditHours) {this.creditHours = creditHours;}
    public void setDepartment(Department department) {this.department = department;}


}
