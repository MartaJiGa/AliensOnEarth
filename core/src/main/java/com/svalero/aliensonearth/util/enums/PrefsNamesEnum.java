package com.svalero.aliensonearth.util.enums;

public enum PrefsNamesEnum {
    MUSIC("music"),
    MUSIC_VOLUME("musicVolume");

    private final String settingName;

    PrefsNamesEnum(String settingName) {
        this.settingName = settingName;
    }

    public String getPrefsName() {
        return settingName;
    }
}
