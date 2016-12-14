import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class hook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hook extends Actor
{
    Cargo cargo = null;
    int indexCargo;
    EmptyCargo emptySpot = null;
    Actor transport = null;
    int movingSpeed = 4;

    private static final int runnerOffset = 140;
    Cables cable = new Cables();
    HookRunner runner = new HookRunner();
    
    /**
     * When added to a world also add the back of the ship
     */
    protected void addedToWorld(World world)
    {
        // Runner only moves on a horizontal line
        world.addObject(runner, getX(), runnerOffset);
        world.addObject(cable, getX(), runnerOffset + ((getY() - runnerOffset) / 2));
        cable.setLength(getY() - runnerOffset);
    }
    
    /**
     * Set the same location for tha back of the ship
     */
    public void setLocation(int x, int y)
    {
        if (getX() < getWorld().getWidth() / 2)
        {
            x = Math.min((getWorld().getWidth() / 2) - 40, Math.max(35, x));
        }
        else
        {
            x = Math.max((getWorld().getWidth() / 2) + 40, Math.min(getWorld().getWidth() - 35, x));
        }
        y = Math.min(450, Math.max(runnerOffset + 40, y));
        // Also check other objects on the location
        int offset = 10;
        if (cargo != null)
        {
            offset += cargo.getImage().getHeight();
        }
        
        List<Cargo> l = getWorld().getObjectsAt(x - (getImage().getWidth() / 2), y + offset, Cargo.class);
        List<Cargo> r = getWorld().getObjectsAt(x + (getImage().getWidth() / 2), y + offset, Cargo.class);
        l.remove(cargo);
        r.remove(cargo);
        // Up is always good, downwards we check what's beneath
        if (getY() > y || (l.isEmpty() && r.isEmpty() &&
             getWorld().getObjectsAt(x, y + offset, Transport.class).isEmpty()))
        {
            super.setLocation(x, y);
            runner.setLocation(getX(), runnerOffset);
            cable.setLocation(getX(), runnerOffset + ((getY() - runnerOffset) / 2));
            cable.setLength(getY() - runnerOffset);
            if (cargo != null) {
                cargo.setLocation(getX(), getY() + 10 + (cargo.getImage().getHeight() / 2));
            }
        }
    }
    
   
    /**
     * Act - do whatever the hook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        hookMovement();
        cargoInTransport();
        EmptyCargoCheck();
    }
    
    public void hookMovement() {
        // Check for Cargo actor intersection.
        if (cargo == null) {
            
            if (Greenfoot.isKeyDown("s")) {
                cargo = (Cargo)getOneObjectAtOffset(0, 20, Cargo.class);
            }
        
            if(cargo!=null){
                getWorldOfType(myWorld.class).liftCargo(cargo);
                cargo.setRotation(0);
            }
        }

        if (Greenfoot.isKeyDown("down")) {
            setLocation(getX(), getY()+3);
        }
        
        if (Greenfoot.isKeyDown("up")) {
            setLocation(getX(), getY()-3);
        }
        
        if (Greenfoot.isKeyDown("right")) {
            setLocation(getX()+3, getY());
        }
        
        if (Greenfoot.isKeyDown("left")) {
            setLocation(getX()-3, getY());
        }
    }
    
    public void cargoInTransport() {
        // Check for Transport actor intersection.
        if (transport == null) {
            transport = getOneObjectAtOffset(0, 0, Transport.class);
        }
        
        // Reset Cargo pickup when Cargo and transport intersect.
        if (transport != null) {
            myWorld world = getWorldOfType(myWorld.class);
            if (world.getObjects(Transport.class).get(0).resetTransport == 1) {
                world.removeObject(cargo);
                cargo = null;
            }
        }
    
    }
     public void EmptyCargoCheck(){
        if (cargo != null && Greenfoot.isKeyDown("d") &&
            getWorldOfType(myWorld.class).putCargoAtSpot(cargo, (EmptyCargo)getOneObjectAtOffset(0, cargo.getImage().getHeight(), EmptyCargo.class)))
        {
            cargo = null;
        }    
    }
}
