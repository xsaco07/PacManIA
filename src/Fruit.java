public class Fruit extends Node {

    public Fruit (int initialPosX, int initialPosY) {
        setPosX(initialPosX);
        setPosY(initialPosY);
    }

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.gCost, node.gCost);
    }

}
