package com.logicbytez.sanctuary.game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

class TileRenderer extends OrthogonalTiledMapRenderer{
	//creates the TileRenderer
	public TileRenderer(Batch batch, TiledMap map){
		super(map, batch);
	}

	@Override
	//renders the map without calling the sprite batch
	public void render(int[] layers){
		for(int layerIdx : layers){
			MapLayer layer = map.getLayers().get(layerIdx);
			if(layer.isVisible()){
				if(layer instanceof TiledMapTileLayer){
					renderTileLayer((TiledMapTileLayer)layer);
				}else if (layer instanceof TiledMapImageLayer){
					renderImageLayer((TiledMapImageLayer)layer);
				}else{
					renderObjects(layer);
				}
			}
		}
	}
}