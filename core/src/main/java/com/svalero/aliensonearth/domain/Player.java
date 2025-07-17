package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.states.AlienAnimationStatesEnum;
import com.svalero.aliensonearth.util.enums.textures.AlienTexturesEnum;
import lombok.Data;

@Data
public class Player extends Character {
    private int score;
    private int lives;

    private Animation<TextureRegion> rightAnimation, leftAnimation;
    private AlienAnimationStatesEnum state;

    private float stateTime;

    public Player(TextureRegion currentFrame, Vector2 position){
        super(currentFrame, 100, 100, position);

        Array<TextureAtlas.AtlasRegion> originalFrames = new Array<>();
        originalFrames.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_WALK_A.getRegionName()));
        originalFrames.addAll(ResourceManager.getAlienRegions(AlienTexturesEnum.PINK_WALK_B.getRegionName()));
        rightAnimation = new Animation<>(0.1f, originalFrames);

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

        state = state.FRONT;
    }

    public void update(float dt){
        stateTime += dt;

        switch (state){
            case FRONT:
                break;
            case WALK_RIGHT:
                currentFrame = rightAnimation.getKeyFrame(stateTime, true);
                break;
            case WALK_LEFT:
                currentFrame = leftAnimation.getKeyFrame(stateTime, true);
                break;
            case IDLE_RIGHT:
                currentFrame = ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_IDLE.getRegionName());
                break;
            case IDLE_LEFT:
                break;
            case JUMP:
                break;
            case HIT:
                break;
            case DUCK:
                break;
            case CLIMB:
                break;
        }
    }

    public void move(int movement){
        position.x += movement;
        rectangle.setPosition(position);
    }

    public void changeScore(int points){
        this.score += points;
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
}
