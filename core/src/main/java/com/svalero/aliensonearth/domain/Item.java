package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum.*;

@Data
@NoArgsConstructor
public class Item {
    //region properties

    protected TextureRegion textureRegion;
    protected String imageName;
    protected Vector2 position;
    protected int width;
    protected int height;
    protected Rectangle rectangle;
    private boolean isSolid;
    private boolean activated;
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

    public Item(TextureRegion textureRegion, int width, int height, Vector2 position, String imageName, boolean isSolid){
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
        this.position = position;
        this.imageName = imageName;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
        this.isSolid = isSolid;
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

    public boolean isActivated() {
        return activated;
    }

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

    //endregion
}
