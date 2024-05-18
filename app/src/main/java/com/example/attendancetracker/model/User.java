package com.example.attendancetracker.model;


public class User {
        private String userId;
        private String username;
        private String email;
        private boolean isTeacher;

    public User() {
    }
    public User(String userId, String username, boolean isTeacher) {
        this.userId = userId;
        this.username = username;
        this.isTeacher = isTeacher;
    }

    public User(String userId, String username, String email, boolean isTeacher) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.isTeacher = isTeacher;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }
// Constructors, getters, and setters
    }

