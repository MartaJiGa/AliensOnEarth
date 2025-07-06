package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.util.enums.CoinType;
import lombok.Data;

@Data
public class BronzeCoin extends Coin {
    public BronzeCoin(Texture image, Vector2 position) {
        super(image, CoinType.BRONZE, 50, 50, position);
    }
}
