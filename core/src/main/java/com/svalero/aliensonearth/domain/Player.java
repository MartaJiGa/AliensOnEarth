package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;

@Data
public class Player extends Character {
    private int score;
    private int lives;

    public Player(TextureRegion image, Vector2 position){
        super(image, 100, 100, position);
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
}
