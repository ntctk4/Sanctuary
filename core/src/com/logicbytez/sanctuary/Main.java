package com.logicbytez.sanctuary;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.MonologueScreen;
import com.logicbytez.sanctuary.game.entities.players.Player;

public class Main extends Game{
	public boolean testing;
	public boolean touchScreen;
	public Array<Player> players;
	public GameScreen gameScreen;
	public SpriteBatch batch;
	public TitleScreen titleScreen;
	public MonologueScreen monologueScreen;
	public SplashScreen splashScreen;
	public Vector2 view;

	//retrieves the testing boolean
	public Main(boolean testing, boolean touchScreen){
		this.testing = testing;
		this.touchScreen = touchScreen;
	}

	@Override
	//loads all of the assets and creates the screens
	public void create(){
		Assets.load();
		batch = new SpriteBatch();
		players = new Array<Player>(2);
		view = new Vector2(240, 135);
		gameScreen = new GameScreen(this);
		titleScreen = new TitleScreen(this);
		monologueScreen = new MonologueScreen(this);
		splashScreen = new SplashScreen(this);
		setScreen(splashScreen);
	}

	@Override
	//frees memory that was stored
	public void dispose(){
		Assets.dispose();
		batch.dispose();
		gameScreen.dispose();
		titleScreen.dispose();
	}
}