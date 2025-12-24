package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.Course;
import com.example.courseschedulerfx.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    // count courses from database
    public static int getCourseCount() {
        String query = "SELECT COUNT(*) AS total FROM courses";
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    // get course by ID
    public static Course getCourseById(int courseID) {
        String query = "SELECT c.course_id, c.course_title, c.credit_hours, d.department_id, d.department_name " +
                "FROM courses c " +
                "JOIN departments d ON c.department_id = d.department_id " +
                "WHERE c.course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, courseID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                return new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_title"),
                        rs.getInt("credit_hours"),
                        department
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static List<Course> getCourseList() {
        String query = "SELECT c.course_id, c.course_title, c.credit_hours, " +
                       "d.department_id, d.department_name " +
                       "FROM Courses c " +
                       "JOIN Departments d ON c.department_id = d.department_id " +
                       "ORDER BY c.course_id";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            List<Course> courses = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                courses.add(
                        new Course(
                                rs.getInt("course_id"),
                                rs.getString("course_title"),
                                rs.getInt("credit_hours"),
                                department
                        )
                );
            }
            return courses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addCourse(String courseTitle, int creditHours, String deptName) {
        String query = "INSERT INTO Courses (course_title, credit_hours, department_id) VALUES (?,?,?)";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, courseTitle);
            stmt.setInt(2, creditHours);
            stmt.setInt(3, DepartmentDAO.getDepartmentIdByName(deptName));
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteCourse(int courseID) {
        String query = "DELETE FROM Courses WHERE course_id = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, courseID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean editCourse(Course course){
        String query = "UPDATE Courses SET course_title = ?, credit_hours = ?, department_id = ? WHERE course_id = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, course.getCourseTitle());
            stmt.setInt(2, course.getCreditHours());
            stmt.setInt(3, course.getDepartment().getDepartmentID());
            stmt.setInt(4, course.getCourseID());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
