package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.coin.Coin;
import com.svalero.aliensonearth.domain.coin.CoinType;
import com.svalero.aliensonearth.domain.Player;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Player player;
    private Coin bronzeCoin;
    private Coin silverCoin;
    private Coin goldCoin;

    Vector2[] bronzeCoinPositions = new Vector2[] {
        new Vector2(100, 150),
        new Vector2(170, 200),
        new Vector2(240, 340)
    };

    Vector2[] silverCoinPositions = new Vector2[] {
        new Vector2(500, 310),
        new Vector2(600, 200)
    };

    Vector2[] goldCoinPositions = new Vector2[] {
        new Vector2(400, 400)
    };

    //region override

    @Override
    public void show() {
        batch = new SpriteBatch();

        player = new Player(new Texture("character_pink_front.png"));
        bronzeCoin = new Coin(new Texture("coin_bronze.png"), CoinType.BRONZE);
        silverCoin = new Coin(new Texture("coin_silver.png"), CoinType.SILVER);
        goldCoin = new Coin(new Texture("coin_gold.png"), CoinType.GOLD);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.741f, 0.89f, 0.973f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Vector2 position : bronzeCoinPositions) {
            batch.draw(bronzeCoin.getImage(), position.x, position.y, 50, 50);
        }
        for (Vector2 position : silverCoinPositions) {
            batch.draw(silverCoin.getImage(), position.x, position.y, 50, 50);
        }
        for (Vector2 position : goldCoinPositions) {
            batch.draw(goldCoin.getImage(), position.x, position.y, 50, 50);
        }
        batch.draw(player.getImage(), player.getX(), player.getY(), 100, 100);

        batch.end();

        managePlayerInput();
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

    //endregion
}
