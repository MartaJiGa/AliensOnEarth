package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coin {
    private Texture image;
    private Vector2 position;
    private CoinType coinType;
    private int width;
    private int height;

    public Coin(Texture image, CoinType coinType, int width, int height){
        this.image = image;
        this.width = width;
        this.height = height;
        this.coinType = coinType;
        this.position = Vector2.Zero;
    }

    public int getPoints() {
        switch (coinType) {
            case BRONZE:
                return 1;
            case SILVER:
                return 5;
            case GOLD:
                return 15;
            default:
                return 0;
        }
    }
}
