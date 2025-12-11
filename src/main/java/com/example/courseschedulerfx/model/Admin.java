package com.example.courseschedulerfx.model;

import com.example.courseschedulerfx.DAO.AdminDAO;

//CREATE TABLE admins (
//        admin_id INT IDENTITY(1,1) PRIMARY KEY,
//admin_name VARCHAR(100),
//email VARCHAR(100) UNIQUE,
//password VARCHAR(255)
//);

public class Admin {
    private int adminID;
    private String adminName;
    private String email;
    private String password;

    public Admin(int adminID, String adminName, String email, String password) {
        this.adminID = adminID;
        this.adminName = adminName;
        this.email = email;
        this.password = password;
    }

    // constructor without ID for new Admins
    public Admin(String adminName, String email, String password) {
        this.adminName = adminName;
        this.email = email;
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }
    }

    public int getAdminID() {
        return adminID;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // method to validate password to have at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character
    public static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }

}
