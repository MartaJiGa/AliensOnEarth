package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Enemy;
import com.svalero.aliensonearth.domain.Item;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.domain.coin.*;
import com.svalero.aliensonearth.util.enums.*;
import com.svalero.aliensonearth.util.enums.states.*;

import static com.svalero.aliensonearth.util.Constants.*;
import static com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum.*;

public class LogicManager {

    //region properties

    protected Player player;
    protected Array<Coin> coins;
    protected Array<Item> items;
    protected Array<Enemy> enemies;
    protected Item fullHubHeart;
    protected Item halfHubHeart;
    protected Item emptyHubHeart;
    private boolean isPaused, moving, jumping, climbing;
    private float enemyCollisionCooldown;
    private float playerEnemyCollisionHitTexture;
    public int currentLevel;

    //endregion

    //region constructor

    public LogicManager(){
        isPaused = false;
        enemyCollisionCooldown = 0f;
        playerEnemyCollisionHitTexture = 0f;
        currentLevel = 1;

        fullHubHeart = new Item(ResourceManager.getInteractionTexture(HUB_HEART_FULL.getRegionName()), 30,30);
        halfHubHeart = new Item(ResourceManager.getInteractionTexture(HUB_HEART_HALF.getRegionName()), 30,30);
        emptyHubHeart = new Item(ResourceManager.getInteractionTexture(HUB_HEART_EMPTY.getRegionName()), 30,30);
    }

    //endregion

    //region methods

    private void managePlayerInput(){
        moving = false;
        jumping = false;
        climbing = false;

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.C)){
            player.setState(AlienAnimationStatesEnum.CLIMB);
            player.climb(+PLAYER_SPEED);
            climbing = true;
            player.setIsFacingRight(null);
            player.setIsFacingUp(true);
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.C)){
            player.setState(AlienAnimationStatesEnum.CLIMB);
            player.climb(-PLAYER_SPEED);
            climbing = true;
            player.setIsFacingRight(null);
            player.setIsFacingUp(false);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setState(AlienAnimationStatesEnum.WALK_RIGHT);
            player.move(PLAYER_SPEED);
            moving = true;
            player.setIsFacingRight(true);
            player.setIsFacingUp(null);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setState(AlienAnimationStatesEnum.WALK_LEFT);
            player.move(-PLAYER_SPEED);
            moving = true;
            player.setIsFacingRight(false);
            player.setIsFacingUp(null);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.setState(AlienAnimationStatesEnum.JUMP);
            player.jump();
            jumping = true;
            player.setIsFacingRight(null);
            player.setIsFacingUp(null);
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pauseGame();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setState(AlienAnimationStatesEnum.JUMP_RIGHT);
            player.setIsFacingRight(true);
            player.setIsFacingUp(null);
            player.jump();
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.setState(AlienAnimationStatesEnum.JUMP_LEFT);
            player.setIsFacingRight(false);
            player.setIsFacingUp(null);
            player.jump();
        }

        if (!moving && !jumping && !climbing) {
            Boolean playerFacingRight = player.getIsFacingRight();
            Boolean playerFacingUp = player.getIsFacingUp();
            if (playerFacingUp != null) {
                player.setState(AlienAnimationStatesEnum.FRONT_CLIMB);
            } else if (playerFacingRight == null) {
                player.setState(AlienAnimationStatesEnum.FRONT);
            } else if (playerFacingRight) {
                player.setState(AlienAnimationStatesEnum.IDLE_RIGHT);
            } else {
                player.setState(AlienAnimationStatesEnum.IDLE_LEFT);
            }
        }
    }

    private void manageCollisions(){
        Rectangle playerRectangle = player.getRectangle();

        for (int i = coins.size - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            if (coin.getRectangle().overlaps(playerRectangle)) {
                coins.removeIndex(i);
                makeCoinCollisionConsequences(coin);
            }
        }

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if (enemy.getRectangle().overlaps(playerRectangle) && enemyCollisionCooldown <= 0) {
                makeEnemyCollisionConsequences();
            }
        }

        for (int i = items.size - 1; i >= 0; i--) {
            Item item = items.get(i);
            if (item.getRectangle().overlaps(playerRectangle)) {
                makeItemCollisionConsequences();
            }
        }
    }

    public void makeCoinCollisionConsequences(Coin coin){
        player.changeScore(coin.getPoints());
        ResourceManager.getSound(SoundsEnum.COIN).play();
    }

    public void makeEnemyCollisionConsequences(){
        player.reduceLives();
        player.setState(AlienAnimationStatesEnum.HIT);
        ResourceManager.getSound(SoundsEnum.HURT).play();

        enemyCollisionCooldown = ENEMY_COLLISION_COOLDOWN_TIME;
        playerEnemyCollisionHitTexture = PLAYER_ENEMY_COLLISION_HIT_TEXTURE_TIME;
    }

    public void makeItemCollisionConsequences(){
        System.out.println("Game Finished");
    }

    public void pauseGame(){
        isPaused = true;
    }

    public void resumeGame(){
        isPaused = false;
    }

    public boolean isPaused(){
        return isPaused;
    }

    public void update(float dt){
        if(!isPaused){
            if(playerEnemyCollisionHitTexture <= 0){
                managePlayerInput();
            }

            manageCollisions();

            if(playerEnemyCollisionHitTexture <= 0 || playerEnemyCollisionHitTexture == PLAYER_ENEMY_COLLISION_HIT_TEXTURE_TIME)
                player.update(dt);

            for(int i = 0; i < enemies.size; i++){
                Enemy enemy = enemies.get(i);

                Vector2 enemyPos = enemy.getPosition();
                Vector2 playerPos = player.getPosition();
                float distance = enemyPos.dst(playerPos);

                enemy.setPlayerNearby(distance < 500);
                enemy.update(dt);
            }

            if (enemyCollisionCooldown > 0)
                enemyCollisionCooldown -= dt;

            if (playerEnemyCollisionHitTexture > 0)
                playerEnemyCollisionHitTexture -= dt;
        }
    }

    public void dispose() {

    }

    //endregion
}
