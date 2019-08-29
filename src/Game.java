public class Game {

    private int pacManScore = 0;
    private int ghostsSocre = 0;
    private AStar aStar;

    public void startGame() {

    }

    public Ficha checkForWinner() {
        return new PacMan(0, 0);
    }



}
