import java.util.Random;

/**
 * This class contains all the game logic
 */
public class Game {
    public static final int NUM_PLAYERS = 2;
    public static final int PLAYER_BLACK = 0;
    public static final int PLAYER_WHITE = 1;
    public static final int WIN_SCORE = 80;

    private int width, height;
    private int turn;
    private Player[] players = new Player[NUM_PLAYERS];
    private Fraction[] field;
    private int winner = -1;
    private Event lastEvent;

    Game(int width, int height) {

        // generate field
        generateField(width, height);

        // place the players onto the field
        for (int i = 0; i < NUM_PLAYERS; i++) {
            char name = (char)(i == PLAYER_BLACK ? '▓' : i == PLAYER_WHITE ? '░' : 65+i-2);
            placePlayer(i, name);
        }
    }

    public void processInput(Move move){
        lastEvent = new Event(getCurrentPlayer(), move);

        if(move != Move.INVALID) {

            Player currentPlayer = getCurrentPlayer();
            int moveX = currentPlayer.x;
            int moveY = currentPlayer.y;

            switch (move){
                case DOWN:
                    moveY++;
                    break;
                case UP:
                    moveY--;
                    break;
                case LEFT:
                    moveX--;
                    break;
                case RIGHT:
                    moveX++;
            }

            //check if move is out of bounds
            if(moveX >= width || moveY >= height || moveX < 0 || moveY < 0){
                //move is out of bounds
                return;
            }

            //check if move is blocked by other player
            if(getPlayerAt(moveX,moveY) != null){
                //Move is blocked by other player
                return;
            }

            //move is valid!

            //add score to player
            if(!getFieldPos(moveX,moveY).isNaN()){
                if(currentPlayer.score.isNaN()){
                    currentPlayer.score = getFieldPos(moveX,moveY);
                }else{
                    currentPlayer.score = currentPlayer.score.add(getFieldPos(moveX,moveY));
                }
            }

            //set value to 0 at move location
            field[posToIndex(moveX,moveY)] = new Fraction(0,0);

            //move player
            currentPlayer.x = moveX;
            currentPlayer.y = moveY;

            checkWinner();

            turn = (turn + 1) % NUM_PLAYERS;
        }

    }

    private void generateField(int width, int height) {
        this.width = width;
        this.height = height;

        field = new Fraction[width*height];

        for (int i = 0; i < field.length; i++) {
            // generate a random numerator in range [1..99] (fractions shall not be zero)
            int numerator = Util.randInt(1, 99);

            // generate a denominator that assures the fraction to be less than or equals to 10
            int denomBoundMin = (int)Math.ceil( ((double)numerator)/10.0 );
            int denomBoundMax = Math.max(1, numerator-1); // the fraction must be greater than 1
            int denominator = Util.randInt(denomBoundMin, denomBoundMax);

            // make *really* sure that everything is sane (XXX should actually be unnecessary)
            if((double)numerator/(double)denominator <= 10.0)
                field[i] = new Fraction(numerator, denominator);
            else
                i--; // try again - XXX SHOULD NEVER HAPPEN!
        }
    }

    private void placePlayer(int id, char name) {
        Random rng = new Random(System.nanoTime());

        // place players around the middle, in the second third of the field
        final int xw = (int)Math.ceil((double)width/3.0);
        final int yh = (int)Math.ceil((double)height/3.0);

        while(true) {
            int xCoord = rng.nextInt(xw) + xw;
            int yCoord = rng.nextInt(yh) + yh;

            // make sure players don't get placed right in the middle (cuz forbidden by rulez)
            if(width % 2 == 0 && height % 2 == 0 &&
                    xCoord == width/2 && yCoord == height/2)
                continue;

            if(getPlayerAt(xCoord, yCoord) == null) {
                players[id] = new Player(xCoord, yCoord, name);
                field[posToIndex(xCoord, yCoord)] = new Fraction(0,0);
                break;
            }
        }
    }

    private void checkWinner(){

        if(!getCurrentPlayer().score.isNaN() && getCurrentPlayer().score.intValue() >= WIN_SCORE){
            winner = turn;
            return;
        }

        //TODO: check if all tiles in the field are empty or if winning is still possible
    }

    // getters and setters

    public int posToIndex(int x, int y) {
        return x + width * y;
    }

    public Fraction getFieldPos(int x, int y){
        return field[posToIndex(x, y)];
    }

    public Player getPlayerAt(int x, int y) {
        for(Player p : players) {
            if(p != null &&
                    p.x == x && p.y == y)
                return p;
        }
        return null;
    }

    public Player getCurrentPlayer() {
        return players[turn];
    }

    public Event getLastEvent() {
        return lastEvent;
    }

    public int getWinner() {
        return winner;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTurn() {
        return turn;
    }
}

/**
 * Represents a move conducted by a specific player
 */
class Event {
    public final Player player;
    public final Move move;

    public Event(Player player, Move move) {
        this.player = player;
        this.move = move;
    }

    @Override
    public String toString() {
        if(move == Move.INVALID) {
            return "!!! Player " + player.getFigure() + " gave an invalid input, try again! !!!";
        } else {
            String dir =
                    move == Move.UP ? "up" :
                    move == Move.LEFT ? "left" :
                    move == Move.DOWN ? "down" :
                    move == Move.RIGHT ? "right" : "weird";
            return "Player " + player.getFigure() + " moved " + dir;
        }
    }
}