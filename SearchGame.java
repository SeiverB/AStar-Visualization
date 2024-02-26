import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList; // For arbitrary size lists 
import java.util.HashMap;

import util.Node;
import util.AStar;
import util.Board;
import util.Connection;
import util.Heuristic;
import util.Vector2;

public class SearchGame extends JPanel implements MouseListener, MouseMotionListener{
    
    static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 655;

    // add a timer for automatically stepping
    private Timer timer; 

    // Game state
    // 0 = level build, 1 = running level
    private int gameState;

    // Game board
    private Board board;

    // List of Nodes
    private ArrayList<Node> nodeList;

    public AStar astar;

    public int mousex = 0;
    public int mousey = 0;

    // Cost lookup
    // 0 = open = cost of 1
    // 1 = grass = cost of 3
    // 2 = swamp = cost of 4
    private static int[] costLookup = {1, 3, 4, 0};
    
    // Node type ratios used to calculate heuristic 
    private HashMap<Integer, Integer> nodeRatios = new HashMap<Integer, Integer>();

    public SearchGame(){

        int boardWidth = 16;
        int boardHeight = 16;
        int cellSize = 40;

        this.gameState = 0;

        // Center board on screen
        Vector2 position = new Vector2((WINDOW_WIDTH - (boardWidth * cellSize)) / 2, 8);

        this.board = new Board(boardWidth, boardHeight, cellSize, position);

        //listen for mouse events (clicks and movements) on this object
        addMouseMotionListener(this);
        addMouseListener(this);

    }

    public boolean isInRange(Vector2 position, Vector2 minBounds, Vector2 maxBounds){
        if(((position.x >= minBounds.x) && (position.x <= maxBounds.x)) &&
        ((position.y >= minBounds.y) && (position.y <= maxBounds.y))){
            return true;
        }
        return false;
    }

    public void drawCenteredString(Graphics g, Vector2 pos, String text, Font font){
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        int width = metrics.stringWidth(text);
        int height = metrics.getHeight();
        g.drawString(text, Math.round(pos.x - (width / 2)), Math.round(pos.y + (height / 2)));
    }

    public boolean isBoardValid(){
        return this.board.validGS;
    }
    
    public void buildGame(Board board){
        Node StartNode = null; 
        Node EndNode = null;
        this.nodeList = new ArrayList<Node>();
        nodeRatios = new HashMap<Integer, Integer>();

        for(int i = 0; i < costLookup.length; i++){
            nodeRatios.put(costLookup[i], 0);
        }


        /* DEBUG
        for(int i = 0; i < 16 * 16; i++){
            if((i % 17) == 0){
                System.out.println();
            }
            System.out.printf("%3d", this.board.getValueFromArr(i));
        }
        */

        // Build list of nodes
        for(int i = 0; i < (board.width * board.height); i++){
            Vector2 nodePos = new Vector2(i % board.width, Math.floorDiv(i, board.width));
            Node current = new Node(nodePos, board.getTileCoords(i), i, costLookup[board.getValue(i)]);

            if(i == board.startIndex){
                StartNode = current;
            }
            else if(i == board.goalIndex){
                current.setGoal(true);
                EndNode = current;
            }            

            this.nodeList.add(current);
        }

        // Build node connections
        for(int i = 0; i < this.nodeList.size(); i++){
            Node current = this.nodeList.get(i);
            int cost = costLookup[board.getValue(i)];
            int newVal = this.nodeRatios.get(cost) + 1;
            this.nodeRatios.put(cost, newVal);
            
            // If its a barrier, it won't have any conns. 
            if(board.getValue(i) == 3){
                continue;
            }
            ArrayList<Integer> neighArrs = board.getNeighbours(i);
            for(int j = 0; j < neighArrs.size(); j++){
                    int arr = neighArrs.get(j);
                    int index = board.arrToIndex(arr);
                    Connection conn = new Connection(costLookup[board.getValueFromArr(arr)], current, this.nodeList.get(index)); 
                    current.addConnection(conn);
            }

        }

        if((StartNode == null) || (EndNode == null)){
            return;
        }
        Heuristic heuristic = new Heuristic(EndNode, this.nodeRatios, this.board.width * this.board.height);
        this.astar = new AStar(StartNode, EndNode, heuristic);
    }

    public void startGame(){
        this.gameState = 1;
        buildGame(this.board);
        this.timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doStep();
                timer.restart();
            }
        });
    }
    
    public void setFrameAdvanceTime(float time){
        int newTime = Math.round(time * 1000);
        this.timer.setDelay(newTime);
        this.timer.setInitialDelay(newTime);
    }

    public void exitGame(){
        this.astar = null;
        this.nodeList = new ArrayList<Node>();
        this.gameState = 0;
    }

    public void doStep(){
        //System.out.println("result: " + this.astar.doStep());
        this.astar.doStep();
    }

    public void setPauseState(Boolean state){
        // if we are to be paused
        if(state){
            timer.stop();
        }
        // if we are to be unpaused
        else{
            doStep();
            timer.start();
        }
    }

    public void advanceFrame(){
        doStep();
    }

    public void clearBoard(){
        if(this.gameState == 0){
            board.initializeValues();
        }
    }

    // Redraws the graphics on the game window
    public void paintComponent(Graphics g){
        // Set background to black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, 800);

        g.setColor(Color.WHITE);
        this.board.drawBoard(g);

        // only draw if simulation is started
        if(this.gameState != 0){

    
            this.astar.drawPathfinding(g, this.board);

            if(this.astar.goalState > 0){
                this.astar.drawPath(g, this.board, this.astar.getBestPath(false));
            }
        }
        else{
            String str="G";
                if(this.board.isPlacingStart){
                    str = "S";
                }
                g.drawString(str, (int)mousex, (int)mousey);
        }
    }

    // Capture mouse drag events
    @Override
    public void mouseDragged(MouseEvent e) {
        this.mousex = e.getX();
        this.mousey = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e){
        Vector2 clicked = new Vector2(e.getX(), e.getY());

        if((this.gameState == 0) && isInRange(clicked, board.minBounds, board.maxBounds)){
            this.board.isClickedOn(new Vector2(e.getX(), e.getY()), e.getButton());
        }



    }

    // Capture mouse move events
    @Override
    public void mouseMoved(MouseEvent e) {
        this.mousex = e.getX();
        this.mousey = e.getY();
    }

    @Override
    public void mouseExited(MouseEvent e){
    }

    @Override
    public void mouseEntered(MouseEvent e){
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

}