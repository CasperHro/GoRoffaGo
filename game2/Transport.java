import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Transport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Transport extends Actor
{
    Cargo cargo = null;
    public int resetTransport = 0;
    public int transportCount = 0;
    int pause = 120;
    public String color;
    
    public Transport()
    {
        GreenfootImage image = getImage();  
        image.scale(120, 60);
        setImage(image);
    }
    
    /**
     * Act - do whatever the Transport wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        cargo = (Cargo)getOneObjectAtOffset(0, 0, Cargo.class);
        if (cargo != null) {
            System.out.println(getColor());
            System.out.println(cargo.getColor());
            if (cargo.getColor() == getColor()) {
                resetTransport = 1;
            }
        }
        
        if (resetTransport == 1) {
            if(pause > 0) {
                setLocation(getX()+3, getY());
                pause--;
            }
              
            if(pause == 0) {
                // @TODO: NEW RANDOM TRANSPORT ACTOR.
                if (getX() >= 720) {
                    setLocation(getX()-3, getY());
                }
                else {
                    resetTransport = 0;
                    pause = 120;
                    transportCount += 1;
                }
                

            }

        }
        
    }
    
    public String getColor() {
        return color;
    }
}
