import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.*;

/*
 * TODO:
 * X DONE X Create additional fruit graphics, and select between them randomly X DONE X
 * X DONE X Implement bombs X DONE X
 * - Implement penalties
 * - Improve blade graphics (?)
 * - Allow additional fruits to be spawned if all existing fruits have been slashed (classic) and/or missed (arcade)
 * - Optimize
 * etc.
 */

public class FruitNinja extends GraphicsProgram {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int BALL_SIZE = 100;
	public static final int BREAK_MS = 30;
	public static int WINDAGE = 15; // Determines the strength of wind in the dojo
	public static int NUM_BALLS = 50; // Determines the starting quantity of fruits
	public static int GRAVITY_MULTIPLIER = 25; // Determines the strength of gravity in the dojo
	
	ArrayList<GImage> myBalls = new ArrayList<GImage>();
	//ArrayList<GOval> trailOfBalls = new ArrayList<GOval>();
	GLabel currScore = new GLabel("Points: 0", 10, 10);
	int scoreVal = 0;
	GOval ballBlade = new GOval(0, 0, 20, 20);
	
	private GImage ball;
	private int xVelocity;
	private RandomGenerator rgen;
	//ArrayList<GOval> balls;
	int k = 0;
	
	
	
	public void run() {
		ballBlade.setColor(Color.CYAN);
		ballBlade.setFilled(true);
		int i = 0;
		rgen = RandomGenerator.getInstance();
		xVelocity = WINDAGE;
		
		String BGMpath = "media/bgm-arcade.wav";
		WAVplayInstance BGMplayer = new WAVplayInstance();
		BGMplayer.playWAV(BGMpath);
		
		for (i=0; i<NUM_BALLS; i++) {
			int fallHeight = (rgen.nextInt(1000,10000)); // Random selection of the coordinates at which each fruit spawns, so that they do not all appear on screen at once.
			int fallWidth = (rgen.nextInt(100,700));
			System.out.println(fallHeight);
			int typeSelector = (rgen.nextInt(0,3));
			String fruitType;
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
			ball = new GImage("../media/whole" + fruitType + ".png", fallWidth, WINDOW_HEIGHT-fallHeight);
			ball.setSize(100,typeCode);
			add(ball);
			myBalls.add(ball);
		}
		
		add(currScore);
        addMouseListeners();
		
        for (GImage ballInstance:myBalls) {
        	animateBall(GRAVITY_MULTIPLIER); // Set the gravity of my balls.
        }
        
	}

	class WAVplayInstance {
	        void playWAV(String WAVpath){
	                 try {
	                         File pathString = new File(WAVpath);
	                          if(pathString.exists()){ 
	                                  AudioInputStream WAVstream = AudioSystem.getAudioInputStream(pathString);
	                                  Clip tempIn = AudioSystem.getClip();
	                                  tempIn.open(WAVstream);
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

	private void animateBall(int gravityMult) {
		while(true) {
			for (GImage ballInstance:myBalls) {
				//int fallVelocity = rgen.nextInt(50,150);
				ballInstance.move(xVelocity, gravityMult);
				if(outOfBounds(ballInstance)) {
					xVelocity *= -1;
				}
				//GOval nullBall = new GOval(0,0);
				//trailOfBalls.add(nullBall);
				//trailCount.setLabel(Integer.toString(trailOfBalls.size()));
				pause(1);
				remove(ballBlade);
			}
		}
	}
	/*
	private void tossBall(GImage flyingBall) {
		flyingBall.setLocation(WINDOW_WIDTH/2, WINDOW_HEIGHT-flyingBall.getHeight()*-2);
		while(true) {
			flyingBall.move(rgen.nextDouble(5,15), rgen.nextInt(1,5));
		}
	}
	*/
	@Override
	public void mousePressed(MouseEvent e) {
		/*for(GImage instBall:myBalls) {
			if(getElementAt(e.getX(), e.getY()) == instBall) {
				instBall.setColor(rgen.nextColor());
			}
			else {
				instBall.setLocation(instBall.getX(),e.getY()-50);
			}
		}*/
	}
	
	public void drawBlade(int curX, int curY) {
		ballBlade.setLocation(curX-15, curY);
		add(ballBlade);

	}
	
	//@Override
	//public boolean GImage.sliced = false;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		for(GImage instBall:myBalls) {
			//System.out.println("Mouse dragged!");
			drawBlade(e.getX(), e.getY());
			if(getElementAt(e.getX(), e.getY()) == instBall) {
				if (instBall.getWidth()==99) {
					
				}
				else {
					// Begin TypeCode check
					if (instBall.getHeight()==100.000) {
						instBall.setImage("../media/slicedwatermelon.png");
						instBall.setSize(99,100); // By reducing the size of the object by a single pixel, its already-slashed state is stored in a memory-efficient manner without additional variables or an array of fruits.
						scoreVal = scoreVal+50;
					}
					
					else if (instBall.getHeight()==100.001) {
						instBall.setImage("../media/slicedcoconut.png");
						instBall.setSize(99,100); // By reducing the size of the object by a single pixel, its already-slashed state is stored in a memory-efficient manner without additional variables or an array of fruits.
						scoreVal = scoreVal+75;
					}
					else if (instBall.getHeight()==100.002) {
						instBall.setImage("../media/slicedapple.png");
						instBall.setSize(99,100); // By reducing the size of the object by a single pixel, its already-slashed state is stored in a memory-efficient manner without additional variables or an array of fruits.
						scoreVal = scoreVal+75;
					}
					else if (instBall.getHeight()==100.003) {
						instBall.setImage("../media/kaboom.png");
						instBall.setSize(99,100);
						scoreVal = scoreVal-150;
					}
					// End TypeCode check
					
					currScore.setLabel("Points: " + Integer.toString(scoreVal));
				}
				//tossBall(instBall);
				System.out.println("Mouse dragged over ball!");
			}
			
			else {
			
			}
		}
	}
	/*
	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("Mouse moved!");
		for (GOval instanceBall:myBalls) {
			if(getElementAt(e.getX(), e.getY()) == instanceBall) {
				System.out.println("Mouse moved over ball!");
			}
			else {
			
			}
		}
	}
	*/
	/*
	 @Override
	 public void mouseReleased(MouseEvent e) {
	  tossBall(instBall);
	 }
	 */
	
	private boolean outOfBounds(GImage ballInstance) {
		double x = ballInstance.getX();
		return (x < 0 && xVelocity < 0 || x > WINDOW_WIDTH-100 && xVelocity > 0);
	}

	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		requestFocus();
	}
	
	public static void main(String[] args) {

		new FruitNinja().start();
	}
}