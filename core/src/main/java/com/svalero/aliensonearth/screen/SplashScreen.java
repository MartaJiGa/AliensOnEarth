package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Scaling;
import com.svalero.aliensonearth.manager.ResourceManager;

public class SplashScreen implements Screen {

    //region properties

    private Game game;
    private Stage stage;

    private Texture splashTexture;
    private Image splashImage;

    private boolean splashDone = false;

    //endregion

    //region constructor

    public SplashScreen(Game game){
        this.game = game;
        this.stage = new Stage();

        splashTexture = new Texture(Gdx.files.internal("textures/SplashPicture.png"));
        splashImage = new Image(splashTexture);
        splashImage.setScaling(Scaling.fill);

        Container<Image> splashContainer = new Container<>(splashImage);
        splashContainer.setFillParent(true);
        splashContainer.setTransform(true);
    }

    //endregion

    //region override
    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        splashImage.addAction(Actions.sequence(
            Actions.alpha(0),
            Actions.fadeIn(1f),
            Actions.delay(1.5f),
            Actions.run(() -> splashDone = true))
        );

        table.add(splashImage).center();
        stage.addActor(table);

        ResourceManager.loadAllResources();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(dt);
        stage.draw();

        if(ResourceManager.update()){
            if(splashDone){
                ResourceManager.generateLabels();
                game.setScreen(new MainMenuScreen(game));
            }
        }
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
        splashTexture.dispose();
    }

    //endregion
}
