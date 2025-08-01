package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.SoundsEnum;
import com.svalero.aliensonearth.util.enums.states.AlienAnimationStatesEnum;
import com.svalero.aliensonearth.util.enums.textures.AlienTexturesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.aliensonearth.util.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Player extends Character {
    //region properties

    private int level;
    private int lives;
    private int score;
    private boolean playStateSound;

    private Boolean isFacingRight, isFacingUp;

    private Animation<TextureRegion> rightAnimation, leftAnimation, climbAnimation;
    private TextureRegion leftIdle, leftJump;
    private AlienAnimationStatesEnum state;

    //endregion

    //region constructor

    public Player(TextureRegion currentFrame, Vector2 position, TiledMapTileLayer groundLayer){
        super(currentFrame, 70, 70, position, groundLayer);

        formRightAnimation();
        formLeftAnimation();
        formClimbAnimation();
        formLeftIdleTextureRegion();
        formLeftJumpTextureRegion();

        isFacingRight = null;
        isFacingUp = null;
        playStateSound = true;
        lives = 6;
        state = state.FRONT;
    }

    //endregion

    //region override

    @Override
    public void update(float dt){
        float stateTime = getStateTime();
        stateTime += dt;
        setStateTime(stateTime);

        changeTextureState();

        if(state != state.CLIMB && state != state.FRONT_CLIMB){
            manageMovement(dt);

            getSpeed().y -= GRAVITY;
            if (getSpeed().y < -PLAYER_JUMPING_SPEED)
                getSpeed().y = -PLAYER_JUMPING_SPEED;
        }
    }

    //endregion

    //region methods

    public void formRightAnimation() {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_WALK_A.getRegionName()));
        frames.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_WALK_B.getRegionName()));
        rightAnimation = new Animation<>(0.1f, frames);
    }

    public void formLeftAnimation() {
        Array<TextureAtlas.AtlasRegion> framesToFlip = new Array<>();
        Array<TextureAtlas.AtlasRegion> leftFrames = new Array<>();
        framesToFlip.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_WALK_A.getRegionName()));
        framesToFlip.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_WALK_B.getRegionName()));

        for (TextureAtlas.AtlasRegion region : framesToFlip) {
            TextureAtlas.AtlasRegion flipped = new TextureAtlas.AtlasRegion(region);
            flipped.flip(true, false); // Horizontal inversion
            leftFrames.add(flipped);
        }
        leftAnimation = new Animation<>(0.1f, leftFrames);
    }

    public void formClimbAnimation() {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_CLIMB_A.getRegionName()));
        frames.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_CLIMB_B.getRegionName()));
        climbAnimation = new Animation<>(0.1f, frames);
    }

    public void formLeftIdleTextureRegion() {
        TextureAtlas.AtlasRegion leftIdleRegion = new TextureAtlas.AtlasRegion(ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_IDLE.getRegionName()));
        leftIdleRegion.flip(true, false); // Horizontal inversion
        leftIdle = leftIdleRegion;
    }

    public void formLeftJumpTextureRegion() {
        TextureAtlas.AtlasRegion leftJumpRegion = new TextureAtlas.AtlasRegion(ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_JUMP.getRegionName()));
        leftJumpRegion.flip(true, false); // Horizontal inversion
        leftJump = leftJumpRegion;
    }

    public void changeTextureState(){
        switch (state){
            case FRONT:
            case FRONT_CLIMB:
                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_FRONT.getRegionName());
                playStateSound = true;
                break;
            case WALK_RIGHT:
                textureRegion = rightAnimation.getKeyFrame(getStateTime(), true);
                playStateSound = true;
                break;
            case WALK_LEFT:
                textureRegion = leftAnimation.getKeyFrame(getStateTime(), true);
                playStateSound = true;
                break;
            case IDLE_RIGHT:
                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_IDLE.getRegionName());
                playStateSound = true;
                break;
            case IDLE_LEFT:
                textureRegion = leftIdle;
                playStateSound = true;
                break;
            case JUMP:
                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_FRONT.getRegionName());
                if(playStateSound){
                    ResourceManager.getSound(SoundsEnum.JUMP).play();
                    playStateSound = false;
                }
                break;
            case JUMP_RIGHT:
                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_JUMP.getRegionName());
                if(playStateSound){
                    ResourceManager.getSound(SoundsEnum.JUMP).play();
                    playStateSound = false;
                }
                break;
            case JUMP_LEFT:
                textureRegion = leftJump;
                if(playStateSound){
                    ResourceManager.getSound(SoundsEnum.JUMP).play();
                    playStateSound = false;
                }
                break;
            case HIT:
                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_HIT.getRegionName());
                if(playStateSound){
                    ResourceManager.getSound(SoundsEnum.HURT).play();
                    playStateSound = false;
                }
                break;
            case DUCK:
                textureRegion = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_DUCK.getRegionName());
                playStateSound = true;
                break;
            case CLIMB:
                textureRegion = climbAnimation.getKeyFrame(getStateTime(), true);
                playStateSound = true;
                break;
        }
    }

    public void climb(int movement){
        position.y += movement;
        rectangle.setPosition(position);
    }

    public void jump(){
        if(!isJumping()){
            getSpeed().y = PLAYER_JUMPING_SPEED;
            setJumping(true);
        }
    }

    public void changeScore(int points){
        this.score += points;
    }

    public void reduceLives(){
        this.lives -= 1;
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public AlienAnimationStatesEnum getState(){
        return state;
    }

    public void setState(AlienAnimationStatesEnum state){
        this.state = state;
    }

    //endregion
}
