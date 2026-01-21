package main;

import java.awt.image.BufferedImage;

public class Entity {
	boolean idle,walking,death;
	static boolean playerMoved = false;
	boolean deathAnimationFinished = false;
	
	public enum AnimationState {
	    IDLE,
	    WALKING,
	    DEATH
	};
	
	AnimationState currentState = AnimationState.IDLE;
	AnimationState previousState = AnimationState.IDLE;
	
	public enum directions{ //0-up 1-left 2-down 3-right
		up, 
		left,
		down,
		right;
	}
	
	directions direction = directions.down;
	int currentFrame=0; 
	int idleFPF = 30,walkingFPF = 30,deathFPF = 100;
	int idleCFPF=0,walkingCFPF=0,deathCFPF=0;
	
	BufferedImage idleAnimation[][];
	BufferedImage walkingAnimation[][];
	BufferedImage deathAnimation[][];
	
	
	
	public BufferedImage getCurrentFrame() {

	    // determine current animation state
	    if (death) {
	        currentState = AnimationState.DEATH;
	    } else if (walking) {
	        currentState = AnimationState.WALKING;
	    } else {
	        currentState = AnimationState.IDLE;
	    }

	    // reset frames if animation changed
	    if (currentState != previousState) {
	        resetAnimation();
	        previousState = currentState;
	    }

	    switch (currentState) {

	        case DEATH:
	            if (!deathAnimationFinished) {
	                deathCFPF++;
	                if (deathCFPF >= deathFPF) {
	                    deathCFPF = 0;
	                    currentFrame++;
	                }

	                if (currentFrame >= deathAnimation[0].length - 1) {
	                    currentFrame = deathAnimation[0].length - 1;
	                    deathAnimationFinished = true;
	                }
	            }
	            return deathAnimation[direction.ordinal()][currentFrame];

	        case WALKING:
	            walkingCFPF++;
	            if (walkingCFPF >= walkingFPF) {
	                walkingCFPF = 0;
	                currentFrame++;
	            }
	            if (currentFrame >= walkingAnimation[0].length) {
	                currentFrame = 0;
	            }
	            return walkingAnimation[direction.ordinal()][currentFrame];

	        case IDLE:
	        default:
	            idleCFPF++;
	            if (idleCFPF >= idleFPF) {
	                idleCFPF = 0;
	                currentFrame++;
	            }
	            if (currentFrame >= idleAnimation[0].length) {
	                currentFrame = 0;
	            }
	            return idleAnimation[direction.ordinal()][currentFrame];
	    }
	}

	
	private void resetAnimation() {
	    currentFrame = 0;
	    idleCFPF = 0;
	    walkingCFPF = 0;
	    deathCFPF = 0;
	}

}
