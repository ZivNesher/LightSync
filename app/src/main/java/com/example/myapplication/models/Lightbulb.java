package com.example.myapplication.models;
public class Lightbulb {
    private String id; // Matches backend ObjectBoundary ID
    private String name;
    private boolean isOn;
    private int brightness;
    private int color;

    public Lightbulb(String name, boolean isOn, int brightness, int color) {
        this.name = name;
        this.isOn = isOn;
        this.brightness = brightness;
        this.color = color;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
