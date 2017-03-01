package com.logicbytez.sanctuary.game.input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.players.Player;

public class Touchpad{
	private Camera display;
	private GameScreen game;
	private Player player;
	private Rectangle leftPad, leftPadButtons[];
	private Rectangle pausedQuit, pausedResume;
	private Rectangle rightPad, rightPadButtons[], screenTop;
	private Sprite leftSprite, rightSprite;
	private Vector2 pressing;
	private Vector3 touch;

	//creates the touch pads and their button areas
	public Touchpad(float x1, float y1, Camera display, GameScreen game, Player player){
		this.display = display;
		this.game = game;
		this.player = player;
		leftSprite = new Sprite(Assets.texture_PadOutline);
		rightSprite = new Sprite(Assets.texture_PadOutline);
		float x2 = x1 * -1 - rightSprite.getWidth();
		leftSprite.setPosition(x1, y1);
		rightSprite.setPosition(x2, y1);
		leftPad = new Rectangle(x1, y1, 180, 180);
		pausedQuit = game.getPauseScreen().getQuitRectangle();
		pausedResume = game.getPauseScreen().getResumeRectangle();
		rightPad = new Rectangle(x2 - 45, y1, 180, 180);
		screenTop = new Rectangle(x1, y1 * -.5f, x1 * -2, y1 * -.5f);
		leftPadButtons = new Rectangle[9];
		rightPadButtons = new Rectangle[4];
		int i = 0, j = 0;
		for(int ry = 2; ry >= 0; ry--){
			for(int rx = 0; rx < 3; rx++){
				leftPadButtons[i] = new Rectangle();
				leftPadButtons[i].setPosition(x1 + rx * 45, y1 + ry * 45);
				leftPadButtons[i++].setSize(rx != 2 ? 45 : 90, ry != 2 ? 45 : 90);
				if(i % 2 == 0){
					float height = ry != 2 ? ry != 0 ? 45 : 67.5f : 112.5f;
					rightPadButtons[j] = new Rectangle();
					rightPadButtons[j].setPosition(x2 + rx * 45, y1 + ry * 45);
					rightPadButtons[j++].setSize(rx != 0 ? 45 : 90, height);
				}
			}
		}
		rightPadButtons[0].y -= 22.5;
		rightPadButtons[1].x -= 45;
		pressing = new Vector2();
		touch = new Vector3();
	}

	//checks for input from the touch screen
	public void update(Vector2 move){
		leftSprite.setRegion(Assets.texture_PadOutline);
		rightSprite.setRegion(Assets.texture_PadOutline);
		if(pressing.x != -1 && !Gdx.input.isTouched((int)pressing.x)){
			pressing.set(-1, -1);
		}
		for(int i = 0; i < 2; i++){
			if(Gdx.input.isTouched(i)){
				touch.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				display.unproject(touch);
				if(game.isPaused()){
					if(pausedResume.contains(touch.x, touch.y)){
						player.action(Gamepad.START);
					}else if(pausedQuit.contains(touch.x, touch.y)){
						game.exit();
					}
				}else if(screenTop.contains(touch.x, touch.y)){
					player.action(Gamepad.START);
				}else{
					updateLeftPad(move);
					updateRightPad(i);
				}
			}
		}
	}

	//updates the left pad sprite and player movement
	private void updateLeftPad(Vector2 move){
		if(leftPad.contains(touch.x, touch.y)){
			if(leftPadButtons[0].contains(touch.x, touch.y)){
				setSprite(true, 0, Assets.texture_PadDiagonal);
				move.set(-1, 1);
			}else if(leftPadButtons[1].contains(touch.x, touch.y)){
				setSprite(true, 0, Assets.texture_PadStraight);
				move.set(0, 1);
			}else if(leftPadButtons[3].contains(touch.x, touch.y)){
				setSprite(true, 90, Assets.texture_PadStraight);
				move.set(-1, 0);
			}else if(leftPadButtons[5].contains(touch.x, touch.y)){
				setSprite(true, -90, Assets.texture_PadStraight);
				move.set(1, 0);
			}else if(leftPadButtons[6].contains(touch.x, touch.y)){
				setSprite(true, 90, Assets.texture_PadDiagonal);
				move.set(-1, -1);
			}else if(leftPadButtons[7].contains(touch.x, touch.y)){
				setSprite(true, 180, Assets.texture_PadStraight);
				move.set(0, -1);
			}else if(leftPadButtons[8].contains(touch.x, touch.y)){
				setSprite(true, 180, Assets.texture_PadDiagonal);
				move.set(1, -1);
			}else{
				setSprite(true, -90, Assets.texture_PadDiagonal);
				move.set(1, 1);
			}
		}
	}

	//updates the right pad sprite and player action
	private void updateRightPad(int i){
		if(rightPad.contains(touch.x, touch.y)){
			if(rightPadButtons[0].contains(touch.x, touch.y)){
				setSprite(false, 0, Assets.texture_PadButton);
				if(pressing.x <= i && pressing.y != 0){
					player.action(Gamepad.Y);
					pressing.set(i, 0);
				}
			}else if(rightPadButtons[1].contains(touch.x, touch.y)){
				setSprite(false, 90, Assets.texture_PadButton);
				if(pressing.x <= i && pressing.y != 1){
					player.action(Gamepad.X);
					pressing.set(i, 1);
				}
			}else if(rightPadButtons[2].contains(touch.x, touch.y)){
				setSprite(false, -90, Assets.texture_PadButton);
				if(pressing.x <= i && pressing.y != 2){
					player.action(Gamepad.B);
					pressing.set(i, 2);
				}
			}else if(rightPadButtons[3].contains(touch.x, touch.y)){
				setSprite(false, 180, Assets.texture_PadButton);
				if(pressing.x <= i && pressing.y != 3){
					player.action(Gamepad.A);
					pressing.set(i, 3);
				}
			}else if(pressing.x == i){
				pressing.set(-1, -1);
			}
		}else if(pressing.x == i){
			pressing.set(-1, -1);
		}
	}

	//Retrieves the sprite for one of the touch pads
	public Sprite getSprite(boolean left){
		if(left){
			return leftSprite;
		}else{
			return rightSprite;
		}
	}

	//sets the sprite of one of the touch pads
	private void setSprite(boolean left, int rotate, TextureRegion pad){
		if(left){
			leftSprite.setRegion(pad);
			leftSprite.setRotation(rotate);
		}else{
			rightSprite.setRegion(pad);
			rightSprite.setRotation(rotate);
		}
	}
}