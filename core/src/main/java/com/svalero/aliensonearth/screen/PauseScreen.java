package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.svalero.aliensonearth.manager.LogicManager;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.LabelsEnum;

public class PauseScreen implements Screen {

    //region properties

    private Stage stage;
    private LogicManager logicManager;

    private Game game;
    private GameScreen gameScreen;

    //endregion

    //region constructor

    public PauseScreen(Game game, GameScreen gameScreen, LogicManager logicManager){
        this.game = game;
        this.gameScreen = gameScreen;
        this.logicManager = logicManager;
    }

    //endregion

    //region override

    @Override
    public void show() {
        loadStage();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
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
        logicManager.dispose();
        gameScreen.dispose();
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

        VisTextButton resumeButton = new VisTextButton("Resume");
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                logicManager.resumeGame();
                game.setScreen(gameScreen);
            }
        });

        VisTextButton mainMenuButton = new VisTextButton("Main Menu");
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
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
        table.add(ResourceManager.getLabel(LabelsEnum.PAUSE)).center();
        table.row().padTop(60);
        table.add(resumeButton).center();
        table.row().padTop(10);
        table.add(mainMenuButton).center();
        table.row().padTop(10);
        table.add(exitButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    //endregion
}
