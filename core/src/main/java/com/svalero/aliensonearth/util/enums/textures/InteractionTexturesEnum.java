package com.svalero.aliensonearth.util.enums.textures;

public enum InteractionTexturesEnum {
    HUB_HEART_FULL("hud_heart"),
    HUB_HEART_HALF("hud_heart_half"),
    HUB_HEART_EMPTY("hud_heart_empty");

    private final String regionName;

    InteractionTexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
