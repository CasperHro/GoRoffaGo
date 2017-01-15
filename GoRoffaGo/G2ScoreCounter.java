import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
import java.awt.Color;

/**
 * Counter that displays a text and number.
 */
public class G2ScoreCounter extends Actor
{
    private String text;
    private int transportScoreValue = 0;

    public G2ScoreCounter(String playerString)
    {
        text = playerString;
        updateTransportScoreImage();
    }
    
    public void act() {
        // We only act on scores, no actions here
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
        setImage(new GreenfootImage(text + transportScoreValue, TextSize.size(16), Color.white, new Color(0,0,0,0)));
    }
}
