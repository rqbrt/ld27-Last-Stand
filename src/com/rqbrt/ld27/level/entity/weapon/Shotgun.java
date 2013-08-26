package com.rqbrt.ld27.level.entity.weapon;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;

public class Shotgun extends Weapon {

	public Shotgun(Level level, int x, int y) {
		super(level, x, y);
		fireRate = 0.8;
		sprite = Sprites.entities[5][0];
		name = "Shotgun";
		ammo = 10;
		damage = 10;
	}
	
	public void fire(int x, int y, double angle, int damage) {
		if(level.getPlayer().getShotgunAmmo() > 0) {
			double degrees = Math.toDegrees(angle);
			super.fire(x, y, angle, damage);
			super.fire(x, y, Math.toRadians(degrees + 15), damage);
			super.fire(x, y, Math.toRadians(degrees + 30), damage);
			super.fire(x, y, Math.toRadians(degrees - 15), damage);
			super.fire(x, y, Math.toRadians(degrees - 30), damage);
			level.getPlayer().setShotgunAmmo(level.getPlayer().getShotgunAmmo() - 1);
		}
	}
}
