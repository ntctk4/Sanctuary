package com.logicbytez.sanctuary.game.input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.game.entities.players.Player;

public final class Keyboard{
	//never instantiate this class
	private Keyboard(){}

	//checks for any input via the keyboard
	public static void update(Player player, Vector2 move){
		if(Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE)){
			player.action(Gamepad.A);
		}else if(Gdx.input.isKeyJustPressed(Keys.ALT_LEFT) || Gdx.input.isKeyJustPressed(Keys.ALT_RIGHT)){
			player.action(Gamepad.B);
		}else if(Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT)){
			player.action(Gamepad.X);
		}else if(Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Keys.SHIFT_RIGHT)){
			player.action(Gamepad.Y);
		}else if(Gdx.input.isKeyJustPressed(Keys.END) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			player.action(Gamepad.START);
		}
		checkMoveKeys(move);
	}

	//checks for input from the keys that move the player
	private static void checkMoveKeys(Vector2 move){
		if(Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)){
			if(!Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.DOWN)){
				move.y = 1;
			}
		}else if(Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)){
			if(!Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.UP)){
				move.y = -1;
			}
		}
		if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)){
			if(!Gdx.input.isKeyPressed(Keys.D) && !Gdx.input.isKeyPressed(Keys.RIGHT)){
				move.x = -1;
			}
		}else if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)){
			if(!Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.LEFT)){
				move.x = 1;
			}
		}
	}
}