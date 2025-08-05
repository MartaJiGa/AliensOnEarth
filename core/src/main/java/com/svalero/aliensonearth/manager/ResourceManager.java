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
import com.badlogic.gdx.utils.Array;
import com.svalero.aliensonearth.util.enums.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.svalero.aliensonearth.Main.prefs;

@Data
public class ResourceManager {

    //region properties

    private static AssetManager assetManager = new AssetManager();
    private static Map<String, Label> labels = new HashMap<>();

    private static String TEXTURE_ATLAS_ALIENS = "textures/aliens.atlas";
    private static String TEXTURE_ATLAS_ENEMIES = "textures/enemies.atlas";
    private static String TEXTURE_ATLAS_INTERACTION = "textures/interaction.atlas";

    //endregion

    //region methods

    public static boolean update(){
        return assetManager.update();
    }

    public static void loadAllResources(){
        assetManager.load(TEXTURE_ATLAS_ALIENS, TextureAtlas.class);
        assetManager.load(TEXTURE_ATLAS_ENEMIES, TextureAtlas.class);
        assetManager.load(TEXTURE_ATLAS_INTERACTION, TextureAtlas.class);

        loadMusic();
        loadSounds();
    }

    public static void loadMusic(){
        assetManager.load(MusicEnum.BACKGROUND.getFileName(), Music.class);
        assetManager.load(MusicEnum.MENU.getFileName(), Music.class);
    }

    public static void loadSounds(){
        assetManager.load(SoundsEnum.COIN.getFileName(), Sound.class);
        assetManager.load(SoundsEnum.DESAPPEAR.getFileName(), Sound.class);
        assetManager.load(SoundsEnum.HURT.getFileName(), Sound.class);
        assetManager.load(SoundsEnum.JUMP.getFileName(), Sound.class);
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
        labels.put(LabelsEnum.GAME_OVER.name(), new Label("GAME OVER", pauseStyle));
        labels.put(LabelsEnum.FINISH.name(), new Label("LEVEL FINISHED", pauseStyle));
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
        Music music = assetManager.get(musicEnum.getFileName(), Music.class);
        music.setVolume(prefs.getFloat(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName()));
        return music;
    }

    public static Sound getSound(SoundsEnum soundsEnum){
        return assetManager.get(soundsEnum.getFileName(), Sound.class);
    }

    public static TextureRegion getAlienTexture(String name){
        return assetManager.get(TEXTURE_ATLAS_ALIENS, TextureAtlas.class).findRegion(name);
    }

    public static TextureRegion getEnemyTexture(String name){
        return assetManager.get(TEXTURE_ATLAS_ENEMIES, TextureAtlas.class).findRegion(name);
    }

    public static TextureRegion getInteractionTexture(String name){
        return assetManager.get(TEXTURE_ATLAS_INTERACTION, TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureAtlas.AtlasRegion> getAlienRegions(String name){
        return assetManager.get(TEXTURE_ATLAS_ALIENS, TextureAtlas.class).findRegions(name);
    }

    public static Array<TextureAtlas.AtlasRegion> getEnemyRegions(String name){
        return assetManager.get(TEXTURE_ATLAS_ENEMIES, TextureAtlas.class).findRegions(name);
    }

    public static Label getLabel(LabelsEnum labelsEnum){
        return labels.get(labelsEnum.name());
    }

    public static void dispose() {
        labels.clear();
    }

    //endregion
}
