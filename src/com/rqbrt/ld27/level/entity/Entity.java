package com.rqbrt.ld27.level.entity;

import java.awt.Rectangle;

import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gfx.Sprite;
import com.rqbrt.ld27.level.Level;

public class Entity {

	protected Level level;
	protected int x, y, width, height;
	protected Sprite sprite;
	protected Rectangle bb;
	protected int health = 10;
	protected boolean killed = false;

	public Entity(Level level, int x, int y, int width, int height) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		bb = new Rectangle(x, y, width, height);
	}
	
	public void tick(int ticks) {
		bb = new Rectangle(level.getX() + x, level.getY() + y, width, height);
	}
	
	public void render(Render render) {
		render.drawSprite(sprite, level.getX() + x, level.getY() + y);
	}
	
	public void kill() {
		killed = true;
		level.removeEntity(this);
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
}
