public class Ghost extends Node {

    public Ghost (int initialPosX, int initialPosY) {
        setPosX(initialPosX);
        setPosY(initialPosY);
    }

    // The ghosts can moveOverGrid randomly in any direction but a valid one
    public boolean moveOverGrid(Grid gameGrid) {
        // Get random value between 1 and 4 inclusive
        int valorEntero = (int) Math.floor(Math.random()*(4-1+1)+1);
        switch (valorEntero) {
            case 1: return up(gameGrid);
            case 2: return down(gameGrid);
            case 3: return left(gameGrid);
            case 4: return right(gameGrid);
            default: return false;
        }
    }

    private boolean up(Grid gameGrid) {
//        System.out.println("Up");
        int newPosX = this.getPosX()-1;
        if (isInBounds(gameGrid, newPosX, this.getPosY()) && isNotBlocked(gameGrid, newPosX, this.getPosY())) {
            this.setPosX(newPosX);
            return true;
        }
        return false;
    }

    private boolean down(Grid gameGrid) {
//        System.out.println("down");
        int newPosX = this.getPosX()+1;
        if (isInBounds(gameGrid, newPosX, this.getPosY()) && isNotBlocked(gameGrid, newPosX, this.getPosY())) {
            this.setPosX(newPosX);
            return true;
        }
        return false;
    }

    private boolean left(Grid gameGrid) {
//        System.out.println("left");
        int newPosY = this.getPosY()-1;
        if (isInBounds(gameGrid, this.getPosX(), newPosY) && isNotBlocked(gameGrid, this.getPosX(), newPosY)) {
            this.setPosY(newPosY);
            return true;
        }
        return false;
    }

    private boolean right(Grid gameGrid) {
//        System.out.println("right");
        int newPosY = this.getPosY()+1;
        if (isInBounds(gameGrid, this.getPosX(), newPosY) && isNotBlocked(gameGrid, this.getPosX(), newPosY)) {
            this.setPosY(newPosY);
            return true;
        }
        return false;
    }

    private boolean isNotBlocked(Grid gameGrid, int posX, int posY) {
        return (!(gameGrid.cells[posX][posY] instanceof Block));
    }

    private  boolean isInBounds(Grid gameGrid, int posX, int posY) {
        return (posX < (gameGrid.width) && posX >= 0)
                &&
                (posY < (gameGrid.height) && posY >= 0);
    }

}
