import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class G4_Container here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4Container extends Actor
{
    private String cargo = "cont1";
    /**
     * Act - do whatever the G4_Container wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public String getCargo()
    
    {
        return cargo;
    }
   
    /**
     * 
     * 
     */
    public void setCargo(String value) 
    {
        cargo = value;
        if (cargo.equals("cont1"))
        {
            setImage(new GreenfootImage("G4cont11.png"));
        }
        else if (cargo.equals("cont2"))
        {
            setImage(new GreenfootImage("G4cont22.png"));
        }
        else if (cargo.equals("cont3"))
        {
            setImage(new GreenfootImage("G4cont33.png"));
        }
        else 
        {
            setImage(new GreenfootImage("G4cont44.png"));
        }
    }
    
}

