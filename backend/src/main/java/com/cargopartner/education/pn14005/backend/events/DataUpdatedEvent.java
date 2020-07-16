package com.cargopartner.education.pn14005.backend.events;

public class DataUpdatedEvent {
    private DataActionType actionType;
    private Long objectId;
    private String jsonString;

    public DataUpdatedEvent(DataActionType actionType, Long objectId, String jsonString) {
        this.actionType = actionType;
        this.objectId = objectId;
        this.jsonString = jsonString;
    }

    public DataActionType getActionType() {
        return actionType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public String getJsonString() {
        return jsonString;
    }
}
