package game;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class Barrel extends Actor{
	
	private int steps = 0;
	private Location leftLoc;
	private Location rightLoc;
	private Actor leftA;
	private Actor rightA;
	private Location currentLoc;

	public Barrel() {
		this.setColor(null);
	}
	public boolean canMove() { //checks if the barrel can move (barrel can move if it is not on the 9th row)
		if(this.getLocation().getCol() == 9) {
			return false;
		}
		else {
			return true;
		}
	}
	public void getLocs() { //stores the current location, the location to the left, and the location to the right of the Barrel for use later on
		//In addition, if actors are occupying the left and right location, the actors there are stored in actor variables for later use
		currentLoc = this.getLocation();
		leftLoc = new Location(currentLoc.getRow(), currentLoc.getCol()-1);
		rightLoc = new Location(currentLoc.getRow(), currentLoc.getCol()+1);
		if(this.getGrid().isValid(leftLoc) && !(this.getGrid().get(leftLoc) instanceof Mario)) {
		leftA = this.getGrid().get(leftLoc);
		}
		if(this.getGrid().isValid(rightLoc) && !(this.getGrid().get(rightLoc) instanceof Mario)) {
		rightA = this.getGrid().get(rightLoc);
		}
	}
	public void act() { //sets up the replacement of ladders as the Barrel rolls and will remove it self from the world if Mario has a hammer
		if(this.canMove()) {
			if(steps%2==0) {
				getLocs();
			}
			Location ahead = new Location(this.getLocation().getRow(), this.getLocation().getCol()+1);
			if (Mario.hasHammer && this.getGrid().get(ahead) instanceof Mario) {
				this.removeSelfFromGrid();
			}
			else {
				this.moveTo(ahead);
			}
			steps++;
			if(this.getGrid() != null) {
				if(steps%2==0 && getGrid().isValid(rightLoc) && rightA !=null && !this.isAlreadyInGrid(this.getGrid(), rightA) && !(rightA instanceof Mario)) {
				    rightA.putSelfInGrid(getGrid(), rightLoc);
				}
			}
			if(this.getGrid() != null) {
			    if(steps%2==0 && getGrid().isValid(leftLoc) && leftA !=null && !this.isAlreadyInGrid(this.getGrid(), leftA) && !(leftA instanceof Mario)) {
			    	leftA.putSelfInGrid(getGrid(), leftLoc);
			     }
			}
		}
		else {
			Location tempLoc = new Location(this.getLocation().getRow()+LevelRunner.rowSpacing,0);
			if(this.getGrid().isValid(tempLoc)) {
				this.moveTo(tempLoc);
				steps++;
			}
			else {
				this.removeSelfFromGrid();
			}
		}
	}
	public boolean isAlreadyInGrid(Grid gr, Actor actr) { //Checks to see if Ladder is in the grid already
		for(int i=0; i<gr.getNumRows(); i++) {
			for(int j=0; j<gr.getNumCols(); j++) {
				if(gr.get(new Location(i,j)) != null) {
					if(gr.get(new Location(i,j)).equals(actr)) {
						return true;
					}
				}
			}
		}
		return false;		
	}
	
}
