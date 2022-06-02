package com.tartarus.petriflowbackend.enu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tartarus.petriflowbackend.util.EnumUtil;

public enum UpdateTypeEnu implements NumCodeEnu<UpdateTypeEnu> {

    MOVE(1),

    LABEL(2),

    TOKEN(3),

    FORM(4),

    COLUMNS(5),

    DATA_REF(6),

    ;

    private final Integer numCode;

    UpdateTypeEnu(Integer numCode) {
        this.numCode = numCode;
    }

    @Override
    @JsonValue
    public Integer getNumCode() {
        return numCode;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UpdateTypeEnu valueOf(Integer numCode) {
        return EnumUtil.valueOf(numCode, values());
    }
}
