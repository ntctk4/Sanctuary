package com.logicbytez.sanctuary.game.labyrinth;
import com.badlogic.gdx.utils.Array;
import com.logicbytez.sanctuary.game.entities.Eidolon;

public class Wave{
	private Room room;
	private Array<Eidolon> eidolons;

	public Wave(Room room, Array<Eidolon> eidolons){
		this.eidolons = eidolons;
		this.room = room;
	}

	public void setRoom(Room room){
		this.room = room;
	}

	public Array<Eidolon> getEidolons(){
		return eidolons;
	}

	public Room getRoom(){
		return room;
	}
}