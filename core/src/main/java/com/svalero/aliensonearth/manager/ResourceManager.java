package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.svalero.aliensonearth.util.enums.Labels;
import com.svalero.aliensonearth.util.enums.Musics;
import com.svalero.aliensonearth.util.enums.Sounds;
import com.svalero.aliensonearth.util.enums.Textures;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResourceManager {

    //region properties

    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();
    private static Map<String, Music> musics = new HashMap<>();
    private static Map<String, Label> labels = new HashMap<>();

    //endregion

    //region methods

    public static void loadAllResources(){
        loadMusic();
        loadSounds();
        loadTextures();
        loadFonts();
    }

    public static void loadMusic(){
        musics.put(Musics.BACKGROUND.name(), Gdx.audio.newMusic(Gdx.files.internal("sounds/gameAmbient-funk.mp3")));
        musics.put(Musics.MENU.name(), Gdx.audio.newMusic(Gdx.files.internal("sounds/menuSong.mp3")));
    }

    public static void loadSounds(){
        sounds.put(Sounds.COIN.name(), Gdx.audio.newSound(Gdx.files.internal("sounds/coin_sound.ogg")));
    }

    public static void loadTextures(){
        textures.put(Textures.PLAYER.name(), new Texture("textures/character_pink_front.png"));
        textures.put(Textures.BRONZE_COIN.name(), new Texture("textures/coin_bronze.png"));
        textures.put(Textures.SILVER_COIN.name(), new Texture("textures/coin_silver.png"));
        textures.put(Textures.GOLD_COIN.name(), new Texture("textures/coin_gold.png"));
    }

    public static void loadFonts(){
        BitmapFont aliensPartTitle = generateFont("fonts/AliensFontTitle.ttf", 80);
        BitmapFont onEarthPartTitle = generateFont("fonts/OnEarthFontTitle.ttf", 48);

        Label.LabelStyle aliensStyle = new Label.LabelStyle(aliensPartTitle, Color.BLACK);
        Label.LabelStyle onEarthStyle = new Label.LabelStyle(onEarthPartTitle, Color.BLACK);
        Label.LabelStyle pauseStyle = new Label.LabelStyle(onEarthPartTitle, Color.WHITE);

        labels.put(Labels.ALIEN.name(), new Label("ALIENS", aliensStyle));
        labels.put(Labels.ON_EARTH.name(), new Label("ON EARTH", onEarthStyle));
        labels.put(Labels.PAUSE.name(), new Label("PAUSE", pauseStyle));
        labels.put(Labels.SETTINGS.name(), new Label("SETTINGS", pauseStyle));
    }

    public static BitmapFont generateFont(String fontPathWithFile, int fontSize){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPathWithFile));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    public static Music getMusic(String name){
        return musics.get(name);
    }

    public static Sound getSound(String name){
        return sounds.get(name);
    }

    public static Texture getTexture(String name){
        return textures.get(name);
    }

    public static Label getLabel(String name){
        return labels.get(name);
    }

    public static void dispose() {
        for (Music music : musics.values()) {
            if (music != null) music.dispose();
        }

        for (Sound sound : sounds.values()) {
            if (sound != null) sound.dispose();
        }

        for (Texture texture : textures.values()) {
            if (texture != null) texture.dispose();
        }

        musics.clear();
        sounds.clear();
        textures.clear();
        labels.clear();
    }

    //endregion
}
