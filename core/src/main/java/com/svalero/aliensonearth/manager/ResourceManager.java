package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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

    //endregion

    //region methods

    public void loadAllResources(){
        playerTexture = new Texture("character_pink_front.png");
        bronzeTexture = new Texture("coin_bronze.png");
        silverTexture = new Texture("coin_silver.png");
        goldTexture = new Texture("coin_gold.png");

        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin_sound.ogg"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("gameAmbient-funk.mp3"));
    }

    public Music getBackgroundMusic(){
        return backgroundMusic;
    }

    public void dispose() {
        playerTexture.dispose();
        bronzeTexture.dispose();
        silverTexture.dispose();
        goldTexture.dispose();

        backgroundMusic.dispose();
        coinSound.dispose();
    }

    //endregion
}
