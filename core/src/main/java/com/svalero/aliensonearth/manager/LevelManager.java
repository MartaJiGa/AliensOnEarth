package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.Coin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;
import com.svalero.aliensonearth.util.enums.CoinTypeEnum;
import com.svalero.aliensonearth.util.enums.textures.CoinTexturesEnum;

import static com.svalero.aliensonearth.util.Constants.*;

public class LevelManager {

    //region properties

    private TiledMap map;
    private TiledMapTileLayer backgroundLayer;
    private TiledMapTileLayer groundLayer;
    private MapLayer enemiesLayer;
    private MapLayer coinsLayer;

    private LogicManager logicManager;

    //endregion

    //region constructor

    public LevelManager(LogicManager logicManager){
        this.logicManager = logicManager;

        loadCurrentLevel();
    }

    //endregion

    //region methods

    public TiledMap getMap(){
        return map;
    }

    public void loadCurrentLevel(){
        map = new TmxMapLoader().load(TILE_LEVEL1);
        backgroundLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_BACKGROUND);
        groundLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_GROUND);
        enemiesLayer = map.getLayers().get(TILE_LAYER_ENEMIES);
        coinsLayer = map.getLayers().get(TILE_LAYER_COINS);

        loadBackground();
        loadGround();
        loadEnemies();
        loadItems();
    }

    public void loadBackground(){

    }

    public void loadGround(){

    }

    public void loadEnemies(){

    }

    public void loadItems(){
        for(MapObject mapObject : map.getLayers().get(TILE_LAYER_COINS).getObjects()){
            if(mapObject.getProperties().get(TILE_ATTRIBUTE_INTERACT).equals(TILE_ATT_VALUE_BRONZE_COIN)){
                TiledMapTileMapObject object = (TiledMapTileMapObject)mapObject;
                BronzeCoin coin = new BronzeCoin(ResourceManager.getCoinTexture(CoinTexturesEnum.BRONZE_COIN.getRegionName()), new Vector2(object.getX(), object.getY()));
                logicManager.bronzeCoins.add(coin);
            }
            if(mapObject.getProperties().get(TILE_ATTRIBUTE_INTERACT).equals(TILE_ATT_VALUE_SILVER_COIN)){
                TiledMapTileMapObject object = (TiledMapTileMapObject)mapObject;
                SilverCoin coin = new SilverCoin(ResourceManager.getCoinTexture(CoinTexturesEnum.SILVER_COIN.getRegionName()), new Vector2(object.getX(), object.getY()));
                logicManager.silverCoins.add(coin);
            }
            if(mapObject.getProperties().get(TILE_ATTRIBUTE_INTERACT).equals(TILE_ATT_VALUE_GOLD_COIN)){
                TiledMapTileMapObject object = (TiledMapTileMapObject)mapObject;
                GoldCoin coin = new GoldCoin(ResourceManager.getCoinTexture(CoinTexturesEnum.GOLD_COIN.getRegionName()), new Vector2(object.getX(), object.getY()));
                logicManager.goldCoins.add(coin);
            }
        }
    }

    //endregion
}
