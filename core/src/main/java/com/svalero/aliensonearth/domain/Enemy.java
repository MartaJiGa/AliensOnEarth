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

    private int lives;
    private boolean isShort, isPlayerNearby;
    private float enemyDistanceFromPlayer, attackCooldown, timeSinceLastAttack;

    private Boolean isFacingRight, alive;

    private Animation<TextureRegion> wormLeftAnimation, wormRightAnimation, attackAnimation, beeAnimation, flyAnimation;
    private EnemyAnimationStatesEnum state;
    private EnemyTypeEnum enemyType;

    //endregion

    //region constructor
    public Enemy(TextureRegion currentFrame, Vector2 position, int width, int height, TiledMapTileLayer groundLayer, EnemyTypeEnum enemyType){
        super(currentFrame, width, height, position, groundLayer);

        this.enemyType = enemyType;

        isPlayerNearby = false;
        alive = true;
        isFacingRight = null;

        if(enemyType.equals(EnemyTypeEnum.WORM)){
            state = state.REST;
            formWormLeftAnimation(EnemyTexturesEnum.WORM_MOVE_A.getRegionName(), EnemyTexturesEnum.WORM_MOVE_B.getRegionName());
            formWormRightAnimation(EnemyTexturesEnum.WORM_MOVE_A.getRegionName(), EnemyTexturesEnum.WORM_MOVE_B.getRegionName());
        } else if(enemyType.equals(EnemyTypeEnum.BEE)){
            state = state.FLY_LEFT;
            formBeeAnimation(EnemyTexturesEnum.BEE_A.getRegionName(), EnemyTexturesEnum.BEE_B.getRegionName());
        } else if(enemyType.equals(EnemyTypeEnum.FLY)){
            state = state.FLY_LEFT;
            formFlyAnimation(EnemyTexturesEnum.FLY_A.getRegionName(), EnemyTexturesEnum.FLY_B.getRegionName());
        } else if(enemyType.equals(EnemyTypeEnum.BARNACLE)){
            state = state.REST;
            formAttackAnimation(EnemyTexturesEnum.BARNACLE_ATTACK_A.getRegionName(), EnemyTexturesEnum.BARNACLE_ATTACK_B.getRegionName());
        }

        if(enemyType.equals(EnemyTypeEnum.BARNACLE)){
            enemyDistanceFromPlayer = BARNACLE_DISTANCE_FROM_PLAYER;
            attackCooldown = ENEMY_COLLISION_COOLDOWN_TIME;
            timeSinceLastAttack = 0f;
        }
        else
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

        if(enemyType.equals(EnemyTypeEnum.WORM))
            changeTextureState(EnemyTexturesEnum.WORM_REST.getRegionName());
        else if(enemyType.equals(EnemyTypeEnum.BARNACLE))
            changeTextureState(EnemyTexturesEnum.BARNACLE_REST.getRegionName());

        manageMovement(dt);

        if (!isPlayerNearby) {
            state = EnemyAnimationStatesEnum.REST;
            return;
        }

        if(enemyType.equals(EnemyTypeEnum.WORM)){
            if (isFacingRight == null) isFacingRight = true;

            int direction = isFacingRight ? ENEMY_SPEED : -ENEMY_SPEED;

            if (canMove(direction)) {
                position.x += direction;
                rectangle.setPosition(position);
                state = isFacingRight ? EnemyAnimationStatesEnum.WALK_RIGHT : EnemyAnimationStatesEnum.WALK_LEFT;
            } else {
                isFacingRight = !isFacingRight;
            }
        } else if(enemyType.equals(EnemyTypeEnum.BARNACLE)){
            state = EnemyAnimationStatesEnum.ATTACK;
            timeSinceLastAttack += dt;
        }
    }

    //endregion

    //region methods

    public void changeTextureState(String enemyName){
        switch (state){
            case REST:
                textureRegion = ResourceManager.getEnemyTexture(enemyName);
                break;
            case WALK_LEFT:
                textureRegion = wormLeftAnimation.getKeyFrame(super.getStateTime(), true);
                break;
            case WALK_RIGHT:
                textureRegion = wormRightAnimation.getKeyFrame(super.getStateTime(), true);
                break;
            case ATTACK:
                textureRegion = attackAnimation.getKeyFrame(super.getStateTime(), true);
                break;
            case FLY_LEFT:
                if(enemyName.equals(EnemyTypeEnum.BEE.name()))
                    textureRegion = beeAnimation.getKeyFrame(super.getStateTime(), true);
                if(enemyName.equals(EnemyTypeEnum.FLY.name()))
                    textureRegion = flyAnimation.getKeyFrame(super.getStateTime(), true);
                break;
        }
    }

    public void formWormLeftAnimation(String textureA, String textureB) {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getEnemyRegions(textureA));
        frames.addAll(ResourceManager.getEnemyRegions(textureB));
        wormLeftAnimation = new Animation<>(0.1f, frames);
    }

    public void formWormRightAnimation(String textureA, String textureB) {
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

    public void formAttackAnimation(String textureA, String textureB) {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getEnemyRegions(textureA));
        frames.addAll(ResourceManager.getEnemyRegions(textureB));
        attackAnimation = new Animation<>(0.1f, frames);
    }

    private boolean canMove(int direction) {
        float nextX = direction > 0 ? position.x + width : position.x - 1;
        float footY = position.y + 5f;
        float headY = position.y + height - 5f;

        return !isSolidTile(nextX, footY) && !isSolidTile(nextX, headY);
    }

    public void setPlayerNearby(boolean nearby) {
        this.isPlayerNearby = nearby;
    }

    public Fireball launchFireballAtPlayer(Player player){
        Vector2 fireballPosition = new Vector2(position.x + width / 2, position.y + height / 2);
        TextureRegion fireballTexture = ResourceManager.getInteractionTexture(InteractionTexturesEnum.FIREBALL.getRegionName());

        Fireball fireball = new Fireball(fireballTexture, fireballPosition);

        Vector2 direction = new Vector2(player.getPosition()).sub(fireballPosition).nor();

        float fireballSpeed = MathUtils.random(200, 300);
        fireball.setVelocity(direction.scl(fireballSpeed));

        return fireball;
    }

    //endregion
}
