package com.logicbytez.sanctuary.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.logicbytez.sanctuary.Assets;


public class PauseScreen {
	private SpriteBatch batch;
	private Vector2 view;
	private GameScreen game;
	
	private Selection selected;
	private boolean select = false;
	
	GlyphLayout pause_layout;
	GlyphLayout resume;
	GlyphLayout quit;
	
	enum Selection {
		RESUME,
		QUIT
	}
	
	public PauseScreen(SpriteBatch batch, Vector2 view, GameScreen game) {
		this.batch = batch;
		this.view = view;
		this.game = game;
		selected = Selection.RESUME;
		
		pause_layout = new GlyphLayout(Assets.font50, "Paused");
		resume = new GlyphLayout(Assets.font25, "Resume");
		quit = new GlyphLayout(Assets.font25, "Quit");
	}
	
	public void update() {
		
		Assets.font50.draw(batch, "Paused", -pause_layout.width/2, pause_layout.height*2);
		batch.draw(Assets.texture_PauseBar, -Assets.texture_PauseBar.getRegionWidth()/2, pause_layout.height/2);
		if(selected == Selection.RESUME) {
			Assets.font25.setColor(Color.RED);
			Assets.font25.draw(batch, "Resume", -resume.width/2, 0);
			Assets.font25.setColor(Color.WHITE);
			Assets.font25.draw(batch, "Quit", -quit.width/2, -quit.height*2);
		} else {
			Assets.font25.draw(batch, "Resume", -resume.width/2, 0);
			Assets.font25.setColor(Color.RED);
			Assets.font25.draw(batch, "Quit", -quit.width/2, -quit.height*2);
			Assets.font25.setColor(Color.WHITE);
		}
		
		if(select) {
			if (selected == Selection.RESUME) game.switchMenu();
			else game.exit();
			select = false;
			selected = Selection.RESUME;
		}
	}
	
	public void changeSelection() {
		if (selected == Selection.RESUME) selected = Selection.QUIT;
		else selected = Selection.RESUME;
	}
	
	public void select() {
		select = true;
	}
	
	public Rectangle getQuitRectangle() {
		return new Rectangle(-quit.width/2, -quit.height*2, quit.width, quit.height);
	}
	
	public Rectangle getResumeRectangle() {
		return new Rectangle(-resume.width/2, 0, resume.width, resume.height);
	}
}
