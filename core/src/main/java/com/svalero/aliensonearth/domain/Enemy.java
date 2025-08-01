package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.states.*;
import com.svalero.aliensonearth.util.enums.textures.AlienTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.aliensonearth.util.Constants.ENEMY_SPEED;
import static com.svalero.aliensonearth.util.Constants.GRAVITY;

@EqualsAndHashCode(callSuper = true)
@Data
public class Enemy extends Character {
    //region properties

    private int lives;
    private boolean isShort;
    private boolean isPlayerNearby;

    private Boolean isFacingRight;

    private Animation<TextureRegion> wormLeftAnimation, wormRightAnimation;
    private EnemyAnimationStatesEnum state;
    private EnemyTypeEnum enemyType;

    //endregion

    //region constructor
    public Enemy(TextureRegion currentFrame, Vector2 position, int width, int height, TiledMapTileLayer groundLayer, EnemyTypeEnum enemyType){
        super(currentFrame, width, height, position, groundLayer);

        this.enemyType = enemyType;
        state = state.REST;

        isPlayerNearby = false;
        isFacingRight = null;

        formLeftAnimation(EnemyTexturesEnum.WORM_MOVE_A.getRegionName(), EnemyTexturesEnum.WORM_MOVE_B.getRegionName());
        formRightAnimation(EnemyTexturesEnum.WORM_MOVE_A.getRegionName(), EnemyTexturesEnum.WORM_MOVE_B.getRegionName());
    }

    //endregion

    //region override

    @Override
    public void update(float dt){
        float stateTime = getStateTime();
        stateTime += dt;
        setStateTime(stateTime);

        getSpeed().y -= GRAVITY * dt;

        changeTextureState();
        manageMovement(dt);

        if (!isPlayerNearby) {
            state = EnemyAnimationStatesEnum.REST;
            return;
        }

        if (isFacingRight == null) isFacingRight = true;

        int direction = isFacingRight ? ENEMY_SPEED : -ENEMY_SPEED;

        if (canMove(direction)) {
            position.x += direction;
            rectangle.setPosition(position);
            state = isFacingRight ? EnemyAnimationStatesEnum.WALK_RIGHT : EnemyAnimationStatesEnum.WALK_LEFT;
        } else {
            isFacingRight = !isFacingRight;
        }
    }

    //endregion

    //region methods

    public void changeTextureState(){
        switch (state){
            case REST:
                switch (enemyType) {
                    case WORM:
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

    public void formLeftAnimation(String textureA, String textureB) {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getEnemyRegions(textureA));
        frames.addAll(ResourceManager.getEnemyRegions(textureB));
        wormLeftAnimation = new Animation<>(0.1f, frames);
    }

    public void formRightAnimation(String textureA, String textureB) {
        Array<TextureAtlas.AtlasRegion> framesToFlip = new Array<>();
        Array<TextureAtlas.AtlasRegion> rightFrames = new Array<>();
        framesToFlip.addAll(ResourceManager.getEnemyRegions(textureA));
        framesToFlip.addAll(ResourceManager.getEnemyRegions(textureB));

        for (TextureAtlas.AtlasRegion region : framesToFlip) {
            TextureAtlas.AtlasRegion flipped = new TextureAtlas.AtlasRegion(region);
            flipped.flip(true, false); // Horizontal inversion
            rightFrames.add(flipped);
        }
        wormRightAnimation = new Animation<>(0.1f, rightFrames);
    }

    private boolean canMove(int direction) {
        float nextX = direction > 0 ? position.x + width : position.x - 1;
        float footY = position.y + 5f;
        float headY = position.y + height - 5f;

        return !isSolid(nextX, footY) && !isSolid(nextX, headY);
    }

    public void setPlayerNearby(boolean nearby) {
        this.isPlayerNearby = nearby;
    }

    //endregion
}
