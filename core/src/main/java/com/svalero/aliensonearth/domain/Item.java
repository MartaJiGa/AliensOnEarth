package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.aliensonearth.util.Constants.TILE_HEIGHT;
import static com.svalero.aliensonearth.util.Constants.TILE_WIDTH;
import static com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum.*;

@Data
@NoArgsConstructor
public class Item {
    //region properties

    protected TextureRegion textureRegion;
    private TiledMapTileLayer groundLayer;
    protected String imageName;
    protected Vector2 position;
    protected Rectangle rectangle;
    protected int width, height;
    private boolean isSolid, activated;
    private float activationTime;

    //endregion

    //region constructors

    public Item(TextureRegion textureRegion, int width, int height, Vector2 position){
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
    }

    public Item(TextureRegion textureRegion, int width, int height, Vector2 position, TiledMapTileLayer groundLayer){
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
        this.position = position;
        this.groundLayer = groundLayer;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
    }

    public Item(TextureRegion textureRegion, int width, int height, Vector2 position, String imageName, boolean isSolid){
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
        this.position = position;
        this.imageName = imageName;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
        this.isSolid = isSolid;
    }

    public Item(TextureRegion textureRegion, int width, int height, Vector2 position, String imageName, boolean isSolid, TiledMapTileLayer groundLayer){
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
        this.position = position;
        this.imageName = imageName;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
        this.isSolid = isSolid;
        this.groundLayer = groundLayer;
    }

    public Item(TextureRegion textureRegion, Vector2 position, String imageName){
        this.textureRegion = textureRegion;
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
        this.imageName = imageName;
    }

    public Item(TextureRegion textureRegion, int width, int height){
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
    }

    //endregion

    //region methods

    public void activate() {
        activated = true;
        activationTime = 0.5f;
        this.setTextureRegion(ResourceManager.getInteractionTexture(InteractionTexturesEnum.SPRING_OUT.getRegionName()));
    }

    public void update(float dt) {
        if (activated) {
            activationTime -= dt;
            if (activationTime <= 0) {
                activated = false;
                this.setTextureRegion(ResourceManager.getInteractionTexture(SPRING.getRegionName()));
            }
        }
    }

    public boolean isSolidTileBelow() {
        if (groundLayer == null) return false;

        float footX = position.x + width / 2f;
        float footY = position.y - 1f;

        return isSolidTile(footX, footY);
    }

    public boolean isSolidTile(float worldX, float worldY) {
        if (groundLayer == null) return false;

        int tileX = (int) (worldX / TILE_WIDTH);
        int tileY = (int) (worldY / TILE_HEIGHT);

        TiledMapTileLayer.Cell cell = groundLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;

        MapProperties props = cell.getTile().getProperties();

        boolean isSolid = props.containsKey("Solid") && Boolean.parseBoolean(props.get("Solid").toString());
        boolean isLadder = props.containsKey("Ladder");

        return isSolid && !isLadder;
    }

    //endregion
}
