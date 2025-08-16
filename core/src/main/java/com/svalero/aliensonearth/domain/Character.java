package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.svalero.aliensonearth.util.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class Character extends Item {
    //region properties

    private Vector2 speed;
    private boolean isJumping;
    private float stateTime;

    //endregion

    //region constructor

    public Character(TextureRegion textureRegion, int width, int height, Vector2 position, TiledMapTileLayer groundLayer) {
        super(textureRegion, width, height, position, groundLayer);

        speed = new Vector2();
        isJumping = false;
    }

    //endregion

    //region methods

    public void update(float dt, float mapWidth){
        stateTime += dt;
    }

    public void manageMovement(float dt){
        float newY = position.y + speed.y * dt;

        if (speed.y < 0) {
            if (isSolidTile(position.x + width / 2f, newY)) {
                position.y = ((int)(newY / TILE_HEIGHT) + 1) * TILE_HEIGHT;
                speed.y = 0;
                isJumping = false;
            } else {
                position.y = newY;
                isJumping = true;
            }
        } else if (speed.y > 0) {
            if (isSolidTile(position.x + width / 2f, newY + height)) {
                speed.y = 0;
            } else {
                position.y = newY;
            }
        }
    }

    public boolean isDeadlyGround(float worldX, float worldY) {
        int tileX = (int) (worldX / TILE_WIDTH);
        int tileY = (int) (worldY / TILE_HEIGHT);

        TiledMapTileLayer.Cell cell = getGroundLayer().getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;

        MapProperties props = cell.getTile().getProperties();

        if (props.containsKey("Water") || props.containsKey("Lava")) {
            return true;
        }

        return false;
    }

    public boolean isOnClimbTile(Vector2 position) {
        float centerX = position.x + width / 2f;
        float centerY = position.y + height / 2f;

        int tileX = (int)(centerX / TILE_WIDTH);
        int tileY = (int)(centerY / TILE_HEIGHT);

        TiledMapTileLayer.Cell cell = getGroundLayer().getCell(tileX, tileY);
        if (cell != null && cell.getTile() != null) {
            Object climbObject = cell.getTile().getProperties().get("Ladder");
            if(climbObject != null) return true;

            climbObject = cell.getTile().getProperties().get("Rope");
            if(climbObject != null) return true;
        }

        return false;
    }

    public void move(int movement){
        float newX = position.x + movement;
        float checkX = movement < 0 ? newX : newX + width;

        float footY = position.y + 5f;
        float headY = position.y + height - 5f;

        if (!isSolidTile(checkX, footY) && !isSolidTile(checkX, headY)) {
            position.x = newX;
            rectangle.setPosition(position);
        }
    }

    //endregion
}
