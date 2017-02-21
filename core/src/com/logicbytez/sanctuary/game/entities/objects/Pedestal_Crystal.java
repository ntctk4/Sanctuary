package com.logicbytez.sanctuary.game.entities.objects;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Pedestal_Crystal extends Entity{
	boolean crystal = true;

	//creates the pedestal by setting up its data
	public Pedestal_Crystal(GameScreen game, MapObject object){
		super(game, object);
		animation = Assets.animate(2, 1, 0, Assets.texture_PedestalCrystal);
		impede = true;
		collisionBox = new Rectangle(box.x + 8, box.y + 8, box.width / 2, box.height / 2);
		sprite = new Sprite(animation.getKeyFrame(0));
		sprite.setPosition(collisionBox.x, collisionBox.y);
	}

	//removes the crystal from the pedestal and gives it to the player
	public void dispenseCrystal(){
		if(crystal){
			Assets.sound_Crystal.play();
			crystal = false;
			game.getHud().addCrystal(true);
			sprite.setRegion(animation.getKeyFrame(1));
		}
	}
}