package com.logicbytez.sanctuary.game.entities.objects.sanctuary;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Eidolon;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Orb extends Entity{
	private boolean fired, hit;
	private int eidolonCount = 0;
	private Vector2 dir, orbSpeed, position, velocity;

	public Orb(int speed, GameScreen game, Eidolon target, Sprite pillarSprite){
		super(game);
		impede = false;
		animation = Assets.animate(1, 1, 0, Assets.texture_Orb);
		sprite = new Sprite(animation.getKeyFrame(0));
		sprite.setRegion(Assets.texture_Orb);
		//pillar width = 16 height = 47
		sprite.setPosition(pillarSprite.getX() + 3, pillarSprite.getY() + 35);
		box.set(sprite.getBoundingRectangle());
		box.setHeight(box.width);
		//calculate velocity depending on closest target's initial position (Use getCenter() method!)
		//velocity will essentially be a ratio between x and y in relation to the enemy's distance from the pillar
		//For example, y=1 and x=.5 means that the enemy is twice as far vertically than it is horizontally
		//then multiply velocity by speed, which depends on how many sunstones are in the pillar that created this orb
		velocity = new Vector2(1, -1);
		dir = new Vector2();
		orbSpeed = new Vector2(100, 100);
		position = new Vector2();
	}

	public void update(float delta){
		if(!fired){
			position.set(box.x, box.y);
			for(int i = 0; i < game.getEntities().size; i++){
				Entity entity1 = game.getEntities().get(i);
				if(entity1.getClass() == Eidolon.class){
					Eidolon enemy = (Eidolon)entity1;
					if(enemy.getHealth() > 0){
						dir.x = entity1.getBox().x;
						dir.y = entity1.getBox().y;
						dir.sub(position).nor();
						break;
					}
				}
			}
			velocity.set(dir).scl(orbSpeed).scl(delta);
			fired = true;
		}
		sprite.translate(velocity.x, velocity.y);

		box.x = sprite.getX();
		box.y = sprite.getY();
		//move sprite with velocity
		if(!hit){
			for(int i = 0; i < game.getEntities().size; i++){
				Entity entity = game.getEntities().get(i);
				if(box.overlaps(entity.getBox()) && entity.getClass() == Eidolon.class){
					Eidolon enemy = (Eidolon)entity;
					if(enemy.getHealth() > 0){
						Assets.sound_Crystal.play();
						enemy.takeDamage(3);
						game.getLabyrinth().getCurrentRoom().getEntities().removeValue(this, true);
						hit = true;
						break;
					}
				}
			}
		}
		for(int i = 0; i < game.getEntities().size; i++){
			Entity entity = game.getEntities().get(i);
			if(entity.getClass() == Eidolon.class)
			{
				Eidolon enemy = (Eidolon)entity;
				if(enemy.getHealth() != 0)
					eidolonCount++;
			}
		}
		if(eidolonCount == 0)
			game.getLabyrinth().getCurrentRoom().setDanger(false);
		eidolonCount = 0;
	}
}