package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.ClassRoom;

import java.sql.*;
import java.util.List;

public class ClassRoomDAO {
    // count total number of classrooms
    public static int getTotalClassRooms() {
        String query = "SELECT COUNT(*) AS total FROM classrooms";
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

    // get classroom by ID
    public static ClassRoom getClassRoomById(int roomID) {
        String query = "SELECT * FROM classrooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, roomID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ClassRoom(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getInt("capacity")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // get all classrooms
    public static List<ClassRoom> getAllRooms() {
        String query = "SELECT * FROM classrooms ORDER BY room_id";
        List<ClassRoom> rooms = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rooms.add(new ClassRoom(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getInt("capacity")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rooms;
    }

    // add new classroom
    public static boolean addClassRoom(String roomName, int capacity) {
        String query = "INSERT INTO classrooms (room_name, capacity) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, roomName);
            ps.setInt(2, capacity);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // update existing classroom
    public static boolean updateClassRoom(int roomID, String roomName, int capacity) {
        String query = "UPDATE classrooms SET room_name = ?, capacity = ? WHERE room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, roomName);
            ps.setInt(2, capacity);
            ps.setInt(3, roomID);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // delete classroom
    public static String deleteClassRoom(int roomID) {
        // First check if the classroom is being used in any schedules
        String checkQuery = "SELECT COUNT(*) FROM Schedules WHERE room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, roomID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Cannot delete this classroom because it is being used in existing schedules. Please remove all schedules using this classroom first.";
                }
            }

            // If not used, proceed with deletion
            String deleteQuery = "DELETE FROM classrooms WHERE room_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, roomID);
                int rowsDeleted = deleteStmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("[ClassRoomDAO] Classroom deleted successfully - ID: " + roomID);
                    return null; // null means success
                } else {
                    return "Classroom not found.";
                }
            }
        } catch (SQLException e) {
            System.err.println("[ClassRoomDAO] Error deleting classroom: " + e.getMessage());
            e.printStackTrace();
            return "Database error occurred while deleting the classroom.";
        }
    }

    // check if classroom name already exists
    public static boolean classroomExists(String roomName) {
        String query = "SELECT COUNT(*) AS count FROM classrooms WHERE room_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, roomName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
