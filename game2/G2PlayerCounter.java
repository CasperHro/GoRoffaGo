import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

import java.awt.Color;
import java.awt.Graphics;

/**
 * Counter that displays a text and number.
 */
public class G2PlayerCounter extends Actor
{
    private static final Color textColor = new Color(0, 0, 0);
    
    private int transportScoreValue = 0;
    private static int target = 0;
    private String text;
    private int stringLength;

    public G2PlayerCounter()
    {
        text = "Player score: ";
        stringLength = (text.length() + 2) * 10;

        
        setImage(new GreenfootImage(stringLength, 30));
        GreenfootImage image = getImage();
        image.setColor(textColor);
        image.setColor(Color.white);
        image.fill();
        
        
        updateTransportScoreImage();
    }
    
    public void act() {
    }
    
    public void addTransportScore(int value)
    {
        transportScoreValue += value;
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