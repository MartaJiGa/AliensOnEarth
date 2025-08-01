package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.util.enums.CoinTypeEnum;
import lombok.Data;

@Data
public class GoldCoin extends Coin {
    public GoldCoin(TextureRegion image, Vector2 position) {
        super(image, CoinTypeEnum.GOLD, 30, 30, position);
    }
}
