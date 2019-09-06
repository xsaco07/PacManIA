import javafx.util.Pair;

import java.util.*;

public class AStar {

    private static Grid gameGrid;
    public static ArrayList<Pair<Integer, Integer>> finalPath;

    private static ArrayList<Node> generateChildren(Node currentNode) {

        // List of valid children generated from pacMan
        ArrayList<Node> children = new ArrayList<>();

        int currentNodePosX = currentNode.getPosX();
        int currentNodePosY = currentNode.getPosY();

        // Can moveOverGrid ? Add that one
        if (isInBounds(currentNodePosX+1, currentNodePosY) && isNotBlocked(currentNodePosX + 1, currentNodePosY)) {
            children.add(gameGrid.cells[currentNodePosX+1][currentNodePosY]);
        }
        // Can moveOverGrid ? Add that one
        if (isInBounds(currentNodePosX, currentNodePosY+1) && isNotBlocked(currentNodePosX, currentNodePosY + 1)) {
            children.add(gameGrid.cells[currentNodePosX][currentNodePosY+1]);
        }
        // Can moveOverGrid ? Add that one
        if (isInBounds(currentNodePosX-1, currentNodePosY) && isNotBlocked(currentNodePosX - 1, currentNodePosY)) {
            children.add(gameGrid.cells[currentNodePosX-1][currentNodePosY]);
        }
        // Can moveOverGrid ? Add that one
        if (isInBounds(currentNodePosX, currentNodePosY-1) && isNotBlocked(currentNodePosX, currentNodePosY - 1)) {
            children.add(gameGrid.cells[currentNodePosX][currentNodePosY-1]);
        }

        return children;
    }

    private static boolean isNotBlocked(int posX, int posY) {
        return (!(gameGrid.cells[posX][posY] instanceof Block) && !(gameGrid.cells[posX][posY] instanceof Ghost));
    }

    public static boolean isInBounds(int posX, int posY) {
        return (posX < (gameGrid.width) && posX >= 0)
                &&
                (posY < (gameGrid.height) && posY >= 0);
    }

    // Manhattan distance
    static int getDistance(Node origin, Node destiny) {
        return ((Math.abs(origin.getPosX() - destiny.getPosX())) +
                (Math.abs(origin.getPosY()- destiny.getPosY())));
    }

    public static void setGrid(Grid grid) {gameGrid = grid;}

    public static void findPath(Node startNode, Node goalNode) {

        PriorityQueue<Node> openNodes = new PriorityQueue<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();

        openNodes.add(startNode);

        while(openNodes.size() > 0) {

            Node current = openNodes.poll(); // Get the node with the smallest F cost
            visitedNodes.add(current);

            if(reachGoal(current, goalNode)) {
//                System.out.println("Path Found!");
                finalPath = buildPath(startNode, goalNode);
                return;
            }

            for(Node neighbor : generateChildren(current)) { // Generates only valid states

                if(!visitedNodes.contains(neighbor)) {

                    int tentativeNeighborGCost = current.gCost + getDistance(current, neighbor);

                    if (tentativeNeighborGCost < neighbor.gCost || !openNodes.contains(neighbor)) {
                        neighbor.gCost = tentativeNeighborGCost;
                        neighbor.hCost = getDistance(neighbor, goalNode);
                        neighbor.parent = current;
                        if (!openNodes.contains(neighbor)) {
                            openNodes.add(neighbor);
                        }
                    }
                }
            }
        }
        System.out.println("Solution Not Found!");
    }

    private static boolean reachGoal(Node node, Node goalNode) {
        return getDistance(node, goalNode) == 0;
    }

    private static ArrayList<Pair<Integer, Integer>> buildPath(Node startNode, Node endNode) {

        Node iterator = endNode;
        ArrayList<Pair<Integer, Integer>> pathPositions = new ArrayList<>();
        Pair<Integer, Integer> currentPositions;

        while (iterator != startNode) {
            currentPositions = new Pair<>(iterator.getPosX(), iterator.getPosY());
            pathPositions.add(currentPositions);
            iterator = iterator.parent;
        }

        Collections.reverse(pathPositions);
        return pathPositions;
    }

}
