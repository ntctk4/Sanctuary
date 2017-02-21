package com.logicbytez.sanctuary.game.labyrinth;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;
import com.logicbytez.sanctuary.game.entities.objects.Door;
import com.logicbytez.sanctuary.game.entities.objects.Pedestal_Crystal;
import com.logicbytez.sanctuary.game.entities.objects.antechamber.Portal;
import com.logicbytez.sanctuary.game.entities.objects.sanctuary.Altar;
import com.logicbytez.sanctuary.game.entities.objects.Pedestal_Stone;

public class Labyrinth{
	public final static int[] backgroundLayers = {0, 1}, foregroundLayer = {2};
	private final static int maxSize = 11, center = maxSize / 2;
	private int crystalAmount = 2, roomAmount = 10, stoneAmount = 2;
	private Altar altar;
	private Array<Room> rooms;
	private Array<Entity> entities;
	private Array<Pedestal_Stone> pedestals;
	private GameScreen game;
	private Room currentRoom, layout[][];
	private Vector2 roomSize;

	//creates the entire level of the labyrinth
	public Labyrinth(Array<Entity> entities, GameScreen game){
		this.entities = entities;
		this.game = game;
		int index = 2;
		pedestals = new Array<Pedestal_Stone>(false, stoneAmount);
		layout = new Room[maxSize][maxSize];
		currentRoom = layout[center][center] = new Room(center, center, Room.UP);
		currentRoom.switchType(Room.Type.SANCTUARY);
		Room adjacentRoom = layout[center][center + 1] = new Room(center, center + 1, Room.DOWN);
		rooms = new Array<Room>(false, Math.max(roomAmount -= 3, 3));
		rooms.addAll(currentRoom, adjacentRoom, createRoom(adjacentRoom));
		roomSize = new Vector2();
		roomSize.x = currentRoom.getBottomLayer().getWidth();
		roomSize.y = currentRoom.getBottomLayer().getHeight();
		while(roomAmount > 0 && index < rooms.size){
			int nodeIndex = MathUtils.random(index, rooms.size - 1);
			Room room = createRoom(rooms.get(nodeIndex));
			if(room != null){
				if(roomAmount-- == crystalAmount + stoneAmount + 1){
					if(crystalAmount > 0 || stoneAmount > 0){
						if(crystalAmount > 0){
							crystalAmount--;
							room.switchType(Room.Type.CRYSTAL_PEDESTAL_ROOM);
						}else if(stoneAmount > 0){
							stoneAmount--;
							room.switchType(Room.Type.SUNSTONE_PEDESTAL_ROOM);
						}
						rooms.add(room);
						rooms.swap(index++, rooms.size - 1);
					}else{
						room.addEntity(new Portal(game));
						room.switchType(Room.Type.ANTECHAMBER);
						rooms.add(room);
					}
				}else{
					rooms.add(room);
				}
			}else{
				rooms.swap(index++, nodeIndex);
			}
		}
		updateCurrentRoom(0, 0);
		/*System.out.println("Crystals Not Generated: " + stoneAmount);
		System.out.println("Stones Not Generated: " + stoneAmount);
		System.out.println("Rooms Not Generated: " + roomAmount);
		System.out.println("Number of Rooms: " + rooms.size);*/
	}

	//adds a new room to an adjacent one
	private Room createRoom(Room node){
		int x = (int)node.getLocation().x, y = (int)node.getLocation().y;
		int sides[] = {Room.UP, Room.RIGHT, Room.DOWN, Room.LEFT};
		Room room = null;
		shuffleArray(sides);
		loop: for(int i = 0; i < sides.length; i++){
			switch(sides[i]){
			case Room.UP:
				if(y + 1 < maxSize && layout[x][y + 1] == null){
					room = layout[x][y + 1] = new Room(x, y + 1, Room.DOWN);
					node.update(Room.UP);
					break loop;
				}
				break;
			case Room.RIGHT:
				if(x + 1 < maxSize  && layout[x + 1][y] == null){
					room = layout[x + 1][y] = new Room(x + 1, y, Room.LEFT);
					node.update(Room.RIGHT);
					break loop;
				}
				break;
			case Room.DOWN:
				if(y - 1 >= 0  && layout[x][y - 1] == null){
					room = layout[x][y - 1] = new Room(x, y - 1, Room.UP);
					node.update(Room.DOWN);
					break loop;
				}
				break;
			case Room.LEFT:
				if(x - 1 >= 0 && layout[x - 1][y] == null){
					room = layout[x - 1][y] = new Room(x - 1, y, Room.RIGHT);
					node.update(Room.LEFT);
					break loop;
				}
			}
		}
		return room;
	}

	//randomly shuffles an array of integers
	private static void shuffleArray(int[] array){
		for(int i = 0; i < array.length; i++){
			int newIndex = MathUtils.random(array.length - 1);
			int value = array[i];
			array[i] = array[newIndex];
			array[newIndex] = value;
		}
	}

	//changes the current map to a new one
	public void updateCurrentRoom(int x, int y){
		if(x != 0 || y != 0){
			Vector2 currentLocation = currentRoom.getLocation();
			currentRoom = layout[(int)currentLocation.x + x][(int)currentLocation.y + y];
			game.setTileRendererMap(currentRoom.getMap());
			entities.clear();
		}
		entities.addAll(game.getPlayers());
		MapLayer objectLayer = currentRoom.getMap().getLayers().get("objects");
		if(objectLayer != null){
			for(MapObject object : objectLayer.getObjects()){
				if(object.getName().equals("altar")){
					altar = new Altar(game, object);
					entities.add(altar);
				}else if(object.getName().equals("door")){
					entities.add(new Door(game, object));
				}else if(object.getName().equals("pedestal")){
					if(!currentRoom.hasGenerated()){
						if(currentRoom.getType() == Room.Type.CRYSTAL_PEDESTAL_ROOM){
							currentRoom.addEntity(new Pedestal_Crystal(game, object));
						}else{
							Pedestal_Stone pedestal = new Pedestal_Stone(game, object);
							currentRoom.addEntity(pedestal);
							pedestals.add(pedestal);
						}
					}
				}else{
					entities.add(new Entity(game, object));
				}
			}
		}
		for(int i = 0; i < currentRoom.getEntities().size; i++){
			entities.add(currentRoom.getEntities().get(i));
		}
	}

	//pauses or unpauses all of the initialized pedestal timers
	public void modifyPedestalTimers(){
		for(Pedestal_Stone pedestal : pedestals){
			pedestal.update(0);
		}
	}

	//returns the sun altar for sunstone amount
	public Altar getAltar(){
		return altar;
	}
	
	//returns the room the players are in
	public Room getCurrentRoom(){
		return currentRoom;
	}

	//returns the size of every room
	public Vector2 getRoomSize(){
		return roomSize;
	}
}