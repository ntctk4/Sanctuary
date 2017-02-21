package com.logicbytez.sanctuary.game.entities;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;

public class Entity{
	protected boolean colorable = true, flat, impede;
	protected float frameTimer;
	protected Animation<TextureRegion> animation;
	protected GameScreen game;
	protected Rectangle box, collisionBox;
	protected Sprite sprite;

	//creates a standard rectangle
	public Entity(GameScreen game){
		this.game = game;
		box = new Rectangle();
	}

	//creates an object that is part of a map
	public Entity(GameScreen game, MapObject object){
		this(game);
		MapProperties traits = object.getProperties();
		float x = (Float)traits.get("x"), y = (Float)traits.get("y");
		box.set(x, y, (Float)traits.get("width"), (Float)traits.get("height"));
		if(object.getName().equals("column")){
			impede = true;
			sprite = new Sprite(Assets.texture_Column);
		}
		if(sprite != null){
			sprite.setPosition(x, y);
		}
	}

	//updates the entity
	public void update(float delta){
	}

	//returns the standard rectangle
	public Rectangle getBox(){
		return box;
	}

	//returns the collision rectangle
	public Rectangle getCollisionBox(){
		if(collisionBox == null){
			return box;
		}
		return collisionBox;
	}

	//returns true if it blocks movement
	public boolean getImpede(){
		return impede;
	}

	//returns its sprite
	public Sprite getSprite(){
		return sprite;
	}

	//returns true if it is flat
	public boolean isFlat(){
		return flat;
	}

	//returns its color tinting
	public void setColor(Color color){
		if(colorable){
			sprite.setColor(color);
		}
	}
}