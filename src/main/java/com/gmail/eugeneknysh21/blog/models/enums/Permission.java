package com.gmail.eugeneknysh21.blog.models.enums;

public enum Permission {
    CREATE;

    @Override
    public String toString() {
        return "OP_" + name();
    }
}
