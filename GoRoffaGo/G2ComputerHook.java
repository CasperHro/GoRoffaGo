import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class G2ComputerHook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2ComputerHook extends G2Hook
{
    boolean correctCargo = true;
    boolean cargoInTransport = false;
    boolean cargoAboveTransport = false;
    int maxRange = 450;
    
    int cargoheight = 40;
    int transportX;
    int transportY;
    
    // Up these vars per level.
    int height = 180;
    int speed = 1;

    
    /**
     * Act - do whatever the ComputerHook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (running)
        {
           transport = getTransport();
            
            if (transport != null) {
                transportX = transport.getX();
                transportY = transport.getY();
                
                if (cargo == null) {
                    getCargoOnHook();
                } else {
                    moveCargo();
                    setCargoOnTransport();    
                }
            }
        }
    }
    
    public double getDistance(Actor actor) {
        return Math.hypot(actor.getX() - getX(), actor.getY() - getY());
    }
    
    public G2Transport getTransport() {
        List<G2Transport> nearTransports = getObjectsInRange(maxRange, G2Transport.class);
       
        G2Transport nearestTransport = null;
        double nearestDistance = maxRange;
        double distance;
        
        for (int i = 0; i < nearTransports.size(); i++) {
            distance = getDistance(nearTransports.get(i));

            if (distance < nearestDistance) {
                nearestTransport = nearTransports.get(i);
                nearestDistance = distance;
            }
        }
        
        return nearestTransport;
    }
    
    public G2Cargo getNearestCargoByColor(String color) {
        List<G2Cargo> nearCargos = getObjectsInRange(maxRange, G2Cargo.class);

        G2Cargo nearestCargo = null;
        double nearestDistance = maxRange;
        double distance;
        
        for (int i = 0; i < nearCargos.size(); i++) {
            if (nearCargos.get(i).getColor() == color) {
                distance = getDistance(nearCargos.get(i));
    
                if (distance < nearestDistance) {
                    nearestCargo = nearCargos.get(i);
                    nearestDistance = distance;
                }            
            }

        }
        
        return nearestCargo;
    }
    
    public G2EmptyCargo getNearestEmptyCargo() {
        List<G2EmptyCargo> nearEmptyCargos = getObjectsInRange(maxRange, G2EmptyCargo.class);

        G2EmptyCargo nearestEmptyCargo = null;
        double nearestDistance = maxRange;
        double distance;
        
        for (int i = 0; i < nearEmptyCargos.size(); i++) {
            distance = getDistance(nearEmptyCargos.get(i));

            if (distance < nearestDistance) {
                nearestEmptyCargo = nearEmptyCargos.get(i);
                nearestDistance = distance;
            }

        }
        
        return nearestEmptyCargo;
    }
    
    public void getCargoOnHook() {
        if (!transport.hasCargo()) {
            G2Cargo nearbyCargo = getNearestCargoByColor(transport.getColor());
            if(nearbyCargo != null) {
                if (getX() != nearbyCargo.getX()) {
                    setLocation(getX()-speed, height);
                }
                else if(getY() != nearbyCargo.getY()) {
                    setLocation(getX(), getY()+speed);
                }
    
                cargoCollide();
            } else {
                // ALL CARGO GONE.
                G2Dock world = getWorldOfType(G2Dock.class);
                world.endGame();
            }
        }
    }

    private void cargoCollide()
    {
        G2Cargo cargoCollide = (G2Cargo)getOneObjectAtOffset(0, 12, G2Cargo.class);
        if (cargoCollide != null) {
            G2Dock world = getWorldOfType(G2Dock.class);
            world.liftCargo(cargoCollide);
            if (transport.getColor() == cargoCollide.getColor()) {
                correctCargo = true;
                cargo = cargoCollide;     
            } else {
                correctCargo = false;
                cargo = cargoCollide;  
            }
        }  
    }
    
    public void moveCargo() {
        if (cargo != null) {
            if (!correctCargo) {
                // Drop on different location.
                G2Dock world = getWorldOfType(G2Dock.class);
                // : SET CARGO ON EMPTY SPOT: when?
                world.removeObject(cargo);
                cargo = null;
                correctCargo = true;
            } else if (!cargoAboveTransport) {
                moveToTransport();
            }        
        }
    }

    private void moveToTransport()
    {
        // Move to Transport
        if (cargo.getY() > height + cargoheight && getY() > height) {
            setLocation(getX(), getY() - speed);
        } else {
            if (cargo.getX() != transportX && getX() != transportX) {
                setLocation(getX() + speed, getY());
            } else {
                cargoAboveTransport = true;
            }
        }
    }
    
    public void setCargoOnTransport() {
        if (cargo != null && cargoAboveTransport) {
            G2Transport transportIntersect = (G2Transport)getOneObjectAtOffset(0, cargoheight, G2Transport.class); 
            if (transportIntersect != null && transportIntersect.setCargo(cargo))
            {
                cargo = null;
                correctCargo = true;
                cargoAboveTransport = false;
                G2Dock world = getWorldOfType(G2Dock.class);
                world.addScore(this);
            } else {
                setLocation(getX(), getY() + speed);
            }
        }        
    }
}
