package com.logicbytez.sanctuary.game.labyrinth;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Room{
	public enum Type{ANTECHAMBER, CRYSTAL_PEDESTAL_ROOM, ENTRYWAY, SANCTUARY, SUNSTONE_PEDESTAL_ROOM}
	private boolean danger, hasForeground, hasGenerated;
	private char[] doors;
	private int doorAmount, side;
	private Type type;
	public final static int NONE = -1, UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	private Array<Entity> entities;
	private TiledMap map;
	private Vector2 location;
	private Room parent;

	//creates the room and map from its door amount
	public Room(int x, int y, int side, Room parent){
		doors = new char[]{'0', '0', '0', '0'};
		entities = new Array<Entity>(false, 0);
		location = new Vector2(x, y);
		this.parent = parent;
		this.side = side;
		if(side != NONE){
			doorAmount++;
			doors[side] = '1';
			map = Assets.hallways.get(convertBinaryToDecimal(doors));
			if(map.getLayers().get(2).getName().equals("foreground")){
				hasForeground = true;
			}
		}
	}

	//changes the doors and map of the room
	void update(int side){
		doorAmount++;
		doors[side] = '1';
		map = Assets.hallways.get(convertBinaryToDecimal(doors));
	}

	//converts binary characters to a decimal integer
	private static int convertBinaryToDecimal(char[] binary){
		int decimal = 0;
		for(int i = 0; i < binary.length; i++){
			if(binary[i] == '1'){
				decimal += Math.pow(2, binary.length - i - 1);
			}
		}
		return decimal;
	}

	//changes the map from a hallway to a type of room
	public void switchType(Type type){
		this.type = type;
		int decimal = convertBinaryToDecimal(doors);
		switch(type){
		case ANTECHAMBER:
			switch(decimal){
			case 8:
				map = Assets.room_Antechamber_Up;
				break;
			case 4:
				map = Assets.room_Antechamber_Right;
				break;
			case 2:
				map = Assets.room_Antechamber_Down;
				break;
			case 1:
				map = Assets.room_Antechamber_Left;
			}
			break;
		case ENTRYWAY:
			map = Assets.room_Entryway;
			break;
		case SUNSTONE_PEDESTAL_ROOM:
			hasForeground = true;
		case CRYSTAL_PEDESTAL_ROOM:
			switch(decimal){
			case 8:
				map = Assets.room_Pedestal_Up;
				break;
			case 4:
				map = Assets.room_Pedestal_Right;
				break;
			case 2:
				map = Assets.room_Pedestal_Down;
				break;
			case 1:
				map = Assets.room_Pedestal_Left;
			}
			break;
		case SANCTUARY:
			map = Assets.room_Sanctuary;
		}
	}

	//adds a new entity to the room
	public void addEntity(Entity entity){
		entities.add(entity);
	}

	//returns true if this room has already been generated
	public boolean hasGenerated(){
		return hasGenerated;
	}

	//returns the bottom layer of the map
	public TiledMapTileLayer getBottomLayer(){
		return (TiledMapTileLayer)map.getLayers().get(0);
	}

	//returns the amount of doors the room has
	public int getDoorAmount(){
		return doorAmount;
	}

	//returns the entities found in the room during generation
	public Array<Entity> getEntities(){
		hasGenerated = true;
		return entities;
	}

	//returns the labyrinth location
	public Vector2 getLocation(){
		return location;
	}

	//returns the map of the room
	public TiledMap getMap(){
		return map;
	}

	public Room getParent(){
		return parent;
	}

	//returns the side of the parent room
	public int getSide(boolean opposite){
		if(opposite){
			switch(side){
			case UP:
				return DOWN;
			case RIGHT:
				return LEFT;
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			}
		}
		return side;
	}

	//returns the type of the room
	public Type getType(){
		return type;
	}
	
	//returns true if there is a foreground
	public boolean hasForeground(){
		return hasForeground;
	}

	//Activates when enemies are in the room
	public void setDanger(){
		danger = true;
	}

	public boolean getDanger(){
		return danger;
	}
}