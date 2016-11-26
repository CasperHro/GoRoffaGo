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
     * Act - do whatever the G1_Dock wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
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
