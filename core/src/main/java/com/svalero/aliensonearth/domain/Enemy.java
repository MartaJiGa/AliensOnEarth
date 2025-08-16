package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.states.*;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.aliensonearth.util.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Enemy extends Character {
    //region properties

    protected int lives;
    protected boolean isShort, isPlayerNearby;
    protected float enemyDistanceFromPlayer;

    protected Boolean isFacingRight, alive;

    protected Animation<TextureRegion> wormLeftAnimation, wormRightAnimation, attackAnimation, beeAnimation, flyAnimation;
    protected EnemyAnimationStatesEnum state;
    protected EnemyTypeEnum enemyType;

    //endregion

    //region constructor
    public Enemy(TextureRegion currentFrame, Vector2 position, int width, int height, TiledMapTileLayer groundLayer, EnemyTypeEnum enemyType){
        super(currentFrame, width, height, position, groundLayer);

        this.enemyType = enemyType;

        isPlayerNearby = false;
        alive = true;
        isFacingRight = null;

        if(enemyType.equals(EnemyTypeEnum.BEE)){
            state = state.FLY_LEFT;
            formBeeAnimation(EnemyTexturesEnum.BEE_A.getRegionName(), EnemyTexturesEnum.BEE_B.getRegionName());
        } else if(enemyType.equals(EnemyTypeEnum.FLY)){
            state = state.FLY_LEFT;
            formFlyAnimation(EnemyTexturesEnum.FLY_A.getRegionName(), EnemyTexturesEnum.FLY_B.getRegionName());
        }

        enemyDistanceFromPlayer = MathUtils.random(150, 350);
    }

    //endregion

    //region override

    @Override
    public void update(float dt){
        float stateTime = getStateTime();
        stateTime += dt;
        setStateTime(stateTime);

        getSpeed().y -= GRAVITY * dt;

        manageMovement(dt);

        if (!isPlayerNearby) {
            state = EnemyAnimationStatesEnum.REST;
        }
    }

    //endregion

    //region methods

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

    public void setPlayerNearby(boolean nearby) {
        this.isPlayerNearby = nearby;
    }

    //endregion
}
