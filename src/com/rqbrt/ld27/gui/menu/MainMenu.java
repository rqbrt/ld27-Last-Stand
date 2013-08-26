package com.rqbrt.ld27.gui.menu;

import java.awt.Rectangle;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.level.Level;

public class MainMenu extends Menu {
	
	private String lastStand = "Last Stand";
	private String sStart = "Start";
	
	private boolean startHovered = false;
	private Rectangle start;

	public MainMenu(Game game) {
		super(game);
		
		width = 200;
		height = 200;
		x = ((Game.width / Game.scale) - width) / 2;
		y = ((Game.height / Game.scale) - height) / 2;
		
		start = new Rectangle(x + (sStart.length() * 8) + 40, y + 64, 50, 16);
	}
	
	public void tick(int ticks) {
		Rectangle mouse = game.input.mousePos;
		if(mouse != null) {
			startHovered = mouse.intersects(start);
			
			if(game.input.mouseDown) {
				if(startHovered) {
					game.level = new Level(game, "/level/level.png");
					game.menu = null;
				}
			}
		}
	}
	
	public void render(Render render) {
		render.drawString(lastStand, x + (lastStand.length() * 8) - 20, y + 16);
		render.drawString(sStart, start.x, start.y);
		render.drawString("WASD to move", start.x - 28, start.y + 64);
		render.drawString("Mouse to shoot", start.x - 36, start.y + 80);
		render.drawString("Mouse wheel to select weapon", x - 4, start.y + 96);
		render.drawString("Or Numbers 1-4 to select weapon", x - 20, start.y + 112);
		
		if(startHovered) render.drawHollowBox(start.x - 4, start.y - 4, start.width, start.height, 0xffffff);
	}
}
