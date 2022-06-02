package com.tartarus.petriflowbackend.enu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tartarus.petriflowbackend.util.EnumUtil;

public enum OperationTypeEnu implements NumCodeEnu<OperationTypeEnu> {

    CREATE(1),

    UPDATE(2),

    DELETE(3),

    INIT(4),

    ;

    private final Integer numCode;

    OperationTypeEnu(Integer numCode) {
        this.numCode = numCode;
    }

    @Override
    @JsonValue
    public Integer getNumCode() {
        return numCode;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OperationTypeEnu valueOf(Integer numCode) {
        return EnumUtil.valueOf(numCode, values());
    }
}
