public class Game {
    private  int sizeX,sizeY;
    private int turn;
    private Player[] players;
    private Fraction[] field;
    private int winner = -1;

    Game(){

    }

    public void processInput(Move move){

    }

    //Getter & Setter
    public int posToIndex(int x, int y) {
        return x + sizeX * y;
    }

    public Fraction getFieldPos(int x, int y){
        return field[posToIndex(x, y)];
    }

    public Player getPlayerAt(int x, int y) {
        for(Player p : players) {
            if(p.x == x && p.y == y)
                return p;
        }
        return null;
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
