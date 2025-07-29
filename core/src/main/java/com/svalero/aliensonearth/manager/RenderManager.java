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

        //TODO: AJUSTAR TAMAÑO ANCHO Y ALTURA (NÚMERO PUESTO A PIÑÓN EN ESTOS PARÁMETROS) CON LO HECHO EN TILED AL EJECUTAR EL JUEGO.
        camera.setToOrtho(false, 30 * TILE_WIDTH, 20 * TILE_HEIGHT);
        camera.update();
    }

    //endregion

    //region methods

    public void render(){
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

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
