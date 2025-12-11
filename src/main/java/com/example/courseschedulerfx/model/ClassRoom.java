package com.example.courseschedulerfx.model;

public class ClassRoom {
    private int roomID;
    private String roomName;
    private int capacity;

    public ClassRoom(int roomID, String roomName, int capacity) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public int getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getCapacity() {
        return capacity;
    }
}
