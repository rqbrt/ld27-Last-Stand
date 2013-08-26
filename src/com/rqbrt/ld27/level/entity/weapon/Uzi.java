package com.rqbrt.ld27.level.entity.weapon;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;

public class Uzi extends Weapon {

	public Uzi(Level level, int x, int y) {
		super(level, x, y);
		fireRate = 0.1;
		sprite = Sprites.entities[4][0];
		name = "Uzi";
		ammo = 30;
		damage = 3;
	}
	
	public void fire(int x, int y, double angle, int damage) {
		if(level.getPlayer().getUziAmmo() > 0) {
			super.fire(x, y, angle, damage);
			level.getPlayer().setUziAmmo(level.getPlayer().getUziAmmo() - 1);
		}
	}
}
