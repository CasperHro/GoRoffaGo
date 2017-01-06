import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * G3_Command is the base class for the commands. It implements the click event 
 * for the program card.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3Command extends Actor
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
            getWorldOfType(G3World.class).appendProgramStep(command);
        }
    }    
}
