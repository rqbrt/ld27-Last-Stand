package com.rqbrt.ld27.gfx;

import com.rqbrt.ld27.Game;

public class Screen extends Render {
	
	public Game game;

	public Screen(Game game, int[] pixels, int width, int height) {
		super(pixels, width, height);
		this.game = game;
	}
	
	public void render(int ticks) {
		clear();
		if(game.level != null) {
			game.level.render(this);
		}
		
		if(game.menu != null) {
			game.menu.render(this);
		}
	}
}
