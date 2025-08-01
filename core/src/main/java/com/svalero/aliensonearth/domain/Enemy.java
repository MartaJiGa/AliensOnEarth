package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.states.*;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Enemy extends Character {
    //region properties

    private int lives;
    private boolean isShort;

    private Boolean isFacingRight;

    private Animation<TextureRegion> wormRightAnimation, wormLeftAnimation;
    private EnemyAnimationStatesEnum state;
    private EnemyTypeEnum enemyType;

    //endregion

    //region constructor
    public Enemy(TextureRegion currentFrame, Vector2 position, TiledMapTileLayer groundLayer, boolean isShort, EnemyTypeEnum enemyType){
        super(currentFrame, 64, isShort ? 32 : 64, position, groundLayer);

        this.enemyType = enemyType;
        state = state.REST;
    }

    //endregion

    //region override

    @Override
    public void update(float dt){
        float stateTime = getStateTime();
        stateTime += dt;
        setStateTime(stateTime);

        changeTextureState();
        manageMovement(dt);
    }

    //endregion

    //region methods

    public void changeTextureState(){
        switch (state){
            case REST:
                switch (enemyType) {
                    case BEE:
                        textureRegion = ResourceManager.getEnemyTexture(EnemyTexturesEnum.WORM_REST.getRegionName());
                }
                break;
//            case FLAT:
//                textureRegion = leftAnimation.getKeyFrame(super.getStateTime(), true);
//                break;
            case WALK_LEFT:
                textureRegion = wormLeftAnimation.getKeyFrame(super.getStateTime(), true);
                break;
            case WALK_RIGHT:
                textureRegion = wormRightAnimation.getKeyFrame(super.getStateTime(), true);
                break;
//            case ATTACK:
//                textureRegion = rightAnimation.getKeyFrame(super.getStateTime(), true);
//                break;
//            case IDLE_FRONT:
//                textureRegion = rightAnimation.getKeyFrame(super.getStateTime(), true);
//                break;
//            case IDLE_LEFT:
//                textureRegion = leftIdle;
//                break;
//            case IDLE_RIGHT:
//                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_IDLE.getRegionName());
//                break;
//            case JUMP:
//                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_FRONT.getRegionName());
//                break;
//            case JUMP_LEFT:
//                textureRegion = leftJump;
//                break;
//            case JUMP_RIGHT:
//                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_JUMP.getRegionName());
//                break;
        }
    }

    //endregion
}
