package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;

public class HeadUpDisplay{
	int crystals = 0, stones = 10;
	SpriteBatch batch;
	Vector2 view;
	
	//sets up the head-up display's data
	public HeadUpDisplay(SpriteBatch batch, Vector2 view){
		this.batch = batch;
		this.view = view;
	}
	
	//draws the head-up display to the screen
	void update(){
		int crystalHeight = Assets.texture_HudCrystal.getRegionHeight(), crystalFontAdjuster = crystals > 9 ? 5 : 0;
		int stoneHeight = Assets.texture_HudSunstone.getRegionHeight(), stoneFontAdjuster = stones > 9 ? 5 : 0;
		int itemWidth = Assets.texture_HudSunstone.getRegionWidth();
		batch.draw(Assets.texture_HudSunstone, -view.x, view.y - stoneHeight);
		batch.draw(Assets.texture_HudCrystal, -view.x, view.y - stoneHeight - crystalHeight);
		Assets.fontHud.draw(batch, String.valueOf(stones), -view.x - stoneFontAdjuster + itemWidth / 2 - 5, view.y - stoneHeight / 2 + 2);
		Assets.fontHud.draw(batch, String.valueOf(crystals), -view.x - crystalFontAdjuster + itemWidth / 2 - 5, view.y - stoneHeight - crystalHeight / 2 + 6);
		//Assets.fontHud.draw(batch, "Wave Incoming!", view.x - 160, view.y - 12);
	}
	
	//gives one light crystal to the player
	public void addCrystal(){
		crystals++;
	}
	
	//gives one sunstone to the player
	public void addSunstone(){
		stones++;
	}
	
	//takes one light crystal from the player
	public void subCrystal(){
		crystals--;
	}
	
	//takes one sunstone from the player
	public void subSunstone(){
		stones--;
	}
	
	//returns amount of player light crystals
	public int getCrystals(){
		return crystals;
	}
	
	//returns amount of player sunstones
	public int getSunstones(){
		return stones;
	}
}