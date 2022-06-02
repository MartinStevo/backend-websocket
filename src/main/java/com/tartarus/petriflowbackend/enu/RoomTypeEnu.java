package com.tartarus.petriflowbackend.enu;

public enum RoomTypeEnu {

    MODELER("modeler"),

    FORM_BUILDER("formbuilder"),

    ;

    private final String code;

    RoomTypeEnu(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
