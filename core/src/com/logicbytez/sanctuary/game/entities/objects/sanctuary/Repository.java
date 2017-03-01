package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Repository extends Entity{
	
	int stonesInserted;
	MapObject object;
	
	public Repository(GameScreen game, MapObject object){
		super(game, object);
		//flat = true;
		this.object = object;
		animation = Assets.animate(9, 1, 0, Assets.texture_Repository);
		impede = true;
		stonesInserted = 0;
		sprite = new Sprite(animation.getKeyFrame(stonesInserted));
		sprite.setPosition(box.x, box.y);
	}
	
public void insertStone(){
		
		if(stonesInserted < 8 && game.getHud().getStones() > 0)
		{
			Assets.sound_Sunstone.play();
			game.getHud().addStone(false);
			object.getProperties().put("Type", ++stonesInserted);
			sprite.setRegion(animation.getKeyFrame(stonesInserted));
		}
	
	}
}