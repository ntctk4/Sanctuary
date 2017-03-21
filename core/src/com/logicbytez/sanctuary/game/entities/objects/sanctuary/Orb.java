package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Eidolon;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Orb extends Entity{
	private int speed;
	private Eidolon target; //either use this, or loop through all entities(enemies) in the room
	private Vector2 velocity;

	public Orb(int speed, GameScreen game, Eidolon target, Sprite sprite){
		super(game);
		this.speed = speed;
		this.target = target;
		sprite.setRegion(Assets.texture_Orb);
		sprite.setPosition(sprite.getX(), sprite.getY());
		//calculate velocity depending on closest target's initial position (Use getCenter() method!)
		//velocity will essentially be a ratio between x and y in relation to the enemy's distance from the pillar
		//For example, y=1 and x=.5 means that the enemy is twice as far vertically than it is horizontally
		//then multiply velocity by speed, which depends on how many sunstones are in the pillar that created this orb
	}

	public void update(float delta){
		sprite.translate(velocity.x, velocity.y);										//move sprite with velocity
		if(box.overlaps(target.getBox())){												//check if box overlaps eidolon's box
			Assets.sound_Crystal.play();												//play sound effect
			target.takeDamage(3);														//do damage to the eidolon
			game.getLabyrinth().getCurrentRoom().getEntities().removeValue(this, true); //remove this orb from the entities array in the room (this is probably not the best way)
			//what you could do instead, a better way, is to let the magic pillar reuse this orb
		}
	}
}