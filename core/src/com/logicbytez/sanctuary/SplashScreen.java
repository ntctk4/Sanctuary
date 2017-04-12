package com.logicbytez.sanctuary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen implements Screen {
	private Main game;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;
		
	private float stateTime;
	private boolean timeToTransition, transitionStarted;
	
	public SplashScreen(Main game) {
		this.game = game;
				
		Texture tex = new Texture(Gdx.files.internal("LogicBytes.png"));
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(game.view.x * 2, game.view.y * 2, camera);
		stage = new Stage(viewport, game.batch);
		
		Label.LabelStyle style = new Label.LabelStyle(Assets.fontSplash, Color.WHITE);
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		if(game.testing)
			table.debug();
		table.background(new TextureRegionDrawable(new TextureRegion(tex)));
		
		table.add(new Label("Reem Alharbi", style)).expandX().align(Align.left).pad(10, 10, 0, 0);
		table.add(new Label("Nathaniel Callahan", style)).expandX().align(Align.right).pad(10, 0, 0, 10);
		table.row();
		table.add().expand();
		table.row();
		table.add(new Label("Luke Moss", style)).expandX().align(Align.left).pad(0, 10, 10, 0);
		table.add(new Label("Scott Strothmann", style)).expandX().align(Align.right).pad(0, 0, 10, 10);
		
		stage.addActor(table);
		
		stateTime = 0;
		timeToTransition = transitionStarted = false;
	}
	
	@Override
	public void show() {
		stage.getRoot().setColor(1, 1, 1, 0);
		stage.addAction(Actions.fadeIn(0.5f));
	}

	@Override
	public void render(float delta) {
		stateTime += delta;
		
		if(stateTime >= 5f || Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			timeToTransition = true;
		}
		
		if(timeToTransition && !transitionStarted) {
			transitionStarted = true;
			stage.addAction(Actions.sequence(Actions.fadeOut(2f), Actions.run(new Runnable() {
				@Override
				public void run() {
					game.setScreen(game.titleScreen);
				}
			})));
			return;
		}
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
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
	}
}
