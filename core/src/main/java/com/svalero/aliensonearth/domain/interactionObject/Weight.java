package com.svalero.aliensonearth.domain.interactionObject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.aliensonearth.util.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Weight extends Item {
    //region Properties

    private int weightId;
    private Vector2 velocity;
    private boolean finished;
    private boolean pendingRemoval;
    private float removalTimer;

    //endregion

    //region Constructor

    public Weight(TextureRegion textureRegion, int width, int height, Vector2 position, String imageName, boolean isSolid, int weightId, TiledMapTileLayer groundLayer){
        super(textureRegion, width, height, position, imageName, isSolid, groundLayer);

        this.weightId = weightId;
        velocity = new Vector2(0, 0);
        pendingRemoval = false;
        removalTimer = 1f;
    }

    //endregion

    //region Override

    @Override
    public void activate() {
        setActivated(true);
    }

    @Override
    public void setPosition(Vector2 newPosition) {
        super.setPosition(newPosition);
        rectangle.setPosition(newPosition.x, newPosition.y);
    }

    @Override
    public void update(float dt) {
        if (isActivated()) {
            float gravityMultiplier = 10f;
            velocity.y -= GRAVITY * gravityMultiplier * dt;
            float newY = getPosition().y + velocity.y * dt;

            setPosition(new Vector2(getPosition().x, newY));

            if (isSolidTileBelow()) {
                float footY = getPosition().y;
                int tileY = (int)(footY / TILE_HEIGHT);
                setPosition(new Vector2(getPosition().x, (tileY + 1) * TILE_HEIGHT));

                velocity.y = 0;
                setActivated(false);
                finished = true;
            }
        }
    }

    //endregion

    //region Methods

    public void markForRemoval() {
        pendingRemoval = true;
        removalTimer = 1f;
    }

    public boolean shouldRemove(float dt) {
        if (pendingRemoval) {
            removalTimer -= dt;
            return removalTimer <= 0;
        }
        return false;
    }

    //endregion
}
