public class State {

    int width;
    int height;

    public Ficha[][] cells;

    public State(int width, int height){
        this.width = width;
        this.height = height;

        this.cells = new Ficha[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) cells[i][j] = new Blank(i, j);
        }
    }

    public void put(Ficha ficha){
        int posX = ficha.getPosX(), posY = ficha.getPosY();
        cells[posX][posY] = ficha;
    }

//    @Override
//    public String toString() {
//        StringBuilder b = new StringBuilder();
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                b.append(cells[i][j] + " ");
//            }
//            b.append("\n");
//        }
//        return b.toString();
//    }
}
