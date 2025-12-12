package com.example.courseschedulerfx.model;

import java.time.LocalDate;

public class Batch {
    private int batchID;
    private String batchName;
    private int yearOfAdmission;
    private Department department;
    private int totalStudents;

    // constructor for retrieving from database
    public Batch(int batchID, String batchName, int yearOfAdmission, Department department, int totalStudents) {
        this.batchID = batchID;
        this.batchName = batchName;
        this.yearOfAdmission = yearOfAdmission;
        this.department = department;
        this.totalStudents = totalStudents;
    }

    // constructor for creating new batch
    public Batch(int yearOfAdmission, Department department, int totalStudents) {
        this.batchName = department.getDepartmentName() + "-" + yearOfAdmission;
        this.yearOfAdmission = yearOfAdmission;
        this.department = department;
        this.totalStudents = totalStudents;
    }

    public int getBatchID() {
        return batchID;
    }

    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getYearOfAdmission() {
        return yearOfAdmission;
    }

    public void setYearOfAdmission(int yearOfAdmission) {
        this.yearOfAdmission = yearOfAdmission;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }
}
