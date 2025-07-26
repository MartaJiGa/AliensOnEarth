package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.domain.coin.*;
import com.svalero.aliensonearth.util.enums.*;
import com.svalero.aliensonearth.util.enums.states.*;
import com.svalero.aliensonearth.util.enums.textures.*;

public class LogicManager {

    //region properties

    protected Player player;
    protected Array<Coin> coins;

    public int currentLevel;

    private boolean isPaused = false;

    //endregion

    //region constructor

    public LogicManager(){
        player = new Player(ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_FRONT.getRegionName()), new Vector2(0, 0));

        coins = new Array<>();

        currentLevel = 1;
    }

    //endregion

    //region methods

    private void managePlayerInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setState(AlienAnimationStatesEnum.WALK_RIGHT);
            player.move(10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setState(AlienAnimationStatesEnum.WALK_LEFT);
            player.move(-10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pauseGame();
        } else{
            if(player.getState() == AlienAnimationStatesEnum.WALK_RIGHT){
                player.setState(AlienAnimationStatesEnum.IDLE_RIGHT);
            } else if(player.getState() == AlienAnimationStatesEnum.WALK_LEFT){
                player.setState(AlienAnimationStatesEnum.IDLE_LEFT);
            } else{
                player.setState(AlienAnimationStatesEnum.FRONT);
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
        }
    }

    public void dispose() {

    }

    //endregion
}
