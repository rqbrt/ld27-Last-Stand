package com.rqbrt.ld27.level.entity.weapon;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.entity.Bomb;

public class Grenade extends Weapon {

	public Grenade(Level level, int x, int y) {
		super(level, x, y);
		automatic = false;
		fireRate = 1.0;
		range = 4;
		sprite = Sprites.entities[2][0];
		name = "Grenade";
		ammo = 1;
	}
	
	public void fire(int x, int y, double angle, int damage) {
		//super.fire(x, y, angle);
		if(level.getPlayer().getGrenades() > 0) {
			level.getEntities().add(new Bomb(level, level.getPlayer().getX(), level.getPlayer().getY(), angle, range));
			level.getPlayer().setGrenades(level.getPlayer().getGrenades() - 1);
		}
	}
}
