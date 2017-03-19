package com.logicbytez.sanctuary.game.entities;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.game.GameScreen;

public abstract class Being extends Entity{
	protected boolean attacking, facing;
	protected int attackPower, health, speed;
	protected Animation<TextureRegion> animationAttack, animationMove;
	protected Vector2 center, move;

	//creates the required components for a Being
	public Being(GameScreen game){
		super(game);
		center = new Vector2();
		collisionBox = new Rectangle();
		move = new Vector2();
	}

	//animates and moves a sprite
	public void update(float delta){
		if(attacking){
			frameTimer += Math.sqrt(speed) * delta * 2;
			if(frameTimer > 7){
				attacking = false;
			}
			sprite.setRegion(animationAttack.getKeyFrame(frameTimer));
		}else{
			if(move.isZero() || game.getActivity().checkCollision(collisionBox, move)){
				frameTimer = 0;
			}else{
				frameTimer += Math.sqrt(speed * 2) * delta;
				if(frameTimer < 1 || frameTimer > 7){
					frameTimer = 1;
				}
				if(move.x > 0){
					facing = false;
				}else if(move.x < 0){
					facing = true;
				}
				sprite.translate(move.x, move.y);
				updateBoxes();
			}
			sprite.setRegion(animationMove.getKeyFrame(frameTimer));
		}
		if(facing){
			sprite.flip(true, false);
		}
		move.setZero();
	}

	//returns the center of the sprite
	public Vector2 getCenter(){
		center.x = sprite.getX() + sprite.getWidth() / 2;
		center.y = sprite.getY() + sprite.getHeight() / 2;
		return center;
	}

	//returns the remaining health number
	public int getHealth(){
		return health;
	}

	//sets up the rectangles based on the sprite
	protected void setBoxes(){
		box.set(sprite.getBoundingRectangle());
		box.setHeight(box.width);
		collisionBox.set(box);
		collisionBox.setHeight(1);
		box.y -= sprite.getWidth() / 2;
	}

	//moves the being to a new position
	public void setPosition(float x, float y){
		sprite.setPosition(x, y);
		updateBoxes();
	}

	//makes the being take damage to their health
	protected void takeDamage(int damage){
		health -= damage;
	}

	//updates the rectangles based on the sprite
	protected void updateBoxes(){
		box.setPosition(sprite.getX(), sprite.getY());
		collisionBox.setPosition(box.x, box.y);
		box.y -= sprite.getWidth() / 2;
	}
}