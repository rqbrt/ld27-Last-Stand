package com.rqbrt.ld27;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.rqbrt.ld27.gfx.Screen;
import com.rqbrt.ld27.gui.menu.MainMenu;
import com.rqbrt.ld27.gui.menu.Menu;
import com.rqbrt.ld27.gui.menu.PauseMenu;
import com.rqbrt.ld27.input.Controller;
import com.rqbrt.ld27.input.InputHandler;
import com.rqbrt.ld27.level.Level;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 2905542218766769495L;

	public static final int width = 640;
	public static final int height = 480;
	public static final int scale = 2;
	
	private BufferedImage image;
	private int[] pixels;
	
	private boolean running = false;
	private Thread thread;
	private boolean paused = false;
	
	public InputHandler input;
	private Controller controls;
	private Screen screen;
	
	public Level level;
	public Menu menu;
	
	public Game() {
		setPreferredSize(new Dimension(width, height));
		
		image = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		input = new InputHandler(this);
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		addMouseWheelListener(input);
		addFocusListener(input);
		
		controls = new Controller(this);
		screen = new Screen(this, pixels, width / scale, height / scale);
		
		menu = new MainMenu(this);
		//level = new Level(this, "/level/level.png");
	}
	
	public synchronized void start() {
		if(!running) {
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	@Override
	public void run() {
		int frames = 0;
		long beforeTime = System.nanoTime();
		double unprocessed = 0.0;
		double secondsPerTick = 1 / 60.0;
		int ticks = 0;
		
		requestFocus();
		while(running) {
			long currentTime = System.nanoTime();
			double passedTime = (currentTime - beforeTime) / 1000000000.0;
			unprocessed += passedTime;
			beforeTime = currentTime;
			
			boolean ticked = false;
			while(unprocessed >= secondsPerTick) {
				unprocessed -= secondsPerTick;
				tick(ticks);
				ticked = true;
				ticks++;
				if(ticks % 60 == 0) {
					System.out.println(frames + " fps");
					frames = 0;
					ticks = 0;
					beforeTime += 1000;
				}
			}
			if(ticked) {
				render(ticks);
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void tick(int ticks) {
		if(paused) {
			menu = new PauseMenu(this);
		} else {
			if(menu != null) {
				if(menu instanceof PauseMenu) {
					menu = null;
				}
			}
		}
		
		if(level != null && !level.isGameOver()) {
			boolean up = input.keys[KeyEvent.VK_W];
			boolean down = input.keys[KeyEvent.VK_S];
			boolean left = input.keys[KeyEvent.VK_A];
			boolean right = input.keys[KeyEvent.VK_D];
			boolean one = input.keys[KeyEvent.VK_1];
			boolean two = input.keys[KeyEvent.VK_2];
			boolean three = input.keys[KeyEvent.VK_3];
			boolean four = input.keys[KeyEvent.VK_4];
			boolean esc = input.keys[KeyEvent.VK_ESCAPE];
			controls.tick(up, down, left, right, one, two, three, four, esc);
		}
		
		if(level != null && !paused) {
			level.tick(ticks);
		}
		
		if(menu != null) {
			menu.tick(ticks);
		}
	}
	
	public void mouseWheelMoved(int dir) {
		if(level != null) level.mouseWheelMoved(dir);
	}
	
	private void render(int ticks) {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.render(ticks);
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public synchronized void stop() {
		if(running) {
			running = false;
			try {
				thread.join();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean b) {
		paused = b;
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		
		JFrame frame = new JFrame("Last Stand");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(g);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		g.start();
	}
}
