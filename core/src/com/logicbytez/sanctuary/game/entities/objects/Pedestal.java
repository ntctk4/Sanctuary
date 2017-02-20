package com.logicbytez.sanctuary.game.entities.objects;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Pedestal extends Entity{
	int sunstoneMeter = 6;
	long timer;

	//creates the pedestal by setting up its data
	public Pedestal(GameScreen game, MapObject object){
		super(game, object);
		impede = true;
		animation = Assets.animate(7, 1, 0, Assets.texture_PedestalStone);
		collisionBox = new Rectangle(box.x + 8, box.y + 8, box.width / 2, box.height / 2);
		sprite = new Sprite(animation.getKeyFrame(sunstoneMeter));
		sprite.setPosition(collisionBox.x, collisionBox.y);
	}

	//updates the sunstone generation meter
	public void update(float delta){
		if(sunstoneMeter < 6){
			sunstoneMeter = Math.min(6, (int)(TimeUtils.timeSinceMillis(timer) / 10000));
			sprite.setRegion(animation.getKeyFrame(sunstoneMeter));
		}
	}

	//removes the sunstone from the pedestal and gives it to the player
	public void dispenseSunstone(){
		if(sunstoneMeter == 6){
			Assets.sound_InsertSunstone.play();
			game.getHud().addSunstone(true);
			sunstoneMeter = 0;
			sprite.setRegion(animation.getKeyFrame(sunstoneMeter));
			timer = TimeUtils.millis();
		}
	}
}