package com.rqbrt.ld27.gui;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.level.Level;

public class HealthIndicator extends GUIElement {

	public int health = 10;
	
	public HealthIndicator(Level level) {
		super(level);
	}
	
	public void tick(int ticks) {
		health = level.getPlayer().getHealth();
	}
	
	public void render(Render render) {
		render.drawString("Health:" + health, 0, (Game.height / Game.scale) - 8);
	}
}
