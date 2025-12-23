package com.example.courseschedulerfx.DAO;

import com.example.courseschedulerfx.model.*;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    // Get all schedules with related information
    public static List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT s.schedule_id, s.day_of_week, " +
                       "c.course_id, c.course_title, c.credit_hours, cd.department_id, cd.department_name, " +
                       "cr.room_id, cr.room_name, cr.capacity, " +
                       "cls.slot_id, cls.start_time, cls.end_time, " +
                       "t.teacher_id, t.teacher_name, t.teacher_email, td.department_id AS t_dept_id, td.department_name AS t_dept_name, " +
                       "b.batch_id, b.batch_name, b.year_of_admission, bd.department_id AS b_dept_id, bd.department_name AS b_dept_name, b.total_students " +
                       "FROM schedules s " +
                       "JOIN courses c ON s.course_id = c.course_id " +
                       "JOIN departments cd ON c.department_id = cd.department_id " +
                       "JOIN classrooms cr ON s.room_id = cr.room_id " +
                       "JOIN ClassSlots cls ON s.slot_id = cls.slot_id " +
                       "JOIN teachers t ON s.teacher_id = t.teacher_id " +
                       "JOIN departments td ON t.department_id = td.department_id " +
                       "JOIN batches b ON s.batch_id = b.batch_id " +
                       "JOIN departments bd ON b.department_id = bd.department_id " +
                       "ORDER BY s.day_of_week, cls.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Schedule schedule = createScheduleFromResultSet(rs);
                schedules.add(schedule);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    // Get schedule by ID
    public static Schedule getScheduleByID(int scheduleID) {
        String query = "SELECT s.schedule_id, s.day_of_week, " +
                       "c.course_id, c.course_title, c.credit_hours, cd.department_id, cd.department_name, " +
                       "cr.room_id, cr.room_name, cr.capacity, " +
                       "cls.slot_id, cls.start_time, cls.end_time, " +
                       "t.teacher_id, t.teacher_name, t.teacher_email, td.department_id AS t_dept_id, td.department_name AS t_dept_name, " +
                       "b.batch_id, b.batch_name, b.year_of_admission, bd.department_id AS b_dept_id, bd.department_name AS b_dept_name, b.total_students " +
                       "FROM schedules s " +
                       "JOIN courses c ON s.course_id = c.course_id " +
                       "JOIN departments cd ON c.department_id = cd.department_id " +
                       "JOIN classrooms cr ON s.room_id = cr.room_id " +
                       "JOIN ClassSlots cls ON s.slot_id = cls.slot_id " +
                       "JOIN teachers t ON s.teacher_id = t.teacher_id " +
                       "JOIN departments td ON t.department_id = td.department_id " +
                       "JOIN batches b ON s.batch_id = b.batch_id " +
                       "JOIN departments bd ON b.department_id = bd.department_id " +
                       "WHERE s.schedule_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, scheduleID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return createScheduleFromResultSet(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Add new schedule
    public static boolean addSchedule(Schedule schedule) {
        // First check for clashes
        if (checkTeacherClash(schedule)) {
            throw new RuntimeException("Teacher is already scheduled at this time!");
        }
        if (checkRoomClash(schedule)) {
            throw new RuntimeException("Room is already booked at this time!");
        }
        if (checkBatchClash(schedule)) {
            throw new RuntimeException("Batch already has a class at this time!");
        }

        String query = "INSERT INTO schedules (day_of_week, course_id, room_id, slot_id, teacher_id, batch_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, schedule.getDayOfWeek());
            ps.setInt(2, schedule.getCourse().getCourseID());
            ps.setInt(3, schedule.getClassRoom().getRoomID());
            ps.setInt(4, schedule.getClassSlot().getSlotID());
            ps.setInt(5, schedule.getTeacher().getTeacherID());
            ps.setInt(6, schedule.getBatch().getBatchID());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Update schedule
    public static boolean updateSchedule(Schedule schedule) {
        // First check for clashes (excluding the current schedule)
        if (checkTeacherClashExcept(schedule)) {
            throw new RuntimeException("Teacher is already scheduled at this time!");
        }
        if (checkRoomClashExcept(schedule)) {
            throw new RuntimeException("Room is already booked at this time!");
        }
        if (checkBatchClashExcept(schedule)) {
            throw new RuntimeException("Batch already has a class at this time!");
        }

        String query = "UPDATE schedules SET day_of_week = ?, course_id = ?, room_id = ?, slot_id = ?, teacher_id = ?, batch_id = ? WHERE schedule_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, schedule.getDayOfWeek());
            ps.setInt(2, schedule.getCourse().getCourseID());
            ps.setInt(3, schedule.getClassRoom().getRoomID());
            ps.setInt(4, schedule.getClassSlot().getSlotID());
            ps.setInt(5, schedule.getTeacher().getTeacherID());
            ps.setInt(6, schedule.getBatch().getBatchID());
            ps.setInt(7, schedule.getScheduleID());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Delete schedule
    public static boolean deleteSchedule(int scheduleID) {
        String query = "DELETE FROM schedules WHERE schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, scheduleID);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Clash detection methods

    // Check if teacher is already scheduled at the same day and time
    private static boolean checkTeacherClash(Schedule schedule) {
        String query = "SELECT COUNT(*) AS count FROM schedules s " +
                       "WHERE s.teacher_id = ? AND s.day_of_week = ? AND s.slot_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, schedule.getTeacher().getTeacherID());
            ps.setString(2, schedule.getDayOfWeek());
            ps.setInt(3, schedule.getClassSlot().getSlotID());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Check if teacher clash excluding current schedule (for updates)
    private static boolean checkTeacherClashExcept(Schedule schedule) {
        String query = "SELECT COUNT(*) AS count FROM schedules s " +
                       "WHERE s.teacher_id = ? AND s.day_of_week = ? AND s.slot_id = ? AND s.schedule_id != ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, schedule.getTeacher().getTeacherID());
            ps.setString(2, schedule.getDayOfWeek());
            ps.setInt(3, schedule.getClassSlot().getSlotID());
            ps.setInt(4, schedule.getScheduleID());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Check if room is already booked at the same day and time
    private static boolean checkRoomClash(Schedule schedule) {
        String query = "SELECT COUNT(*) AS count FROM schedules s " +
                       "WHERE s.room_id = ? AND s.day_of_week = ? AND s.slot_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, schedule.getClassRoom().getRoomID());
            ps.setString(2, schedule.getDayOfWeek());
            ps.setInt(3, schedule.getClassSlot().getSlotID());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Check if room clash excluding current schedule (for updates)
    private static boolean checkRoomClashExcept(Schedule schedule) {
        String query = "SELECT COUNT(*) AS count FROM schedules s " +
                       "WHERE s.room_id = ? AND s.day_of_week = ? AND s.slot_id = ? AND s.schedule_id != ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, schedule.getClassRoom().getRoomID());
            ps.setString(2, schedule.getDayOfWeek());
            ps.setInt(3, schedule.getClassSlot().getSlotID());
            ps.setInt(4, schedule.getScheduleID());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Check if batch already has a class at the same day and time
    private static boolean checkBatchClash(Schedule schedule) {
        String query = "SELECT COUNT(*) AS count FROM schedules s " +
                       "WHERE s.batch_id = ? AND s.day_of_week = ? AND s.slot_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, schedule.getBatch().getBatchID());
            ps.setString(2, schedule.getDayOfWeek());
            ps.setInt(3, schedule.getClassSlot().getSlotID());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Check if batch clash excluding current schedule (for updates)
    private static boolean checkBatchClashExcept(Schedule schedule) {
        String query = "SELECT COUNT(*) AS count FROM schedules s " +
                       "WHERE s.batch_id = ? AND s.day_of_week = ? AND s.slot_id = ? AND s.schedule_id != ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, schedule.getBatch().getBatchID());
            ps.setString(2, schedule.getDayOfWeek());
            ps.setInt(3, schedule.getClassSlot().getSlotID());
            ps.setInt(4, schedule.getScheduleID());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //

    // Search by teacher
    public static List<Schedule> searchByTeacher(int teacherID) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT s.schedule_id, s.day_of_week, " +
                       "c.course_id, c.course_title, c.credit_hours, cd.department_id, cd.department_name, " +
                       "cr.room_id, cr.room_name, cr.capacity, " +
                       "cls.slot_id, cls.start_time, cls.end_time, " +
                       "t.teacher_id, t.teacher_name, t.teacher_email, td.department_id AS t_dept_id, td.department_name AS t_dept_name, " +
                       "b.batch_id, b.batch_name, b.year_of_admission, bd.department_id AS b_dept_id, bd.department_name AS b_dept_name, b.total_students " +
                       "FROM schedules s " +
                       "JOIN courses c ON s.course_id = c.course_id " +
                       "JOIN departments cd ON c.department_id = cd.department_id " +
                       "JOIN classrooms cr ON s.room_id = cr.room_id " +
                       "JOIN ClassSlots cls ON s.slot_id = cls.slot_id " +
                       "JOIN teachers t ON s.teacher_id = t.teacher_id " +
                       "JOIN departments td ON t.department_id = td.department_id " +
                       "JOIN batches b ON s.batch_id = b.batch_id " +
                       "JOIN departments bd ON b.department_id = bd.department_id " +
                       "WHERE s.teacher_id = ? ORDER BY s.day_of_week, cls.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, teacherID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule schedule = createScheduleFromResultSet(rs);
                schedules.add(schedule);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    // Search by course
    public static List<Schedule> searchByCourse(int courseID) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT s.schedule_id, s.day_of_week, " +
                       "c.course_id, c.course_title, c.credit_hours, cd.department_id, cd.department_name, " +
                       "cr.room_id, cr.room_name, cr.capacity, " +
                       "cls.slot_id, cls.start_time, cls.end_time, " +
                       "t.teacher_id, t.teacher_name, t.teacher_email, td.department_id AS t_dept_id, td.department_name AS t_dept_name, " +
                       "b.batch_id, b.batch_name, b.year_of_admission, bd.department_id AS b_dept_id, bd.department_name AS b_dept_name, b.total_students " +
                       "FROM schedules s " +
                       "JOIN courses c ON s.course_id = c.course_id " +
                       "JOIN departments cd ON c.department_id = cd.department_id " +
                       "JOIN classrooms cr ON s.room_id = cr.room_id " +
                       "JOIN ClassSlots cls ON s.slot_id = cls.slot_id " +
                       "JOIN teachers t ON s.teacher_id = t.teacher_id " +
                       "JOIN departments td ON t.department_id = td.department_id " +
                       "JOIN batches b ON s.batch_id = b.batch_id " +
                       "JOIN departments bd ON b.department_id = bd.department_id " +
                       "WHERE s.course_id = ? ORDER BY s.day_of_week, cls.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, courseID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule schedule = createScheduleFromResultSet(rs);
                schedules.add(schedule);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    // Search by batch
    public static List<Schedule> searchByBatch(int batchID) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT s.schedule_id, s.day_of_week, " +
                       "c.course_id, c.course_title, c.credit_hours, cd.department_id, cd.department_name, " +
                       "cr.room_id, cr.room_name, cr.capacity, " +
                       "cls.slot_id, cls.start_time, cls.end_time, " +
                       "t.teacher_id, t.teacher_name, t.teacher_email, td.department_id AS t_dept_id, td.department_name AS t_dept_name, " +
                       "b.batch_id, b.batch_name, b.year_of_admission, bd.department_id AS b_dept_id, bd.department_name AS b_dept_name, b.total_students " +
                       "FROM schedules s " +
                       "JOIN courses c ON s.course_id = c.course_id " +
                       "JOIN departments cd ON c.department_id = cd.department_id " +
                       "JOIN classrooms cr ON s.room_id = cr.room_id " +
                       "JOIN ClassSlots cls ON s.slot_id = cls.slot_id " +
                       "JOIN teachers t ON s.teacher_id = t.teacher_id " +
                       "JOIN departments td ON t.department_id = td.department_id " +
                       "JOIN batches b ON s.batch_id = b.batch_id " +
                       "JOIN departments bd ON b.department_id = bd.department_id " +
                       "WHERE s.batch_id = ? ORDER BY s.day_of_week, cls.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, batchID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule schedule = createScheduleFromResultSet(rs);
                schedules.add(schedule);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    // Search by day of week
    public static List<Schedule> searchByDay(String dayOfWeek) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT s.schedule_id, s.day_of_week, " +
                       "c.course_id, c.course_title, c.credit_hours, cd.department_id, cd.department_name, " +
                       "cr.room_id, cr.room_name, cr.capacity, " +
                       "cls.slot_id, cls.start_time, cls.end_time, " +
                       "t.teacher_id, t.teacher_name, t.teacher_email, td.department_id AS t_dept_id, td.department_name AS t_dept_name, " +
                       "b.batch_id, b.batch_name, b.year_of_admission, bd.department_id AS b_dept_id, bd.department_name AS b_dept_name, b.total_students " +
                       "FROM schedules s " +
                       "JOIN courses c ON s.course_id = c.course_id " +
                       "JOIN departments cd ON c.department_id = cd.department_id " +
                       "JOIN classrooms cr ON s.room_id = cr.room_id " +
                       "JOIN ClassSlots cls ON s.slot_id = cls.slot_id " +
                       "JOIN teachers t ON s.teacher_id = t.teacher_id " +
                       "JOIN departments td ON t.department_id = td.department_id " +
                       "JOIN batches b ON s.batch_id = b.batch_id " +
                       "JOIN departments bd ON b.department_id = bd.department_id " +
                       "WHERE s.day_of_week = ? ORDER BY cls.start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, dayOfWeek);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule schedule = createScheduleFromResultSet(rs);
                schedules.add(schedule);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    // Helper method to create Schedule object from ResultSet
    private static Schedule createScheduleFromResultSet(ResultSet rs) throws SQLException {
        Department courseDept = new Department(
                rs.getInt("department_id"),
                rs.getString("department_name")
        );
        Course course = new Course(
                rs.getInt("course_id"),
                rs.getString("course_title"),
                rs.getInt("credit_hours"),
                courseDept
        );

        ClassRoom classRoom = new ClassRoom(
                rs.getInt("room_id"),
                rs.getString("room_name"),
                rs.getInt("capacity")
        );

        ClassSlot classSlot = new ClassSlot(
                rs.getInt("slot_id"),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime()
        );

        Department teacherDept = new Department(
                rs.getInt("t_dept_id"),
                rs.getString("t_dept_name")
        );
        Teacher teacher = new Teacher(
                rs.getInt("teacher_id"),
                rs.getString("teacher_name"),
                rs.getString("teacher_email"),
                teacherDept
        );

        Department batchDept = new Department(
                rs.getInt("b_dept_id"),
                rs.getString("b_dept_name")
        );
        Batch batch = new Batch(
                rs.getInt("batch_id"),
                rs.getString("batch_name"),
                rs.getInt("year_of_admission"),
                batchDept,
                rs.getInt("total_students")
        );

        return new Schedule(
                rs.getInt("schedule_id"),
                rs.getString("day_of_week"),
                course,
                classRoom,
                classSlot,
                teacher,
                batch
        );
    }
}
