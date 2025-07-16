package com.svalero.aliensonearth.util.enums.textures;

public enum CoinTexturesEnum {
    BRONZE_COIN("coin_bronze"),
    BRONZE_COIN_SIDE("coin_bronze_side"),
    SILVER_COIN("coin_silver"),
    SILVER_COIN_SIDE("coin_silver_side"),
    GOLD_COIN("coin_gold"),
    GOLD_COIN_SIDE("coin_gold_side");

    private final String regionName;

    CoinTexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
