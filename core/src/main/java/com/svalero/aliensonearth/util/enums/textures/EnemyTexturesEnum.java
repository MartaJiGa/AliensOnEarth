package com.svalero.aliensonearth.util.enums.textures;

public enum EnemyTexturesEnum {
    WORM_MOVE_A("worm_ring_move_a"),
    WORM_MOVE_B("worm_ring_move_b"),
    WORM_REST("worm_ring_rest");

    private final String regionName;

    EnemyTexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
