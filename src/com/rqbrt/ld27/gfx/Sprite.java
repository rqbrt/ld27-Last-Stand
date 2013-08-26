package com.rqbrt.ld27.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	public int w, h;
	public int[] pixels;
	
	public Sprite(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}
	
	public static class Sprites {
		
		public static Sprite[][] tiles = loadSpriteSheet(16, 16, "/sprites/tiles.png");
		public static Sprite[][] entities = loadSpriteSheet(16, 16, "/sprites/entities.png");
		public static Sprite[][] player = loadSpriteSheet(32, 32, "/sprites/player.png");
		public static Sprite[][] font = loadSpriteSheet(8, 8, "/sprites/font.png");
		public static Sprite[][] explosion = loadSpriteSheet(16, 16, "/sprites/explosion.png");
		public static Sprite[][] zombie = loadSpriteSheet(32, 32, "/sprites/zombie.png");
		
		public static Sprite[][] loadSpriteSheet(int w, int h, String path) {
			try {
				BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
				int xTotal = image.getWidth() / w;
				int yTotal = image.getHeight() / h;
				Sprite[][] result = new Sprite[xTotal][yTotal];
				for(int y = 0; y < yTotal; y++) {
					for(int x = 0; x < xTotal; x++) {
						result[x][y] = new Sprite(w, h);
						image.getRGB(x * w, y * h, w, h, result[x][y].pixels, 0, w);
					}
				}
				return result;
			} catch(IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
