package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.Course;
import com.example.courseschedulerfx.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        }catch (Exception e) {
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

    // get all courses
    public static List<Course> getAllCourses() {
        String query = "SELECT c.course_id, c.course_title, c.credit_hours, d.department_id, d.department_name " +
                       "FROM courses c " +
                       "JOIN departments d ON c.department_id = d.department_id";
        List<Course> courses = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_title"),
                        rs.getInt("credit_hours"),
                        department
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return courses;
    }
}
