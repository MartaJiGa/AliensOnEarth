package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {
    protected TextureRegion textureRegion;
    protected String imageName;
    protected Vector2 position;
    protected int width;
    protected int height;
    protected Rectangle rectangle;

    public Item(TextureRegion textureRegion, int width, int height, Vector2 position){
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
    }

    public Item(TextureRegion textureRegion, Vector2 position, String imageName){
        this.textureRegion = textureRegion;
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, width, height);
        this.imageName = imageName;
    }
}
