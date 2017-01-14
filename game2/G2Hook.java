import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class G2hook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Hook extends Actor
{
    protected G2Cargo cargo = null;
    protected G2Transport transport = null;
    protected int indexCargo;
    protected G2EmptyCargo emptySpot = null;
    protected int movingSpeed = 4;
    protected boolean running = false;

    private static final int runnerOffset = 140;
    private G2Cables cable = new G2Cables();
    private G2HookRunner runner = new G2HookRunner();
    
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
        
        List<G2Transport> t = getWorld().getObjectsAt(x, y + offset, G2Transport.class);
        List<G2Cargo> l = getWorld().getObjectsAt(x - (getImage().getWidth() / 2), y + offset, G2Cargo.class);
        List<G2Cargo> r = getWorld().getObjectsAt(x + (getImage().getWidth() / 2), y + offset, G2Cargo.class);
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
        // Check for G2Cargo actor intersection.
        if (cargo == null) {
            
            if (Greenfoot.isKeyDown("s")) {
                cargo = (G2Cargo)getOneObjectAtOffset(0, 20, G2Cargo.class);
            }
        
            if(cargo!=null){
                getWorldOfType(G2Dock.class).liftCargo(cargo);
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
        G2Dock world = getWorldOfType(G2Dock.class);
        if (cargo != null && Greenfoot.isKeyDown("d") &&
            getWorldOfType(G2Dock.class).putCargoAtSpot(cargo, (G2EmptyCargo)getOneObjectAtOffset(0, cargo.getImage().getHeight(), G2EmptyCargo.class))) {
            cargo = null;
        }
        
        if (transport == null && cargo != null) {
            transport = (G2Transport)getOneObjectAtOffset(0, 40, G2Transport.class);
        }
        
        if (cargo != null && Greenfoot.isKeyDown("d")) {
            if (transport != null && transport.setCargo(cargo)) {
                // Add score.
                G2PlayerCounter c = getWorld().getObjects(G2PlayerCounter.class).get(0);
                if (c != null) {
                    c.addTransportScore(1);
                }
                
                cargo = null;
                transport = null;
            }
        }
    }
}
