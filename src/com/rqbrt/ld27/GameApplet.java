package com.rqbrt.ld27;

import java.applet.Applet;
import java.awt.BorderLayout;

public class GameApplet extends Applet {
	private static final long serialVersionUID = 2299165573162433505L;

	private Game game = new Game();
	
	public void init() {
		setLayout(new BorderLayout());
		add(game);
	}
	
	public void start() {
		game.start();
	}
	
	public void stop() {
		game.stop();
	}
}
