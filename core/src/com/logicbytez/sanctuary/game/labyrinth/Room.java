package com.logicbytez.sanctuary.game.labyrinth;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.entities.Entity;

public class Room{
	private boolean hasForeground;
	private char[] doors;
	final static int NONE = -1, UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	private Array<Entity> entities;
	private TiledMap map;
	private Vector2 location;

	//creates the room and map from its door amount
	public Room(int x, int y, int side){
		doors = new char[]{'0', '0', '0', '0'};
		location = new Vector2(x, y);
		entities = new Array<Entity>(false, 0);
		if(side != NONE){
			doors[side] = '1';
			map = Assets.hallways.get(convertBinaryToDecimal(doors));
			if(map.getLayers().get("foreground") != null){
				hasForeground = true;
			}
		}
	}

	//changes the doors and map of the room
	void update(int side){
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

	//adds a new entity to the room
	public void addEntity(Entity entity){
		entities.add(entity);
	}

	//returns the bottom layer of the map
	public TiledMapTileLayer getBottomLayer(){
		return (TiledMapTileLayer)map.getLayers().get(0);
	}

	//returns the entities found in the room
	public Array<Entity> getEntities(){
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
	
	//sets the map of the room
	public void setMap(TiledMap map){
		this.map = map;
	}
	
	//returns true if there is a foreground
	public boolean hasForeground(){
		return hasForeground;
	}

	//changes the map from a hallway to a room
	public void switchType(){
		switch(convertBinaryToDecimal(doors)){
		case 8:
			map = Assets.room_Up;
			break;
		case 4:
			map = Assets.room_Right;
			break;
		case 2:
			map = Assets.room_Down;
			break;
		case 1:
			map = Assets.room_Left;
		}
	}
}