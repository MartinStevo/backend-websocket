package com.tartarus.petriflowbackend.util;

import com.tartarus.petriflowbackend.enu.NumCodeEnu;

public class EnumUtil {

    public static <T extends NumCodeEnu> T valueOf(Integer numCode, T[] values) {
        if (numCode == null) {
            return null;
        }

        for (T value : values) {
            if (numCode.equals(value.getNumCode())) {
                return value;
            }
        }

        return null;
    }
}
