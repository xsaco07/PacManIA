import javafx.util.Pair;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Game implements Listener {

    private int pacManScore;
    private int ghostsScore;
    private Grid gameGrid;
    private ArrayList<Fruit> gridFruits;
    private ArrayList<Pair<Integer, Integer>> completePath;
    private ArrayList<Pair<Integer, Integer>> currentPath;

    private volatile boolean diagonalsAllowed;
    private volatile boolean onPause;

    // This is put in a class attributes because couldn't reach it through the JFrame
    private MyPanel myPanel;
    private JFrame frame;
    private int cellSize;

    Game(Grid gameGrid, int cellSize) {

        this.gameGrid = gameGrid;

        completePath = new ArrayList<>();
        currentPath = new ArrayList<>();
        pacManScore = 0;
        ghostsScore = 0;

        diagonalsAllowed = true;
        onPause = false;

        // Register this class to be able to listen words from user
        VoiceHelper.getInstance().register(this);

        initializeGUI(this.gameGrid, cellSize);
    }

    // Initialize JFrame and MyPanel
    private void initializeGUI(Grid grid, int cellSize) {

        this.cellSize = cellSize;

        frame = new JFrame("PacManIA");

        frame.setBackground(Color.BLACK);

        myPanel = new MyPanel(grid, cellSize);
        myPanel.setBackground(new Color(0, 0, 47));

        frame.add(myPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        resetUI(grid, cellSize); // call resetUI here to avoid duplicating code

        frame.setVisible(true);

    }

    // Adjusts the window to show the new grid and cellSize
    // Re-sizes the frame to match the new dimensions
    // Resets the MyPanel's grid and cellSize attributes
    private void resetUI(Grid grid, int cellSize){
        // Update de JPanel's attributes
        myPanel.setGrid(grid);
        myPanel.setCellSize(cellSize);

        // Don't know why it's necessary to add this to the frame's height to be displayed properly
        int EXTRA_HEIGHT = 30;

        // Resize the frame
        frame.setBounds(200,100, grid.width*cellSize,grid.height*cellSize+EXTRA_HEIGHT);
    }

    void play() {
        while (!winnerFound()) {
            findEntirePath();
            pause(); // As it is inside a do-while always sleep for 1 second
            movePacMan();
            moveGhosts();
            cleanPath();
            gameGrid.repaintCells();
            frame.repaint();
        }
        System.out.println("Game over!");
        System.out.println("PacMan score: " + pacManScore);
        System.out.println("Ghost score: " + ghostsScore);
        if (pacManScore > ghostsScore) VoiceHelper.getInstance().say("PacMan has won with" + pacManScore + "points");
        else if (pacManScore < ghostsScore) VoiceHelper.getInstance().say("Ghosts have won with " + ghostsScore + "points");
        else VoiceHelper.getInstance().say("It's a tie");
    }

    private void movePacMan() {
        int pathSize = completePath.size();
        if (pathSize > 0) {

            // Move PacMan to the next path cell
            int prevPosX = gameGrid.pacManNode.getPosX();
            int prevPosY = gameGrid.pacManNode.getPosY();

            Pair<Integer, Integer> nextPathCellPosition = completePath.get(0);
            int nextPosX = nextPathCellPosition.getKey();
            int nextPosY = nextPathCellPosition.getValue();

            gameGrid.pacManNode.setPosX(nextPosX);
            gameGrid.pacManNode.setPosY(nextPosY);

            // Node where pacMan is at after move to nextPosition
            Node currentNode = gameGrid.cells[nextPosX][nextPosY];

            if (currentNode instanceof Fruit) {
                pacManScore++;
                gameGrid.fruitsNodes.remove(currentNode);
            }

            gameGrid.cells[prevPosX][prevPosY] = new Blank(prevPosX, prevPosY);

            gameGrid.repaintCells();

        }
        else System.out.println("Complete path empty");

    }

    private void findEntirePath() {

        // Restore the fruits to search them on each iteration
        gridFruits = new ArrayList<>(gameGrid.fruitsNodes);

        // Restore completePath so it does not accumulate
        completePath.clear();

        int numFruits = gridFruits.size();

        // Copy the original pacMan to find the completePath through the fruits without move at all
        Node pacManCopy = new PacMan((PacMan) gameGrid.pacManNode);

        for (int i = 0; i < numFruits; i++) { // Perform A* many times as fruits exist in the grid

            Fruit closestFruit = getClosestFruit(pacManCopy);

            currentPath = AStar.findPath(gameGrid, pacManCopy, closestFruit, diagonalsAllowed);

            if (currentPath != null) {
                paintFinalPath(currentPath);
                // Update pacMan position to find next path to closest fruit
                pacManCopy.setPosX(closestFruit.getPosX());
                pacManCopy.setPosY(closestFruit.getPosY());
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void paintFinalPath(ArrayList<Pair<Integer, Integer>> currentPath) {
        for (Pair<Integer, Integer> pair : currentPath) {

            // Save each cell of the path to clean them in the grid later
            completePath.add(pair);

            int posX = pair.getKey();
            int posY = pair.getValue();
            // Avoid painting the fruit cell
            if (!(gameGrid.cells[posX][posY] instanceof Fruit) && !(gameGrid.cells[posX][posY] instanceof PacMan))
                gameGrid.cells[posX][posY] = new PathCell(posX, posY);
        }
    }

    private void cleanPath() {
        for (Pair<Integer, Integer> pair : completePath) {
            int posX = pair.getKey();
            int posY = pair.getValue();
            // Avoid cleaning the fruit cell
            if (!(gameGrid.cells[posX][posY] instanceof Fruit))
                gameGrid.cells[posX][posY] = new Blank(posX, posY);
        }
    }

    private boolean winnerFound() {
        return gameGrid.fruitsNodes.size() == 0;
    }

    private Fruit getClosestFruit(Node startNode) {

        Fruit closestFruit = gridFruits.get(0);
        closestFruit.gCost = AStar.getManhattanDistance(startNode, closestFruit);

        for (int i = 1; i < gridFruits.size(); i++) {

            Fruit currentFruit = gridFruits.get(i);

            // Set comparable value to current fruit
            currentFruit.gCost = AStar.getManhattanDistance(startNode, currentFruit);

            if (currentFruit.gCost < closestFruit.gCost) {
                closestFruit = currentFruit;
            }
        }
        gridFruits.remove(closestFruit);
        return closestFruit;
    }

    private void moveGhosts() {

        for (Ghost ghost : gameGrid.ghostsNodes) {
            int previousPosX = ghost.getPosX(), previousPosY = ghost.getPosY();

            // While the movement is invalid try an other one
            while (!ghost.moveOverGrid(gameGrid)) ghost.moveOverGrid(gameGrid);

            int currentPosX = ghost.getPosX(), currentPosY = ghost.getPosY();

            // Node were the ghost is after the valid movement
            Node currentGhostPosition = gameGrid.cells[currentPosX][currentPosY];

            if (currentGhostPosition instanceof Fruit) {
                ghostsScore++;
                gameGrid.fruitsNodes.remove(currentGhostPosition);
            }

            gameGrid.cells[previousPosX][previousPosY] = new Blank(previousPosX, previousPosY);
        }
    }

    private void pause() {
        do sleep(); while (onPause);
    }

    @Override
    public void onRecognitionResult(String result) {
        switch (result) {

            case "on" : diagonalsAllowed = true; break;

            case "off" : diagonalsAllowed = false; break;

            case "stop" : onPause = true; break;

            case "resume" : onPause = false; break;

            case "restart" : {

                onPause = false;
                Grid newGrid = new Grid(gameGrid.width, gameGrid.height);
                this.gameGrid = newGrid;

                // Restart the scores
                pacManScore = 0;
                ghostsScore = 0;

                resetUI(newGrid, cellSize);
                break;
            }

            default: System.out.println(result);
        }
    }
}
