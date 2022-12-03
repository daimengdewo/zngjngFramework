package com.awen.energy.tool;

import java.util.ArrayList;

public class NumTools {
    public static ArrayList<Integer> reverse(String[] msgList) {
        //取出表号
        ArrayList<String> deviceId = new ArrayList<String>();
        //取出数据
        ArrayList<String> deviceData = new ArrayList<String>();
        //翻转
        for (int i = 1; i < 7; i++) {
            deviceId.add(0, msgList[i]);
        }
        System.out.println(deviceId);
        //翻转
        for (int i = 0; i < Integer.parseInt(msgList[9]); i++) {
            deviceData.add(0, msgList[10 + i]);
        }
        System.out.println(deviceData);
        ArrayList<Integer> res = new ArrayList<Integer>();
        return null;
    }
}
