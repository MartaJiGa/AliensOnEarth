package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Character {
    protected TextureRegion currentFrame;
    protected Vector2 position;
    protected int width;
    protected int height;
    protected Rectangle rectangle;

    public Character(TextureRegion currentFrame, int width, int height, Vector2 position){
        this.currentFrame = currentFrame;
        this.width = width;
        this.height = height;
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
    }
}
