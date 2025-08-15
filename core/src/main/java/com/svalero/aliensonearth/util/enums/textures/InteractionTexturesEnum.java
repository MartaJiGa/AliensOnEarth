package com.svalero.aliensonearth.util.enums.textures;

public enum InteractionTexturesEnum {
    HUB_HEART_FULL("hud_heart"),
    HUB_HEART_HALF("hud_heart_half"),
    HUB_HEART_EMPTY("hud_heart_empty"),
    UFO("ufo"),
    SPRING("spring"),
    SPRING_OUT("spring_out"),
    WEIGHT("weight"),
    SWITCH("switch"),
    SWITCH_PRESSED("switch_pressed"),
    LEVER("lever"),
    LEVER_LEFT("lever_left"),
    LEVER_RIGHT("lever_right"),
    FIREBALL("fireball");

    private final String regionName;

    InteractionTexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
