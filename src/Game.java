public class Game {
    private final int sizeX,sizeY;
    private int turn;
    private Player[] players;
    private Fraction[] field;
    private int winner = -1;

    Game(){

    }

    public void processInput(Move move){

    }

    //Getter & Setter
    public Fraction getFieldPos(int x, int y){
        return field[x + sizeX * y];
    }

    public int getWinner() {
        return winner;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getTurn() {
        return turn;
    }
}
