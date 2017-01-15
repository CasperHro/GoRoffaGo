import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G4Explosion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4Explosion extends Actor
{
    /**
     * Act - do whatever the G1_Explotion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int timer = 15;
    
    public void act() 
    {
        // Add your action code here.
        if (timer > 0)
        {
            setImage("exp" + timer + ".png");
            timer--;
        }
        if (timer == 0)
        {
            getWorld().removeObject(this);
        }
    }
}
