package com.svalero.aliensonearth.util.enums;

public enum SoundsEnum {
    COIN("sounds/coin_sound.ogg"),
    DESAPPEAR("sounds/disappearing_sound.ogg"),
    HURT("sounds/hurt_sound.ogg"),
    JUMP("sounds/jump_sound.ogg");

    private final String fileName;

    SoundsEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
