import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Point;

/**
 * Write a description of class Game3Dock here.
 * 
 * @author C. Karreman
 * @version 1.0
 */
public class Game3World extends World
{
    public static String[] colors = {"red","green","yellow","blue"}; 
    public static int gridSize = 40;
    
    /**
     * Game vars
     */
    private int level = 1; // Game level
    private int score = 0; // Game score
    private int crashes = 0; // Crash counter
    private int maxCrashes = 5; // Maximum crashes before game over
    private int maxTrucks = 5; // Maximum number of programmable trucks
    private int timeOut = 300; // Timeout for the pragramming stage
    private boolean firstStep = true; // Does the first step routine
    private boolean running = false; // Flag to indicate a running game (may be paused by Greenfoot class)
    private Date startTime;
    private String lastKey = "";
    //private GreenfootSound backgroundMusic = new GreenfootSound("theme.mp3");

    /*
     * The running stages
     */
    private GameStage gameStage = GameStage.WAITING;
    private G3_Truck programming;
    private int programStep;
    
    
    /**
     * Constructor for objects of class Game3Dock.
     * 
     */
    public Game3World()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        // This is a setting for the speed of the game. Tested to be playable.
        Greenfoot.setSpeed(45);
        
        //backgroundMusic.setVolume(70);
        
        // Prepare the game
        prepare();
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        // Nothing to prepare, all is done in startGame()
    }
    
    /**
     * Does the run initialization. To prevent 
     */
    private void firstStep()
    {
        // Reset the flag to prevent another call to this function
        firstStep = false;
        
        // Reset counters
        crashes = 0;
        score = 0;
        showText("Level: ", 60, 20);
        addScore(0);
        addCrash(0);
        
        // Start music
        //backgroundMusic.playLoop();
        
        // And show game info overlay
        G3_GameInfo g3_info = new G3_GameInfo();
        addObject(g3_info, getWidth() / 2, getHeight() / 2); // Centered on screen
    }
    
    /**
     * StartGame is called to start a new game. Score and other counters are reset
     * to begin with the game defaults.
     */
    public void startGame(int nextLevel)
    {
        // First clear all current trucks
        programming = null;
        programStep = 0;
        for(Actor a : getObjects(G3_FieldObject.class))
        {
            removeObject(a);
        }
        
        // Then init all game vars
        level = nextLevel;
        showText("Level: "+level, 60, 20);
        
        // Depending on the level the timeout will be less
        if (level > 5)
        {
            timeOut = 300 - ((level -5) * 10);
        }
        else
        {
            timeOut = 300;
        }
        
        if (level > 2)
        {
            createObstacles(Math.min(level, maxTrucks));
        }
        
        // In the first levels create as many trucks as the level but create at most maxTrucks
        createTrucks(Math.min(level, maxTrucks));
        
        // Create the program options
        addObject(new G3_CmdLeft(), 655, 505);
        addObject(new G3_CmdRight(), 745, 505);
        addObject(new G3_CmdForward(), 700, 505);
        addObject(new G3_CmdFFwd(), 725, 565);
        addObject(new G3_CmdPark(), 675, 565);
        addObject(new G3_BtnAllDone(), 700, 20);
                
        // To show the current score call AddScore()
        addScore(0);
        // To show the current crashes call AddCrash()
        addCrash(0);
        
        // The vars reset
        gameStage = GameStage.PROGRAMMING;
        selectNextTruck();
        startTime = new Date();
        running = true;
    }

    /**
     * The end of the game.
     */
    public void gameOver()
    {
        // Reset running flag and stop game loop
        running = false;
        
        Actor gameOver = new GameOver();
        addObject(gameOver,getWidth() / 2, getHeight() / 2);
        
        //backgroundMusic.pause();
       
        Greenfoot.stop();
    }

    /**
     * Act is the game step. In this world boats are created with a time interval. A boat
     * mut be selected to take over the controls. Use "S" to select a boat or click on one.
     */
    public void act()
    {
        // Only start music when running the game
        if (firstStep)
        {
            firstStep();
        }

        // Check running, when not running nothing is done. Game info is till visible.
        if (running)
        {
            // Remember the last keystroke to prevent multiple actions
            if (lastKey == "" || !Greenfoot.isKeyDown(lastKey))
            {
                // The s-key selects the next boat for controls.
                if (Greenfoot.isKeyDown("s"))
                {
                    lastKey = "s";
                    selectNextTruck();
                }
                else if (Greenfoot.isKeyDown("m"))
                {
                    lastKey = "m";
                    //if (backgroundMusic.isPlaying())
                    //{
                    //    backgroundMusic.stop();
                    //}
                }
                else if (Greenfoot.isKeyDown("p"))
                {
                    lastKey = "p";
                    //if (!backgroundMusic.isPlaying())
                    //{
                    //    backgroundMusic.playLoop();
                    //}
                }
                else
                {
                    lastKey = "";
                }
            }

            // Check the gamestage and act on what to do in this stage
            if (gameStage == GameStage.PROGRAMMING)
            {
                // Check if the time is up or the done is hit
                int timer = timeOut - Math.toIntExact((new Date().getTime() - startTime.getTime()) / 1000);
                String s = String.format("Time left: %d:%02d", (int)Math.floor(timer / 60), timer % 60);
                showText(s, 480, 20);
                
                // when time is up we must go to the next stage
                if (timer <= 0)
                {
                    startStage(GameStage.RUNNINGCODE, "Time is up, let's see where the trucks go!!");
                }
            }
            else if (gameStage == GameStage.RUNNINGCODE)
            {
                // All truck must execute their next program step
                boolean allDone = true;
                for(G3_Truck t : getObjects(G3_Truck.class))
                {
                    allDone = allDone && !t.getExecuting();
                }
                
                if (allDone)
                {
                    boolean hasMore = false;
                    for(G3_Truck t : getObjects(G3_Truck.class))
                    {
                        hasMore = hasMore || (t.getProgramLength() > 0);
                    }
                    
                    if (hasMore)
                    {
                        Greenfoot.delay(20);
                        programStep++;
                        showText("Execute step: " + programStep, 700, 60);
                        
                        for(G3_Truck t : getObjects(G3_Truck.class))
                        {
                            t.executeStep();
                        }
                    }
                    else
                    {
                        setStage(GameStage.EVALUATING);
                    }
                }
            }
            else if (gameStage == GameStage.EVALUATING)
            {
                // TODO: Count the points and show the results
                int scored = 0;
                
                for(G3_Destination d : getObjects(G3_Destination.class))
                {
                    for(G3_Truck t : getObjects(G3_Truck.class))
                    {
                        if (t.getColor() == d.getColor() && t.getX() == d.getX() && t.getX() == d.getX())
                        {
                            t.setActive(true);
                            scored++;
                        }
                    }
                }
                
                String msg = "";
                if (scored == 0)
                {
                    msg = "You scored no points this round. Too bad.\nTry harder next level.";
                }
                else if (scored > 3)
                {
                    msg = String.format("Wow you scored %d points, good job!!!\nOn to the next level.", scored);
                }
                else
                {
                    msg = String.format("You scored %d points, nice!\nOn to the next level.", scored);
                }
                addScore(scored);
                startStage(GameStage.PROGRAMMING, msg);
            }
            
            // Check crashcounter and game over maxCrashes is reached
            if (crashes >= maxCrashes)
            {
                gameOver();
            }
        }
    }

    /**
     * Create the number of trucks in a recursive loop
     */
    private void createTrucks(int count)
    {
        // Don't create when counter reaches 0
        if (count > 0)
        {
            // Now, create a truck with a random color
            String color = colors[Greenfoot.getRandomNumber(colors.length)];
            G3_Truck truck = new G3_Truck(color, count);
            G3_Destination dest = new G3_Destination(color);
            
            // And find a location for the truck and for the destination
            // Play field is h x w = 14 x 15. destinations stay left, trucks on the right
            int orientation = Greenfoot.getRandomNumber(4) * 90;
            Point coord = getValidLocation(orientation, 0, 7);
            
            dest.setRotation(orientation);
            addObject(dest, getRealX(coord.x), getRealY(coord.y));
            repaint();
            
            // And for the truck
            orientation = 90 + Greenfoot.getRandomNumber(3) * 90; // Not facing water
            coord = getValidLocation(orientation, 8, 15);
            
            truck.setRotation(orientation);
            addObject(truck, getRealX(coord.x), getRealY(coord.y));
            repaint();
            
            // And create next one
            createTrucks(count-1);
        }
    }
    
    private void createObstacles(int count)
    {
        if (count > 0)
        {
            // TODO: Create static containers on the dock. 
            // Not too close to the borders of the playfield.
            int orientation = Greenfoot.getRandomNumber(2) * 90;
            Point coord = getValidLocation(orientation, 3, 12, 3, 11);
            
            G3_Obstacle obj = new G3_Obstacle();
            obj.setRotation(orientation);
            addObject(obj, coord.x * 40 + 20, coord.y * 40 + 60);
            repaint();
            
            createObstacles(count - 1);
        }
    }
    
    /**
     * Overload with no Y boundaries
     */
    private Point getValidLocation(int orientation, int minX, int maxX)
    {
        return getValidLocation(orientation, minX, maxX, 0, 14);
    }
    
    /**
     * Gets a valid location 
     */
    private Point getValidLocation(int orientation, int minX, int maxX, int minY, int maxY)
    {
        // Play field is h x w = 14 x 15. destinations stay left, trucks on the right
        Point result = new Point(minX + Greenfoot.getRandomNumber(maxX - minX), 
                                 minY + Greenfoot.getRandomNumber(maxY - minY));
        int tryCounter = 0;
        while(!testLocation(orientation, result) && tryCounter < 100)
        {
            result = new Point(minX + Greenfoot.getRandomNumber(maxX - minX), 
                               minY + Greenfoot.getRandomNumber(maxY - minY));
            tryCounter++;
        }
        
        //System.out.println("tested "+tryCounter+" coord(x:"+result.x+",y:"+result.y+",r:"+orientation+")");
        
        return result;
    }
    
    private boolean testLocation(int orientation, Point coord)
    {
        //  Check if an object can be found on the destination coordinates
        List<G3_FieldObject> l = getObjectsAt(getRealX(coord.x), getRealY(coord.y), G3_FieldObject.class);
        
        if (orientation % 180 == 0 && coord.x > 0 && coord.x < 14)
        {
            // Test horizontal cells
            l.addAll(getObjectsAt(getRealX(coord.x - 1), getRealY(coord.y), G3_FieldObject.class));
            l.addAll(getObjectsAt(getRealX(coord.x + 1), getRealY(coord.y), G3_FieldObject.class));
        }
        else if (orientation % 180 != 0 && coord.y > 0 && coord.y < 13)
        {
            // Test vertical cells
            l.addAll(getObjectsAt(getRealX(coord.x), getRealY(coord.y - 1), G3_FieldObject.class));
            l.addAll(getObjectsAt(getRealX(coord.x), getRealY(coord.y + 1), G3_FieldObject.class));
        }
        else 
        {
            // Coordinates not valid by orientation boudaries
            return false;
        }
        
        return (l.size() == 0);
    }
    
    public int getRealX(int x)
    {
        return (x * gridSize) + (gridSize / 2);
    }
    
    public int getRealY(int y)
    {
        return ((y + 1) * gridSize) + (gridSize / 2);
    }
    
    /**
     * When in programming stage a truck is selected and the program is made
     * visible and editable for the user
     */
    private void selectNextTruck()
    {
        if (gameStage == GameStage.PROGRAMMING)
        {
            int nextID = 1;
            List<G3_Truck> trucks = getObjects(G3_Truck.class);

            if (programming != null)
            {
                // ID's start at 1
                nextID = programming.getID() + 1;
                if (nextID > trucks.size())
                {
                    nextID = 1;
                }
            }
            
            for(G3_Truck t : trucks)
            {
                if (t.getID() == nextID)
                {
                    selectTruckForProgramming(t);
                    break;
                }            
            }
        }
    }
    
    public void selectTruckForProgramming(G3_Truck truck)
    {
        if (programming != null)
        {
            programming.setActive(false);
        }
        
        programming = truck;
        programming.setActive(true);
        showText("Program truck: " + programming.getID(), 700, 60);
        
        // TODO: Display last x program steps
    }
    
    public void startStage(GameStage stage, String message)
    {
        // In program mode the active truck blink, deactivate all trucks
        if (gameStage == GameStage.PROGRAMMING)
        {
            for(G3_Truck t : getObjects(G3_Truck.class))
            {
                t.setActive(false);
            }
        }
        
        // Set the gamestage to waiting and show the messagebox
        gameStage = GameStage.WAITING;
        
        // And show game info overlay
        G3_Waitbox g3_wait = new G3_Waitbox(stage, message);
        addObject(g3_wait, getWidth() / 2, getHeight() / 2); // Centered on screen
    }
    
    public void addScore(int increase)
    {
        score += increase;
        showText("Score: "+score, 180, 20);
    }

    public void addCrash(int increase)
    {
        crashes += increase;
        showText("Crashes: "+crashes, 320, 20);
    }
    
    /**
     * Set the stage from other actors
     */
    public void setStage(GameStage stage)
    {
        if (stage == GameStage.PROGRAMMING)
        {
            startGame(level + 1);
        }
        else
        {
            // Clear all programming tools
            for (G3_BtnAllDone o : getObjects(G3_BtnAllDone.class))
            {
                removeObject(o);
            }
            for (G3_Command o : getObjects(G3_Command.class))
            {
                removeObject(o);
            }
        
            gameStage = stage;
        }
    }
}
