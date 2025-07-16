package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Character {
    protected TextureRegion image;
    protected Vector2 position;
    protected int width;
    protected int height;
    protected Rectangle rectangle;

    public Character(TextureRegion image, int width, int height, Vector2 position){
        this.image = image;
        this.width = width;
        this.height = height;
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
    }
}
