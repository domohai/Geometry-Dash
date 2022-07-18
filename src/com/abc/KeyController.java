package com.abc;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter {
	private boolean[] key_pressed = new boolean[128];

	@Override
	public void keyPressed(KeyEvent e) {
		key_pressed[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		key_pressed[e.getKeyCode()] = false;
	}
	
	public boolean key_is_pressed(int keycode) {
		return key_pressed[keycode];
	}
}
