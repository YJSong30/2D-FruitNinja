import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Graphics;
import javax.swing.ImageIcon;


public class LaunchPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JFrame credFrame = new JFrame("Credits");
	JButton btnArcade = new JButton("Arcade Mode");
	JButton btnClassic = new JButton ("Classic Mode");
	JButton btnArcadeLeaderboard = new JButton ("Arcade Leaderboard");
	JButton btnClassicLeaderboard = new JButton ("Classic Leaderboard");
	JButton credits = new JButton ("Credits");
	JButton creditBack = new JButton("Back");
	String BGMpath = "media/bgm-mainmenu.wav";
	WAVplayInstance BGMplayer = new WAVplayInstance();	
	
	
	
	LaunchPage() {

		BGMplayer.playWAV(BGMpath, true);
		
		ImagePanel bgPanel = new ImagePanel("media/dojo.jpg");
		bgPanel.setLayout(null);
		bgPanel.setSize(frame.getSize());
		frame.setContentPane(bgPanel);
		
		JLabel ninjaLogo = menuImage("media/ninja-logo.png"); 
		frame.add(ninjaLogo);
		ninjaLogo.setBounds(20, 30, 80, 80);
		ninjaLogo.setSize(800, 225);
		
		btnArcade.setBounds(450, 400, 200, 40);
		btnArcade.setFocusable(false);
		btnArcade.addActionListener(this);
		
		btnClassic.setBounds(150, 400, 200, 40);
		btnClassic.setFocusable(false);
		btnClassic.addActionListener(this);
		
		credits.setBounds(300, 480, 200, 40);
		credits.setFocusable(false);
		credits.addActionListener(this);
		
		btnArcadeLeaderboard.setBounds(45, 40, 200, 40);
		btnArcadeLeaderboard.setFocusable(false);
		btnArcadeLeaderboard.addActionListener(this);
		
		btnClassicLeaderboard.setBounds(550, 40, 200, 40);
		btnClassicLeaderboard.setFocusable(false);
		btnClassicLeaderboard.addActionListener(this);
		
		frame.add(credits);
		frame.add(btnArcade);
		frame.add(btnClassic);
		frame.add(btnArcadeLeaderboard);
		frame.add(btnClassicLeaderboard);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setVisible(true);
		
		JLabel imageLabel = menuImage("media/fruitninjalogo.png"); 
		frame.add(imageLabel);
		imageLabel.setBounds(290, 180, 100, 100);
		imageLabel.setSize(200, 200);
		
		
	}
	
	class ImagePanel extends JPanel {
		
	    private Image image;
	    public ImagePanel(Image image) {
	        this.image = image;
	        
	    }
	    
	    public ImagePanel(String string) {
			// TODO Auto-generated constructor stub
	    	try {
				image = ImageIO.read(new File(string));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		}

		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, this);
	    }
	}
	
	private JLabel menuImage(String imagePath){
		
		ImageIcon imageIcon = new ImageIcon(imagePath);
	    JLabel imageLabel = new JLabel(imageIcon);
	    return imageLabel;
	 
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
	
	//implement leader board
	
	private void showCredits() {
		
		
		credFrame.setSize(800, 600);
		credFrame.setLayout(null);
		credFrame.setVisible(true);

		ImagePanel credits = new ImagePanel("media/Credits.png");
		credits.setLayout(null);
		credits.setSize(frame.getSize());
		credFrame.setContentPane(credits);
		
		credFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
		
		creditBack.setBounds(10, 30, 200, 40);
		creditBack.setFocusable(false);
		creditBack.addActionListener(this);
		credFrame.add(creditBack);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnArcade) {
			BGMplayer.stopWAV();
			frame.dispose();
			new ArcadeMode().start();
			
		}
		else if (e.getSource() == btnClassic) {
			BGMplayer.stopWAV();
			frame.dispose();
			new ClassicMode().start();
		}
		
		else if (e.getSource() == credits) {
			frame.dispose();
			showCredits();
			
		}
		
		else if (e.getSource() == creditBack) {
			credFrame.dispose();
			LaunchPage launchPage = new LaunchPage();
			BGMplayer.stopWAV();
			
			//TO DO
		}
		
		else if (e.getSource() == btnArcadeLeaderboard) {
			BGMplayer.stopWAV();
			frame.dispose();
			ArcadeLeaderboard newArcadeBoard = new ArcadeLeaderboard();
			Collections.sort(newArcadeBoard.arcadeScores);
			Collections.reverse(newArcadeBoard.arcadeScores);
			newArcadeBoard.start();
		}
		
		else if (e.getSource() == btnClassicLeaderboard) {
			BGMplayer.stopWAV();
			frame.dispose();
			ClassicLeaderboard newClassicBoard = new ClassicLeaderboard();
			Collections.sort(newClassicBoard.classicScores);
			Collections.reverse(newClassicBoard.classicScores);
			newClassicBoard.start();
		}
		
	}
}
