package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    // login validation for admin users
    public static Admin validateAdmin(String email, String password){
        String query = "SELECT * FROM admins WHERE email = ? AND password = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Admin(
                        rs.getInt("admin_id"),
                        rs.getString("admin_name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // count total number of admins
    public static int countAdmins() {
        String query = "SELECT COUNT(*) AS total FROM admins";
        int totalAdmins = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalAdmins = rs.getInt("total");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return totalAdmins;
    }

    // add new admin to database
    public static boolean addAdmin(Admin admin) {
        String query = "INSERT INTO admins (admin_name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, admin.getAdminName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPassword());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // delete admin from database by ID
    public static boolean deleteAdmin(int adminID) {
        String query = "DELETE FROM admins WHERE admin_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, adminID);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
