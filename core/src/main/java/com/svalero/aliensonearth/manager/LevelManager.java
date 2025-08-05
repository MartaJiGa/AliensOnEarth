package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.domain.Enemy;
import com.svalero.aliensonearth.domain.Item;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.textures.AlienTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.CoinTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;

import static com.svalero.aliensonearth.util.Constants.*;

public class LevelManager {

    //region properties

    private TiledMap map;
    private TiledMapTileLayer skyLayer, backgroundLayer, groundLayer;
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
        skyLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_SKY);
        backgroundLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_BACKGROUND);
        groundLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_GROUND);

        Vector2 initialPosition = getInitialPlayerPositionFromObjectLayer();

        this.logicManager.player = new Player(ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_FRONT.getRegionName()), initialPosition, groundLayer);
        this.logicManager.coins = new Array<>();
        this.logicManager.enemies = new Array<>();
        this.logicManager.items = new Array<>();

        loadItems();
    }

    public void loadItems(){
        for(MapObject mapObject : map.getLayers().get(TILE_LAYER_COINS).getObjects()){
            String imageName = mapObject.getName();
            if (imageName == null) continue;

            Item item = getItem(mapObject, imageName, TILE_LAYER_COINS);
            addItemToCoinList(item);
        }
        for(MapObject mapObject : map.getLayers().get(TILE_LAYER_ENEMIES).getObjects()){
            String imageName = mapObject.getName();
            if (imageName == null) continue;

            Item item = getItem(mapObject, imageName, TILE_LAYER_ENEMIES);
            addItemToEnemyList(item);
        }
        for(MapObject mapObject : map.getLayers().get(TILE_LAYER_UFO).getObjects()){
            String imageName = mapObject.getName();
            if (imageName == null) continue;

            Item item = getItem(mapObject, imageName, TILE_LAYER_UFO);
            addItemToItemList(item);
        }
    }

    public Item getItem(MapObject mapObject, String imageName, String layerName){
        float x = 0, y = 0;

        if (mapObject instanceof TiledMapTileMapObject) {
            x = ((TiledMapTileMapObject) mapObject).getX();
            y = ((TiledMapTileMapObject) mapObject).getY();
        } else if (mapObject.getProperties().containsKey("x") && mapObject.getProperties().containsKey("y")) {
            x = Float.parseFloat(mapObject.getProperties().get("x").toString());
            y = Float.parseFloat(mapObject.getProperties().get("y").toString());
        }

        if(layerName.equals("Coins") || layerName.equals("Ufo")){
            return new Item(ResourceManager.getInteractionTexture(imageName), new Vector2(x, y), imageName);
        } else if(layerName.equals("Enemies")){
            return new Item(ResourceManager.getEnemyTexture(imageName), new Vector2(x, y), imageName);
        } else{
            return null;
        }
    }

    public void addItemToCoinList(Item item){
        if(item.getImageName().equals(CoinTexturesEnum.BRONZE_COIN.getRegionName()))
            logicManager.coins.add(new BronzeCoin(item.getTextureRegion(), item.getPosition()));
        else if(item.getImageName().equals(CoinTexturesEnum.SILVER_COIN.getRegionName()))
            logicManager.coins.add(new SilverCoin(item.getTextureRegion(), item.getPosition()));
        else if(item.getImageName().equals(CoinTexturesEnum.GOLD_COIN.getRegionName()))
            logicManager.coins.add(new GoldCoin(item.getTextureRegion(), item.getPosition()));
    }

    public void addItemToEnemyList(Item item){
        int width, height;
        if(item.getImageName().equals(EnemyTexturesEnum.WORM_REST.getRegionName())){
            width = 64; height = 32;
            Vector2 groundedPosition = getGroundedEnemyPosition(item.getPosition(), width);
            logicManager.enemies.add(new Enemy(item.getTextureRegion(), groundedPosition, width, height, groundLayer, EnemyTypeEnum.WORM));
        }
    }

    public void addItemToItemList(Item item){
        if(item.getImageName().equals("ufo")){
            logicManager.items.add(new Item(item.getTextureRegion(), UFO_WIDTH, UFO_HEIGHT, item.getPosition()));
        }
    }

    private Vector2 getInitialPlayerPositionFromObjectLayer() {
        MapObject spawnObject = map.getLayers().get("SpawnPoints").getObjects().get("PlayerSpawn");

        if (spawnObject != null) {
            float x = Float.parseFloat(spawnObject.getProperties().get("x").toString());
            float y = Float.parseFloat(spawnObject.getProperties().get("y").toString());

            return new Vector2(x, y);
        }

        return new Vector2(0, 0);
    }

    private Vector2 getGroundedEnemyPosition(Vector2 originalPosition, int width) {
        float x = originalPosition.x;
        float y = originalPosition.y;

        float centerX = x + width / 2f;
        int tileX = (int)(centerX / TILE_WIDTH);
        int tileY = (int)(y / TILE_HEIGHT);

        while (tileY >= 0) {
            TiledMapTileLayer.Cell cell = groundLayer.getCell(tileX, tileY);
            if (cell != null && cell.getTile() != null) {
                Object solid = cell.getTile().getProperties().get("Solid");
                if (solid != null && solid.toString().trim().equalsIgnoreCase("true")) {
                    return new Vector2(x, (tileY + 1) * TILE_HEIGHT);
                }
            }
            tileY--;
        }

        return originalPosition;
    }

    //endregion
}
