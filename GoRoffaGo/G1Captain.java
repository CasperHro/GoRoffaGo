import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The G1_Captain does nothing. It only shows the controlled boat.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G1Captain extends Actor
{
    private int opacity = 255;
    private int opacityStep = -16;
    
    /**
     * Animate the transparency to create a recognizable visual indicator 
     * that attracts attention of the player.
     */
    public void act() 
    {
        opacity += opacityStep;
        if (opacity <= 96)
        {
            opacity = 96;
            opacityStep = 16;
        }
        if (opacity >= 255)
        {
            opacity = 255;
            opacityStep = -16;
        }
        
        getImage().setTransparency(opacity);
    }    
}
