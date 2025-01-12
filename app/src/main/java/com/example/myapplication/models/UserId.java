package com.example.myapplication.models;

import java.util.Objects;

public class UserId {
    private String systemID;
    private String email;

    public UserId() {
    }

    public UserId(String systemID, String email) {
        this.systemID = systemID;
        this.email = email;
    }

    public String getSystemID() {
        return systemID;
    }

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(systemID, userId.systemID) && Objects.equals(email, userId.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, email);
    }

    @Override
    public String toString() {
        return "UserId{" +
                "systemID='" + systemID + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
