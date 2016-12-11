import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.*;
import java.util.List;

/**
 * Write a description of class MyWorld here.
 * 
 * @author Y.Kriulina 
 * @version 1.0
 */
public class Game4Hunter extends World
{

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    
    private int newManInterval = 90;
    private int newManSteps = 0;
    private int manCounter = 0;
    private int score = 0;
    private int missed = 0;
    private int maxMissed = 5;
    private boolean firstStep = true;
    private boolean running = false;
    private String role = "criminal"; 
    
    private GreenfootSound backgroundMusic = new GreenfootSound("club_dance.mp3");
    
    public Game4Hunter()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        
        Greenfoot.setSpeed(40);
        
        backgroundMusic.setVolume(70);
        
        prepare();
        
        
    }
    
    private void prepare()
    {
        //add obstacles to theWorld
        G4_Obstacle g4_obstacle1 = new G4_Obstacle();
        addObject(g4_obstacle1, 196, 430);
        g4_obstacle1.setRole("pop1");
        
        G4_Obstacle g4_obstacle2 = new G4_Obstacle();
        addObject(g4_obstacle2, 246, 132);
        g4_obstacle1.setRole("pop2");
        
        G4_Obstacle g4_obstacle3 = new G4_Obstacle();
        addObject(g4_obstacle3, 645, 181);
        g4_obstacle1.setRole("pop3");
        
        G4_Obstacle g4_obstacle4 = new G4_Obstacle();
        addObject(g4_obstacle4, 530, 404);
        g4_obstacle4.setRotation(90);
        
  
        G4_Officer g4_officer = new G4_Officer();
        addObject(g4_officer, 100, 550);
        
        G4_Entry g4_entry = new G4_Entry();
        addObject(g4_entry, 20, 270);
        g4_entry.setRotation(90);
    }
    
    private void firstStep()
    {
        firstStep = false;
        
        //Start music
        backgroundMusic.playLoop();
        
        //Add show game info overlay
        G4_Gameinfo g4_info = new G4_Gameinfo();
        addObject(g4_info, getWidth()/2, getHeight()/2);
    }
    
    public void startGame()
    {
        newManInterval = 90;
        manCounter = 0;
        missed = 0;
        
        score = 0;
        running = true;
        
        addScore(0);
        
        addMissed(0);
        
        newManSteps = newManInterval - 5;

    }
    
    public void act()
    {
        if (firstStep)
        {
            firstStep();
        }
        
        if (running)
        {
            newManSteps++;
            
            if(newManSteps > newManInterval)
            {
                createMan();
            }
        }
        
        if (missed >= maxMissed)
        {
            gameOver();
        }
       
    }
    
    private void createMan()
    {
        List<G4_Obstacle> roles = getObjects(G4_Obstacle.class);
        int rndY = Greenfoot.getRandomNumber(40);
        int rndRole = Greenfoot.getRandomNumber(roles.size());
        
        G4_Staff man = new G4_Staff();
        addObject(man, 5, 270 + rndY);
        man.setRotation(rndY/-2);
        man.setRole(roles.get(rndRole).getRole());
        manCounter++;
        newManSteps = 0;
        
        if (manCounter % 7 == 0)
        {
            newManInterval -= 2;
        }
    }
    
    public void gameOver()
    {

        running = false;
        
        Actor gameOver = new G4_GameOver();
        addObject(gameOver, getWidth()/2, getHeight()/2);
        
        backgroundMusic.pause();
        
        Greenfoot.stop();
        
    }
    
    public void addScore(int increase)
    {
        score += increase;
        showText("Score: " +score, 740,20);
    }
    
    public void addMissed(int increase)
    {
        missed += increase;
        showText("Missed: " +missed, 740,580);
    }
}
