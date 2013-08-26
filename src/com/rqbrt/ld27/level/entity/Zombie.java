package com.rqbrt.ld27.level.entity;

import java.util.Random;

import com.rqbrt.ld27.gfx.Animation;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gfx.Sprite;
import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.tile.Tile;
import com.rqbrt.ld27.sfx.Sound;
import com.rqbrt.ld27.sfx.SoundPlayer;

public class Zombie extends Entity {
	
	private Tile t;
	private Tile destination;
	
	private int prevX, prevY;
	private int speed = 1;
	private byte dir = 0b0001;
	private byte prevDir;
	
	private double attackTime = 1;
	private long lastAttack = System.nanoTime();
	private long currentAttack = System.nanoTime();
	
	private boolean moving = false;
	private Animation animation;
	
	private Sprite[] down = new Sprite[7];
	private Sprite[] up = new Sprite[7];
	private Sprite[] left = new Sprite[7];
	private Sprite[] right = new Sprite[7];
	
	public Zombie(Level level, int x, int y) {
		super(level, x, y, 32, 32);
		sprite = Sprites.zombie[0][0];
		initSprites();
		
		animation = new Animation(this, down);
		
		t = level.getTiles()[(x / 16) + (y / 16) * (level.getWidth() / 16)];
	}
	
	public void tick(int ticks) {
		super.tick(ticks);
		if(health <= 0) {
			kill();
		}
		prevX  = x;
		prevY = y;
		t = level.getTiles()[((x + 16) / 16) + ((y + 16) / 16) * (level.getWidth() / 16)];
		t.setOccupied(true);
		t.setEntity(this);
		if(t.getPathValue() > 1) {
			if(destination == null || t == destination) {
				//update movement
				int[] direction = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};
				int[] ids = new int[]{
					(t.getX() / 16) + ((t.getY() - 16) / 16) * (level.getWidth() / 16), // up
					((t.getX() - 16) / 16) + ((t.getY() - 16) / 16) * (level.getWidth() / 16), //up left
					((t.getX() + 16) / 16) + ((t.getY() - 16) / 16) * (level.getWidth() / 16), //up right
					(t.getX() / 16) + ((t.getY() + 16) / 16) * (level.getWidth() / 16), //down
					((t.getX() - 16) / 16) + ((t.getY() + 16) / 16) * (level.getWidth() / 16), //down left
					((t.getX() + 16) / 16) + ((t.getY() + 16) / 16) * (level.getWidth() / 16), //down right
					((t.getX() - 16) / 16) + (t.getY() / 16) * (level.getWidth() / 16), // left
					((t.getX() + 16) / 16) + (t.getY() / 16) * (level.getWidth() / 16) // right
				};
				//above
				Tile tile = getTile(ids[0]);
				if (tile != null) direction[0] = tile.getPathValue();
				//above-left
				tile = getTile(ids[1]);
				if (tile != null) direction[1] = tile.getPathValue();
				//above-right
				tile = getTile(ids[2]);
				if (tile != null) direction[2] = tile.getPathValue();
				//below
				tile = getTile(ids[3]);
				if (tile != null) direction[3] = tile.getPathValue();
				//below-left
				tile = getTile(ids[4]);
				if (tile != null) direction[4] = tile.getPathValue();
				//below-right
				tile = getTile(ids[5]);
				if (tile != null) direction[5] = tile.getPathValue();
				//left
				tile = getTile(ids[6]);
				if (tile != null) direction[6] = tile.getPathValue();
				//right
				tile = getTile(ids[7]);
				if (tile != null) direction[7] = tile.getPathValue();
				
				//int smallest = 1;
				int index = 1;
				boolean found = false;
				for(int i = 0; i < direction.length; i++) {
					int current = direction[i];
					//System.out.println(current);
					if(current != -1 && current > 0) {
						//smallest = current;
						index = i;
						found = true;
						for(int j = 0; j < direction.length; j++) {
							int possible = direction[j];
							if(possible != -1 && possible > 0) {
								if(possible < current) {
									//smallest = possible;
									index = j;
									found = true;
								}
							} else {
								continue;
							}
						}
					} else {
						continue;
					}
				}
				if(!found) {
					destination = null;
					moving = false;
				} else {
					destination = level.getTiles()[ids[index]];
					moving = true;
				}
				//System.out.println("Found: " + found + " Index: " + index + " Smallest: " + smallest + " ID: " + ids[index]);
			} else {
				moving = true;
				if(destination.getX() > t.getX()) x += speed;
				if(destination.getX() < t.getX()) x -= speed;
				if(destination.getY() > t.getY()) y += speed;
				if(destination.getY() < t.getY()) y -= speed;
			}
		} else {
			moving = false;
		}
		
		prevDir = dir;
		dir = 0b0000;
		if(x == prevX && y == prevY) dir = prevDir;
		if(y < prevY) dir = (byte) (dir | 0b0001);
		if(y > prevY) dir = (byte) (dir | 0b0010);
		if(x < prevX) dir = (byte) (dir | 0b0100);
		if(x > prevX) dir = (byte) (dir | 0b1000);
		
		if(dir == 0b0001 || dir == 0b1001 || dir == 0b0101) animation.setFrames(up);
		if(dir == 0b0010 || dir == 0b1010 || dir == 0b0110) animation.setFrames(down);
		if(dir == 0b0100) animation.setFrames(left);
		if(dir == 0b1000) animation.setFrames(right);
		
		if(moving) {
			if(ticks % 10 == 0) {
				animation.tick();
			}
		}
		
		if(!level.isGameOver() && bb.intersects(level.getPlayer().bb)) {
			currentAttack = System.nanoTime();
			double passedTime = (currentAttack - lastAttack) / 1000000000.0;
			if(passedTime >= attackTime) {
				lastAttack = currentAttack;
				level.getPlayer().setHealth(level.getPlayer().getHealth() - 1);
				SoundPlayer.playSound(Sound.HURT);
			}
		}
	}
	
	public void kill() {
		killed = true;
		Random rand = new Random();
		if(rand.nextInt(4) == 0) {
			t.setPickup(level.generateRandomPickup(t.getX(), t.getY()));
		}
		level.removeEntity(this);
		level.setScore(level.getScore() + 10);
	}
	
	private Tile getTile(int id) {
		try {
			Tile t = level.getTiles()[id];
			if(t.isOccupied()) {
				return null;
			} else {
				return t;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void render(Render render) {
		render.drawSprite(sprite, level.getX() + x, level.getY() + y);
		//render.drawHollowBox(level.getX() + t.getX(), level.getY() + t.getY(), 16, 16, 0xff);
		//if(destination != null) render.drawHollowBox(level.getX() + destination.getX(), level.getY() + destination.getY(), 16, 16, 0xff0000);
		
		//render.drawHollowBox(bb.x, bb.y, bb.width, bb.height, 0xffffff);
	}
	
	private void initSprites() {
		down[0] = Sprites.zombie[0][0];
		down[1] = Sprites.zombie[1][0];
		down[2] = Sprites.zombie[2][0];
		down[3] = Sprites.zombie[3][0];
		down[4] = Sprites.zombie[4][0];
		down[5] = Sprites.zombie[5][0];
		down[6] = Sprites.zombie[6][0];
		
		up[0] = Sprites.zombie[0][1];
		up[1] = Sprites.zombie[1][1];
		up[2] = Sprites.zombie[2][1];
		up[3] = Sprites.zombie[3][1];
		up[4] = Sprites.zombie[4][1];
		up[5] = Sprites.zombie[5][1];
		up[6] = Sprites.zombie[6][1];
		
		right[0] = Sprites.zombie[0][2];
		right[1] = Sprites.zombie[1][2];
		right[2] = Sprites.zombie[2][2];
		right[3] = Sprites.zombie[3][2];
		right[4] = Sprites.zombie[4][2];
		right[5] = Sprites.zombie[5][2];
		right[6] = Sprites.zombie[6][2];
		
		left[0] = Sprites.zombie[0][3];
		left[1] = Sprites.zombie[1][3];
		left[2] = Sprites.zombie[2][3];
		left[3] = Sprites.zombie[3][3];
		left[4] = Sprites.zombie[4][3];
		left[5] = Sprites.zombie[5][3];
		left[6] = Sprites.zombie[6][3];
	}
	
	public int getHealth() {
		return health;
	}
}
