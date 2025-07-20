package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;

import static com.svalero.aliensonearth.util.Constants.*;

public class RenderManager {

    //region properties

    private LogicManager logicManager;
    private Batch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    //endregion

    //region constructor

    public RenderManager(LogicManager logicManager, TiledMap map){
        this.logicManager = logicManager;

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        batch = mapRenderer.getBatch();

        camera = new OrthographicCamera();

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

        for (BronzeCoin coin : logicManager.bronzeCoins) {
            batch.draw(coin.getCurrentFrame(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        for (SilverCoin coin : logicManager.silverCoins) {
            batch.draw(coin.getCurrentFrame(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        for (GoldCoin coin : logicManager.goldCoins) {
            batch.draw(coin.getCurrentFrame(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        batch.draw(logicManager.player.getCurrentFrame(), logicManager.player.getX(), logicManager.player.getY(), logicManager.player.getWidth(), logicManager.player.getHeight());

        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }

    //endregion
}
