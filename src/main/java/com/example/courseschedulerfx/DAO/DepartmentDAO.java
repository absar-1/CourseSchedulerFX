package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    // count total departments
    public static int getTotalDepartments() {
        String query = "SELECT COUNT(*) AS total FROM departments";
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

    // get department by id
    public static Department getDepartmentById(int id) {
        String query = "SELECT * FROM departments WHERE department_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // get all departments
    public static List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM departments ORDER BY department_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                departments.add(department);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return departments;
    }

    public static boolean addDepartment(Department department) {
        String query = "INSERT INTO departments (department_name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, department.getDepartmentName());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteDepartment(int departmentID) {
        String query = "DELETE FROM departments WHERE department_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, departmentID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updateDepartment(Department department) {
        String query = "UPDATE departments SET department_name = ? WHERE department_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, department.getDepartmentName());
            ps.setInt(2, department.getDepartmentID());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAllDepartmentNames() {
        List<String> departmentNames = new ArrayList<>();
        String query = "SELECT department_name FROM departments ORDER BY department_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                departmentNames.add(rs.getString("department_name"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return departmentNames;
    }

    public static int getDepartmentIdByName(String departmentName) {
        String query = "SELECT department_id FROM departments WHERE department_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, departmentName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("department_id");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
