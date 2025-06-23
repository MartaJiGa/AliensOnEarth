package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.coin.Coin;
import com.svalero.aliensonearth.domain.coin.CoinType;
import com.svalero.aliensonearth.domain.Player;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Player player;
    private Coin bronzeCoin;
    private Coin silverCoin;
    private Coin goldCoin;

    private Array<Vector2> bronzeCoinPositions = new Array<>(new Vector2[] {
        new Vector2(100, 20),
        new Vector2(100, 150),
        new Vector2(170, 200),
        new Vector2(240, 340)
    });

    private Array<Vector2> silverCoinPositions = new Array<>(new Vector2[] {
        new Vector2(220, 45),
        new Vector2(500, 310),
        new Vector2(600, 200)
    });

    private Array<Vector2> goldCoinPositions = new Array<>(new Vector2[] {
        new Vector2(400, 400)
    });

    //region override

    @Override
    public void show() {
        batch = new SpriteBatch();

        player = new Player(new Texture("character_pink_front.png"), 100, 100);
        bronzeCoin = new Coin(new Texture("coin_bronze.png"), CoinType.BRONZE, 50, 50);
        silverCoin = new Coin(new Texture("coin_silver.png"), CoinType.SILVER, 50, 50);
        goldCoin = new Coin(new Texture("coin_gold.png"), CoinType.GOLD, 50, 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.741f, 0.89f, 0.973f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Vector2 position : bronzeCoinPositions) {
            batch.draw(bronzeCoin.getImage(), position.x, position.y, bronzeCoin.getWidth(), bronzeCoin.getHeight());
        }
        for (Vector2 position : silverCoinPositions) {
            batch.draw(silverCoin.getImage(), position.x, position.y, silverCoin.getWidth(), silverCoin.getHeight());
        }
        for (Vector2 position : goldCoinPositions) {
            batch.draw(goldCoin.getImage(), position.x, position.y, goldCoin.getWidth(), goldCoin.getHeight());
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

        for (int i = bronzeCoinPositions.size - 1; i >= 0; i--) {
            Vector2 position = bronzeCoinPositions.get(i);
            Rectangle coinRectangle = new Rectangle(position.x, position.y, bronzeCoin.getWidth(), bronzeCoin.getHeight());
            if (coinRectangle.overlaps(playerRectangle)) {
                bronzeCoinPositions.removeIndex(i);
                player.changeScore(bronzeCoin.getPoints());
                System.out.println("Score: " + player.getScore());
                //TODO: Poner sonido moneda
            }
        }

        for (int i = silverCoinPositions.size - 1; i >= 0; i--) {
            Vector2 position = silverCoinPositions.get(i);
            Rectangle coinRectangle = new Rectangle(position.x, position.y, silverCoin.getWidth(), silverCoin.getHeight());
            if (coinRectangle.overlaps(playerRectangle)) {
                silverCoinPositions.removeIndex(i);
                player.changeScore(silverCoin.getPoints());
                System.out.println("Score: " + player.getScore());
                //TODO: Poner sonido moneda
            }
        }

        for (int i = goldCoinPositions.size - 1; i >= 0; i--) {
            Vector2 position = goldCoinPositions.get(i);
            Rectangle coinRectangle = new Rectangle(position.x, position.y, goldCoin.getWidth(), goldCoin.getHeight());
            if (coinRectangle.overlaps(playerRectangle)) {
                goldCoinPositions.removeIndex(i);
                player.changeScore(goldCoin.getPoints());
                System.out.println("Score: " + player.getScore());
                //TODO: Poner sonido moneda
            }
        }
    }

    //endregion
}
