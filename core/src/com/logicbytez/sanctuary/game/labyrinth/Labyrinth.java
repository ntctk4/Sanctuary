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
	public static final int MAX_CRYSTALS = 5;
	private boolean activated;
	public final static int[] backgroundLayers = {0, 1}, foregroundLayer = {2};
	private final static int maxSize = 10, center = maxSize / 2;
	private int crystalAmount = MAX_CRYSTALS, roomAmount = 25, stoneAmount = 5, sanctuaryX = 0, sanctuaryY = 0;
	private float eidolonTimer;
	private Altar altar;
	private Array<Entity> entities;
	private Array<Obelisk> obelisks;
	private Array<Pedestal_Stone> pedestals;
	private Array<Pillar> entrywayPillars, sanctuaryPillars;
	private Array<Repository> repositories;
	private Array<Room> rooms;
	private Array<Wave> waves;
	private GameScreen game;
	private Portal portal;
	private Room entryway, antechamber, currentRoom, firstHallway, sanctuary, layout[][];
	private Vector2 roomSize;
	private Wave latestWave;

	//creates the entire level of the labyrinth
	public Labyrinth(Array<Entity> entities, GameScreen game){
		this.entities = entities;
		this.game = game;
		int index = 2;
		obelisks = new Array<Obelisk>(false, 4);
		pedestals = new Array<Pedestal_Stone>(false, stoneAmount);
		entrywayPillars = new Array<Pillar>(false, 2);
		sanctuaryPillars = new Array<Pillar>(false, 4);
		repositories = new Array<Repository>(false, 2);
		waves = new Array<Wave>();
		layout = new Room[maxSize][maxSize];
		currentRoom = layout[center][center] = new Room(center, center, Room.UP, null);
		currentRoom.switchType(Room.Type.SANCTUARY);
		sanctuary = currentRoom;
		entryway = layout[center][center + 1] = new Room(center, center + 1, Room.DOWN, sanctuary);
		firstHallway = layout[center][center + 2] = new Room(center, center + 2, Room.DOWN, entryway);
		entryway.switchType(Room.Type.ENTRYWAY);
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
		//System.out.println(path.size);
		System.out.println("Crystals Not Generated: " + stoneAmount);
		System.out.println("Stones Not Generated: " + stoneAmount);
		System.out.println("Rooms Not Generated: " + roomAmount + "Out Of" + rooms.size);
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
				eidolonTimer += delta;
				if(eidolonTimer >= 10){
					eidolonTimer = 0;
					Array<Eidolon> eidolons = wave.getEidolons();
					if(currentRoom != eidolonRoom){
						//move eidolons to next room
						ArrayIterator<Eidolon> itEidolons = (ArrayIterator<Eidolon>)eidolons.iterator();
						while(itEidolons.hasNext()){
							Eidolon eidolon = itEidolons.next();
							if(eidolon.getHealth() <= 0){
								//Eidolon is dead
								eidolonRoom.addEntity(eidolon);
								itEidolons.remove();
							}else{
								//update eidolon position
								if(eidolonRoom == sanctuary){
									eidolon.setPosition(Room.UP);
								}else{
									eidolon.setPosition(eidolonRoom.getSide(false));
								}
							}
						}
						if(eidolons.size != 0){
							int sunstones = 0;
							for(Repository repository : repositories){
								sunstones += repository.getStonesInserted();
							}
							if(parentRoom == sanctuary && sunstones > 0){
								//take damage to sanctuary's door
								System.out.println("Sanctuary Door is under attack!  Number: " + sunstones); //testing
								if(repositories.get(0).getStonesInserted() > 0){
									repositories.get(0).substractStone();
								}else{
									repositories.get(1).substractStone();
								}
								//kill some eidolons in the entryway
								int pillarStones = 0;
								for(Pillar pillar : entrywayPillars){
									pillarStones = pillar.getStonesInserted() + 1;
								}
								wave.getEidolons().setSize(wave.getEidolons().size - MathUtils.random(pillarStones / 2, wave.getEidolons().size));
							}else if(parentRoom == null && altar.getStonesInserted() > 0){
								//move eidolons to altar & take damage to sun altar
								System.out.println("Sun Altar is under attack!  Number: " + altar.getStonesInserted()); //testing
								altar.substractStone();
								//kill some eidolons in the sanctuary
								int pillarStones = 0;
								for(Pillar pillar : sanctuaryPillars){
									pillarStones = pillar.getStonesInserted() + 1;
								}
								wave.getEidolons().setSize(wave.getEidolons().size - MathUtils.random(pillarStones / 2, wave.getEidolons().size));
							}else if(parentRoom == null){
								//game over
								game.getPlayers().get(0).takeDamage(50);
								if(game.getPlayers().size > 1){
									game.getPlayers().get(1).takeDamage(50);
								}
								break;
							}else if(currentRoom == parentRoom){
								//move eidolons to players' current room
								for(Entity entity : game.getEntities()){
									if(entity.getClass() == Door.class){
										Door door = (Door)entity;
										if(door.getSide().ordinal() == eidolonRoom.getSide(true)){
											door.activate();
										}
									}
								}
								game.getEntities().addAll(eidolons);
								parentRoom.setDanger(true);
								wave.setRoom(parentRoom);
							}else{
								//move eidolons to next room that players are not in
								parentRoom.setDanger(true);
								wave.setRoom(parentRoom);
							}
						}else{
							//remove empty wave
							itWaves.remove();
						}
					}
				}
			}
		}
	}

	//changes the current map to a new one
	public void updateCurrentRoom(int x, int y){
		sanctuaryLocation(-x, -y);
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
				}else if(object.getName().equals("door")){
					entities.add(new Door(game, object));
				}else if(object.getName().equals("pillar")){
					if(!currentRoom.hasGenerated()){
						Pillar pillar = new Pillar(game, object);
						currentRoom.addEntity(pillar);
						if(currentRoom == sanctuary){
							sanctuaryPillars.add(pillar);
						}else{
							entrywayPillars.add(pillar);
						}
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

	//returns the portal from the antechamber
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

	public boolean canEnter(int x, int y){
		Vector2 currentLocation = currentRoom.getLocation();
		Room room = layout[(int)currentLocation.x + x][(int)currentLocation.y + y];
		if(room.getType() != Room.Type.ANTECHAMBER || MAX_CRYSTALS == game.getHud().getCrystals()){
			return true;
		}
		return false;
	}
	
	public void sanctuaryLocation(int x, int y){
		this.sanctuaryX += x;
		this.sanctuaryY += y;
	}
	
	public int getSanctuaryX(){
		return this.sanctuaryX;
	}
	
	public int getSanctuaryY(){
		return this.sanctuaryY;
	}
}