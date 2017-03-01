package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Pillar extends Entity{
	
	int stonesInserted;
	MapObject object;
	//can keep strack 
	public Pillar(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		animation = Assets.animate(1, 1, 0, Assets.texture_Pillar);
		//animation = Assets.animate(11, 1, 0, Assets.texture_Piller);
		impede = true;
		stonesInserted = 0;
		sprite = new Sprite(animation.getKeyFrame(stonesInserted));
		sprite.setPosition(box.x, box.y);
		//This needs to be implemented!
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