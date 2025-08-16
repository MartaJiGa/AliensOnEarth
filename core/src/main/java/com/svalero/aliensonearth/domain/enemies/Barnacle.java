package com.svalero.aliensonearth.domain.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Enemy;
import com.svalero.aliensonearth.domain.Fireball;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.states.EnemyAnimationStatesEnum;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.aliensonearth.util.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Barnacle extends Enemy {
    //region properties

    private float enemyDistanceFromPlayer, attackCooldown, timeSinceLastAttack;
    private Animation<TextureRegion> attackAnimation;

    //endregion

    //region constructor
    public Barnacle(TextureRegion currentFrame, Vector2 position, int width, int height, TiledMapTileLayer groundLayer, EnemyTypeEnum enemyType){
        super(currentFrame, position, width, height, groundLayer, enemyType);

        state = state.REST;
        formAttackAnimation(EnemyTexturesEnum.BARNACLE_ATTACK_A.getRegionName(), EnemyTexturesEnum.BARNACLE_ATTACK_B.getRegionName());

        enemyDistanceFromPlayer = BARNACLE_DISTANCE_FROM_PLAYER;
        attackCooldown = ENEMY_COLLISION_COOLDOWN_TIME;
        timeSinceLastAttack = 0f;
    }

    //endregion

    //region override

    @Override
    public void update(float dt){
        float stateTime = getStateTime();
        stateTime += dt;
        setStateTime(stateTime);

        getSpeed().y -= GRAVITY * dt;

        changeTextureState(EnemyTexturesEnum.BARNACLE_REST.getRegionName());

        manageMovement(dt);

        if (!isPlayerNearby) {
            state = EnemyAnimationStatesEnum.REST;
            return;
        }

        state = EnemyAnimationStatesEnum.ATTACK;
        timeSinceLastAttack += dt;
    }

    @Override
    public void changeTextureState(String enemyName){
        switch (state){
            case REST:
                textureRegion = ResourceManager.getEnemyTexture(enemyName);
                break;
            case ATTACK:
                textureRegion = attackAnimation.getKeyFrame(super.getStateTime(), true);
                break;
        }
    }

    //endregion

    //region methods

    public void formAttackAnimation(String textureA, String textureB) {
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        frames.addAll(ResourceManager.getEnemyRegions(textureA));
        frames.addAll(ResourceManager.getEnemyRegions(textureB));
        attackAnimation = new Animation<>(0.1f, frames);
    }

    public Fireball launchFireballAtPlayer(Player player){
        Vector2 fireballPosition = new Vector2(position.x + width / 2, position.y + height / 2);
        TextureRegion fireballTexture = ResourceManager.getInteractionTexture(InteractionTexturesEnum.FIREBALL.getRegionName());

        Fireball fireball = new Fireball(fireballTexture, fireballPosition);

        Vector2 direction = new Vector2(player.getPosition()).sub(fireballPosition).nor();

        float fireballSpeed = MathUtils.random(200, 300);
        fireball.setVelocity(direction.scl(fireballSpeed));

        return fireball;
    }

    //endregion
}
