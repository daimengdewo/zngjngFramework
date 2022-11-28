package com.awen.feign.tool;

public enum FunctionMenu {

    //操作标识
    ADD("add"), DELETE("delete"), UPDATE("update"), DELETE_ONE("delete_one");

    private final String menu;

    FunctionMenu(String menu) {
        this.menu = menu;
    }

    public String getMenu() {
        return menu;
    }

}
