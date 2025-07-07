package com.svalero.aliensonearth.util.enums;

public enum TexturesEnum {
    PLAYER("character_pink_front"),
    BRONZE_COIN("coin_bronze"),
    SILVER_COIN("coin_silver"),
    GOLD_COIN("coin_gold");

    private final String regionName;

    TexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
