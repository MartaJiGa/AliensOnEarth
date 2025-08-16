package com.svalero.aliensonearth.domain.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Enemy;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.states.EnemyAnimationStatesEnum;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlyingEnemy extends Enemy {

    //region Properties

    private float speedX;

    //endregion

    //region Constructors

    public FlyingEnemy(TextureRegion texture, Vector2 position, int width, int height,
                       TiledMapTileLayer groundLayer, EnemyTypeEnum enemyType, float speedX) {
        super(texture, position, width, height, groundLayer, enemyType);
        this.speedX = speedX;
    }

    //endregion

    //region Override

    @Override
    public void update(float dt) {
        super.update(dt);

        position.x += speedX * dt;
        rectangle.setPosition(position);

        if (position.x + getWidth() < 0) {
            setPlayerNearby(false);
        }

        if (getEnemyType() == EnemyTypeEnum.BEE) {
            state = state.FLY_LEFT;
            formBeeAnimation(EnemyTexturesEnum.BEE_A.getRegionName(), EnemyTexturesEnum.BEE_B.getRegionName());
            changeTextureState(EnemyTypeEnum.BEE.name());
        } else if(getEnemyType() == EnemyTypeEnum.FLY){
            state = state.FLY_LEFT;
            formFlyAnimation(EnemyTexturesEnum.FLY_A.getRegionName(), EnemyTexturesEnum.FLY_B.getRegionName());
            changeTextureState(EnemyTypeEnum.FLY.name());
        }
    }

    @Override
    public void changeTextureState(String enemyName){
        switch (state){
            case REST:
                textureRegion = ResourceManager.getEnemyTexture(enemyName);
                break;
            case FLY_LEFT:
                if(enemyName.equals(EnemyTypeEnum.BEE.name()))
                    textureRegion = beeAnimation.getKeyFrame(super.getStateTime(), true);
                if(enemyName.equals(EnemyTypeEnum.FLY.name()))
                    textureRegion = flyAnimation.getKeyFrame(super.getStateTime(), true);
                break;
        }
    }

    //endregion

    //region Methods

    public void formBeeAnimation(String textureA, String textureB) {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getEnemyRegions(textureA));
        frames.addAll(ResourceManager.getEnemyRegions(textureB));
        beeAnimation = new Animation<>(0.1f, frames);
    }

    public void formFlyAnimation(String textureA, String textureB) {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getEnemyRegions(textureA));
        frames.addAll(ResourceManager.getEnemyRegions(textureB));
        flyAnimation = new Animation<>(0.1f, frames);
    }

    //endregion
}
