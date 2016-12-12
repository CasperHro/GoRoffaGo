import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    Actor Transport = null;
    int movingSpeed = 4;

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
                myWorld world = getWorldOfType(myWorld.class);
                indexCargo = world.liftCargo(cargo);
                world.looted += world.getWeight(indexCargo);
                world.grid[indexCargo] = null;
                
                world.setEmpty();
                System.out.println(indexCargo);
            }
        }

        if (Greenfoot.isKeyDown("down")) {
            setLocation(getX(), getY()+3);
        
            if (cargo != null) {
                cargo.setLocation(getX(), getY()+3);
            }
        }
        
        if (Greenfoot.isKeyDown("up")) {
            setLocation(getX(), getY()-3);
            
            if (cargo != null) {
                cargo.setLocation(getX(), getY()-3);
            }
        }
        
        if (Greenfoot.isKeyDown("right")) {
            setLocation(getX()+3, getY());
            
            if (cargo != null) {
                cargo.setLocation(getX()+3, getY());
            }
        }
        
        if (Greenfoot.isKeyDown("left")) {
            setLocation(getX()-3, getY());
            
            if (cargo != null) {
                cargo.setLocation(getX()-3, getY());
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
            myWorld world = getWorldOfType(myWorld.class);
            if (world.getObjects(Transport.class).get(0).resetTransport == 1) {
                world.removeObject(cargo);
                cargo = null;
            }
        }
    
    }
     public void EmptyCargoCheck(){
         if(cargo!=null){
            emptySpot = (EmptyCargo)getOneObjectAtOffset(0, 0, EmptyCargo.class);
            if(emptySpot!=null){
                
                if (Greenfoot.isKeyDown("d")) {
                    myWorld world = getWorldOfType(myWorld.class);
                    world.grid[emptySpot.getId()]=cargo;
                    
                    world.setEmpty();
                    cargo = null;
                    //System.out.println("beep");
                }
            }
         }    
    }
}
