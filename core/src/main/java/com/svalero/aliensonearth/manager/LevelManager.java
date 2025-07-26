package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.svalero.aliensonearth.domain.Item;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;
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
//        enemiesLayer = map.getLayers().get(TILE_LAYER_ENEMIES);
//        coinsLayer = map.getLayers().get(TILE_LAYER_COINS);

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
            String imageName = mapObject.getName();
            if (imageName == null) continue;

            Item item = getItem(mapObject, imageName);
            addItemToCoinList(item);
        }
    }

    public Item getItem(MapObject mapObject, String imageName){
        float x = 0, y = 0;

        if (mapObject instanceof TiledMapTileMapObject) {
            x = ((TiledMapTileMapObject) mapObject).getX();
            y = ((TiledMapTileMapObject) mapObject).getY();
        } else if (mapObject.getProperties().containsKey("x") && mapObject.getProperties().containsKey("y")) {
            x = Float.parseFloat(mapObject.getProperties().get("x").toString());
            y = Float.parseFloat(mapObject.getProperties().get("y").toString());
        }

        return new Item(ResourceManager.getCoinTexture(imageName), new Vector2(x, y), imageName);
    }

    public void addItemToCoinList(Item item){
        if(item.getImageName().equals(CoinTexturesEnum.BRONZE_COIN.getRegionName()))
            logicManager.coins.add(new BronzeCoin(item.getTextureRegion(), item.getPosition()));
        else if(item.getImageName().equals(CoinTexturesEnum.SILVER_COIN.getRegionName()))
            logicManager.coins.add(new SilverCoin(item.getTextureRegion(), item.getPosition()));
        else if(item.getImageName().equals(CoinTexturesEnum.GOLD_COIN.getRegionName()))
            logicManager.coins.add(new GoldCoin(item.getTextureRegion(), item.getPosition()));
    }

    //endregion
}
