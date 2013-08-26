package com.rqbrt.ld27.gfx;

import com.rqbrt.ld27.gfx.Sprite.Sprites;

public class Render {
	
	private int width, height;
	private int[] pixels;
	
	private String chars = "abcdefghijklmnopqrstuvwxyz0123456789.! -:";
	
	public Render(int[] pixels, int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void drawBox(int xOffs, int yOffs, int w, int h, int color) {
		for(int y = 0; y < h; y++) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= height) continue;
			for(int x = 0; x < w; x++) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= width) continue;
				pixels[xPix + yPix * width] = color;
			}
		}
	}
	
	public void drawHollowBox(int xOffs, int yOffs, int w, int h, int color) {
		for(int y = 0; y < h; y++) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= height) continue;
			for(int x = 0; x < w; x++) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= width) continue;
				if(x == 0 || x == w - 1 || y == 0 || y == h - 1) {
					pixels[xPix + yPix * width] = color;
				}
			}
		}
	}
	
	public void drawTransparentBox(int xOffs, int yOffs, int w, int h, int brightness) {
		for(int y = 0; y < h; y++) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= height) continue;
			for(int x = 0; x < w; x++) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= width) continue;
				
				int color = pixels[xPix + yPix * width];
				int r = (color & 0xff0000) >> 16;
				int g = (color & 0xff00) >> 8;
				int b = (color & 0xff);
				
				r += brightness;
				g += brightness;
				b += brightness;
				
				if(r < 0) r = 0;
				if (r > 255) r = 255;
				if(g < 0) g = 0;
				if (g > 255) g = 255;
				if(b < 0) b = 0;
				if (b > 255) b = 255;
				
				color = r << 16 | g << 8 | b;
				
				pixels[xPix + yPix * width] = color;
			}
		}
	}
	
	public void drawSprite(Sprite sprite, int xOffs, int yOffs) {
		for(int y = 0; y < sprite.h; y++) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= height) continue;
			for(int x = 0; x < sprite.w; x++) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= width) continue;
				
				int color = sprite.pixels[x + y * sprite.w];
				if(color != -65281) {
					pixels[xPix + yPix * width] = color;
				}
			}
		}
	}
	
	public void drawSprite(Sprite sprite, int xOffs, int yOffs, int brightness) {
		for(int y = 0; y < sprite.h; y++) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= height) continue;
			for(int x = 0; x < sprite.w; x++) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= width) continue;
				
				int color = sprite.pixels[x + y * sprite.w];
				if(color != -65281) {
					int r = (color & 0xff0000) >> 16;
					int g = (color & 0xff00) >> 8;
					int b = (color & 0xff);
					
					r += brightness;
					g += brightness;
					b += brightness;
					
					if(r < 0) r = 0;
					if (r > 255) r = 255;
					if(g < 0) g = 0;
					if (g > 255) g = 255;
					if(b < 0) b = 0;
					if (b > 255) b = 255;
					
					color = r << 16 | g << 8 | b;
					
					pixels[xPix + yPix * width] = color;
				}
			}
		}
	}
	
	public void drawString(String s, int xOffs, int yOffs) {
		s = s.toLowerCase();
		int spacing = 8;
		for(int i = 0; i < s.length(); i++) {
			int c = chars.indexOf(s.charAt(i));
			if(c >= 0) {
				int x = c % (208 / 8);
				int y = c / (208 / 8);
				
				drawSprite(Sprites.font[x][y], xOffs + i * spacing, yOffs);
			}
		}
	}
}
