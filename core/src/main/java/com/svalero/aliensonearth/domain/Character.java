package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.svalero.aliensonearth.util.Constants.TILE_HEIGHT;
import static com.svalero.aliensonearth.util.Constants.TILE_WIDTH;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class Character extends Item {

    private TiledMapTileLayer groundLayer;
    private Vector2 speed;
    private boolean isJumping;

    public Character(TextureRegion textureRegion, int width, int height, Vector2 position, TiledMapTileLayer groundLayer) {
        super(textureRegion, width, height, position);

        this.groundLayer = groundLayer;
        speed = new Vector2();
        isJumping = false;
    }

    public void manageMovement(float dt){
        float newY = position.y + speed.y * dt;

        if (speed.y < 0) {
            if (isSolid(position.x + width / 2f, newY)) {
                position.y = ((int)(newY / TILE_HEIGHT) + 1) * TILE_HEIGHT;
                speed.y = 0;
                isJumping = false;
            } else {
                position.y = newY;
                isJumping = true;
            }
        } else if (speed.y > 0) {
            if (isSolid(position.x + width / 2f, newY + height)) {
                speed.y = 0;
            } else {
                position.y = newY;
            }
        }
    }

    private boolean isSolid(float worldX, float worldY) {
        int tileX = (int) (worldX / TILE_WIDTH);
        int tileY = (int) (worldY / TILE_HEIGHT);

        TiledMapTileLayer.Cell cell = groundLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;

        MapProperties props = cell.getTile().getProperties();

        if (props.containsKey("Solid")) {
            String value = props.get("Solid").toString().trim().toLowerCase();
            return value.equals("true");
        }

        return false;
    }

    public void move(int movement){
        float newX = position.x + movement;
        float checkX = movement < 0 ? newX : newX + width;

        float footY = position.y + 5f;
        float headY = position.y + height - 5f;

        if (!isSolid(checkX, footY) && !isSolid(checkX, headY)) {
            position.x = newX;
            rectangle.setPosition(position);
        }
    }
}
