package com.rqbrt.ld27.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.rqbrt.ld27.Game;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gui.AmmoIndicator;
import com.rqbrt.ld27.gui.GUIElement;
import com.rqbrt.ld27.gui.HealthIndicator;
import com.rqbrt.ld27.gui.Hotbar;
import com.rqbrt.ld27.level.entity.Entity;
import com.rqbrt.ld27.level.entity.Explosion;
import com.rqbrt.ld27.level.entity.Player;
import com.rqbrt.ld27.level.entity.Zombie;
import com.rqbrt.ld27.level.entity.item.Ammo;
import com.rqbrt.ld27.level.entity.item.Health;
import com.rqbrt.ld27.level.entity.weapon.Grenade;
import com.rqbrt.ld27.level.entity.weapon.Pistol;
import com.rqbrt.ld27.level.entity.weapon.Shotgun;
import com.rqbrt.ld27.level.entity.weapon.Uzi;
import com.rqbrt.ld27.level.tile.Floor;
import com.rqbrt.ld27.level.tile.Tile;
import com.rqbrt.ld27.level.tile.Wall;
import com.rqbrt.ld27.sfx.Sound;
import com.rqbrt.ld27.sfx.SoundPlayer;

public class Level {
	
	private Game game;
	private int x, y, width, height;
	private Tile[] tiles;
	private ArrayList<Entity> entities;
	private ArrayList<Tile> zombieSpawnTiles;
	private ArrayList<Tile> zombieSpawns;
	private ArrayList<Zombie> zombies;
	private Hotbar hotbar;
	private ArrayList<GUIElement> gui;
	private Player player;
	private PathMap pathMap;
	private boolean gameOver = false;
	
	private int wave = 0;
	private boolean alotOfZombies = false;
	private int total = wave * 10;
	private int zombieHealth = 10;
	
	private boolean intermission = true;
	private int passedTime;
	
	private int score = 0;
	
	public Level(Game game, String path) {
		this.game = game;
		try {
			BufferedImage image = ImageIO.read(Level.class.getResource(path));
			width = image.getWidth() * 16;
			height = image.getHeight() * 16;
			
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			
			tiles = new Tile[image.getWidth() * image.getHeight()];
			entities = new ArrayList<Entity>();
			zombieSpawnTiles = new ArrayList<Tile>();
			zombieSpawns = new ArrayList<Tile>();
			
			gui = new ArrayList<GUIElement>();
			hotbar = new Hotbar(this);
			gui.add(hotbar);
			gui.add(new HealthIndicator(this));
			gui.add(new AmmoIndicator(this));
			
			int tileX = 0, tileY = 0;
			for(int i = 0; i < pixels.length; i++) {
				if(tileX >= width) {
					tileX = 0;
					tileY += 16;
				}
				if(pixels[i] == 0xffc0c0c0) {
					tiles[i] = new Floor(this, tileX, tileY, 16, 16);
				} else if(pixels[i] == 0xFF606060) {
					tiles[i] = new Wall(this, tileX, tileY, 16, 16);
				} else if(pixels[i] == 0xFF4CFF00) {
					tiles[i] = new Floor(this, tileX, tileY, 16, 16);
					tiles[i].setPickup(generateRandomPickup(tileX, tileY));
				} else if(pixels[i] == 0xFFFF0000) {
					tiles[i] = new Floor(this, tileX, tileY, 16, 16);
					tiles[i].setZombieSpawn(true);
					zombieSpawnTiles.add(tiles[i]);
				} else if(pixels[i] == 0xFF0094FF) {
					tiles[i] = new Floor(this, tileX, tileY, 16, 16);
					x = -tileX;
					y = -tileY;
				}
				tileX += 16;
			}
			
			player = new Player(game, this, ((Game.width / Game.scale) - 16) / 2, ((Game.height / Game.scale) - 16) / 2);
			entities.add(player);
			
			pathMap = new PathMap(this, tiles);
			pathMap.refreshMap();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Entity generateRandomPickup(int tileX, int tileY) {
		Entity pickup = null;
		Random rand = new Random();
		int type = rand.nextInt(3);
		if(type == 0) {
			int weaponType = rand.nextInt(4);
			if(weaponType == 0) pickup = new Pistol(this, tileX, tileY);
			if(weaponType == 1) pickup = new Grenade(this, tileX, tileY);
			if(weaponType == 2) pickup = new Uzi(this, tileX, tileY);
			if(weaponType == 3) pickup = new Shotgun(this, tileX, tileY);
		} else if(type == 1) {
			pickup = new Ammo(this, tileX, tileY);
		} else if(type == 2) {
			pickup = new Health(this, tileX, tileY);
		}
		return pickup;
	}
	
	int i = 0;
	public void tick(int ticks) {
		if(zombies == null || zombies.size() == 0) {
			intermission = true;
		}
		
		if(!intermission && alotOfZombies) {
			if(zombies.size() < 50) {
				spawnNextBatch();
			}
		}
		
		if(intermission) {
			if(ticks % 60 == 0) passedTime++;
			if(passedTime >= 10) {
				passedTime = 0;
				intermission = false;
				wave++;
				spawnZombies();
			}
		}
		if(player.getTile() != player.getPreviousTile()) {
			refreshPathMap();
		}
		for(Tile t : tiles) {
			if(t != null) t.tick();
		}
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i) != null) entities.get(i).tick(ticks);
		}
		for(GUIElement e : gui) {
			e.tick(ticks);
		}
	}
	
	private void spawnZombies() {
		zombieSpawns = zombieSpawnTiles;
		zombies = new ArrayList<Zombie>();
		total = wave * 10;
		zombieHealth = 10;
		if(total > zombieSpawns.size()) {
			total = zombieSpawns.size();
			//make them harder
			zombieHealth += wave / 10;
		}
		Tile t;
		Zombie z;
		Random rand = new Random();
		if(total < 100) {
			alotOfZombies = false;
			for(int i = 0; i < total; i++) {
				int index = rand.nextInt(zombieSpawns.size());
				t = zombieSpawns.get(index);
				z = new Zombie(this, t.getX(), t.getY());
				z.setHealth(zombieHealth);
				entities.add(z);
				zombies.add(z);
				t.setEntity(z);
				zombieSpawns.remove(index);
			}
		} else {
			alotOfZombies = true;
			for(int i = 0; i < 100; i++) {
				int index = rand.nextInt(zombieSpawns.size());
				t = zombieSpawns.get(index);
				z = new Zombie(this, t.getX(), t.getY());
				z.setHealth(zombieHealth);
				entities.add(z);
				zombies.add(z);
				t.setEntity(z);
				zombieSpawns.remove(index);
			}
			total -= 100;
		}
	}
	
	public void spawnNextBatch() {
		System.out.println("next batch");
		Tile t;
		Random rand = new Random();
		Zombie z;
		if(total < 100) {
			alotOfZombies = false;
			for(int i = 0; i < total; i++) {
				int index = rand.nextInt(zombieSpawns.size());
				t = zombieSpawns.get(index);
				z = new Zombie(this, t.getX(), t.getY());
				z.setHealth(zombieHealth);
				entities.add(z);
				zombies.add(z);
				t.setEntity(z);
				zombieSpawns.remove(index);
			}
		} else {
			alotOfZombies = true;
			for(int i = 0; i < 100; i++) {
				int index = rand.nextInt(zombieSpawns.size());
				t = zombieSpawns.get(index);
				z = new Zombie(this, t.getX(), t.getY());
				z.setHealth(zombieHealth);
				entities.add(z);
				zombies.add(z);
				t.setEntity(z);
				zombieSpawns.remove(index);
			}
			total -= 100;
		}
	}
	
	public void render(Render render) {
		if(tiles != null) {
			for(Tile t : tiles) {
				if(t != null) t.render(render);
			}
		}
		
		if(entities != null) {
			for(int i = 0; i < entities.size(); i++) {
				if(entities.get(i) != null) {
					entities.get(i).render(render);
				}
			}
		}
		
		player.render(render);
		
		for(GUIElement e : gui) {
			e.render(render);
		}
		render.drawString("Wave " + wave, 0, 0);
		if(intermission) render.drawString("Time until next wave: " + Math.abs(passedTime - 10), 0, 16);
	}
	
	private void refreshPathMap() {
		pathMap.refreshMap();
	}
	
	public void createExplosion(int xPos, int yPos, int blastRadius) {
		entities.add(new Explosion(this, xPos, yPos));
		SoundPlayer.playSound(Sound.EXPLOSION);
		Tile t = tiles[(xPos / 16) + (yPos / 16) * (width / 16)];
		ArrayList<Tile> blastTiles = getTilesWithinBlastRadius(t, blastRadius);
		
		for(Tile bt : blastTiles) {
			if(bt.isOccupied()) {
				if(bt.getEntity() instanceof Zombie) {
					Zombie z = (Zombie) bt.getEntity();
					z.setHealth(z.getHealth() - 10);
				}
			}
		}
	}
	
	private ArrayList<Tile> getTilesWithinBlastRadius(Tile origin, int radius) {
		ArrayList<Tile> blastTiles = new ArrayList<Tile>();
		for(int i = 1; i < radius + 1; i++) {
			blastTiles.add(tiles[(origin.getX() / 16) + ((origin.getY() + i * 16) / 16) * (width / 16)]); //up
			blastTiles.add(tiles[((origin.getX() - i * 16) / 16) + ((origin.getY() + i * 16) / 16) * (width / 16)]); //up-left
			blastTiles.add(tiles[((origin.getX() + i * 16) / 16) + ((origin.getY() + i * 16) / 16) * (width / 16)]); //up-right
			blastTiles.add(tiles[(origin.getX() / 16) + ((origin.getY() - i * 16) / 16) * (width / 16)]); //down
			blastTiles.add(tiles[((origin.getX() - i * 16) / 16) + ((origin.getY() - i * 16) / 16) * (width / 16)]); //down-left
			blastTiles.add(tiles[((origin.getX() + i * 16) / 16) + ((origin.getY() - i * 16) / 16) * (width / 16)]); //down-right
			blastTiles.add(tiles[((origin.getX() - i * 16) / 16) + (origin.getY() / 16) * (width / 16)]); //left
			blastTiles.add(tiles[((origin.getX() + i * 16) / 16) + (origin.getY() / 16) * (width / 16)]); //right
		}
		return blastTiles;
	}
	
	public void mouseWheelMoved(int dir) {
		hotbar.selection += dir;
		if(hotbar.selection > 3) {
			hotbar.selection = 0;
		}
		if(hotbar.selection < 0) {
			hotbar.selection = 3;
		}
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
		if(e instanceof Zombie) {
			zombies.remove(e);
		}
	}
	
	public int getWave() {
		return wave;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
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
	
	public Game getGame() {
		return game;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public Hotbar getHotbar() {
		return hotbar;
	}
	
	public void setGameOver(boolean b) {
		 gameOver = b;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	/*public void shootProjectile(Rectangle mouse, int ticks) {
		if(ticks % 4 == 0) {
			int xDiff = mouse.x - Game.width / 2;
			int yDiff = mouse.y - Game.height / 2;
			double angle = Math.atan2(yDiff, xDiff);
			//System.out.println(Math.toDegrees(angle));
			int xOffs = 8, yOffs = 8;
			
			double degrees = Math.toDegrees(angle);
			player.dir = 0b0000;
			if(degrees <= -50 && degrees > -120) player.dir = 0b0001;
			if(degrees <= -120 && degrees > -179 || degrees <= 179 && degrees > 120) player.dir = 0b0100;
			if(degrees <= 120 && degrees > 50) player.dir = 0b0010;
			if(degrees <= 50 && degrees >= 0 || degrees <= 0 && degrees > -50) player.dir = 0b1000;
			
			if(player.dir == 0b0001 || player.dir == 0b1001 || player.dir == 0b0101) sprite = Sprites.player[0][1];
			if(player.dir == 0b0010 || player.dir == 0b1010 || player.dir == 0b0110) sprite = Sprites.player[0][0];
			if(player.dir == 0b0100) sprite = Sprites.player[0][3];
			if(player.dir == 0b1000) sprite = Sprites.player[0][2];
			
			entities.add(new Projectile(this, player.x + xOffs, player.y + yOffs, angle));
		}
	}*/
}
