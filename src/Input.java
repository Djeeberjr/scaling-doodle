import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Waits for interactive input from stdin and passes it to the game
 *
 * @author Niklas Kapelle
 * @author Henri Bußmann
 * @version 1.3 08.01.18
 */
public class Input {

    private static Scanner sc = new Scanner(System.in);


    public static char getInput(){
        String s;

        System.out.println("[w|k]: Move up      [c]: Quit Game ");
        System.out.println("[a|h]: Move left                   ");
        System.out.println("[s|j]: Move down                   ");
        System.out.println("[d|l]: Move right                  ");

        while(true) {

            System.out.print("> ");

            try{
                s = sc.nextLine();
            }catch (NoSuchElementException  e){
                return 'c';
            }

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
