package com.logicbytez.sanctuary.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.Main;
import com.logicbytez.sanctuary.game.input.Gamepad;

public class MonologueScreen implements Screen {

	private boolean shown, touched;
	private float alphaTimer;
	private Main game;
	private OrthographicCamera display;
	private SpriteBatch batch;
	
	public MonologueScreen(Main game) {
		shown = touched = false;
		this.game = game;
		batch = game.batch;
		display = new OrthographicCamera(game.view.x * 2, game.view.y * 2);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		Assets.fontMonologue.setColor(1, 1, 1, 0);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(display.combined);
		
		String monologue = 	"Welcome, Hero, to the ancient Labyrinth. You are the last\n" + 
							"of the guardians of the Sanctuary. Your brethren have all\n" +
							"perished, so now you alone must defend the Sanctuary\n" +
							"against an army of shadowy monsters, known as Eidolon, and\n" +
							"find a way to defeat the encroaching evil once and for all.";
		
		String message = "Continue";
		
		GlyphLayout layout = new GlyphLayout(Assets.fontMonologue, monologue);
		GlyphLayout msglayout = new GlyphLayout(Assets.fontMonologue, message);
		
		batch.begin();
		Assets.fontMonologue.draw(batch, layout, -layout.width/2, layout.height);
		Assets.fontMonologue.draw(batch, msglayout, -msglayout.width/2, -layout.height/2);
		batch.end();
		
		if(!shown){
			alphaTimer += delta;
			Assets.fontMonologue.setColor(1, 1, 1, alphaTimer - 1);
			if(alphaTimer > 2){
				alphaTimer = 0;
				shown = true;
			}
		}else if(Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.justTouched() || touched || 
				(game.players.first().getGamePad() != null && game.players.first().getGamePad().getController().getButton(Gamepad.A)) ){
			alphaTimer += delta;
			Assets.fontMonologue.setColor(1, 1, 1, 1 - alphaTimer);
			touched = true;
			if(alphaTimer > 2){
				game.setScreen(game.gameScreen);
			}
		}
		
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
		// TODO Auto-generated method stub
		
	}

}
