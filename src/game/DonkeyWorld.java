package game;

import info.gridworld.actor.*;
import info.gridworld.grid.*;
import info.gridworld.gui.*;

import javax.swing.*;

public class DonkeyWorld extends ActorWorld {
	Object obj;
	public DonkeyWorld(Object obj) {
		super(new BoundedGrid(12, 10));
		this.obj = obj;
		if(obj instanceof Actor) {
			this.add(new Location(10,9), (Actor) obj);
		}
	}
	public boolean keyPressed(String description, Location loc) //Listens for key input and based on certain input, will cary out actions such as displaying a help screen or moving Mario in a certain direction
	{
		if(obj instanceof Mario) 
		{
			Mario m = (Mario) obj;
			m.keyIn(description, loc);
		
			
			if (description.equals("H") || description.equals("shift H"))
			{
				LevelRunner.activeGame = false;
				GUIController.getTimer().stop();
				LevelRunner.stopwatch.stop();
				JOptionPane p = new JOptionPane();
				String output = p.showInputDialog(null, "The player will be placed in the world starting at level 1 with 3 lives\n" + 
						"The player will move about the obstacle course with the intention of reaching the top of the tower/structure by climbing ladders while not interfering with bosses and obstacles \n" + 
						"If the player accidently comes in contact with any of the obstacles they will lose a life and will be restarted at the beginning of the course\n" + 
						"If the player comes in contact with any of the barrels they can either press the W key to jump over them strategically OR grab a hammer above them by pressing the space bar (Mario can only hold hammers for 15 secs) and smashing the barrel with the hammer. \nIf the player does not time the jump correctly they will interfere with the object and will lose a life. If the player is not directly above a hammer when they press the space bar they will not be able to grab the hammer.\n" + 
						"Players can not move up a broken ladder" + 
						"\nIf Mario successfully reaches the top of the tower the game ends. If Mario is unable to successfully reach the top of the tower within three lives, the game will end. \nBehind the scenes the scoring data for that attempt will be stored in a text file containing all the scoring records for the computer.\n" + 
						"\nPlease type OK to continue back to the game");
				
				if(output.equals("OK") || output.equals("ok")) {
					LevelRunner.activeGame = true;
					GUIController.getTimer().start();
					LevelRunner.stopwatch.start();
				}
				else {
					p.showMessageDialog(null, "OK was not typed game will now close");
					System.exit(1);
				}
			
			}
			return true;
		}
		
		
		return false;
		
	}
	
	public void reAddMario(Object obj) { //Adds Mario back into the world in the case that he has more lives to spare
		this.obj = obj;
		if(obj instanceof Actor) {
			this.add(new Location(10,9), (Actor) obj);
		}
		LevelRunner.addLadders();
	}
	

	
}
