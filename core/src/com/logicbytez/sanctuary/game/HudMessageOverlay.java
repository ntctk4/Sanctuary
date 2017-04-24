package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.logicbytez.sanctuary.Assets;

public class HudMessageOverlay {
	public Stage stage;
	private Label messageLabel;
	private Label messageLabel2;
	private Table table;
	
	public HudMessageOverlay(Vector2 view){
		stage = new Stage(new StretchViewport(view.x * 4, view.y * 4));
		Label.LabelStyle style = new Label.LabelStyle(Assets.font50, Color.WHITE);
		messageLabel = new Label("", style);
		messageLabel2 = new Label("", style);
		
		table = new Table();
		table.top();
		table.setFillParent(true);
		table.add(messageLabel).padTop(40).expandX();
		table.row();
		table.add(messageLabel2).expandX();
		
		stage.addActor(table);
	}
	
	public void notifyWave() {
		messageLabel.setText("Wave Incoming");
		messageLabel2.setText("");
		stage.getRoot().setColor(1, 1, 1, 0);
		stage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.delay(5f), Actions.fadeOut(2f), Actions.run(new Runnable() {
			@Override
			public void run() {
				messageLabel.setText("");
			}
		})));
	}
	
	public void notifyCrystals() {
		messageLabel.setText("You've found all");
		messageLabel2.setText("the Light Crystals.");
		stage.getRoot().setColor(1, 1, 1, 0);
		stage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.delay(5f), Actions.fadeOut(1f), Actions.run(new Runnable() {
			@Override
			public void run() {
				notifyAntechamber();
			}
		})));
	}
	
	private void notifyAntechamber() {
		messageLabel.setText("Find the Antechamber and");
		messageLabel2.setText("destroy the Obelisks");
		stage.getRoot().setColor(1, 1, 1, 0);
		stage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.delay(5f), Actions.fadeOut(1f), Actions.run(new Runnable() {
			@Override
			public void run() {
				messageLabel.setText("");
				messageLabel2.setText("");						
			}
		})));
	}
	
	public void displayMessage(String str) {
		messageLabel.setText(str);
		stage.getRoot().setColor(1, 1, 1, 0);
		stage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.delay(5f), Actions.fadeOut(1f), Actions.run(new Runnable() {
			@Override
			public void run() {
				messageLabel.setText("");
			}
		})));
	}
}
