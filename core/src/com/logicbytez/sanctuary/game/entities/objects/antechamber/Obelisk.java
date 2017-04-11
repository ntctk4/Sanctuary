package com.logicbytez.sanctuary.game.entities.objects.antechamber;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Obelisk extends Entity{
	private int hitsTaken;
	private MapObject object;
	
	public Obelisk(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		animation = Assets.animate(4, 1, 0, Assets.texture_Obelisk);
		impede = true;
		collisionBox = new Rectangle(box.x, box.y, box.width - 2, box.height);
		collisionBox.fitInside(box);
		sprite = new Sprite(animation.getKeyFrame(hitsTaken));
		sprite.setPosition(box.x, box.y);
	}

	public void damageTaken(){
		Assets.sound_SwordClang.play();
		if(++hitsTaken % 4 == 0 && hitsTaken <= 12){
			object.getProperties().put("Type", hitsTaken);
			sprite.setRegion(animation.getKeyFrame(hitsTaken / 4));
			if(hitsTaken != 12){
				Assets.sound_ObeliskDamaged.play();
			}else{
				Assets.sound_ObeliskCollapse.play();
			}
		}
		if(hitsTaken % 12 == 0 && hitsTaken <= 12){
			game.getLabyrinth().getPortal().destroyedObelisk();
		}
	}
}