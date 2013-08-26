package com.rqbrt.ld27.level.tile;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;

public class Wall extends Tile {

	public Wall(Level level, int x, int y, int width, int height) {
		super(level, x, y, width, height, Sprites.tiles[1][0]);
		name = "Wall";
		solid = true;
	}
}
