package com.example.myapplication.models;

public class Lightbulb {
    private String name;  // The name of the lightbulb (e.g., "Ceiling Light")
    private boolean isOn; // Whether the lightbulb is on or off
    private int brightness; // Brightness level from 0 to 100

    // Constructor
    public Lightbulb(String name, boolean isOn, int brightness) {
        this.name = name;
        this.isOn = isOn;
        this.brightness = brightness;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    @Override
    public String toString() {
        return "Lightbulb{" +
                "name='" + name + '\'' +
                ", isOn=" + isOn +
                ", brightness=" + brightness +
                '}';
    }
}
