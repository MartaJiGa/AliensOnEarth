package com.svalero.aliensonearth.domain.interactionObject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Weight extends Item {
    //region Properties

    private int weightId;

    //endregion

    //region Constructor

    public Weight(TextureRegion textureRegion, int width, int height, Vector2 position, String imageName, boolean isSolid, int weightId){
        super(textureRegion, width, height, position, imageName, isSolid);

        this.weightId = weightId;
    }

    //endregion
}
