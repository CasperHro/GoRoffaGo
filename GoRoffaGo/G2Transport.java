import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G2Transport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Transport extends Actor
{
    private G2Cargo cargo = null;
    private int pause = 60;
    private String color;
    private int scale = 0;
    private int counter = 2;
    
    public G2Transport(String value)
    {
        color = value;
    }

    protected void addedToWorld(World world)
    {
        switch(color)
        {
            case "red":
                setImage("g2_truckRed.png");
                break;
            case "green":
                setImage("g2_truckYellow.png");
                break;
            case "blue":
                setImage("g2_truckBlue.png");
                break;
            default:
                setImage("g2_truckGreen.png");
                break;
        }
    }
    
    /**
     * Act - do whatever the Transport wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public boolean setCargo(G2Cargo container)
    {
        if (cargo == null && container.getColor() == getColor()) {
            cargo = container;
            cargo.setLocation(getX(), getY()-12);
            return true;
        }

        return false;
    }
    
    public void act() 
    {
        if (cargo != null) {
            if(pause > 0) {
                GreenfootImage image = getImage();
                GreenfootImage cimage = cargo.getImage();
                if (image.getWidth() > 9 && scale == 1) {
                    image.scale(image.getWidth() - 1, image.getHeight() - 1);
                    setLocation(getX(), getY()+1);
                    
                    cimage.scale(cimage.getWidth() - 1, cimage.getHeight() - 1);
                    cargo.setLocation(cargo.getX(), cargo.getY()+1);
                }

                
                pause--;
            } else {
                G2Dock world = (G2Dock)getWorld();
                world.removeObject(cargo);
                world.removeTransport(this);
                cargo = null;
            }
            
            if (counter > 0) {
                scale = 0;
                counter--;
            } else {
                scale = 1;
                counter = 2;
            }

        }
        
    }
    
    public String getColor() {
        return color;
    }
    
    public boolean hasCargo() {
        return cargo != null;
    }
}
