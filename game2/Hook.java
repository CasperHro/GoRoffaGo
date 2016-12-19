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
    protected Cargo cargo = null;
    protected Transport transport = null;
    protected int indexCargo;
    protected EmptyCargo emptySpot = null;
    protected int movingSpeed = 4;
    protected boolean running = false;

    private static final int runnerOffset = 140;
    private Cables cable = new Cables();
    private HookRunner runner = new HookRunner();
    
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
        
        List<Transport> t = getWorld().getObjectsAt(x, y + offset, Transport.class);
        List<Cargo> l = getWorld().getObjectsAt(x - (getImage().getWidth() / 2), y + offset, Cargo.class);
        List<Cargo> r = getWorld().getObjectsAt(x + (getImage().getWidth() / 2), y + offset, Cargo.class);
        l.remove(cargo);
        r.remove(cargo);
        // Up is always good, downwards we check what's beneath
        if (getY() > y || (l.isEmpty() && r.isEmpty() && (t.isEmpty() || t.get(0).getY() -43 > y)))
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
    
    public void setRunning(boolean value)
    {
        running = value;
    }
    
    /**
     * Act - do whatever the hook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (running)
        {
            hookMovement();
            EmptyCargoCheck();
        } 
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
    
    public void EmptyCargoCheck(){
        myWorld world = getWorldOfType(myWorld.class);
        if (cargo != null && Greenfoot.isKeyDown("d") &&
            getWorldOfType(myWorld.class).putCargoAtSpot(cargo, (EmptyCargo)getOneObjectAtOffset(0, cargo.getImage().getHeight(), EmptyCargo.class))) {
                cargo = null;
        }
        
        if (transport == null) {
            transport = (Transport)getOneObjectAtOffset(0, 40, Transport.class);
        }
        
        if (cargo != null && Greenfoot.isKeyDown("d")) {
            if (transport != null && transport.setCargo(cargo)) {
                cargo = null;
            }
        }
    }
}
