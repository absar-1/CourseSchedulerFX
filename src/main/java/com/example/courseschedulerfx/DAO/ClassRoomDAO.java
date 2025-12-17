package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.ClassRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
