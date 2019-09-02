public class Ghost extends Node {

    public Ghost (int initialPosX, int initialPosY) {
        setPosX(initialPosX);
        setPosY(initialPosY);
    }

    // The ghosts can move randomly in any direction
    // The Game class checks whether the move is valid or not
    public void move () {
        // Get random value between 1 and 4 inclusive
        int valorEntero = (int) Math.floor(Math.random()*(1-4+1)+4);
        switch (valorEntero) {
            case 1: up(); break;
            case 2: down(); break;
            case 3: left(); break;
            case 4: right(); break;
        }
    }

    private void up() {
        setPosX(getPosX() - 1);
    }

    private void down() {
        setPosX(getPosX() + 1);
    }

    private void left() {
        setPosY(getPosY() - 1);
    }

    private void right() {
        setPosY(getPosY() + 1);
    }

}
