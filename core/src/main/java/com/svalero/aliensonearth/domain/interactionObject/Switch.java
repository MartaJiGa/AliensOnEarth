package com.svalero.aliensonearth.domain.interactionObject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Switch extends Item {
    //region Properties

    private int switchId;

    //endregion

    //region Constructor

    public Switch(TextureRegion textureRegion, int width, int height, Vector2 position, String imageName, boolean isSolid, int switchId){
        super(textureRegion, width, height, position, imageName, isSolid);

        this.switchId = switchId;
    }

    //endregion
}
