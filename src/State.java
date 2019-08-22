public class State {

    int width;
    int height;

    public char cells[][];

    public State(int width, int height){
        this.width = width;
        this.height = height;

        this.cells = new char[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = Symbols.EMPTY;
            }
        }
    }

    public void put(int x, int y, char symbol){
        cells[x][y] = symbol;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                b.append(cells[i][j] + " ");
            }
            b.append("\n");
        }
        return b.toString();
    }
}
