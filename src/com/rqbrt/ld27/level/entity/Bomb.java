package com.rqbrt.ld27.level.entity;

import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.tile.Tile;

public class Bomb extends Entity {
	
	private int startX, startY;
	private double angle;
	private double xVelocity, yVelocity;
	private int speed = 1;
	private Tile currentTile;
	private boolean canMove = true;
	private int range;
	
	private long startTime;
	private double timer = 1.0;

	public Bomb(Level level, int x, int y, double angle, int range) {
		super(level, x, y, 16, 16);
		startX = x;
		startY = y;
		this.angle = angle;
		this.range = range;
		sprite = Sprites.entities[3][0];
		startTime = System.nanoTime();
	}
	
	public void tick(int ticks) {
		double passedTime = (System.nanoTime() - startTime) / 1000000000.0;
		if(passedTime >= timer) {
			explode();
			return;
		}
		if(canMove) {
			xVelocity += speed * Math.cos(angle);
			yVelocity += speed * Math.sin(angle);
			
			x += xVelocity;
			y += yVelocity;
		}
		
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
			int xDif = Math.abs(currentTile.getX() - startX);
			int yDif = Math.abs(currentTile.getY() - startY);
			//System.out.println(xDif + " " + yDif + " " + 16 * range);
			if(xDif >= 16 * range || yDif >= 16 * range) canMove = false; 
			if(currentTile.isSolid()) {
				canMove = false;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			return;
		}
	}
	
	public void render(Render render) {
		super.render(render);
		//render.drawHollowBox(level.getX() + currentTile.getX(), level.getY() + currentTile.getY(), 16, 16, 0xffffff);
	}
	
	private void explode() {
		level.createExplosion(x, y, 3);
		level.removeEntity(this);
	}
}
