package util;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Color;


public class Board {
    private int board[], neighbourOffsets[];
    public int width, height, indexLimit, cellSize, startIndex, goalIndex;
    private Color colors[];

    public Vector2 minBounds;
    public Vector2 maxBounds;
    public Vector2 position;

    public Boolean isPlacingStart = true;
    
    // Whether the start & goal states are valid.
    public Boolean validGS = true;

    public Board(int width, int height, int cellSize, Vector2 position) {
        this.width = width;
        this.height = height;
        this.position = position;
        this.cellSize = cellSize;
        
        this.minBounds = position;
        this.maxBounds = position.add(new Vector2(width*cellSize - 1, height*cellSize - 1));

        // Colors for different values
        this.colors = new Color[] {Color.LIGHT_GRAY, Color.GREEN, Color.DARK_GRAY, Color.BLACK};

        // Left, right, up, down, NE, SE, SW, NW
        //this.neighbourOffsets = new int[] {-1, 1, -width - 1, width + 1, -width - 2, -width, width + 2, width};
        // Left, right, up, down
        this.neighbourOffsets = new int[] {-1, 1, -width - 1, width + 1};

        this.indexLimit = (width + 1) * (height + 2);

        this.board = new int[this.indexLimit];

        // set all values to -1
        for(int i = 0; i < this.indexLimit; i++){
            this.board[i] = -1;
        }

        // DEBUG: Randomize cells
        //Random r = new Random();
        //r.setSeed(1535483);



        // Set values of board that are part of game to 0
        initializeValues();
        

        // DEBUG
        /*
        for(int i = 0; i < this.board.length; i++){
            if((i % 17) == 0){
                System.out.println();
            }
            System.out.printf("%3d", this.board[i]);
        }
        for(int i = 0; i < this.board.length; i++){
            if((i % 17) == 0){
                System.out.println();
            }
            System.out.printf("|%4d %4d", i, arrToIndex(i));
        }
        */

        

    }

    public void initializeValues(){
        this.startIndex = 0;
        this.goalIndex = (width * height) - 1;

        for(int i = 0; i < this.height; i++){
            int index = (i + 1) * (this.width + 1);
            // DEBUG: randomize cells
            /*
            for(int j = 0; j < this.width; j++){
                // Generate random number from [0,3], biased towards 0.
                double randVal = Math.abs(r.nextFloat()) * 3;
                board[index + j] = (int)Math.round(randVal);
            }
            */

            // default behaviour 
            for(int j = 0; j < this.width; j++){
                board[index + j] = 0;
            }

        }
    }

    // returns a list of neighbours in arr reference
    public ArrayList<Integer> getNeighbours(int index){
        ArrayList<Integer> results = new ArrayList<Integer>();

        int arrIndex = indexToArr(index);
        for(int i = 0; i < neighbourOffsets.length; i++){
            int a = arrIndex + neighbourOffsets[i];
            if(a < 0){
                continue;
            }
            int newNeighbour = this.board[a];
            if((newNeighbour != -1) && (newNeighbour != 3)){
                results.add(a);
            }
        }
        return results;
    }

    public int indexToArr(int index){
        int a = Math.floorDiv(index, this.width);
        return index + (this.width + 1) + a;
    }

    public int arrToIndex(int arr){
        int a = Math.floorDiv(arr, this.width+1);
        return arr - this.width - a;
    }

    public void setValue(int index, int value){
        int a = indexToArr(index);
        this.board[a] = value;
    }

    public int getValue(int index){
        int a = indexToArr(index);
        return this.board[a];
    }

    public int getValueFromArr(int arr){
        return this.board[arr];
    }

    // performs appropriate behaviour to tile that is clicked on according to mouse button 
    // returns index of tile that is clicked on
    public int isClickedOn(Vector2 position, int mouseButton){
        // Get local coordinates of tile that is clicked on
        int basex = Math.round(position.x - this.position.x);
        int basey = Math.round(position.y - this.position.y);
        int xindex = Math.floorDiv(basex, this.cellSize);
        int yindex = Math.floorDiv(basey, this.cellSize);
        int a = xindex + (yindex * this.width);
        int arr = indexToArr(a);

        switch(mouseButton){
            case(MouseEvent.BUTTON1):
                this.board[arr] = (this.board[arr] + 1) % 4;
                break;

            case(MouseEvent.BUTTON3):
                this.validGS = true;

                if(this.isPlacingStart){
                    this.startIndex = a;
                    if(this.goalIndex == a){
                        this.goalIndex = -1;
                        this.validGS = false;
                    }
                }
                else{
                    this.goalIndex = a;
                    if(this.startIndex == a){
                        this.startIndex = -1;
                        this.validGS = false;
                    }
                }
                this.isPlacingStart = !this.isPlacingStart;
                
                break;
        }
        // Ensure that neither the start, nor the goal tiles are inside of an obstacle.
        if((this.board[arr] == 3) && (a == this.goalIndex || a == this.startIndex)){
            this.board[arr] = 0;
        }

        return xindex + (yindex * this.width);
    }

    public void drawBoard(Graphics g){
        int index = 0;
        for(int y = 0; y < this.height; y++){
            for(int x = 0; x < this.width; x++){
                int a = getValue(index);
                g.setColor(this.colors[a]);
                int posx = Math.round(this.position.x + this.cellSize * x);
                int posy = Math.round(this.position.y + this.cellSize * y);
                g.fillRect(posx, posy, this.cellSize, this.cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(posx, posy, this.cellSize, this.cellSize);
                g.setColor(Color.BLACK);
                posx += 2;
                posy += this.cellSize - 2;
                if(index == this.goalIndex){
                    g.drawString("G", posx, posy);
                }
                else if(index == this.startIndex){
                    g.drawString("S", posx, posy);
                }
                index++;
            }
        }
    }

    // Get coordinates of top left corner of particular tile
    public Vector2 getTileCoords(int index){
        int y = Math.floorDiv(index, this.width);
        int x = index % this.width;
        float posx = this.position.x + this.cellSize * x;
        float posy = this.position.y + this.cellSize * y;
        return new Vector2(posx, posy);
    }

}