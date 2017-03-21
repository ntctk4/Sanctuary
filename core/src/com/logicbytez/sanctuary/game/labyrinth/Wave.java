package com.logicbytez.sanctuary.game.labyrinth;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.game.entities.Eidolon;

public class Wave{
	private Room room;
	private Array<Eidolon> eidolons;

	//creates a new wave of eidolons
	public Wave(Room room){
		this.room = room;
		eidolons = new Array<Eidolon>();
	}

	//sets the room that the eidolons are in
	public void setRoom(Room room){
		this.room = room;
	}

	//returns the array of eidolons in the wave
	public Array<Eidolon> getEidolons(){
		return eidolons;
	}

	//returns the room that the eidolons are in
	public Room getRoom(){
		return room;
	}
}