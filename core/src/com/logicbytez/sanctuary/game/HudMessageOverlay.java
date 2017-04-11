package com.logicbytez.sanctuary.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.logicbytez.sanctuary.Assets;

public class HudMessageOverlay {
	public Stage stage;
	private Viewport viewport;
	private Label messageLabel;
	private Label messageLabel2;
	private Table table;
	
	public HudMessageOverlay(Vector2 view, Batch batch) {
		viewport = new FitViewport(view.x, view.y, new OrthographicCamera());
		stage = new Stage(viewport);
		Label.LabelStyle style = new Label.LabelStyle(Assets.fontMonologue, Color.WHITE);
		messageLabel = new Label("", style);
		messageLabel2 = new Label("", style);
		
		table = new Table();
		table.top();
		table.setFillParent(true);
		table.add(messageLabel).padTop(10).expandX();
		table.row();
		table.add(messageLabel2).expandX();
		
		
		stage.addActor(table);
	}
	
	public void setText(String message) {
		messageLabel.setText(message);
		messageLabel2.setText("");
	}
	
	public void setText(String message, String line2) {
		messageLabel.setText(message);
		messageLabel2.setText(line2);
		
	}
}
