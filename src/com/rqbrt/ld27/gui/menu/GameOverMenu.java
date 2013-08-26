package com.rqbrt.ld27.gui.menu;

import java.awt.Rectangle;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.level.Level;

public class GameOverMenu extends Menu {
	
	private String infected = "You have been infected!";
	private String waves = "Waves: " + game.level.getWave();
	private String score = "Score: " + game.level.getScore();
	private Rectangle restart, exit;
	private boolean restartHovered = false, exitHovered = false;
	
	public GameOverMenu(Game game) {
		super(game);
		
		width = 200;
		height = 100;
		x = ((Game.width / Game.scale) - width) / 2;
		y = ((Game.height / Game.scale) - height) / 2;
		
		restart = new Rectangle(x + 8, y + height - 16, 65, 16);
		exit = new Rectangle(x + width - ("exit".length() * 8) - 8, y + height - 16, 40, 16);
	}
	
	public void tick(int ticks) {
		Rectangle mouse = game.input.mousePos;
		restartHovered = mouse.intersects(restart);
		exitHovered = mouse.intersects(exit);
		
		if(game.input.mouseDown) {
			if(restartHovered) {
				game.level = new Level(game, "/level/level.png");
				game.menu = null;
			}
			if(exitHovered) {
				game.level = null;
				game.menu = new MainMenu(game);
			}
		}
	}
	
	public void render(Render render) {
		render.drawTransparentBox(x, y, width, height, -100);
		render.drawString(infected, x + (infected.length() * 8) / 16, y + 8);
		render.drawString(waves, x + 8, y + 24);
		render.drawString(score, x + 8, y + 40);
		
		render.drawString("Restart", restart.x, restart.y);
		render.drawString("Exit", exit.x, exit.y);
		
		if(restartHovered) render.drawHollowBox(restart.x - 4, restart.y - 4, restart.width, restart.height, 0xffffff);
		if(exitHovered) render.drawHollowBox(exit.x - 4, exit.y - 4, exit.width, exit.height, 0xffffff);		
	}
}
