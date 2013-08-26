package com.rqbrt.ld27.gui;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.entity.Entity;
import com.rqbrt.ld27.level.entity.weapon.Pistol;
import com.rqbrt.ld27.level.entity.weapon.Weapon;

public class Hotbar extends GUIElement {
	
	public Entity[] entities;
	public int selection = 0;
	private int total = 0;
	public Entity selected;
	
	private int x, y, width, height;
	
	public Hotbar(Level level) {
		super(level);
		entities = new Entity[4];
		entities[0] = new Pistol(level, 0, 0);
		total++;
		selected = entities[0];
		
		width = 4 * 18;
		height = 18;
		
		x = ((Game.width / Game.scale) - width) / 2;
		y = (Game.height / Game.scale) - height;
	}
	
	public void tick(int ticks) {
		selected = entities[selection];
	}
	
	public void render(Render render) {
		render.drawTransparentBox(x, y, width, height, -125);
		for(int i = 0; i < entities.length; i++) {
			render.drawHollowBox(x + i * 18, y, 18, 18, 0x666666);
			if(entities[i] != null) render.drawSprite(entities[i].getSprite(), x + i * 18, y);
		}
		render.drawHollowBox(x + selection * 18, y, 18, 18, 0xffffff);
	}
	
	public boolean addWeapon(Weapon w) {
		try {
			entities[total] = w;
			total++;
			return true;
		} catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean hasWeapon(Weapon w) {
		for(int i = 0; i < entities.length; i++) {
			if(entities[i] instanceof Weapon) {
				if(w.getName().equals(((Weapon)entities[i]).getName())) {
					return true;
				}
			}
		}
		return false;
	}
}
