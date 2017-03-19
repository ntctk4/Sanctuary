package com.logicbytez.sanctuary.game.entities.objects.antechamber;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;
import com.logicbytez.sanctuary.game.entities.Eidolon;

public class Portal extends Entity{
	private boolean activated = true; //testing!
	private float spawnTimer;
	private int enemyAmount;

	public Portal(GameScreen game){
		super(game);
		this.game = game;
		animation = Assets.animate(7, 2, 0, Assets.texture_Portal);
		flat = true;
		sprite = new Sprite(animation.getKeyFrame(0));
		sprite.setPosition(18 * 16 / 2 - sprite.getWidth() / 2, 20 * 16 / 2 - sprite.getHeight() + 2);
		box.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}

	public void update(float delta){
		if(!game.isPaused()){
			if(game.getLabyrinth().insideAntechamber()){
				frameTimer += delta * 15;
				if(frameTimer > 7){
					frameTimer = 0;
				}
				sprite.setRegion(animation.getKeyFrame(frameTimer));
			}
			if(activated){
				spawnTimer += delta * 5;
				if(enemyAmount < 10 && spawnTimer > 10){
					Eidolon eidolon = new Eidolon(game);
					enemyAmount++;
					game.getLabyrinth().addEidolon(eidolon);
					if(game.getLabyrinth().insideAntechamber()){
						game.getEntities().add(eidolon);
					}
					spawnTimer = 0;
				}
			}
		}
	}

	public void activate(){
		activated = true;
	}
}