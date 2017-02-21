package com.logicbytez.sanctuary.game.entities.objects;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Pedestal_Stone extends Entity{
	boolean paused;
	int stoneMeter = 6;
	long timer;

	//creates the pedestal by setting up its data
	public Pedestal_Stone(GameScreen game, MapObject object){
		super(game, object);
		animation = Assets.animate(7, 1, 0, Assets.texture_PedestalStone);
		impede = true;
		collisionBox = new Rectangle(box.x + 8, box.y + 8, box.width / 2, box.height / 2);
		sprite = new Sprite(animation.getKeyFrame(stoneMeter));
		sprite.setPosition(collisionBox.x, collisionBox.y);
	}

	//updates the sunstone generation meter
	public void update(float delta){
		if(stoneMeter < 6){
			if(!paused){
				if(!game.isPaused()){
					int rate = 11 - game.getLabyrinth().getAltar().getStonesInserted();
					stoneMeter = Math.min(6, (int)(TimeUtils.timeSinceMillis(timer) / rate / 10000)); //ranges from 11~1min
					sprite.setRegion(animation.getKeyFrame(stoneMeter));
				}else{
					timer = -TimeUtils.timeSinceMillis(timer);
					paused = true;
				}
			}else if(!game.isPaused()){
				timer += TimeUtils.millis();
				paused = false;
			}
		}
	}

	//removes the sunstone from the pedestal and gives it to the player
	public void dispenseSunstone(){
		if(stoneMeter == 6){
			Assets.sound_Sunstone.play();
			game.getHud().addStone(true);
			stoneMeter = 0;
			sprite.setRegion(animation.getKeyFrame(stoneMeter));
			timer = TimeUtils.millis();
		}
	}
}