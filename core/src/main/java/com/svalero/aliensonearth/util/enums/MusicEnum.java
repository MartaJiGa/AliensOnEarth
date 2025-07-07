package com.svalero.aliensonearth.util.enums;

public enum MusicEnum {
    BACKGROUND("music/gameSong-funk.mp3"),
    MENU("music/menuSong.mp3");

    private final String fileName;

    MusicEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
