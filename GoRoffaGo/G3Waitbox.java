import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math.*;
import java.util.*;

/**
 * G3GameInfo is a overlay to interupt the change of the game stages.
 * It shows information about the next stage and waits for user input 
 * to proceed.
 * 
 * @author C. Karreman 
 * @version 1.0
 */
public class G3Waitbox extends Overlay
{
    private static final String GAMEON = "\n\nPress Enter or click to continue...";

    private G3GameStage nextStage;
    
    /**
     * Contructor
     */
    public G3Waitbox(G3GameStage forStage, String message)
    {
        text = message;
        nextStage = forStage;
    }
    
    /**
     * Check the mouse click to hide
     */
    public void act() 
    {
        if (isShowing && opacity < 255)
        {
            opacity = Math.min(255, opacity + showStep);
            getImage().setTransparency(opacity);
        }
        else if (!isShowing && opacity > 0)
        {
            opacity = Math.max(0, opacity - hideStep);
            getImage().setTransparency(opacity);
            
             if (opacity <= 0)
            {
                G3Dock world = getWorldOfType(G3Dock.class);
                world.setStage(nextStage);
                world.removeObject(this);
            }
        }
        
        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("Enter"))
        {
            isShowing = false;
        }
    }    

    public String getText()
    {
        return text + GAMEON;
    }
}
