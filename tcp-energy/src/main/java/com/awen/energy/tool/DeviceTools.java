package com.awen.energy.tool;

import java.util.ArrayList;

public class DeviceTools {
    //报文表号
    public static String reverseDeviceId(String[] msgList) {
        //取出表号
        ArrayList<String> deviceId = new ArrayList<>();
        //翻转
        for (int i = 1; i < 7; i++) {
            deviceId.add(0, msgList[i]);
        }
        return String.join("", deviceId);
    }

    //报文标识
    public static String reverseDeviceDataName(String[] msgList) {
        //取出数据
        ArrayList<String> deviceDataName = new ArrayList<>();
        //翻转
        for (int i = 0; i < 4; i++) {
            deviceDataName.add(0, String.format("%x", Long.parseLong(msgList[10 + i], 16)));
        }
        return String.join("", deviceDataName);
    }

    //报文正文
    public static String reverseDeviceData(String[] msgList) {
        //取出数据
        ArrayList<String> deviceData = new ArrayList<>();
        int data_length = Integer.parseInt(msgList[9]) - 4;
        //翻转
        for (int i = 0; i < data_length; i++) {
            deviceData.add(0, String.format("%x", Long.parseLong(msgList[14 + i], 16) - 33));
        }
        return String.join("", deviceData);
    }

    //检查数据完整性
    public static boolean check(String[] msgList) {
        long num = 0L;
        for (int i = 0; i < msgList.length - 2; i++) {
            num = num + Long.parseLong(msgList[i], 16);
        }
        long num2 = Long.parseLong(msgList[msgList.length - 2], 16);
        String tag1 = Long.toHexString(num);
        String tag2 = Long.toHexString(num2);
        return tag1.substring(tag1.length() - 2).equals(tag2);
    }
}
