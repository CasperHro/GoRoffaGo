import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G4_Obstacle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4Obstacle extends Actor
{
    /**
     * Act - do whatever the G4_Obstacle wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // No action here
    }
    
    public void setValue(String value) 
    {
        if ("obs1".equals(value))
        {
            setImage(new GreenfootImage("G4obs1.png"));
        }
        else if ("obs2".equals(value))
        {
            setImage(new GreenfootImage("G4obs2.png"));
        }
        else 
        {
            setImage(new GreenfootImage("G4obs3.png"));
        }
    } 
    public void explosion()
    {
        G4Explosion explosion = new G4Explosion();
        getWorld().addObject(explosion, getX(), getY());
        explosion.setRotation(getRotation());
        getWorld().removeObject(this);
        return;

    }
}
