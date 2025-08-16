package com.svalero.aliensonearth.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.aliensonearth.util.Constants.FIREBALL_SIZE;

@EqualsAndHashCode(callSuper = true)
@Data
public class Fireball extends Item {
    //region Properties

    protected Vector2 velocity;

    //endregion

    //region Constructors

    public Fireball(TextureRegion textureRegion, Vector2 position){
        super(textureRegion, FIREBALL_SIZE, FIREBALL_SIZE, position);
    }

    //endregion
}
