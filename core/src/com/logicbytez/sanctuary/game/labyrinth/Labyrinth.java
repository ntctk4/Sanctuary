package com.logicbytez.sanctuary.game.labyrinth;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Eidolon;
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
	public static final int MAX_CRYSTALS = 2;
	private boolean activated;
	public final static int[] backgroundLayers = {0, 1}, foregroundLayer = {2};
	private final static int maxSize = 11, center = maxSize / 2;
	private int crystalAmount = MAX_CRYSTALS, roomAmount = 10, stoneAmount = 2;
	private float eidolonTimer;
	private Altar altar;
	private Array<Entity> entities;
	private Array<Obelisk> obelisks;
	private Array<Pedestal_Stone> pedestals;
	private Array<Pillar> pillars;
	private Array<Repository> repositories;
	private Array<Room> rooms;
	private Array<Wave> waves;
	private Door door;
	private GameScreen game;
	private Portal portal;
	private Room entryway, antechamber, currentRoom, sanctuary, layout[][];
	private Vector2 roomSize;
	private Wave latestWave;

	//creates the entire level of the labyrinth
	public Labyrinth(Array<Entity> entities, GameScreen game){
		this.entities = entities;
		this.game = game;
		int index = 2;
		obelisks = new Array<Obelisk>(false, 4);
		pedestals = new Array<Pedestal_Stone>(false, stoneAmount);
		pillars = new Array<Pillar>(false, 4);
		repositories = new Array<Repository>(false, 2);
		waves = new Array<Wave>();
		layout = new Room[maxSize][maxSize];
		currentRoom = layout[center][center] = new Room(center, center, Room.UP, null);
		currentRoom.switchType(Room.Type.SANCTUARY);
		sanctuary = currentRoom;
		entryway = layout[center][center + 1] = new Room(center, center + 1, Room.DOWN, sanctuary);
		Room firstHallway = layout[center][center + 2] = new Room(center, center + 2, Room.DOWN, entryway);
		//Room firstHallway = createRoom(entryway);
		entryway.switchType(Room.Type.ENTRYWAY);
		//Door door = new SanctuaryDoor(game);
		roomAmount = Math.max(roomAmount - 3, 1);
		rooms = new Array<Room>(false, roomAmount + 3);
		rooms.addAll(currentRoom, entryway, firstHallway);
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
						antechamber = room;
						portal = new Portal(game);
						room.addEntity(portal);
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

	//updates the location of the eidolons and door movement
	public void update(float delta){
		if(activated && !game.isPaused()){
			ArrayIterator<Wave> itWaves = (ArrayIterator<Wave>)waves.iterator();
			while(itWaves.hasNext()){
			    Wave wave = itWaves.next();
				Room eidolonRoom = wave.getRoom();
				Room parentRoom = wave.getRoom().getParent();
				//sanctuary should be persistent if player is in entryway and vice-versa
				if(eidolonRoom != currentRoom){
					if(entryway == eidolonRoom || sanctuary == eidolonRoom){
						//persistent rooms
						for(Eidolon eidolon : wave.getEidolons()){
							eidolon.setPersistence(true);
							eidolon.update(delta);
						}
						for(Entity entity : eidolonRoom.getEntities()){
							//for magical pillars
							entity.update(delta);
						}
					}
				}else{
					//change eidolon behavior when player isn't near
					for(Eidolon eidolon : wave.getEidolons()){
						eidolon.setPersistence(false);
					}
				}
				//non-persistent rooms
				eidolonTimer += delta;
				if(eidolonTimer >= 10){
					eidolonTimer = 0;
					Array<Eidolon> eidolons = wave.getEidolons();
					ArrayIterator<Eidolon> itEidolons = (ArrayIterator<Eidolon>)eidolons.iterator();
					while(itEidolons.hasNext()){
						Eidolon eidolon = itEidolons.next();
						if(eidolon.getHealth() <= 0){
							eidolonRoom.addEntity(eidolon);
							itEidolons.remove();
						}else if(parentRoom != null){
							eidolon.setPosition(eidolonRoom.getSide(false));
						}
					}
					if(eidolons.size != 0){
						if(parentRoom != null){
							if(parentRoom.equals(currentRoom)){
								for(Entity entity : game.getEntities()){
									if(entity.getClass() == Door.class){
										Door door = (Door)entity;
										if(door.getSide().ordinal() == eidolonRoom.getSide(true)){
											door.activate();
										}
									}
								}
								game.getEntities().addAll(eidolons);
							}
							parentRoom.setDanger(true);
							wave.setRoom(parentRoom);
						}
					}else{
						itWaves.remove();
					}
				}
			}
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
		for(Wave wave : waves){
			if(wave.getRoom().equals(currentRoom)){
				entities.addAll(wave.getEidolons());
			}
		}
		MapLayer objectLayer = currentRoom.getMap().getLayers().get("objects");
		if(objectLayer != null){
			for(MapObject object : objectLayer.getObjects()){
				if(object.getName().equals("altar")){
					if(!currentRoom.hasGenerated()){
						altar = new Altar(game, object);
						currentRoom.addEntity(altar);
					}
				}else if(object.getName().equals("door")){ //check if its THE door, add THE door (In both Sanctuary & Adjacent Room)
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

	//orders the portal to spawn enemies
	public void activatePortal(){
		activated = true;
		latestWave = new Wave(antechamber);
		waves.add(latestWave);
		portal.activate();
	}

	//adds an eidolon to the array
	public void addEidolon(Eidolon eidolon){
		latestWave.getEidolons().add(eidolon);
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
	
	public Portal getPortal(){
		return portal;
	}

	//returns true if the player is inside the antechamber
	public boolean insideAntechamber(){
		return antechamber.equals(currentRoom);
	}

	//returns the room the players are in
	public Room getCurrentRoom(){
		return currentRoom;
	}

	//returns the size of every room
	public Vector2 getRoomSize(){
		return roomSize;
	}

	public Door getDoor(){
		return door;
	}
}