package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.ClassSlot;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClassSlotDAO {

    // Get all class slots from the database
    public static List<ClassSlot> getAllSlots() {
        List<ClassSlot> slots = new ArrayList<>();
        String query = "SELECT slot_id, start_time, end_time FROM ClassSlots ORDER BY start_time";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ClassSlot slot = new ClassSlot(
                        rs.getInt("slot_id"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime()
                );
                slots.add(slot);
            }

            System.out.println("[ClassSlotDAO] Retrieved " + slots.size() + " slots from database");

        } catch (SQLException e) {
            System.err.println("[ClassSlotDAO] Error retrieving slots: " + e.getMessage());
            e.printStackTrace();
        }

        return slots;
    }

    // Get slot by ID
    public static ClassSlot getSlotByID(int slotID) {
        String query = "SELECT slot_id, start_time, end_time FROM ClassSlots WHERE slot_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, slotID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ClassSlot slot = new ClassSlot(
                            rs.getInt("slot_id"),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime()
                    );
                    System.out.println("[ClassSlotDAO] Retrieved slot ID: " + slotID + ", Time: " + slot.getStartTime() + " - " + slot.getEndTime());
                    return slot;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ClassSlotDAO] Error retrieving slot by ID: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("[ClassSlotDAO] Slot not found - ID: " + slotID);
        return null;
    }

    // Add a new class slot to the database
    public static boolean addSlot(LocalTime startTime, LocalTime endTime) {
        // Validate input
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            System.err.println("[ClassSlotDAO] Invalid time range: start time must be before end time");
            return false;
        }

        String insertQuery = "INSERT INTO ClassSlots (start_time, end_time) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTime(1, java.sql.Time.valueOf(startTime));
            pstmt.setTime(2, java.sql.Time.valueOf(endTime));

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newSlotID = rs.getInt(1);
                        System.out.println("[ClassSlotDAO] Slot added successfully - ID: " + newSlotID + ", Time: " + startTime + " - " + endTime);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ClassSlotDAO] Error adding slot: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing class slot
    public static boolean updateSlot(int slotID, LocalTime startTime, LocalTime endTime) {
        // Validate input
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            System.err.println("[ClassSlotDAO] Invalid time range: start time must be before end time");
            return false;
        }

        String updateQuery = "UPDATE ClassSlots SET start_time = ?, end_time = ? WHERE slot_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setTime(1, java.sql.Time.valueOf(startTime));
            pstmt.setTime(2, java.sql.Time.valueOf(endTime));
            pstmt.setInt(3, slotID);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("[ClassSlotDAO] Slot updated successfully - ID: " + slotID + ", New Time: " + startTime + " - " + endTime);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ClassSlotDAO] Error updating slot: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Delete a class slot
    public static String deleteSlot(int slotID) {
        // First check if the slot is being used in any schedules
        String checkQuery = "SELECT COUNT(*) FROM Schedules WHERE slot_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, slotID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Cannot delete this time slot because it is being used in existing schedules. Please remove all schedules using this slot first.";
                }
            }

            // If not used, proceed with deletion
            String deleteQuery = "DELETE FROM ClassSlots WHERE slot_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, slotID);
                int rowsDeleted = deleteStmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("[ClassSlotDAO] Slot deleted successfully - ID: " + slotID);
                    return null; // null means success
                } else {
                    return "Time slot not found.";
                }
            }
        } catch (SQLException e) {
            System.err.println("[ClassSlotDAO] Error deleting slot: " + e.getMessage());
            e.printStackTrace();
            return "Database error occurred while deleting the time slot.";
        }
    }

    // Check if a time slot is valid
    public static boolean isTimeSlotAvailable(LocalTime startTime, LocalTime endTime, int excludeSlotID) {
        // Only reject if start time is after or equal to end time
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            return false;
        }

        return true;
    }
}
