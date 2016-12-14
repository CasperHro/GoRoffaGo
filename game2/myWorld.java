import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

/**
 * Write a description of class myWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myWorld extends World
{   
    int shipCentre = 260;
    int deckLevel = 473;
    int width = 4; //number of cargo
    int cargoWidth = 50; //cargo=80px, padding = 5px
    
    int maxHeight = 4;
    int maxWidth = width;
    
    
    Hook p1_hook;
    Deck p1_deck;
    Transport p1_transport;
    Cargo[] p1_grid;
    EmptyCargo[] p1_emptyGrid = new EmptyCargo[width];

    Hook p2_hook;
    Deck p2_deck;
    Transport p2_transport;
    Cargo[] p2_grid;
    EmptyCargo[] p2_emptyGrid = new EmptyCargo[width];

    Clock clock;
    Counter scoreCounter;

    public int tilt;
    public int looted = 0;
    
    boolean gameOver = false;
    int delayTiltTimer; //Timestamp van de laatste Tilt Actie
    int stepsCount=0;
    int counter =0;
    int cycle;
    //WeightCounter WeightCounter = new WeightCounter();
    
    private boolean firstStep = true; // Does the first step routine
    
    /**
     * Constructor for objects of class myWorld.
     * 
     */
    public myWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // False makes the world boundless.
        super(800, 600, 1, false);
        
        // TODO: Set the paintorder for the overlaying classes
        setPaintOrder(//G2_GameInfo.class,
                  //G2_Waitbox.class,
                  //GameOver.class,
                  Deck.class,
                  Hook.class,
                  Transport.class,
                  Cargo.class,
                  Cables.class,
                  BoatBack.class
                  );

        // Prepare all initial actors
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        p1_hook = new Hook();
        p1_deck = new Deck();
    
        p2_hook = new ComputerHook();
        p2_deck = new Deck();

        clock = new Clock(false, true, 0, null);
        scoreCounter = new Counter();

        addObject(p1_deck, shipCentre, deckLevel - 52);
        addObject(p1_hook, 35, 300);
        
        addObject(p2_deck, getWidth() - shipCentre, deckLevel - 52);
        addObject(p2_hook, getWidth() - 35, 300);
        
        addObject(clock, 750, 40);
        addObject(scoreCounter, 750, 20);
        
        Water water = new Water();
        addObject(water, 400, getHeight() - (water.getImage().getHeight() / 2));
    }
    
    /**
     * Does the run initialization. To prevent 
     */
    private void firstStep()
    {
        // Reset the flag to prevent another call to this function
        firstStep = false;
        
        // Start music
        //backgroundMusic.playLoop();
        
        // And show game info overlay
        //G2_GameInfo g2_info = new G2_GameInfo();
        //addObject(g2_info, getWidth() / 2, getHeight() / 2); // Centered on screen
        startGame();
    }

    public void startGame()
    {
        // First clear the current playfield and reset the ships
        removeObjects(getObjects(Transport.class));
        removeObjects(getObjects(Cargo.class));
        
        
        createGrid(maxHeight, maxWidth);
        
        setCargo();
        setEmpty();
        
        // TODO: Call create for first transport
        setTransport(720, 420);
        
        printGrid();
        System.out.println("start");
        System.out.println("Weight row 0 = "+getWeightPerX(0));
        System.out.println("Weight row 1 = "+getWeightPerX(1));
        System.out.println("Weight row 2 = "+getWeightPerX(2));
        System.out.println("Weight row 3 = "+getWeightPerX(3));
    }

    public void firstRun()
    {
        startGame();
    }
    
    public void act(){
        // Only start music when running the game
        if (firstStep)
        {
            firstStep();
        }
        
        
        counter++;
        if(counter == 90 && !gameOver) { //om de 7(1~sec) ticks code uitvoeren
            delayTiltTimer++; //
            
            tilt = getTilt();
            if(cycle == 1) { //adjust ship once every 2 cycles
                p1_deck.adjustShip(); //pas de hoek van het schip aan
                
                for(int i = 0; i < p1_grid.length; i++) {//pas de hoek en plek van de cargo aan
                    if(p1_grid[i] != null) {
                        p1_grid[i].adjustBooty();
                    }
                }
                
                cycle=0;
            }else{
                cycle++;
            }
            //System.out.println("rotation "+deck.getRotation());
            showText("CargoWeight :" + tilt, shipCentre, deckLevel + 50);
            
            showText("looted         :" + looted,720,530);
            //showText("Transport score:"+looted,720,550);
            
            
            counter=0;
        }else if (gameOver){
            showText("Game over "+"looted  :" + looted, shipCentre, deckLevel - 250);
        }else if (stepsCount > 10 || stepsCount < -10){
            gameOver = true;
        }
    }
    
        
        
        
        
        //@@ preparation
        
    public void liftCargo(Cargo cargo){
        int i =0;
        for(Cargo c : p1_grid){
            if(c==cargo){
                break;
            }
            i++;
        }
        
        looted += getWeight(i);
        p1_grid[i] = null;
        
        setEmpty();
        System.out.println(i);
    }
    
    public void setTransport(int paramX, int paramY) {
        List<String> availableColors = getAvailableColors();
        
        System.out.println(availableColors);
        
        int idx = new Random().nextInt(availableColors.size());
        String randomTransportType = (availableColors.get(idx));

        p1_transport = new Transport();
        p1_transport.setColor(randomTransportType);
        addObject(p1_transport, paramX, paramY);
    }
        
        
    public void setCargo() {
        int cargoHeight = 50;
        int space = 4;
        int StackHeight = deckLevel-(cargoHeight/2);
        int height = maxHeight-1;
        int i = 0;
        int hi = 0;                                     //how high the cargo is stacked
        int x = shipCentre-(width/2*(cargoWidth+space))+((cargoWidth+space)/2);

        
        while(i<width*height){
            if(getWeightPerX(i%width)<=25){
                int randomCargoType = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                if(randomCargoType==1){
                    p1_grid[i] = new red();
                    addObject(p1_grid[i],x,StackHeight);
                    //System.out.println(cargo.getColor());
                    //System.out.println(cargo.getWeight());
                }
                if(randomCargoType==2){
                    p1_grid[i] = new blue();
                    addObject(p1_grid[i],x,StackHeight);
                }
                if(randomCargoType==3){
                    p1_grid[i] = new green();
                    addObject(p1_grid[i],x,StackHeight);
                }
            }
            x += cargoWidth+4;// spacing 6 pixles
            i++;
            if(i%width==0){
                x = shipCentre-(width/2*(cargoWidth+space))+((cargoWidth+space)/2);
                StackHeight-=cargoHeight;
            }
            
        }
        
    }
    
    public void setEmpty(){
        for(int i = 0; i<width; i++){
            if(p1_emptyGrid[i]==null){
                p1_emptyGrid[i] = new EmptyCargo();
                p1_emptyGrid[i].setId(getCargoPerX(i)*width+i);
                addObject(p1_emptyGrid[i],p1_grid[i].getX(),p1_grid[i].getY()+(getCargoPerX(i)*-45));
            }else{
                p1_emptyGrid[i].setLocation(p1_grid[i].getX(),p1_grid[i].getY()+(getCargoPerX(i)*-45));
                
                p1_emptyGrid[i].setId(getCargoPerX(i)*width+i);
            }
            //System.out.println("row "+i+" - "+getCargoPerX(i));
        }
    }
    
    public int getTilt(){
        int half = width/2-width%2;
        int i=0;
        int halfLeft=0;
        int halfRight=0;
        int windSpeed;
        
       while(i<half){
            halfLeft+=getWeightPerX(i)*(half-i);
            halfRight+=getWeightPerX(width-i-1)*(half-i);
            i++;
       }
       windSpeed = halfLeft-halfRight;
        
       //System.out.println("Balans = "+windSpeed);
       //System.out.println("halfLeft = "+halfLeft+": halfRight = "+halfRight);
       return windSpeed;
    }
    
    public int getWeightPerX(int x){
        int weight = 0;
        int i = 0;
        while(i < maxHeight){
            int spot = x+(i*width);
            //System.out.println("spot no "+spot);
            if(p1_grid[spot]!=null){
                weight+=p1_grid[spot].getWeight();
            }
            i++;
        }
        return weight;
    }
    
    public int getCargoPerX(int x){
        int stack = 0;
        for(int i = 0;i < maxHeight; i++){
            int spot = x+(i*width);
            //System.out.println("spot no "+spot);
            if(p1_grid[spot]!=null){
                stack+=1;
            }
        }
        return stack;
    }
    
    public int getSizeGrid(int x, int y){
        int spots = x*y;
        maxHeight = y;
        maxWidth = x;
        return spots;
    }
        
    
    
    
    
    public void removeCargo(char Cargo){
        //removeObject(Cargo);
        //setGrid(i,0);
    }
    
    
    
    
    
    //@@@ Grid functies
    public void createGrid(int x,int y){
        int gridSize = getSizeGrid(x,y);
        p1_grid = new Cargo[gridSize];
    }
        
    public int getSpot(int x, int y){
        int spot = (x*maxHeight)+y;
        return p1_grid[spot].getWeight();
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
        
    public boolean setCoord(int x,int y,Cargo value){
        int spot = (x*maxHeight)+y;
        p1_grid[spot] = value;
        return true;
    }     
    
    public void setGrid(int spot,Cargo value){
        p1_grid[spot] = value;
    }
    
    public Cargo getValue(int x,int y){
        int spot = (x*maxHeight)+y;
        return p1_grid[spot];
    }
    
    public int getWeight(int spot){
        return p1_grid[spot].getWeight();
    }
    
    public void printGrid(){
        /*for(int i : grid){
            System.out.println(grid[i]);
            
           }*/
            System.out.println(p1_grid[0]);
            System.out.println(p1_grid.length);

    }
    
    public List getAvailableColors(){
        List<String> colors = new ArrayList<String>();
        String color = "";
        
        for (Cargo c : p1_grid) {
            if (c != null) {
                color = c.getColor();
                if(!colors.contains(color)){
                    colors.add(color);
                }            
            }
        }
        
        return colors;
    }
    
    public boolean putCargoAtSpot(Cargo cargo, EmptyCargo emptySpot)
    {
        // TODO: Check both players
        if (emptySpot != null)
        {
            p1_grid[emptySpot.getId()] = cargo;
            setEmpty();
            return true;
        }
        return false;
    }
}
