package com.example.myapplication.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private String name; // The name of the room (e.g., "Living Room")
    private boolean active; // Whether the room is active
    private List<Lightbulb> lightbulbs; // List of lightbulbs in the room
    private String id;

    // Constructor with only name (new)
    public Room(String name) {
        this.name = name;
        this.active = true; // Default to active
        this.lightbulbs = new ArrayList<>(); // Initialize with an empty list of lightbulbs
    }

    // Constructor with name and active status
    public Room(String name, boolean active) {
        this.name = name;
        this.active = active;
        this.lightbulbs = new ArrayList<>(); // Initialize with an empty list of lightbulbs
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Constructor with name, active status, and lightbulbs
    public Room(String name, boolean active, List<Lightbulb> lightbulbs) {
        this.name = name;
        this.active = active;
        this.lightbulbs = lightbulbs;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Lightbulb> getLightbulbs() {
        return lightbulbs;
    }

    public void setLightbulbs(List<Lightbulb> lightbulbs) {
        this.lightbulbs = lightbulbs;
    }

    // Add a lightbulb
    public void addLightbulb(Lightbulb lightbulb) {
        this.lightbulbs.add(lightbulb);
    }

    // Update a lightbulb
    public void updateLightbulb(int index, Lightbulb lightbulb) {
        this.lightbulbs.set(index, lightbulb);
    }

    // Remove a lightbulb
    public void removeLightbulb(Lightbulb lightbulb) {
        this.lightbulbs.remove(lightbulb);
    }
    public List<Map<String, Object>> getSerializedLightbulbs() {
        List<Map<String, Object>> serialized = new ArrayList<>();
        for (Lightbulb lightbulb : lightbulbs) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", lightbulb.getName());
            map.put("isOn", lightbulb.isOn());
            map.put("brightness", lightbulb.getBrightness());
            map.put("color", lightbulb.getColor());
            serialized.add(map);
        }
        return serialized;
    }


    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", active=" + active +
                ", lightbulbs=" + lightbulbs +
                '}';
    }
}
