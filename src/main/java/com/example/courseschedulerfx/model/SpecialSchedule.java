package com.example.courseschedulerfx.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpecialSchedule {
    private int specialScheduleID;
    private Course course;
    private ClassRoom classRoom;
    private Teacher teacher;
    private LocalDate scheduleDate;
    private Batch batch;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String type; // "makeup" or "extra"

    public SpecialSchedule(int specialScheduleID, Course course, ClassRoom classRoom, Teacher teacher,
                           LocalDate scheduleDate, Batch batch, String dayOfWeek, LocalTime startTime, LocalTime endTime, String type) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.specialScheduleID = specialScheduleID;
        this.course = course;
        this.classRoom = classRoom;
        this.teacher = teacher;
        this.scheduleDate = scheduleDate;
        this.batch = batch;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    // constructor for headlines
    public SpecialSchedule(Course course, ClassRoom classroom, Teacher teacher, LocalDate scheduleDate,String type) {
        this.course = course;
        this.classRoom = classroom;
        this.teacher = teacher;
        this.scheduleDate = scheduleDate;
        this.type = type;
    }

    public int getSpecialScheduleID() {
        return specialScheduleID;
    }

    public Course getCourse() {
        return course;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getType() {
        return type;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

}
