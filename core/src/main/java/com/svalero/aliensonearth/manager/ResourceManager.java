package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    protected static Label pauseLabel;

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
        BitmapFont aliensPartTitle = generateFont("fonts/AliensFontTitle.ttf", 80);
        BitmapFont onEarthPartTitle = generateFont("fonts/OnEarthFontTitle.ttf", 48);

        Label.LabelStyle aliensStyle = new Label.LabelStyle(aliensPartTitle, Color.BLACK);
        Label.LabelStyle onEarthStyle = new Label.LabelStyle(onEarthPartTitle, Color.BLACK);
        Label.LabelStyle pauseStyle = new Label.LabelStyle(onEarthPartTitle, Color.WHITE);

        aliensLabel = new Label("ALIENS", aliensStyle);
        onEarthLabel = new Label("ON EARTH", onEarthStyle);
        pauseLabel = new Label("PAUSE", pauseStyle);
    }

    public BitmapFont generateFont(String fontPathWithFile, int fontSize){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPathWithFile));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
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

    public Label getPauseLabel(){
        return pauseLabel;
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
