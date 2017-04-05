package com.logicbytez.sanctuary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen implements Screen {
	private Main game;
	
	private Texture logic_bytes;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;
		
	private float stateTime;
	
	public SplashScreen(Main game) {
		this.game = game;
				
		logic_bytes = new Texture(Gdx.files.internal("LogicBytes.png"));
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(game.view.x * 2, game.view.y * 2, camera);
		stage = new Stage(viewport, game.batch);
		
		Label.LabelStyle style = new Label.LabelStyle(Assets.fontSplash, Color.WHITE);
		Table tableTop = new Table();
		tableTop.top();
		tableTop.setFillParent(true);
		
		if(game.testing)
			tableTop.debug();
		
		tableTop.add(new Label("Reem Alharbi", style)).expandX().padTop(10);
		tableTop.add(new Label("Nathaniel Callahan", style)).expandX().padTop(10);
		tableTop.row();
		tableTop.add().expand();
		tableTop.row();
		tableTop.add(new Label("Luke Moss", style)).expandX().padBottom(10);
		tableTop.add(new Label("Scott Strothmann", style)).expandX().padBottom(10);
		
		stage.addActor(tableTop);
		
		stateTime = 0;
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		stateTime += delta;
		
		if(stateTime >= 5f || Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			game.setScreen(game.titleScreen);
			return;
		}
		
		Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.batch.draw(logic_bytes, 0, 0);
		game.batch.end();
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
		logic_bytes.dispose();
	}
}
