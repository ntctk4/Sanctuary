package com.logicbytez.sanctuary.game.entities;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Being;

public class Eidolon extends Being{
	private int deathFrame = MathUtils.random(7);
	private float colorTimer = 0;
	private Animation<TextureRegion> animationDead;

	//calculates an initial position, then spawns the enemy
	public Eidolon(GameScreen game){
		super(game);
		animationAttack = Assets.animate(7, 3, 1, Assets.texture_Eidolon);
		animationDead = Assets.animate(7, 3, 2, Assets.texture_Eidolon);
		animationMove = Assets.animate(7, 3, 0, Assets.texture_Eidolon);
		attackPower = 1;
		colorable = false;
		health = 3;
		speed = MathUtils.random(25, 50);
		sprite = new Sprite(animationMove.getKeyFrame(0));
		sprite.setColor(colorTimer, colorTimer, colorTimer, 1);
		sprite.setPosition(144 - sprite.getWidth() / 2, 160 - sprite.getHeight() / 2 - sprite.getWidth());
		setBoxes();
	}

	//controls the movement of the enemy
	public void update(float delta){
		if(health > 0 && !getGame().isStopped()){
			if(colorTimer < 1){
				colorTimer += delta;
				if(colorTimer >= 1){
					colorable = true;
					colorTimer = 1;
				}
				sprite.setColor(colorTimer, colorTimer, colorTimer, 1);
			}
			if(getGame().getPlayers().first().getHealth() > 0 && (getGame().getPlayers().size < 2 || getGame().getPlayers().get(1).getHealth() < 1 || distance(getGame().getPlayers().first()) < distance(getGame().getPlayers().get(1)))){
				if(follow(delta, getGame().getPlayers().first().getBox())){
					getGame().getPlayers().first().takeDamage(attackPower); //put this in middle or end of animation?
				}
			}else if(getGame().getPlayers().size > 1 && getGame().getPlayers().get(1).getHealth() > 0){
				if(follow(delta, getGame().getPlayers().get(1).getBox())){
					getGame().getPlayers().get(1).takeDamage(attackPower); //put this in middle or end of animation?
				}
			}
		}else{
			colorable = true;
		}
		super.update(delta);
		if(health <= 0 && frameTimer == 0){
			sprite.setRegion(animationDead.getKeyFrame(deathFrame));
		}
	}

	//calculates the distance to another being
	private double distance(Being being){
		double x = Math.pow(getCenter().x - being.getCenter().x, 2);
		double y = Math.pow(getCenter().y - being.getCenter().y, 2);
		return Math.sqrt(x + y);
	}

	//moves the enemy towards a target
	private boolean follow(float delta, Rectangle playerBox){
		if(box.overlaps(playerBox)){
			if(!attacking){
				Assets.sound_SwordSwing.play();
				attacking = true;
				frameTimer = 0;
				sprite.setRegion(animationAttack.getKeyFrame(0));
				return true;
			}
		}else{
			if(box.getX() + box.getWidth() <= playerBox.getX()){
				move.x = speed * delta;
			}else if(box.getX() >= playerBox.getX() + playerBox.getWidth()){
				move.x = -speed * delta;
			}
			if(box.getY() + box.getWidth() <= playerBox.getY()){
				move.y = speed * delta;
			}else if(box.getY() >= playerBox.getY() + playerBox.getWidth()){
				move.y = -speed * delta;
			}
		}
		return false;
	}

	//makes the enemy take damage to their health
	public void takeDamage(int damage){
		if(health > 0){
			super.takeDamage(damage);
			if(health < 1){
				long id = Assets.sound_EidolonDeath.play();
				Assets.sound_EidolonDeath.setPitch(id, 1 + MathUtils.randomTriangular() / 2);
				flat = true;
				frameTimer = 0;
				sprite.rotate(MathUtils.random(90, 180));
				sprite.setColor(.75f, .75f, .75f, 1);
			}else{
				long id = Assets.sound_EidolonHurt.play();
				Assets.sound_EidolonHurt.setPitch(id, 1 + MathUtils.randomTriangular() / 4);
				speed += MathUtils.random(1, 10);
			}
		}
	}
}