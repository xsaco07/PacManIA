import java.util.ArrayList;
import java.util.Random;

class Grid {

    // Any grid must have exactly 5 fruits and ghosts
    private final int FRUITS_COUNT = 5;
    private final int GHOSTS_COUNT = 5;

    int width;
    int height;

    Node pacManNode;

    ArrayList<Fruit> fruitsNodes;
    ArrayList<Ghost> ghostsNodes;
    private ArrayList<Block> blocksNodes;

    Node[][] cells;


    /**
     * Initializes attrs, sets all cells to blanks, puts the Pacman randomly, spawns fuits and ghosts
     * @param width
     * @param height
     */
    Grid(int width, int height){

        // Initialize attributes
        this.width = width;
        this.height = height;

        this.fruitsNodes = new ArrayList<>();
        this.ghostsNodes = new ArrayList<>();
        this.blocksNodes = new ArrayList<>();


        // Initialize all cells to Blanks
        this.cells = new Node[height][width];
        System.out.println("Creating a grid of "+ height + " x " + width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) cells[i][j] = new Blank(i, j);
        }

        // Spawn the Pacman in a random position
        int[] position = findFreePosition();
        this.pacManNode = new PacMan(position[0], position[1]);
        put(pacManNode);


        // Spawn ghosts
        int[] freePosition;
        for (int i = 0; i < GHOSTS_COUNT; i++) {
            freePosition = findFreePosition();
            put(new Ghost(freePosition[0], freePosition[1]));
        }

        // Spawn fruits
        int[] pos;
        for (int i = 0; i < FRUITS_COUNT; i++) {
            pos = findFreePosition();
            put(new Fruit(pos[0], pos[1]));
        }
    }



    /**
     * Find a free space in the grid randomly
     * @return A random [x,y] free position
     */
    private int[] findFreePosition() {
        int[] result = new int[2];
        boolean found = false;

        int x, y;
        Random rand = new Random();
        while(! found){
            x = rand.nextInt(width);
            y = rand.nextInt(height);

            if(cells[y][x] instanceof Blank){
                result[0] = x;
                result[1] = y;
                found = true;
            }
        }
        return result;
    }


    /**
     * Puts the received node in -its- coordinates at the cells
     * If the node is fruit, ghost or block adds it to the corresponding list
     * @param node
     */
    private void put(Node node){
        int posX = node.getPosX(), posY = node.getPosY();
        cells[posX][posY] = node;

        if (node instanceof Fruit) fruitsNodes.add((Fruit) node);
        else if (node instanceof Ghost) ghostsNodes.add((Ghost) node);
        else if (node instanceof Block) blocksNodes.add((Block) node);
    }


    /**
     * This is called to make the cells match the data on the lists
     * Because
     */
    public void repaintCells() {
        // Paint pacMan
        cells[pacManNode.getPosX()][pacManNode.getPosY()] = pacManNode;
        // Paint ghosts
        for (Ghost ghost : ghostsNodes) {
            int currentPosX = ghost.getPosX(), currentPosY = ghost.getPosY();
            cells[currentPosX][currentPosY] = ghost;
        }
    }


}
