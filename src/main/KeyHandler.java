package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	public boolean pressedW,pressedD,pressedS,pressedA,pressedE;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		 if (key == java.awt.event.KeyEvent.VK_W) {
	           pressedW = true;
	        }
	        if (key == java.awt.event.KeyEvent.VK_S) {
	        	pressedS = true;
	        }
	        if (key == java.awt.event.KeyEvent.VK_A) {
	        	pressedA = true;
	        }
	        if (key == java.awt.event.KeyEvent.VK_D) {
	        	pressedD = true; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_E) {
	        	pressedE = true; 
	        }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		 if (key == java.awt.event.KeyEvent.VK_W) {
	           pressedW = false;
	        }
	        if (key == java.awt.event.KeyEvent.VK_S) {
	        	pressedS = false;
	        }
	        if (key == java.awt.event.KeyEvent.VK_A) {
	        	pressedA = false;
	        }
	        if (key == java.awt.event.KeyEvent.VK_D) {
	        	pressedD = false; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_E) {
	        	pressedE = false; 
	        }
		
	}

}
