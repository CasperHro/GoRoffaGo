import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class hook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class hook extends Actor
{
    int hasCargo = 0;
    int movingSpeed = 3;

    /**
     * Act - do whatever the hook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        hookMovement();
        atWorldEdge();
    }
    
    public void hookMovement() {
        if (Greenfoot.isKeyDown("down")) {
            setRotation(90);
            move(movingSpeed);
        }
        
        if (Greenfoot.isKeyDown("up")) {
            setRotation(270);
            move(movingSpeed);
        }
        
        if (Greenfoot.isKeyDown("right")) {
            setRotation(0);
            move(movingSpeed);
        }
        
        if (Greenfoot.isKeyDown("left")) {
            setRotation(180);
            move(movingSpeed);
        }
    }
    
    public void atWorldEdge() {
        //if ((direction == 1 && getX() == getWorld().getWidth()-1) || (direction == -1 && getX() == 0)) {
        //    direction = -direction;
        //}
    
    }
    
}
