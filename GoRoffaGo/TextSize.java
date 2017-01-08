import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;

/**
 * The TextSize class returns a scaled size to apply the design size to all 
 * different platforms. Windows, linux and MacOsx all handle a different font
 * so we calc the best working fontsize to be able to show all text on screen.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TextSize  
{
    // Text size scale
    private static double scale = 0.0;

    /**
     * Resturns the real size.
     */
    public static int size(int size)
    {
        if (scale == 0.0)
        {
            calcSize();
        }
        
        return (int)Math.round(size * scale);
    }

    /**
     * This function calcs the size.
     */
    private static void calcSize()
    {
        GreenfootImage tw = new GreenfootImage("O", 22, Color.white, new Color(0, 0, 0, 0));
        scale = tw.getWidth() / 13.0;
    }
}
