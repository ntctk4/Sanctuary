package com.logicbytez.sanctuary.game.input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.game.entities.players.Player;

public class Gamepad{
	private boolean pressing;
	public final static int A = 0, B = 1, X = 2, Y = 3, START = 7;
	public final static int D_PAD = 0, LEFT_Y = 0, LEFT_X = 1;
	private Controller controller;
	private Player player;

	//stores information for the controller
	public Gamepad(Controller controller, Player player){
		this.controller = controller;
		this.player = player;
	}

	//checks for input from a controller
	public void update(Vector2 move){
		pressing = false;
		controller.addListener(new ControllerAdapter(){
			@Override
			public boolean buttonDown(Controller controller, int button){
				if(!pressing){
					player.action(button);
					pressing = true;
				}
				return false;
			}
		});
		float axis = controller.getAxis(LEFT_Y);
		if(axis > .25 || axis < -.25){
			if (player.getGame().isPaused()) doPauseSelect();
			axis = axis > .5 ? 1 : axis < -.5 ? -1 : axis;
			move.y = -axis;
		}
		axis = controller.getAxis(LEFT_X);
		if(axis > .25 || axis < -.25){
			axis = axis > .5 ? 1 : axis < -.5 ? -1 : axis;
			move.x = axis;
		}
		switch(controller.getPov(D_PAD)){
		case northWest:
			move.x = -1;
		case north:
			if (player.getGame().isPaused()) doPauseSelect();
			move.y = 1;
			break;
		case northEast:
			if (player.getGame().isPaused()) doPauseSelect();
			move.y = 1;
		case east:
			move.x = 1;
			break;
		case southEast:
			move.x = 1;
		case south:
			if (player.getGame().isPaused()) doPauseSelect();
			move.y = -1;
			break;
		case southWest:
			if (player.getGame().isPaused()) doPauseSelect();
			move.y = -1;
		case west:
			move.x = -1;
		default:
		}
	}
	
	private void doPauseSelect() {
		player.getGame().getPauseScreen().changeSelection();
		try {
			Thread.sleep(100);
		} catch (Exception ex) {
			// Don't care if the sleep fails
		}
	}
}