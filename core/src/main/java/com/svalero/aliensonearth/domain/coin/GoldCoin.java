package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;

@Data
public class GoldCoin extends Coin {
    public GoldCoin(Texture image, Vector2 position) {
        super(image, CoinType.GOLD, 50, 50, position);
    }
}
