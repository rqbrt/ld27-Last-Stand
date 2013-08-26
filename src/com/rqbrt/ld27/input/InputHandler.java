package com.rqbrt.ld27.input;

import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.rqbrt.ld27.Game;

public class InputHandler implements KeyListener, MouseListener, FocusListener, MouseMotionListener, MouseWheelListener {
	
	public Game g;
	public boolean[] keys = new boolean[256];
	public Rectangle mouse = new Rectangle(0, 0, 1, 1);
	public Rectangle mousePos;
	public boolean mouseDown = false;
	
	public InputHandler(Game g) {
		this.g = g;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() > 0 && e.getKeyCode() <= keys.length) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() > 0 && e.getKeyCode() <= keys.length) {
			keys[e.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	@Override
	public void focusGained(FocusEvent e) {
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(g.menu == null) {
			g.setPaused(true);
		}
		for(int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse.x = e.getX();
		mouse.y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse.x = e.getX();
		mouse.y = e.getY();
		mousePos = new Rectangle(e.getX() / Game.scale, e.getY() / Game.scale, 1, 1);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		g.mouseWheelMoved(e.getWheelRotation());
	}
}
