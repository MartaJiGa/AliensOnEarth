package com.svalero.aliensonearth.domain.coin;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.Character;
import com.svalero.aliensonearth.util.enums.CoinTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Coin extends Character {
    private CoinTypeEnum coinType;

    public Coin(TextureRegion image, CoinTypeEnum coinType, int width, int height, Vector2 position){
        super(image, width, height, position);
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
