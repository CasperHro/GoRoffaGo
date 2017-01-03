import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Point;

/**
 * Write a description of class Game3Dock here.
 * 
 * @author C. Karreman
 * @version 1.0
 */
public class G3World extends World
{
    public static final int GRIDSIZE = 40;
    private static final int GRIDWIDTH = 15;
    private static final int GRIDHEIGHT = 14;
    
    private static final String[] colors = {"red","green","yellow","blue"}; 
    
    /**
     * Game vars
     */
    private int level = 1; // Game level
    private int score = 0; // Game score
    private int crashes = 0; // Crash counter
    private int maxCrashes = 5; // Maximum crashes before game over
    private int maxTrucks = 5; // Maximum number of programmable trucks
    private int timeOut = 300; // Timeout for the pragramming stage
    private int delay = 0; // Delay before action (decrease counter
    private boolean firstStep = true; // Does the first step routine
    private boolean running = false; // Flag to indicate a running game (may be paused by Greenfoot class)
    private Date startTime; // Used for the countdown timer
    private String lastKey = ""; // For preventing multiple actions on the same keydo
    private GreenfootSound backgroundMusic = new GreenfootSound("lazy_susan.mp3");

    /*
     * The running stages
     */
    private GameStage gameStage = GameStage.WAITING;
    private G3Truck programming;
    private int programStep;
    
    
    /**
     * Constructor for objects of class Game3Dock.
     * 
     */
    public G3World()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        // This is a setting for the speed of the game. Tested to be playable.
        Greenfoot.setSpeed(45);
        setPaintOrder(G3GameInfo.class,
                      G3Waitbox.class,
                      G3Tutorial.class,
                      GameOver.class,
                      G3Explosion.class,
                      G3Truck.class,
                      G3Destination.class,
                      G3Obstacle.class
                      );
        
        backgroundMusic.setVolume(70);
        
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
        backgroundMusic.playLoop();
        
        // And show game info overlay
        G3GameInfo g3info = new G3GameInfo();
        addObject(g3info, getWidth() / 2, getHeight() / 2); // Centered on screen
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
        removeObjects(getObjects(G3FieldObject.class));
        removeObjects(getObjects(G3Explosion.class));
        
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
        
        if (level > 4)
        {
            createObstacles(Math.min(level-3, maxTrucks));
        }
        
        // First levels are tutorials
        if (level == 1)
        {
            createTutorial1();
        }
        else if (level == 2)
        {
            createTutorial2();
        }
        else if (level == 3)
        {
            createTutorial3();
        }
        else
        {
            // In the first levels create as many trucks as the level but create at most maxTrucks
            createTrucks(Math.min(level - 2, maxTrucks));
        }
        
        // Create the program options
        addObject(new G3CmdForward(), 700, 505);
        addObject(new G3CmdLeft(), 655, 505);
        addObject(new G3CmdRight(), 745, 505);
        addObject(new G3CmdFFwd(), 725, 565);
        addObject(new G3CmdPark(), 675, 565);
        addObject(new G3BtnAllDone(), 700, 20);
                
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
        
        backgroundMusic.pause();
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
            checkKeys();

            // Check the gamestage and act on what to do in this stage
            if (gameStage == GameStage.PROGRAMMING)
            {
                // Check if the time is up or the done is hit
                int timer = timeOut - Math.toIntExact((new Date().getTime() - startTime.getTime()) / 1000);
                showText(String.format("Time left: %d:%02d", timer / 60, timer % 60), 480, 20);
                
                // when time is up we must go to the next stage
                if (timer <= 0)
                {
                    startStage(GameStage.RUNNINGCODE, "Time is up, let's see where the trucks go!!");
                }
            }
            else if (gameStage == GameStage.RUNNINGCODE)
            {
                runCode();
            }
            else if (gameStage == GameStage.EVALUATING)
            {
                evaluate();
            }
            
            // Check crashcounter and game over maxCrashes is reached
            if (crashes >= maxCrashes)
            {
                gameOver();
            }
        }
    }

    private void checkKeys()
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

    private void runCode()
    {
        // All truck must execute their next program step
        boolean allDone = true;
        boolean hasMore = false;
        for(G3Truck t : getObjects(G3Truck.class))
        {
            allDone = allDone && !t.getExecuting();
            hasMore = hasMore || (t.getProgramLength() > 0);
        }
        
        // When all trucks are done moving the next command can be executed
        // First check if at least one truck has more steps
        if (allDone && hasMore && isDelayed(20))
        {
            doNextStep();
        }
        
        // When executing is done and there are no more commands in any program 
        // start the evaluate stage.
        if (allDone && !hasMore)
        {
            showText("No more steps", 700, 60);
            if (isDelayed(10))
            {
                // When all trucks are done go to the next stage
                setStage(GameStage.EVALUATING);
            }
        }
    }

    private void doNextStep()
    {
        programStep++;
        showText("Execute step: " + programStep, 700, 60);
        
        for(G3Truck t : getObjects(G3Truck.class))
        {
            t.executeStep();
        }
    }

    private void evaluate()
    {
        // Start flashing of rightly parked trucks immediately
        if (delay == 0)
        {
            for(G3Destination d : getObjects(G3Destination.class))
            {
                
                G3Truck t = d.getParkedTruck();
                if (t != null)
                {
                    t.setActive(true);
                }
            }
        }
        
        if (isDelayed(40))
        {
            // Count the points and show the results
            int scored = 0;
            for(G3Destination d : getObjects(G3Destination.class))
            {
                G3Truck t = d.getParkedTruck();
                if (t != null)
                {
                    scored++;
                }
            }

            addScore(scored);
            startStage(GameStage.PROGRAMMING, getEndMessage(scored));
        }
    }
    
    private String getEndMessage(int score)
    {
        if (score == 0)
        {
            return "You scored no points this round. Too bad.\nTry harder next level.";
        }
        else if (score > 3)
        {
            return String.format("Wow you scored %d points, good job!!!%nOn to the next level.", score);
        }
        else
        {
            return String.format("You scored %d points, nice!%nOn to the next level.", score);
        }
    }
    
    private void createTutorial1()
    {
        // Tutorial 1, move forward
        addTruckAndDestination(8, 9, 8, 4, -90);
        
        G3Tutorial g3tuto = new G3Tutorial("G3forward.png", "Use the move forward command to move the truck \nto it's destination");
        addObject(g3tuto, 300, getHeight() -60);
    }
    
    private void createTutorial2()
    {
        // Tutorial 2, rotate
        addTruckAndDestination(9, 6, 7, 6, -90);
        
        G3Tutorial g3tuto = new G3Tutorial("G3left.png", "Use the rotate left command to move the truck \nto it's destination");
        addObject(g3tuto, 300, getHeight() -60);
    }
    
    private void createTutorial3()
    {
        // Tutorial 2, pause
        addTruckAndDestination(8, 9, 8, 4, -90);
        addTruckAndDestination(10, 7, 5, 7, 180);
        
        G3Tutorial g3tuto = new G3Tutorial("G3pause.png", "Use the pause command to wait for the \nother truck before moving");
        addObject(g3tuto, 300, getHeight() -60);
    }
    
    private void addTruckAndDestination(int truckX, int truckY, int destX, int destY, int orientation)
    {
        String color = colors[Greenfoot.getRandomNumber(colors.length)];
        G3Truck truck = new G3Truck(color, 1);
        G3Destination dest = new G3Destination(color);
        
        dest.setRotation(orientation);
        truck.setRotation(orientation);
        addObject(truck, getRealX(truckX), getRealY(truckY));
        addObject(dest, getRealX(destX), getRealY(destY));
    }
    
    /**
     * Create the number of trucks in a recursive loop
     */
    private void createTrucks(int count)
    {
        // Don't create when counter reaches 0
        if (count > 0)
        {
            // Now, create a truck with a random color and a destination
            String color = colors[Greenfoot.getRandomNumber(colors.length)];
            G3Truck truck = new G3Truck(color, count);
            G3Destination dest = new G3Destination(color);
            
            // And find a location for the truck and for the destination
            // Play field is h x w = 14 x 15. destinations stay left, trucks on the right
            int orientation = Greenfoot.getRandomNumber(4) * 90;
            Point coord = getValidLocation(orientation, 0, GRIDWIDTH / 2);
            
            dest.setRotation(orientation);
            addObject(dest, getRealX(coord.x), getRealY(coord.y));
            // Do repaint so the containter is painted right away. This is needed
            // for the check of the valid location for the next object.
            repaint();
            
            // And for the truck
            orientation = 90 + Greenfoot.getRandomNumber(3) * 90; // Not facing water
            coord = getValidLocation(orientation, (GRIDWIDTH / 2) + 1, GRIDWIDTH);
            
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
            // Create static containers on the dock. 
            // Not too close to the borders of the playfield.
            int orientation = Greenfoot.getRandomNumber(2) * 90;
            Point coord = getValidLocation(orientation, 3, 12, 3, 11);
            
            G3Obstacle obj = new G3Obstacle();
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
        return getValidLocation(orientation, minX, maxX, 0, GRIDHEIGHT);
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
        
        return result;
    }
    
    private boolean testLocation(int orientation, Point coord)
    {
        //  Check if an object can be found on the destination coordinates
        List<G3FieldObject> l = getObjectsAt(getRealX(coord.x), getRealY(coord.y), G3FieldObject.class);
        
        if (orientation % 180 == 0 && coord.x > 0 && coord.x < GRIDWIDTH-1)
        {
            // Test horizontal cells
            l.addAll(getObjectsAt(getRealX(coord.x - 1), getRealY(coord.y), G3FieldObject.class));
            l.addAll(getObjectsAt(getRealX(coord.x + 1), getRealY(coord.y), G3FieldObject.class));
        }
        else if (orientation % 180 != 0 && coord.y > 0 && coord.y < GRIDHEIGHT-1)
        {
            // Test vertical cells
            l.addAll(getObjectsAt(getRealX(coord.x), getRealY(coord.y - 1), G3FieldObject.class));
            l.addAll(getObjectsAt(getRealX(coord.x), getRealY(coord.y + 1), G3FieldObject.class));
        }
        else 
        {
            // Coordinates not valid by orientation boudaries
            return false;
        }
        
        return l.isEmpty();
    }
    
    public int getRealX(int x)
    {
        return (x * GRIDSIZE) + (GRIDSIZE / 2);
    }
    
    public int getRealY(int y)
    {
        return ((y+1) * GRIDSIZE) + (GRIDSIZE / 2);
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
            List<G3Truck> trucks = getObjects(G3Truck.class);

            if (programming != null)
            {
                // ID's start at 1
                nextID = programming.getID() + 1;
                if (nextID > trucks.size())
                {
                    nextID = 1;
                }
            }
            
            for(G3Truck t : trucks)
            {
                if (t.getID() == nextID)
                {
                    selectTruckForProgramming(t);
                    break;
                }            
            }
        }
    }
    
    public void selectTruckForProgramming(G3Truck truck)
    {
        if (gameStage == GameStage.PROGRAMMING)
        {
            // Unset last active truck
            if (programming != null)
            {
                programming.setActive(false);
            }
            // Remove all visual program steps
            removeObjects(getObjects(G3ProgramStep.class));
            
            programming = truck;
            programming.setActive(true);
            showText("Program truck: " + programming.getID(), 700, 60);
            
            // Display program steps
            int i = 0;
            for(String cmd : programming.getProgram())
            {
                addObject(new G3ProgramStep(cmd, i), 700, 300);
                i++;
            }
            arrangeProgramSteps();
        }
    }
    
    public void startStage(GameStage stage, String message)
    {
        // In program mode the active truck blinks, deactivate all trucks
        if (gameStage == GameStage.PROGRAMMING)
        {
            if (programming != null)
            {   
                programming.setActive(false);
            }
            removeObjects(getObjects(G3ProgramStep.class));
        }
        
        // Set the gamestage to waiting and show the messagebox
        gameStage = GameStage.WAITING;
        
        // And show game info overlay
        G3Waitbox g3wait = new G3Waitbox(stage, message);
        addObject(g3wait, getWidth() / 2, getHeight() / 2); // Centered on screen
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
            removeObjects(getObjects(G3Tutorial.class));
            removeObjects(getObjects(G3BtnAllDone.class));
            removeObjects(getObjects(G3Command.class));
            
            gameStage = stage;
        }
    }
    
    /**
     * Adds the command to the truck program and draws it on screen
     */
    public void appendProgramStep(String command)
    {          
        if (programming != null)
        {
            addObject(new G3ProgramStep(command, programming.addCommand(command)), 700, 300);
            arrangeProgramSteps();
        }
    }
    
    
    public void removeProgramStep(G3ProgramStep programStep)
    {
        if (programming != null)
        {
            programming.removeCommandAt(programStep.getStep());
            removeObject(programStep);
            arrangeProgramSteps();
        }
    }
    
    /**
     * 
     */
    private void arrangeProgramSteps()
    {
        // Arrange the steps
        // We separate 3 parts, last 5 steps are shown full, then 5 steps are
        // shown compact. More steps are shown overlaying eachother.
        List<G3ProgramStep> pSteps = getObjects(G3ProgramStep.class);
        int cnt = pSteps.size();
        int offset = Math.min(5, cnt);

        for(G3ProgramStep s : pSteps)
        {
            if (s.getStep() < cnt - 10)
            {
                s.setYLocation(130);
            }
            else if (s.getStep() < cnt - 4)
            {
                s.setYLocation(180 - ((cnt - 5 - s.getStep()) * 10));
            }
            else
            {
                 s.setYLocation(330 + ((offset - (2* (cnt - s.getStep()))) * 30));
            }
        }
    }
    
    /**
     * Starts or decreases a loopcounter until number of loops is passed
     */
    private boolean isDelayed(int value)
    {
        if (delay == 0)
        {
            delay = value;
            return false;
        }
        else
        {
            delay--;
            return delay == 0;
        }
    }
    
    public boolean checkOutOfBounds(Actor a)
    {
        double oX = a.getImage().getWidth() / 2.0 -2;
        double oY = a.getImage().getHeight() / 2.0 -2;
        double r = Math.toRadians(a.getRotation());
        double over = GRIDSIZE / 12.0;
        double x1 = a.getX() - Math.abs(oX * Math.cos(r)) - Math.abs(oY * Math.sin(r));
        double x2 = a.getX() + Math.abs(oX * Math.cos(r)) + Math.abs(oY * Math.sin(r));
        double y1 = a.getY() - Math.abs(oY * Math.cos(r)) - Math.abs(oX * Math.sin(r));
        double y2 = a.getY() + Math.abs(oY * Math.cos(r)) + Math.abs(oX * Math.sin(r));
        return x1 < -over || 
               x2 > GRIDWIDTH * GRIDSIZE + over ||
               y1 < GRIDSIZE - over || 
               y2 > (GRIDHEIGHT+1) * GRIDSIZE + over;
    }        
}
