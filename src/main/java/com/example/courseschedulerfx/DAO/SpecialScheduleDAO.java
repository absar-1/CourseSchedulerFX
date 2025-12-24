package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.datastructures.Stackk;
import com.example.courseschedulerfx.model.SpecialSchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialScheduleDAO {
    // return a stack of 5 recent special schedules
    public static Stackk<SpecialSchedule> getRecentSpecialSchedules() {
        Stackk<SpecialSchedule> specialScheduleStack = new Stackk<>();

        String query = "SELECT TOP (5) special_schedule_id, course_id, room_id, teacher_id, batch_id, " +
                       "schedule_date, start_time, end_time, type, day_of_week " +
                       "FROM SpecialSchedules ORDER BY schedule_date DESC";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                SpecialSchedule specialSchedule = new SpecialSchedule(
                        rs.getInt("special_schedule_id"),
                        CourseDAO.getCourseById(rs.getInt("course_id")),
                        ClassRoomDAO.getClassRoomById(rs.getInt("room_id")),
                        TeacherDAO.getTeacherById(rs.getInt("teacher_id")),
                        rs.getDate("schedule_date").toLocalDate(),
                        BatchDAO.getBatchByID(rs.getInt("batch_id")),
                        rs.getString("day_of_week"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime(),
                        rs.getString("type")
                );
                specialScheduleStack.push(specialSchedule);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return specialScheduleStack;
    }

    // Get all special schedules
    public static List<SpecialSchedule> getAllSpecialSchedules() {
        List<SpecialSchedule> schedules = new ArrayList<>();
        String query = "SELECT special_schedule_id, course_id, room_id, teacher_id, batch_id, " +
                       "schedule_date, start_time, end_time, type, day_of_week " +
                       "FROM SpecialSchedules ORDER BY schedule_date DESC";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                SpecialSchedule schedule = buildSpecialSchedule(rs);
                schedules.add(schedule);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    // Get special schedule by ID
    public static SpecialSchedule getSpecialScheduleById(int specialScheduleID) {
        String query = "SELECT special_schedule_id, course_id, room_id, teacher_id, batch_id, " +
                       "schedule_date, start_time, end_time, type, day_of_week " +
                       "FROM SpecialSchedules WHERE special_schedule_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, specialScheduleID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return buildSpecialSchedule(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Add new special schedule
    public static boolean addSpecialSchedule(SpecialSchedule schedule) {
        String query = "INSERT INTO SpecialSchedules (course_id, room_id, teacher_id, batch_id, " +
                       "schedule_date, start_time, end_time, type, day_of_week) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, schedule.getCourse().getCourseID());
            ps.setInt(2, schedule.getClassRoom().getRoomID());
            ps.setInt(3, schedule.getTeacher().getTeacherID());
            ps.setInt(4, schedule.getBatch().getBatchID());
            ps.setDate(5, java.sql.Date.valueOf(schedule.getScheduleDate()));
            ps.setTime(6, java.sql.Time.valueOf(schedule.getStartTime()));
            ps.setTime(7, java.sql.Time.valueOf(schedule.getEndTime()));
            ps.setString(8, schedule.getType());
            ps.setString(9, schedule.getDayOfWeek());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Update special schedule
    public static boolean updateSpecialSchedule(SpecialSchedule schedule) {
        String query = "UPDATE SpecialSchedules SET course_id = ?, room_id = ?, teacher_id = ?, " +
                       "batch_id = ?, schedule_date = ?, start_time = ?, end_time = ?, type = ?, day_of_week = ? " +
                       "WHERE special_schedule_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, schedule.getCourse().getCourseID());
            ps.setInt(2, schedule.getClassRoom().getRoomID());
            ps.setInt(3, schedule.getTeacher().getTeacherID());
            ps.setInt(4, schedule.getBatch().getBatchID());
            ps.setDate(5, java.sql.Date.valueOf(schedule.getScheduleDate()));
            ps.setTime(6, java.sql.Time.valueOf(schedule.getStartTime()));
            ps.setTime(7, java.sql.Time.valueOf(schedule.getEndTime()));
            ps.setString(8, schedule.getType());
            ps.setString(9, schedule.getDayOfWeek());
            ps.setInt(10, schedule.getSpecialScheduleID());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Delete special schedule
    public static boolean deleteSpecialSchedule(int specialScheduleID) {
        String query = "DELETE FROM SpecialSchedules WHERE special_schedule_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, specialScheduleID);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to build SpecialSchedule from ResultSet
    private static SpecialSchedule buildSpecialSchedule(ResultSet rs) throws SQLException {
        return new SpecialSchedule(
                rs.getInt("special_schedule_id"),
                CourseDAO.getCourseById(rs.getInt("course_id")),
                ClassRoomDAO.getClassRoomById(rs.getInt("room_id")),
                TeacherDAO.getTeacherById(rs.getInt("teacher_id")),
                rs.getDate("schedule_date").toLocalDate(),
                BatchDAO.getBatchByID(rs.getInt("batch_id")),
                rs.getString("day_of_week"),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                rs.getString("type")
        );
    }
}
