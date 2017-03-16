package com.logicbytez.sanctuary.game.entities.objects.antechamber;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Obelisk extends Entity{
	
	int hitsTaken;
	MapObject object;
	
	public Obelisk(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		hitsTaken = 0;
		animation = Assets.animate(4, 1, 0, Assets.texture_Obelisk);
		impede = true;
		collisionBox = new Rectangle(box.x, box.y, box.width - 2, box.height);
		collisionBox.fitInside(box);
		sprite = new Sprite(animation.getKeyFrame(hitsTaken));
		sprite.setPosition(box.x, box.y);
	}
	
	public void damageTaken(){
		hitsTaken++;
		if(hitsTaken % 4 == 0){
			//Assets.sound_Sunstone.play();
			object.getProperties().put("Type", ++hitsTaken);
			sprite.setRegion(animation.getKeyFrame(hitsTaken/4));
		}
	}
}