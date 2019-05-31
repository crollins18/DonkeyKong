package game;
import java.util.*;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class Mario extends Actor {
	
	private int steps;
	private Location currentLoc;
	private Location leftLoc;
	private Location rightLoc;
	private Actor leftA;
	private Actor rightA;
	private int suffix;
	public static boolean hasHammer;
	private int dir;
	
	public Mario(){ //constructor method
		this.setColor(null);
		steps = 0;
		suffix = 1;
		hasHammer = false;
	}
	
	public void getLocs() { //stores the current location, the location to the left, and the location to the right of the Barrel for use later on
		//In addition, if actors are occupying the left and right location, the actors there are stored in actor variables for later use
		currentLoc = this.getLocation();
		leftLoc = new Location(currentLoc.getRow(), currentLoc.getCol()-1);
		rightLoc = new Location(currentLoc.getRow(), currentLoc.getCol()+1);
		if(this.getGrid().isValid(leftLoc)) {
		leftA = this.getGrid().get(leftLoc);
		}
		if(this.getGrid().isValid(rightLoc)) {
		rightA = this.getGrid().get(rightLoc);
		}
	}

	public void act() { //the act method contain noting as Mario should only move when keys are pressed

	}
	public boolean canMove(String key) { //checks to see if Mario can move based on certain keys pressed. Mario can not move left or right when a BlackSquare, Platform, or DonkeyKong are next to it. In addition, Mario can only climb FullLadders and jump when nothing is above it
		Actor leftAct = null;
		Actor rightAct=null;
        if (this.getGrid() == null) {
            return false;
        }
		Location leftLoca = new Location(this.getLocation().getRow(), this.getLocation().getCol()-1);
		Location rightLoca = new Location(this.getLocation().getRow(), this.getLocation().getCol()+1);
		if(this.getGrid().isValid(leftLoca)) {
			leftAct=this.getGrid().get(leftLoca);
		}
		else if(this.getGrid().isValid(rightLoca)) {
			rightAct=this.getGrid().get(rightLoca);
		}
		if(key.equals("A")) {
			
			if(this.getLocation().getCol() == 0 || (leftAct instanceof Platform || rightAct instanceof Platform || leftAct instanceof BlackSquare || rightAct instanceof BlackSquare || leftAct instanceof Barrel || leftAct instanceof DonkeyKong)) {
				return false;
			}
			else {
				return true;
			}
		}
		else if(key.equals("D")) {
			if(this.getLocation().getCol() ==9 || (rightAct instanceof Platform || leftAct instanceof Platform || rightAct instanceof BlackSquare || leftAct instanceof BlackSquare|| rightAct instanceof Barrel || rightAct instanceof DonkeyKong)) {
				return false;
			}
			else {
				return true;
			}
		}
		else if(key.equals("W") && getLocation().getRow() != 0) {
			currentLoc = getLocation();
			int currentRow = currentLoc.getRow();
			int currentCol = currentLoc.getCol();  
			int upRow = currentRow - 1;
			int sameCol = currentCol;
			Location upOne = new Location(upRow, sameCol); //Ladders loc will be one up from mario
			if(this.getGrid().get(upOne) instanceof BrokenLadder) {
				return false;
			}
			else if (this.getGrid().get(upOne) instanceof FullLadder){
				return true;
			}
		}
		else if(key.equals("SPACE") && getLocation().getRow() !=0) {
			currentLoc = getLocation();
			int currentRow = currentLoc.getRow();
			int currentCol = currentLoc.getCol();  
			int upRow = currentRow - 1;
			int sameCol = currentCol;
			Location upOne = new Location(upRow, sameCol); 
			if(this.getGrid().get(upOne) instanceof BrokenLadder || this.getGrid().get(upOne) instanceof FullLadder) {
				return false;
			}
			else {
				return true;
			}
		}
		return false;
		
	}
	public boolean keyIn(String description, Location loc) { //changes GIFs as Mario moves aswell as replaces ladders as Mario moves based on key input
		if (description.equals("A")) {
			if(canMove(description)) {
				dir = 270;
				if(steps%2==0) {
				getLocs();
				}
				move(dir);
				if(!hasHammer) {
					suffix = 1;
				}
				else if(hasHammer) {
					suffix = 5;
				}
			}
			return true;
		}
		else if (description.equals("D")) {
			if(canMove(description)) {
				dir = 90;
				if(steps%2==0) {
				getLocs();
				}
				move(dir);
				if(!hasHammer) {
				suffix = 2;
				}
				else if(hasHammer) {
					suffix=6;
				}
			}
			return true;
		}
		else if (description.equals("W"))
		{
			if(canMove(description)) {
				dir = 0;
				move(dir);
			}
			return true;
		}
		else if (description.equals("SPACE")) {
			if(canMove(description)) {
				jump();
			}
			
			return true;
		}
		return false;
	}
	
	public void jump() { //Changes GIFs and locations while jumping
		currentLoc = this.getLocation();
		Location upOne = new Location(currentLoc.getRow()-1, currentLoc.getCol()); 
		Actor u = this.getGrid().get(upOne);
		this.moveTo(upOne);
		if(suffix == 1) {
			suffix=4;
		}
		else if (suffix == 2){
			suffix = 7;
		}
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
				moveTo(currentLoc);
				if(suffix == 7) {
					suffix = 2;

				}
				else if (suffix == 4){
					suffix = 1;
				}
				LevelRunner.a.show();
				if(getGrid().isValid(upOne) && u!=null && u instanceof BlackSquare) {
					BlackSquare b = new BlackSquare();
					LevelRunner.a.add(upOne, b);
				}
				else if(getGrid().isValid(upOne) && u!=null && u instanceof Hammer && hasHammer==false) {
					if(suffix == 2) {
						suffix = 6;
						hasHammer = true;
						new Timer().schedule(new TimerTask() {          
						    @Override
						    public void run() {
						    	suffix = 2;
						    	hasHammer = false;
						    }
						}, 15000);

					}
					else if (suffix == 1){
						suffix = 5;
						hasHammer = true;
						new Timer().schedule(new TimerTask() {          
						    @Override
						    public void run() {
						    	suffix = 1;
						    	hasHammer = false;
						    }
						}, 15000);
					}
				}
		    }
		}, 450);
		


	}

	public void move(int direction) { //Moves Mario based on direction from key input and increments steps. Instructions to replace Ladders are contained in this method
		//System.out.println("Before move: " + steps);
		 currentLoc = getLocation();
		 if (getGrid() == null)
	            return;
	     Location next = currentLoc.getAdjacentLocation(direction);
	     if (getGrid().isValid(next) && (direction == 270)) {
	            moveTo(next);
	            steps = steps + 1;
	     }
	     if (getGrid().isValid(next) && (direction == 90)) {
	            moveTo(next);
	            steps = steps - 1;
	     }
	     else if(getGrid().isValid(next)) {
	            moveTo(next);
	     }
	     if(direction == 0) {
	         FullLadder l = new FullLadder();
	         l.putSelfInGrid(getGrid(), currentLoc);
	     }
	     //System.out.println("After move: " + steps);
	     //System.out.println(leftLoc);
	     //System.out.println(leftA);
	     //System.out.println(direction); 
	     //System.out.println(steps%2);
	     if(direction == 270 && steps%2==0 && getGrid().isValid(leftLoc) && leftA !=null && !this.isAlreadyInGrid(this.getGrid(), leftA)) {
	    	 leftA.putSelfInGrid(getGrid(), leftLoc);
	     }
	     if(direction == 90 && steps%2==0 && getGrid().isValid(rightLoc) && rightA !=null && !this.isAlreadyInGrid(this.getGrid(), rightA)) {

	    	 rightA.putSelfInGrid(getGrid(), rightLoc);
	     }
	     if(direction == 90 && steps%2==0 && getGrid().isValid(leftLoc) && leftA !=null && !this.isAlreadyInGrid(this.getGrid(), leftA)) {

	    	 leftA.putSelfInGrid(getGrid(), leftLoc);
	     }
	     if(direction == 270 && steps%2==0 && getGrid().isValid(rightLoc) && rightA !=null && !this.isAlreadyInGrid(this.getGrid(), rightA)) {
	    	 rightA.putSelfInGrid(getGrid(), rightLoc);
	     }
	}
	
	public boolean isAlreadyInGrid(Grid gr, Actor actr) { //Checks to see if an object is already in a grid
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
	
	public String getImageSuffix() //adjusts GIF images for Mario based on what suffix equals
	{
	    return "" + suffix;   
	}

}
