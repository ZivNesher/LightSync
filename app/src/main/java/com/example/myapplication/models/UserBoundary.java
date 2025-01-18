package com.example.myapplication.models;

import com.example.myapplication.data.RoleEnum;

public class UserBoundary {
    private UserId userId;
    private RoleEnum role;
    private String username;
    private String avatar;

    // Getters and setters
    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
