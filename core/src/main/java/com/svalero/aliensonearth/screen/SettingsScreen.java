package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.Labels;

import static com.svalero.aliensonearth.util.Constants.GAME_NAME;

public class SettingsScreen implements Screen {

    //region properties

    private Stage stage;
    private Preferences prefs;

    private Game game;

    //endregion

    //region constructor

    public SettingsScreen(Game game){
        this.game = game;
        loadPreferences();
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
    }

    //endregion

    //region methods

    private void loadPreferences(){
        prefs = Gdx.app.getPreferences(GAME_NAME);
    }

    public void loadStage(){
        if(!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        table.center();

        stage = new Stage();
        stage.addActor(table);

        VisLabel settingsSaved = new VisLabel("Settings saved successfully.");
        settingsSaved.setFontScale(0.5f);
        settingsSaved.setVisible(false);

        VisCheckBox musicCheckBox = new VisCheckBox("Music");
        musicCheckBox.setChecked(prefs.getBoolean("music", true));

        VisTextButton saveButton = new VisTextButton("Save");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                prefs.putBoolean("music", musicCheckBox.isChecked());

                prefs.flush();
                settingsSaved.setVisible(true);
            }
        });

        VisTextButton returnButton = new VisTextButton("Return");
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        table.row();
        table.add(ResourceManager.getLabel(Labels.SETTINGS.name())).center();
        table.row().padTop(60);
        table.add(musicCheckBox).center();
        table.row().padTop(30);
        table.add(settingsSaved).center();
        table.row().padTop(10);
        table.add(saveButton).center();
        table.row().padTop(10);
        table.add(returnButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    //endregion
}
