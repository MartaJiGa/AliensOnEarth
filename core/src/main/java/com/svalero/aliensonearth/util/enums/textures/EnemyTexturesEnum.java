package com.svalero.aliensonearth.util.enums.textures;

public enum EnemyTexturesEnum {
    WORM_MOVE_A("worm_ring_move_a"),
    WORM_MOVE_B("worm_ring_move_b"),
    WORM_REST("worm_ring_rest"),
    BARNACLE_ATTACK_A("barnacle_attack_a"),
    BARNACLE_ATTACK_B("barnacle_attack_b"),
    BARNACLE_REST("barnacle_attack_rest"),
    BEE_A("bee_a"),
    BEE_B("bee_b"),
    FLY_A("fly_a"),
    FLY_B("fly_b");

    private final String regionName;

    EnemyTexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
