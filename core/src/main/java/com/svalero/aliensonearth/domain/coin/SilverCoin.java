package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;

@Data
public class SilverCoin extends Coin {
    public SilverCoin(Texture image, Vector2 position) {
        super(image, CoinType.SILVER, 50, 50, position);
    }
}
