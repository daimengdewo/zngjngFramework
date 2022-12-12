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
    POWER("110433343635"),

    //总有功电量
    ALL_ELECTRICITY("110433333333"),

    //尖
    ELECTRICITY_A("110433343333"),

    //峰
    ELECTRICITY_B("110433353333"),

    //平
    ELECTRICITY_C("110433363333"),

    //谷
    ELECTRICITY_D("110433373333"),

    //开闸
    VALVE_UP("1c1035333333348967454d3347773b3a443c"),

    //合闸
    VALVE_DOWN("1c1035333333348967454e3347773b3a443c"),

    //设置时间
    VALVE_TIME("140f353433373533333334896745"),

    //设置日期
    SET_DATE("1410343433373533333334896745");

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
