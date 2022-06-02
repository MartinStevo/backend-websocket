package com.tartarus.petriflowbackend.model;

import com.tartarus.petriflowbackend.enu.ObjectTypeEnu;

import java.io.Serializable;

public class PetriNetObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private ObjectTypeEnu objectType;

    private Object object;

    public PetriNetObject() {
    }

    public PetriNetObject(String id, ObjectTypeEnu objectType, Object object) {
        this.id = id;
        this.objectType = objectType;
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectTypeEnu getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectTypeEnu objectType) {
        this.objectType = objectType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
