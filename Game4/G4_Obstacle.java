import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class G4_Obstacle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4_Obstacle extends Actor
{
    private String role = "criminal"; 
    /**
     * Act - do whatever the G4_Obstacle wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    /**
     * Returns the role type
     */
    public String getRole()
    {
        return role;
    }
    
    /**
     * Sets the role type
     */
    public void setRole(String value)
    {
        role = value;
    }
}
