package com.example.myapplication.models;

import java.util.Map;
public class CommandBoundary {
    private CommandId commandId;
    private String command;
    private TargetObject targetObject;
    private String invocationTimestamp;
    private InvokedBy invokedBy;
    private CommandAttributes commandAttributes;

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

    public CommandAttributes getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(CommandAttributes commandAttributes) {
        this.commandAttributes = commandAttributes;
    }

    // Inner classes for nested fields
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
        private ObjectId objectId;

        public ObjectId getObjectId() {
            return objectId;
        }

        public void setObjectId(ObjectId objectId) {
            this.objectId = objectId;
        }

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
    }

    public static class InvokedBy {
        private UserBoundary.UserId userId;

        public UserBoundary.UserId getUserId() {
            return userId;
        }

        public void setUserId(UserBoundary.UserId userId) {
            this.userId = userId;
        }
    }

    public static class CommandAttributes {
        private Object additionalProp1;
        private Object additionalProp2;
        private Object additionalProp3;

        public Object getAdditionalProp1() {
            return additionalProp1;
        }

        public void setAdditionalProp1(Object additionalProp1) {
            this.additionalProp1 = additionalProp1;
        }

        public Object getAdditionalProp2() {
            return additionalProp2;
        }

        public void setAdditionalProp2(Object additionalProp2) {
            this.additionalProp2 = additionalProp2;
        }

        public Object getAdditionalProp3() {
            return additionalProp3;
        }

        public void setAdditionalProp3(Object additionalProp3) {
            this.additionalProp3 = additionalProp3;
        }
    }
}
