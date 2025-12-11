package com.example.courseschedulerfx.DAO;

import java.sql.Connection;
import java. sql.DriverManager;
import java. sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:sqlserver://course-scheduler-server.database.windows.net:1433;database=CourseSchedulerDB;" +
            "user=adminUser@course-scheduler-server;password=cOursescHeduler123;" +
            "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
            "loginTimeout=30;";

    private static final String USER = "adminUser";
    private static final String PASSWORD = "cOursescHeduler123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System. out.println("Connected to Azure SQL Database successfully!");
        } catch (SQLException e) {
            System. out.println("Connection failed!");
            e. printStackTrace();
        }
    }
}