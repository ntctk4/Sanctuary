package com.logicbytez.sanctuary.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.Main;
import com.logicbytez.sanctuary.game.input.Gamepad;

public class MonologueScreen implements Screen {
	private Main game;
	private SpriteBatch batch;
	
	private Stage stage;
	private Viewport viewport;
		
	public MonologueScreen(Main game) {
		this.game = game;
		batch = game.batch;
		viewport = new FitViewport(game.view.x * 2, game.view.y * 2, new OrthographicCamera());
		
		stage = new Stage(viewport);
		Table table = new Table();
		table.center();
		table.setFillParent(true);
		stage.addActor(table);
		
		Label.LabelStyle style = new Label.LabelStyle(Assets.fontMonologue, Color.WHITE);
		
		table.add(new Label("Welcome, Hero, to the ancient Labyrinth. You are the last", style)).expandX();
		table.row();
		table.add(new Label("of the guardians of the Sanctuary. Your brethren have all", style)).expandX();
		table.row();
		table.add(new Label("perished, so now you alone must defend the Sanctuary", style)).expandX();
		table.row();
		table.add(new Label("against an army of shadowy monsters, known as Eidolon, and", style)).expandX();
		table.row();
		table.add(new Label("find a way to defeat the encroaching evil once and for all.", style)).expandX();
		table.row();
		table.add(new Label("", style));
		table.row();
		table.add(new Label("Continue", style));
		
		if(game.testing)
			table.debug();
	}
	
	@Override
	public void show() {
		stage.getRoot().setColor(1, 1, 1, 0);
		stage.addAction(Actions.fadeIn(1f));
	}

	private void handleInput() {
		if(Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.justTouched() ||
				(game.players.first().getGamePad() != null && game.players.first().getGamePad().getController().getButton(Gamepad.A))) {
			stage.addAction(Actions.sequence(Actions.fadeOut(2f), Actions.run(new Runnable() {
				@Override
				public void run() {
					game.setScreen(game.gameScreen);
					dispose();
				}
			})));

		}
	}
	
	@Override
	public void render(float delta) {
		handleInput();
		stage.act(delta);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
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
}
