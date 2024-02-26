package util;

import java.util.ArrayList;

public class Node {

    // If this node has been visited (is considered a closed node)
    private boolean isGoal;
    private boolean isClosed;

    // position in screen coordinates
    private Vector2 position;

    // Position local to the board
    private Vector2 basePosition;

    // List of outgoing connections
    private ArrayList<Connection> outgoing;
    private int nodeIndex;
    private int terrainCost;


    private float costSoFar;
    private float estimatedTotalCost;
    private Connection bestConnection;

    public Node(Vector2 basePosition, Vector2 position, int nodeIndex, int terrainCost) {
        this.isGoal = false;
        this.isClosed = false;
        this.outgoing = new ArrayList<Connection>();
        this.position = position;
        this.basePosition = basePosition;
        this.nodeIndex = nodeIndex;
        this.terrainCost = terrainCost;
    }
    
    public Vector2 getBasePosition() {
        return basePosition;
    }

    public void setBasePosition(Vector2 basePosition) {
        this.basePosition = basePosition;
    }

    public int getTerrainCost() {
        return terrainCost;
    }

    public void setTerrainCost(int terrainCost) {
        this.terrainCost = terrainCost;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String toString(){
        return position.toString();
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public ArrayList<Connection> getConnections() {
        return outgoing;
    }

    public void setConnections(ArrayList<Connection> outgoing) {
        this.outgoing = outgoing;
    }
    
    public void addConnection(Connection c){
        this.outgoing.add(c);
    }

    public ArrayList<Connection> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(ArrayList<Connection> outgoing) {
        this.outgoing = outgoing;
    }

    public float getCostSoFar() {
        return costSoFar;
    }

    public void setCostSoFar(float costSoFar) {
        this.costSoFar = costSoFar;
    }

    public float getEstimatedTotalCost() {
        return estimatedTotalCost;
    }

    public void setEstimatedTotalCost(float estimatedTotalCost) {
        this.estimatedTotalCost = estimatedTotalCost;
    }

    public Connection getBestConnection() {
        return bestConnection;
    }

    public void setBestConnection(Connection connection) {
        this.bestConnection = connection;
    }
    
    public boolean isGoal() {
        return isGoal;
    }

    public void setGoal(boolean isGoal) {
        this.isGoal = isGoal;
    }

    public Vector2 getPosition() {
        return position;
    }

}
