
public class Main{

    public static void main(String[] args){
        //int[] data = UserInput.getInstance().askDimensions();
        //int rows = data[0], cols = data[1], cellSize = data[2];
        int rows = 8, cols = 8, cellSize = 60;
        Grid grid = new Grid(rows, cols);
        Game game = new Game(grid, cellSize);
        game.play();
    }

}
