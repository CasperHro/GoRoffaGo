import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

/**
 * Write a description of class myWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myWorld extends World
{   
    int shipCentre = 375;
    int waterLevel = 525;
    int width = 4; //number of cargo
    int cargoWidth = 80+5; //cargo=80px, padding = 5px
    
    
    int maxHeight = 4;
    int maxWidth = width;
    Cargo[] grid;
    
    
    sky sky = new sky();
    sky sky2 = new sky();
    Water water = new Water();
    Hook hook = new Hook();
    Line line = new Line();
    Bar bar = new Bar();
    Harbor harbor = new Harbor();
    Harbor harbor2 = new Harbor();
    Harbor harbor3 = new Harbor();
    Harbor harbor4 = new Harbor();
    Transport transport = new Transport();
    Deck deck = new Deck();
    
    Counter scoreCounter = new Counter();
    public int tilt;
    
    public int looted = 0;
    
    boolean gameOver = false;
    int delayTiltTimer; //Timestamp van de laatste Tilt Actie
    int stepsCount=0;
    int counter =0;
    //WeightCounter WeightCounter = new WeightCounter();
    
    
    /**
     * Constructor for objects of class myWorld.
     * 
     */
    public myWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // False makes the world boundless.
        super(800, 600, 1, false);
        createGrid(maxHeight,maxWidth);
        printGrid();
        prepare();   
        System.out.println("start");
        System.out.println("Weight row 0 = "+getWeightPerX(0));
        System.out.println("Weight row 1 = "+getWeightPerX(1));
        System.out.println("Weight row 2 = "+getWeightPerX(2));
        System.out.println("Weight row 3 = "+getWeightPerX(3));
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        

        addObject(sky,400,200);
        addObject(sky2,400,500);
        addObject(water,400,600);
        addObject(hook,400,90);
        addObject(line,400,-970);
        addObject(bar,400,20);
        addObject(harbor,750,550);
        addObject(harbor2,700,550);
        addObject(harbor3,750,500);
        addObject(harbor4,700,500);
        addObject(transport,720,420);
        addObject(scoreCounter,750,500);
        addObject(deck,shipCentre,waterLevel);

        setCargo();
    }
    
    public Actor getHook()
    {
        return hook;
    }
    
    public Actor getLine()
    {
        return line;
    }
    
    public void act(){
            counter++;
            if(counter ==120 && !gameOver){ //om de 7(1~sec) ticks code uitvoeren
                delayTiltTimer++; //
                
                tilt = getTilt();
                
                deck.adjustShip();
                
                System.out.println("rotation "+deck.getRotation());
                showText("CargoWeight :"+tilt,shipCentre,waterLevel+50);
                
                showText("looted         :"+looted,720,530);
                //showText("Transport score:"+looted,720,550);
                
                
                
                
                
                counter=0;
            }else if (gameOver){
                showText("Game over "+"looted  :"+looted,shipCentre,waterLevel-250);
            }else if (stepsCount>10 || stepsCount<-10){
                gameOver=true;
            }
        }
    
        
        
        
        
        //@@ preparation
        
    public int liftCargo(Cargo cargo){//cargo->grid[ID]
        int i =0;
        for(Cargo c :grid){
            if(c==cargo){
                break;
            }
            i++;
        }
        return i;
    }    
        
        
    public void setCargo() {
        int cargoHeight = 40;
        int StackHeight = waterLevel-50;
        int height = maxHeight-1;
        int i = 0;
        int hi = 0;                                     //how high the cargo is stacked
        int x = shipCentre-(cargoWidth*(width/2)-40);

        
        while(i<width*height){
            int randomCargoType = ThreadLocalRandom.current().nextInt(1, 3 + 1);
            if(randomCargoType==1){
                Cargo cargo = new red();
                addObject(cargo,x,StackHeight);
                grid[i]=cargo;
                //System.out.println(cargo.getColor());
                //System.out.println(cargo.getWeight());
            }
            if(randomCargoType==2){
                Cargo cargo = new blue();
                addObject(cargo,x,StackHeight);
                grid[i]=cargo;
            }
            if(randomCargoType==3){
                Cargo cargo = new green();
                addObject(cargo,x,StackHeight);
                grid[i]=cargo;
            }
            
            x = x+cargoWidth;
            i++;
            if(i%width==0){
                x = shipCentre-(cargoWidth*(width/2)-40);
                StackHeight-=cargoHeight;
            }
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
       if(halfLeft>halfRight){
           tilt = 1;
       }else if (halfLeft<halfRight){
           tilt = -1;
       }else{
           tilt=0;
       }
        
       System.out.println("Balans = "+windSpeed);
       //System.out.println("halfLeft = "+halfLeft+": halfRight = "+halfRight);
       return windSpeed;
    }
    
    public int getWeightPerX(int x){
        int weight = 0;
        int i = 0;
        while(i < maxHeight){
            int spot = x+(i*width);
            //System.out.println("spot no "+spot);
            if(grid[spot]!=null){
                weight+=grid[spot].getWeight();
            }
            i++;
        }
        return weight;
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
        grid = new Cargo[gridSize];
    }
        
    public int getSpot(int x, int y){
        int spot = (x*maxHeight)+y;
        return grid[spot].getWeight();
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
        grid[spot] = value;
        return true;
    }     
    
    public void setGrid(int spot,Cargo value){
        grid[spot] = value;
    }
    
    public Cargo getValue(int x,int y){
        int spot = (x*maxHeight)+y;
        return grid[spot];
    }
    
    public int getWeight(int spot){
        return grid[spot].getWeight();
    }
    
    public void printGrid(){
        /*for(int i : grid){
            System.out.println(grid[i]);
            
           }*/
            System.out.println(grid[0]);
            System.out.println(grid.length);

    }
    
}
