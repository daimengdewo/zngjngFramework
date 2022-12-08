package com.awen.energy.protocol;

import lombok.Getter;

@Getter
public enum SendType {

    //单相电流
    ONE_PHASE_CURRENT("110433343535"),
    //三相电流
    THREE_PHASE_CURRENT("110433323535"),
    //电压
    VOLTAGE("110433343435"),
    //功率
    POWER("1104333436351d16"),
    //开闸
    VALVE_UP("1c1035333333348967454d3347773b3a443c"),
    //合闸
    VALVE_DOWN("1c1035333333348967454e3347773b3a443c");

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
