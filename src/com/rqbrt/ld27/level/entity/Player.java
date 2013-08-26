package com.rqbrt.ld27.level.entity;

import java.awt.Rectangle;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Animation;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gfx.Sprite;
import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.gui.menu.GameOverMenu;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.level.entity.item.Ammo;
import com.rqbrt.ld27.level.entity.item.Health;
import com.rqbrt.ld27.level.entity.item.Item;
import com.rqbrt.ld27.level.entity.weapon.Grenade;
import com.rqbrt.ld27.level.entity.weapon.Pistol;
import com.rqbrt.ld27.level.entity.weapon.Shotgun;
import com.rqbrt.ld27.level.entity.weapon.Uzi;
import com.rqbrt.ld27.level.entity.weapon.Weapon;
import com.rqbrt.ld27.level.tile.Tile;
import com.rqbrt.ld27.sfx.Sound;
import com.rqbrt.ld27.sfx.SoundPlayer;

public class Player extends Entity {
	
	public Game game;
	private int tileID;
	private Tile t;
	private Tile prevTile;
	
	private int xPos, yPos;
	private int prevX, prevY;
	private byte dir = 0b0001;
	private byte prevDir;
	private boolean moving = false;
	private Animation animation;
	
	private Weapon weapon;
	private boolean shooting = false;
	
	private long lastShot = System.nanoTime();
	private long currentShot = System.nanoTime();
	
	private Sprite[] down = new Sprite[7];
	private Sprite[] up = new Sprite[7];
	private Sprite[] left = new Sprite[7];
	private Sprite[] right = new Sprite[7];
	
	private int pistolAmmo = 25;
	private int uziAmmo = 0;
	private int shotgunAmmo = 0;
	private int grenades = 0;
	
	public Player(Game game, Level level, int x, int y) {
		super(level, x, y, 32, 32);
		this.game = game;
		sprite = Sprites.player[0][0];
		weapon = (Weapon) level.getHotbar().selected;
		bb = new Rectangle(x, y, 32, 32);
		initSprites();
		
		animation = new Animation(this, down);
	}

	public void tick(int ticks) {
		if(health <= 0) {
			level.setGameOver(true);
			sprite = Sprites.zombie[0][0];
			level.getGame().menu = new GameOverMenu(level.getGame());
		}
		if(!level.isGameOver()) {
			prevTile = t;
			prevX = xPos;
			prevY = yPos;
			xPos = (-game.level.getX() + x);
			yPos = (-game.level.getY() + y);
			if(xPos != prevX || yPos != prevY) {
				moving = true;
			} else {
				moving = false;
			}
			
			weapon = (Weapon) level.getHotbar().selected;
			
			if(game.input.mouseDown) {
				shooting = true;
				shoot();
			} else {
				shooting = false;
			}
			
			prevDir = dir;
			if(!shooting) {
				dir = 0b0000;
				if(yPos == prevY && xPos == prevX) dir = prevDir;
				if(yPos < prevY) dir = (byte) (dir | 0b0001);
				if(yPos > prevY) dir = (byte) (dir | 0b0010);
				if(xPos < prevX) dir = (byte) (dir | 0b0100);
				if(xPos > prevX) dir = (byte) (dir | 0b1000);
			}
			
			if(dir == 0b0001 || dir == 0b1001 || dir == 0b0101) animation.setFrames(up);
			if(dir == 0b0010 || dir == 0b1010 || dir == 0b0110) animation.setFrames(down);
			if(dir == 0b0100) animation.setFrames(left);
			if(dir == 0b1000) animation.setFrames(right);
			
			tileID = ((xPos + 16) / 16) + ((yPos + 16) / 16) * (game.level.getWidth() / 16);
			t = level.getTiles()[tileID];
			checkPickup();
			//System.out.println(t);
			
			if(moving) {
				if(ticks % 4 == 0) animation.tick();
			}
		}
	}
	
	private void checkPickup() {
		if(t.hasPickup()) {
			Entity pickup = t.getPickup();
			if(pickup instanceof Item) {
				Item i = ((Item)t.getPickup());
				if(i instanceof Health) {
					if(health < 10) {
						i.powerUp();
						t.setPickup(null);
					} else {
						System.out.println("Full Health!");
					}
				} else if(i instanceof Ammo) {
					i.powerUp();
					t.setPickup(null);
				}
			} else if(pickup instanceof Weapon) {
				Weapon w = ((Weapon)t.getPickup());
				if(hasWeapon(w)) {
					//only add ammo
					if(w instanceof Pistol) {
						SoundPlayer.playSound(Sound.PICKUP);
						pistolAmmo += w.getAmmo();
						t.setPickup(null);
					} else if(w instanceof Grenade) {
						SoundPlayer.playSound(Sound.PICKUP);
						grenades += w.getAmmo();
						t.setPickup(null);
					} else if(w instanceof Uzi) {
						SoundPlayer.playSound(Sound.PICKUP);
						uziAmmo += w.getAmmo();
						t.setPickup(null);
					} else if(w instanceof Shotgun) {
						SoundPlayer.playSound(Sound.PICKUP);
						shotgunAmmo += w.getAmmo();
						t.setPickup(null);
					}
				} else {
					//take the gun
					if(!level.getHotbar().addWeapon(w)) {
						System.out.println("Inventory Full!");
					} else {
						SoundPlayer.playSound(Sound.PICKUP);
					}
				}
			}
		}
	}
	
	private boolean hasWeapon(Weapon w) {
		return level.getHotbar().hasWeapon(w);
	}
	
	public void render(Render render) {
		render.drawSprite(sprite, x, y);
		//render.drawHollowBox(bb.x, bb.y, 32, 32, 0xffffff);
		//render.drawHollowBox(x, y, 32, 32, 0xffffff);
		//render.drawHollowBox(level.getX() + t.getX(), level.getY() + t.getY(), 16, 16, 0xff);
	}
	
	public void shoot() {
		if(weapon != null) {
			if(weapon.getAmmo() > 0) {
				currentShot = System.nanoTime();
				double passed = (currentShot - lastShot) / 1000000000.0;
				if(passed >= weapon.getFireRate()) {
					lastShot = currentShot;
					int xDiff = game.input.mouse.x - Game.width / 2;
					int yDiff = game.input.mouse.y - Game.height / 2;
					double angle = Math.atan2(yDiff, xDiff);
					
					int xOffs = 8, yOffs = 8;
					double degrees = Math.toDegrees(angle);
					dir = 0b0000;
					if(degrees <= -50 && degrees > -120) dir = 0b0001;
					if(degrees <= -120 && degrees > -179 || degrees <= 179 && degrees > 120) dir = 0b0100;
					if(degrees <= 120 && degrees > 50) dir = 0b0010;
					if(degrees <= 50 && degrees >= 0 || degrees <= 0 && degrees > -50) dir = 0b1000;
					weapon.fire(xPos + xOffs, yPos + yOffs, angle, weapon.getDamage());
				}
			}
		}
	}
	
	public Tile getTile() {
		return t;
	}
	
	public Tile getPreviousTile() {
		return prevTile;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void addHealth(int health) {
		this.health += health;
		if(this.health > 10) {
			this.health = 10;
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public void addAllAmmo(int ammo) {
		pistolAmmo += ammo;
		uziAmmo += ammo;
	}
	
	public int getPistolAmmo() {
		return pistolAmmo;
	}
	
	public void setPistolAmmo(int ammo) {
		pistolAmmo = ammo;
	}
	
	public int getUziAmmo() {
		return uziAmmo;
	}
	
	public void setUziAmmo(int ammo) {
		uziAmmo = ammo;
	}
	
	public int getShotgunAmmo() {
		return shotgunAmmo;
	}
	
	public void setShotgunAmmo(int ammo) {
		shotgunAmmo = ammo;
	}
	
	public void setGrenades(int g) {
		grenades = g;
	}
	
	public int getGrenades() {
		return grenades;
	}
	
	private void initSprites() {
		down[0] = Sprites.player[0][0];
		down[1] = Sprites.player[1][0];
		down[2] = Sprites.player[2][0];
		down[3] = Sprites.player[3][0];
		down[4] = Sprites.player[4][0];
		down[5] = Sprites.player[5][0];
		down[6] = Sprites.player[6][0];
		
		up[0] = Sprites.player[0][1];
		up[1] = Sprites.player[1][1];
		up[2] = Sprites.player[2][1];
		up[3] = Sprites.player[3][1];
		up[4] = Sprites.player[4][1];
		up[5] = Sprites.player[5][1];
		up[6] = Sprites.player[6][1];
		
		right[0] = Sprites.player[0][2];
		right[1] = Sprites.player[1][2];
		right[2] = Sprites.player[2][2];
		right[3] = Sprites.player[3][2];
		right[4] = Sprites.player[4][2];
		right[5] = Sprites.player[5][2];
		right[6] = Sprites.player[6][2];
		
		left[0] = Sprites.player[0][3];
		left[1] = Sprites.player[1][3];
		left[2] = Sprites.player[2][3];
		left[3] = Sprites.player[3][3];
		left[4] = Sprites.player[4][3];
		left[5] = Sprites.player[5][3];
		left[6] = Sprites.player[6][3];
	}
}
