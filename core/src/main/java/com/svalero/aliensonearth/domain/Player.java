package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {
    private Texture image;
    private Vector2 position;
    private Rectangle rectangle;
    private int width;
    private int height;
    private int score;
    private int lives;

    public Player(Texture image, int width, int height){
        this.image = image;
        this.width = width;
        this.height = height;
        position = Vector2.Zero;
        rectangle = new Rectangle(position.x, position.y, width, height);
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
