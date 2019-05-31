package game;

import java.io.*;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Location;

public class DonkeyKong extends Actor{
	
	public DonkeyKong() {
		this.setColor(null);
	}
	public void act() { //Checks to see if Mario is next to DonkeyKong and if true will set Mario's lives to 0 thus triggering the end of the game
		if(this.isNextToMario()) {
			try {
				PrintWriter p = new PrintWriter("live.txt");
				p.print(0);
				p.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean isNextToMario() { //Checks to see if the location to the right of DonkeyKong contains Mario
		if(this.getGrid().get(new Location(this.getLocation().getRow(), this.getLocation().getCol()+1)) instanceof Mario) {
			LevelRunner.won = true;
			return true;
		}
		return false;
	}
	
	

}
