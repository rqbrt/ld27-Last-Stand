package com.rqbrt.ld27.level.entity.weapon;

import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.entity.Entity;
import com.rqbrt.ld27.level.entity.Projectile;
import com.rqbrt.ld27.sfx.Sound;
import com.rqbrt.ld27.sfx.SoundPlayer;

public class Weapon extends Entity {
	
	protected String name = "weapon";
	protected double fireRate = 0.08;
	protected int damage = 1;
	protected int range;
	protected int ammo;
	protected boolean pickedUp = false;
	protected boolean automatic = true;

	public Weapon(Level level, int x, int y) {
		super(level, x, y, 16, 16);
	}
	
	public void tick() {
		
	}
	
	public void render(Render render) {
		if(!pickedUp) {
			render.drawSprite(sprite, level.getX() + x, level.getY() + y);
		}
	}
	
	public void fire(int x, int y, double angle, int damage) {
		level.getEntities().add(new Projectile(level, x, y, angle, damage));
		SoundPlayer.playSound(Sound.GUN);
	}
	
	public String getName() {
		return name;
	}
	
	public int getAmmo() {
		return ammo;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public double getFireRate() {
		return fireRate;
	}
}
