package main;

import java.awt.image.BufferedImage;

public class Entity {
	boolean idle,walking,death;
	int direction=2,currentFrame=0; //0-up 1-left 2-down 3-right
	int idleFPF = 30,walkingFPF = 30,deathFPF = 10;
	int idleCFPF=0,walkingCFPF=0,deathCFPF=0;
	BufferedImage idleAnimation[][];
	BufferedImage walkingAnimation[][];
	BufferedImage deathAnimation[][];
	
	
	
	public BufferedImage getCurrentFrame() {
		if(death) {
			deathCFPF++;
			if(deathCFPF==deathFPF) {
				currentFrame++;
				deathCFPF=0;
			}
			if(currentFrame>=idleAnimation[0].length)
				currentFrame=0;
			return deathAnimation[direction][currentFrame];
			
		} else if(walking) {
			walkingCFPF++;
			if(walkingCFPF>=walkingFPF) {
				walkingCFPF=0;
				currentFrame++;
			}
			if(currentFrame>=walkingAnimation[0].length)
				currentFrame=0;
			return walkingAnimation[direction][currentFrame];
		}else {
			idleCFPF++;
			if(idleCFPF>=idleFPF) {
				idleCFPF=0;
				currentFrame++;
			}
			if(currentFrame>=idleAnimation[0].length)
				currentFrame=0;
			return idleAnimation[direction][currentFrame];
		}
	}
}
