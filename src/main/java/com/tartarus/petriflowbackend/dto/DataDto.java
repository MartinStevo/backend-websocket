package com.tartarus.petriflowbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tartarus.petriflowbackend.enu.ObjectTypeEnu;
import com.tartarus.petriflowbackend.enu.OperationTypeEnu;
import com.tartarus.petriflowbackend.enu.UpdateTypeEnu;

import java.io.Serializable;

//TODO rename
public class DataDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String objectID;

    @JsonProperty(value = "operationType")
    private OperationTypeEnu operationType;

    @JsonProperty(value = "objectType")
    private ObjectTypeEnu objectType;

    @JsonProperty(value = "updateType")
    private UpdateTypeEnu updateType;

    private Object object;

    public DataDto() {
    }

    public DataDto(String objectID,
                   OperationTypeEnu operationType,
                   ObjectTypeEnu objectType,
                   UpdateTypeEnu updateType,
                   Object object) {

        this.objectID = objectID;
        this.operationType = operationType;
        this.objectType = objectType;
        this.updateType = updateType;
        this.object = object;
    }

    @Override
    public String toString() {
        return "Data{" +
                "objectID='" + objectID + '\'' +
                ", operationType=" + operationType +
                ", objectType=" + objectType +
                ", updateType=" + updateType +
                ", object=" + object +
                '}';
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public OperationTypeEnu getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeEnu operationType) {
        this.operationType = operationType;
    }

    public ObjectTypeEnu getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectTypeEnu objectType) {
        this.objectType = objectType;
    }

    public UpdateTypeEnu getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateTypeEnu updateType) {
        this.updateType = updateType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}