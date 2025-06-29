package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lombok.Data;

@Data
public class ResourceManager {

    //region properties

    protected static Texture playerTexture;
    protected static Texture bronzeTexture;
    protected static Texture silverTexture;
    protected static Texture goldTexture;

    protected static Sound coinSound;
    protected static Music backgroundMusic;
    protected static Music menuMusic;

    protected static Label aliensLabel;
    protected static Label onEarthLabel;

    //endregion

    //region methods

    public void loadAllResources(){
        loadSounds();
        loadTextures();
        loadFonts();
    }

    public void loadTextures(){
        playerTexture = new Texture("textures/character_pink_front.png");
        bronzeTexture = new Texture("textures/coin_bronze.png");
        silverTexture = new Texture("textures/coin_silver.png");
        goldTexture = new Texture("textures/coin_gold.png");
    }

    public void loadSounds(){
        coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin_sound.ogg"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameAmbient-funk.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menuSong.mp3"));
    }

    public void loadFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AliensFontTitle.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        parameter.color = Color.BLACK;
        BitmapFont aliensPartTitle = generator.generateFont(parameter);
        generator.dispose();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OnEarthFontTitle.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.color = Color.BLACK;
        BitmapFont onEarthPartTitle = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle aliensStyle = new Label.LabelStyle(aliensPartTitle, null);
        Label.LabelStyle onEarthStyle = new Label.LabelStyle(onEarthPartTitle, null);

        aliensLabel = new Label("ALIENS", aliensStyle);
        onEarthLabel = new Label("ON EARTH", onEarthStyle);
    }

    public Music getBackgroundMusic(){
        return backgroundMusic;
    }

    public Music getMenuMusic(){
        return menuMusic;
    }

    public Label getAliensLabel(){
        return aliensLabel;
    }

    public Label getOnEarthLabel(){
        return onEarthLabel;
    }

    public void dispose() {
        playerTexture.dispose();
        bronzeTexture.dispose();
        silverTexture.dispose();
        goldTexture.dispose();

        backgroundMusic.dispose();
        menuMusic.dispose();
        coinSound.dispose();
    }

    //endregion
}
