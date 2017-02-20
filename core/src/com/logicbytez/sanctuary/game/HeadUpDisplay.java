package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;

public class HeadUpDisplay{
	int crystals = 0, stones = 10;
	SpriteBatch batch;
	Vector2 view;
	
	private TextureRegion[] hourglass_frames, sand_frames;
	private TextureRegion current_Hourglass, current_Sand;
	private Animation<TextureRegion> hourglass, sand;
	
	float timer = 60;
	float time = 0F;
	
	//sets up the head-up display's data
	public HeadUpDisplay(SpriteBatch batch, Vector2 view){
		this.batch = batch;
		this.view = view;
		
		TextureRegion[][] tempH = Assets.texture_Hourglass.split(38, 59);
		hourglass_frames = new TextureRegion[8];
		for(int i = 0; i < 8; i++) {
			hourglass_frames[i] = tempH[0][i];
		}
		
		TextureRegion[][] tempS = Assets.texture_Sand.split(38, 59);
		sand_frames = new TextureRegion[4];
		for(int i = 0; i < 4; i++) {
			sand_frames[i] = tempS[0][i];
		}
		
		hourglass = new Animation<TextureRegion>(timer/8, hourglass_frames);
		sand = new Animation<TextureRegion>(.2F, sand_frames);
	}
	
	//draws the head-up display to the screen
	void update(){
		int crystalHeight = Assets.texture_HudCrystal.getRegionHeight(), crystalFontAdjuster = crystals > 9 ? 5 : 0;
		int stoneHeight = Assets.texture_HudSunstone.getRegionHeight(), stoneFontAdjuster = stones > 9 ? 5 : 0;
		int itemWidth = Assets.texture_HudSunstone.getRegionWidth();
		batch.draw(Assets.texture_HudSunstone, -view.x, view.y - stoneHeight);
		batch.draw(Assets.texture_HudCrystal, -view.x, view.y - stoneHeight - crystalHeight);
		
		if(timer > 0) {
			timer -= Gdx.graphics.getDeltaTime();
			time += Gdx.graphics.getDeltaTime();
			current_Hourglass = hourglass.getKeyFrame(time, true);
			current_Sand = sand.getKeyFrame(time, true);
			batch.draw(current_Hourglass, view.x - 38, view.y - 59);
			batch.draw(current_Sand, view.x - 38, view.y - 59);
		} else {
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
	public void addSunstone(boolean add){
		stones += add ? 1 : -1;
	}
	
	//returns amount of player light crystals
	public int getCrystals(){
		return crystals;
	}
	
	//returns amount of player sunstones
	public int getSunstones(){
		return stones;
	}
	
	//resets time stuff for hourglass animation
	public void startTimer() {
		timer = 60;
		time = 0;
	}
}