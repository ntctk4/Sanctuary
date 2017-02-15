package com.logicbytez.sanctuary.game.entities.objects;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Pedestal extends Entity{
	
	int sunstone;
	MapObject object;
	
	public Pedestal(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		impede = true;
		animation = Assets.animate(7, 1, 0, Assets.texture_PedestalStone);
		Object type = object.getProperties().get("Type");
		if(type != null){
			sunstone = (Integer)type;
		}
		sprite = new Sprite(animation.getKeyFrame(sunstone));
		sprite.setPosition(box.x, box.y);
		//This needs to be implemented!
		//There will be two types of these.
		//This might have to be split into two classes.
	}
	
	public void takeSunstone()
	{
		if(sunstone < 1 && game.getHud().getSunstones() > 0){
			Assets.sound_InsertSunstone.play();
			game.getHud().addSunstone(true);
			object.getProperties().put("Type", ++sunstone);
			sprite.setRegion(animation.getKeyFrame(sunstone));
		}
	}
}