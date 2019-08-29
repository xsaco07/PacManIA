// This class is the parent class for all cells in the matrix of any State
public abstract class Ficha {

    private int posX;
    private int posY;

    int getPosX() {return posX;}
    int getPosY() {return posY;}
    void setPosX(int posX) {this.posX = posX;}
    void setPosY(int posY) {this.posY = posY;}

}
