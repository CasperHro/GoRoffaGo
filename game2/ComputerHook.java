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
    
    int cargoheight = 35;
    int transportX;
    int transportY;
    
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
            
            getCargoOnHook();
            moveCargo();
            setCargoOnTransport();
            reset();
        }
    }
    
    public double getDistance(Actor actor) {
        return Math.hypot(actor.getX() - getX(), actor.getY() - getY());
    }
    
    public Transport getTransport() {
        List<Transport> nearTransports = getObjectsInRange(1600, Transport.class);
       
        String color = "";
        Transport nearestTransport = null;
        double nearestDistance = 800;
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
        List<Cargo> nearCargos = getObjectsInRange(1600, Cargo.class);

        Cargo nearestCargo = null;
        double nearestDistance = 800;
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
    
    public void getCargoOnHook() {
        Cargo nearbyCargo = getNearestCargoByColor(transport.getColor());
        
        if(nearbyCargo != null) {
            if (getX() != nearbyCargo.getX()) {
                    setLocation(getX()-speed, height);
            }
            else if(getY() != nearbyCargo.getY()) {
                setLocation(getX(), getY()+speed);
            }

            Cargo cargoCollide = (Cargo)getOneObjectAtOffset(0, 40, Cargo.class); 
            if (cargoCollide != null) {
                if (transport.getColor() == cargoCollide.getColor()) {
                    correctCargo = true;
                    cargoOnHook = cargoCollide;     
                } else {
                    correctCargo = false;
                    cargoOnHook = cargoCollide;  
                }
            }  
        }
    }
    
    public void moveCargo() {
        if (cargoOnHook != null) {
            if (correctCargo == false) {
                // Drop on different location.
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
                if (cargoOnHook.getY() < transportY + cargoheight && getY() < transportY) {
                        cargoOnHook.setLocation(cargoOnHook.getX(), cargoOnHook.getY() + speed);
                        setLocation(getX(), getY() + speed);
                } else {
                    cargoInTransport = true;
                }
            }        
        }

    }
    
    public void reset() {
        if (cargoOnHook != null) {
            if (cargoAboveTransport == true) {
                if (cargoInTransport == true) {
                        correctCargo = true;

                    if (getY() < height) {
                        setLocation(getX(), getY()-speed);
                    } else {
                        cargoInTransport = false;
                        cargoAboveTransport = false;
                    } 
                }
            }
        }
    }
}
