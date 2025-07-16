package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.svalero.aliensonearth.util.enums.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResourceManager {

    //region properties

    private static AssetManager assetManager = new AssetManager();
    private static Map<String, Label> labels = new HashMap<>();

    //endregion

    //region methods

    public static boolean update(){
        return assetManager.update();
    }

    public static void loadAllResources(){
        assetManager.load("textures/coins/coins.atlas", TextureAtlas.class);
        assetManager.load("textures/aliens/aliens.atlas", TextureAtlas.class);

        loadMusic();
        loadSounds();
    }

    public static void loadMusic(){
        assetManager.load(MusicEnum.BACKGROUND.getFileName(), Music.class);
        assetManager.load(MusicEnum.MENU.getFileName(), Music.class);
    }

    public static void loadSounds(){
        assetManager.load(SoundsEnum.COIN.getFileName(), Sound.class);
    }

    public static void generateLabels(){
        BitmapFont aliensPartTitle = generateFont(FontsEnum.ALIEN.getFileName(), 80);
        BitmapFont onEarthPartTitle = generateFont(FontsEnum.ON_EARTH.getFileName(), 48);

        Label.LabelStyle aliensStyle = new Label.LabelStyle(aliensPartTitle, Color.BLACK);
        Label.LabelStyle onEarthStyle = new Label.LabelStyle(onEarthPartTitle, Color.BLACK);
        Label.LabelStyle pauseStyle = new Label.LabelStyle(onEarthPartTitle, Color.WHITE);

        labels.put(LabelsEnum.ALIEN.name(), new Label("ALIENS", aliensStyle));
        labels.put(LabelsEnum.ON_EARTH.name(), new Label("ON EARTH", onEarthStyle));
        labels.put(LabelsEnum.PAUSE.name(), new Label("PAUSE", pauseStyle));
        labels.put(LabelsEnum.SETTINGS.name(), new Label("SETTINGS", pauseStyle));
    }

    public static BitmapFont generateFont(String fontPathWithFile, int fontSize){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPathWithFile));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    public static Music getMusic(MusicEnum musicEnum){
        return assetManager.get(musicEnum.getFileName(), Music.class);
    }

    public static Sound getSound(SoundsEnum soundsEnum){
        return assetManager.get(soundsEnum.getFileName(), Sound.class);
    }

    public static TextureRegion getAlienTexture(String name){
        return assetManager.get("textures/aliens/aliens.atlas", TextureAtlas.class).findRegion(name);
    }

    public static TextureRegion getCoinTexture(String name){
        return assetManager.get("textures/coins/coins.atlas", TextureAtlas.class).findRegion(name);
    }

    public static Label getLabel(LabelsEnum labelsEnum){
        return labels.get(labelsEnum.name());
    }

    public static void dispose() {
        labels.clear();
    }

    //endregion
}
