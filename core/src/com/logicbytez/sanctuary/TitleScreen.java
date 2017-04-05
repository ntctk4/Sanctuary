package com.logicbytez.sanctuary;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.game.entities.players.Player;
import com.logicbytez.sanctuary.game.input.Gamepad;

public class TitleScreen implements Screen{
	private boolean shown, touched;
	private float alphaTimer;
	private Array<Player> players;
	private Array<Controller> controllers;
	private Main game;
	private OrthographicCamera display;
	private SpriteBatch batch;
	

	public TitleScreen(Main game){
		this.game = game;
		batch = game.batch;
		display = new OrthographicCamera(game.view.x * 2, game.view.y * 2);
		players = game.players;
	}

	@Override
	public void show(){
		Assets.font50.setColor(1, 1, 1, 0);
		Gdx.app.setLogLevel(Application.LOG_ERROR);
		controllers = Controllers.getControllers();
		//testing stuff below here
		boolean alone = true;
		if(controllers.size < 1){
			alone = false;
		}
		players.add(new Player(alone, true, game.gameScreen));
		if(controllers.size > 0){
			players.add(new Player(alone, false, game.gameScreen));
			players.get(1).setGamePad(new Gamepad(controllers.first(), players.get(1)));
		}
	}
	
	private void handleInput() {
		if(shown && (Gdx.input.isKeyJustPressed(Keys.SPACE) || 
					Gdx.input.isKeyJustPressed(Keys.ENTER) || 
					Gdx.input.justTouched() || 
					(controllers.size > 0 && controllers.first().getButton(Gamepad.A))))
			touched = true;
	}

	@Override
	public void render(float delta){
		handleInput();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(display.combined);
		batch.begin();
		Assets.font50.draw(batch, "sanctuary", -100, 50);
		batch.end();
		//game.setScreen(game.gameScreen); //skips title
		if(!shown){
			alphaTimer += delta;
			Assets.font50.setColor(1, 1, 1, alphaTimer - 1);
			if(alphaTimer > 2){
				alphaTimer = 0;
				shown = true;
			}
		}else if(touched){
			alphaTimer += delta;
			Assets.font50.setColor(1, 1, 1, 1 - alphaTimer);
			if(alphaTimer > 2){
				game.setScreen(game.monologueScreen);
			}
		}
		//Assets.font50.setColor(1, 1, 1, 1); //resets font
	}

	@Override
	public void resize(int width, int height){
	}

	@Override
	public void pause(){
	}

	@Override
	public void resume(){
	}

	@Override
	public void hide(){
		Assets.font50.setColor(1, 1, 1, 1);
		alphaTimer = 0;
		shown = touched = false;
	}

	@Override
	//frees memory that was stored
	public void dispose(){
	}
}