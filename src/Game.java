import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;

class Game {

    private int pacManScore;
    private int ghostsScore;
    private int goalScore;
    private Grid gameGrid;
    private JFrame frame;
    private ArrayList<Fruit> gridFruits;
    private ArrayList<Pair<Integer, Integer>> completePath;
    private ArrayList<Pair<Integer, Integer>> currentPath;

    Game(Grid gameGrid, JFrame frame) {
        this.gameGrid = gameGrid;
        this.frame = frame;
        goalScore = gameGrid.fruitsNodes.size();
        completePath = new ArrayList<>();
        currentPath = new ArrayList<>();
        pacManScore = 0;
        ghostsScore = 0;
    }

    void play() {
        while (!winnerFound()) {
            findEntirePath();
            sleep();
            movePacMan();
            moveGhosts();
            cleanPath();
            gameGrid.repaintCells();
            frame.repaint();
        }
        System.out.println("Game over!");
    }

    private void movePacMan() {
        //System.out.println(completePath);
        int pathSize = completePath.size();
        if (pathSize > 0) {

            // Move PacMan to the next path cell
            int prevPosX = gameGrid.pacManNode.getPosX();
            int prevPosY = gameGrid.pacManNode.getPosY();

            Pair<Integer, Integer> nextPathCellPosition = completePath.get(0);
            int nextPosX = nextPathCellPosition.getKey();
            int nextPosY = nextPathCellPosition.getValue();

            //System.out.println(nextPosX + " " + nextPosY);
            //System.out.println("Node at " + nextPosX + " " + nextPosY + ": " + gameGrid.cells[nextPosX][nextPosY]);

            gameGrid.pacManNode.setPosX(nextPosX);
            gameGrid.pacManNode.setPosY(nextPosY);

            // Node where pacMan is at after move to nextPosition
            Node currentNode = gameGrid.cells[nextPosX][nextPosY];

            if (currentNode instanceof Fruit) {
                pacManScore++;
                gameGrid.fruitsNodes.remove(currentNode);
                System.out.println("PacMan just ate a fruit");
                System.out.println("PacMan score: " + pacManScore);
            }

            gameGrid.cells[prevPosX][prevPosY] = new Blank(prevPosX, prevPosY);

            //System.out.println(prevPosX + " " + prevPosY);

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

            currentPath = AStar.findPath(gameGrid, pacManCopy, closestFruit);

            //System.out.println(pacManCopy);
            //System.out.println(closestFruit);
            //System.out.println("PacMan" + pacManCopy.getPosX() + "," + pacManCopy.getPosY());
            //System.out.println("Fruit" + closestFruit.getPosX() + "," + closestFruit.getPosY());

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
            Thread.sleep(900);
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
            if (!(gameGrid.cells[posX][posY] instanceof Fruit))
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
        return pacManScore == goalScore || ghostsScore == goalScore;
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

            while (!ghost.moveOverGrid(gameGrid)) ghost.moveOverGrid(gameGrid);

            int currentPosX = ghost.getPosX(), currentPosY = ghost.getPosY();

            // Node were the ghost is now in after the valid movement
            Node currentGhostPosition = gameGrid.cells[currentPosX][currentPosY];

            if (currentGhostPosition instanceof Fruit) {
                ghostsScore++;
                gameGrid.fruitsNodes.remove(currentGhostPosition);
                System.out.println("Ghost just ate a fruit at " + previousPosX + ", " + previousPosY);
                System.out.println("Ghost score: " + ghostsScore);
            }

            gameGrid.cells[previousPosX][previousPosY] = new Blank(previousPosX, previousPosY);
        }
    }

}
