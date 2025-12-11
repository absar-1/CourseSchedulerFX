package com.example.courseschedulerfx.model;

public class Schedule {
    private int scheduleID;
    private String dayOfWeek;
    private Course course; // Database will store courseID
    private ClassRoom classRoom; // Database will store roomID
    private ClassSlot classSlot; // Database will store slotID
    private Teacher teacher; // Database will store teacherID
    private boolean isScheduled;

    public Schedule(int scheduleID, String dayOfWeek, Course course, ClassRoom classRoom,
                    ClassSlot classSlot, Teacher teacher, boolean isScheduled) {
        this.scheduleID = scheduleID;
        this.dayOfWeek = dayOfWeek;
        this.course = course;
        this.classRoom = classRoom;
        this.classSlot = classSlot;
        this.teacher = teacher;
        this.isScheduled = isScheduled;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public ClassSlot getClassSlot() {
        return classSlot;
    }

    public void setClassSlot(ClassSlot classSlot) {
        this.classSlot = classSlot;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        isScheduled = scheduled;
    }
}
