package com.svalero.aliensonearth.util.enums.textures;

public enum AlienTexturesEnum {
    PINK_CLIMB_A("character_pink_climb_a"),
    PINK_CLIMB_B("character_pink_climb_b"),
    PINK_DUCK("character_pink_duck"),
    PINK_HIT("character_pink_hit"),
    PINK_IDLE("character_pink_idle"),
    PINK_JUMP("character_pink_jump"),
    PINK_WALK_A("character_pink_walk_a"),
    PINK_WALK_B("character_pink_walk_b"),
    PINK_FRONT("character_pink_front");

    private final String regionName;

    AlienTexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
