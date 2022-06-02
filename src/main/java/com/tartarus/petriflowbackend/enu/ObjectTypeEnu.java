package com.tartarus.petriflowbackend.enu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tartarus.petriflowbackend.util.EnumUtil;

public enum ObjectTypeEnu implements NumCodeEnu<ObjectTypeEnu> {

    ARC(1),

    PLACE(2),

    ROLE(3),

    TRANSACTION(4),

    DATA(5),

    DOCUMENT(6),

    I18N(7),

    TRANSITION(8),

    FORM_BUILDER(9),

    MODEL(10),

    DATA_FIELD(11),

    DATA_REF(12),

    ;

    private final Integer numCode;

    ObjectTypeEnu(Integer numCode) {
        this.numCode = numCode;
    }

    @Override
    @JsonValue
    public Integer getNumCode() {
        return numCode;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ObjectTypeEnu valueOf(Integer numCode) {
        return EnumUtil.valueOf(numCode, values());
    }
}
