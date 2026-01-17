package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	public boolean pressedW,pressedD,pressedS,pressedA,pressedE,pressed1,pressed2,pressed3,pressed4,pressed5;

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
	        if (key == java.awt.event.KeyEvent.VK_1) {
	        	pressed1 = true; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_2) {
	        	pressed2 = true; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_3) {
	        	pressed3 = true; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_4) {
	        	pressed4 = true; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_5) {
	        	pressed5 = true; 
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
	        if (key == java.awt.event.KeyEvent.VK_1) {
	        	pressed1 = false; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_2) {
	        	pressed2 = false; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_3) {
	        	pressed3 = false; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_4) {
	        	pressed4 = false; 
	        }
	        if (key == java.awt.event.KeyEvent.VK_5) {
	        	pressed5 = false; 
	        }
		
	}

}
