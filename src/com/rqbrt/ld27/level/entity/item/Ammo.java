package com.rqbrt.ld27.level.entity.item;

import com.rqbrt.ld27.gfx.Sprite.Sprites;
import com.rqbrt.ld27.level.Level;
import com.rqbrt.ld27.sfx.Sound;
import com.rqbrt.ld27.sfx.SoundPlayer;

public class Ammo extends Item {

	public Ammo(Level level, int x, int y) {
		super(level, x, y);
		sprite = Sprites.entities[2][1];
	}
	
	public void powerUp() {
		System.out.println("+10 Ammo for all guns");
		level.getPlayer().addAllAmmo(15);
		SoundPlayer.playSound(Sound.PICKUP);
	}
}
