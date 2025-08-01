package com.svalero.aliensonearth.util;

public class Constants {

    //region General

    public static final String GAME_NAME = "AliensOnEarth";
    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;

    public static final int GRAVITY = 10;

    //endregion

    //region Player

    public static final int PLAYER_SPEED = 4;
    public static final int PLAYER_JUMPING_SPEED = 220;
    public static final float PLAYER_ENEMY_COLLISION_HIT_TEXTURE_TIME = 0.5f;

    //endregion

    //region Enemy

    public static final int ENEMY_SPEED = 2;
    public static final float ENEMY_COLLISION_COOLDOWN_TIME = 2f; //Number of seconds the program can't make the collision consequences.

    //endregion

    //region Tiled

        // General
        public static final int TILE_WIDTH = 32;
        public static final int TILE_HEIGHT = 32;

        // Levels
        public static final String TILE_LEVEL1 = "levels/level1.tmx";

        // Layers
        public static final String TILE_LAYER_BACKGROUND = "Background";
        public static final String TILE_LAYER_GROUND = "Ground";
        public static final String TILE_LAYER_ENEMIES = "Enemies";
        public static final String TILE_LAYER_COINS = "Coins";

        // Attributes
        public static final String TILE_ATTRIBUTE_INTERACT = "Interaction";
        public static final String TILE_ATTRIBUTE_ENEMY = "Enemy";
        public static final String TILE_ATTRIBUTE_GROUND = "Ground";
        public static final String TILE_ATTRIBUTE_BACKGROUND = "Background";
        public static final String TILE_ATTRIBUTE_HUD = "Hud";
        public static final String TILE_ATTRIBUTE_FINISH = "FinishLine";

        // Attribute Values
        public static final String TILE_ATT_VALUE_BRONZE_COIN = "BronzeCoin";
        public static final String TILE_ATT_VALUE_SILVER_COIN = "SilverCoin";
        public static final String TILE_ATT_VALUE_GOLD_COIN = "GoldCoin";

    //endregion
}
