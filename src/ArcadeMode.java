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
//import java.util.Timer;
import javax.swing.Timer;

//import ArcadeMode.WAVplayInstance;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

/*
 * TODO:
 * X DONE X Create additional fruit graphics, and select between them randomly X DONE X
 * X DONE X Implement bombs X DONE X
 * X DONE X Implement background music X DONE X
 * X DONE X Implement additional sound effects X DONE X
 * - Improve blade graphics (?)
 * X DONE X Allow additional fruits to be spawned if all existing fruits have been slashed (classic) and/or missed (arcade)
 * - Optimize
 * etc.
 */

public class ArcadeMode extends GraphicsProgram implements ActionListener {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int BREAK_MS = 30;
	public static int WINDAGE = 7; // Determines the strength of wind in the dojo
	public static int NUM_BALLS = 50; // Determines the starting quantity of fruits
	public static int NUM_BORNANAS = 3;
	public static int GRAVITY_MULTIPLIER = 12; // Determines the strength of gravity in the dojo
	 
	ArrayList<GImage> myBalls = new ArrayList<GImage>();
	//ArrayList<GOval> trailOfBalls = new ArrayList<GOval>();
	GLabel currScore = new GLabel("Points: 0", 10, 25);
	
	int scoreVal = 0;
	int scoreMultiplier = 1;
	GLabel lblMultiplier = new GLabel ("Point multiplier: x" + scoreMultiplier, 10, 50);
	GOval ballBlade = new GOval(0, 0, 20, 20);

	public boolean gameActive = true;
	
	private GImage ball;
	private GImage bornana;
	private int xVelocity;
	private RandomGenerator rgen;
	//ArrayList<GOval> balls;
	int k = 0;
	
	private LaunchPage launchPage;
	private int timeRemaining = 120;
	
	GLabel timerLabel;
	WAVplayInstance BGMplayer = new WAVplayInstance();

	
	
	
	public void run() {
		Timer timer;
		timer = new Timer(1000, this);
		timer.setInitialDelay(1000);
		timer.start();
		ballBlade.setColor(Color.WHITE);
		ballBlade.setFilled(true);
		int i = 0;
		rgen = RandomGenerator.getInstance();
		xVelocity = WINDAGE;
		
		String BGMpath = "media/bgm-arcade.wav";
		//WAVplayInstance BGMplayer = new WAVplayInstance();
		
		BGMplayer.playWAV(BGMpath, true);
		
		GImage dojoBackground = new GImage("../media/dojo.jpg");
		add(dojoBackground);
		
		for (i=0; i<NUM_BALLS; i++) {
			generateNewFruit();
		}
		
		for (i=0; i<NUM_BORNANAS; i++) {
			generateBornana();
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
		lblMultiplier.setFont(ninjaFont);
		lblMultiplier.setColor(Color.ORANGE);
		add(lblMultiplier);
        addMouseListeners();
        
    	timerLabel = new GLabel("Time: " + timeRemaining, 350, 30);
    	timerLabel.setFont("Gang of three-30");
    	timerLabel.setColor(Color.RED);
    	add(timerLabel);
    	
        new Thread(() -> {
            while (gameActive==true) {
                animateAllFruits(GRAVITY_MULTIPLIER);
                remove(ballBlade);
                pause(BREAK_MS); 
                lblMultiplier.setLabel("Point multiplier: x" + Integer.toString(scoreMultiplier));
                if (timeRemaining<=0) {
                	//gameActive=false;
                	gameOver();
                }
            }
        }).start();
        
        
	}
	
	public void actionPerformed(ActionEvent e) {
		if (timeRemaining != 0) {
		timeRemaining--;
		timerLabel.setLabel("Time: " + timeRemaining);
		}
	}
	
	private void gameOver() {
        for (int i = myBalls.size() - 2; i >= 0; i--) {
            GImage ballInstance = myBalls.get(i);
            remove(ballInstance);
            myBalls.remove(i); // Remove the current ballInstance by index
        }
        Font ninjaFont = new Font("Gang of Three", Font.PLAIN, 44);
        GLabel lblGameOver = new GLabel("Game over!", WINDOW_HEIGHT/2, WINDOW_WIDTH/3);
        lblGameOver.setFont(ninjaFont);
        lblGameOver.setColor(Color.CYAN);//changed  color
        add(lblGameOver);
        currScore.setLocation(WINDOW_HEIGHT/2, WINDOW_WIDTH/2.5);
        remove(lblMultiplier);
        BGMplayer.stopWAV();
        BGMplayer.playWAV("media/gameover.wav", false);
        gameActive=false;
        pause(5000);
        LaunchPage launchPage = new LaunchPage();
        ArcadeMode.super.gw.dispose();
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
		Clip tempIn;
	        void playWAV(String WAVpath, boolean loopState){
	                 try {
	                         File pathString = new File(WAVpath);
	                          if(pathString.exists()){ 
	                                  AudioInputStream WAVstream = AudioSystem.getAudioInputStream(pathString);
	                                  tempIn = AudioSystem.getClip();
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
	        void stopWAV() {
	        	if (tempIn != null) {
	        		tempIn.stop();
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
		//bornana = new GImage("../media/wholebornana.png", fallWidth, WINDOW_HEIGHT-fallHeight);
		
	}
	
	private void generateBornana() {
		int fallHeight = (rgen.nextInt(1000,10000)); // Random selection of the coordinates at which each fruit spawns, so that they do not all appear on screen at once.
		int fallWidth = (rgen.nextInt(100,700));
		System.out.println(fallHeight);
		bornana = new GImage("../media/wholebornana.png", fallWidth, WINDOW_HEIGHT-fallHeight);
		bornana.setSize(150, 100.999);
		add(bornana);
		myBalls.add(bornana);
	}
	
	private void animateFruit(int gravityMult, GImage ballInst) { //modify this function
		 ballInst.move(xVelocity, gravityMult);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//Nothing for now.
	}
	
	public void drawBlade(int curX, int curY) {
		ballBlade.setLocation(curX-15, curY);
		add(ballBlade);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("Click");
		int ballsSize = myBalls.size();
		for (int i = ballsSize-1; i>=0; i--) {
			GImage instFruit = myBalls.get(i);
			//System.out.println("Mouse dragged!");
			drawBlade(e.getX(), e.getY());
			if(getElementAt(e.getX(), e.getY()) == instFruit) {
				if (instFruit.getWidth()==99||instFruit.getWidth()==149) {
					
				}
				else {
					// Begin typeCode check
					if (instFruit.getHeight()==100.000) {
						instFruit.setImage("../media/slicedwatermelon.png");
						instFruit.setSize(99,100.000); // By reducing the size of the object by a single pixel, its already-slashed state is stored in a memory-efficient manner without additional variables or an array of fruits.
						scoreVal = scoreVal+(50*scoreMultiplier);
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/slice.wav", false);
					}
					
					else if (instFruit.getHeight()==100.001) {
						instFruit.setImage("../media/slicedcoconut.png");
						instFruit.setSize(99,100.001);
						scoreVal = scoreVal+(75*scoreMultiplier);
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/slice.wav", false);
					}
					else if (instFruit.getHeight()==100.002) {
						instFruit.setImage("../media/slicedapple.png");
						instFruit.setSize(99,100.002);
						scoreVal = scoreVal+(75*scoreMultiplier);
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/slice.wav", false);
					}
					else if (instFruit.getHeight()==100.003) {
						instFruit.setImage("../media/kaboom.png");
						instFruit.setSize(99,100.003);
						//scoreVal = scoreVal-150;
						timeRemaining = timeRemaining-5;
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/bang.wav", false);
					}
					
					else if (instFruit.getHeight()==100.999) {
						instFruit.setImage("../media/slicedbornana.png");
						instFruit.setSize(149,100.999);
						//scoreVal = scoreVal+100;
						//timeRemaining = timeRemaining-5;
						WAVplayInstance slicedPlayer = new WAVplayInstance();
						slicedPlayer.playWAV("media/bornana.wav", false);
				        new Thread(() -> {
				        	int tempTime = timeRemaining;
				            while (timeRemaining>(tempTime-10)) {
				            	scoreMultiplier = 2;
				            	System.out.println(tempTime); // DO NOT REMOVE THIS LINE!
				            	// For reasons beyond my comprehension, continually printing tempTime during the while loop allows the multiplier to update dynamically.
				            	// THE BORNANA CODE DEPENDS UPON THIS PRINT STATEMENT!
				                
				            }
				            scoreMultiplier = 1;
				            
				            }).start();
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
		if (ballInstance.getX()>=WINDOW_WIDTH-50||ballInstance.getX()<=0||ballInstance.getY()>=WINDOW_HEIGHT) {
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