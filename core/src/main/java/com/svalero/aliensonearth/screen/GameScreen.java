package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.coin.*;
import com.svalero.aliensonearth.domain.Player;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Player player;
    private Array<BronzeCoin> bronzeCoins;
    private Array<SilverCoin> silverCoins;
    private Array<GoldCoin> goldCoins;
    private Sound coinSound;
    private Music aMusic;

    //region override

    @Override
    public void show() {
        batch = new SpriteBatch();

        player = new Player(new Texture("character_pink_front.png"), 100, 100, Vector2.Zero);

        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin_sound.ogg"));
        aMusic = Gdx.audio.newMusic(Gdx.files.internal("gameAmbient-funk.mp3"));
        aMusic.setLooping(true);
        aMusic.play();

        Texture bronzeTexture = new Texture("coin_bronze.png");
        Texture silverTexture = new Texture("coin_silver.png");
        Texture goldTexture = new Texture("coin_gold.png");

        bronzeCoins = new Array<>(new BronzeCoin[] {
            new BronzeCoin(bronzeTexture, new Vector2(100, 20)),
            new BronzeCoin(bronzeTexture, new Vector2(100, 150)),
            new BronzeCoin(bronzeTexture, new Vector2(170, 200)),
            new BronzeCoin(bronzeTexture, new Vector2(240, 340))
        });

        silverCoins = new Array<>(new SilverCoin[] {
            new SilverCoin(silverTexture, new Vector2(220, 45)),
            new SilverCoin(silverTexture, new Vector2(500, 310)),
            new SilverCoin(silverTexture, new Vector2(600, 200))
            });

        goldCoins = new Array<>(new GoldCoin[] {
            new GoldCoin(goldTexture, new Vector2(400, 400)),
            new GoldCoin(goldTexture, new Vector2(500, 50))
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.741f, 0.89f, 0.973f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (BronzeCoin coin : bronzeCoins) {
            batch.draw(coin.getImage(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        for (SilverCoin coin : silverCoins) {
            batch.draw(coin.getImage(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        for (GoldCoin coin : goldCoins) {
            batch.draw(coin.getImage(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        batch.draw(player.getImage(), player.getX(), player.getY(), player.getWidth(), player.getHeight());

        batch.end();

        managePlayerInput();
        manageCollisions();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();

        aMusic.dispose();
        coinSound.dispose();
    }

    //endregion

    //region methods

    private void managePlayerInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.move(10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.move(-10);
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
                coinSound.play();
            }
        }

        for (int i = silverCoins.size - 1; i >= 0; i--) {
            Coin coin = silverCoins.get(i);
            if (coin.getRectangle().overlaps(playerRectangle)) {
                silverCoins.removeIndex(i);
                player.changeScore(coin.getPoints());
                System.out.println("Score: " + player.getScore());
                coinSound.play();
            }
        }

        for (int i = goldCoins.size - 1; i >= 0; i--) {
            Coin coin = goldCoins.get(i);
            if (coin.getRectangle().overlaps(playerRectangle)) {
                goldCoins.removeIndex(i);
                player.changeScore(coin.getPoints());
                System.out.println("Score: " + player.getScore());
                coinSound.play();
            }
        }
    }

    //endregion
}
