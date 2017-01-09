import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G4_OfficerScored here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4_OfficerScored extends Actor
{
    /**
     * Act - do whatever the G4_OfficerScored wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int timer = 4;
    
    public void act() 
    {
        // Add your action code here.
        if (timer > 0)
        {
            setImage("officer" + timer + ".png");
            timer--;
        }
        if (timer == 0)
        {
            getWorld().removeObject(this);
        }
    }   
}
