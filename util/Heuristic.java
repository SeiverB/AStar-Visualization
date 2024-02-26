package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Heuristic {
    
    private Node endNode;
    public HashMap<Integer, Integer> nodeRatios;
    private float passableToNot, averageNodeCost;

    public Heuristic(Node endNode, HashMap<Integer, Integer> nodeRatios, int boardSize) {
        this.endNode = endNode;
        this.nodeRatios = nodeRatios;

        // Get ratio of passable tiles to impassable
        this.passableToNot = (boardSize - (float)nodeRatios.get(0)) / boardSize;
        // distribute this value according to custom distribution that seems to work aight
        this.passableToNot = (float)Math.pow(this.passableToNot, 0.5f/this.passableToNot);

        // Average cost of nodes in the graph
        this.averageNodeCost = ((float)this.nodeRatios.get(1) + (float)this.nodeRatios.get(3)*3 + (float)this.nodeRatios.get(4)*4)/(boardSize-(float)this.nodeRatios.get(0));

    }

    public float estimate(Node node){
        return this.endNode.getBasePosition().distanceManhattan(node.getBasePosition());
    }
    
    public float estimate_euc(Node node){
        return this.endNode.getBasePosition().distance(node.getBasePosition());
    }
    
    // Experimental heuristic
    public float estimate2(Node node, Boolean useHeuristic3){
        //float dist = this.endNode.getBasePosition().distanceManhattan(node.getBasePosition());
        //dist = (float)Math.pow((dist / 2), 2);
        float dist;
        if(!useHeuristic3){
            dist = this.endNode.getBasePosition().worstCaseDistance(node.getBasePosition());
        }
        else{
            dist = estimate3(node);
        }

        List<Integer> arr = new ArrayList<>(this.nodeRatios.keySet());
        
        // Worst case
        Collections.sort(arr, Collections.reverseOrder());

        // Best Case
        //Collections.sort(arr);


        float heuristicValue = 0;
        for(int i = 0; i < arr.size(); i++){
            int terrainCost = arr.get(i);
            int terrainAmount = this.nodeRatios.get(terrainCost);
            float subtr = Math.min((float)terrainAmount, dist);
            dist -= subtr;
            heuristicValue += subtr * terrainCost;
            if(dist <= 0){
                break;
            }
        }
        return heuristicValue;
    }

    // Experimental heuristic
    public float estimate22(Node node, Boolean useHeuristic3, int num_nodes){

        float dist;
        if(!useHeuristic3){
            dist = this.endNode.getBasePosition().worstCaseDistance(node.getBasePosition());
        }
        else{
            dist = estimate3(node);
        }

        List<Integer> arr = new ArrayList<>(this.nodeRatios.keySet());
    

        float heuristicValue = 0;
        for(int i = 0; i < arr.size(); i++){
            int terrainCost = arr.get(i);
            int terrainAmount = this.nodeRatios.get(terrainCost);
            heuristicValue += terrainCost * terrainAmount;
        }
        heuristicValue = (heuristicValue / num_nodes) * dist;
        
        return heuristicValue;
    }


    // Manhattan distance, but its also the worst possible path from location to goal in submaze.
    public float estimate3(Node node){
        Vector2 nodePos1 = node.getBasePosition();
        Vector2 nodePos2 = endNode.getBasePosition();
        Vector2 absDist = nodePos1.absSubtract(nodePos2);

        int roundx = (int)(absDist.x + 1);
        int roundy = (int)(absDist.y + 1);

        Boolean oddx = (roundx % 2) == 1;
        Boolean oddy = (roundy % 2) == 1;

        // int oddicator = Math.ceilDiv(((int)(absDist.x) % 2) + ((int)(absDist.y) % 2), 2);
        int pildim, talldim, oddicator;
        if(oddx || oddy){
            pildim = oddx ? roundx : roundy;
            talldim = !oddx ? roundx : roundy;
            oddicator = 1;
        }
        else{
            pildim = roundx;
            talldim = roundy;
            oddicator = 0;
        }

        float floor2 = Math.floorDiv(pildim, 2);
        return (talldim * (floor2 + oddicator)) + floor2 - 1;
    }

    // heuristic based on a weighted average of best and worst case
    // where the number of passable tiles determines how the best/worst case are weighted
    // Also, multiply this by the average tile cost of the board.
    public float estimate4(Node node){
        float worstcase = estimate3(node) * (1 - passableToNot);
        float bestcase = (estimate(node) * passableToNot)*1.01f;
        return (worstcase + bestcase) * this.averageNodeCost;
    }
}
