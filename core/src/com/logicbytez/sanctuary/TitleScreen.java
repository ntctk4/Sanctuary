package com.logicbytez.sanctuary;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.logicbytez.sanctuary.game.entities.players.Player;
import com.logicbytez.sanctuary.game.input.Gamepad;

public class TitleScreen implements Screen{
	private boolean shown;
	private Array<Player> players;
	private Array<Controller> controllers;
	private Main game;
	
	private Viewport viewport;
	private Stage stage;
	
	public TitleScreen(Main game){
		this.game = game;
		players = game.players;
		
		viewport = new FitViewport(game.view.x * 2, game.view.y * 2, new OrthographicCamera());
		stage = new Stage(viewport, game.batch);
		
		Table table = new Table();
		table.center();
		table.setFillParent(true);
		stage.addActor(table);
		
		Label.LabelStyle style = new Label.LabelStyle(Assets.font50, Color.WHITE);
		Label.LabelStyle style2 = new Label.LabelStyle(Assets.font25, Color.WHITE);
		
		Label sanctuary = new Label("sanctuary", style);
		Label start = new Label("start game", style2);		
		
		table.add(sanctuary).expandX().padBottom(20);
		table.row();
		table.add(start);
		
		if(game.testing)
			table.debug();
	}

	@Override
	public void show(){
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
		System.out.println("show");
		stage.getRoot().setColor(1, 1, 1, 0);
		stage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable() {
			@Override
			public void run() {
				shown = true;
			}
		})));
	}
	
	private void handleInput() {
		if(shown && (Gdx.input.isKeyJustPressed(Keys.SPACE) || 
					Gdx.input.isKeyJustPressed(Keys.ENTER) || 
					Gdx.input.justTouched() || 
					(controllers.size > 0 && controllers.first().getButton(Gamepad.A))))
			stage.addAction(Actions.sequence(Actions.fadeOut(2f), Actions.run(new Runnable() {
				@Override
				public void run() {
					game.setScreen(game.monologueScreen);
				}
			})));
	}

	@Override
	public void render(float delta){
		handleInput();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
		//uncomment to skip title/monologue
//		game.setScreen(game.gameScreen);
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
	}

	@Override
	//frees memory that was stored
	public void dispose(){
		stage.dispose();
	}
}