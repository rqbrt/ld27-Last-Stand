package com.rqbrt.ld27.gui;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.entity.weapon.Grenade;
import com.rqbrt.ld27.level.entity.weapon.Pistol;
import com.rqbrt.ld27.level.entity.weapon.Shotgun;
import com.rqbrt.ld27.level.entity.weapon.Uzi;
import com.rqbrt.ld27.level.entity.weapon.Weapon;

public class AmmoIndicator extends GUIElement {

	private int ammo = 0;
	
	public AmmoIndicator(Level level) {
		super(level);
	}

	public void tick(int ticks) {
		Weapon w = level.getPlayer().getWeapon();
		if(w instanceof Pistol) {
			ammo = level.getPlayer().getPistolAmmo();
		} else if(w instanceof Grenade) {
			ammo = level.getPlayer().getGrenades();
		} else if(w instanceof Uzi) {
			ammo = level.getPlayer().getUziAmmo();
		} else if(w instanceof Shotgun) {
			ammo = level.getPlayer().getShotgunAmmo();
		}
	}
	
	public void render(Render render) {
		render.drawString("Ammo:" + ammo, ((Game.width / Game.scale) / 2) + 85, (Game.height / Game.scale) - 8);
	}
}
