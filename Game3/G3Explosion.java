import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G3Explosion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3Explosion extends Actor
{
    /**
     * Act - do whatever the G3Explosion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int explosionStep = 16;
    private int step = -1;
    private int wait = 2;
    private GreenfootImage[] images = new GreenfootImage[16];
    
    public G3Explosion()
    {
        for(int i = 1; i < 16; i++)
        {
            images[i] = new GreenfootImage("exp" + i + ".png");
        }
    }
    
    public void act() 
    {
        // Add your action code here.
        if (wait > 0)
        {
            wait--;
        }
        else
        {
            explosionStep = explosionStep + step;
            setImage(images[explosionStep]);
            wait = 3;
        }
        
        if (explosionStep < 5)
        {
            step = 2;
        }
        if (explosionStep > 8)
        {
            step = -1;
        }
    }
}
