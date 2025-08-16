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
import com.svalero.aliensonearth.domain.interactionObject.Lever;
import com.svalero.aliensonearth.domain.Player;
import com.svalero.aliensonearth.domain.coin.BronzeCoin;
import com.svalero.aliensonearth.domain.coin.GoldCoin;
import com.svalero.aliensonearth.domain.coin.SilverCoin;
import com.svalero.aliensonearth.domain.interactionObject.Switch;
import com.svalero.aliensonearth.domain.interactionObject.Weight;
import com.svalero.aliensonearth.util.enums.EnemyTypeEnum;
import com.svalero.aliensonearth.util.enums.LeverOrientationEnum;
import com.svalero.aliensonearth.util.enums.PrefsNamesEnum;
import com.svalero.aliensonearth.util.enums.textures.AlienTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.CoinTexturesEnum;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;

import static com.svalero.aliensonearth.Main.db;
import static com.svalero.aliensonearth.Main.prefs;
import static com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum.*;

import static com.svalero.aliensonearth.util.Constants.*;

public class LevelManager {

    //region properties

    private TiledMap map;
    private TiledMapTileLayer skyLayer, backgroundLayer, groundLayer;
    private LogicManager logicManager;

    //endregion

    //region constructor

    public LevelManager(LogicManager logicManager, Boolean retryLevel){
        this.logicManager = logicManager;
        loadCurrentLevel(retryLevel);
    }

    //endregion

    //region methods

    public TiledMap getMap(){
        return map;
    }

    public void loadCurrentLevel(Boolean retryLevel){
        String playerName = prefs.getString(PrefsNamesEnum.PLAYER_NAME.getPrefsName());

        int playerId = db.getPlayerIdByName(playerName);
        if(playerId <= 0){
            db.savePlayerProgress(playerName, 1, 1, 1, 0, -1);
            playerId = db.getPlayerIdByName(playerName);
        }

        int playerLevel = db.getPlayerLevel(playerId);
        playerLevel = playerLevel <= 0 ? 1: playerLevel;

        switch (db.getHigherLevelPlayed(playerId)) {
            default:
            case -1:
            case 1:
                map = new TmxMapLoader().load(TILE_LEVEL1);
                setMapSize();
                prefs.putInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName(), 1);
                break;
            case 2:
                if(retryLevel == null){
                    int lastLevelPlayed = db.getLastLevelPlayed(playerId);
                    if(lastLevelPlayed == 1 && prefs.getInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName()) == 1){
                        map = new TmxMapLoader().load(TILE_LEVEL1);
                        setMapSize();
                        prefs.putInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName(), 1);
                    } else {
                        map = new TmxMapLoader().load(TILE_LEVEL2);
                        setMapSize();
                        prefs.putInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName(), 2);
                    }
                } else if(!retryLevel){
                    map = new TmxMapLoader().load(TILE_LEVEL2);
                    setMapSize();
                    prefs.putInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName(), 2);
                }else{
                    map = new TmxMapLoader().load(TILE_LEVEL1);
                    setMapSize();
                    prefs.putInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName(), 1);
                }

                break;
        }

        skyLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_SKY);
        backgroundLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_BACKGROUND);
        groundLayer = (TiledMapTileLayer)map.getLayers().get(TILE_LAYER_GROUND);

        Vector2 initialPosition = getInitialPlayerPositionFromObjectLayer();

        this.logicManager.player = new Player(ResourceManager.getAlienTexture(AlienTexturesEnum.PINK_FRONT.getRegionName()), initialPosition, groundLayer);
        this.logicManager.player.setName(playerName);
        this.logicManager.player.setId(playerId);
        this.logicManager.player.setCurrentGameLevel(prefs.getInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName()));
        this.logicManager.player.setLevel(playerLevel);
        this.logicManager.player.setGlobalScore(db.getPlayerGlobalScore(playerId));

        this.logicManager.checkPlayerLevel();

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
        for(MapObject mapObject : map.getLayers().get(TILE_LAYER_INTERACTION).getObjects()){
            String imageName = mapObject.getName();
            if (imageName == null) continue;

            Item item = getItem(mapObject, imageName, TILE_LAYER_INTERACTION);
            addItemToItemList(item, imageName, mapObject);
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

        if(layerName.equals(TILE_LAYER_COINS) || layerName.equals(TILE_LAYER_INTERACTION)){
            return new Item(ResourceManager.getInteractionTexture(imageName), new Vector2(x, y), imageName);
        } else if(layerName.equals(TILE_LAYER_ENEMIES)){
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
        } else if(item.getImageName().equals(EnemyTexturesEnum.BARNACLE_REST.getRegionName())){
            width = 64; height = 64;
            Vector2 groundedPosition = getGroundedEnemyPosition(item.getPosition(), width);
            logicManager.enemies.add(new Enemy(item.getTextureRegion(), groundedPosition, width, height, groundLayer, EnemyTypeEnum.BARNACLE));
        }
    }

    public void addItemToItemList(Item item, String imageName, MapObject mapObject){
        if(item.getImageName().equals(UFO.getRegionName())){
            logicManager.items.add(new Item(item.getTextureRegion(), UFO_WIDTH, UFO_HEIGHT, item.getPosition(), imageName, false));
        } else if(item.getImageName().equals(SPRING.getRegionName())){
            logicManager.items.add(new Item(item.getTextureRegion(), STANDARD_OBJECT_SIZE, SPRING_HEIGHT, item.getPosition(), imageName, true));
        } else if(item.getImageName().equals(WEIGHT.getRegionName())){
            Integer weightId = Integer.parseInt(mapObject.getProperties().get("weightId").toString());
            logicManager.items.add(new Weight(item.getTextureRegion(), STANDARD_OBJECT_SIZE, STANDARD_OBJECT_SIZE, item.getPosition(), imageName, true, weightId, groundLayer));
        } else if(item.getImageName().equals(SWITCH.getRegionName())){
            Integer switchId = Integer.parseInt(mapObject.getProperties().get("switchId").toString());
            logicManager.items.add(new Switch(item.getTextureRegion(), SWITCH_SIZE, SWITCH_SIZE, item.getPosition(), imageName, true, switchId));
        } else if(item.getImageName().equals(LEVER.getRegionName())){
            Integer leverId = Integer.parseInt(mapObject.getProperties().get("leverId").toString());
            logicManager.items.add(new Lever(item.getTextureRegion(), LEVER_SIZE, LEVER_SIZE, item.getPosition(), imageName, true, leverId));
            putLeverOrientations(leverId);
        }
    }

    private void putLeverOrientations(Integer leverId){
        if(leverId == 1) logicManager.leverOrientations.put(leverId, LeverOrientationEnum.RIGHT);
        if(leverId == 2) logicManager.leverOrientations.put(leverId, LeverOrientationEnum.UP);
        if(leverId == 3) logicManager.leverOrientations.put(leverId, LeverOrientationEnum.LEFT);
        if(leverId == 4) logicManager.leverOrientations.put(leverId, LeverOrientationEnum.RIGHT);
        if(leverId == 5) logicManager.leverOrientations.put(leverId, LeverOrientationEnum.LEFT);
    }

    private Vector2 getInitialPlayerPositionFromObjectLayer() {
        MapObject spawnObject = map.getLayers().get(TILE_LAYER_SPAWN).getObjects().get("PlayerSpawn");

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

    private void setMapSize(){
        logicManager.setMapWidth(map.getProperties().get("width", Integer.class)
            * map.getProperties().get("tilewidth", Integer.class));

        logicManager.setMapHeight(map.getProperties().get("height", Integer.class)
            * map.getProperties().get("tileheight", Integer.class));
    }

    //endregion
}
