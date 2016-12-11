import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G3_Command here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3_Command extends Actor
{
    protected String command = "";
    
    /**
     * Act - do whatever the G3_Command wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this))
        {
            getWorldOfType(Game3World.class).appendProgramStep(command);
        }
    }    
}
