import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * The G1_Boat is the base class for the boats in Game1. All default behaviour
 * is coded in this class. Other properties of different boats can be set in 
 * descending classes.
 * 
 * @author C. Karreman 
 * @version 1.0
 */
public class G1_Boat extends Actor
{
    /**
     * Vars for the boat
     */
    protected int maxSpeed = 5; // Maximum speed
    private int speed = 3; // Current speed
    private boolean controlled = false; // Is True when the boat must react on user input
    private boolean crashed = false; // Flag if boat is crashed, animation can still be running
    private int crashLoop = 0; // Crash animation stepcounter
    private String cargo = "container"; // The cargo of this ship
    private G1_Captain captain;
    
    /**
     * Return the cargo to the rest of the world, read only.
     */
    public String getCargo()
    {
        return cargo;
    }
    
    /**
     * Sets the cargo and loads the image for that type
     */
    public void setCargo(String value)
    {
        cargo = value;
        if (cargo.equals("oil"))
        {
            setImage(new GreenfootImage("BoatOil.png"));
        }
        else if (cargo.equals("solids"))
        {
            setImage(new GreenfootImage("BoatSolids.png"));
        }
        else
        {
            setImage(new GreenfootImage("BoatContainer.png"));
        }
    }
    
    /**
     * Act - do whatever the G1_Boat wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // When crashed no handling is done, instead the sinking is started
        if (!crashed)
        {
            // When controlled check the cursor keys to speed up or slow down 
            // or change the direction.
            if (controlled)
            {
                if (Greenfoot.isKeyDown("up"))
                {
                    speed = Math.min(maxSpeed, speed+1);
                }
                if (Greenfoot.isKeyDown("down"))
                {
                    speed = Math.max(-maxSpeed, speed-1);
                }
                if (Greenfoot.isKeyDown("left"))
                {
                    turn(-5);
                }
                if (Greenfoot.isKeyDown("right"))
                {
                    turn(5);
                }
            }
            else if (Greenfoot.mouseClicked(this))
            {
                // When the player clicks with the mouse the boat is also selected
                takeControls();
            }
            
            // And move the moat to its new location on the map.
            move(speed);
            if (captain != null)
            {
                captain.setRotation(getRotation());
                captain.setLocation(getX(), getY());
            }
            
            // Finally check if this boat is crashing into other boats
            checkCollision();

        }
        else
        {
            // Crash animation
            crashLoop++;
            if (crashLoop > 70)
            {
                releaseControls();
                getWorld().removeObject(this);
            }
        }
    }
    
    /**
     * When other boats intersect we are crashed into that boat. 2 boats sink...
     */
    private void checkCollision()
    {
        List<G1_Boat> others = getIntersectingObjects(G1_Boat.class);
        if (others.size() > 0)
        {
            // Only register 2 crashes when the other boat was not yet crashed
            int crashCount = 1;
            if (!others.get(0).getCrashed())
            {
                crashCount++;
            }
            
            // Make the other boat crash as well as this boat
            others.get(0).crash();
            crash();
            
            // Now register the crash of 2 boats in the world
            ((Game1Harbour)getWorld()).addCrash(crashCount);
        }
    }
    
    /**
     * Returns the controlled flag. This way other objects can only read the value
     * of the variable. This is needed in the world to check which boat handles user
     * input.
     */
    public boolean getControlled()
    {
        return controlled;
    }
    
    /**
     * TakeControls steals the user input from other boats. Only one boat can be 
     * controlled by the player so remove controls from the others bofore taking
     * over the controls.
     */
    public void takeControls()
    {
        // Remove control from other ships.
        for(G1_Boat boat : getWorld().getObjects(G1_Boat.class))
        {
            boat.releaseControls();
        }
        
        // And get the user input from now on.
        controlled = true;
        captain = new G1_Captain();
        getWorld().addObject(captain, getX(), getY());
        captain.setRotation(getRotation());
    }
    
    /**
     * Releases the user input.
     */
    public void releaseControls()
    {
        controlled = false;
        getWorld().removeObject(captain);
    }
    
    /**
     * Oh no, we crashed!!! Start the crash animation ans begin sinking the boat.
     */
    public void crash()
    {
        // TODO: Add Explosion and crash sound
        
        // Set the crashed flag, the act() function handles the sink animation 
        releaseControls();
        crashed = true;
    }
    
    /**
     * Returns the crashed value so the world does not assign user control.
     */
    public boolean getCrashed()
    {
        return crashed;
    }
}
