package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;

public class Display{
	int crystals, stones;
	SpriteBatch batch;
	Vector2 view;
	
	public Display(SpriteBatch batch, Vector2 view){
		this.batch = batch;
		this.view = view;
	}
	
	void update(){
		int stoneHeight = Assets.texture_DisplaySunstone.getRegionHeight();
		int stoneWidth = Assets.texture_DisplaySunstone.getRegionWidth();
		batch.draw(Assets.texture_DisplaySunstone, -view.x, view.y - stoneHeight);
		Assets.fontHud.draw(batch, String.valueOf(stones), -view.x + stoneWidth / 2 - 5, view.y - 12);
	}
}