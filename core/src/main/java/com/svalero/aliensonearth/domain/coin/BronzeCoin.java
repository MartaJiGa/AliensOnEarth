package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.util.enums.CoinTypeEnum;
import lombok.Data;

@Data
public class BronzeCoin extends Coin {
    public BronzeCoin(TextureRegion image, Vector2 position) {
        super(image, CoinTypeEnum.BRONZE, 50, 50, position);
    }
}
