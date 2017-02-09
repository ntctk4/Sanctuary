package com.logicbytez.sanctuary.game.entities.objects.antechamber;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.logicbytez.sanctuary.Assets;
import com.logicbytez.sanctuary.game.GameScreen;
import com.logicbytez.sanctuary.game.entities.Entity;
import com.logicbytez.sanctuary.game.entities.Eidolon;

public class Portal extends Entity{
	private float spawnTimer;
	private int enemyAmount;

	public Portal(GameScreen game){
		super(game);
		this.setGame(game);
		animation = Assets.animate(7, 2, 0, Assets.texture_Portal);
		flat = true;
		sprite = new Sprite(animation.getKeyFrame(0));
		sprite.setPosition(18 * 16 / 2 - sprite.getWidth() / 2, 20 * 16 / 2 - sprite.getHeight() + 2);
		box.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}

	public void update(float delta){
		frameTimer += delta * 15;
		spawnTimer += delta * 5;
		if(frameTimer > 7){
			frameTimer = 0;
		}
		sprite.setRegion(animation.getKeyFrame(frameTimer));
		if(!getGame().isStopped()){
			if(enemyAmount < 10 && spawnTimer > 10){
				spawnTimer = 0;
				if(enemyAmount < 1){ //TESTING: set 1 back to 10
					Eidolon enemy = new Eidolon(getGame());
					enemyAmount++;
					getGame().getLabyrinth().getCurrentRoom().addEntity(enemy);
					getGame().getEntities().add(enemy);
				}
			}else if(enemyAmount >= 10 && spawnTimer > 1){
				Eidolon enemy = new Eidolon(getGame());
				getGame().getLabyrinth().getCurrentRoom().addEntity(enemy);
				getGame().getEntities().add(enemy);
				spawnTimer = 0;
			}
		}
	}
}