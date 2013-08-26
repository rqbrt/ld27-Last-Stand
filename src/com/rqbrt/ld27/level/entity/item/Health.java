package com.rqbrt.ld27.level.entity.item;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.sfx.Sound;
import com.rqbrt.ld27.sfx.SoundPlayer;

public class Health extends Item {

	public Health(Level level, int x, int y) {
		super(level, x, y);
		sprite = Sprites.entities[1][1];
	}
	
	public void powerUp() {
		System.out.println("+5 Health");
		level.getPlayer().addHealth(5);
		SoundPlayer.playSound(Sound.PICKUP);
	}
}
