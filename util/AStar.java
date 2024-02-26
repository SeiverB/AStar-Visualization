package util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

public class AStar {
    
    private Node start; 
    private Node end;
    private Heuristic heuristic;
    private PathFindingList open;
    private ArrayList<Connection> goalPath;
    public int goalState = 0;

    public AStar(Node start, Node end, Heuristic heuristic) {
        this.start = start;
        this.end = end;
        // Set ending node with goal value
        this.end.setGoal(true);
        this.heuristic = heuristic;
        
        this.goalPath = null;

        // List of "open" nodes
        this.open = new PathFindingList();

        // Initialize start node
        this.start.setCostSoFar(0);
        // this.start.setEstimatedTotalCost(this.heuristic.estimate(this.start));

        open.addNode(this.start);

    }

    public PriorityQueue<Node> getList(){
        return this.open.getList();
    }

    // Do a single step of pathfinding. Returns 1 if the goal has been found, otherwise returns 0.
    // Returns -1 if no goal can be found
    public int doStep(){

        if(this.open.size() == 0){
            if(this.goalState <= 0){
                this.goalState = -1;
                return -1;
            }
            getBestPath(true);
            this.goalState = 1;
            return 1;
        }

        Node currentNode = open.getSmallest();

        if(currentNode.isGoal()){
            this.goalState = 1;
        }

        if(this.goalState >= 1){
            getBestPath(true);
        }


        /* DEBUG: print out open list
        Node[] arr1 = this.open.getList().toArray(new Node[0]);
        ArrayList<Node> arr = new ArrayList<Node>(Arrays.asList(arr1));

        for(int i = 0; i < arr.size(); i++){
            System.out.print(" " + arr.get(i).getEstimatedTotalCost());
        }
        System.out.println();
        */

        ArrayList<Connection> connections = currentNode.getConnections();

        // Loop thru each connection from currentNode
        for(int i = 0; i < connections.size(); i++){
            Connection connection = connections.get(i);
            Node endNode = connection.getToNode();
            float endNodeCost = currentNode.getCostSoFar() + connection.getCost();
            float endNodeHeuristic;

            // If node is considered closed, then we may have to skip this connection
            // Or re-open it.
            if(endNode.isClosed()){

                // If new route isn't shorter than previous, then skip
                if(endNode.getCostSoFar() <= endNodeCost){
                    continue;
                }

                // Otherwise, we must re-open it
                endNode.setClosed(false);

                //int thisNodeTerrainCost = endNode.getTerrainCost();
                //heuristic.nodeRatios.put(thisNodeTerrainCost, heuristic.nodeRatios.get(thisNodeTerrainCost) + 1);

                // Re-use node's old cost values to calculate its heuristic without calling
                // potentially expensive heuristic fn
                endNodeHeuristic = endNode.getEstimatedTotalCost() - endNode.getCostSoFar();
                //endNodeHeuristic = this.heuristic.estimate4(endNode);       
            }

            // Skip if the node is open and we've not found a better route
            else if(open.contains(endNode)){
                // If new route is not shorter than previous, then skip
                if(endNode.getCostSoFar() <= endNodeCost){
                    continue;
                }

                // Re-use node's old cost values to calculate its heuristic without calling
                // potentially expensive heuristic fn
                endNodeHeuristic = endNode.getEstimatedTotalCost() - endNode.getCostSoFar();    
                //endNodeHeuristic = this.heuristic.estimate4(endNode);    
                this.open.removeNode(endNode);
            }
            // Else we have an unvisited node.
            else{
                endNodeHeuristic = this.heuristic.estimate4(endNode);         
            }

            // Update the node, (cost, estimate, bestConnection)
            endNode.setCostSoFar(endNodeCost);
            endNode.setBestConnection(connection);
            endNode.setEstimatedTotalCost(endNodeCost + endNodeHeuristic);
            this.open.addNode(endNode);
        }

        // As we have opened all the connected nodes, close this node.
        this.open.removeNode(currentNode);
        currentNode.setClosed(true);

        
        //int cost = currentNode.getTerrainCost();
        //int newVal = this.heuristic.nodeRatios.get(cost) - 1;
        //this.heuristic.nodeRatios.put(cost, newVal);

        return this.goalState;
    }

    public ArrayList<Connection> getBestPath(Boolean forceGenerateNew){

        ArrayList<Connection> newgoalPath = new ArrayList<Connection>();

        // If we haven't found goal yet.
        if(this.goalState < 1){
            return newgoalPath;
        }

        // If we have already generated the best path
        if((this.goalPath != null) && !forceGenerateNew){
            return this.goalPath;
        }

        Node currentNode = this.end;
        while(currentNode != this.start){
            Connection curCon = currentNode.getBestConnection();
            newgoalPath.add(curCon);
            currentNode = curCon.getFromNode();
        }
        Collections.reverse(newgoalPath);
        this.goalPath = newgoalPath;
        return goalPath;
    }

    public void drawPath(Graphics g, Board board, ArrayList<Connection> connections){

        Vector2 bib = new Vector2(board.cellSize/2, board.cellSize/2);
        g.setColor(Color.YELLOW);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        
        for(int i = 0; i < connections.size(); i++){
            Connection curCon = connections.get(i);
            Vector2 pos2 = curCon.getToNode().getPosition().add(bib);
            Vector2 pos1 = curCon.getFromNode().getPosition().add(bib);
            g2.draw(new Line2D.Float(pos1.x, pos1.y, pos2.x, pos2.y));
        }

    }

    public void drawPathfinding(Graphics g, Board board){

        PriorityQueue<Node> a = this.open.getList();

        Iterator<Node> value = a.iterator(); 

        //Vector2 bib = new Vector2(board.cellSize/2, board.cellSize/2);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));

        int f = 0;
        while (value.hasNext()) { 
            Node b = value.next();
            Vector2 pos = b.getPosition();
            g.setColor(Color.RED);
            if(f == 0){
                g.setColor(Color.MAGENTA);
            }
            g.fillRect(Math.round(pos.x), Math.round(pos.y), board.cellSize, board.cellSize);
                        
            // Loop thru each connection from currentNode
            /*
            ArrayList<Connection> connections = b.getConnections();
            g.setColor(Color.GREEN);
            pos = pos.add(bib);
            for(int i = 0; i < connections.size(); i++){
                Connection connection = connections.get(i);
                Node endNode = connection.getToNode();
                Vector2 pos2 = endNode.getPosition();
                pos2 = pos2.add(bib);
                g2.draw(new Line2D.Float(pos.x, pos.y, pos2.x, pos2.y));
            }
            */
            f++;     
        }
    }

}