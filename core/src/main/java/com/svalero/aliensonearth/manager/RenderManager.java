package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.svalero.aliensonearth.domain.Enemy;
import com.svalero.aliensonearth.domain.Fireball;
import com.svalero.aliensonearth.domain.FlyingEnemy;
import com.svalero.aliensonearth.domain.Item;
import com.svalero.aliensonearth.domain.coin.Coin;

import static com.svalero.aliensonearth.util.Constants.*;

public class RenderManager {

    //region properties

    private LogicManager logicManager;
    private Batch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    Vector3 camPosition;
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
        camPosition = camera.position;

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Coin coin : logicManager.coins) {
            batch.draw(coin.getTextureRegion(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        for (Enemy enemy : logicManager.enemies) {
            batch.draw(enemy.getTextureRegion(), enemy.getPosition().x, enemy.getPosition().y, enemy.getWidth(), enemy.getHeight());
        }
        for (Item item : logicManager.items) {
            batch.draw(item.getTextureRegion(), item.getPosition().x, item.getPosition().y, item.getWidth(), item.getHeight());
        }
        for (FlyingEnemy enemy : logicManager.flyingEnemies) {
            batch.draw(enemy.getTextureRegion(), enemy.getPosition().x, enemy.getPosition().y, enemy.getWidth(), enemy.getHeight());
        }
        for (Fireball fireball : logicManager.fireballs) {
            batch.draw(fireball.getTextureRegion(), fireball.getPosition().x, fireball.getPosition().y, fireball.getWidth(), fireball.getHeight());
        }
        batch.draw(logicManager.player.getTextureRegion(), logicManager.player.getX(), logicManager.player.getY(), logicManager.player.getWidth(), logicManager.player.getHeight());

        font.draw(batch, "Name: " + logicManager.player.getName(), camPosition.x - 370, camPosition.y + 220);
        font.draw(batch, "Level: " + logicManager.player.getLevel(), camPosition.x - 370, camPosition.y + 200);
        font.draw(batch, "Lives: ", camPosition.x - 370, camPosition.y + 180);
        font.draw(batch, "Score: " + logicManager.player.getScore(), camPosition.x - 370, camPosition.y + 160);

        getLivesInHub(logicManager.player.getLives());

        batch.end();
    }

    public void getLivesInHub(int lives){
        switch (lives){
            default:
            case 0:
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 335, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 315, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 295, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                break;
            case 1:
                batch.draw(logicManager.halfHubHeart.getTextureRegion(), camPosition.x - 335, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 315, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 295, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                break;
            case 2:
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 335, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 315, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 295, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                break;
            case 3:
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 335, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.halfHubHeart.getTextureRegion(), camPosition.x - 315, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 295, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                break;
            case 4:
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 335, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 315, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.emptyHubHeart.getTextureRegion(), camPosition.x - 295, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                break;
            case 5:
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 335, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 315, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.halfHubHeart.getTextureRegion(), camPosition.x - 295, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                break;
            case 6:
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 335, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 315, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                batch.draw(logicManager.fullHubHeart.getTextureRegion(), camPosition.x - 295, camPosition.y + 158, logicManager.fullHubHeart.getWidth(), logicManager.fullHubHeart.getHeight());
                break;
        }
    }

    public void dispose() {
        batch.dispose();
    }

    //endregion
}
