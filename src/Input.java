import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Waits for interactive input from stdin and passes it to the game
 */
public class Input {

    private static Scanner sc = new Scanner(System.in);


    public static char getInput() throws NoSuchElementException {
        String s;
        while(true) {
            System.out.println("[w|k]: Move up      [c]: Quit Game ");
            System.out.println("[a|h]: Move left                   ");
            System.out.println("[s|j]: Move down                   ");
            System.out.println("[d|l]: Move right                  ");
            System.out.print("> ");
            s = sc.nextLine();

            if(s.length() >= 1){
                return s.charAt(0);
            }
        }
    }

    public static Move getMove(char input){
        switch (input){
            case 'w':
            case 'k':
            return Move.UP;
            case 'a':
            case 'h':
                return Move.LEFT;
            case 's':
            case 'j':
                return Move.DOWN;
            case 'd':
            case 'l':
                return Move.RIGHT;
            default:
                return Move.INVALID;
        }
    }
}
