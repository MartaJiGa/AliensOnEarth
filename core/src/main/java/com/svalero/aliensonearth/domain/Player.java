package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {
    private Texture image;
    private Vector2 position;

    public Player(Texture image){
        this.image = image;
        position = Vector2.Zero;
    }

    public void move(int movement){
        position.x += movement;
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }
}
