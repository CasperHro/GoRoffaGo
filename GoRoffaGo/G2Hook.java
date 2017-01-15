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
    protected static final int RUNNEROFFSET = 140;
    
    protected G2Cargo cargo = null;
    protected G2Transport transport = null;
    protected int movingSpeed = 4;
    protected boolean running = false;

    private G2Cables cable = new G2Cables();
    private G2HookRunner runner = new G2HookRunner();
    
    /**
     * When added to a world also add the back of the ship
     */
    protected void addedToWorld(World world)
    {
        // Runner only moves on a horizontal line
        world.addObject(runner, getX(), RUNNEROFFSET);
        world.addObject(cable, getX(), RUNNEROFFSET + ((getY() - RUNNEROFFSET) / 2));
        cable.setLength(getY() - RUNNEROFFSET);
    }
    
    /**
     * Set the same location for tha back of the ship
     */
    public void setLocation(int x, int y)
    {
        int newX;
        int newY;
        int offset = 10;
        
        if (getX() < getWorld().getWidth() / 2)
        {
            newX = Math.min((getWorld().getWidth() / 2) - 40, Math.max(35, x));
        }
        else
        {
            newX = Math.max((getWorld().getWidth() / 2) + 40, Math.min(getWorld().getWidth() - 35, x));
        }
        newY = Math.min(450, Math.max(RUNNEROFFSET + 40, y));
        // Also check other objects on the location
        if (cargo != null)
        {
            offset += cargo.getImage().getHeight();
        }
        
        List<G2Transport> t = getWorld().getObjectsAt(newX, newY + offset, G2Transport.class);
        List<G2Cargo> l = getWorld().getObjectsAt(newX - (getImage().getWidth() / 2), newY + offset, G2Cargo.class);
        List<G2Cargo> r = getWorld().getObjectsAt(newX + (getImage().getWidth() / 2), newY + offset, G2Cargo.class);
        l.remove(cargo);
        r.remove(cargo);
        // Up is always good, downwards we check what's beneath
        if (getY() > newY || (emptyCargo(l, r) && checkTransport(t, newY)))
        {
            super.setLocation(newX, newY);
            runner.setLocation(getX(), RUNNEROFFSET);
            cable.setLocation(getX(), RUNNEROFFSET + ((getY() - RUNNEROFFSET) / 2));
            cable.setLength(getY() - RUNNEROFFSET);
            if (cargo != null) {
                cargo.setLocation(getX(), getY() + 10 + (cargo.getImage().getHeight() / 2));
            }
        }
    }
    
    private boolean emptyCargo(List<G2Cargo> l, List<G2Cargo> r)
    {
        return l.isEmpty() && r.isEmpty();
    }
    
    private boolean checkTransport(List<G2Transport> t, int newY)
    {
        return t.isEmpty() || t.get(0).getY() -43 > newY;
    }
    
    public void setRunning(boolean value)
    {
        cargo = null;
        transport = null;
        running = value;
    }

    public void reset(int x, int y)
    {
        cargo = null;
        transport = null;
        setLocation(x, y);
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
            world.putCargoAtSpot(cargo, (G2EmptyCargo)getOneObjectAtOffset(0, cargo.getImage().getHeight(), G2EmptyCargo.class))) {
            cargo = null;
        }
        
        if (transport == null && cargo != null) {
            transport = (G2Transport)getOneObjectAtOffset(0, 40, G2Transport.class);
        }
        
        if (transport != null && cargo != null && Greenfoot.isKeyDown("d") && transport.setCargo(cargo)) {
            cargo = null;
            transport = null;
            world.addScore(this);
        }
    }
    
    public boolean hasCargo()
    {
        return cargo != null;
    }
}
