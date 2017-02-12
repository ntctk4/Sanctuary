package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Altar extends Entity{
	int sunstonesInserted;
	MapObject object;
	
	//creates the sun altar by setting up its data
	public Altar(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		impede = true;
		animation = Assets.animate(11, 1, 0, Assets.texture_Altar);
		Object type = object.getProperties().get("Type");
		if(type != null){
			sunstonesInserted = (Integer)type;
		}
		sprite = new Sprite(animation.getKeyFrame(sunstonesInserted));
		sprite.setPosition(box.x, box.y);
	}

	//places a sunstone from the player into the sun altar
	public void insertSunstone(){
		if(sunstonesInserted < 10 && game.getHud().getSunstones() > 0){
			Assets.sound_InsertSunstone.play();
			game.getHud().subSunstone();
			object.getProperties().put("Type", ++sunstonesInserted);
			sprite.setRegion(animation.getKeyFrame(sunstonesInserted));
		}
	}
}