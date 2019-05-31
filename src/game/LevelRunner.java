package game;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.google.common.base.Stopwatch;
import info.gridworld.grid.Location;
import info.gridworld.gui.GUIController;
public class LevelRunner {
	public static int topRowNum;
	public static int rowSpacing;
	public static int barrelStartCol;
	private static int donkeyKongStartCol;
	public static Mario m = new Mario();
	public static DonkeyWorld a = new DonkeyWorld(m);
	public static GameHelper bar;
	public static long start;
	public long current; 
	public static long score = 0;
	public int temp;
	public static int state = 1;
	public static boolean activeGame = true;
	public static Stopwatch stopwatch;
	public static boolean won = false;
	
	
	//Adds valid scores to a text file to save score between runs.
	public void updateScoreList(String nam)
	{
		try
		{
		FileWriter p = new FileWriter("scorelist.txt");
		if(won)
		{
			p.write(""+nam+">"+score+ "\n");
		}
		p.close();
		}
		catch (Exception e)
		{
			System.out.print(e);
		}
	}
	//updates temp score in score.txt
	public int refresh()
	{

		String lives = "";
		stopwatch.stop();
		current = stopwatch.elapsed(TimeUnit.MILLISECONDS)/1000;
		stopwatch.start();
		score = current*10;
		
		try
		{
			Scanner in = new Scanner(new File("live.txt"));
			temp = in.nextInt();
			lives =  ""+temp;
			if (temp == 0)
			{
				//System.out.print("check");
				state = 0;
				
			}
			a.setMessage("lives = "+ lives + ":score "+ current*10);
			in.close();
			
			Writer out = new FileWriter(new File("score.txt"));
			out.write(""+current*10);
			out.close();
			
		}
		catch (Exception e)
		{
			
		}
		
		return state;
	
	}

	
	public void generateLevelOne() //generates the level with the necessary actors and initializes lives file
	{
		
		//m = new Mario();
		//a = new DonkeyWorld(m);
		addElements();
		Scanner in = null;
		
		
		try 
		{
			in = new Scanner(new File("live.txt"));
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.nextLine();
		in.close();
		PrintWriter p = null;
		try 
		{
			p = new PrintWriter(new File("live.txt"));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.println(3);
		p.close();
		a.show();
		GUIController.runButton.setEnabled(false);
		GUIController.stepButton.setEnabled(false);
		GUIController.stopButton.setEnabled(false);
		GUIController.controlPanel.setEnabled(false);
		GUIController.getTimer().start();
		stopwatch = Stopwatch.createStarted();
	}
	
	public static void addElements() //adds ladders, hammers, and platforms based on the spacing variables and other instance variables such as the starting col for DonkeyKong
	{
		topRowNum = 1;
		rowSpacing = 3; //at least 3 for jumps
		barrelStartCol = 3;
		donkeyKongStartCol = barrelStartCol - 1;
		
		for(int r=topRowNum+1; r<a.getGrid().getNumRows(); r+=rowSpacing) 
		{
			for(int c=0; c<a.getGrid().getNumCols(); c++)
			{
				a.add(new Location(r,c), new Platform());
			}
		}
		
		//Make sure ladders are placed on even numbers columns
		
		addLadders();
		a.add(new Location(6,4), new Hammer());
		a.add(new Location(6,9), new Hammer());
		a.add(new Location(3,8), new Hammer());
		bar = new GameHelper();
		a.add(new Location(0, 0), bar);
		a.add(new Location(topRowNum, donkeyKongStartCol),  new DonkeyKong());
		
	} 
	
	public static void addLadders() //helper method to add ladders and black squares to prevent Mario from moving while on a ladder
	{
		a.add(new Location(9,8), new FullLadder());
		a.add(new Location(8,8), new FullLadder());
		a.add(new Location(7,8), new FullLadder());
		a.add(new Location(7,2), new FullLadder());
		a.add(new Location(6,2), new FullLadder());
		a.add(new Location(5,2), new FullLadder());
		a.add(new Location(1,4), new FullLadder());
		a.add(new Location(2,4), new FullLadder());
		a.add(new Location(4,4), new FullLadder());
		a.add(new Location(4,2), new FullLadder());
		a.add(new Location(10,8), new FullLadder());
		a.add(new Location(3,4), new FullLadder());
		a.add(new Location(9,7), new BlackSquare());
		a.add(new Location(9,9), new BlackSquare());
		a.add(new Location(6,3), new BlackSquare());
		a.add(new Location(6,1), new BlackSquare());
		a.add(new Location(3,3), new BlackSquare());
		a.add(new Location(3,5), new BlackSquare());
		a.add(new Location(5,6), new BrokenLadder());
		a.add(new Location(6,6), new BrokenLadder());
		a.add(new Location(7,6), new BrokenLadder());
	}
	
	
	
	public static void main(String args[]) 
	{
		
		//name to associate with a score.
		String name;
		try
		{
			PrintWriter s = new PrintWriter("score.txt");
			s.print("");
			s.close();
		}
		catch (Exception e)
		{
			System.out.print(e);
		}
		
		
		LevelRunner one = new LevelRunner();
		name = JOptionPane.showInputDialog(new JFrame(), "Welcome to Donkey Kong! Your job is to make it to the top of the level and save princess peach. (To end the level you must be directly next to Donkey Kong) The basic controls are \nWSAD for movement and space to jump. \n Pressing h at any point will bring up a help dialog, you will be timed for level completion and the timer starts when you've exited this dialog. \nYour goal is to finish the level as fast as possible with the lowest score\nPress ok to conintue to the game\n Please enter your name below");
		one.generateLevelOne();
		
		
		while (state != 0)
		{
			System.out.println("");
			if(activeGame) {
				one.refresh();
			}
		}
		
		one.updateScoreList(name);
		
	}
	


}
