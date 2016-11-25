import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.List;

/**
 * Write a description of class Game1Harbour here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game1Harbour extends World
{
    private int lastBoatCounter = 0;
    private int newBoatInterval = 120; // Steps
    private int score = 0;
    private int crashes = 0; // Crash counter
    private boolean running = false;
    private GreenfootImage waters;

    /**
     * Constructor for objects of class Game1Harbour.
     * 
     */
    public Game1Harbour()
    {    
        // Create a new world with 800x600 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);
        waters = new GreenfootImage("Game1BgBit.png");

        prepare();
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        Greenfoot.setSpeed(40);
        
        // TODO: Show the game info until clicked
                
        StartGame();
       
    }

    public void StartGame()
    {
        // First init all game vars
        newBoatInterval = 120;
        crashes = 0;
        score = 0;
        running = true;
        
        // To show the current score
        AddScore(0);

        // Now create a boat soon
        lastBoatCounter = newBoatInterval - 5;
    }

    public void act() 
    {
        if (running)
        {
            // Check if we have to crate a boat or increase the wait counter
            if (lastBoatCounter > newBoatInterval)
            {
                CreateBoat();
            }
            else
            {
                lastBoatCounter++;
            }
            
            // The s-key selects the next boat for controls
            if (Greenfoot.isKeyDown("s"))
            {
                SelectNextBoat();
            }
            
            // First check any collisions
            CheckBoats();

            // Check crashcounter and game over when > 4
            if (crashes > 4)
            {
                GameOver();
            }
        }
    }

    private void CreateBoat()
    {
        // TODO: Create random boat type
        G1_Boat boat = new G1_Boat();
        addObject(boat, 5, 270 + Greenfoot.getRandomNumber(70));
        lastBoatCounter = 0;
        if (getObjects(G1_Boat.class).size() == 1)
        {
            boat.TakeControls();
        }
        
        // After 5 boats decrease the interval of boat creating
        if (getObjects(G1_Boat.class).size() % 5 == 0)
        {
            newBoatInterval -= 5;
        }
    }

    private void CheckBoats()
    {
        // Check each boat for crashing into the land
        for(G1_Boat boat : getObjects(G1_Boat.class))
        {
            if (!boat.getCrashed())
            {
                Color c = waters.getColorAt(boat.getX(), boat.getY());
                
                if (c.getRed() != 0)
                {
                    crashes++;
                    boat.Crash();
                }
            }
        }
    }
    
    public void SelectNextBoat()
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
            boats.get(nextBoat).TakeControls();
        }
    }
    
    private void GameOver()
    {

        Greenfoot.stop();

    }
    
    public void AddScore(int increase)
    {
        score += increase;
        showText("Score: "+score, 730, 20);
    }
    
    public void AddCrash(int increase)
    {
        crashes += increase;
    }

}
