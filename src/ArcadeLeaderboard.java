import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;



import acm.graphics.GImage;
import acm.graphics.GLabel;

import acm.program.GraphicsProgram;


public class ArcadeLeaderboard extends GraphicsProgram implements ActionListener {
	
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static ArrayList<Integer> arcadeScores = new ArrayList<Integer>();

	private LaunchPage launchPage;
	
	GLabel timerLabel;
	GLabel btnBack = new GLabel("Back", 10, 575);
	//WAVplayInstance BGMplayer = new WAVplayInstance();

	
	
	
	public void run() {
		
		/*
        arcadeScores.add(1200);
        arcadeScores.add(100);
        arcadeScores.add(3850);
        arcadeScores.add(7);
        arcadeScores.add(3000);
		arcadeScores.add(450);
        */
		
		int i = 0;

		GImage dojoBackground = new GImage("../media/dojo.jpg");
		add(dojoBackground);
		
		try { //Attempt to register custom font from TrueType file in media directory
		     GraphicsEnvironment tempEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     tempEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("media/fruitninja.ttf")));
		} catch (IOException|FontFormatException e) {
		     System.out.println(e);
		}
		Font ninjaFont = new Font("Gang of Three", Font.PLAIN, 33);
		
		GLabel titleLabel = new GLabel("Arcade mode high scores", 100, 50);
		titleLabel.setFont(ninjaFont);
		titleLabel.setColor(Color.CYAN);
		titleLabel.scale(1.5);
		add(titleLabel);
		
		btnBack.setFont(ninjaFont);
		btnBack.setColor(Color.GREEN);
		btnBack.scale(1.5);
		add(btnBack);
		
		Collections.sort(arcadeScores);
		Collections.reverse(arcadeScores);
		
		int lblHeightIncrease = 0;
		if (arcadeScores.size()>0) {
			if (arcadeScores.size()<=10) {
				for (i=0; i<arcadeScores.size(); i++) {
					GLabel tempLabel = new GLabel((i+1) + ": " + arcadeScores.get(i).toString(), 300, 125+lblHeightIncrease);
					tempLabel.setFont(ninjaFont);
					tempLabel.setColor(Color.ORANGE);
					tempLabel.scale(2);
					add(tempLabel);
					lblHeightIncrease = lblHeightIncrease + 50;
				}
			}
			else {
				for (i=0; i<10; i++) {
					GLabel tempLabel = new GLabel((i+1) + ": " + arcadeScores.get(i).toString(), 300, 125+lblHeightIncrease);
					tempLabel.setFont(ninjaFont);
					tempLabel.setColor(Color.ORANGE);
					tempLabel.scale(2);
					add(tempLabel);
					lblHeightIncrease = lblHeightIncrease + 50;
				}
			}
		}
		else {
			GLabel tempLabel = new GLabel("No scores yet!", WINDOW_HEIGHT/3, 300);
			tempLabel.setFont(ninjaFont);
			tempLabel.setColor(Color.ORANGE);
			tempLabel.scale(2);
			add(tempLabel);
		}

        addMouseListeners();
       
        
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (getElementAt(e.getX(), e.getY()) == btnBack) {
			System.out.println("weiner");
			//BGMplayer.stopWAV();
			ArcadeLeaderboard.super.gw.dispose();
			LaunchPage launchPage = new LaunchPage();
		}
	}
	
	
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		requestFocus();
	}
	
	public static void main(String[] args) {
		
		LaunchPage launchPage = new LaunchPage();
		
	}
}