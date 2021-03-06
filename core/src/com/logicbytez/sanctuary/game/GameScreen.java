package com.logicbytez.sanctuary.game;
import java.util.Comparator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.Main;
import com.logicbytez.sanctuary.game.labyrinth.Labyrinth;
import com.logicbytez.sanctuary.game.entities.Entity;
import com.logicbytez.sanctuary.game.entities.objects.Pedestal_Crystal;
import com.logicbytez.sanctuary.game.entities.Eidolon;
import com.logicbytez.sanctuary.game.entities.players.Player;
import com.logicbytez.sanctuary.game.input.Touchpad;

public class GameScreen implements Screen{
	private boolean leavingGameScreen = false, altarDestroyed = false, paused, stopped, testing, touchScreen;
	private float fadeInTimer, fadeOutTimer, shakeTimer;
	private int shakeMagnitude;
	private Activity activity;
	private Array<Player> players;
	private Array<Entity> entities;
	private HeadUpDisplay hud;
	private Labyrinth labyrinth;
	private TileRenderer tileRenderer;
	private Main game;
	private Music music;
	public OrthographicCamera camera, display;
	private PauseScreen pauseScreen;
	private ShaderProgram shader;
	private ShapeRenderer boxRenderer;
	private SpriteBatch batch;
	private Touchpad touchPads;
	private Vector2 cameraCenter, view;

	//receives objects from the main class
	public GameScreen(Main game){
		this.game = game;
		batch = game.batch;
		this.pauseScreen = new PauseScreen(batch, this);
		players = game.players;
		testing = game.testing;
		touchScreen = game.touchScreen;
		view = game.view;
	}

	@Override
	//initializes the rest of the objects
	public void show(){
		altarDestroyed = false;
		batch.setColor(1, 1, 1, 0);
		leavingGameScreen = false;
		shader = Assets.vignette;
		batch.setShader(shader);
		boxRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(view.x, view.y);
		cameraCenter = new Vector2();
		display = new OrthographicCamera(view.x * 2, view.y * 2);
		hud = new HeadUpDisplay(batch, view, this);
		entities = new Array<Entity>();
		activity = new Activity(entities, this);
		labyrinth = new Labyrinth(entities, this);
		tileRenderer = new TileRenderer(batch, labyrinth.getCurrentRoom().getMap());
		if(touchScreen){
			touchPads = new Touchpad(-view.x, -view.y, display, this, players.first());
			players.first().setTouchPads(touchPads);
		}
		music = Assets.music_Exploration;
		music.play();
	}

	@Override
	//calls draw and updates the camera and entities
	public void render(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		delta = delta > .15 ? .15f : delta;
		updateCamera(delta);
		labyrinth.update(delta);
		entities.sort(new Comparator<Entity>(){
			@Override
			public int compare(Entity t1, Entity t2){
				if(t1.isFlat() || t2.isFlat()){
					return !t1.isFlat() ? 1 : !t2.isFlat() ? -1 : 0;
				}
				float y1 = t1.getSprite().getY(), y2 = t2.getSprite().getY();
				return y1 == y2 ? 0 : y1 < y2 ? 1 : -1;
			}
		});
		draw(delta);
		if(!leavingGameScreen) {
			if(fadeInTimer < 1){
				fadeInTimer += delta;
				if(fadeInTimer > 1){
					fadeInTimer = 1;
				}
				batch.setColor(fadeInTimer - fadeOutTimer, fadeInTimer - fadeOutTimer, fadeInTimer - fadeOutTimer, 1);
			}
			if(fadeOutTimer > 0){
				if(fadeOutTimer > 2){
					fadeOutTimer = 2;
				}
				batch.setColor(1-fadeOutTimer/2, 1-fadeOutTimer/2, 1-fadeOutTimer/2, 1);
			}
			for(Entity entity : entities){
				entity.update(delta);
			}
			if(players.size > 1){
				if(players.first().getHealth() > 0 && players.get(1).getHealth() > 0){
					for(Player player : players){
						player.contain(camera, view);
					}
				}
			}
		}
	}

	//updates the camera and its center
	public void updateCamera(float delta){
		if(players.size > 1 && players.get(1).getHealth() > 0){
			if(players.first().getHealth() > 0){
				cameraCenter.set(players.first().getCenter().add(players.get(1).getCenter()).scl(.5f));
			}else{
				cameraCenter.set(players.get(1).getCenter());
			}
		}else if(players.first().getHealth() > 0){
			cameraCenter.set(players.first().getCenter());
		}
		if(shakeTimer > 0){
			float magnitude = Math.min(shakeTimer -= delta, 1) * shakeMagnitude;
			camera.position.x = cameraCenter.x + magnitude * MathUtils.randomTriangular();
			camera.position.y = cameraCenter.y + magnitude * MathUtils.randomTriangular();
		}else{
			camera.position.set(cameraCenter, 0);
		}
		camera.update();
	}

	//draws everything visible to the screen
	private void draw(float delta){
		tileRenderer.setView(camera);
		batch.begin();
		tileRenderer.render(Labyrinth.backgroundLayers);
		for(Entity entity : entities){
			Sprite sprite = entity.getSprite();
			if(sprite != null && culling(sprite.getBoundingRectangle())){
				entity.setColor(batch.getColor());
				sprite.draw(batch);
				if(entity.getClass() == Player.class){
					((Player)entity).getHealthIndicator().getSprite().draw(batch);
				}else if(entity.getClass() == Pedestal_Crystal.class){
					Sprite light = ((Pedestal_Crystal)entity).getLight();
					if(light != null){
						light.draw(batch);
					}
				}
			}
		}
		if(labyrinth.getCurrentRoom().hasForeground()){
			batch.setColor(.75f, .75f, .75f, alphaRatio());
			tileRenderer.render(Labyrinth.foregroundLayer);
			batch.setColor(1, 1, 1, 1);
		}
		batch.setProjectionMatrix(display.combined);
		hud.update(delta);
		if(paused){
			pauseScreen.update();
		}else if(players.first().getHealth() <= 0){
			if(players.size < 2 || (players.size > 1 && players.get(1).getHealth() <= 0)){
				endGame(false, delta);
			}
		}else if(this.getLabyrinth().getPortal().getDestroyedObelisk() == 4){
			endGame(true, delta);
		}else if(altarDestroyed){
			endGame(false, delta);
		}else if(touchScreen){
			touchPads.getSprite(true).draw(batch);
			touchPads.getSprite(false).draw(batch);
		}
		if(testing){
			testing();
		}else{
			batch.end();
		}
	}

	//returns the transparency percentage for the foreground layer
	public float alphaRatio(){
		float ratio = players.first().getSprite().getY();
		if(players.size > 1){
			ratio = Math.min(ratio, players.get(1).getSprite().getY());
		}
		ratio = (ratio - 16) / 32;
		return ratio > 1 ? 1 : ratio > .5 ? ratio : .5f;
	}

	//displays testing information and draws the invisible boundaries
	private void testing(){
		float m1 = java.lang.Runtime.getRuntime().totalMemory(), m2 = java.lang.Runtime.getRuntime().maxMemory();
		Assets.font25.draw(batch, Integer.toString(batch.renderCalls), 5 - view.x, -view.y + 75);
		Assets.font25.draw(batch, Integer.toString(Gdx.graphics.getFramesPerSecond()), 5 - view.x, -view.y + 50);
		Assets.font25.draw(batch, Float.toString(Math.round(m1 / m2 * 10000) / 100f) + "%", 5 - view.x, -view.y + 25);
		batch.end();
		if(boxRenderer == null) return;
		boxRenderer.setProjectionMatrix(camera.combined);
		boxRenderer.begin(ShapeType.Line);
		boxRenderer.setColor(0, 0, 1, 1);
		for(int y = (int)((camera.position.y - view.y / 2) / 16); y < (camera.position.y + view.y / 2) / 16; y++){
			for(int x = (int)((camera.position.x - view.x / 2) / 16); x < (camera.position.x + view.x / 2) / 16; x++){
				if(activity.getTileImpede(x, y) && x >= 0 && x < labyrinth.getRoomSize().x && y >= 0 && y < labyrinth.getRoomSize().y){
					boxRenderer.rect(x * 16, y * 16, 16, 16);
				}
			}
		}
		for(Entity entity : entities){
			if(culling(entity.getBox())){
				if(entity.getClass() == Eidolon.class){
					boxRenderer.setColor(1, 0, 0, 1);
					drawBox(entity.getBox());
					boxRenderer.setColor(.5f, 0, 0, 1);
				}else if(entity.getClass() == Player.class){
					boxRenderer.setColor(0, 1, 0, 1);
					drawBox(entity.getBox());
					boxRenderer.setColor(0, .5f, 0, 1);
				}else{
					if(entity.getBox() != entity.getCollisionBox()){
						boxRenderer.setColor(.5f, .5f, 0, 1);
						drawBox(entity.getBox());
					}
					boxRenderer.setColor(1, 1, 0, 1);
				}
				drawBox(entity.getCollisionBox());
			}
		}
		boxRenderer.end();
	}

	//checks if a rectangle is visible by the camera
	private boolean culling(Rectangle box){
		if(box.x + box.width > camera.position.x - view.x / 2 && box.x < camera.position.x + view.x / 2){
			if(box.y + box.height > camera.position.y - view.y / 2 && box.y < camera.position.y + view.y / 2){
				return true;
			}
		}
		return false;
	}

	//draws a rectangle for testing purposes
	private void drawBox(Rectangle box){
		boxRenderer.rect(box.x, box.y, box.width, box.height);
	}

	@Override
	//resizes the vignette to the screen
	public void resize(int width, int height){
		shader.begin();
		shader.setUniformf("u_resolution", width, height);
		shader.end();
	}

	@Override
	//activates the menu
	public void pause(){
		switchMenu();
	}

	@Override
	public void resume(){
	}

	@Override
	//resets data and calls dispose
	public void hide(){
		fadeInTimer = 0;
		dispose();
		paused = stopped = false;
		music.stop();
		players.clear();
	}

	@Override
	//frees memory that was stored
	public void dispose(){
		if(boxRenderer != null){
			boxRenderer.dispose();
			boxRenderer = null;
		}
	}
	
	private void endGame(boolean isWon, float delta) {
		GlyphLayout layout;
		String displayString;
		fadeOutTimer += delta/2;
		
		if(isWon) {
			displayString = "congratulations, you've defeated\n    the eidolon once and for all";
		} else {
			if(players.first().getHealth() <= 0)
				displayString = "You Died";
			else
				displayString = "Sanctuary Destroyed";
		}
		
		layout = new GlyphLayout(Assets.font25, displayString);
		Assets.font50.setColor(1, 1, 1, 1 - fadeOutTimer/2);
		Assets.font25.setColor(1, 1, 1, 1 - fadeOutTimer/2); // fade out the HUD font as well
		Assets.font25.draw(batch, displayString, -layout.width/2, layout.height/2);
		stopped = true;
		touchScreen = false;
		
		if(fadeOutTimer >= 2) {
			exit();
		}
	}

	public void exit(){
		leavingGameScreen = true;
		// exit function needs to do a lot more... i.e. destroy everything and start over.
		Assets.font50.setColor(1, 1, 1, 1);
		Assets.font25.setColor(1, 1, 1, 1);
		fadeInTimer = 0;
		fadeOutTimer = 0;
		batch.setColor(1, 1, 1, 1);
		batch.setShader(null);
		game.setScreen(game.splashScreen);
	}

	//returns the activities for beings
	public Activity getActivity(){
		return activity;
	}

	//returns the current labyrinth
	public Labyrinth getLabyrinth(){
		return labyrinth;
	}

	//returns the array of entities
	public Array<Entity> getEntities(){
		return entities;
	}
	
	//returns the head-up display
	public HeadUpDisplay getHud(){
		return hud;
	}

	//returns the array of players
	public Array<Player> getPlayers(){
		return players;
	}
	
	public PauseScreen getPauseScreen() {
		return pauseScreen;
	}

	//returns the pause menu boolean
	public boolean isPaused(){
		return paused;
	}

	//returns the being stopping boolean
	public boolean isStopped(){
		return stopped;
	}
	
	public boolean isTouchScreen() {
		return touchScreen;
	}

	//sets the map of the tile renderer
	public void setTileRendererMap(TiledMap map){
		tileRenderer.setMap(map);
	}

	//makes the camera shake
	public void shakeScreen(float duration, int magnitude){
		Gdx.input.vibrate(magnitude);
		shakeMagnitude = magnitude;
		shakeTimer = duration;
	}

	//turns the menu on and off
	public void switchMenu(){
		paused = !paused;
		stopped = !stopped;
		labyrinth.modifyPedestalTimers();
	}

	//flips the stopped boolean
	public void switchStopped(){
		stopped = !stopped;
	}
	
	//sets altarDestroyed to true to signal game over
	public void setAltarDestroyed(){
		altarDestroyed = true;
	}
}