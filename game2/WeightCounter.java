import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

import java.awt.Color;
import java.awt.Graphics;

/**
 * Write a description of class WeightCounter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WeightCounter extends Actor
{
    private static final Color textColor = new Color(255, 255, 255);
    
    World world;
    private int transportScoreValue = 0;
    private static int target = 0;
    private String text;
    private int stringLength;

    public WeightCounter()
    {
        text = "Cargo Weight score: ";
        stringLength = (text.length() + 2) * 10;

        setImage(new GreenfootImage(stringLength, 30));
        GreenfootImage image = getImage();
        image.setColor(textColor);

        updateTransportScoreImage();
    }
    
    public void act() {
        World world;
        transportScoreValue = getWorld().getObjects(Transport.class).get(0).transportCount;
        updateTransportScoreImage();
    }
    
    public int getTransportScoreValue()
    {
        return transportScoreValue;
    }
   
    /**
     * Make the Transport score image
     */
    private void updateTransportScoreImage()
    {
        GreenfootImage image = getImage();
        image.clear();
        image.drawString(text + transportScoreValue, 1, 12);
    }
}
