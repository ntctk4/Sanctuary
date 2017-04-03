package com.logicbytez.sanctuary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen implements Screen {
	private Main game;
	
	private TextureRegion logo;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;
		
	private float stateTime;
	
	public SplashScreen(Main game) {
		this.game = game;
				
		//load logo
		camera = new OrthographicCamera();
		viewport = new FitViewport(game.view.x, game.view.y, camera);
		stage = new Stage(viewport, game.batch);
		
		Label.LabelStyle style = new Label.LabelStyle(Assets.fontMonologue, Color.WHITE);
		Table table = new Table();
		table.bottom();
		table.setFillParent(true);
		
		if(!game.testing)
			table.debug();
		
		table.add(new Label("Reem Alharbi", style)).expandX();
		table.add(new Label("Nathaniel Callahan", style)).expandX();
		table.row();
		table.add(new Label("Luke Moss", style)).expandX().padBottom(10);
		table.add(new Label("Scott Strothmann", style)).expandX().padBottom(10);
		
		stage.addActor(table);
		
		stateTime = 0;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		stateTime += delta;
		
		if(stateTime >= 5f) {
			game.setScreen(game.titleScreen);
			stage.dispose();
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		game.batch.begin();
		//game.batch.draw(logo, 0, 0);
		game.batch.end();
		
		
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
