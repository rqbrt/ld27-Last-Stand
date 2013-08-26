package com.rqbrt.ld27.input;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gui.menu.PauseMenu;
import com.rqbrt.ld27.level.tile.Tile;

public class Controller {
	
	public Game game;
	
	private int speed = 3;
	
	private boolean escape = false;
	private long lastEscapeDown = System.nanoTime();
	private long escapeDown;
	
	public Controller(Game game) {
		this.game = game;
	}
	
	public void tick(boolean up, boolean down, boolean left, boolean right, boolean one, boolean two, boolean three, boolean four, boolean esc) {
		if(!game.isPaused()) {
			Tile t;
			if(right) {
				t = game.level.getTiles()[((game.level.getPlayer().getX() + 32) / 16) + ((game.level.getPlayer().getY() + 16) / 16) * (game.level.getWidth() / 16)];
				if(!t.isSolid()) game.level.setX(game.level.getX() - speed);
				//game.level.player.sprite = Sprites.player[0][2];
			}
			if(left) {
				t = game.level.getTiles()[((game.level.getPlayer().getX()) / 16) + ((game.level.getPlayer().getY() + 16) / 16) * (game.level.getWidth() / 16)];
				if(!t.isSolid()) game.level.setX(game.level.getX() + speed);
				//game.level.player.sprite = Sprites.player[0][3];
			}
			if(up) {
				t = game.level.getTiles()[((game.level.getPlayer().getX() + 16) / 16) + ((game.level.getPlayer().getY()) / 16) * (game.level.getWidth() / 16)];
				if(!t.isSolid()) game.level.setY(game.level.getY() + speed);
				//game.level.player.sprite = Sprites.player[0][1];
			}
			if(down) {
				t = game.level.getTiles()[((game.level.getPlayer().getX() + 16) / 16) + ((game.level.getPlayer().getY() + 32) / 16) * (game.level.getWidth() / 16)];
				if(!t.isSolid()) game.level.setY(game.level.getY() - speed);
				//game.level.player.sprite = Sprites.player[0][0];
			}
			if(one) {
				game.level.getHotbar().selection = 0;
			}
			if(two) {
				game.level.getHotbar().selection = 1;
			}
			if(three) {
				game.level.getHotbar().selection = 2;
			}
			if(four) {
				game.level.getHotbar().selection = 3;
			}
		}
		
		if(esc) {
			if(!escape) {
				escapeDown = System.nanoTime();
				double passedTime = (escapeDown - lastEscapeDown) / 1000000000.0;
				if(passedTime >= 0.1) {
					lastEscapeDown = escapeDown;
					escape = true;
					game.setPaused(!game.isPaused());
					System.out.println(game.isPaused());
					if(game.isPaused()) {
						game.menu = new PauseMenu(game);
					} else {
						game.menu = null;
					}
				}
			}
		}
		if(!esc) {
			escape = false;
		}
	}
}
