import java.util.ArrayList;

public class Grid {

    int width;
    int height;
    Node pacManNode;
    ArrayList<Fruit> fruitsNodes;

    public Node[][] cells;

    public Grid(int width, int height, PacMan pacMan){
        this.width = width;
        this.height = height;

        this.cells = new Node[height][width];
        this.fruitsNodes = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) cells[i][j] = new Blank(i, j);
        }

        this.pacManNode = pacMan;
        this.put(pacMan);
    }

    public void put(Node node){
        int posX = node.getPosX(), posY = node.getPosY();
        cells[posX][posY] = node;
        if (node instanceof Fruit) {
            fruitsNodes.add((Fruit) node);
        }
    }

}
