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
public class Game4Hunter extends Game
{

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    
    public static final String[] roles = {"criminal", "criminal", "pop1","criminal", "pop2", "pop3"};
    public static final String[] obstacles = {"obs1", "obs2", "obs3"};
    public static final String[] cargos = {"cont1", "cont2","cont3","cont4"};
    public static final int[] rndYs = {140, 240, 340, 440};
    public static final int[] rndXs = {198, 327, 456, 585};
    
    private boolean[] CargoPlaced = new boolean[16];
    
    private int newManInterval = 80;
    private int newManSteps = 0;
    private int manCounter = 0;
    int score = 0;
    private int missed = 0;
    private int maxMissed = 3;
    private boolean firstStep = true;
    private boolean running = false;
    private int hidden = 0;
    private int timerHidden = 0;
    private int HIDDEN_TIMEOUT = Greenfoot.getRandomNumber(60);
    private String lastKey = "";
    private int newContainerInterval = 3;
    
    private GreenfootSound backgroundMusic = new GreenfootSound("g4club_dance.mp3");
    
    
    public Game4Hunter()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
     
       
        Greenfoot.setSpeed(40);
        
        backgroundMusic.setVolume(60);
        
        prepare();
        
        
    }
    
    private void prepare()
    {
       
        G4Officer g4_officer = new G4Officer();
        addObject(g4_officer, 100, 550);
        
        G4Entry g4_entry = new G4Entry();
        addObject(g4_entry, 20, 270);
        g4_entry.setRotation(90);
        
        setPaintOrder(G4GameOver.class, G4Complete.class, G4Cloud.class, G4Staff.class, G4Officer.class);
       
    }
    
    private void firstStep()
    {
        firstStep = false;
        
        //Start music
        backgroundMusic.playLoop();
        
        //Add show game info overlay
        G4Gameinfo g4_info = new G4Gameinfo();
        addObject(g4_info, getWidth()/2, getHeight()/2);
    }
    
    public void startGame()
    {
        newManInterval = 70;
        manCounter = 0;
        missed = 0;
        hidden = 0;
        score = 0;
        running = true;
        
        addScore(0);
        addHidden(0);
        addMissed(0);
        
        setObstacle();
        
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
            if (lastKey == "" || !Greenfoot.isKeyDown(lastKey))
            {
                if (Greenfoot.isKeyDown("m"))
                {
                    lastKey = "m";
                    if (backgroundMusic.isPlaying())
                    {
                        backgroundMusic.stop();
                    }
                }
                else if (Greenfoot.isKeyDown("p"))
                {
                    lastKey = "p";
                    if (!backgroundMusic.isPlaying())
                    {
                        backgroundMusic.playLoop();
                    }
                }
                else
                {
                    lastKey = "";
                }
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
        
        G4Staff man = new G4Staff();
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
            Greenfoot.playSound("g4alarm.mp3");
        }
        
        if (manCounter % 9 == 0)
        {
            createCloud();
        }
    }
    
    private void setObstacle()
    {
        String obstacle = obstacles[Greenfoot.getRandomNumber(obstacles.length)];
        
        G4Obstacle obs1 = new G4Obstacle();
        addObject(obs1, 600, 570);
        obs1.setValue(obstacle);
        
        G4Obstacle obs2 = new G4Obstacle();
        addObject(obs2, 100, 30);
        obs2.setValue(obstacle);
        
        G4Obstacle obs3 = new G4Obstacle();
        addObject(obs3, 650, 30);
        obs3.setValue(obstacle);
        
    }
    
    
    public void setContainer()
    {
        boolean foundEmpty = false;
        int rndY=0;
        int rndX=0;
        while(!foundEmpty){
            int indexX = Greenfoot.getRandomNumber(rndXs.length);
            int indexY = Greenfoot.getRandomNumber(rndYs.length);
            rndY = rndYs[indexY];
            rndX = rndXs[indexX];
            System.out.println(rndY+", "+rndX);
            if(CargoPlaced[indexY*4+indexX]!=true){
                CargoPlaced[indexY*4+indexX]=true;
                foundEmpty=true;
            }
        }
        
        String cargo = cargos[Greenfoot.getRandomNumber(cargos.length)];
        
        G4Container container = new G4Container();
        addObject(container, rndX, rndY);
        container.setCargo(cargo);
    }
    
    private void createCloud()
    {
        int rndY = Greenfoot.getRandomNumber(400);
        
        G4Cloud cloud = new G4Cloud();
        addObject(cloud, 5, rndY+100);

    }
    
    private void checkClouds()
    {
        for(G4Cloud cloud : getObjects(G4Cloud.class))
            if (cloud.isAtEdge())
            {
                removeObject(cloud);
            }
    }
    
    public void gameOver()
    {

        running = false;
        
        Actor gameOver = new G4GameOver();
        addObject(gameOver, getWidth()/2, getHeight()/2);
        
        backgroundMusic.pause();
        Greenfoot.playSound("g4down_scale.mp3");
        Greenfoot.stop();
        
    }
    
    public void missionComplete()
    {
        running = false;
        
        Actor missionComplete = new G4Complete();
        addObject(missionComplete, getWidth()/2, getHeight()/2);
        
        backgroundMusic.pause();
        Greenfoot.playSound("g4up_scale.mp3");
        Greenfoot.stop();
    }
    
    public void addCriminal(int hidden)
    {
        int rndY = Greenfoot.getRandomNumber(400);
        int rndX = Greenfoot.getRandomNumber(650);
        
        G4Staff man = new G4Staff();
        addObject(man, rndX + 100, rndY + 100);
        man.setRotation(rndY/-2);
        man.setRole("criminal");
    }
    
    public void addScore(int increase)
    {
        score += increase;
        showText("Score: " +score, 740,20);
        
        if (score >= newContainerInterval*14 + 1){
            missionComplete();
        }
        
        if (score % newContainerInterval == 0){
            setContainer();
        }
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
