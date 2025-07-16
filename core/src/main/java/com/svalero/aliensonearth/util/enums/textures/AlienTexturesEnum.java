package com.svalero.aliensonearth.util.enums.textures;

public enum AlienTexturesEnum {
    PINK_PLAYER_CLIMB_A("character_pink_climb_a"),
    PINK_PLAYER_CLIMB_B("character_pink_climb_b"),
    PINK_PLAYER_DUCK("character_pink_duck"),
    PINK_PLAYER_HIT("character_pink_hit"),
    PINK_PLAYER_IDLE("character_pink_idle"),
    PINK_PLAYER_JUMP("character_pink_jump"),
    PINK_PLAYER_WALK_A("character_pink_walk_a"),
    PINK_PLAYER_WALK_B("character_pink_walk_b"),
    PINK_PLAYER_FRONT("character_pink_front");

    private final String regionName;

    AlienTexturesEnum(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }
}
