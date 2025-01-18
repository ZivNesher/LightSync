package com.example.myapplication.models;

import java.util.Map;

public class ObjectBoundary {
    private ObjectId objectId;
    private String type;
    private String alias;
    private String status;
    private Location location;
    private boolean active;
    private CreatedBy createdBy;
    private Map<String, Object> objectDetails;
    private String creationTimeStamp;

    // Nested classes
    public static class ObjectId {
        private String id;
        private String systemId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }
    }

    public static class Location {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    public static class UserId {
        private String systemId;
        private String email;

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // Getters and setters
    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }

    public String getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(String creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }
}
