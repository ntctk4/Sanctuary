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
import com.logicbytez.sanctuary.game.entities.players.Player;
import com.logicbytez.sanctuary.game.labyrinth.Room;

public class Eidolon extends Being{
	private boolean persistence, playerDirection, stuck, stuckDirection;
	private int deathFrame = MathUtils.random(7);
	private float colorTimer = 0;
	private Animation<TextureRegion> animationDead;
	private Player nearestPlayer;
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
		if(health > 0 && !game.isStopped()){
			if(colorTimer < 1){
				colorTimer += delta;
				if(colorTimer >= 1){
					colorable = true;
					colorTimer = 1;
				}
				sprite.setColor(colorTimer, colorTimer, colorTimer, 1);
			}
			//persistence
			if(persistence){
				//game.getLabyrinth().getDoor();
				/*if(follow(delta, door.getBox())){
					door.takeDamage(attackPower);
				}*/
				//track Eidolon and attack door/altar instead of player(s)
			}else if(game.getPlayers().size > 0){
				nearestPlayer = game.getPlayers().first();
				if(game.getPlayers().size > 1 && game.getPlayers().get(1).getHealth() > 0){
					if(nearestPlayer.getHealth() < 1 || distance(nearestPlayer) > distance(game.getPlayers().get(1))){
						nearestPlayer = game.getPlayers().get(1);
					}
				}
				if(follow(delta, nearestPlayer.getBox())){
					nearestPlayer.takeDamage(attackPower);
				}
				if(!attacking && !stuck && (move.x == 0 || move.y == 0)){
					if(stuckDirection = move.y == 0 ? true : false){
						playerDirection = getCenter().y < nearestPlayer.getCenter().y ? true : false;
					}else{
						playerDirection = getCenter().x > nearestPlayer.getCenter().x ? true : false;
					}
					stuck = true;
				}
				if(stuck){
					center.x = sprite.getX() + sprite.getWidth() / 2;
					center.y = sprite.getY() + sprite.getHeight() / 2;
					if(stuckDirection){
						if(center.x != oldPosition.x){
							stuck = false;
						}else if(playerDirection){
							move.y = speed * delta;
						}else{
							move.y = -speed * delta;
						}
					}else{
						if(center.y != oldPosition.y){
							stuck = false;
						}else if(playerDirection){
							move.x = -speed * delta;
						}else{
							move.x = speed * delta;
						}
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

	//sets the position of the enemy to a door
	public void setPosition(int side){
		float boxSizeX = getCollisionBox().getWidth();
		float boxSizeY = getCollisionBox().getHeight();
		float roomSizeX = game.getLabyrinth().getRoomSize().x * 16;
		float roomSizeY = game.getLabyrinth().getRoomSize().y * 16;
		switch(side){
		case Room.UP:
			setPosition(roomSizeX / 2 - boxSizeX / 2, 16);
			break;
		case Room.RIGHT:
			setPosition(16, roomSizeY / 2 - 16);
			break;
		case Room.DOWN:
			float y = roomSizeY - boxSizeY - 16 * 3;
			setPosition(roomSizeX / 2 - boxSizeX / 2, y);
			break;
		case Room.LEFT:
			float x = roomSizeX - boxSizeX - 16;
			setPosition(x, roomSizeY / 2 - 16);
		}
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

	public void setPersistence(boolean bool){
		persistence = bool;
	}
}