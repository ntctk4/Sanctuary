package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;

public class HeadUpDisplay{
	private int crystals = 0, stones = 10;
	private float time, timer = 60;
	private Animation<TextureRegion> hourglass, sand;
	private SpriteBatch batch;
	private TextureRegion current_Hourglass, current_Sand;
	private TextureRegion[] hourglass_frames, sand_frames;
	private Vector2 view;
	GameScreen game;

	//sets up the head-up display's data
	public HeadUpDisplay(SpriteBatch batch, Vector2 view, GameScreen game){
		this.batch = batch;
		this.view = view;
		this.game = game;
		TextureRegion[][] hourglassArray = Assets.texture_Hourglass.split(38, 59);
		hourglass_frames = new TextureRegion[8];
		for(int i = 0; i < 8; i++){
			hourglass_frames[i] = hourglassArray[0][i];
		}
		TextureRegion[][] sandArray = Assets.texture_Sand.split(38, 59);
		sand_frames = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			sand_frames[i] = sandArray[0][i];
		}
		hourglass = new Animation<TextureRegion>(timer / 8, hourglass_frames);
		sand = new Animation<TextureRegion>(.2f, sand_frames);
	}

	//draws the head-up display to the screen
	void update(){
		int crystalHeight = Assets.texture_HudCrystal.getRegionHeight(), crystalFontAdjuster = crystals > 9 ? 5 : 0;
		int stoneHeight = Assets.texture_HudSunstone.getRegionHeight(), stoneFontAdjuster = stones > 9 ? 5 : 0;
		int itemWidth = Assets.texture_HudSunstone.getRegionWidth();
		batch.draw(Assets.texture_HudSunstone, -view.x, view.y - stoneHeight);
		batch.draw(Assets.texture_HudCrystal, -view.x, view.y - stoneHeight - crystalHeight);
		if(timer > 0){
			timer -= Gdx.graphics.getDeltaTime();
			time += Gdx.graphics.getDeltaTime();
			current_Hourglass = hourglass.getKeyFrame(time, true);
			current_Sand = sand.getKeyFrame(time, true);
			batch.draw(current_Hourglass, view.x - 38, view.y - 59);
			batch.draw(current_Sand, view.x - 38, view.y - 59);
		}else{
			//initiate wave phase here
			Assets.fontHud.draw(batch, "Wave Incoming!", view.x - 160, view.y - 12);
		}
		Assets.fontHud.draw(batch, String.valueOf(stones), -view.x - stoneFontAdjuster + itemWidth / 2 - 5, view.y - stoneHeight / 2 + 2);
		Assets.fontHud.draw(batch, String.valueOf(crystals), -view.x - crystalFontAdjuster + itemWidth / 2 - 5, view.y - stoneHeight - crystalHeight / 2 + 6);
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

	//resets time stuff for hourglass animation
	public void startTimer(){
		time = 0;
		timer = 60;
	}
}