package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Altar extends Entity{
	int stonesInserted;
	MapObject object;
	
	//creates the sun altar by setting up its data
	public Altar(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		animation = Assets.animate(11, 1, 0, Assets.texture_Altar);
		impede = true;
		Object type = object.getProperties().get("Type");
		if(type != null){
			stonesInserted = (Integer)type;
		}
		sprite = new Sprite(animation.getKeyFrame(stonesInserted));
		sprite.setPosition(box.x, box.y);
	}

	//places a sunstone from the player into the sun altar
	public void insertStone(){
		if(stonesInserted < 10 && game.getHud().getStones() > 0){
			Assets.sound_Sunstone.play();
			game.getHud().addStone(false);
			object.getProperties().put("Type", ++stonesInserted);
			sprite.setRegion(animation.getKeyFrame(stonesInserted));
		}
	}

	//returns the amount of sunstones inserted into it
	public int getStonesInserted(){
		return stonesInserted;
	}
}