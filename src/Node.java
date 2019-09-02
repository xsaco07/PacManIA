// This class is the parent class for all cells in the matrix of any Grid
public class Node implements Comparable<Node> {

    private int posX;
    private int posY;
    public int gCost = Integer.MAX_VALUE;
    public int hCost = 0;
    public Node parent;

    int getFcost() {
        return gCost + hCost;
    }

    int getPosX() {return posX;}

    int getPosY() {return posY;}

    void setPosX(int posX) {this.posX = posX;}

    void setPosY(int posY) {this.posY = posY;}

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.getFcost(), node.getFcost());
    }
}
