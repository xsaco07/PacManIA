public class Main{

    public static void main(String[] args){
        int rows = 11, cols = 11;
        int cellSize = 60;
        Grid grid = new Grid(rows, cols);
        Game game = new Game(grid, cellSize);
        game.play();
    }

}
