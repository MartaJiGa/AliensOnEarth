package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.svalero.aliensonearth.manager.ResourceManager;

public class MainMenuScreen implements Screen {

    //region properties

    private Stage stage;
    private ResourceManager resourceManager;

    private Game game;
    private Music menuMusic;

    //endregion

    //region constructor

    public MainMenuScreen(Game game){
        this.game = game;
    }

    //endregion

    //region override

    @Override
    public void show() {
        resourceManager = new ResourceManager();
        resourceManager.loadAllResources();

        menuMusic = resourceManager.getMenuMusic();
        menuMusic.setLooping(true);
        menuMusic.play();

        loadStage();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0.953f, 0.780f, 0.647f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();

        menuMusic.dispose();
    }

    //endregion

    //region methods

    public void loadStage(){
        if(!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        table.center();

        stage = new Stage();
        stage.addActor(table);

        VisTextButton playButton = new VisTextButton("Play");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });

        VisTextButton configButton = new VisTextButton("Configuration");
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen(game));
            }
        });

        VisTextButton exitButton = new VisTextButton("Exit");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                VisUI.dispose();
                Gdx.app.exit();
            }
        });

        table.row();
        table.add(resourceManager.getAliensLabel()).center();
        table.row();
        table.add(resourceManager.getOnEarthLabel()).center();
        table.row().padTop(60);
        table.add(playButton).center();
        table.row().padTop(10);
        table.add(configButton).center();
        table.row().padTop(10);
        table.add(exitButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    //endregion
}
