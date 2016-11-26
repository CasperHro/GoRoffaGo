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
    Actor Transport = null;
    int movingSpeed = 3;

    /**
     * Act - do whatever the hook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        hookMovement();
        cargoInTransport();
    }
    
    public void hookMovement() {
        // Check for Cargo actor intersection.
        if (Cargo == null) {
            Cargo = getOneObjectAtOffset(0, 20, Cargo.class);        
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
    
    public void cargoInTransport() {
        // Check for Transport actor intersection.
        if (Transport == null) {
            Transport = getOneObjectAtOffset(0, 0, Transport.class);
        }
        
        // Reset Cargo pickup when Cargo and Transport intersect.
        if (Transport != null) {
            World world;
            world = getWorld();
            if (world.getObjects(Transport.class).get(0).resetTransport == 1) {
                world.removeObject(Cargo);
                Cargo = null;
            }
        }
    
    }
    
}
