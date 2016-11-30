import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G1_Explosion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G1_Explosion extends Actor
{
    private int steps = 0;
    private GreenfootImage[] images = new GreenfootImage[16];
    
    /**
     * Constructor for the explosion
     */
    public G1_Explosion()
    {
        GreenfootImage img = getImage();
        for(int y = 0; y < 4; y++)
        {
            for(int x = 0; x < 4; x++)
            {
                //images[(y*4)+x].setWidth(img.getWidth() / 4);
                
            }
        }
        
    }
    
    /**
     * Act - do whatever the G1_Explosion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
}
