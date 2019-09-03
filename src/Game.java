import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Game {

    private int pacManScore;
    private int ghostsSocre;
    private Grid gameGrid;
    private JFrame frame;
    public ArrayList<Pair<Integer, Integer>> finalPath;
    private PriorityQueue<Fruit> gridFruits;

    public Game(Grid gameGrid, JFrame frame) {
        this.gameGrid = gameGrid;
        this.frame = frame;
        gridFruits = new PriorityQueue<>();
        pacManScore = 0;
        ghostsSocre = 0;
    }

    public void findEntirePath() {

        setGridFruitsQueue();
        int gridFruitsSize = gridFruits.size();

        for (int i = 0; i < gridFruitsSize; i++) {

            //sleep(2);

            Fruit closestFruit = gridFruits.poll();
            AStar.setGrid(gameGrid);
            AStar.findPath(gameGrid.pacManNode, closestFruit);

            if (AStar.finalPath != null) {
                Node pacManNode = gameGrid.pacManNode;
                int pacManNodePosX = pacManNode.getPosX();
                int pacManNodePosY = pacManNode.getPosY();

                //gameGrid.cells[pacManNodePosX][pacManNodePosY] = new Blank(pacManNodePosX, pacManNodePosY);
                repaintGrid();
                pacManNode.setPosX(closestFruit.getPosX());
                pacManNode.setPosY(closestFruit.getPosY());
                gameGrid.drawCells();
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void repaintGrid() {
        for (int i = 0; i < AStar.finalPath.size()-1; i++) {
            int posX = AStar.finalPath.get(i).getValue();
            int posY = AStar.finalPath.get(i).getKey();
            gameGrid.cells[posY][posX] = new PathCell(posX, posY);
            frame.repaint();
            sleep();
        }
    }

    public boolean checkForWinner(Grid grid) {
        return grid.fruitsNodes.size() == 0;
    }

    private void setGridFruitsQueue() {

        // Initial Node positions to reach all the fruits
        int currentStartNodePosX = gameGrid.pacManNode.getPosX();
        int currentStartNodePosY = gameGrid.pacManNode.getPosY();

        for (int i = 0; i < gameGrid.fruitsNodes.size(); i++) {
            Node currentStartNode = gameGrid.cells[currentStartNodePosY][currentStartNodePosX];
            Fruit currentFruit = gameGrid.fruitsNodes.get(i);

            // Set comparable value to current fruit
            currentFruit.gCost = AStar.getDistance(currentStartNode, currentFruit);
            gridFruits.add(currentFruit);
        }
    }

    private void moveGhosts() {
        for (Ghost ghost : gameGrid.ghostsNodes) {
            do {
                ghost.move();
            }while (!AStar.isInBounds(ghost.getPosX(), ghost.getPosY()));
        }
    }

}
