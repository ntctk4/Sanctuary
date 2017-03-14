package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Pillar extends Entity{
	
	int stonesInserted;
	MapObject object;
	public Pillar(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		animation = Assets.animate(3, 1, 0, Assets.texture_Pillar);
		impede = true;
		collisionBox = new Rectangle(box.x, box.y, box.width - 2, box.height);
		collisionBox.fitInside(box);
		stonesInserted = 0;
		sprite = new Sprite(animation.getKeyFrame(stonesInserted));
		sprite.setPosition(box.x, box.y);
	}
	
	public void insertStone(){
		
		if(stonesInserted < 4 && game.getHud().getStones() > 0)
		{
			Assets.sound_Sunstone.play();
			game.getHud().addStone(false);
			object.getProperties().put("Type", ++stonesInserted);
			sprite.setRegion(animation.getKeyFrame(stonesInserted));
		}
	
	}

}