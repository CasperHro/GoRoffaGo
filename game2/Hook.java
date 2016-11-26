import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class hook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hook extends Actor
{
    Actor Cargo = null;
    int movingSpeed = 3;

    /**
     * Act - do whatever the hook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        hookMovement();
    }
    
    public void hookMovement() {
        if (Cargo == null) {
            Cargo = getOneObjectAtOffset(10, 0, Cargo.class);        
        }

        if (Greenfoot.isKeyDown("down")) {
            setLocation(getX(), getY()+3);
        
            if (Cargo != null) {
                Cargo.setLocation(getX(), getY()+3);
            }
        }
        
        if (Greenfoot.isKeyDown("up")) {
            setLocation(getX(), getY()-3);
            
            if (Cargo != null) {
                Cargo.setLocation(getX(), getY()-3);
            }
        }
        
        if (Greenfoot.isKeyDown("right")) {
            setLocation(getX()+3, getY());
            
            if (Cargo != null) {
                Cargo.setLocation(getX()+3, getY());
            }
        }
        
        if (Greenfoot.isKeyDown("left")) {
            setLocation(getX()-3, getY());
            
            if (Cargo != null) {
                Cargo.setLocation(getX()-3, getY());
            }
        }
    }
    
    public void atWorldEdge() {
        //if ((direction == 1 && getX() == getWorld().getWidth()-1) || (direction == -1 && getX() == 0)) {
        //    direction = -direction;
        //}
    
    }
    
}
