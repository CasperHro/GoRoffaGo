import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author Y.Kriulina 
 * @version 1.0
 */
public class G4Hunter extends Game
{
    private static final String TEXT = "Navigator : game info\n\n"+
                  "Your mission is to catch all criminals which come inside the port.\n"+
                  "When a criminal is catched a point is scored!\n\n"+
                  "Be alert! They can reach warehouses and they are missed.\n\n"+
                  "They can also hide inside the containers and appear anywhere again!\n\n"+
                  "Use the arrows to navigate:\n"+
                  "up/down - go ahead/ go back\n"+
                  "left/right - rotate left/right\n\n"+
                  "When you miss 3 criminanls, you're game over!\n\n\n"+
                  "Click or press Enter to start...";
    private static final String TEXTSTART = "Get ready...\n\nHere we go!!!";
    
    private static final String CRIMINAL = "criminal";

    protected static final String[] roles = {CRIMINAL, CRIMINAL, "pop1",CRIMINAL, "pop2"};
    protected static final String[] obstacles = {"obs1", "obs2", "obs3"};
    protected static final String[] cargos = {"cont1", "cont2","cont3","cont4"};
    protected static final int[] rndYs = {140, 240, 340, 440};
    protected static final int[] rndXs = {198, 327, 456, 585};
    
    private boolean[] cargoPlaced = new boolean[16];
    
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
    private int hiddenTimeout = Greenfoot.getRandomNumber(60);
    private int newContainerInterval = 3;
    
    /**
     * Constructor for objects of class MyWorld.
     */
    public G4Hunter()
    {    
        Greenfoot.setSpeed(40);
        
        backgroundMusic = new GreenfootSound("g4club_dance.mp3");
        backgroundMusic.setVolume(60);
        
        prepare();
    }
    
    private void prepare()
    {
       
        G4Officer g4officer = new G4Officer();
        addObject(g4officer, 100, 550);
        
        G4Entry g4entry = new G4Entry();
        addObject(g4entry, 20, 270);
        g4entry.setRotation(90);
        
        setPaintOrder(GameOver.class, G4Cloud.class, G4Staff.class, G4Officer.class);
       
    }
    
    private void firstStep()
    {
        firstStep = false;
        
        //Start music
        backgroundMusic.playLoop();
        
        // And show game info overlay
        GameInfo ginfo = new GameInfo(TEXT, TEXTSTART);
        addObject(ginfo, getWidth() / 2, getHeight() / 2); // Centered on screen
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
            checkKeys();
            if (missed >= maxMissed)
            {
                gameOver();
            }
            
            if (score >= newContainerInterval*15 + 1)
            {
                missionComplete();
            }
        }
        
        checkClouds();
        
        if (hidden >= 1) 
        {
            timerHidden++;
            if (timerHidden >= hiddenTimeout + 10)
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
        
        if (role == CRIMINAL)
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
            
            if(!cargoPlaced[indexY*4+indexX]){
                cargoPlaced[indexY*4+indexX]=true;
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
        
        addObject(new GameOver("G4GameOver.png"), getWidth()/2, getHeight()/2);
        
        backgroundMusic.stop();
        Greenfoot.playSound("g4down_scale.mp3");
    }
    
    public void missionComplete()
    {
        running = false;
        
        addObject(new GameOver("G4MissionComplete.png"), getWidth()/2, getHeight()/2);
        
        backgroundMusic.stop();
        Greenfoot.playSound("g4up_scale.mp3");
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
