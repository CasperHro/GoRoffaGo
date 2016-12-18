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
    
    public static final String[] roles = {"criminal", "criminal", "criminal", "pop1","pop2", "pop3"};
    public static final String[] obstacles = {"obs1", "obs2", "obs3"};
    public static final String[] cargos = {"cont1", "cont2","cont3","cont4"};
    public static final int[] rndYs = {140, 240, 340, 440};
    public static final int[] rndXs = {198, 327, 456, 585};
    
    private int newManInterval = 90;
    private int newManSteps = 0;
    private int manCounter = 0;
    private int score = 0;
    private int missed = 0;
    private int maxMissed = 3;
    private boolean firstStep = true;
    private boolean running = false;
    private int hidden = 0;
    private int timerHidden = 0;
    private int HIDDEN_TIMEOUT = Greenfoot.getRandomNumber(60);
    
    private GreenfootSound backgroundMusic = new GreenfootSound("tetris_theme.mp3");
    
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
        hidden = 0;
        score = 0;
        running = true;
        
        addScore(0);
        addHidden(0);
        addMissed(0);
        
        setObstacle();
        setContainer();
        
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
        
        checkClouds();
        
        if (missed >= maxMissed)
        {
            gameOver();
        }
        
        if (hidden >= 1) 
        {
            timerHidden++;
            if (timerHidden >= HIDDEN_TIMEOUT + 10)
            {
                timerHidden = 0;
                addCriminal(hidden);
                hidden = 0;
                return;
            }
        }
 
    }
    
    private void createMan()
    {
        int rndY = Greenfoot.getRandomNumber(40);
        String role = roles[Greenfoot.getRandomNumber(roles.length)];
        
        G4_Staff man = new G4_Staff();
        addObject(man, 5, 270 + rndY);
        man.setRotation(rndY/-5);
        man.setRole(role);
        manCounter++;
        newManSteps = 0;
        
        if (manCounter % 10 == 0)
        {
            newManInterval -= 1;
        }
        
        if (role == "criminal")
        {
            Greenfoot.playSound("alarm.mp3");
        }
        
        if (manCounter % 9 == 0)
        {
            createCloud();
        }
    }
    
    private void setObstacle()
    {
        String obstacle = obstacles[Greenfoot.getRandomNumber(obstacles.length)];
        
        G4_Obstacle obs1 = new G4_Obstacle();
        addObject(obs1, 600, 570);
        obs1.setValue(obstacle);
        
        G4_Obstacle obs2 = new G4_Obstacle();
        addObject(obs2, 100, 30);
        obs2.setValue(obstacle);
        
        G4_Obstacle obs3 = new G4_Obstacle();
        addObject(obs3, 650, 30);
        obs3.setValue(obstacle);
        
    }
    
    private void setContainer()
    {
        int rndY = rndYs[Greenfoot.getRandomNumber(rndYs.length)];
        int rndX = rndXs[Greenfoot.getRandomNumber(rndXs.length)];
        String cargo = cargos[Greenfoot.getRandomNumber(cargos.length)];
        
        G4_Container container = new G4_Container();
        addObject(container, rndX, rndY);
        container.setCargo(cargo);
    }
    
    private void createCloud()
    {
        int rndY = Greenfoot.getRandomNumber(400);
        
        G4_Cloud cloud = new G4_Cloud();
        addObject(cloud, 5, rndY+100);

    }
    
    private void checkClouds()
    {
        for(G4_Cloud cloud : getObjects(G4_Cloud.class))
            if (cloud.isAtEdge())
            {
                removeObject(cloud);
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
    
    
    public void addCriminal(int hidden)
    {
        int rndY = Greenfoot.getRandomNumber(500);
        int rndX = Greenfoot.getRandomNumber(600);
        
        G4_Staff man = new G4_Staff();
        addObject(man, 200+ rndX, rndY - 100);
        man.setRotation(rndY/-2);
        man.setRole("criminal");
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
    
    public void addHidden(int increase)
    {
        hidden += increase;
    }
}
