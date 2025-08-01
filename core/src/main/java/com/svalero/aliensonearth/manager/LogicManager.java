package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Enemy;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.domain.coin.*;
import com.svalero.aliensonearth.util.enums.*;
import com.svalero.aliensonearth.util.enums.states.*;

import static com.svalero.aliensonearth.util.Constants.*;

public class LogicManager {

    //region properties

    protected Player player;
    protected Array<Coin> coins;
    protected Array<Enemy> enemies;
    private boolean isPaused, moving, jumping, climbing;

    public int currentLevel;

    //endregion

    //region constructor

    public LogicManager(){
        isPaused = false;
        currentLevel = 1;
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
                makeCollisionCommonConsequences(coin);
            }
        }
    }

    public void makeCollisionCommonConsequences(Coin coin){
        player.changeScore(coin.getPoints());
        System.out.println("Score: " + player.getScore());
        ResourceManager.getSound(SoundsEnum.COIN).play();
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
            managePlayerInput();
            manageCollisions();

            player.update(dt);

            for(int i = 0; i < enemies.size; i++){
                Enemy enemy = enemies.get(i);
                enemy.update(dt);
            }
        }
    }

    public void dispose() {

    }

    //endregion
}
