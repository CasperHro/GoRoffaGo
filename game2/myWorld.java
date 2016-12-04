import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Write a description of class myWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myWorld extends World
{   
    int shipCentre = 405;
    int waterLevel = 680;
    int width = 4; //number of cargo
    int cargoWidth = 80+5; //cargo=80px, padding = 5px
    
    
    int maxHeight = 4;
    int maxWidth = width;
    int[] grid;
    
    
    /**
     * Constructor for objects of class myWorld.
     * 
     */
    public myWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // False makes the world boundless.
        super(1000, 800, 1, false);
        createGrid(maxHeight,maxWidth);
        prepare();   
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        sky sky = new sky();
        sky sky2 = new sky();
        Water water = new Water();
        Hook hook = new Hook();
        Harbor harbor = new Harbor();
        Harbor harbor2 = new Harbor();
        Harbor harbor3 = new Harbor();
        Harbor harbor4 = new Harbor();
        Transport transport = new Transport();
        
        Counter counter = new Counter();
        
        Deck deck = new Deck();

        addObject(sky,500,200);
        addObject(sky2,500,500);
        addObject(water,500,750);
        addObject(hook,502,70);
        addObject(harbor,952,754);
        addObject(harbor2,848,754);
        addObject(harbor3,848,658);
        addObject(harbor4,952,658);
        addObject(transport,888,563);
        addObject(counter,958,650);
        addObject(deck,shipCentre,waterLevel);

        setCargo();
        //transport.setLocation(888,563); je zet dit al goed bij addObject(name, x ,y);
        //cargo3.setLocation(4056,430);
    }
    
    public void setCargo() {
        int cargoHeight = 50;
        int i = 0;
        int x = shipCentre-(cargoWidth*(width/2)-40);
        
        while(i<width){
            int randomCargoType = ThreadLocalRandom.current().nextInt(1, 3 + 1);
            if(randomCargoType==1){
                Cargo cargo = new red();
                addObject(cargo,x,waterLevel-50);
                System.out.println(cargo.getColor());
                System.out.println(cargo.getWeight());
            }
            if(randomCargoType==2){
                Cargo cargo = new blue();
                addObject(cargo,x,waterLevel-50);
                System.out.println(cargo.getColor());
                System.out.println(cargo.getWeight());
            }
            if(randomCargoType==3){
                Cargo cargo = new green();
                addObject(cargo,x,waterLevel-50);
                System.out.println(cargo.getColor());
                System.out.println(cargo.getWeight());
            }
            
            x = x+cargoWidth;
            i++;
        }
    }
    
    public int getWeightPerX(int x){
        int zero = shipCentre-(cargoWidth*(width/2)-40);
        return x;
    }
    
    
    public int getSizeGrid(int x, int y){
    	int spots = x*y;
    	maxHeight = y;
    	maxWidth = x;
    	return spots;
    }
    	
    public void createGrid(int x,int y){
    	int gridSize = getSizeGrid(x,y);
        grid = new int[gridSize];
    }
    	
    public int getSpot(int x, int y){
    	int spot = (x*maxHeight)+y;
    	return grid[spot];
    }
    	
    public int getCoordX(int spot){
        int x = spot%maxWidth;
    	return x;
    }
    	
    public int getCoordY(int spot){
        int x = spot%maxWidth;
        int y = (spot-x)/maxWidth;
    	return y;
    }
    	
    public boolean setCoord(int x,int y,int value){
    	int spot = (x*maxHeight)+y;
    	grid[spot] = value;
    	return true;
    }
    
}
