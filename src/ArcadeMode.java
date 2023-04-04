import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.*;

/*
 * TODO:
 * X DONE X Create additional fruit graphics, and select between them randomly X DONE X
 * X DONE X Implement bombs X DONE X
 * X DONE X Implement background music X DONE X
 * X DONE X Implement additional sound effects X DONE X
 * - Implement penalties
 * - Improve blade graphics (?)
 * - Allow additional fruits to be spawned if all existing fruits have been slashed (classic) and/or missed (arcade)
 * - Optimize
 * etc.
 */

public class ArcadeMode extends GraphicsProgram {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int BREAK_MS = 30;
	public static int WINDAGE = 7; // Determines the strength of wind in the dojo
	public static int NUM_BALLS = 50; // Determines the starting quantity of fruits
	public static int GRAVITY_MULTIPLIER = 12; // Determines the strength of gravity in the dojo
	
	ArrayList<GImage> myBalls = new ArrayList<GImage>();
	//ArrayList<GOval> trailOfBalls = new ArrayList<GOval>();
	GLabel currScore = new GLabel("Points: 0", 10, 25);
	int scoreVal = 0;
	GOval ballBlade = new GOval(0, 0, 20, 20);

	
	private GImage ball;
	private int xVelocity;
	private RandomGenerator rgen;
	//ArrayList<GOval> balls;
	int k = 0;
	
	private LaunchPage launchPage;
	
	
	public void run() {
		ballBlade.setColor(Color.CYAN);
		ballBlade.setFilled(true);
		int i = 0;
		rgen = RandomGenerator.getInstance();
		xVelocity = WINDAGE;
		
		String BGMpath = "media/bgm-arcade.wav";
		WAVplayInstance BGMplayer = new WAVplayInstance();
		
		BGMplayer.playWAV(BGMpath, true);
		
		GImage dojoBackground = new GImage("../media/dojo.jpg");
		add(dojoBackground);
		
		for (i=0; i<NUM_BALLS; i++) {
			generateNewFruit();
		}
	
		
		try { //Attempt to register custom font from TrueType file in media directory
		     GraphicsEnvironment tempEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     tempEnv.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("media/fruitninja.ttf")));
		} catch (IOException|FontFormatException e) {
		     System.out.println(e);
		}
		Font ninjaFont = new Font("Gang of Three", Font.PLAIN, 33);
		currScore.setFont(ninjaFont);
		currScore.setColor(Color.ORANGE);
		add(currScore);
        addMouseListeners();
        
        new Thread(() -> {
            while (true) {
                animateAllFruits(GRAVITY_MULTIPLIER);
                remove(ballBlade);
                pause(BREAK_MS); 
            }
        }).start();
        
        
	}
	
	private void animateAllFruits(int gravityMult) {
	    synchronized (myBalls) {
	        for (int i = myBalls.size() - 1; i >= 0; i--) {
	            GImage ballInstance = myBalls.get(i);
	            animateFruit(GRAVITY_MULTIPLIER, ballInstance);
	            if (!fruitBoundaryCheck(ballInstance)) {
	                remove(ballInstance);
	                myBalls.remove(i); // Remove the current ballInstance by index
	                generateNewFruit(); // Generates a new fruit to ensure that the user never "runs out" of fruits.
	            }
	            if (windageBounceCheck(ballInstance)) {
	                xVelocity *= -1;
	            }
	        }
	    }
	}

	class WAVplayInstance {
	        void playWAV(String WAVpath, boolean loopState){
	                 try {
	                         File pathString = new File(WAVpath);
	                          if(pathString.exists()){ 
	                                  AudioInputStream WAVstream = AudioSystem.getAudioInputStream(pathString);
	                                  Clip tempIn = AudioSystem.getClip();
	                                  tempIn.open(WAVstream);
	                                  if (loopState==false) {
	                                	  
	                                  }
	                                  else {
	                                	  tempIn.loop(Clip.LOOP_CONTINUOUSLY);
	                                  }
	                                  tempIn.start();

	                           }
	                          else{
	                                   System.out.println("WAV path nonexistent!");
	                                }
	                }
	                catch (Exception e){
	                           System.out.println(e);
	                     }
	           }
	}

	private void generateNewFruit() {
		int fallHeight = (rgen.nextInt(1000,10000)); // Random selection of the coordinates at which each fruit spawns, so that they do not all appear on screen at once.
		int fallWidth = (rgen.nextInt(100,700));
		//System.out.println(fallHeight);
		int typeSelector = (rgen.nextInt(0,3));
		String fruitType;
		//Begin fruit typeCode selector
		double typeCode;
		if (typeSelector == 0) {
			fruitType = "watermelon";
			typeCode = 100.000;
		}
		else if (typeSelector == 1) {
			fruitType = "coconut";
			typeCode = 100.001;
		}
		else if (typeSelector == 2) {
			fruitType = "apple";
			typeCode = 100.002;
		}
		else {
			fruitType = "bomb";
			typeCode = 100.003;
		}
		//End fruit typeCode selector
		ball = new GImage("../media/whole" + fruitType + ".png", fallWidth, WINDOW_HEIGHT-fallHeight);
		ball.setSize(100,typeCode);
		add(ball);
		myBalls.add(ball);
	}
	
	private void animateFruit(int gravityMult, GImage ballInst) { //modify this function
		 ballInst.move(xVelocity, gravityMult);
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		//Nothing for now.
	}
	
//	@Override
//	    public void mouseClicked(MouseEvent e) {
//	        for (GImage ballInstance : myBalls) {
//	            animateFruit(GRAVITY_MULTIPLIER, ballInstance);
//	        }
//	    }
	
	public void drawBlade(int curX, int curY) {
		ballBlade.setLocation(curX-15, curY);
		add(ballBlade);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Click");
		int ballsSize = myBalls.size();
		for (int i = ballsSize-1; i>=0; i--) {
			GImage instFruit = myBalls.get(i);
			//System.out.println("Mouse dragged!");
			drawBlade(e.getX(), e.getY());
			if(getElementAt(e.getX(), e.getY()) == instFruit) {
				if (instFruit.getWidth()==99) {
					
				}
				else {
					// Begin typeCode check
					if (instFruit.getHeight()==100.000) {
						instFruit.setImage("../media/slicedwatermelon.png");
						instFruit.setSize(99,100.000); // By reducing the size of the object by a single pixel, its already-slashed state is stored in a memory-efficient manner without additional variables or an array of fruits.
						scoreVal = scoreVal+50;
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/slice.wav", false);
					}
					
					else if (instFruit.getHeight()==100.001) {
						instFruit.setImage("../media/slicedcoconut.png");
						instFruit.setSize(99,100.001);
						scoreVal = scoreVal+75;
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/slice.wav", false);
					}
					else if (instFruit.getHeight()==100.002) {
						instFruit.setImage("../media/slicedapple.png");
						instFruit.setSize(99,100.002);
						scoreVal = scoreVal+75;
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/slice.wav", false);
					}
					else if (instFruit.getHeight()==100.003) {
						instFruit.setImage("../media/kaboom.png");
						instFruit.setSize(99,100.003);
						scoreVal = scoreVal-150;
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/bang.wav", false);
					}
					// End typeCode check
					

					
					currScore.setLabel("Points: " + Integer.toString(scoreVal));
				}
				//tossBall(instBall);
				//System.out.println("Mouse dragged over ball!");
			}
			
			else {
			
			}
		}
	}
	
	private boolean fruitBoundaryCheck(GImage ballInstance) {
		if (ballInstance.getX()>=WINDOW_WIDTH||ballInstance.getY()>=WINDOW_HEIGHT) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean windageBounceCheck(GImage ballInstance) {
		double x = ballInstance.getX();
		return (x < 0 && xVelocity < 0 || x > WINDOW_WIDTH-100 && xVelocity > 0);
	}

	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		requestFocus();
	}
	
	public static void main(String[] args) {
		
		LaunchPage launchPage = new LaunchPage();
		
		
	}
}