import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;

public class Game {

    private int pacManScore;
    private int ghostsScore;
    private Grid gameGrid;
    private JFrame frame;
    private ArrayList<Fruit> gridFruits;
    private ArrayList<Pair<Integer, Integer>> completePath;

    public Game(Grid gameGrid, JFrame frame) {
        this.gameGrid = gameGrid;
        this.frame = frame;
        completePath = new ArrayList<>();
        pacManScore = 0;
        ghostsScore = 0;
    }

    public void play() {
        while (!winnerFound(gameGrid)) {
            sleep();
            findEntirePath();
            //cleanPath();
            //moveGhosts();
            //movePacMan();
            gameGrid.drawCells();
            frame.repaint();
        }
    }

    private void movePacMan() {
    }

    public void findEntirePath() {

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
        System.out.println(AStar.finalPath);
        for (int i = 0; i < AStar.finalPath.size()-1; i++) {
            // Save each cell of the partial path to clean them then
            completePath.add(AStar.finalPath.get(i));
            int posX = AStar.finalPath.get(i).getValue();
            int posY = AStar.finalPath.get(i).getKey();
            gameGrid.cells[posY][posX] = new PathCell(posX, posY);
            frame.repaint();
        }
        //System.out.println(completePath);
    }

    private void cleanPath() {
        for (Pair<Integer, Integer> integerIntegerPair : completePath) {
            int posX = integerIntegerPair.getValue();
            int posY = integerIntegerPair.getKey();
            gameGrid.cells[posY][posX] = new Blank(posX, posY);
            frame.repaint();
        }
    }

    private boolean winnerFound(Grid grid) {
        return pacManScore == gameGrid.fruitsNodes.size() || ghostsScore == gameGrid.fruitsNodes.size();
    }

//    private void setGridFruitsQueue() {
//
//        // Initial Node positions to reach all the fruits
//        int currentStartNodePosX = gameGrid.pacManNode.getPosX();
//        int currentStartNodePosY = gameGrid.pacManNode.getPosY();
//
//        for (int i = 0; i < gameGrid.fruitsNodes.size(); i++) {
//            Node currentStartNode = gameGrid.cells[currentStartNodePosY][currentStartNodePosX];
//            Fruit currentFruit = gameGrid.fruitsNodes.get(i);
//
//            // Set comparable value to current fruit
//            currentFruit.gCost = AStar.getDistance(currentStartNode, currentFruit);
//            gridFruits.add(currentFruit);
//        }
//    }

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
            int currentPosX = ghost.getPosX(), currentPosY = ghost.getPosY();
            while (!ghost.moveOverGrid(gameGrid)) ghost.moveOverGrid(gameGrid);
            if (gameGrid.cells[currentPosX][currentPosY] instanceof Fruit) {
                ghostsScore++;
            }
            gameGrid.cells[currentPosX][currentPosY] = new Blank(currentPosX, currentPosY);
        }
    }

}
