package com.example.myapplication.models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name; // The name of the room (e.g., "Living Room")
    private List<Lightbulb> lightbulbs; // List of lightbulbs in the room

    // Constructor
    public Room(String name) {
        this.name = name;
        this.lightbulbs = new ArrayList<>(); // Initialize with an empty list
    }

    public Room(String name, List<Lightbulb> lightbulbs) {
        this.name = name;
        this.lightbulbs = lightbulbs;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Lightbulb> getLightbulbs() {
        return lightbulbs;
    }

    public void setLightbulbs(List<Lightbulb> lightbulbs) {
        this.lightbulbs = lightbulbs;
    }

    // Add a lightbulb to the room
    public void addLightbulb(Lightbulb lightbulb) {
        lightbulbs.add(lightbulb);
    }

    // Remove a lightbulb from the room
    public boolean removeLightbulb(Lightbulb lightbulb) {
        return lightbulbs.remove(lightbulb);
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", lightbulbs=" + lightbulbs +
                '}';
    }
}
