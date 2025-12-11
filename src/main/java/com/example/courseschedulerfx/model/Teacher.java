package com.example.courseschedulerfx.model;

public class Teacher {
    private int teacherID;
    private String teacherName;
    private String teacherEmail;
    private Department department; // Database will store departmentID

    public Teacher(int teacherID, String teacherName, String teacherEmail, Department department) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.department = department;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public Department getDepartment() {
        return department;
    }
}
