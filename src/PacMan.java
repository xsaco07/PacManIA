public class PacMan extends Node {

    public PacMan (int initialPosX, int initialPosY) {
        setPosX(initialPosX);
        setPosY(initialPosY);
    }

    public PacMan(PacMan originalPacMan) {
        setPosX(originalPacMan.getPosX());
        setPosY(originalPacMan.getPosY());
        this.gCost = originalPacMan.gCost;
        this.hCost = originalPacMan.hCost;
    }
}
