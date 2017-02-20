package com.logicbytez.sanctuary.game.entities.objects;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;
import com.logicbytez.sanctuary.game.entities.players.Player;

public class Door extends Entity{
	private boolean opening;
	private Array<Player> players;
	private Direction direction;

	//cardinal direction that it faces
	private static enum Direction{
		UP, RIGHT, DOWN, LEFT
	}

	//creates the sprite and positions it
	public Door(GameScreen game, MapObject object){
		super(game, object);
		this.players = game.getPlayers();
		sprite = new Sprite();
		sprite.setPosition(box.x, box.y);
		switch(Direction.valueOf(object.getProperties().get("type").toString())){
		case UP:
			animation = Assets.animate(9, 2, 0, Assets.texture_Door);
			direction = Direction.UP;
			sprite.translateY(8);
			break;
		case RIGHT:
			animation = Assets.animate(9, 2, 1, Assets.texture_Door);
			direction = Direction.RIGHT;
			sprite.translate(8, 32);
			break;
		case DOWN:
			animation = Assets.animate(9, 2, 0, Assets.texture_Door);
			colorable = false;
			direction = Direction.DOWN;
			sprite.setColor(.75f, .75f, .75f, 1);
			sprite.translateY(-4);
			break;
		case LEFT:
			animation = Assets.animate(9, 2, 1, Assets.texture_Door);
			direction = Direction.LEFT;
			sprite.translate(-4, 32);

		}
		sprite.setRegion(animation.getKeyFrame(0));
		sprite.setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
	}

	//begins opening the door
	public void open(){
		Assets.sound_Door.play();
		game.shakeScreen(.5f, 5);
		game.switchStopped();
		opening = true;
		if(direction == Direction.LEFT){
			players.first().setFacing(true);
			if(players.size > 1){
				players.get(1).setFacing(true);
			}
		}else if(direction == Direction.RIGHT){
			players.first().setFacing(false);
			if(players.size > 1){
				players.get(1).setFacing(false);
			}
		}
	}

	//animates the door and initiates the teleport
	public void update(float delta){
		if(opening){
			frameTimer += delta * 15;
			if(frameTimer > 9){
				opening = false;
				game.switchStopped();
				teleportPlayers(delta);
				game.updateCamera(delta);
			}
			sprite.setRegion(animation.getKeyFrame(frameTimer));
		}else if(direction == Direction.DOWN){
			sprite.setAlpha(game.alphaRatio());
		}
	}

	//transfers the players to a new room
	private void teleportPlayers(float delta){
		float boxSizeX = players.first().getCollisionBox().getWidth();
		float boxSizeY = players.first().getCollisionBox().getHeight();
		float roomSizeX = game.getLabyrinth().getRoomSize().x * 16;
		float roomSizeY = game.getLabyrinth().getRoomSize().y * 16;
		switch(direction){
		case UP:
			game.getLabyrinth().updateCurrentRoom(0, 1);
			if(players.size > 1){
				players.first().setPosition(roomSizeX / 2 - boxSizeX - 1, 16);
				players.get(1).setPosition(roomSizeX / 2 + 1, 16);
			}else{
				players.first().setPosition(roomSizeX / 2 - boxSizeX / 2, 16);
			}
			break;
		case RIGHT:
			game.getLabyrinth().updateCurrentRoom(1, 0);
			if(players.size > 1){
				players.first().setPosition(16, roomSizeY / 2 - boxSizeX / 2 - 16 - 1);
				players.get(1).setPosition(16, roomSizeY / 2 + boxSizeX / 2 - 16 + 1);
			}else{
				players.first().setPosition(16, roomSizeY / 2 - 16);
			}
			break;
		case DOWN:
			float y = roomSizeY - boxSizeY - 16 * 3;
			game.getLabyrinth().updateCurrentRoom(0, -1);
			if(players.size > 1){
				players.first().setPosition(roomSizeX / 2 - boxSizeX - 1, y);
				players.get(1).setPosition(roomSizeX / 2 + 1, y);
			}else{
				players.first().setPosition(roomSizeX / 2 - boxSizeX / 2, y);
			}
			break;
		case LEFT:
			float x = roomSizeX - boxSizeX - 16;
			game.getLabyrinth().updateCurrentRoom(-1, 0);
			if(players.size > 1){
				players.first().setPosition(x, roomSizeY / 2 - boxSizeX / 2 - 16 - 1);
				players.get(1).setPosition(x, roomSizeY / 2 + boxSizeX / 2 - 16 + 1);
			}else{
				players.first().setPosition(x, roomSizeY / 2 - 16);
			}
		}
	}
}