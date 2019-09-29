import javafx.util.Pair;
import java.util.*;

class AStar {

    private static Grid gameGrid;
    private static final int DIAGONAL_COST = 14;
    private static final int FORWARD_DISTANCE = 10;
    private static boolean diagonalsAllowed = false;

    private static ArrayList<Node> generateChildren(Node currentNode) {

        // List of valid children generated from pacMan
        ArrayList<Node> children = new ArrayList<>();

        int currentNodePosX = currentNode.getPosX();
        int currentNodePosY = currentNode.getPosY();

        // Up, Down, Left, Right

        if (isInBounds(currentNodePosX+1, currentNodePosY) && isNotBlocked(currentNodePosX + 1, currentNodePosY))
            children.add(gameGrid.cells[currentNodePosX+1][currentNodePosY]);

        if (isInBounds(currentNodePosX, currentNodePosY+1) && isNotBlocked(currentNodePosX, currentNodePosY + 1))
            children.add(gameGrid.cells[currentNodePosX][currentNodePosY+1]);

        if (isInBounds(currentNodePosX-1, currentNodePosY) && isNotBlocked(currentNodePosX - 1, currentNodePosY))
            children.add(gameGrid.cells[currentNodePosX-1][currentNodePosY]);

        if (isInBounds(currentNodePosX, currentNodePosY-1) && isNotBlocked(currentNodePosX, currentNodePosY - 1))
            children.add(gameGrid.cells[currentNodePosX][currentNodePosY-1]);

        // Diagonals

        if (diagonalsAllowed) {

            if (isInBounds(currentNodePosX + 1, currentNodePosY + 1) && isNotBlocked(currentNodePosX + 1, currentNodePosY + 1))
                children.add(gameGrid.cells[currentNodePosX+1][currentNodePosY+1]);

            if (isInBounds(currentNodePosX + 1, currentNodePosY - 1) && isNotBlocked(currentNodePosX + 1, currentNodePosY - 1))
                children.add(gameGrid.cells[currentNodePosX+1][currentNodePosY-1]);

            if (isInBounds(currentNodePosX - 1, currentNodePosY + 1) && isNotBlocked(currentNodePosX - 1, currentNodePosY + 1))
                children.add(gameGrid.cells[currentNodePosX-1][currentNodePosY+1]);

            if (isInBounds(currentNodePosX - 1, currentNodePosY - 1) && isNotBlocked(currentNodePosX - 1, currentNodePosY - 1))
                children.add(gameGrid.cells[currentNodePosX-1][currentNodePosY-1]);
        }

        return children;
    }

    private static boolean isNotBlocked(int posX, int posY) {
        return (!(gameGrid.cells[posX][posY] instanceof Block) && !(gameGrid.cells[posX][posY] instanceof Ghost));
    }

    private static boolean isInBounds(int posX, int posY) {
        return (posX < (gameGrid.width) && posX >= 0)
                &&
                (posY < (gameGrid.height) && posY >= 0);
    }

    // Formula: 14*y + 10*(x-y) where x = horizontal distance and y = vertical distance
    static int getManhattanDistance(Node origin, Node destiny) {
        int distX = Math.abs(origin.getPosX() - destiny.getPosX());
        int distY = Math.abs(origin.getPosY() - destiny.getPosY());
        if (distX > distY)
            return (distY * DIAGONAL_COST) + (FORWARD_DISTANCE * (distX - distY));
        return (distX * DIAGONAL_COST) + (FORWARD_DISTANCE * (distY - distX));
    }

    static ArrayList<Pair<Integer, Integer>> findPath(Grid grid, Node startNode, Node goalNode, boolean diagonalsAllowedFlag) {

        gameGrid = grid; // Set grid
        diagonalsAllowed = diagonalsAllowedFlag;

        PriorityQueue<Node> openNodes = new PriorityQueue<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();

        openNodes.add(startNode);

        while(openNodes.size() > 0) {

            Node current = openNodes.poll(); // Get the node with the smallest F cost
            visitedNodes.add(current);

            if(reachGoal(current, goalNode)) {
                return buildPath(startNode, goalNode);
            }

            for(Node neighbor : generateChildren(current)) { // Generates only valid states

                if(!visitedNodes.contains(neighbor)) {

                    int tentativeNeighborGCost = current.gCost + getManhattanDistance(current, neighbor);

                    if (tentativeNeighborGCost < neighbor.gCost || !openNodes.contains(neighbor)) {
                        neighbor.gCost = tentativeNeighborGCost;
                        neighbor.hCost = getManhattanDistance(neighbor, goalNode);
                        neighbor.parent = current;
                        if (!openNodes.contains(neighbor)) {
                            openNodes.add(neighbor);
                        }
                    }
                }
            }
        }
        System.out.println("Solution Not Found!");
        return null;
    }

    private static boolean reachGoal(Node node, Node goalNode) {
        return getManhattanDistance(node, goalNode) == 0;
    }

    private static ArrayList<Pair<Integer, Integer>> buildPath(Node startNode, Node endNode) {

        Node iterator = endNode;
        ArrayList<Pair<Integer, Integer>> pathPositions = new ArrayList<>();
        Pair<Integer, Integer> currentPositions;

        while (iterator != startNode && iterator != null) {
            currentPositions = new Pair<>(iterator.getPosX(), iterator.getPosY());
            pathPositions.add(currentPositions);
            iterator = iterator.parent;
        }

        Collections.reverse(pathPositions);
        return pathPositions;
    }
}
