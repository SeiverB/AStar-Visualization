package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

class Sorter implements Comparator<Node>{

    @Override
    public int compare(Node n1, Node n2) {
        float n1cost = n1.getEstimatedTotalCost();
        float n2cost = n2.getEstimatedTotalCost();

        if(n1cost > n2cost){
            return 1;
        }
        else if (n1cost < n2cost){
            return -1;
        }
        return 0;
    }
}

class CostSorter implements Comparator<Node>{

    @Override
    public int compare(Node n1, Node n2) {
        float n1cost = n1.getCostSoFar();
        float n2cost = n2.getCostSoFar();

        if(n1cost > n2cost){
            return 1;
        }
        else if (n1cost < n2cost){
            return -1;
        }
        return 0;
    }
}

public class PathFindingList {
    private PriorityQueue<Node> nodeList;

    public PathFindingList() {
        this.nodeList = new PriorityQueue<Node>(new Sorter());
    }
    
    public PriorityQueue<Node> getList(){
        return this.nodeList;
    }

    public void addNode(Node n){
        this.nodeList.add(n);
    }

    public void removeNode(Node n){
        this.nodeList.remove(n);
    }

    public Node getSmallest(){
        return this.nodeList.peek();
    }

    // This is probably not what you want
    public boolean doWeHaveBestPossiblePath(ArrayList<Connection> bestPath){
        
        // Get node with minimum costSoFar
        Node[] arr1 = this.nodeList.toArray(new Node[0]);
        ArrayList<Node> arr = new ArrayList<Node>(Arrays.asList(arr1));
        Node min = Collections.min(arr, new CostSorter());
        float minVal = min.getCostSoFar();

        // Get cost of current best path
        float bestPathCost = getCostOfPath(bestPath);

        if(minVal > bestPathCost){
            return true;
        }
        return false;
    }

    public static float getCostOfPath(ArrayList<Connection> path){
        float bestPathCost = 0;
        for(int i = 0; i < path.size(); i++){
            bestPathCost += path.get(i).getCost();
        }
        return bestPathCost;
    }

    public int size(){
        return this.nodeList.size();
    }

    public Boolean contains(Node n){
        return this.nodeList.contains(n);
    }

    public String toString(){
        return this.nodeList.toString();
    }


}
