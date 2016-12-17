import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class ComputerHook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ComputerHook extends Hook
{
    Transport transport = null;
    Cargo cargoOnHook = null;
    boolean correctCargo = true;
    boolean cargoInTransport = false;
    boolean cargoAboveTransport = false;
    int maxRange = 450;
    
    int cargoheight = 40;
    int transportX;
    int transportY;
    int reset = 1;
    
    // Up these vars per level.
    int height = 180;
    int speed = 1;

    
    /**
     * Act - do whatever the ComputerHook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        transport = getTransport();
        
        if (transport != null) {
            transportX = transport.getX();
            transportY = transport.getY();
            
            if (transport.resetTransport == 1) {
                cargoOnHook = null;
            }
            
            if (reset == 1) {
                getCargoOnHook();
                moveCargo();
                setCargoOnTransport();    
            }
  
            reset();
        }
    }
    
    public double getDistance(Actor actor) {
        return Math.hypot(actor.getX() - getX(), actor.getY() - getY());
    }
    
    public Transport getTransport() {
        List<Transport> nearTransports = getObjectsInRange(maxRange, Transport.class);
       
        String color = "";
        Transport nearestTransport = null;
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
    
    public Cargo getNearestCargoByColor(String color) {
        List<Cargo> nearCargos = getObjectsInRange(maxRange, Cargo.class);

        Cargo nearestCargo = null;
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
    
    public EmptyCargo getNearestEmptyCargo() {
        List<EmptyCargo> nearEmptyCargos = getObjectsInRange(maxRange, EmptyCargo.class);

        EmptyCargo nearestEmptyCargo = null;
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
        Cargo nearbyCargo = getNearestCargoByColor(transport.getColor());
        if (reset == 1) {
            if(nearbyCargo != null) {
                if (getX() != nearbyCargo.getX()) {
                    setLocation(getX()-speed, height);
                }
                else if(getY() != nearbyCargo.getY()) {
                    setLocation(getX(), getY()+speed);
                }
    
                Cargo cargoCollide = (Cargo)getOneObjectAtOffset(0, 12, Cargo.class);
                if (cargoCollide != null) {
                    myWorld world = getWorldOfType(myWorld.class);
                    world.aiOn = true;
                    world.liftCargo(cargoCollide);
                    world.aiOn = false;
                    if (transport.getColor() == cargoCollide.getColor()) {
                        correctCargo = true;
                        cargoOnHook = cargoCollide;     
                    } else {
                        correctCargo = false;
                        cargoOnHook = cargoCollide;  
                    }
                }  
            } else {
                // ALL CARGO GONE.
                myWorld world = getWorldOfType(myWorld.class);
                world.gameOver = true;
            }
        }

    }
    
    public void moveCargo() {
        if (cargoOnHook != null) {
            if (correctCargo == false) {
                // Drop on different location.
                myWorld world = getWorldOfType(myWorld.class);
                // @TODO: SET CARGO ON EMPTY SPOT.
                //EmptyCargo emptyCargoSpot = getNearestEmptyCargo();
                //world.putCargoAtSpot(cargoOnHook, emptyCargoSpot);
                
                world.removeObject(cargoOnHook);
                cargoOnHook = null;
                correctCargo = true;
            } else {
                // Move to Transport;
                if (cargoAboveTransport == false) {
                    if (cargoOnHook.getY() > height + cargoheight && getY() > height) {
                        cargoOnHook.setLocation(cargoOnHook.getX(), cargoOnHook.getY() - speed);
                        setLocation(getX(), getY() - speed);
                    } else {
                        if (cargoOnHook.getX() != transportX && getX() != transportX) {
                            cargoOnHook.setLocation(cargoOnHook.getX() + speed, cargoOnHook.getY());
                            setLocation(getX() + speed, getY());
                        } else {
                            cargoAboveTransport = true;
                        }
                    }
                }
            }        
        }
    }
    
    public void setCargoOnTransport() {
        if (cargoOnHook != null) {
            if (cargoAboveTransport == true) {
                Transport transportIntersect = (Transport)getOneObjectAtOffset(cargoheight, 0, Transport.class); 
                if (transportIntersect != null)
                {
                    cargoInTransport = true;
                }
                else {
                    cargoOnHook.setLocation(cargoOnHook.getX(), cargoOnHook.getY() + speed);
                    setLocation(getX(), getY() + speed);
                }
            }        
        }
        
        if (cargoOnHook == null) {
            if (cargoAboveTransport == true) {
                cargoInTransport = true;
            }
        }

    }
    
    public void reset() {
        if (cargoInTransport == true) {
            correctCargo = true;
            cargoAboveTransport = false;
            reset = 1;
            cargoInTransport = false;
        }
    }
}
