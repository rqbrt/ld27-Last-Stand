package com.rqbrt.ld27.sfx;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
	
	public static synchronized void playSound(final String path) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream stream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource(path));
					clip.open(stream);
					clip.start();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
