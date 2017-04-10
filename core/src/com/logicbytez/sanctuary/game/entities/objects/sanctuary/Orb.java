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

	public Orb(int speed, GameScreen game, Eidolon target, Sprite pillarSprite){
		super(game);
		this.speed = speed;
		this.target = target;
		impede = true;
		animation = Assets.animate(1, 1, 0, Assets.texture_Orb);
		sprite = new Sprite(animation.getKeyFrame(0));
		//this.sprite = new Sprite();
		sprite.setRegion(Assets.texture_Orb);
		//sprite.setPosition(144 - pillarSprite.getWidth() / 2, 160 - pillarSprite.getHeight() / 2 - pillarSprite.getWidth());
		//pillar width = 16 height = 47
		sprite.setPosition(pillarSprite.getX() + 3, pillarSprite.getY() + 47);
		box.set(sprite.getBoundingRectangle());
		box.setHeight(box.width);
		//sprite.setPosition(box.x, box.y);
		//sprite.setPosition(sprite.getX(), sprite.getY());
		//calculate velocity depending on closest target's initial position (Use getCenter() method!)
		//velocity will essentially be a ratio between x and y in relation to the enemy's distance from the pillar
		//For example, y=1 and x=.5 means that the enemy is twice as far vertically than it is horizontally
		//then multiply velocity by speed, which depends on how many sunstones are in the pillar that created this orb
		//will need this
		velocity = new Vector2();
		velocity.x = 0;
		velocity.y = 0;
		//velocity.x = target.getSprite().getX();
		//velocity.y = target.getSprite().getY();
		
	}

	public void update(float delta){
		sprite.translate(velocity.x, velocity.y);
		//box.x = sprite.getX();
		//box.y = sprite.getY();
		//move sprite with velocity
		if(box.overlaps(target.getBox())){												//check if box overlaps eidolon's box
			Assets.sound_Crystal.play();												//play sound effect
			target.takeDamage(3);														//do damage to the eidolon
			game.getLabyrinth().getCurrentRoom().getEntities().removeValue(this, true); //remove this orb from the entities array in the room (this is probably not the best way)
			//what you could do instead, a better way, is to let the magic pillar reuse this orb
		}
	}
}