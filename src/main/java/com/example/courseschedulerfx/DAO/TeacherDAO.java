package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.Department;
import com.example.courseschedulerfx.model.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TeacherDAO {

    // count of teachers in database
    public static int getTeacherCount() {
        String query = "SELECT COUNT(*) AS teacherCount FROM teachers";
        int count = 0;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                count = rs.getInt("teacherCount");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    // get teacher by ID
    public static Teacher getTeacherById(int teacherID) {
        String query = "SELECT t.teacher_id, t.teacher_name, t.teacher_email, d.department_id, d.department_name " +
                       "FROM teachers t " +
                       "JOIN departments d ON t.department_id = d.department_id " +
                       "WHERE t.teacher_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ps.setInt(1, teacherID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                return new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("teacher_name"),
                        rs.getString("teacher_email"),
                        department
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // get all teachers
    public static List<Teacher> getAllTeachers() {
        String query = "SELECT t.teacher_id, t.teacher_name, t.teacher_email, d.department_id, d.department_name " +
                       "FROM teachers t " +
                       "JOIN departments d ON t.department_id = d.department_id";
        List<Teacher> teachers = new java.util.ArrayList<>();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                teachers.add(new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("teacher_name"),
                        rs.getString("teacher_email"),
                        department
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return teachers;
    }
}
