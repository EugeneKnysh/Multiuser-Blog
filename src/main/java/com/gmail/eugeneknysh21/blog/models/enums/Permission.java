package com.gmail.eugeneknysh21.blog.models.enums;

public enum Permission {
    ARTICLE_READ("article:read"),
    ARTICLE_CREATE("article:create");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
