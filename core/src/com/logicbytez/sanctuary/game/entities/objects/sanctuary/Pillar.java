package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
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
		
		impede = true;
		stonesInserted = 0;
		//This needs to be implemented!
	}
	
	public void insertStone(){
		
		if(stonesInserted < 4 && game.getHud().getStones() > 0)
		{
			Assets.sound_Sunstone.play();
			game.getHud().addStone(false);
			sprite.setRegion(animation.getKeyFrame(stonesInserted));
		}
	
	}

}