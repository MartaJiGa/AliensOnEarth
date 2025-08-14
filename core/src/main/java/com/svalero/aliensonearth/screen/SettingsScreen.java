package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.util.enums.LabelsEnum;
import com.svalero.aliensonearth.util.enums.PrefsNamesEnum;

import static com.svalero.aliensonearth.Main.db;
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

        VisLabel settingsSaved = new VisLabel();
        settingsSaved.setFontScale(0.5f);
        settingsSaved.setVisible(false);

        VisCheckBox musicCheckBox = new VisCheckBox("Music");
        musicCheckBox.setChecked(prefs.getBoolean(PrefsNamesEnum.MUSIC.getPrefsName(), true));

        VisLabel musicVolumeLabel = new VisLabel("Music volume");
        settingsSaved.setFontScale(0.5f);

        VisSlider musicVolumeSlider = new VisSlider(0.1f, 1f, 0.01f, false);
        musicVolumeSlider.setValue(prefs.getFloat(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName()));

        VisCheckBox newPlayerCheckBox = new VisCheckBox("New Player");
        newPlayerCheckBox.setChecked(false);

        VisLabel playerLabel = new VisLabel("Select player");
        VisSelectBox<String> playerSelectBox = new VisSelectBox<>();
        playerSelectBox.setItems(db.getAllPlayers().toArray(new String[0]));
        playerSelectBox.setSelected(prefs.getString(PrefsNamesEnum.PLAYER_NAME.getPrefsName()));

        VisLabel newPlayerLabel = new VisLabel("New player");
        VisTextField newPlayerTextField = new VisTextField();

        VisTable selectPlayerTable = new VisTable(true);
        selectPlayerTable.add(playerLabel).padRight(10).left();
        selectPlayerTable.add(playerSelectBox).width(400);

        VisTable newPlayerTable = new VisTable(true);
        newPlayerTable.add(newPlayerLabel).padRight(10).left();
        newPlayerTable.add(newPlayerTextField).width(400);

        Stack playerStack = new Stack();
        playerStack.add(selectPlayerTable);
        playerStack.add(newPlayerTable);
        playerStack.setWidth(500);

        selectPlayerTable.setVisible(true);
        newPlayerTable.setVisible(false);

        newPlayerCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isNew = newPlayerCheckBox.isChecked();
                selectPlayerTable.setVisible(!isNew);
                newPlayerTable.setVisible(isNew);
            }
        });

        VisTextButton saveButton = new VisTextButton("Save");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                boolean duplicateName = false;

                if(newPlayerTextField.isEmpty()){
                    prefs.putString(PrefsNamesEnum.PLAYER_NAME.getPrefsName(), playerSelectBox.getSelected());
                } else{
                    Array<String> players = playerSelectBox.getItems();
                    for(int i = 0; i < players.size; i++){
                        if(players.get(i).toLowerCase().equals(newPlayerTextField.getText().toLowerCase())){
                            duplicateName = true;
                        }
                    }

                    if(!duplicateName){
                        db.savePlayerProgress(newPlayerTextField.getText(), 1, 1, 1, 0, -1);
                        prefs.putString(PrefsNamesEnum.PLAYER_NAME.getPrefsName(), newPlayerTextField.getText());
                        playerSelectBox.clearItems();
                        playerSelectBox.setItems(db.getAllPlayers().toArray(new String[0]));
                        playerSelectBox.setSelected(prefs.getString(PrefsNamesEnum.PLAYER_NAME.getPrefsName()));
                        newPlayerCheckBox.setChecked(false);
                    }

                    newPlayerTextField.setText("");
                }

                prefs.putBoolean(PrefsNamesEnum.MUSIC.getPrefsName(), musicCheckBox.isChecked());
                prefs.putFloat(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName(), musicVolumeSlider.getValue());

                prefs.flush();

                if(duplicateName){
                    settingsSaved.setText("Settings saved successfully except new player, which it already exists in database.");
                } else{
                    settingsSaved.setText("Settings saved successfully.");
                }
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
        table.add(musicCheckBox).left().padLeft(50);

        table.row().padTop(15);
        table.add(musicVolumeLabel).left().padLeft(50);
        table.add(musicVolumeSlider).left().padLeft(50);

        table.row().padTop(15);
        table.add(newPlayerCheckBox).left().padLeft(50);
        table.add(playerStack).colspan(2).expandX().fillX();

        table.row().padTop(30);
        table.add(settingsSaved).center();

        table.row().padTop(10);
        table.add(saveButton).right();
        table.add(returnButton).right().padRight(50);

        Gdx.input.setInputProcessor(stage);
    }

    //endregion
}
