package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.labyrinth.Labyrinth;

public class HeadUpDisplay{
	private static final float TIMER = 60f;
	private static final float DELAY = 15f;
	private float stateTime;
	private boolean wave_launched = false;
	private int crystals = 0, stones = 10;
	private Animation<TextureRegion> hourglass, sand;
	private GameScreen game;
	private SpriteBatch batch;
	private TextureRegion current_Hourglass, current_Sand;
	private Vector2 view;
	private HudMessageOverlay messenger;
	private boolean notifiedCrystals = false;
	
	private int stoneHeight;
	private int crystalHeight;
	private int gemWidth;
	private int stoneFontAdjuster;

	//sets up the head-up display's data
	public HeadUpDisplay(SpriteBatch batch, Vector2 view, GameScreen game){
		this.batch = batch;
		this.view = view;
		this.game = game;
		stateTime = 0;
		
		hourglass = Assets.animate(8, 1, 0, Assets.texture_Hourglass);
		hourglass.setFrameDuration(TIMER / ((float) hourglass.getKeyFrames().length));
		sand = Assets.animate(4, 1, 0, Assets.texture_Sand);
		sand.setFrameDuration(0.2f);
		sand.setPlayMode(Animation.PlayMode.LOOP);
		
		messenger = new HudMessageOverlay(view);
		
		crystalHeight = Assets.texture_HudCrystal.getRegionHeight();
		stoneHeight = Assets.texture_HudSunstone.getRegionHeight();
		gemWidth = Assets.texture_HudSunstone.getRegionWidth();
	}
	
	private void updateHourglass(float delta) {
		current_Hourglass = hourglass.getKeyFrame(stateTime);
		current_Sand = sand.getKeyFrame(stateTime);
	}

	//draws the head-up display to the screen
	public void update(float delta){
		stoneFontAdjuster = stones > 9 ? 5 : 0;
		
		batch.draw(Assets.texture_HudSunstone, -view.x, view.y - stoneHeight);
		batch.draw(Assets.texture_HudCrystal, -view.x, view.y - stoneHeight - crystalHeight);
		
		Assets.font25.draw(batch, String.valueOf(stones), 
				-view.x - stoneFontAdjuster + gemWidth / 2 - 5, view.y - stoneHeight / 2 + 2);

		Assets.font25.draw(batch, String.valueOf(crystals) + "/" +
			   String.valueOf(Labyrinth.MAX_CRYSTALS),
			   -view.x + gemWidth / 2 - 17, view.y - stoneHeight - crystalHeight / 2 + 6);
		
		if(!game.isPaused()) {
			stateTime += delta;
			if(stateTime <= TIMER) {
				// hourglass is running
				updateHourglass(delta);
				batch.draw(current_Hourglass, view.x - 38, view.y - 59);
				batch.draw(current_Sand, view.x - 38, view.y - 59);
			}
			else if (!wave_launched){
				// launch wave
				messenger.notifyWave();;
				game.getLabyrinth().activatePortal();
				wave_launched = true;
			}
			
			if(stateTime > TIMER + DELAY) {
				// restart the hourglass
				stateTime = 0;
				wave_launched = false;
			}
		}

		if(crystals == Labyrinth.MAX_CRYSTALS && !notifiedCrystals) {
			messenger.notifyCrystals();
			notifiedCrystals = true;
		}
		if(!game.isPaused()) {
			batch.end();
			messenger.stage.act(delta);
			messenger.stage.draw();
			batch.begin();
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