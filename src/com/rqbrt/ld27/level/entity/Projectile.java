package com.rqbrt.ld27.level.entity;

import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.tile.Tile;
import com.rqbrt.ld27.sfx.Sound;
import com.rqbrt.ld27.sfx.SoundPlayer;

public class Projectile extends Entity {

	private double angle;
	private int speed = 1;
	private int damage = 1;
	private double xVelocity, yVelocity;
	private Tile currentTile;
	
	public Projectile(Level level, int x, int y, double angle, int damage) {
		super(level, x, y, 16, 16);
		this.angle = angle;
		this.damage = damage;
		sprite = Sprites.entities[0][0];
	}
	
	public void tick(int ticks) {
		super.tick(ticks);
		xVelocity += speed * Math.cos(angle);
		yVelocity += speed * Math.sin(angle);
		
		x += xVelocity;
		y += yVelocity;
		
		if(x < 0 || x >= level.getWidth()) {
			level.removeEntity(this);
			return;
		}
		if(y < 0 || y >= level.getHeight()) {
			level.removeEntity(this);
			return;
		}
		
		try {
			currentTile = level.getTiles()[((x + 8) / 16) + ((y + 8) / 16) * (level.getWidth() / 16)];
			if(currentTile.isOccupied()) {
				Entity e = currentTile.getEntity();
				if(e instanceof Zombie) {
					if(bb.intersects(e.bb)) {
						e.setHealth(e.getHealth() - damage);
						SoundPlayer.playSound(Sound.ZOMBIE_HURT);
						level.removeEntity(this);
					}
				}
			}
			if(currentTile.isSolid()) {
				level.removeEntity(this);
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			return;
		}
	}
	
	public void render(Render render) {
		render.drawSprite(sprite, level.getX() + x, level.getY() + y);
		//render.drawHollowBox(bb.x, bb.y, bb.width, bb.height, 0xffffff);
	}
}
