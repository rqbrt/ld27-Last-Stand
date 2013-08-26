package com.rqbrt.ld27.gui.menu;

import java.awt.Rectangle;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Render;

public class PauseMenu extends Menu {
	
	private Rectangle resume, exit;
	private boolean resumeHovered, exitHovered;

	public PauseMenu(Game game) {
		super(game);
		
		width = 200;
		height = 50;
		x = ((Game.width / Game.scale) - width) / 2;
		y = ((Game.height / Game.scale) - height) / 2;
		
		resume = new Rectangle(x + 8, y + height - 16, 56, 16);
		exit = new Rectangle(x + width - 40, y + height - 16, 40, 16);
	}
	
	public void tick(int ticks) {
		Rectangle mouse = game.input.mousePos;
		resumeHovered = mouse.intersects(resume);
		exitHovered = mouse.intersects(exit);
		
		if(game.input.mouseDown) {
			if(resumeHovered) {
				game.menu = null;
				game.setPaused(false);
			}
			
			if(exitHovered) {
				game.level = null;
				game.setPaused(false);
				game.menu = new MainMenu(game);
			}
		}
	}
	
	public void render(Render render) {
		render.drawTransparentBox(x, y, width, height, -100);
		render.drawString("Paused", x + 74, y + 8);
		
		render.drawString("Resume", resume.x, resume.y);
		render.drawString("Exit", exit.x, exit.y);
		
		if(resumeHovered) render.drawHollowBox(resume.x - 4, resume.y - 4, resume.width, resume.height, 0xffffff);
		if(exitHovered) render.drawHollowBox(exit.x - 4, exit.y - 4, exit.width, exit.height, 0xffffff);
	}
}
