import java.util.Random;

public class Game {
    public static final int NUM_PLAYERS = 2;
    public static final int PLAYER_BLACK = 0;
    public static final int PLAYER_WHITE = 1;

    private  int sizeX,sizeY;
    private int turn;
    private Player[] players = new Player[NUM_PLAYERS];
    private Fraction[] field;
    private int winner = -1;

    Game(int width, int height) {
        this.sizeX = width;
        this.sizeY = height;

        // generate field
        Random rng = new Random(System.currentTimeMillis());
        field = new Fraction[width*height];
        for (int i = 0; i < field.length; i++) {
            int numerator = rng.nextInt(100);
            int denominator = rng.nextInt(100-1)+1; // TODO: make sure the fraction's value is within range [1..10]
            field[i] = new Fraction(numerator, denominator);
        }

        // place the players onto the field
        for (int i = 0; i < NUM_PLAYERS; i++) {
            // TODO: place the players close to the center instead of random!
            int posX = rng.nextInt(width);
            int posY = rng.nextInt(height);
            char name = (char)(i == PLAYER_BLACK ? '▓' : i == PLAYER_WHITE ? '░' : 65+i-2);
            players[i] = new Player(posX, posY, name);
            field[posToIndex(posX, posY)] = null;
        }
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
