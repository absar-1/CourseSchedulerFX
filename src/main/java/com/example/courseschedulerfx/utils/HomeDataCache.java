package com.example.courseschedulerfx.utils;

import com.example.courseschedulerfx.datastructures.Stackk;
import com.example.courseschedulerfx.model.SpecialSchedule;

public class HomeDataCache {
    private static HomeDataCache instance;

    private Integer cachedTeachers;
    private Integer cachedCourses;
    private Integer cachedDepartments;
    private Integer cachedClassrooms;
    private Stackk<SpecialSchedule> cachedSpecialSchedules;
    private long lastCacheTime;
    private static final long CACHE_VALIDITY_MS = 5 * 60 * 1000;

    private HomeDataCache() {
        this.lastCacheTime = 0;
    }

    public static synchronized HomeDataCache getInstance() {
        if (instance == null) {
            instance = new HomeDataCache();
        }
        return instance;
    }

    public boolean isCacheValid() {
        if (cachedTeachers == null || cachedCourses == null ||
            cachedDepartments == null || cachedClassrooms == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastCacheTime) < CACHE_VALIDITY_MS;
    }

    public void setCache(int teachers, int courses, int departments, int classrooms,
                        Stackk<SpecialSchedule> specialSchedules) {
        this.cachedTeachers = teachers;
        this.cachedCourses = courses;
        this.cachedDepartments = departments;
        this.cachedClassrooms = classrooms;
        this.cachedSpecialSchedules = copyStack(specialSchedules);
        this.lastCacheTime = System.currentTimeMillis();
    }

    /**
     * Create a copy of the special schedules stack
     */
    private Stackk<SpecialSchedule> copyStack(Stackk<SpecialSchedule> original) {
        if (original == null) {
            return new Stackk<>();
        }

        // Extract all items from original stack
        Stackk<SpecialSchedule> temp = new Stackk<>();
        Stackk<SpecialSchedule> copy = new Stackk<>();

        // Pop all items and store in temp
        while (!original.isEmpty()) {
            SpecialSchedule item = original.pop();
            temp.push(item);
        }

        // Push items back to original and copy
        while (!temp.isEmpty()) {
            SpecialSchedule item = temp.pop();
            original.push(item);
            copy.push(item);
        }

        return copy;
    }

    public Integer getTeachers() {
        return cachedTeachers;
    }

    public Integer getCourses() {
        return cachedCourses;
    }

    public Integer getDepartments() {
        return cachedDepartments;
    }

    public Integer getClassrooms() {
        return cachedClassrooms;
    }

    public Stackk<SpecialSchedule> getSpecialSchedules() {
        return cachedSpecialSchedules;
    }

    public void clearCache() {
        cachedTeachers = null;
        cachedCourses = null;
        cachedDepartments = null;
        cachedClassrooms = null;
        cachedSpecialSchedules = null;
        lastCacheTime = 0;
    }

    public long getCacheRemainingTime() {
        if (!isCacheValid()) {
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        long remaining = CACHE_VALIDITY_MS - (currentTime - lastCacheTime);
        return remaining / 1000;
    }
}

