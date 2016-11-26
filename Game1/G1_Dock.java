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
    
    /**
     * Act - The dock checks for a ship nearby and when the speed is low enough
     * it attached the ship to the dock.
     */
    public void act() 
    {
        // Add your action code here.
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
