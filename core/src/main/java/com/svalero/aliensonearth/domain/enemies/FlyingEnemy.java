package com.svalero.aliensonearth.domain.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.Enemy;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.states.EnemyAnimationStatesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlyingEnemy extends Enemy {

    //region Properties

    private float speedX;

    //endregion

    //region Constructors

    public FlyingEnemy(TextureRegion texture, Vector2 position, int width, int height,
                       TiledMapTileLayer groundLayer, EnemyTypeEnum enemyType, float speedX) {
        super(texture, position, width, height, groundLayer, enemyType);
        this.speedX = speedX;
    }

    //endregion

    //region Override

    @Override
    public void update(float dt) {
        super.update(dt);

        position.x += speedX * dt;
        rectangle.setPosition(position);

        if (position.x + getWidth() < 0) {
            setPlayerNearby(false);
        }

        if (getEnemyType() == EnemyTypeEnum.BEE) {
            setState(EnemyAnimationStatesEnum.FLY_LEFT);
            changeTextureState(EnemyTypeEnum.BEE.name());
        } else if(getEnemyType() == EnemyTypeEnum.FLY){
            setState(EnemyAnimationStatesEnum.FLY_LEFT);
            changeTextureState(EnemyTypeEnum.FLY.name());
        }
    }

    //endregion
}
