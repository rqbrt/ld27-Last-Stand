package com.rqbrt.ld27.level.entity;

import com.rqbrt.ld27.gfx.Animation;
import com.rqbrt.ld27.gfx.Render;
import com.rqbrt.ld27.gfx.Sprite;
import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;

public class Explosion extends Entity {
	
	private Animation animation;
	private Sprite[] sprites;

	public Explosion(Level level, int x, int y) {
		super(level, x, y, 16, 16);
		sprites = new Sprite[8];
		
		sprites[0] = Sprites.explosion[0][0];
		sprites[1] = Sprites.explosion[1][0];
		sprites[2] = Sprites.explosion[2][0];
		sprites[3] = Sprites.explosion[3][0];
		sprites[4] = Sprites.explosion[4][0];
		sprites[5] = Sprites.explosion[5][0];
		sprites[6] = Sprites.explosion[6][0];
		sprites[7] = Sprites.explosion[7][0];
		
		sprite = sprites[0];
		animation = new Animation(this, sprites);
		animation.setLooping(false);
	}
	
	public void tick(int ticks) {
		if(!animation.isFinished()) {
			if(ticks % 2 == 0) animation.tick();
		} else {
			level.removeEntity(this);
		}
	}
	
	public void render(Render render) {
		render.drawSprite(sprite, level.getX() + x, level.getY() + y);
	}
}
