package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Item;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;
import com.svalero.aliensonearth.util.enums.textures.AlienTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.CoinTexturesEnum;

import static com.svalero.aliensonearth.util.Constants.*;

public class LevelManager {

    //region properties

    private TiledMap map;
    private TiledMapTileLayer backgroundLayer;
    private TiledMapTileLayer groundLayer;

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

        Vector2 initialPosition = getInitialPlayerPositionOnGround();

        this.logicManager.player = new Player(ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_FRONT.getRegionName()), initialPosition, groundLayer);
        this.logicManager.coins = new Array<>();

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

        return new Item(ResourceManager.getInteractionTexture(imageName), new Vector2(x, y), imageName);
    }

    public void addItemToCoinList(Item item){
        if(item.getImageName().equals(CoinTexturesEnum.BRONZE_COIN.getRegionName()))
            logicManager.coins.add(new BronzeCoin(item.getTextureRegion(), item.getPosition()));
        else if(item.getImageName().equals(CoinTexturesEnum.SILVER_COIN.getRegionName()))
            logicManager.coins.add(new SilverCoin(item.getTextureRegion(), item.getPosition()));
        else if(item.getImageName().equals(CoinTexturesEnum.GOLD_COIN.getRegionName()))
            logicManager.coins.add(new GoldCoin(item.getTextureRegion(), item.getPosition()));
    }

    private Vector2 getInitialPlayerPositionOnGround() {
        for (int y = groundLayer.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < groundLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                if (cell != null && cell.getTile() != null) {
                    return new Vector2(x * TILE_WIDTH, (y + 1) * TILE_HEIGHT);
                }
            }
        }
        return new Vector2(0, 0);
    }

    //endregion
}
