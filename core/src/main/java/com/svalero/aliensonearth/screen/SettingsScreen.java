package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.LabelsEnum;
import com.svalero.aliensonearth.util.enums.PrefsNamesEnum;

import static com.svalero.aliensonearth.Main.prefs;

public class SettingsScreen implements Screen {

    //region properties

    private Stage stage;
    private Game game;

    //endregion

    //region constructor

    public SettingsScreen(Game game){
        this.game = game;
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
        musicCheckBox.setChecked(prefs.getBoolean(PrefsNamesEnum.MUSIC.getPrefsName(), true));

        VisLabel musicVolumeLabel = new VisLabel("Music volume");
        settingsSaved.setFontScale(0.5f);

        VisSlider musicVolumeSlider = new VisSlider(0.1f, 1f, 0.01f, false);
        musicVolumeSlider.setValue(prefs.getFloat(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName()));

        VisTextButton saveButton = new VisTextButton("Save");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                prefs.putBoolean(PrefsNamesEnum.MUSIC.getPrefsName(), musicCheckBox.isChecked());
                prefs.putFloat(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName(), musicVolumeSlider.getValue());

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
        table.add(ResourceManager.getLabel(LabelsEnum.SETTINGS)).center();
        table.row().padTop(60);
        table.add(musicCheckBox).left();
        table.row().padTop(15);
        table.add(musicVolumeLabel).left();
        table.add(musicVolumeSlider).left();
        table.row().padTop(30);
        table.add(settingsSaved).center();
        table.row().padTop(10);
        table.add(saveButton);
        table.add(returnButton);

        Gdx.input.setInputProcessor(stage);
    }

    //endregion
}
