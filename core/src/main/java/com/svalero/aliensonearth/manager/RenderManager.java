package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;

public class RenderManager {

    //region properties

    private LogicManager logicManager;
    private SpriteBatch batch;

    //endregion

    //region constructor

    public RenderManager(LogicManager logicManager){
        this.logicManager = logicManager;
        batch = new SpriteBatch();
    }

    //endregion

    //region methods

    public void render(){
        batch.begin();

        for (BronzeCoin coin : logicManager.bronzeCoins) {
            batch.draw(coin.getImage(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        for (SilverCoin coin : logicManager.silverCoins) {
            batch.draw(coin.getImage(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        for (GoldCoin coin : logicManager.goldCoins) {
            batch.draw(coin.getImage(), coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        }
        batch.draw(logicManager.player.getImage(), logicManager.player.getX(), logicManager.player.getY(), logicManager.player.getWidth(), logicManager.player.getHeight());

        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }

    //endregion
}
