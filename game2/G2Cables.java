import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Cables here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Cables extends Actor
{
    /**
     * Act - do whatever the Cables wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }
    
    public void setLength(int length)
    {
        GreenfootImage img = getImage();
        img.scale(img.getWidth(), length);
    }
}
