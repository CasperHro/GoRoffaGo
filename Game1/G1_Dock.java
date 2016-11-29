import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G1_Dock here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G1_Dock extends Actor
{
    private String cargo = "container"; // The cargo of this dock
    private G1_Boat dockedBoat; // Placeholder for the docked boat
    private int waitCounter = 0;
    
    private static int DOCK_TIMEOUT = 120; // Wait loops 
    
    /**
     * Act - The dock checks for a ship nearby and when the speed is low enough
     * it attached the ship to the dock.
     */
    public void act() 
    {
        if (dockedBoat == null)
        {
            // First loop through all the boats nearby
            for(G1_Boat boat : getObjectsInRange(30, G1_Boat.class))
            {
                if (!boat.getCrashed() && boat.getCargo().equals(cargo))
                {
                    dockBoat(boat);
                    break;
                }
            }
        }
        else
        {
            waitCounter++;
            // Allmost removing boat from dock, make it flash
            if (waitCounter * 10 > DOCK_TIMEOUT * 8)
            {
                int opacity = (waitCounter % 8) * 32;
                dockedBoat.getImage().setTransparency(opacity);
            }
            // Timeout is passed, remove the boat
            if (waitCounter > DOCK_TIMEOUT)
            {
                clearDock();
            }
        }
    }
    
    /**
     * dockBoat lockes the boat to the dock and initializes the wait timer
     * to remove the boat after a while. A point is scored!!!
     */
    private void dockBoat(G1_Boat boat)
    {
        // Remember the Actor and set docked
        dockedBoat = boat;
        boat.dock();
        waitCounter = 0;
        
        // TODO: Align the boat to the dock
        
        // And count the score
        ((Game1Harbour)getWorld()).addScore(1);
    }
    
    /**
     * Removes the boat from the dock and from the world
     */
    private void clearDock()
    {
        getWorld().removeObject(dockedBoat);
        dockedBoat = null;
    }
    
    /**
     * Returns the cargo type
     */
    public String getCargo()
    {
        return cargo;
    }
    
    /**
     * Sets the cargo type
     */
    public void setCargo(String value)
    {
        cargo = value;
    }
}
