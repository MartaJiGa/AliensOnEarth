package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.svalero.aliensonearth.domain.coin.Coin;

import static com.svalero.aliensonearth.util.Constants.*;

public class RenderManager {

    //region properties

    private LogicManager logicManager;
    private Batch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private BitmapFont font;

    //endregion

    //region constructor

    public RenderManager(LogicManager logicManager, TiledMap map){
        this.logicManager = logicManager;

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        batch = mapRenderer.getBatch();

        camera = new OrthographicCamera();

        font = new BitmapFont();
        font.setColor(Color.BLACK);

        camera.setToOrtho(false, 25 * TILE_WIDTH, 15 * TILE_HEIGHT);
        camera.update();
    }

    //endregion

    //region methods

    public void render(){
        camera.position.set(
            logicManager.player.getX() + logicManager.player.getWidth() / 2f,
            logicManager.player.getY() + logicManager.player.getHeight() / 2f,
            0
        );

        float halfWidth = camera.viewportWidth / 2f;
        float halfHeight = camera.viewportHeight / 2f;
        float mapWidth = mapRenderer.getMap().getProperties().get("width", Integer.class) * TILE_WIDTH;
        float mapHeight = mapRenderer.getMap().getProperties().get("height", Integer.class) * TILE_HEIGHT;

        // Fixed camera position on the corners of the screen
        camera.position.x = Math.max(halfWidth, Math.min(camera.position.x, mapWidth - halfWidth));
        camera.position.y = Math.max(halfHeight, Math.min(camera.position.y, mapHeight - halfHeight));

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Coin coin : logicManager.coins) {
            batch.draw(coin.getTextureRegion(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        batch.draw(logicManager.player.getTextureRegion(), logicManager.player.getX(), logicManager.player.getY(), logicManager.player.getWidth(), logicManager.player.getHeight());

        font.draw(batch, "Level: " + logicManager.player.getLevel(), 20, SCREEN_HEIGHT - 10);
        font.draw(batch, "Lives: " + logicManager.player.getLives(), 20, SCREEN_HEIGHT - 30);
        font.draw(batch, "Score: " + logicManager.player.getScore(), 20, SCREEN_HEIGHT - 50);

        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }

    //endregion
}
