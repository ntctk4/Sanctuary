package com.logicbytez.sanctuary.game.entities;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Being;

public class Eidolon extends Being{
	private boolean random, stuck;
	private int deathFrame = MathUtils.random(7);
	private float colorTimer = 0;
	private Animation<TextureRegion> animationDead;
	private Vector2 oldPosition;

	//calculates an initial position, then spawns the enemy
	public Eidolon(GameScreen game){
		super(game);
		animationAttack = Assets.animate(7, 3, 1, Assets.texture_Eidolon);
		animationDead = Assets.animate(7, 3, 2, Assets.texture_Eidolon);
		animationMove = Assets.animate(7, 3, 0, Assets.texture_Eidolon);
		attackPower = 1;
		colorable = false;
		health = 3;
		oldPosition = new Vector2();
		speed = MathUtils.random(25, 50);
		sprite = new Sprite(animationMove.getKeyFrame(0));
		sprite.setColor(colorTimer, colorTimer, colorTimer, 1);
		sprite.setPosition(144 - sprite.getWidth() / 2, 160 - sprite.getHeight() / 2 - sprite.getWidth());
		setBoxes();
	}

	//controls the movement of the enemy
	public void update(float delta){
		if(health > 0 && !getGame().isStopped()){
			center.x = sprite.getX() + sprite.getWidth() / 2;
			center.y = sprite.getY() + sprite.getHeight() / 2;
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
			if(center.equals(oldPosition) && !attacking){
				if(!stuck){
					random = MathUtils.randomBoolean();
					stuck = true;
				}
				if(move.x > 0 || move.x < 0){
					if(center.x != oldPosition.x){
						stuck = false;
						random = MathUtils.randomBoolean();
					}else if(random){
						System.out.println("UP: " + move.x + " , " + move.y);
						move.y = speed * delta;
					}else{
						System.out.println("DOWN: " + move.x + " , " + move.y);
						move.y = -speed * delta;
					}
				}else if(move.y > 0 || move.y < 0){
					if(center.y != oldPosition.y){
						stuck = false;
						random = MathUtils.randomBoolean();
					}else if(random){
						System.out.println("LEFT: " + move.x + " , " + move.y);
						move.x = -speed * delta;
					}else{
						System.out.println("RIGHT: " + move.x + " , " + move.y);
						move.x = speed * delta;
					}
				}
			}
			oldPosition.set(center);
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
				Assets.sound_PlayerHurt.play();
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
			long id = Assets.sound_EidolonHurt.play();
			float pitch = MathUtils.randomTriangular() / 4;
			Assets.sound_SwordSlash.play();
			if(health < 1){
				flat = true;
				frameTimer = 0;
				pitch += .5;
				sprite.rotate(MathUtils.random(90, 180));
				sprite.setColor(.75f, .75f, .75f, 1);
			}else{
				speed += MathUtils.random(1, 10);
				pitch++;
			}
			Assets.sound_EidolonHurt.setPitch(id, pitch);
		}
	}
}