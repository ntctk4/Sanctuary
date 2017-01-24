package com.logicbytez.sanctuary.game.entities.players;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.logicbytez.sanctuary.Assets;

public class HealthIndicator{
	private float timer;
	private Player player;
	private Sprite sprite;

	public HealthIndicator(Player player){
		this.player = player;
		sprite = new Sprite(Assets.texture_HealthIndicator);
		sprite.setAlpha(0);
	}

	//sets the color of the health indicator and its timer
	void set(int health, int maxHealth){
		if(health == maxHealth){
			sprite.setColor(0, .5f, 0, 1);
		}else if(health > 0){
			float ratio1 = (float)health / maxHealth / 2, ratio2 = 1 - ratio1 / 2 - .5f;
			sprite.setColor(ratio2, ratio1, 0, 1);
		}else{
			sprite.setColor(0, 0, 0, 1);
		}
		timer = 2;
	}

	//controls the width and position of the health indicator
	void update(float delta){
		if(timer > 0){
			timer -= delta;
			if(timer < 0){
				timer = 0;
			}
			float y = player.getSprite().getY() + player.getSprite().getHeight() + sprite.getHeight();
			sprite.setPosition(player.getCenter().x - sprite.getWidth() / 2, y);
			sprite.setSize(Math.min(timer, 1) * sprite.getRegionWidth(), sprite.getRegionHeight());
		}
	}

	//returns the sprite of the health indicator
	public Sprite getSprite(){
		return sprite;
	}
}