/**
 * Represents a player in the game
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
        this.score = new Fraction(0,0);
    }

    public String getFigure() {
        return String.format("%c%c", name, name);
    }
}
