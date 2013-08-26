package com.rqbrt.ld27.level;

import com.rqbrt.ld27.level.tile.Tile;

public class PathMap {
	
	private Level level;
	private Tile[] tiles;
	
	public PathMap(Level level, Tile[] tiles) {
		this.level = level;
		this.tiles = tiles;
	}
	
	public void refreshMap() {
		Tile playerTile = level.getPlayer().getTile();
		if(playerTile != null) {
			for(int i = 0; i < tiles.length; i++) {
				Tile t = tiles[i];
				if(!t.isSolid()) {
					int xDistance = Math.abs(t.getX() - playerTile.getX());
					int yDistance = Math.abs(t.getY() - playerTile.getY());
					int distance = (xDistance / 16) + (yDistance / 16);
					t.setPathValue(distance);
				} else {
					t.setPathValue(-1);
				}
			}
		}
	}
}
