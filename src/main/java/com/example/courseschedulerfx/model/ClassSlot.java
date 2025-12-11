package com.example.courseschedulerfx.model;

import java.time.LocalTime;

public class ClassSlot {
    private int slotID;
    private LocalTime startTime;
    private LocalTime endTime;

    public ClassSlot(int slotID, LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.slotID = slotID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSlotID() {
        return slotID;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
