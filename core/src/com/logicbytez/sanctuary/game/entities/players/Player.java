package com.logicbytez.sanctuary.game.entities.players;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Being;
import com.logicbytez.sanctuary.game.input.Gamepad;
import com.logicbytez.sanctuary.game.input.Keyboard;
import com.logicbytez.sanctuary.game.input.Touchpad;

public class Player extends Being{
	private int maxHealth;
	private Gamepad gamePad;
	private HealthIndicator healthIndicator;
	private Touchpad touchpad;
	private float accumulator = 0;

	//creates one of the players
	public Player(boolean alone, boolean first, GameScreen game){
		super(game);
		attackPower = 1;
		health = maxHealth = 50;
		speed = 100;
		if(first){
			animationAttack = Assets.animate(7, 2, 1, Assets.texture_PlayerBlue);
			animationMove = Assets.animate(7, 2, 0, Assets.texture_PlayerBlue);
		}else{
			animationAttack = Assets.animate(7, 2, 1, Assets.texture_PlayerRed);
			animationMove = Assets.animate(7, 2, 0, Assets.texture_PlayerRed);
		}
		healthIndicator = new HealthIndicator(this);
		sprite = new Sprite(animationMove.getKeyFrame(0));
		if(first){
			if(alone){
				sprite.setPosition(104, 144);
			}else{
				sprite.setPosition(136.5f, 144);
			}
		}else{
			facing = true;
			sprite.flip(true, false);
			sprite.setPosition(169, 144);
		}
		setBoxes();
	}

	//controls the movement of the player
	public void update(float delta){
		if(gamePad != null){
			gamePad.update(move);
		}else if(touchpad != null){
			touchpad.update(move);
		}else{
			Keyboard.update(this, move);
		}
		if(health > 0 && !game.isStopped()){
			move.scl(speed * delta);
		}else{
			move.setZero();
		}
		if(health > 0){
			super.update(delta);
		}
		// regenerate 1 hp/s
		if(health > 0 && health < maxHealth && (accumulator += delta) > 1){
			health++;
			accumulator--;
		}
		healthIndicator.update(delta);
	}

	//makes the player perform an action
	public void action(int button){
		if(game.isPaused()){
			switch (button) {
			case Gamepad.START:
				game.switchMenu();
				break;
			case Gamepad.A:
				game.getPauseScreen().select();
				break;
			case Gamepad.B:
				game.getPauseScreen().changeSelection();
			default:
				break;
			}
		}else if(!attacking && !game.isStopped()){
			switch(button){
			case Gamepad.A:
				game.getActivity().interaction(button, this);
				break;
			case Gamepad.B:
				System.out.println("B");
				break;
			case Gamepad.X:
				Assets.sound_SwordSwing.play();
				attacking = true;
				frameTimer = 0;
				game.getActivity().interaction(button, this);
				game.shakeScreen(.25f, 10);
				healthIndicator.set(health, maxHealth);
				break;
			case Gamepad.Y:
				if(game.getLabyrinth().getSanctuaryX() != 0 || game.getLabyrinth().getSanctuaryY() != 0){
					game.getLabyrinth().updateCurrentRoom(game.getLabyrinth().getSanctuaryX(), game.getLabyrinth().getSanctuaryY());
					sprite.setPosition(144 - sprite.getWidth() / 2, 160 - sprite.getHeight() / 2 - sprite.getWidth());
				}
				break;
			case Gamepad.START:
				game.switchMenu();
			}
		}
	}

	//keeps the player from going off screen
	public void contain(Camera camera, Vector2 view){
		if(sprite.getX() + sprite.getWidth() > camera.position.x + view.x / 2){
			sprite.setX(camera.position.x + view.x / 2 - sprite.getWidth());
		}else if(sprite.getX() < camera.position.x - view.x / 2){
			sprite.setX(camera.position.x - view.x / 2);
		}
		if(sprite.getY() + sprite.getHeight() > camera.position.y + view.y / 2){
			sprite.setY(camera.position.y + view.y / 2 - sprite.getHeight());
		}else if(sprite.getY() < camera.position.y - view.y / 2){
			sprite.setY(camera.position.y - view.y / 2);
		}
		updateBoxes();
	}

	//returns the health indicator for the player
	public HealthIndicator getHealthIndicator(){
		return healthIndicator;
	}

	//returns the amount of damage the player does
	public int getAttackPower(){
		return attackPower;
	}

	//sets the direction the sprite is facing
	public void setFacing(boolean facing){
		this.facing = facing;
	}

	public Gamepad getGamePad() {
		return gamePad;
	}
	
	//sets the controller that the player uses
	public void setGamePad(Gamepad gamepad){
		this.gamePad = gamepad;
	}

	//sets the touch pads to this player
	public void setTouchPads(Touchpad touchpad){
		this.touchpad = touchpad;
	}

	//makes the player take damage to their health
	public void takeDamage(int damage){
		super.takeDamage(damage);
		game.shakeScreen(.25f, 50);
		healthIndicator.set(health, maxHealth);
		if(health <= 0){
			sprite.setRegion(Assets.texture_PlayerDead);
			sprite.setSize(Assets.texture_PlayerDead.getRegionWidth(), Assets.texture_PlayerDead.getRegionHeight());
			if(game.getPlayers().size > 1){
				game.getPlayers().removeValue(this, true);
				game.getLabyrinth().getCurrentRoom().addEntity(this);
			}
		}
	}

	//gets the game object
	public GameScreen getGame(){
		return game;
	}

	//sets the player's health to full
	public void setFullHealth(){
		health = maxHealth;
	}
}