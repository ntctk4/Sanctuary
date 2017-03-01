package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.game.entities.Entity;
import com.logicbytez.sanctuary.game.entities.Eidolon;
import com.logicbytez.sanctuary.game.entities.objects.Door;
import com.logicbytez.sanctuary.game.entities.objects.Pedestal_Crystal;
import com.logicbytez.sanctuary.game.entities.objects.Pedestal_Stone;
import com.logicbytez.sanctuary.game.entities.objects.sanctuary.Altar;
import com.logicbytez.sanctuary.game.entities.objects.sanctuary.Pillar;
import com.logicbytez.sanctuary.game.entities.objects.sanctuary.Repository;
import com.logicbytez.sanctuary.game.entities.players.Player;
import com.logicbytez.sanctuary.game.input.Gamepad;

public class Activity{
	private Array<Entity> entities;
	private GameScreen game;
	private Rectangle boxMoved, otherBox;

	//creates the boxes for collision detection
	public Activity(Array<Entity> entities, GameScreen game){
		this.game = game;
		this.entities = entities;
		boxMoved = new Rectangle();
		otherBox = new Rectangle();
	}

	//checks if a box collides with any another box
	public boolean checkCollision(Rectangle box, Vector2 move){
		boxMoved.set(box);
		otherBox.setSize(16);
		for(int y = (int)(box.y / 16) - 1; y < (box.y + box.height) / 16 + 1; y++){
			for(int x = (int)(box.x / 16) - 1; x < (box.x + box.width) / 16 + 1; x++){
				if(getTileImpede(x, y)){
					otherBox.setPosition(x * 16, y * 16);
					checkOverlap(box, move);
				}
			}
		}
		for(int i = 0; i < entities.size; i++){
			if(entities.get(i).getImpede() == true){
				otherBox.set(entities.get(i).getCollisionBox());
				checkOverlap(box, move);
			}
		}
		otherBox = new Rectangle();
		return move.isZero() ? true : false;
	}

	//checks if a tile blocks movement
	boolean getTileImpede(int x, int y){
		TiledMapTileLayer bottomLayer = game.getLabyrinth().getCurrentRoom().getBottomLayer();
		if(bottomLayer.getCell(x, y) != null){
			return bottomLayer.getCell(x, y).getTile().getProperties().containsKey("impede");
		}
		return true;
	}

	//checks if a box will move over another one
	private void checkOverlap(Rectangle box, Vector2 move){
		if(box.overlaps(otherBox)){
			move.y *= -1;
		}else{
			boxMoved.setPosition(box.x + move.x, box.y);
			if(boxMoved.overlaps(otherBox)){
				if(boxMoved.x < 0){
					move.x = -box.x;
				}else{
					move.x = otherBox.x - box.x;
					if(move.x > 0){
						move.x -= box.width;
					}else if(move.x < 0){
						move.x += otherBox.width;
					}
				}
			}
			boxMoved.setPosition(box.x, box.y + move.y);
			if(boxMoved.overlaps(otherBox)){
				if(boxMoved.y < 0){
					move.y = -box.y;
				}else{
					move.y = otherBox.y - box.y;
					if(move.y > 0){
						move.y -= box.height;
					}else if(move.y < 0){
						move.y += otherBox.height;
					}
				}
			}
		}
	}

	//allows a player to interact with the world
	public void interaction(int button, Player player){
		for(int i = 0; i < entities.size; i++){
			Entity entity = entities.get(i);
			if(entity != player && player.getBox().overlaps(entity.getBox())){
				if(button == Gamepad.A){
					if(entity.getClass() == Altar.class){
						((Altar)entity).insertStone();
						break;
					}else if(entity.getClass() == Door.class){
						((Door)entity).open();
						break;
					}else if(entity.getClass() == Pedestal_Crystal.class){
						((Pedestal_Crystal)entity).dispenseCrystal();
						break;
					}else if(entity.getClass() == Pedestal_Stone.class){
						((Pedestal_Stone)entity).dispenseSunstone();
						break;
					}else if(entity .getClass() == Pillar.class){
						((Pillar)entity).insertStone();
						break;
					}else if(entity.getClass() == Repository.class){
						((Repository)entity).insertStone();
						break;
					}
				}else if(button == Gamepad.X && entity.getClass() == Eidolon.class){
					Eidolon enemy = (Eidolon)entity;
					if(enemy.getHealth() > 0){
						enemy.takeDamage(player.getAttackPower());
						break;
					}
				}
			}
		}
	}
}