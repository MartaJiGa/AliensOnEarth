package com.svalero.aliensonearth.util.enums;

public enum FontsEnum {
    ALIEN("fonts/AliensFontTitle.ttf"),
    ON_EARTH("fonts/OnEarthFontTitle.ttf");

    private final String fileName;

    FontsEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
