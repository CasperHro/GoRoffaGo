import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.*;

/**
 * Game1Harbour is the main game world. When started boats will be created in the waters.
 * The main goal is to dock the different boats in the right harbour so they can unload 
 * the cargo. When a ship is docked a point is scored. When a ship crashes it will
 * dissapear. Multiple crashes and the game is over!
 * 
 * @author C. Karreman
 * @version 1.0
 */
public class Game1Harbour extends World
{
    /**
     * Private variables, use only in this world.
     */
    private int newBoatInterval = 120; // Steps to wait before a new boat is made
    private int newBoatSteps = 0; // Stepcounter for creating new boats
    private int boatCounter = 0; // Counts how many boats are created, every 5 boats the interval is shortened
    private int score = 0; // Game score
    private int crashes = 0; // Crash counter
    private int maxCrashes = 5; // Maximum crashes before game over
    private boolean running = false; // Flag to indicate a running game (may be paused by Greenfoot class)
    private GreenfootImage waters; // Image to load available sailing area
    
    /**
     * Constructor for objects of class Game1Harbour.
     * The constructor is called only ones when the class is initialized.
     */
    public Game1Harbour()
    {    
        // Create a new world with 800x600 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);
        // Load the sailing area
        waters = new GreenfootImage("Game1BgBit.png");

        // Prepare the game
        prepare();
        
        // TODO: Show the game info until clicked, when info is hidden startGame() must be called.
        startGame();
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        // This is a setting for the speed of the game. Tested to be playable.
        Greenfoot.setSpeed(40);

        G1_Dock g1_dock = new G1_Dock();
        addObject(g1_dock,143,404);
        g1_dock.setRotation(-17);
        g1_dock.setLocation(134,404);
        g1_dock.setCargo("oil");

        G1_Dock g1_dock2 = new G1_Dock();
        addObject(g1_dock2,120,550);
        g1_dock2.setRotation(-18);
        g1_dock2.setLocation(116,553);
        g1_dock2.setCargo("oil");

        G1_Dock g1_dock3 = new G1_Dock();
        addObject(g1_dock3,296,359);
        g1_dock3.setRotation(64);
        g1_dock3.setLocation(317,459);
        g1_dock3.setCargo("oil");
        
        G1_Dock g1_dock4 = new G1_Dock();
        addObject(g1_dock4,417,98);
        g1_dock4.setRotation(20);
        g1_dock4.setLocation(411,94);
        
        G1_Dock g1_dock5 = new G1_Dock();
        addObject(g1_dock5,641,89);
        g1_dock5.setRotation(20);
        g1_dock5.setLocation(638,79);
        
        G1_Dock g1_dock6 = new G1_Dock();
        addObject(g1_dock6,571,225);
        g1_dock6.setRotation(20);
        g1_dock6.setLocation(555,205);
        
        G1_Dock g1_dock7 = new G1_Dock();
        addObject(g1_dock7,471,554);
        g1_dock7.setRotation(-13);
        g1_dock7.setLocation(466,561);
        g1_dock7.setCargo("solids");
        
        G1_Dock g1_dock8 = new G1_Dock();
        addObject(g1_dock8,633,558);
        g1_dock8.setRotation(24);
        g1_dock8.setLocation(626,561);
        g1_dock8.setCargo("solids");
    }

    /**
     * StartGame is called to start a new game. Score and other counters are reset
     * to begin with the game defaults.
     */
    public void startGame()
    {
        // First init all game vars
        newBoatInterval = 120;
        boatCounter = 0;
        crashes = 0;
        score = 0;
        running = true;
        
        // To show the current score call AddScore()
        addScore(0);

        // Now create a boat soon (5 steps delay)
        newBoatSteps = newBoatInterval - 5;
    }

    /**
     * The end of the game. 
     */
    public void gameOver()
    {
        // Reset running flag and stop game loop
        running = false;
        Greenfoot.stop();
        
        // TODO: Show game over spash screen, options there are return to 
        //       main menu or restart game
    }
    
    /**
     * Act is the game step. In this world boats are created with a time interval. A boat
     * mut be selected to take over the controls. Use "S" to select a boat or click on one.
     */
    public void act() 
    {
        // Check running, when not running nothing is done. Game info is till visible.
        if (running)
        {
            // Increase the wait stepcounter and check if we have to create a new boat.
            newBoatSteps++;
            if (newBoatSteps > newBoatInterval)
            {
                createBoat();
            }
                        
            // The s-key selects the next boat for controls.
            if (Greenfoot.isKeyDown("s"))
            {
                selectNextBoat();
            }
            
            // First check if all boats are still in the water area.
            checkBoats();

            // Check crashcounter and game over maxCrashes is reached
            if (crashes >= maxCrashes)
            {
                gameOver();
            }
        }
    }

    /**
     * createBoat initializes a new boat class and adds is to the world.
     */
    private void createBoat()
    {
        // Create random boat type depending on the different docks
        List<G1_Dock> docks = getObjects(G1_Dock.class);
        int rnd = Greenfoot.getRandomNumber(docks.size() - 1);
        
        G1_Boat boat = new G1_Boat();
        // Now add the boat to the world, increase the boatcounter and reset the stepcounter
        addObject(boat, 5, 270 + Greenfoot.getRandomNumber(35));
        boat.setCargo(docks.get(rnd).getCargo());
        boatCounter++;
        newBoatSteps = 0;
        
        // If only one boat is in this world this one gets controled by default
        if (getObjects(G1_Boat.class).size() == 1)
        {
            boat.takeControls();
        }
        
        // After 5 boats decrease the interval of boat creating
        if (boatCounter % 5 == 0)
        {
            newBoatInterval -= 3;
        }
    }

    /**
     * checkBoats checks the locations of all the boats on the map. The boats must
     * be in the watered area on the waters image. When a boats is on land it crashes.
     */
    private void checkBoats()
    {
        // Check each boat for crashing into the land
        for(G1_Boat boat : getObjects(G1_Boat.class))
        {
            // Only still sailing boats are checked, already crashed boats are skipped.
            if (!boat.getCrashed())
            {
                // When a boat is at the end of the map it just disappears. No crash, just
                // a missed point.
                if (boat.isAtEdge())
                {
                    boat.releaseControls();
                    removeObject(boat);
                }
                else
                {
                    // The color on the map determines water or land.
                    Color c = waters.getColorAt(boat.getX(), boat.getY());
                    
                    // Blue is water so no blue is land
                    if (c.getBlue() == 0)
                    {
                        // Increase the crash counter and make the boat sink
                        crashes++;
                        boat.crash();
                    }
                }
            }
        }
    }
    
    /**
     * When the S-key is pressed the current boat is left uncontrolled, the next boat in 
     * line is selected to handle by the player.
     */
    public void selectNextBoat()
    {
        // First find current controlled boat
        int current = -1;
        List<G1_Boat> boats = getObjects(G1_Boat.class);
        for(G1_Boat b : boats)
        {
            if (b.getControlled())
            {
                current = boats.indexOf(b);
                break;
            }
        }
        
        // Then find next in line
        int nextBoat = current +1;
        // When the last boat is selected select the first one next.
        if (nextBoat == boats.size())
        {
            nextBoat = 0;
        }
        while(nextBoat < boats.size() && boats.get(nextBoat).getCrashed())
        {
            nextBoat++;
        }
        if (nextBoat < boats.size())
        {
            boats.get(nextBoat).takeControls();
        }
    }
    
    public void addScore(int increase)
    {
        score += increase;
        showText("Score: "+score, 730, 20);
    }
    
    public void addCrash(int increase)
    {
        crashes += increase;
    }

}
