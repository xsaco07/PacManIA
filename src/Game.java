import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;

public class Game {

    private int pacManScore;
    private int ghostsScore;
    private int goalScore;
    private Grid gameGrid;
    private JFrame frame;
    private ArrayList<Fruit> gridFruits;
    private ArrayList<Pair<Integer, Integer>> completePath;
    private Pair<Integer, Integer> nextPathCell;

    public Game(Grid gameGrid, JFrame frame) {
        this.gameGrid = gameGrid;
        this.frame = frame;
        goalScore = gameGrid.fruitsNodes.size();
        completePath = new ArrayList<>();
        nextPathCell = new Pair<>(-1, -1);
        pacManScore = 0;
        ghostsScore = 0;
    }

    public void play() {
        while (!winnerFound()) {
            findEntirePath();
            sleep();
            cleanPath();
            movePacMan();
            moveGhosts();
            gameGrid.repaintCells();
        }
        System.out.println("Game over!");
    }

    private void movePacMan() {
        int pathSize = AStar.finalPath.size();
        if (pathSize > 0) {
            // Move PacMan to the next path cell
            int prevPosX = gameGrid.pacManNode.getPosX();
            int prevPosY = gameGrid.pacManNode.getPosY();
            int nextPosX = nextPathCell.getKey();
            int nextPosY = nextPathCell.getValue();
            gameGrid.pacManNode.setPosX(nextPosX);
            gameGrid.pacManNode.setPosY(nextPosY);

            // Node were PacMan is after the movement
            Node currentPacManPosition = gameGrid.cells[nextPosX][nextPosX];

            if (currentPacManPosition instanceof Fruit) {
                pacManScore++;
                gameGrid.fruitsNodes.remove(currentPacManPosition);
                System.out.println("PacMan just ate a fruit");
                System.out.println("PacMan score: " + pacManScore);
            }

            gameGrid.cells[prevPosX][prevPosY] = new Blank(prevPosX, prevPosY);

        }

    }

    public void findEntirePath() {

        boolean firstTime = true;

        // Restore the fruits to search them on each iteration
        gridFruits = new ArrayList<>(gameGrid.fruitsNodes);

        int gridFruitsSize = gridFruits.size();

        // Copy the original pac man to find all the path traversing all fruits
        Node pacManCopy = new PacMan((PacMan) gameGrid.pacManNode);

        for (int i = 0; i < gridFruitsSize; i++) {

            Fruit closestFruit = getClosestFruit(pacManCopy);
            AStar.setGrid(gameGrid);
            AStar.findPath(pacManCopy, closestFruit);

            if (AStar.finalPath != null) {
                paintFinalPath();
                pacManCopy.setPosX(closestFruit.getPosX());
                pacManCopy.setPosY(closestFruit.getPosY());
                if (firstTime) {
                    System.out.println(AStar.finalPath);
                    // Get the first step that PacMan has to take
                    nextPathCell = AStar.finalPath.get(0);
                    firstTime = false;
                }
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

    private void paintFinalPath() {
        for (int i = 0; i < AStar.finalPath.size()-1; i++) {
            // Save each cell of the partial path to clean them then
            completePath.add(AStar.finalPath.get(i));
            int posX = AStar.finalPath.get(i).getKey();
            int posY = AStar.finalPath.get(i).getValue();
            gameGrid.cells[posX][posY] = new PathCell(posX, posY);
            frame.repaint();
        }
    }

    private void cleanPath() {
        for (Pair<Integer, Integer> integerIntegerPair : completePath) {
            int posX = integerIntegerPair.getKey();
            int posY = integerIntegerPair.getValue();
            gameGrid.cells[posX][posY] = new Blank(posX, posY);
        }
        frame.repaint();
    }

    private boolean winnerFound() {
        return pacManScore == goalScore || ghostsScore == goalScore;
    }

    private Fruit getClosestFruit(Node startNode) {

        Fruit closestFruit = gridFruits.get(0);
        closestFruit.gCost = AStar.getDistance(startNode, closestFruit);

        for (int i = 1; i < gridFruits.size(); i++) {

            Fruit currentFruit = gridFruits.get(i);

            // Set comparable value to current fruit
            currentFruit.gCost = AStar.getDistance(startNode, currentFruit);

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
