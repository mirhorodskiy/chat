package com.mirhorodskiy.chat.model.enums;

public enum Permission {
    READ("read"),
    WRITE("write"),
    MANAGE_USERS("manage_users"),
    MANAGE_CHATS("manage_chats"),
    GENERATE_REPORTS("generate_reports");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
