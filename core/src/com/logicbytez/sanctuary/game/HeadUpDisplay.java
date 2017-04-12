package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.labyrinth.Labyrinth;

public class HeadUpDisplay{
	private boolean paused = false, wave_launched = false;
	private float sand_timer = 0;
	private int crystals = 0, hourglass_state = 0, stones = 10, wave_time = 56;
	private long hourglass_timer;
	private Animation<TextureRegion> hourglass, sand;
	private GameScreen game;
	private SpriteBatch batch;
	private TextureRegion current_Hourglass, current_Sand;
	private Vector2 view;
	private HudMessageOverlay msgOverlay;
	private boolean notifiedCrystals;

	//sets up the head-up display's data
	public HeadUpDisplay(SpriteBatch batch, Vector2 view, GameScreen game){
		this.batch = batch;
		this.view = view;
		this.game = game;
		hourglass_timer = TimeUtils.millis();
		notifiedCrystals = false;
		TextureRegion[][] sandArray = Assets.texture_Sand.split(38, 59);
		TextureRegion[] sand_frames = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			sand_frames[i] = sandArray[0][i];
		}
		hourglass = Assets.animate(8, 1, 0, Assets.texture_Hourglass);
		sand = new Animation<TextureRegion>(.2f, sand_frames);
		msgOverlay = new HudMessageOverlay(view, batch);
	}

	//draws the head-up display to the screen
	void update(float delta){
		int crystalHeight = Assets.texture_HudCrystal.getRegionHeight(), crystalFontAdjuster = crystals > 9 ? 5 : 0;
		int stoneHeight = Assets.texture_HudSunstone.getRegionHeight(), stoneFontAdjuster = stones > 9 ? 5 : 0;
		int itemWidth = Assets.texture_HudSunstone.getRegionWidth();
		batch.draw(Assets.texture_HudSunstone, -view.x, view.y - stoneHeight);
		batch.draw(Assets.texture_HudCrystal, -view.x, view.y - stoneHeight - crystalHeight);
		if(!paused){
			if(!game.isPaused()){
				//sand animation is based on delta time
				if(!game.isStopped()){
					sand_timer += delta;
				}
				current_Sand = sand.getKeyFrame(sand_timer, true);
				//hourglass_state is based on the wave_time and when the the timer started
				//seconds since timer started
				int seconds = (int)(TimeUtils.timeSinceMillis(hourglass_timer) / 1000);
				if(seconds < wave_time){
					//still displaying hourglass find its state (there are 8 state frames)
					hourglass_state = seconds / (wave_time / 8);
				}else if(seconds < wave_time + 15){
					//display the wave message for 15s
					if(!wave_launched){
						msgOverlay.notifyWave();;
						//launch wave here!!
						//game.getLabyrinth().activatePortal();
						wave_launched = true;
					}
					hourglass_state = 10;
				}else{
					//start the hourglass again
					hourglass_state = 0;
					hourglass_timer = TimeUtils.millis();
					wave_launched = false;
				}
				current_Hourglass = hourglass.getKeyFrame(hourglass_state);
			}else{
				//the game has been paused but HUD just found out
				//set the timer to the negative of the amount of time that has already passed
				hourglass_timer = -TimeUtils.timeSinceMillis(hourglass_timer);
				paused = true;
			}
		}else if(!game.isPaused()){
			//the game was paused, but now is not
			//add the current time to the negative time already passed
			//essentially cuts out the amount of time the game was paused
			hourglass_timer += TimeUtils.millis();
			paused = false;
		}
		switch(hourglass_state){
		case 10: // was the wave message flag... refactored out
			break;
		default:
			batch.draw(current_Hourglass, view.x - 38, view.y - 59);
			batch.draw(current_Sand, view.x - 38, view.y - 59);
			break;
		}
		Assets.fontHud.draw(batch, String.valueOf(stones), -view.x - stoneFontAdjuster + itemWidth / 2 - 5, view.y - stoneHeight / 2 + 2);
		Assets.fontHud.draw(batch, String.valueOf(crystals) + "/" + String.valueOf(Labyrinth.MAX_CRYSTALS), -view.x - crystalFontAdjuster + itemWidth / 2 - 16, view.y - stoneHeight - crystalHeight / 2 + 6);

		if(crystals == Labyrinth.MAX_CRYSTALS && !notifiedCrystals) {
			msgOverlay.notifyCrystals();
			notifiedCrystals = true;
		}
		if(!game.isPaused()) {
			Matrix4 old = batch.getProjectionMatrix();
			batch.setProjectionMatrix(msgOverlay.stage.getCamera().combined);
			msgOverlay.stage.act(delta);
			msgOverlay.stage.draw();
			batch.setProjectionMatrix(old);
		}
	}

	//gives or takes one light crystal
	public void addCrystal(boolean add){
		crystals += add ? 1 : -1;
	}

	//gives or takes one sunstone
	public void addStone(boolean add){
		stones += add ? 1 : -1;
	}

	//returns amount of player light crystals
	public int getCrystals(){
		return crystals;
	}

	//returns amount of player sunstones
	public int getStones(){
		return stones;
	}
}