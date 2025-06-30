package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.Coin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;

public class LogicManager {

    //region properties

    protected Player player;
    protected Array<BronzeCoin> bronzeCoins;
    protected Array<SilverCoin> silverCoins;
    protected Array<GoldCoin> goldCoins;

    private boolean isPaused = false;

    //endregion

    //region constructor

    public LogicManager(){
        player = new Player(ResourceManager.playerTexture, new Vector2(0, 0));

        bronzeCoins = new Array<>(new BronzeCoin[] {
            new BronzeCoin(ResourceManager.bronzeTexture, new Vector2(100, 20)),
            new BronzeCoin(ResourceManager.bronzeTexture, new Vector2(100, 150)),
            new BronzeCoin(ResourceManager.bronzeTexture, new Vector2(170, 200)),
            new BronzeCoin(ResourceManager.bronzeTexture, new Vector2(240, 340))
        });

        silverCoins = new Array<>(new SilverCoin[] {
            new SilverCoin(ResourceManager.silverTexture, new Vector2(220, 45)),
            new SilverCoin(ResourceManager.silverTexture, new Vector2(500, 310)),
            new SilverCoin(ResourceManager.silverTexture, new Vector2(600, 200))
        });

        goldCoins = new Array<>(new GoldCoin[] {
            new GoldCoin(ResourceManager.goldTexture, new Vector2(400, 400)),
            new GoldCoin(ResourceManager.goldTexture, new Vector2(500, 50))
        });
    }

    //endregion

    //region methods

    private void managePlayerInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.move(10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.move(-10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pauseGame();
        }
    }

    private void manageCollisions(){
        Rectangle playerRectangle = player.getRectangle();

        for (int i = bronzeCoins.size - 1; i >= 0; i--) {
            Coin coin = bronzeCoins.get(i);
            if (coin.getRectangle().overlaps(playerRectangle)) {
                bronzeCoins.removeIndex(i);
                player.changeScore(coin.getPoints());
                System.out.println("Score: " + player.getScore());
                ResourceManager.coinSound.play();
            }
        }

        for (int i = silverCoins.size - 1; i >= 0; i--) {
            Coin coin = silverCoins.get(i);
            if (coin.getRectangle().overlaps(playerRectangle)) {
                silverCoins.removeIndex(i);
                player.changeScore(coin.getPoints());
                System.out.println("Score: " + player.getScore());
                ResourceManager.coinSound.play();
            }
        }

        for (int i = goldCoins.size - 1; i >= 0; i--) {
            Coin coin = goldCoins.get(i);
            if (coin.getRectangle().overlaps(playerRectangle)) {
                goldCoins.removeIndex(i);
                player.changeScore(coin.getPoints());
                System.out.println("Score: " + player.getScore());
                ResourceManager.coinSound.play();
            }
        }
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

    public void update(){
        if(!isPaused){
            managePlayerInput();
            manageCollisions();
        }
    }

    public void dispose() {

    }

    //endregion
}
