import java.io.Serializable;
import java.util.Random;

/**
 * This class contains all the main game logic
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.7 09.01.18
 */
public class Game  implements Serializable{
    public static final int NUM_PLAYERS = 2;
    public static final int PLAYER_BLACK = 0;
    public static final int PLAYER_WHITE = 1;
    public static final int WIN_SCORE = 80;

    private int width, height;
    private int turn;
    private Player[] players;
    private Fraction[] field;
    private Fraction fieldSum;
    private int winner = 0;
    private Event lastEvent;

    Game(int width, int height) {

        winner = -1;
        do {
            // generate field
            generateField(width, height);

            // place the players onto the field
            players = new Player[NUM_PLAYERS];
            for (int i = 0; i < NUM_PLAYERS; i++) {
                char name = (char) (i == PLAYER_BLACK ? '▓' : i == PLAYER_WHITE ? '░' : 65 + i - 2);
                placePlayer(i, name);
            }

            // make sure there is enough value on the field
        } while(checkWinner() && winner < 0);
        winner = 0;
    }

    public void processInput(int dx, int dy) {

        Player currentPlayer = getCurrentPlayer();
        int moveX = currentPlayer.x+ dx;
        int moveY = currentPlayer.y+dy;

        //check if move is out of bounds
        if(moveX >= width || moveY >= height || moveX < 0 || moveY < 0){
            //move is out of bounds
            lastEvent = new Event(getCurrentPlayer(), Move.INVALID);
            return;
        }

        //check if move is blocked by other player
        if(getPlayerAt(moveX,moveY) != null){
            //Move is blocked by other player
            lastEvent = new Event(getCurrentPlayer(), Move.INVALID);
            return;
        }

        //move is valid!

        //add score to player
        if(getFieldPos(moveX,moveY) != null && !getFieldPos(moveX,moveY).isNaN()){
            if(currentPlayer.score.isNaN()){
                currentPlayer.score = getFieldPos(moveX,moveY);
            }else{
                currentPlayer.score = currentPlayer.score.add(getFieldPos(moveX,moveY));
            }
        }

        // remove the fraction from the field
        field[posToIndex(moveX,moveY)] = null;

        // move player
        currentPlayer.x = moveX;
        currentPlayer.y = moveY;

        if(checkWinner()) {
            // game has ended
        } else {
            turn = (turn + 1) % NUM_PLAYERS;
        }
    }

    public void processInput(Move move) {
        lastEvent = new Event(getCurrentPlayer(), move);

        if(move != Move.INVALID) {

            int dx = 0;
            int dy = 0;

            switch (move){
                case DOWN:
                    dy++;
                    break;
                case UP:
                    dy--;
                    break;
                case LEFT:
                    dx--;
                    break;
                case RIGHT:
                    dx++;
            }

            processInput(dx, dy);

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
            double fracTotal = (double)numerator/(double)denominator;
            if(fracTotal <= 10.0 && fracTotal >= 1.0)
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
                field[posToIndex(xCoord, yCoord)] = null;
                break;
            }
        }
    }

    /**
     * Checks whether there's a winner or draw
     * @return a boolean value indicating if the game has ended
     */
    private boolean checkWinner() {

        if(!getCurrentPlayer().score.isNaN() && getCurrentPlayer().score.longValue() >= WIN_SCORE) {
            winner = turn+1;
            return true;
        }

        // check if all tiles in the field are empty or if winning is still possible

        // count how much value is left on the field
        fieldSum = null;
        for (Fraction frac : field) {
            if (frac != null) {
                if(fieldSum == null) {
                    fieldSum = frac;
                } else {
                    fieldSum = fieldSum.add(frac);
                }
                //System.out.println("+ " + frac + " -> " + frac.doubleValue());
                //System.out.println("= " + fieldSum + " -> " + fieldSum.doubleValue());
            }
        }

        // field is empty?
        if(fieldSum == null || fieldSum.isNaN()) {
            winner = 0;
            return true;
        }

        // find out what the largest score is
        Fraction largestScore = players[0].score;
        for (int i = 1; i < players.length; i++) {
            if(players[i].score.compareTo(largestScore) > 0) {
                largestScore = players[i].score;
            }
        }

        // if the player with most score can't win by eating everything, no more winning is possible
        if(largestScore.add(fieldSum).doubleValue() < WIN_SCORE) {
            System.out.println("fieldSum = " + fieldSum + " -> " + fieldSum.doubleValue());
            winner = 0;
            return true;
        }

        return false;
    }


    // helpers

    public int posToIndex(int x, int y) {
        return x + width * y;
    }

    public int indexToX(int i) {
        return i % width;
    }

    public int indexToY(int i) {
        return i / width;
    }

    // getters and setters

    public Fraction getFieldPos(int x, int y) {
        return field[posToIndex(x, y)];
    }

    public Fraction getFieldIndex(int i) {
        return field[i];
    }

    public Player getPlayerAt(int x, int y) {
        for(Player p : players) {
            if(p != null &&
                    p.x == x && p.y == y)
                return p;
        }
        return null;
    }

    public Player getPlayerOn(int index) {
        return getPlayerAt(indexToX(index), indexToY(index));
    }

    public int getNumPlayers() {
        return players.length;
    }

    public Player getCurrentPlayer() {
        return players[turn];
    }

    public Player getPlayer(int i) {
        return players[i];
    }

    public Event getLastEvent() {
        return lastEvent;
    }

    /**
     * @return 0: draw, >0: player id that won, <0: game running
     */
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

    public Fraction getFieldSum() {
        return fieldSum;
    }
}

/**
 * Represents an action taken by a specific player
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.1 21.12.17
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