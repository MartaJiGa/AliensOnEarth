package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.util.enums.CoinTypeEnum;
import lombok.Data;

@Data
public class SilverCoin extends Coin {
    public SilverCoin(TextureRegion image, Vector2 position) {
        super(image, CoinTypeEnum.SILVER, 30, 30, position);
    }
}
