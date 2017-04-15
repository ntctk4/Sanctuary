package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Eidolon;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Pillar extends Entity{
	private int stonesInserted;
	private float orbTimer;
	private Array<Orb> orbs;
	private MapObject object;
	//temp
	Eidolon eidolon;
	
	Eidolon eidolon1;

	public Pillar(GameScreen game, MapObject object){
		super(game, object);
		this.object = object;
		orbs = new Array<Orb>();
		animation = Assets.animate(3, 1, 0, Assets.texture_Pillar);
		impede = true;
		collisionBox = new Rectangle(box.x, box.y, box.width - 2, box.height);
		collisionBox.fitInside(box);
		stonesInserted = 0;
		sprite = new Sprite(animation.getKeyFrame(stonesInserted));
		sprite.setPosition(box.x, box.y);
		orbTimer = 0;
	}

	public void update(float delta){	
		if(game.getLabyrinth().getCurrentRoom().getDanger()){
			orbTimer += delta;
			if(orbTimer > (10 /(stonesInserted+1))){
				orbTimer = 0;
				spawnOrb();
			}
		}
		if(!game.getLabyrinth().getCurrentRoom().getDanger()){
			orbTimer = 0;
		}
	}

	public void insertStone(){
		if(stonesInserted < 2 && game.getHud().getStones() > 0){
			Assets.sound_Sunstone.play();
			game.getHud().addStone(false);
			object.getProperties().put("Type", ++stonesInserted);
			sprite.setRegion(animation.getKeyFrame(stonesInserted));
			
			if(stonesInserted == 2)
			{
				//spawnOrb();
				game.getLabyrinth().getCurrentRoom().setDanger(true);
				spawnEidolon();
			}
		}
	}

	public void spawnOrb(){
		//Eidolon eidolon = new Eidolon(game); //So there's no error, this should be the nearest enemy!
		Orb orb = new Orb(stonesInserted, game, eidolon, sprite);
		orbs.add(orb);
		game.getLabyrinth().getCurrentRoom().getEntities().add(orb);
		game.getEntities().add(orb);
	}
	
	public void spawnEidolon(){
		Eidolon eidolon = new Eidolon(game); //So there's no error, this should be the nearest enemy!
		game.getLabyrinth().getCurrentRoom().getEntities().add(eidolon);
		game.getEntities().add(eidolon);
	}
}