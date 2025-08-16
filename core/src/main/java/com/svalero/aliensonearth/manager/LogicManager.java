package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.svalero.aliensonearth.domain.*;
import com.svalero.aliensonearth.domain.coin.*;
import com.svalero.aliensonearth.domain.enemies.Barnacle;
import com.svalero.aliensonearth.domain.enemies.FlyingEnemy;
import com.svalero.aliensonearth.domain.interactionObject.Lever;
import com.svalero.aliensonearth.domain.interactionObject.Switch;
import com.svalero.aliensonearth.domain.interactionObject.Weight;
import com.svalero.aliensonearth.util.enums.*;
import com.svalero.aliensonearth.util.enums.states.*;
import com.svalero.aliensonearth.util.enums.textures.EnemyTexturesEnum;

import java.util.HashMap;

import static com.svalero.aliensonearth.Main.db;
import static com.svalero.aliensonearth.Main.prefs;
import static com.svalero.aliensonearth.util.Constants.*;
import static com.svalero.aliensonearth.util.enums.textures.InteractionTexturesEnum.*;

public class LogicManager {

    //region properties

    protected Player player;
    protected Array<Coin> coins;
    protected Array<Item> items;
    protected Array<Enemy> enemies;
    protected Array<FlyingEnemy> flyingEnemies;
    protected Array<Fireball> fireballs;
    protected Item fullHubHeart, halfHubHeart, emptyHubHeart;
    protected HashMap<Integer, LeverOrientationEnum> leverOrientations;
    private boolean isPaused, isFinished, isDead, moving, jumping, climbing;
    private float enemyCollisionCooldown, playerEnemyCollisionHitTexture, spawnTimer, spawnInterval, mapWidth, mapHeight;
    public int currentLevel;

    //endregion

    //region constructor

    public LogicManager(){
        isPaused = false;
        isFinished = false;
        isDead = false;
        enemyCollisionCooldown = 0f;
        playerEnemyCollisionHitTexture = 0f;
        currentLevel = 1;
        spawnTimer = 0f;
        spawnInterval = 2f;

        flyingEnemies = new Array<>();
        fireballs = new Array<>();
        leverOrientations = new HashMap<>();

        fullHubHeart = new Item(ResourceManager.getInteractionTexture(HUB_HEART_FULL.getRegionName()), 30,30);
        halfHubHeart = new Item(ResourceManager.getInteractionTexture(HUB_HEART_HALF.getRegionName()), 30,30);
        emptyHubHeart = new Item(ResourceManager.getInteractionTexture(HUB_HEART_EMPTY.getRegionName()), 30,30);
    }

    //endregion

    //region methods

    private void managePlayerInput(){
        moving = false;
        jumping = false;
        climbing = false;

        Lever leverNearby;

        if (Gdx.input.isKeyPressed(Input.Keys.V) && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            leverNearby = getLeverWhenPlayerIsNear();
            if (leverNearby != null)
                leverNearby.changeOrientation(LeverOrientationEnum.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.V) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            leverNearby = getLeverWhenPlayerIsNear();
            if (leverNearby != null)
                leverNearby.changeOrientation(LeverOrientationEnum.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.V) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            leverNearby = getLeverWhenPlayerIsNear();
            if (leverNearby != null)
                leverNearby.changeOrientation(LeverOrientationEnum.UP);
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.C)){
            if (player.isOnClimbTile(player.getPosition())) {
                player.setState(AlienAnimationStatesEnum.CLIMB);
                player.climb(+PLAYER_SPEED);
                climbing = true;
                player.setIsFacingRight(null);
                player.setIsFacingUp(true);
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.C)){
            if (player.isOnClimbTile(player.getPosition())) {
                player.setState(AlienAnimationStatesEnum.CLIMB);
                player.climb(-PLAYER_SPEED);
                climbing = true;
                player.setIsFacingRight(null);
                player.setIsFacingUp(false);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (player.isSolidTileBelow()){
                player.setState(AlienAnimationStatesEnum.WALK_RIGHT);
            } else{
                player.setState(AlienAnimationStatesEnum.JUMP_RIGHT);
            }
            player.move(PLAYER_SPEED);
            moving = true;
            player.setIsFacingRight(true);
            player.setIsFacingUp(null);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (player.isSolidTileBelow()){
                player.setState(AlienAnimationStatesEnum.WALK_LEFT);
            } else{
                player.setState(AlienAnimationStatesEnum.JUMP_LEFT);
            }
            player.move(-PLAYER_SPEED);
            moving = true;
            player.setIsFacingRight(false);
            player.setIsFacingUp(null);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.setState(AlienAnimationStatesEnum.JUMP);
            player.jump();
            jumping = true;
            player.setIsFacingRight(null);
            player.setIsFacingUp(null);
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pauseGame();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setState(AlienAnimationStatesEnum.JUMP_RIGHT);
            player.setIsFacingRight(true);
            player.setIsFacingUp(null);
            player.jump();
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.setState(AlienAnimationStatesEnum.JUMP_LEFT);
            player.setIsFacingRight(false);
            player.setIsFacingUp(null);
            player.jump();
        }

        if (!moving && !jumping && !climbing) {
            Boolean playerFacingRight = player.getIsFacingRight();
            Boolean playerFacingUp = player.getIsFacingUp();
            if (playerFacingUp != null) {
                player.setState(AlienAnimationStatesEnum.FRONT_CLIMB);
            } else if (playerFacingRight == null) {
                player.setState(AlienAnimationStatesEnum.FRONT);
            } else if (playerFacingRight) {
                player.setState(AlienAnimationStatesEnum.IDLE_RIGHT);
            } else {
                player.setState(AlienAnimationStatesEnum.IDLE_LEFT);
            }
        }
    }

    private void manageCollisions(){
        Rectangle playerRectangle = player.getRectangle();

        for (int i = coins.size - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            if (coin.getRectangle().overlaps(playerRectangle)) {
                coins.removeIndex(i);
                makeCoinCollisionConsequences(coin);
            }
        }

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if (enemy.getRectangle().overlaps(playerRectangle) && enemyCollisionCooldown <= 0) {
                makeEnemyCollisionConsequences();
            }
        }

        for (int i = items.size - 1; i >= 0; i--) {
            Item item = items.get(i);
            if (item.getRectangle().overlaps(playerRectangle)) {
                makeItemCollisionConsequences(item);
            }
        }
    }

    private Lever getLeverWhenPlayerIsNear() {
        Rectangle playerRect = player.getRectangle();

        for (Item item : items) {
            if (item instanceof Lever) {
                Lever lever = (Lever) item;
                if (lever.getRectangle().overlaps(playerRect)) {
                    return lever;
                }
            }
        }
        return null;
    }

    public void makeCoinCollisionConsequences(Coin coin){
        player.changeScore(coin.getPoints());
        ResourceManager.getSound(SoundsEnum.COIN).play();

        checkPlayerLevel();
    }

    public void makeEnemyCollisionConsequences(){
        player.reduceLives(1);
        player.setState(AlienAnimationStatesEnum.HIT);
        ResourceManager.getSound(SoundsEnum.HURT).play();

        enemyCollisionCooldown = ENEMY_COLLISION_COOLDOWN_TIME;
        playerEnemyCollisionHitTexture = PLAYER_ENEMY_COLLISION_HIT_TEXTURE_TIME;
    }

    public void makeItemCollisionConsequences(Item item){
        if(item.getImageName().equals(UFO.getRegionName())){
            isFinished = true;
            saveProgressInDb();
        } else if(item.getImageName().equals(SPRING.getRegionName())){
            boolean isLandingOnSpring = checkIfPlayerIsLandingOnInteractionObject(item);
            if (isLandingOnSpring && !item.isActivated()) {
                item.activate();
                player.bounce(SPRING_JUMP_FORCE);
            }
        } else if(item.getImageName().equals(SWITCH.getRegionName())){
            boolean isLandingOnSwitch = checkIfPlayerIsLandingOnInteractionObject(item);
            if (isLandingOnSwitch) {
                checkLeverDirections(item);
            }
        }
    }

    public boolean checkIfPlayerIsLandingOnInteractionObject(Item item){
        Rectangle playerRect = player.getRectangle();
        Rectangle itemRect = item.getRectangle();

        float playerBottom = playerRect.y;
        float itemTop = itemRect.y + itemRect.height;

        return playerBottom >= itemTop - 10f;
    }

    public void checkLeverDirections(Item item){
        HashMap<Integer, Boolean> checkedOrientations = new HashMap<>();
        Switch pressedSwitch = (Switch)item;

        for (Item itemInList : items) {
            if (itemInList instanceof Lever) {
                Lever lever = (Lever) itemInList;
                int id = lever.getLeverID();

                LeverOrientationEnum objectOrientation = lever.getOrientation();
                LeverOrientationEnum correctOrientation = leverOrientations.get(id);

                if (correctOrientation.equals(objectOrientation))
                    checkedOrientations.put(id, true);
                else
                    checkedOrientations.put(id, false);
            }
        }

        if(pressedSwitch.getSwitchId() == 1){
            Boolean orientation1 = checkedOrientations.get(1);
            Boolean orientation2 = checkedOrientations.get(2);

            if(orientation1 && orientation2){
                activateWeight(1);
            }
        } else if(pressedSwitch.getSwitchId() == 2){
            Boolean orientation1 = checkedOrientations.get(3);
            Boolean orientation2 = checkedOrientations.get(4);
            Boolean orientation3 = checkedOrientations.get(5);

            if(orientation1 && orientation2 && orientation3){
                activateWeight(2);
            }
        }
    }

    public void activateWeight(int weightId){
        for (Item itemInList : items) {
            if (itemInList instanceof Weight) {
                Weight weight = (Weight) itemInList;
                int id = weight.getWeightId();

                if (id == weightId)
                    weight.activate();
                break;
            }
        }
    }

    public void checkPlayerLevel(){
        int newLevel = player.getGlobalScore() / 50 + 1;
        if(newLevel > player.getLevel())
            player.setLevel(newLevel);
    }

    public void saveProgressInDb(){
        int lastLevelPlayed = player.getCurrentGameLevel();
        int higherLevelPlayed = db.getHigherLevelPlayed(player.getId());
        int playerId = db.getPlayerIdByName(prefs.getString(PrefsNamesEnum.PLAYER_NAME.getPrefsName()));

        if(higherLevelPlayed >= lastLevelPlayed){
            higherLevelPlayed = lastLevelPlayed + 1;
        }
        db.savePlayerProgress(player.getName(), higherLevelPlayed, lastLevelPlayed, player.getLevel(), player.getGlobalScore(), playerId);
        db.saveGameProgress(player.getId(), lastLevelPlayed, player.getScore());
    }

    public void pauseGame(){
        isPaused = true;
    }

    public void resumeGame(){
        isPaused = false;
    }

    public boolean isPaused(){
        return isPaused;
    }

    public boolean isFinished(){
        return isFinished;
    }

    public boolean isDead(){
        return isDead;
    }

    public void update(float dt){
        if(!isPaused){
            if(playerEnemyCollisionHitTexture <= 0){
                if(player.getLives() <= 0){
                    isDead = true;
                } else{
                    managePlayerInput();
                }
            }

            manageCollisions();

            // Iterate item list
            for (int i = 0; i < items.size; i++) {
                Item item = items.get(i);
                if (item.getImageName().equals(SPRING)) {
                    Rectangle springRect = item.getRectangle();
                    Rectangle playerRect = player.getRectangle();

                    boolean landedOnSpring = springRect.overlaps(playerRect) &&
                        player.getSpeed().y <= 0 &&
                        player.getPosition().y > item.getPosition().y;

                    if (landedOnSpring) {
                        player.bounce(PLAYER_JUMPING_SPEED * 1.5f);
                        item.setImageName(SPRING_OUT.getRegionName());
                        item.setTextureRegion(ResourceManager.getInteractionTexture(SPRING_OUT.getRegionName()));

                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                item.setImageName(SPRING.getRegionName());
                                item.setTextureRegion(ResourceManager.getInteractionTexture(SPRING.getRegionName()));
                            }
                        }, 1.0f);
                    }
                } else if(item.getImageName().equals(SWITCH.getRegionName())) {
                    Rectangle interactionObjectRect = item.getRectangle();
                    Rectangle playerRect = player.getRectangle();

                    boolean landedOnSwitch = interactionObjectRect.overlaps(playerRect) &&
                        player.getSpeed().y <= 0 &&
                        player.getPosition().y > item.getPosition().y;

                    if (landedOnSwitch) {
                        item.setImageName(SWITCH_PRESSED.getRegionName());
                        item.setTextureRegion(ResourceManager.getInteractionTexture(SWITCH_PRESSED.getRegionName()));

                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                item.setImageName(SWITCH.getRegionName());
                                item.setTextureRegion(ResourceManager.getInteractionTexture(SWITCH.getRegionName()));
                            }
                        }, 0.75f);
                    }
                }
            }

            if(playerEnemyCollisionHitTexture <= 0 || playerEnemyCollisionHitTexture == PLAYER_ENEMY_COLLISION_HIT_TEXTURE_TIME)
                player.update(dt, mapWidth);

            manageFlyingEnemy(dt);

            // Iterate enemy list
            for(int i = 0; i < enemies.size; i++){
                Enemy enemy = enemies.get(i);

                Vector2 enemyPos = enemy.getPosition();
                Vector2 playerPos = player.getPosition();
                float distance = enemyPos.dst(playerPos);

                enemy.setPlayerNearby(distance < enemy.getEnemyDistanceFromPlayer());
                enemy.update(dt);
                if(enemy instanceof Barnacle){
                    Barnacle barnacle = (Barnacle)enemy;

                    if(barnacle.getTimeSinceLastAttack() >= barnacle.getAttackCooldown()){
                        fireballs.add(barnacle.launchFireballAtPlayer(player));
                        barnacle.setTimeSinceLastAttack(0f);
                    }
                }

                for (int j = 0; j < items.size; j++){
                    if (items.get(j) instanceof Weight) {
                        Weight weight = (Weight) items.get(j);

                        if (weight.isFinished()) {
                            if (weight.getRectangle().overlaps(enemy.getRectangle())) {
                                enemies.removeIndex(i);
                                items.removeIndex(j);
                                //TODO: Poner sonido
                                //ResourceManager.getSound(SoundsEnum.HIT).play();
                                break;
                            }
                        }
                    }
                }
            }

            if (enemyCollisionCooldown > 0)
                enemyCollisionCooldown -= dt;

            if (playerEnemyCollisionHitTexture > 0)
                playerEnemyCollisionHitTexture -= dt;


            // Iterate fireball list
            for(int i = 0; i < fireballs.size; i++){
                Fireball fireball = fireballs.get(i);

                fireball.getPosition().add(fireball.getVelocity().cpy().scl(dt));
                fireball.getRectangle().setPosition(fireball.getPosition());

                if (fireball.getRectangle().overlaps(player.getRectangle())) {
                    player.reduceLives(1);
                    fireballs.removeIndex(i);
                    continue;
                }

                if (fireball.getPosition().x < 0 || fireball.getPosition().x > mapWidth ||
                    fireball.getPosition().y < 0 || fireball.getPosition().y > mapHeight) {
                    fireballs.removeIndex(i);
                }
            }

            for (Item item : items) {
                item.update(dt);
            }
        }
    }

    public void manageFlyingEnemy(float dt) {
        spawnTimer += dt;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;

            TextureRegion texture;
            FlyingEnemy flyingEnemy;
            float spawnX = mapWidth;
            float spawnY = MathUtils.random(50, SCREEN_HEIGHT - 50);

            float xSpeed = MathUtils.random(100, 400);

            if(prefs.getInteger(PrefsNamesEnum.CURRENT_LEVEL.getPrefsName()) == 1){
                texture = ResourceManager.getEnemyTexture(EnemyTexturesEnum.BEE_A.getRegionName());
                flyingEnemy = new FlyingEnemy(texture, new Vector2(spawnX, spawnY), 64, 64, new TiledMapTileLayer(1,1,1,1), EnemyTypeEnum.BEE, -xSpeed);
            } else{
                texture = ResourceManager.getEnemyTexture(EnemyTexturesEnum.FLY_A.getRegionName());
                flyingEnemy = new FlyingEnemy(texture, new Vector2(spawnX, spawnY), 64, 64, new TiledMapTileLayer(1,1,1,1), EnemyTypeEnum.FLY, -xSpeed);
            }

            flyingEnemies.add(flyingEnemy);
        }

        for (int i = flyingEnemies.size - 1; i >= 0; i--) {
            FlyingEnemy flyingEnemy = flyingEnemies.get(i);
            flyingEnemy.update(dt);

            if (flyingEnemy.getRectangle().overlaps(player.getRectangle()) && enemyCollisionCooldown <= 0) {
                makeEnemyCollisionConsequences();
            }

            if (flyingEnemy.getPosition().x + flyingEnemy.getWidth() < 0) {
                flyingEnemies.removeIndex(i);
            }
        }
    }

    public void setMapWidth(float mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(float mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void dispose() {

    }

    //endregion
}
