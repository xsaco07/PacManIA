import java.util.ArrayList;

public class Grid {

    int width;
    int height;
    Node pacManNode;
    ArrayList<Fruit> fruitsNodes;
    ArrayList<Ghost> ghostsNodes;
    ArrayList<Block> blocksNodes;

    public Node[][] cells;

    public Grid(int width, int height, PacMan pacMan){
        this.width = width;
        this.height = height;

        this.cells = new Node[height][width];
        this.fruitsNodes = new ArrayList<>();
        this.ghostsNodes = new ArrayList<>();
        this.blocksNodes = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) cells[i][j] = new Blank(i, j);
        }

        this.pacManNode = pacMan;
        this.put(pacMan);
    }

    public void put(Node node){
        int posX = node.getPosX(), posY = node.getPosY();
        cells[posX][posY] = node;
        if (node instanceof Fruit) fruitsNodes.add((Fruit) node);
        else if (node instanceof Ghost) ghostsNodes.add((Ghost) node);
        else if (node instanceof Block) blocksNodes.add((Block) node);
    }

    public void drawCells() {
        // Paint pacMan
        cells[pacManNode.getPosX()][pacManNode.getPosY()] = pacManNode;
        // Paint ghosts
        for (Ghost ghost : ghostsNodes) {
            int currentPosX = ghost.getPosX(), currentPosY = ghost.getPosY();
            cells[currentPosX][currentPosY] = ghost;
        }
    }

}
