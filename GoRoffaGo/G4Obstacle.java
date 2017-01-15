import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class G4_Obstacle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4Obstacle extends Actor
{
    private String value = "obs1";
    private G4Explosion explosion;
    
    /**
     * Act - do whatever the G4_Obstacle wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
    
    }    
    public void setValue(String value) 
    {
        value = value;
        if (value.equals("obs1"))
        {
            setImage(new GreenfootImage("G4obs1.png"));
        }
        else if (value.equals("obs2"))
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
        explosion = new G4Explosion();
        getWorld().addObject(explosion, getX(), getY());
        explosion.setRotation(getRotation());
        getWorld().removeObject(this);
        return;

    }
}
