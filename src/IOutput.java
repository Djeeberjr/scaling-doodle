
/**
 * Interface in front of interchangeable output classes to unify
 * different methods of displaying the game (console, GUI, network,...)
 */
public interface IOutput {
    public void draw(Game game) throws InterruptedException;
}
