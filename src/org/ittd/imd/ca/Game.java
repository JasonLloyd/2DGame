package org.ittd.imd.ca;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

import pbox2d.PBox2D;
import processing.core.PApplet;
import processing.core.PImage;


/**
 * @author Jason Lloyd
 *
 */
public class Game extends PApplet
{
	
	private static final long serialVersionUID = 1L;

	enum States			//these are the states of the game
	{
		menu,
		playing,
		levelEnd,
		levelSelect,
		gameOver,
		win,
	}
	
	/** 
	 * These variables are related to the sound of the game
	 * An external library is used can be found here:
	 * {@link http://code.compartmental.net/tools/minim/ } 
	 * audio_player: player for menu and levels
	 * audio_player_menuMove: player for our menu sound
	 * audio_player_catapult: player for the catapult
	 */
	private AudioPlayer audio_player;
	private AudioPlayer audio_player_menuMove;
	private AudioPlayer audio_player_catapult;
	private Minim minim;
	
	
	
	/**
	 * These variables are related to the ball shapes within our game
	 * player: Player that user of game can control
	 * EndZone: Ball placed at end zone
	 * seesawBall: 1st ball that is placed on see-saw
	 * seesawBallTwo: 2nd ball that is placed on see-sawTwo
	 */
	private Ball player;
	private Ball endZone;
	private Ball seesawBall;
	private Ball seesawBallTwo;
	
	// A reference to our box2d world
	private PBox2D box2d;
	
	
	/**
	 * Variables for our backgrounds and images
	 * bg: is background for levels
	 * chooseLevelBg: holds the background for chooseLevel screen
	 * soundIcon : holds the icon used to mute and play the sound
	 */
	private PImage bg;
	private PImage chooseLevelBg;
	private PImage soundIcon;

	// A list we'll use to track fixed objects
	private ArrayList<Boundary> boundaries;
	
	
	/**
	 * Variables for our see-saws
	 */
	private SeeSaw inGameseeSaw;
	private SeeSaw inGameseeSawTwo;
	
	//Represents the current state of the game
	private States currentState;
	
	
	/**
	 * Integer variables to hold varies things:
	 * selX: Set of initial X position for rect on chooseLevel screen
	 * currentLevel: hold the current level player is on
	 * numberOfBalls: hold the amount of life's the players have to start with
	 * helpStages: sets the initial stage for the tutorial 
	 */
	private int selX = 15;
	private int currentLevel = 1;
	private int numberOfBall = 3;
	private int helpStages = 1;
	
	
	/**
	 * float variables to do with the players score
	 */
	private float score = 0;
	private float maxScore = 1000;
	
	private float platformYPosition = 60;
	
	private float seeSawBallX;
	private float seeSawBallY;
	private float seeSawBallTwoX;
	private float seeSawBallTwoY;

	
	/**
	 * Boolean variables to do with sound and showing the help
	 * muteSound: states if the music is to start or not.
	 * showHelp: states if the help should be shown.
	 */
	private boolean muteSound = true;
	private boolean showHelp = false;
	
	boolean gameOverDisplayed = false;
	
	double textIncrement = 20;
	
	float theta;
	
	 Object o1;					//gets the UserData/ID that we set early for the objects 
	 Object o2;

	 long startTime = 0;
	 long endTime = 0;
	
	
	/* (non-Javadoc)
	 * This is ran first it steps up the screen and the variables
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup()
	{
	  size(640,480);
	  smooth();

	  minim = new Minim(this);
	  
	  currentState = States.menu;

	  //sets the different audio_players to the correct music
	  audio_player = minim.loadFile("Resources/MenuMusic.mp3",2048);
	  audio_player_menuMove = minim.loadFile("Resources/menuMoveSound.wav",2048);
	  audio_player_catapult = minim.loadFile("Resources/Catapult.mp3",2048);
	  
	  //audio_player.play();						//starts the menu music
	  
	  soundIcon = loadImage("Resources/SoundIcon.png");	//loading the soundIcon
	  
	  
	  box2d = new PBox2D(this);			// Initialize box2d physics and create the world
	  box2d.createWorld();				// Creating our world
	  
	  box2d.setGravity(0, -100f);		// We are setting a custom gravity

	  
	  box2d.listenForCollisions();		// Tells box2d to listen for collisions
	  
	  chooseLevelBg = loadImage("Resources/chooseLevelBG.jpg");	//load the choose level background
	  bg = loadImage("Resources/menuBG.jpg");							//load default BG
	 
	}
	

	/**
	 * This method sets up the boundaries/structures for a particular level
	 * @param level : this is the level that the player is currently on. MAX 3
	 * 
	 */
	public void loadLevelBoundaries(int level)
	{
	

		if(level == 1)
		{
			
			bg = loadImage("Resources/bg.jpg"); 									//background for 1st level
			
			player = new Ball(25, 30,12,false, 0f,5f, box2d, this); 	// sets up the player
			player.body.setUserData("BallDropped"); 						//sets up the players body ID used for collision detection
				
			inGameseeSaw = new SeeSaw(220, 440, this, box2d);			//sets up the see-saw
			inGameseeSaw.Boundary1.body.setUserData("seesaw1");			//sets up the see-saw body ID used for collision detection

			
			
			  seeSawBallY = height - 65;
			  seeSawBallX = 275;
			  
	    	seesawBall = new Ball(seeSawBallX,seeSawBallY,8,false,0.0f,4f, box2d, this );	//sets up the ball on the see-saw
			seesawBall.body.setUserData("seesawball1");									// sets up the see-saw ball body ID used for collision detection
			  
			boundaries = new ArrayList<Boundary>();						//sets up the boundaries array
			
			boundaries.add(new Boundary(5,platformYPosition,190 ,10,0,false, this, box2d )); 			//add a boundary for the platform
			
			endZone = new Ball(width-50,255,20,false, 0f,2f, box2d, this);			//sets up the endZone
			endZone.body.setUserData("EndZone");										//sets up the endZone body id used for collision detection
		}
		
		if(level == 2)
		{
			  bg = loadImage("Resources/bg2.jpg");									//background for 2nd level
			//  ballLocked = false;
			  player = new Ball(20, 20,12,false, 0f,5f, box2d, this);	// sets up the player
			  player.body.setUserData("BallDropped");						//sets up the players body ID used for collision detection
			  
			  boundaries = new ArrayList<Boundary>();					//sets up the boundaries array
			
			  seeSawBallX = 200;
			  seeSawBallY = 265;
			  
			  platformYPosition = 100;
			  boundaries.add(new Boundary(20,platformYPosition,60 ,10,0,false, this, box2d )); 		//add the platform up the player
			  

			  boundaries.add(new Boundary(300,height,height + 280 ,10,radians(90),false, this, box2d ));	//add the wall in the middle of the level
			  
			  inGameseeSaw = new SeeSaw(150, 295, this, box2d);			//sets up the see-saw
			  inGameseeSaw.Boundary1.body.setUserData("seesaw1");		//sets up the see-saw body ID used for collision detection
			  
			  inGameseeSawTwo = new SeeSaw(425, 440, this, box2d);		//sets up the see-sawTwo 2
			  inGameseeSawTwo.Boundary1.body.setUserData("seesaw2");	//sets up the see-sawTwo body ID used for collision detection
			 
			  seesawBall = new Ball(200,265,8,false,0.0f,4f, box2d, this );			//sets up the ball on the see-saw
			  seesawBall.body.setUserData("seesawball1");								// sets up the see-saw ball body ID used for collision detection
			  seeSawBallTwoX = 485;
			  seeSawBallTwoY = height - 75;
			  seesawBallTwo = new Ball(seeSawBallTwoX - 10,seeSawBallTwoY,8,false,0.0f,0.5f, box2d, this ); //sets up the ball on the see-sawTwo
			  seesawBallTwo.body.setUserData("seesawball2"); 								//sets up the see-saw ball body ID used for collision detection
			  
			  endZone = new Ball(width-50,255,20,false, 0f,2f, box2d, this);			//sets up the end zone
			  endZone.body.setUserData("EndZone");											//sets up the end zone ID used for collision detection
		}
		
		if(level == 3)
		{
			  bg = loadImage("Resources/bg3.jpg");									//background for 2nd level
			
			  player = new Ball(20, 20,12,false, 0f,5f, box2d, this);		// sets up the player
			  player.body.setUserData("BallDropped");							//sets up the players body ID used for collision detection
			
			  boundaries = new ArrayList<Boundary>();						//sets up the boundaries array
			 
			  platformYPosition = 60;
			  boundaries.add(new Boundary(20,platformYPosition,80 ,10,0,false, this, box2d ));					//add the platform up the player 
			  //boundaries.add(new Boundary(150,100,80 ,10,radians(45),false, this, box2d ));		//add the first boundary in the level rotated (45C)
			  boundaries.add(new Boundary(350,100,160 ,10,radians(-145),false, this, box2d ));	//add the first boundary in the level rotated (65C)
			  boundaries.add(new Boundary(155,140,100 ,10,radians(-45),false, this, box2d ));	//add the first boundary in the level rotated (65C)
				
			
			  inGameseeSaw = new SeeSaw(150, 400, this, box2d);			//sets up the see-saw
			  inGameseeSaw.Boundary1.body.setUserData("seesaw1");		//sets up the see-saw body ID used for collision detection
			 // boundaries.add(new Boundary(150,325,120,10,0,false,this,box2d));
			  
			  inGameseeSawTwo = new SeeSaw(345, 440, this, box2d);		//sets up the see-sawTwo 2
			  inGameseeSawTwo.Boundary1.body.setUserData("seesaw2");	//sets up the see-sawTwo body ID used for collision detection
			  
			  seeSawBallX = 200;
			  seeSawBallY = 375;
			  
			  seesawBall = new Ball(200,370,8,false,0f,1f, box2d, this );					//sets up the ball on the see-saw
			  seesawBall.body.setUserData("seesawball1"); 										// sets up the see-saw ball body ID used for collision detection
			  
			  seeSawBallTwoX = 395;
			  seeSawBallTwoY = 415;
			  
			  seesawBallTwo = new Ball(seeSawBallTwoX,seeSawBallTwoY,8,false,0.8f,0f, box2d, this ); 		//sets up the ball on the see-sawTwo
			  seesawBallTwo.body.setUserData("seesawball2"); 									// sets up the see-saw ball two body ID used for collision detection
			  
			  endZone = new Ball(width-50,255,25,false, 0f,2f, box2d, this);				//sets up the end zone
			  endZone.body.setUserData("EndZone"); 												//sets up the end zone ID used for collision detection	  
		}
		loadBoundaries();	// load the default level boundaries
	}
	
	
	/**
	 * Loads the boundaries on the outside of the level, so far cannot go off screen.
	 * NOTE: There is no bottom boundary if the fall off screen it is deleted.
	 */
	public void loadBoundaries()
	{
		boundaries.add(new Boundary(5,5,width*2,10,0,false, this, box2d));					//adds top boundary
		boundaries.add(new Boundary(width-5,height/2,10,height,0,false, this, box2d));		//adds right boundary
		boundaries.add(new Boundary(5,height/2,10,height,0,false, this, box2d));			//adds left boundary
		//boundaries.add(new Boundary(width/4,height-5,width *2,10,0,false, this, box2d));	//adds bottom boundary
		
		boundaries.add(new Boundary(width-50,300,100 ,10,0,false, this, box2d));			//adds the end zone line
		boundaries.add(new Boundary(width-100,255,100 ,10,radians(90),false, this, box2d));	//adds the end zone line rotated
			
		//inGameseeSaw.Boundary1.body.setUserData("seesaw1");
	}
	
	  
	
	/**
	 * This method is detects collision detection automatically through jbox2d library : see jbox2d documentation
	 * @param contactPoint (Used to get the contact point of a collision that is detect in game)
	 */
	public void beginContact(Contact contactPoint)
	{
		
		Fixture f1 = contactPoint.getFixtureA();		//gets the fixture of the first object in collision
		Fixture f2 = contactPoint.getFixtureB();		//gets the fixture of the second object in collision
		
		Body b1 = f1.getBody();							//gets the 1st fixtures body
		Body b2 = f2.getBody();							//gets the 2nd fixtures body
		
		 o1 = b1.getUserData();					//gets the UserData/ID that we set early for the objects 
		 o2 = b2.getUserData();					// used to identify which object are hitting each other.
		
		
		
		
		if(currentState == States.playing)				//we only check the collision when were actually playing the game
		{
		
			if(b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.DYNAMIC)
			{
				//System.out.println(o1 + ":" + o2);
				//System.out.println(currentLevel);
				
				if(o1 == "seesaw1" && o2 == "BallDropped")		// if the player hits the first see-saw
				{
					audio_player_catapult.play();						//we play the catapult sound
					//fill(0,0,255);
					seesawBall.body.setType(BodyType.DYNAMIC);	//change the ball that on the see-saw to dynamic so it can move
				    score += 100 / numberOfBall + maxScore;		//add to the players score 
				    startTime = System.currentTimeMillis();
				    endTime = startTime + 4*1000; 
				    System.out.println(endTime);
				    
				}
				
				if(o1 == "seesaw2" && o2 == "seesawball1" && currentLevel > 1 || o1 == "seesaw2" && o2 == "BallDropped") //if the ball on see-saw 1 hits see-saw 2(only checked in level 2 & 3)
				{
					audio_player_catapult.play();
					seesawBallTwo.body.setType(BodyType.DYNAMIC);
					score += 200 / numberOfBall + maxScore;
				}
					
			}
			
			if(b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.STATIC)
			{
				System.out.println(o1 + ":" + o2);
				if(isEndOfLevel() && currentLevel == 1)
				{
						currentState = States.levelEnd;
						score += (500/numberOfBall) + maxScore;
						
				}
				if(isEndOfLevel() && currentLevel == 2)
				{
						currentState = States.levelEnd;
						score += (750/numberOfBall) + maxScore;
						
				}
				if(isEndOfLevel() && currentLevel == 3)
				{
					    seesawBallTwo.body.setActive(false);
						
						score += (1000/numberOfBall) + maxScore;
						System.out.println();
				}
				
				System.out.println(seesawBall.body.getUserData());
				System.out.println(endZone.body.getUserData());
				if(o1 == "seesawball1" && o2 == "EndZone" && seesawBallTwo.body.isActive() == false)
					currentState = States.win;
			}
		}
		
	}
	
	public boolean isEndOfLevel()
	{
		if(currentLevel == 1)
			return (o1 == "seesawball1" && o2 == "EndZone");
		else
			return (o1 == "seesawball2" && o2 == "EndZone");
	}
	
	/* (non-Javadoc)
	 * @see processing.core.PApplet#draw()
	 * This method is called FRAMERATE per second usually 60 FPS,
	 * Used to draw object to the screen
	 */
	public void draw() 
	{
		 	background(bg);				//set the background to bg image			
		 
			switch(currentState)		//switch statement that checks the current state and display the right content.
			{
				case menu:				//if were at the menu
					pushMatrix();
					theta = 0;
					textIncrement= 20;
					image(soundIcon,width - 55, height - 50); //displays the sound icon
					
					if(isMouseOnSoundIcon()) //if the mouse if on icon
					{
						strokeWeight(3);
						stroke(0,255,0);
						noFill();	//sets noFill for shapes
							
						rect(width - 55,height - 50, soundIcon.width, soundIcon.height);	//draws a rect in the right corner
					}
					
					if(muteSound)	//if muteSound is true
					{
						rectMode(0);
						line(width - 55,height - 50,width + 55,height + 50);				//draw a line through the soundIcon
						rect(width - 55,height - 50, soundIcon.width, soundIcon.height); 	//draw rect around soundIcon
					}
					popMatrix();
					break;
				
				case levelSelect: // if were at the levelSelect screen
					
				pushMatrix();
					background(chooseLevelBg); // displays chooseLevelBg
					noFill();
					strokeWeight(5);
					
					stroke(0,255,0);
					rect(selX,165,193,145);		//displays a green rect around level
					 
					fill(0,0,0);
					image(loadImage("Resources/levelSelectLogo.png"), 20, 70);
					textSize(24);
					text("Use right and left arrows to choose a level:"+ currentLevel, width/2 - 250,height - 100);
					popMatrix();
					
					break;
					
				case playing: // if were playing the game
					
					player.body.setType(BodyType.DYNAMIC);
					if(numberOfBall != 0)
					{
						if(maxScore > 100)
							maxScore -= 0.25 + 1; 	//Every time the loop is ran we decrement the max score by 0.12 so : 0.40 * 60 = 
						
						box2d.step();			//We must always step through time!
					
						for (Boundary wall: boundaries) 
						  wall.display();		 // Display all the boundaries
							 
						player.display(175,0,0);	//Display the player
						  
					    inGameseeSaw.display();	//Display the see-saw
						  
						if(currentLevel != 1)	//if the current level is not 1 then
						{
							inGameseeSawTwo.display();			//display the 2nd in game see-saw
							seesawBallTwo.display(0, 255, 0);	//display the 2nd see-saw ball
							if(currentLevel != 3)
								seesawBall.display(255,0,0);			//display see-saw ball
							else
								seesawBall.display(0,255,0);
						}
						else
							seesawBall.display(0,255,0);			//display see-saw ball
						 
				
						//endZone.display(0,255,0);	//display the endZone
						rect(width-52,275,84,40); //test
						pushMatrix();
						textSize(18);
						//text("HERE", width-75,245);
						fill(0,255,0);
						text("Ball Left " + numberOfBall,width/3 + 200, 40);				//display the no of ball left
						text("Score " + Math.round(score*100.00)/100,width/3 + 300, 40);	//display the score
						popMatrix();
						
						if(player.done() && !isEndOfLevel())
						{
			
							if(System.currentTimeMillis() > endTime)
							{
								  numberOfBall--;
								  player = new Ball(20, 20,12,false, 0f, 5f, box2d, this);
								  player.body.setUserData("BallDropped");  		 
								  startTime = 0;
							}
					    }
						
						if(seesawBall.done())
						{
						
								  if(currentLevel != 3)
									  seesawBall = new Ball(seeSawBallX,seeSawBallY,8,false,1f,4f, box2d, this );
								  else
									  seesawBall = new Ball(seeSawBallX,seeSawBallY,8,false,0f,0.8f, box2d, this );
									  
								  seesawBall.body.setUserData("seesawball1");
								  inGameseeSaw.Boundary1.body.setTransform( inGameseeSaw.Boundary1.body.getPosition(), -10);		 
						}
						
						if(currentLevel > 1)
						{
							if(seesawBallTwo.done())
							{
							  seesawBallTwo = new Ball(seeSawBallTwoX,seeSawBallTwoY,8,false,0.0f,0.5f, box2d, this );
							  seesawBallTwo.body.setUserData("seesawball2");  
							  inGameseeSawTwo.Boundary1.body.setTransform( inGameseeSawTwo.Boundary1.body.getPosition(), -10);
							}
						}
						
						if(showHelp && helpStages <= 4)			//if showHelp is true and they haven't entered help before
							 text("Press SPACE to continue", width/2,height/2);
						
						
						if(helpStages == 1 && showHelp == true) // show first stage of help
						{
							fill(255,124,100);
							textAlign(CENTER);
							text("Objective of Game is to get the green Ball into the Endzone", width/2,height/2 - 150);
							image(loadImage("Resources/right.png"),width/2 + 150,height/2);
						}
						 
						else if(helpStages == 2)				// show second stage of help
						{
							text("This can be done by use of the seesaw ", width/2, height - 100);
							image(loadImage("Resources/left.png"),width/2 + 100,height - 50);
						}
						 
						else if(helpStages == 3)				//show third stage of help
						{
							text("To get the ball off the seesaw to the endzone you use the ball here",width/2,height/2 - 150);
							text("To move the ball press left and right",width/2,height/2 - 50);
							text("You can move the see saw up and down using arrows on keyboard ", width/2, height/2 - 100);
							image(loadImage("Resources/left.png"),width/2 - 100,height/2 - 250);
						}			
						
						 else
							 showHelp = false; // disable help
					}
					else
						currentState = States.gameOver;
					break;
					
				case levelEnd:	//if the user completes the level
					
					pushMatrix();
					textSize(24);
					translate(width/2,height/2);
					textAlign(CENTER);
					strokeWeight(5);
					fill(255);
					text("Congratuations You make it past level one " + currentLevel, 0, -50);
					textSize(12);
					text("Press Enter to advance to Level " + (currentLevel + 1), 0, -25);
					popMatrix();
					// Kill all the bodies
					player.killBody();
					seesawBall.killBody();
					if(currentLevel != 1)
					{
						inGameseeSawTwo.Boundary1.killBody();
						seesawBallTwo.killBody();
					}
					endZone.killBody();
					
					inGameseeSaw.Boundary1.killBody();
					//inGameseeSaw.Boundary2.killBody();
					//inGameseeSaw.boundary3.killBody();
					
					for(Boundary b : boundaries)
						b.killBody();
					
					break;
				
				case gameOver:	//if the user has no life's left
					//when the balls are 0
					pushMatrix();
					translate(width/2, height/2);
					rotate(theta);
					textAlign(CENTER);
					
					
					
					fill(255,0,0);
				
					if(textIncrement != 60 && theta < 6.3)
					{
						textSize((float) textIncrement);
						text("G	A	M	E	O	V	E	R!!!", 0, 0);
						textIncrement = textIncrement + 0.25;
						theta += 0.05;
					}
					
					else
					{
						textSize((float) textIncrement);
						text("G	A	M	E	O	V	E	R!!!", 0, 0);
						textSize(30);
						text("Press SPACE to continue", 0,40);
						
						
					}
					
					// Kill all the bodies
					player.killBody();
					seesawBall.killBody();
					
					if(currentLevel != 1)
					{
						seesawBallTwo.killBody();
						inGameseeSawTwo.Boundary1.killBody();
						inGameseeSawTwo.Boundary2.killBody();
						inGameseeSawTwo.boundary3.killBody();
					}
					
					//endZone.killBody();
					inGameseeSaw.Boundary1.killBody();
					inGameseeSaw.Boundary2.killBody();
					inGameseeSaw.boundary3.killBody();
					
					for(Boundary b : boundaries)
						b.killBody();
					
					popMatrix();
					
					textAlign(0,0);
					rectMode(0);
					fill(255);
					currentLevel = 1;
					
					
					
					break;
					
				case win:
					
					pushMatrix();
					//translate(width/2,height/2);
					image(loadImage("Resources/winningpng.png"),20,height/2 - 150);
					textSize(30);
					text("Press SPACE to continue",80 ,height/2 + 100);
					popMatrix();
					break;
					
				default:	//default
					break;
			}
	}
	
	/* (non-Javadoc)
	 * @see processing.core.PApplet#mousePressed()
	 * Detects when the mouse is pressed,
	 * This method enables and disables the music if the mute button is pressed
	 */
	public void mousePressed()
	{
		if(mousePressed == true && isMouseOnSoundIcon() && muteSound == false)
		  {
			muteSound = true;	//turn sound off
			noMusic();			//pause music
		  }
		
		else if(mousePressed == true && isMouseOnSoundIcon() && muteSound == true)
		  {
			muteSound = false;	//turn sound on
			music();			//start music
		  }
	}
	
	
	/**
	 * Help Method to determine if the mouse is on the soundIcon
	 * @return true if the mouse is on soundIcon,
	 * else return false
	 */
	public boolean isMouseOnSoundIcon()
	{

		//if the mouse is within the soundIcon locations
		if(mouseX >= width - 55 && mouseX < width - 55 + soundIcon.width
				&& mouseY >= height - 50 && mouseY < height - 50 + soundIcon.height)
		{
			return true;
		}
		return false;
	}

	
	/* (non-Javadoc)
	 * @see processing.core.PApplet#keyPressed()
	 * When a key is pressed this method is ran
	 */
	public void keyPressed() 
	  {
		switch(currentState)	//check the state when key is pressed
		{
			case menu:			//if were at the menu 
				if(keyCode == ENTER)
				{
					currentState = States.levelSelect; // change state to levelSelect
					
					audio_player_menuMove.play();			   // play menu move sound
				}
				if(keyCode == KeyEvent.VK_H)		  //if the player presses H
					helpStages = 1;						//enter 1st stage of help
				break;
				
			case levelSelect:	//if were at the levelSelectScreen 
				if(keyCode == RIGHT)
				{
					 if(selX <= 265)				//move the RECT right
					  {
						  selX += 207;
						  currentLevel++;			//change level
						  audio_player_menuMove.play();	//play sound
					  }
					  else							//move RECT back to start
					  {
						  selX = 15;
						  currentLevel = 1;
						  
					  }
				}
				
				if(keyCode == LEFT)		//move the RECT left
				{
					 if(selX > 15)		
					  {
						  selX -= 207;
						  currentLevel--;
						  audio_player_menuMove.play();
					  }
					  else
					  {
						  currentLevel = 1;
					  }
				}
				
				if(keyCode == ENTER)	//if user clicks enter
				{
					currentState = States.playing;		//change state to playing
					loadLevelBoundaries(currentLevel);	// load the boundaries for the level selected
				}
				break;
				
			case playing:				//if were playing
				
				if(keyCode == RIGHT && player.body.getPosition().y >= boundaries.get(0).body.getPosition().y)
					 player.body.setLinearVelocity(new Vec2(8,0));	//move the player right
				else if(keyCode == LEFT && player.body.getPosition().y >= boundaries.get(0).body.getPosition().y)
					 player.body.setLinearVelocity(new Vec2(-10,0));	//move the player left
				else if(keyCode == KeyEvent.VK_H) 	
				{
					if(helpStages < 4)	//only show help once
						showHelp = true;	//showHelp
				}
				else if(keyCode == KeyEvent.VK_SPACE && showHelp == true)	//if the user press the space button
				{
					if(helpStages < 4) // only show help once
						helpStages++;	//move to next stage of help
				}
				
				else if(keyCode == UP)
				{
					platformYPosition -= 10;
					float platformWidth = boundaries.get(0).w;
					float platformXPosition = boundaries.get(0).x;
					
					boundaries.get(0).body.setActive(false);
					boundaries.remove(0);
					boundaries.add(0, new Boundary(platformXPosition,platformYPosition,platformWidth ,10,0,false, this, box2d )); 
					
				}
				else if(keyCode == DOWN)
				{
					platformYPosition += 10;
					
					float platformWidth = boundaries.get(0).w;
					float platformXPosition = boundaries.get(0).x;
			
					boundaries.get(0).body.setActive(false);
					boundaries.remove(0);
					boundaries.add(0, new Boundary(platformXPosition,platformYPosition,platformWidth ,10,0,false, this, box2d )); 
					
				}
				break;
				
			case levelEnd:				//if the user completes the level
				if(keyCode == ENTER)
				{
					currentLevel += 1;
					loadLevelBoundaries(currentLevel);	//move to next level
					currentState = States.playing;
					
				}
				break;
			case gameOver:				//if user dies
				//display game over screen
				if(keyCode == KeyEvent.VK_SPACE)	//if the user press the space button
				{
					numberOfBall = 3;
					//translate(0,0);
					//rectMode(CENTER);
					currentLevel = 1;
					currentState = States.menu;
					bg = loadImage("Resources/menuBG.jpg");
					selX = 15;
				}
				
				break;
				
			case win:
				if(keyCode == KeyEvent.VK_SPACE)	//if the user press the space button
				{
					currentState = States.menu;
					numberOfBall = 3;
					//translate(0,0);
					//rectMode(CENTER);
					currentLevel = 1;
					bg = loadImage("Resources/menuBG.jpg");
					selX = 15;
				}
				break;
				
		default:
			break;
		}			  
	  }


	/**
	 * Starts the music
	 */
	public void music()
	{
		audio_player.play();
		audio_player_menuMove.play();
		audio_player_catapult.play();
	}
	
	/**
	 * Stop/Pause the music
	 */
	public void noMusic()
	{
		audio_player.pause();
		audio_player_menuMove.pause();
		audio_player_catapult.pause();
		
	}
	

	public static void main(String[] args)
	{
		PApplet.main(new String[] { "org.ittd.imd.ca.Game"});
	}
}  

