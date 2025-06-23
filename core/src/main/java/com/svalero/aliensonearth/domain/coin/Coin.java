package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.Texture;
import com.svalero.aliensonearth.domain.Character;
import lombok.Data;

@Data
public class Coin extends Character {
    private CoinType coinType;

    public Coin(Texture image, CoinType coinType, int width, int height){
        super(image, width, height);
        this.coinType = coinType;
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
