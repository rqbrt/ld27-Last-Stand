package com.rqbrt.ld27.level.tile;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;

public class Floor extends Tile {

	public Floor(Level level, int x, int y, int width, int height) {
		super(level, x, y, width, height, Sprites.tiles[0][0]);
		name = "Floor";
	}
}
