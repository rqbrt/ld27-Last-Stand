package com.rqbrt.ld27.gfx;

import com.rqbrt.ld27.level.entity.Entity;

public class Animation {
	
	private Entity e;
	private Sprite[] frames;
	private int frame = 0;
	private boolean loop = true;
	private boolean finished = false;
	
	public Animation(Entity e, Sprite[] frames) {
		this.e = e;
		this.frames = frames;
	}
	
	public void tick() {
		if(!finished) {
			try {
				frame++;
				e.setSprite(frames[frame]);
			} catch(ArrayIndexOutOfBoundsException e) {
				if(loop) {
					frame = 0;
					this.e.setSprite(frames[frame]);
				} else {
					finished = true;
				}
				return;
			}
		}
	}
	
	public void setFrames(Sprite[] frames) {
		this.frames = frames;
	}
	
	public Sprite[] getFrames() {
		return frames;
	}
	
	public void setLooping(boolean loop) {
		this.loop = loop;
	}
	
	public boolean isLooping() {
		return loop;
	}
	
	public void stop() {
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}
}
