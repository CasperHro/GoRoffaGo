import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.*;

/**
 * Game1Harbour is the game1 world. When started boats will be created in the waters.
 * The main goal is to dock the different boats in the right harbour so they can unload
 * the cargo. When a ship is docked a point is scored. When a ship crashes it will
 * dissapear. Multiple crashes and the game is over!
 *
 * @author C. Karreman
 * @version 1.0
 */
public class G1Harbour extends World
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
    private boolean firstStep = true; // Does the first step routine
    private boolean running = false; // Flag to indicate a running game (may be paused by Greenfoot class)
    private GreenfootImage waters; // Image to load available sailing area
    private String lastKey = ""; // To prevent multiple strokes

    private GreenfootSound backgroundMusic = new GreenfootSound("theme.mp3");
    
    
    /**
     * Constructor for objects of class Game1Harbour.
     * The constructor is called only ones when the class is initialized.
     */
    public G1Harbour()
    {
        // Create a new world with 800x600 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);
        // Load the sailing area
        waters = new GreenfootImage("Game1BgBit.png");
        
        // This is a setting for the speed of the game. Tested to be playable.
        Greenfoot.setSpeed(40);

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
        G1Dock g1dock = new G1Dock();
        addObject(g1dock,143,404);
        g1dock.setRotation(-17);
        g1dock.setCargo("oil");

        G1Dock g1dock2 = new G1Dock();
        addObject(g1dock2,116,553);
        g1dock2.setRotation(-18);
        g1dock2.setCargo("oil");

        G1Dock g1dock3 = new G1Dock();
        addObject(g1dock3,317,459);
        g1dock3.setRotation(64);
        g1dock3.setCargo("oil");

        G1Dock g1dock4 = new G1Dock();
        addObject(g1dock4,411,94);
        g1dock4.setRotation(20);

        G1Dock g1dock5 = new G1Dock();
        addObject(g1dock5,638,79);
        g1dock5.setRotation(20);

        G1Dock g1dock6 = new G1Dock();
        addObject(g1dock6,555,205);
        g1dock6.setRotation(20);

        G1Dock g1dock7 = new G1Dock();
        addObject(g1dock7,466,561);
        g1dock7.setRotation(-13);
        g1dock7.setCargo("solids");

        G1Dock g1dock8 = new G1Dock();
        addObject(g1dock8,626,561);
        g1dock8.setRotation(24);
        g1dock8.setCargo("solids");
    }

    /**
     * Does the run initialization. To prevent 
     */
    private void firstStep()
    {
        // Reset the flag to prevent another call to this function
        firstStep = false;
        
        // Start music
        backgroundMusic.playLoop();
        
        // And show game info overlay
        G1GameInfo g1info = new G1GameInfo();
        addObject(g1info, getWidth() / 2, getHeight() / 2); // Centered on screen
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
        
        //To show the current crashes call AddCrash()
        addCrash(0);

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
            // Increase the wait stepcounter and check if we have to create a new boat.
            newBoatSteps++;
            if (newBoatSteps > newBoatInterval)
            {
                createBoat();
            }

            checkKeys();

            // First check if all boats are still in the water area.
            checkBoats();

            // Check crashcounter and game over maxCrashes is reached
            if (crashes >= maxCrashes)
            {
                gameOver();
            }
        }
    }

    private void checkKeys()
    {
        // While switching the controls to an other boat the isKeyDown() gets true
        // multiple times. Every act run registers the keydown but we only want to
        // react ones. The getKey() should solve this issue but because of the boats
        // that use isKeyDown() the getKey() stays empty. So wee have to use
        // isKeyDown().
        // To prevent the S-key from multiple hits when holding down remember and
        // check the last proceded keystroke.
        if (lastKey == "" || !Greenfoot.isKeyDown(lastKey))
        {
            // The s-key selects the next boat for controls.
            if (Greenfoot.isKeyDown("s"))
            {
                lastKey = "s";
                selectNextBoat();
            }
            else if (Greenfoot.isKeyDown("m"))
            {
                lastKey = "m";
                backgroundMusic.stop();
            }
            else if (Greenfoot.isKeyDown("p"))
            {
                lastKey = "p";
                backgroundMusic.playLoop();
            }
            else
            {
                lastKey = "";
            }
        }
    }
        
    /**
     * createBoat initializes a new boat class and adds is to the world.
     */
    private void createBoat()
    {
        // Create random boat type depending on the different docks
        List<G1Dock> docks = getObjects(G1Dock.class);
        int rndY = Greenfoot.getRandomNumber(40);
        int rndDock = Greenfoot.getRandomNumber(docks.size());

        G1Boat boat = new G1Boat();
        // Now add the boat to the world, increase the boatcounter and reset the stepcounter
        addObject(boat, 5, 270 + rndY);
        // Set the angle based on the y location, otherwise it crashes into shore to fast
        boat.setRotation(rndY / -2);
        // Random cargo is dependant on the docks
        boat.setCargo(docks.get(rndDock).getCargo());
        boatCounter++;
        newBoatSteps = 0;

        // If only one boat is in this world this one gets controled by default
        if (getObjects(G1Boat.class).size() == 1)
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
        for(G1Boat boat : getObjects(G1Boat.class))
        {
            // Only still sailing boats are checked, already crashed boats are skipped.
            if (boat.getCrashed())
            {
                continue;
            }
            
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
                    addCrash(1);
                    boat.crash();
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
        List<G1Boat> boats = getObjects(G1Boat.class);
        for(G1Boat b : boats)
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
        while(nextBoat < boats.size() && !boats.get(nextBoat).takeControls())
        {
            nextBoat++;
        }
    }

    public void addScore(int increase)
    {
        score += increase;
        showText("Score: "+score, 60, 20);
    }

    public void addCrash(int increase)
    {
        crashes += increase;
        showText("Crashes: "+crashes, 70, 50);
    }

}
