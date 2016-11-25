import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class G1_Boat here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G1_Boat extends Actor
{
    private int maxSpeed = 5;
    private int speed = 3;
    private boolean controlled = false;
    private boolean crashed = false;
    private int crashLoop = 0;
    
    
    /**
     * Act - do whatever the G1_Boat wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (!crashed)
        {   
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
                TakeControls();
            }
            
            move(speed);
            
            // And check crashing into other boats
            List<G1_Boat> others = getIntersectingObjects(G1_Boat.class);
            if (others.size() > 0)
            {
                others.get(0).Crash();
                this.Crash();
                ((Game1Harbour)getWorld()).AddCrash(2);
            }
        }
        else
        {
            // Crash animation
            crashLoop++;
            if (crashLoop > 70)
            {
                getWorld().removeObject(this);
            }
        }
    }
    
    public boolean getControlled()
    {
        return controlled;
    }
    
    public void TakeControls()
    {
        // Remove control from other ships
        for(G1_Boat boat : getWorld().getObjects(G1_Boat.class))
        {
            boat.ReleaseControls();
        }
        
        controlled = true;
    }
    
    public void ReleaseControls()
    {
        controlled = false;
    }
    
    public void Crash()
    {
        // TODO: Add sound
        // The act() function handles the animation 
        crashed = true;
    }
    
    public boolean getCrashed()
    {
        return crashed;
    }
}
