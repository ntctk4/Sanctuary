package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;

public class HeadUpDisplay{
	int crystals = 0, stones = 0;
	SpriteBatch batch;
	Vector2 view;
	
	public HeadUpDisplay(SpriteBatch batch, Vector2 view){
		this.batch = batch;
		this.view = view;
	}
	
	void update(){
		int stoneHeight = Assets.texture_HudSunstone.getRegionHeight();
		int stoneWidth = Assets.texture_HudSunstone.getRegionWidth();
		int crystalHeight = Assets.texture_HudCrystal.getRegionHeight();
		batch.draw(Assets.texture_HudSunstone, -view.x, view.y - stoneHeight);
		batch.draw(Assets.texture_HudCrystal, -view.x, view.y - stoneHeight - crystalHeight);
		Assets.fontHud.draw(batch, String.valueOf(stones), -view.x + stoneWidth / 2 - 5, view.y - stoneHeight / 2 + 2);
		Assets.fontHud.draw(batch, String.valueOf(crystals), -view.x + stoneWidth / 2 - 5, view.y - stoneHeight - crystalHeight / 2 + 6);
		
		//Assets.fontHud.draw(batch, "Wave Incoming!", view.x - 160, view.y - 12);
	}
	
	public void setSunstones(int x) {
		stones = x;
	}
	
	public void setCrystals(int x) {
		crystals = x;
	}
}