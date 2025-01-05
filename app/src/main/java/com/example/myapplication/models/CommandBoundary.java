package com.example.myapplication.models;

import java.util.Map;

public class CommandBoundary {
    private CommandId commandId;
    private String command;
    private TargetObject targetObject;
    private String invocationTimestamp;
    private InvokedBy invokedBy;
    private Map<String, Object> commandAttributes;

    // Nested classes
    public static class CommandId {
        private String id;
        private String systemID;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSystemID() {
            return systemID;
        }

        public void setSystemID(String systemID) {
            this.systemID = systemID;
        }
    }

    public static class TargetObject {
        private ObjectBoundary.ObjectId objectId;

        public ObjectBoundary.ObjectId getObjectId() {
            return objectId;
        }

        public void setObjectId(ObjectBoundary.ObjectId objectId) {
            this.objectId = objectId;
        }
    }

    public static class InvokedBy {
        private ObjectBoundary.UserId userId;

        public ObjectBoundary.UserId getUserId() {
            return userId;
        }

        public void setUserId(ObjectBoundary.UserId userId) {
            this.userId = userId;
        }
    }

    // Getters and setters
    public CommandId getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandId commandId) {
        this.commandId = commandId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public TargetObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(TargetObject targetObject) {
        this.targetObject = targetObject;
    }

    public String getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public void setInvocationTimestamp(String invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }
}
