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
import com.logicbytez.sanctuary.game.entities.objects.antechamber.Obelisk;
import com.logicbytez.sanctuary.game.entities.objects.antechamber.Portal;
import com.logicbytez.sanctuary.game.entities.objects.sanctuary.Altar;
import com.logicbytez.sanctuary.game.entities.objects.sanctuary.Pillar;
import com.logicbytez.sanctuary.game.entities.objects.sanctuary.Repository;
import com.logicbytez.sanctuary.game.entities.objects.Pedestal_Stone;

public class Labyrinth{
	public final static int[] backgroundLayers = {0, 1}, foregroundLayer = {2};
	private final static int maxSize = 11, center = maxSize / 2;
	private int crystalAmount = 0, roomAmount = 3, stoneAmount = 0;
	private Altar altar;
	private Array<Room> path, rooms;
	private Array<Entity> entities;
	private Array<Obelisk> obelisks;
	private Array<Pedestal_Stone> pedestals;
	private Array<Pillar> pillars;
	private Array<Repository> repositories;
	private GameScreen game;
	private Portal portal;
	private Room currentRoom, layout[][];
	private Vector2 roomSize;

	//creates the entire level of the labyrinth
	public Labyrinth(Array<Entity> entities, GameScreen game){
		this.entities = entities;
		this.game = game;
		int index = 1;
		path = new Array<Room>();
		obelisks = new Array<Obelisk>(false, 4);
		pedestals = new Array<Pedestal_Stone>(false, stoneAmount);
		pillars = new Array<Pillar>(false, 4);
		repositories = new Array<Repository>(false, 2);
		layout = new Room[maxSize][maxSize];
		currentRoom = layout[center][center] = new Room(center, center, Room.UP, null);
		currentRoom.switchType(Room.Type.SANCTUARY);
		Room adjacentRoom = layout[center][center + 1] = new Room(center, center + 1, Room.DOWN, currentRoom);
		roomAmount = Math.max(roomAmount - 2, 1);
		rooms = new Array<Room>(false, roomAmount + 2);
		rooms.addAll(currentRoom, adjacentRoom);
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
						portal = new Portal(game);
						room.addEntity(portal);
						room.switchType(Room.Type.ANTECHAMBER);
						rooms.add(room);
						while(room != null){
							path.add(room);
							room = room.getParent();
						}
					}
				}else{
					rooms.add(room);
				}
			}else{
				rooms.swap(index++, nodeIndex);
			}
		}
		updateCurrentRoom(0, 0);
		/*System.out.println(path.size);
		System.out.println("Crystals Not Generated: " + stoneAmount);
		System.out.println("Stones Not Generated: " + stoneAmount);
		System.out.println("Rooms Not Generated: " + roomAmount + "Out Of" + rooms.size);*/
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
					room = layout[x][y + 1] = new Room(x, y + 1, Room.DOWN, node);
					node.update(Room.UP);
					break loop;
				}
				break;
			case Room.RIGHT:
				if(x + 1 < maxSize  && layout[x + 1][y] == null){
					room = layout[x + 1][y] = new Room(x + 1, y, Room.LEFT, node);
					node.update(Room.RIGHT);
					break loop;
				}
				break;
			case Room.DOWN:
				if(y - 1 >= 0  && layout[x][y - 1] == null){
					room = layout[x][y - 1] = new Room(x, y - 1, Room.UP, node);
					node.update(Room.DOWN);
					break loop;
				}
				break;
			case Room.LEFT:
				if(x - 1 >= 0 && layout[x - 1][y] == null){
					room = layout[x - 1][y] = new Room(x - 1, y, Room.RIGHT, node);
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

	public void update(){
		/*get hourglass time ratio, multiply it by # of rooms in 'path', and take the floor of it (save this #)
		 * if the number changes, then move the enemies to the next room IF the player isn't in the current one
		 * Do this by changing the room # to the corresponding door and removing from one room's entities array
		 * the enemies and adding them to the next
		 * If the player is in the next room, spawn them at the door's location*/
	}

	//changes the current map to a new one
	public void updateCurrentRoom(int x, int y){
		//if # != 0 then enemies come out of door, 1=top,2=right,3=bottom,4=left?
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
					if(!currentRoom.hasGenerated()){
						altar = new Altar(game, object);
						currentRoom.addEntity(altar);
					}
				}else if(object.getName().equals("door")){
					entities.add(new Door(game, object));
				}else if(object.getName().equals("pillar")){
					if(!currentRoom.hasGenerated()){
						Pillar pillar = new Pillar(game, object);
						currentRoom.addEntity(pillar);
						pillars.add(pillar);
					}
				}else if(object.getName().equals("repository")){
					if(!currentRoom.hasGenerated()){
						Repository repository = new Repository(game, object);
						currentRoom.addEntity(repository);
						repositories.add(repository);
					}
				}else if(object.getName().equals("obelisk")){
					if(!currentRoom.hasGenerated()){
						Obelisk obelisk = new Obelisk(game, object);
						currentRoom.addEntity(obelisk);
						obelisks.add(obelisk);
					}
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

	public void activatePortal(){
		portal.activate();
		//spawn enemies (put in arraylist)
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