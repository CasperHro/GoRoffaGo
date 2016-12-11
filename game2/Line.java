import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Line here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Line extends Hook
{
    /**
     * Act - do whatever the Line wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        int X = ((myWorld)getWorld()).getHook().getX();
        int Y = ((myWorld)getWorld()).getHook().getY() - 1060;
        setLocation(X, Y);
    }
}
