package com.rqbrt.ld27.level.entity.weapon;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;

public class Pistol extends Weapon {

	public Pistol(Level level, int x, int y) {
		super(level, x, y);
		fireRate = 0.3;
		sprite = Sprites.entities[1][0];
		name = "Pistol";
		ammo = 20;
		damage = 5;
	}
	
	public void fire(int x, int y, double angle, int damage) {
		if(level.getPlayer().getPistolAmmo() > 0) {
			super.fire(x, y, angle, damage);
			level.getPlayer().setPistolAmmo(level.getPlayer().getPistolAmmo() - 1);
		}
	}
}
