package com.example.webrise.model.enums;

public enum Gender {
    MALE("Мужчина"),
    FEMALE("Женщина");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
