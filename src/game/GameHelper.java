package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;

import info.gridworld.grid.Location;

public class GameHelper extends Barrel{
	private Barrel bar;
	public boolean removed;
	public boolean gameOver;
	public String temp;
	public String state;

	
	public GameHelper() {
		bar = new Barrel();
		LevelRunner.a.add(new Location(LevelRunner.topRowNum, LevelRunner.barrelStartCol), bar);
		this.setColor(null);
		removed = false;
		gameOver = false;
	}
	
	public void act() { //regenerates new Barrels and updates scoring files and game when Barrels kill Mario
		
	if(bar != null && bar.getLocation() != null && bar.getLocation().getRow() == 1 && bar.getLocation().getCol()==9) {
			bar = new Barrel();
			LevelRunner.a.add(new Location(LevelRunner.topRowNum, LevelRunner.barrelStartCol), bar);
		}
	if(!marioInGrid() && removed == false) {
		Scanner in = null;
		try {
			in = new Scanner(new File("live.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int tempNum = in.nextInt();
		in.close();
		PrintWriter p = null;
		try {
			p = new PrintWriter(new File("live.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.println(tempNum-1);
		p.close();
		removed = true;
	}
	Scanner in = null;
	try {
		in = new Scanner(new File("live.txt"));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	int readNum = Integer.parseInt(in.nextLine());
	
	if(readNum !=0 && removed == true) {
		LevelRunner.a.reAddMario(new Mario());
		removed = false;

	}
	else if(readNum == 0 && gameOver == false) {
		if (LevelRunner.won == true)
			state = "won";
		if (LevelRunner.won == false)
			state = "lost";
		LevelRunner.a.close();
		JOptionPane.showMessageDialog(null, "Game over! You "+state+" and scored: " + LevelRunner.score);
		gameOver = true;
		System.exit(1);
	}
	
	}
	public boolean marioInGrid() { //Checks to see if Mario is already in the grid
		for(int r=0; r<this.getGrid().getNumRows(); r++) {
			for(int c=0; c<this.getGrid().getNumCols(); c++) {
				if(this.getGrid().get(new Location(r,c)) != null) {
					if(this.getGrid().get(new Location(r,c)) instanceof Mario) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
