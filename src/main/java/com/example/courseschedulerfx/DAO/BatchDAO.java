package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.Batch;
import com.example.courseschedulerfx.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchDAO {
    // return batch by batchID
    public static Batch getBatchByID(int batchID) {
        String query = "SELECT * FROM batches WHERE batch_id = ?" ;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ps.setInt(1, batchID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Department department = DepartmentDAO.getDepartmentById(rs.getInt("department_id"));
                return new Batch(
                        rs.getInt("batch_id"),
                        rs.getString("batch_name"),
                        rs.getInt("year_of_admission"),
                        department,
                        rs.getInt("total_students")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // get all batches from database
    public static List<Batch> getAllBatches() {
        List<Batch> batches = new ArrayList<>();
        String query = "SELECT b.*, d.department_id, d.department_name " +
                       "FROM batches b " +
                       "LEFT JOIN departments d ON b.department_id = d.department_id " +
                       "ORDER BY b.batch_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                Batch batch = new Batch(
                        rs.getInt("batch_id"),
                        rs.getString("batch_name"),
                        rs.getInt("year_of_admission"),
                        department,
                        rs.getInt("total_students")
                );
                batches.add(batch);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return batches;
    }

    // count total number of batches
    public static int countBatches() {
        String query = "SELECT COUNT(*) AS total FROM batches";
        int totalBatches = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalBatches = rs.getInt("total");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return totalBatches;
    }

    // add new batch to database
    public static boolean addBatch(Batch batch) {
        String query = "INSERT INTO batches (batch_name, year_of_admission, department_id, total_students) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, batch.getBatchName());
            ps.setInt(2, batch.getYearOfAdmission());
            ps.setInt(3, batch.getDepartment().getDepartmentID());
            ps.setInt(4, batch.getTotalStudents());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // delete batch from database by ID
    public static boolean deleteBatch(int batchID) {
        String query = "DELETE FROM batches WHERE batch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, batchID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // update batch in database
    public static boolean updateBatch(Batch batch) {
        String query = "UPDATE batches SET batch_name = ?, year_of_admission = ?, department_id = ?, total_students = ? WHERE batch_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, batch.getBatchName());
            ps.setInt(2, batch.getYearOfAdmission());
            ps.setInt(3, batch.getDepartment().getDepartmentID());
            ps.setInt(4, batch.getTotalStudents());
            ps.setInt(5, batch.getBatchID());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // check if batch already exists (by year and department)
    public static boolean batchExists(int yearOfAdmission, int departmentID) {
        String query = "SELECT COUNT(*) AS count FROM batches WHERE year_of_admission = ? AND department_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, yearOfAdmission);
            ps.setInt(2, departmentID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // check if batch exists excluding a specific batch ID (for edit validation)
    public static boolean batchExistsExcept(int yearOfAdmission, int departmentID, int excludeBatchID) {
        String query = "SELECT COUNT(*) AS count FROM batches WHERE year_of_admission = ? AND department_id = ? AND batch_id != ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, yearOfAdmission);
            ps.setInt(2, departmentID);
            ps.setInt(3, excludeBatchID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
