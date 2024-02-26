package util;

public class Connection {
    private float cost;
    private Node fromNode;
    private Node toNode;

    public Connection(float cost, Node fromNode, Node toNode){
        this.cost = cost;
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public float getCost() {
        return this.cost;
    }

    public Node getFromNode() {
        return this.fromNode;
    }

    public Node getToNode() {
        return this.toNode;
    }
    
}
