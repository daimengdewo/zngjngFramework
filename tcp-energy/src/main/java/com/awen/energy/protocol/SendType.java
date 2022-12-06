package com.awen.energy.protocol;

import lombok.Getter;

@Getter
public enum SendType {

    //操作标识
    ONE_PHASE_CURRENT("110433343535"), THREE_PHASE_CURRENT("110433323535"),
    VOLTAGE("110433343435");

    private final String type;

    SendType(String type) {
        this.type = type;
    }

    //返回枚举对象
    public static <T extends SendType> T getByType(String type, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (type.equals(each.getType())) {
                return each;
            }
        }
        return null;
    }
}
