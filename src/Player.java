/**
 * Represents a player in the game
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.1 03.01.18
 */
public class Player {
    public int x;
    public int y;
    public Fraction score;
    public final char name; // name doesn't change

    public Player(int x, int y, char name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.score = Fraction.ZERO;
    }

    public String getFigure() {
        return String.format("%c%c", name, name);
    }
}
