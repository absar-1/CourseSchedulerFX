package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.datastructures.Stackk;
import com.example.courseschedulerfx.model.SpecialSchedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
