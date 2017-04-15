package com.logicbytez.sanctuary.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.logicbytez.sanctuary.Assets;


public class PauseScreen {
	private SpriteBatch batch;
	private GameScreen game;
		
	private Selection selected;
	private boolean select = false;
	
	GlyphLayout pause_layout;
	GlyphLayout resume;
	GlyphLayout quit;
	
	Rectangle quitRect;
	Rectangle resumeRect;
	
	enum Selection {
		RESUME,
		QUIT
	}
	
	public PauseScreen(SpriteBatch batch, GameScreen game) {
		this.batch = batch;
		this.game = game;
		selected = Selection.RESUME;
		
		pause_layout = new GlyphLayout(Assets.font50, "Paused");
		resume = new GlyphLayout(Assets.font25, "Resume");
		quit = new GlyphLayout(Assets.font25, "Quit");
				
		quitRect = new Rectangle(-quit.width/2, -quit.height*2-quit.height-5, quit.width, quit.height+5);
		resumeRect = new Rectangle(-resume.width/2, -resume.height-5, resume.width, resume.height+5);
	}
	
	public void update() {
		
		Assets.font50.draw(batch, "Paused", -pause_layout.width/2, pause_layout.height*2);
		batch.draw(Assets.texture_PauseBar, -Assets.texture_PauseBar.getRegionWidth()/2, pause_layout.height/2);
		
		if (game.isTouchScreen()) {
			Assets.font25.draw(batch, "Resume", -resume.width/2, 0);
			Assets.font25.draw(batch, "Quit", -quit.width/2, -quit.height*2);
		} else {
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
		return quitRect;
	}
	
	public Rectangle getResumeRectangle() {
		return resumeRect;
	}
}
