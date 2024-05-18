package com.example.attendancetracker.model;

public class AttendanceRecord {
    private String recordId;
    private String studentId;
    private String teacherId;
    private String date;
    private boolean isPresent;

    // Constructors, getters, and setters
    public AttendanceRecord() {}

    public AttendanceRecord(String recordId, String studentId, String teacherId, String date, boolean isPresent) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.date = date;
        this.isPresent = isPresent;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }


}
