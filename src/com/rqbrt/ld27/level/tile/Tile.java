package com.rqbrt.ld27.level.tile;

import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gfx.Sprite;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.entity.Entity;

public class Tile {
	
	protected Level level;
	protected String name;
	protected int x, y, width, height;
	protected Sprite sprite;
	protected int brightness = 0;
	
	protected Entity pickup;
	protected boolean hasPickup = false;
	
	protected boolean solid = false;
	protected Entity entity;
	protected boolean occupied = false;
	
	protected boolean zombieSpawn = false;
	
	protected int pathValue = 0;
	
	public Tile(Level level, int x, int y, int width, int height, Sprite sprite) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	public void tick() {
		occupied = false;
		if(!occupied) {
			entity = null;
		}
		if(pickup != null) {
			hasPickup = true;
		} else {
			hasPickup = false;
		}
	}
	
	public void render(Render render) {
		render.drawSprite(sprite, level.getX() + x, level.getY() + y, brightness);
		if(pickup != null) render.drawSprite(pickup.getSprite(), level.getX() + x, level.getY() + y);
		//assaarender.drawString(""+pathValue, level.getX() + x, level.getY() + y);
	}
	
	public Entity getPickup() {
		return pickup;
	}
	
	public void setPickup(Entity e) {
		this.pickup = e;
	}
	
	public boolean hasPickup() {
		return hasPickup;
	}
	
	public void setHasPickup(boolean b) {
		hasPickup = b;
	}
	
	public String getName() {
		return name;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setPathValue(int value) {
		pathValue = value;
	}
	
	public int getPathValue() {
		return pathValue;
	}
	
	public void setEntity(Entity e) {
		this.entity = e;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public void setOccupied(boolean b) {
		occupied = b;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public boolean isZombieSpawn() {
		return zombieSpawn;
	}
	
	public void setZombieSpawn(boolean b) {
		zombieSpawn = b;
	}
	
	@Override
	public String toString() {
		return name + " (" + x + ", " + y + ")";
	}
}
