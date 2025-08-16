package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.states.*;
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
        }
    }

    public void setPlayerNearby(boolean nearby) {
        this.isPlayerNearby = nearby;
    }

    //endregion
}
